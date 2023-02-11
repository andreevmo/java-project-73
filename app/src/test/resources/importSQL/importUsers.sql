DELETE FROM task_label;
DELETE FROM tasks;
DELETE FROM users;

INSERT INTO users(id, email, first_name, last_name, password, created_at) VALUES (1, 'ivan@google.com', 'Ivan', 'Petrov', '$2a$12$UwVWm1/LsDYYMFI3yePXNe1gU3tmpT.4R1nMW2qMj7gmyuvmM9lWu', '2022-01-01 00:00:01');
INSERT INTO users(id, email, first_name, last_name, password, created_at) VALUES (2, 'petr@google.com', 'Petr', 'Ivanov', '$2a$12$UwVWm1/LsDYYMFI3yePXNe1gU3tmpT.4R1nMW2qMj7gmyuvmM9lWu', '2022-01-01 00:00:01');

