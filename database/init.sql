CREATE TABLE books (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    available BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO books (title, author, isbn, available)
VALUES
    ('The Hobbit', 'J.R.R. Tolkien', '9780261102217', TRUE),
    ('1984', 'George Orwell', '9780451524935', TRUE),
    ('To Kill a Mockingbird', 'Harper Lee', '9780061120084', TRUE);