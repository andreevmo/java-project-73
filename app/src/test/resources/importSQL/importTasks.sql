DELETE FROM tasks;

INSERT INTO tasks(id, author_id, executor_id, task_status_id, name, description, created_at) VALUES(1, 1, 1, 2, 'Задача #1', 'Описание задачи #1', '2022-01-01 00:00:01');
INSERT INTO tasks(id, author_id, task_status_id, name, description, created_at) VALUES(2, 1, 1, 'Задача #2', 'Описание задачи #2', '2022-01-01 00:00:01');