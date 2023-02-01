DELETE FROM user_roles;
DELETE FROM roles;
DELETE FROM users;
DELETE FROM statuses;

INSERT INTO users(id, email, first_name, last_name, password, created_at) VALUES (1, 'ivan@google.com', 'Ivan', 'Petrov', 'some-password', '2022-01-01 00:00:01');
INSERT INTO users(id, email, first_name, last_name, password, created_at) VALUES (2, 'petr@google.com', 'Petr', 'Ivanov', 'some-password', '2022-01-01 00:00:01');

INSERT INTO statuses(id, name, created_at) VALUES (1, 'Новый', '2022-01-01 00:00:01');
INSERT INTO statuses(id, name, created_at) VALUES (2, 'В работе', '2022-01-01 00:00:01');
INSERT INTO statuses(id, name, created_at) VALUES (3, 'На тестировании', '2022-01-01 00:00:01');
INSERT INTO statuses(id, name, created_at) VALUES (4, 'Завершен', '2022-01-01 00:00:01');

INSERT INTO roles(name) VALUES('ADMIN');
INSERT INTO roles(name) VALUES('USER');
INSERT INTO roles(name) VALUES('MODERATOR');
