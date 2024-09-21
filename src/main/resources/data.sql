

-- Insert a predefined user
INSERT INTO users (username, email, password)
VALUES ('user', 'testuser@example.com', '$2a$12$8LegtLQWe717tIPvZeivjuqKnaAs5.bm0Q05.5GrAmcKzXw2NjoUO');

-- Insert data into tasks table
INSERT INTO tasks (title, description, is_done, user_id) VALUES 
('Task 1', 'Description for task 1', 0, 1),  
('Task 2', 'Description for task 2', 0, 1),
('Task 3', 'Description for task 3', 1, 1);

-- Insert data into subtasks table
INSERT INTO subtasks (title, description, is_done, task_id, user_id) VALUES
('Subtask 1.1', 'Description for subtask 1.1', 0, 1, 1),
('Subtask 1.2', 'Description for subtask 1.2', 1, 1, 1);

-- Insert data into minitasks table
INSERT INTO minitasks (title, description, is_done, subtask_id, task_id, user_id) VALUES
('MiniTask 1.1.1', 'Description for mini task 1.1.1', 0, 1, 1, 1),
('MiniTask 1.1.2', 'Description for mini task 1.1.2', 1, 1, 1, 1);

-- DROP TABLE IF EXISTS subtasks;
-- DROP TABLE IF EXISTS tasks;
-- DROP TABLE IF EXISTS users;
