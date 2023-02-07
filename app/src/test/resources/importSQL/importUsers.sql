DELETE FROM tasks;
DELETE FROM users;

INSERT INTO users(id, email, first_name, last_name, password, created_at) VALUES (1, 'ivan@google.com', 'Ivan', 'Petrov', 'some-password', '2022-01-01 00:00:01');
INSERT INTO users(id, email, first_name, last_name, password, created_at) VALUES (2, 'petr@google.com', 'Petr', 'Ivanov', 'some-password', '2022-01-01 00:00:01');

