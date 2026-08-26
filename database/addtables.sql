CREATE TABLE members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE loans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    borrowed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    returned_at TIMESTAMP NULL,

    CONSTRAINT fk_loan_member
        FOREIGN KEY (member_id) REFERENCES members(id),

    CONSTRAINT fk_loan_book
        FOREIGN KEY (book_id) REFERENCES books(id)
);

INSERT INTO members (name, email)
VALUES
    ('John Smith', 'john@example.com'),
    ('Jane Doe', 'jane@example.com');