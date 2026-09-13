# Library Borrowing System

A Jakarta EE REST API and web application for a library borrowing system. 
Existing users can log in, browse books, view their current loans, and return books.


## 1. Software and Tools 

- Java JDK 25
- Apache Maven 3.9.16
- Payara Server Community 7
- MySQL 8.4
- Jakarta EE 11
- Jakarta REST 
- MicroProfile OpenAPI
- JDBC
- MySQL Connector/J 9.7.0
- HTML, CSS and JavaScript


## 2. Installation / Setup

### Build the project

Run: 
    mvn clean package

Compiled WAR file found at:
    target/Individual_Project.war

### Configure Payara

Payara JDBC connection pool to connect to the MySQL database hosted on AWS.

1. Create JDBC connection pool:
    Connection Pool: LibraryPool
    Datasource Class: com.mysql.cj.jdbc.MysqlDataSource
    Maximum Pool Size: 5

2. Configure the pool as displayed in canvas resources and using the database details provided by Lecturer 
(for privacy reasons, details not added here).

3. Create the JDBC resource:
    JNDI Name: jdbc/LibraryDB
    Pool: LibraryPool


## 3. Starting the System

1. Start Payara Server Community 7.
2. Open the Payara Admin Console:   
    http://localhost:4848
3. Make sure connection pool (LibraryPool) can connect to the AWS database.
4. Deploy to payara server:
    target/Individual_Project.war
5. Application found at:
    http://localhost:8080/Individual_Project/
The login page should appear.

### Application URLs

| Web application | `http://localhost:8080/Individual_Project/` |
| REST API | `http://localhost:8080/Individual_Project/api/v1` |
| OpenAPI | `http://localhost:8080/openapi` |
| Payara Admin | `http://localhost:4848` |


## 4. Test Accounts

The database contains the following dummy library member accounts:

| Name | Email | Password |
| John Smith | `john@example.com` | `qwer1234` |
| Jane Doe | `jane@example.com` | `qwer1234` |
| Michael Brown | `michael@example.com` | `qwer1234` |
| Sarah Wilson | `sarah@example.com` | `qwer1234` |
| David Taylor | `david@example.com` | `qwer1234` |


## 5. Testing the Main Functions

After logging in, test the main workflow:

### Login

Enter a valid test account.

**Expected:** The dashboard opens successfully.

### Browse Books

The dashboard displays the books retrieved from the REST API.

**Expected:** Available books are displayed and can be searched by title or author.

### Borrow a Book

Select an available book and choose **Borrow**.

**Expected:**
- A loan is created.
- The book becomes unavailable.
- The current loan appears in the dashboard.

### View Current Loans

Check the current loans section.

**Expected:** The member's active loan is displayed.

### Return a Book

Choose **Return** on an active loan.

**Expected:**
- The loan is marked as returned.
- The book becomes available again.
- The loan disappears from the current-loans list.

### Borrow the Returned Book Again

Borrow the same book after returning it.

**Expected:** The book can be borrowed again and a new loan is created.


## 6. Main REST API Endpoints

| Method | Endpoint | Function |

| `POST` | `/api/v1/auth/login` | Member login |
| `GET` | `/api/v1/books` | Get books |
| `GET` | `/api/v1/books/{id}` | Get a specific book |
| `GET` | `/api/v1/loans` | Get loans |
| `POST` | `/api/v1/loans` | Borrow a book |
| `GET` | `/api/v1/loans/member/{memberId}` | Get a member's active loans |
| `PUT` | `/api/v1/loans/{id}/return` | Return a book |
| `GET` | `/api/v1/members` | Get members |

The REST API communicates using HTTP and JSON.


## 7. Database Requirements

The application requires the lecturer-provided MySQL database containing:
    members
    books
    loans

The Payara application connects to this database through:
    jdbc/LibraryDB

Database connection values are configured through the Payara JDBC connection pool.

### Required Ports

- 8080 : Payara application
- 4848 : Payara Admin Console
- 3306 : AWS Database port

