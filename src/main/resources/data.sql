-- Insert a predefined user with an initial score of 0
INSERT INTO users (email, password, role, score)
VALUES ('user@gmail.com', '$2a$12$8LegtLQWe717tIPvZeivjuqKnaAs5.bm0Q05.5GrAmcKzXw2NjoUO', 'USER', 0);
-- Profiles
INSERT INTO profiles (first_name, last_name, country, user_id) VALUES
('Vero', 'Doel', 'España', 1);

-- Insert data into tasks table
INSERT INTO tasks (title, description, is_done, user_id) VALUES 
('Task 1', 'Description for task 1', 0, 1),  
('Task 2', 'Description for task 2', 0, 1),
('Task 3', 'Description for task 3', 1, 1);

-- Insert data into subtasks table
INSERT INTO subtasks (title, description, is_done, task_id) VALUES
('Subtask 1.1', 'Description for subtask 1.1', 0, 1),
('Subtask 1.2', 'Description for subtask 1.2', 1, 1),
('Subtask 2.1', 'Description for subtask 2.1', 0, 2),
('Subtask 3.1', 'Description for subtask 3.1', 0, 3);

-- Insert data into minitasks table
INSERT INTO minitasks (title, description, is_done, subtask_id) VALUES
('MiniTask 1.1.1', 'Description for mini task 1.1.1', 0, 1),
('MiniTask 1.1.2', 'Description for mini task 1.1.2', 1, 1),
('MiniTask 1.2.1', 'Description for mini task 1.2.1', 0, 2),
('MiniTask 2.1.1', 'Description for mini task 2.1.1', 0, 3);

-- Sample score increment for task completion
UPDATE users SET score = score + 250 WHERE user_id = 1 AND (SELECT COUNT(*) FROM tasks WHERE user_id = 1 AND is_done = 1) > 0;

-- Updating scores based on existing completed data
UPDATE users SET score = score + 25 WHERE user_id = 1 AND (
    SELECT COUNT(*) FROM subtasks WHERE task_id = 1 AND is_done = 1) > 0;

UPDATE users SET score = score + 5 WHERE user_id = 1 AND (
    SELECT COUNT(*) FROM minitasks WHERE subtask_id = 1 AND is_done = 1) > 0;

-- Insert data into notes table (for the user's notes feature)INSERT INTO notes (title, content, user_id, created_at) 
INSERT INTO notes (title, content, user_id, created_at) 
VALUES 
('Note 1', 'This is the content of the first note.', 1, NOW()),  
('Note 2', 'This is the content of the second note.', 1, NOW());

-- Insert predefined events for specific dates
INSERT INTO events (name, date, user_id) VALUES 
('Meeting with team', '2024-10-05', 1),  
('Project deadline', '2024-10-10', 1),
('Doctor appointment', '2024-10-15', 1),
('Family gathering', '2024-10-20', 1),
('Birthday party', '2024-10-25', 1);

