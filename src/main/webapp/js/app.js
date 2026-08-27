const API_BASE = "/Individual_Project/api/v1";


// ==========================================
// Authentication
// ==========================================

function getToken() {
    return sessionStorage.getItem("libraryToken");
}

function getUser() {
    const user = sessionStorage.getItem("libraryUser");

    return user ? JSON.parse(user) : null;
}

function saveLogin(data) {

    sessionStorage.setItem(
        "libraryToken",
        data.token
    );

    sessionStorage.setItem(
        "libraryUser",
        JSON.stringify({
            memberId: data.memberId,
            name: data.name,
            email: data.email
        })
    );
}

function logout() {

    sessionStorage.removeItem("libraryToken");
    sessionStorage.removeItem("libraryUser");

    window.location.href = "index.html";
}


// ==========================================
// Login
// ==========================================

const loginForm =
    document.getElementById("loginForm");

if (loginForm) {

    // If already logged in, go to dashboard
    if (getToken()) {
        window.location.href = "dashboard.html";
    }

    loginForm.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();

            const email =
                document.getElementById("email").value;

            const password =
                document.getElementById("password").value;

            const error =
                document.getElementById("loginError");

            const button =
                document.getElementById("loginButton");

            error.textContent = "";

            button.disabled = true;
            button.textContent = "Signing in...";

            try {

                const response = await fetch(
                    `${API_BASE}/auth/login`,
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({
                            email: email,
                            password: password
                        })
                    }
                );

                if (!response.ok) {

                    const message =
                        await response.text();

                    throw new Error(
                        message || "Invalid email or password"
                    );
                }

                const data =
                    await response.json();

                saveLogin(data);

                window.location.href =
                    "dashboard.html";

            } catch (err) {

                error.textContent =
                    err.message ||
                    "Unable to sign in.";

            } finally {

                button.disabled = false;
                button.textContent = "Sign in";
            }
        }
    );
}


// ==========================================
// Dashboard authentication
// ==========================================

const booksGrid =
    document.getElementById("booksGrid");

if (booksGrid) {

    if (!getToken()) {
        window.location.href = "index.html";
    } else {
        initialiseDashboard();
    }
}


// ==========================================
// Dashboard
// ==========================================

async function initialiseDashboard() {

    displayUser();

    await loadBooks();

    await loadLoans();
}


// ==========================================
// Display user
// ==========================================

function displayUser() {

    const user = getUser();

    if (!user) {
        return;
    }

    const name =
        document.getElementById("userName");

    const email =
        document.getElementById("userEmail");

    const avatar =
        document.getElementById("userAvatar");

    if (name) {
        name.textContent = user.name;
    }

    if (email) {
        email.textContent = user.email;
    }

    if (avatar) {

        avatar.textContent =
            user.name
                ? user.name.charAt(0).toUpperCase()
                : "U";
    }
}


// ==========================================
// API helper
// ==========================================

async function apiRequest(
    url,
    options = {}
) {

    const token = getToken();

    const headers = {
        ...(options.headers || {})
    };

    if (token) {

        headers.Authorization =
            `Bearer ${token}`;
    }

    const response = await fetch(
        url,
        {
            ...options,
            headers
        }
    );

    if (response.status === 401) {

        logout();

        return null;
    }

    return response;
}


// ==========================================
// Load books
// ==========================================

async function loadBooks() {

    const grid =
        document.getElementById("booksGrid");

    const count =
        document.getElementById("bookCount");

    try {

        const response =
            await apiRequest(
                `${API_BASE}/books`
            );

        if (!response || !response.ok) {

            throw new Error(
                "Could not load books."
            );
        }

        const books =
            await response.json();

        count.textContent =
            `${books.length} books`;

        grid.innerHTML = "";

        if (books.length === 0) {

            grid.innerHTML =
                `<div class="empty-state">
                    No books are currently available.
                 </div>`;

            return;
        }

        books.forEach(book => {

            grid.appendChild(
                createBookCard(book)
            );
        });

    } catch (error) {

        grid.innerHTML =
            `<div class="empty-state">
                Unable to load books.
             </div>`;

        console.error(error);
    }
}


// ==========================================
// Create book card
// ==========================================

function createBookCard(book) {

    const card =
        document.createElement("article");

    card.className = "book-card";

    const available =
        book.available === true;

    card.innerHTML = `
        <div class="book-cover">
            📕
        </div>

        <div class="book-title">
            ${escapeHtml(book.title)}
        </div>

        <div class="book-author">
            ${escapeHtml(book.author || "Unknown author")}
        </div>

        <div class="book-footer">

            <span class="status ${
                available
                    ? "available"
                    : "borrowed"
            }">
                ${
                    available
                        ? "AVAILABLE"
                        : "BORROWED"
                }
            </span>

            ${
                available
                    ? `<button
                            class="book-action"
                            onclick="borrowBook(${book.id})"
                       >
                            Borrow
                       </button>`
                    : ""
            }

        </div>
    `;

    return card;
}


// ==========================================
// Borrow book
// ==========================================

async function borrowBook(bookId) {

    try {

        const response =
            await apiRequest(
                `${API_BASE}/loans`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body: JSON.stringify({
                        bookId: bookId
                    })
                }
            );

        if (!response) {
            return;
        }

        if (!response.ok) {

            const message =
                await response.text();

            throw new Error(message);
        }

        showNotification(
            "Book borrowed successfully."
        );

        await loadBooks();

        await loadLoans();

    } catch (error) {

        showNotification(
            error.message ||
            "Could not borrow book."
        );
    }
}


// ==========================================
// Load user's loans
// ==========================================

async function loadLoans() {

    const container =
        document.getElementById(
            "loansContainer"
        );

    try {

        // Get the authenticated user's loans
        const loanResponse =
            await apiRequest(
                `${API_BASE}/loans`
            );

        if (!loanResponse || !loanResponse.ok) {

            throw new Error(
                "Could not load loans."
            );
        }

        const loans =
            await loanResponse.json();


        // Get all books so that we can match
        // loan.bookId to the actual book.
        const bookResponse =
            await apiRequest(
                `${API_BASE}/books`
            );

        if (!bookResponse || !bookResponse.ok) {

            throw new Error(
                "Could not load books."
            );
        }

        const books =
            await bookResponse.json();


        // Create a lookup table:
        //
        // book ID -> book object
        //
        // Example:
        // bookMap[7] = {
        //     id: 7,
        //     title: "Crime and Punishment",
        //     author: "Fyodor Dostoevsky"
        // }

        const bookMap = {};

        books.forEach(book => {

            bookMap[book.id] = book;
        });


        container.innerHTML = "";


        if (loans.length === 0) {

            container.innerHTML =
                `<div class="empty-state">
                    You currently have no active loans.
                 </div>`;

            return;
        }


        // Create a card for every loan

        loans.forEach(loan => {

            const book =
                bookMap[loan.bookId];

            container.appendChild(
                createLoanCard(
                    loan,
                    book
                )
            );
        });

    } catch (error) {

        container.innerHTML =
            `<div class="empty-state">
                Unable to load your loans.
             </div>`;

        console.error(error);
    }
}


// ==========================================
// Create loan card
// ==========================================

function createLoanCard(
    loan,
    book
) {

    const card =
        document.createElement("div");

    card.className = "loan-card";


    // Use the actual book information
    // when available.

    const title =
        book
            ? book.title
            : `Book #${loan.bookId}`;

    const author =
        book && book.author
            ? book.author
            : "Unknown author";


    card.innerHTML = `
        <div class="loan-info">

            <div class="loan-icon">
                📖
            </div>

            <div>

                <div class="loan-title">
                    ${escapeHtml(title)}
                </div>

                <div class="loan-details">
                    ${escapeHtml(author)}
                </div>

            </div>

        </div>

        <button
            class="return-button"
            onclick="returnBook(${loan.id})"
        >
            Return book
        </button>
    `;

    return card;
}




// ==========================================
// Return book
// ==========================================

async function returnBook(loanId) {

    try {

        const response =
            await apiRequest(
                `${API_BASE}/loans/${loanId}/return`,
                {
                    method: "PUT"
                }
            );

        if (!response) {
            return;
        }

        if (!response.ok) {

            const message =
                await response.text();

            throw new Error(message);
        }

        showNotification(
            "Book returned successfully."
        );

        await loadBooks();

        await loadLoans();

    } catch (error) {

        showNotification(
            error.message ||
            "Could not return book."
        );
    }
}


// ==========================================
// Notification
// ==========================================

function showNotification(message) {

    const notification =
        document.getElementById(
            "notification"
        );

    if (!notification) {
        return;
    }

    notification.textContent =
        message;

    notification.classList.add("show");

    setTimeout(() => {

        notification.classList.remove(
            "show"
        );

    }, 3000);
}


// ==========================================
// HTML escaping
// ==========================================

function escapeHtml(value) {

    const div =
        document.createElement("div");

    div.textContent =
        value ?? "";

    return div.innerHTML;
}


// ==========================================
// Logout
// ==========================================

const logoutButton =
    document.getElementById(
        "logoutButton"
    );

if (logoutButton) {

    logoutButton.addEventListener(
        "click",
        logout
    );
}

