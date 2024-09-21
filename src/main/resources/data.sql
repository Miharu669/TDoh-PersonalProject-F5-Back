-- Create users table if it doesn't exist
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create tasks table
CREATE TABLE IF NOT EXISTS tasks (
    task_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(300),
    is_done BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create subtasks table
CREATE TABLE IF NOT EXISTS subtasks (
    subtask_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    is_done BOOLEAN DEFAULT FALSE,
    task_id BIGINT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id) ON DELETE CASCADE
);

-- Insert a predefined user
INSERT INTO users (username, email, password)
VALUES ('user', 'testuser@example.com', '$2a$12$8LegtLQWe717tIPvZeivjuqKnaAs5.bm0Q05.5GrAmcKzXw2NjoUO');

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
('Subtask 2.2', 'Description for subtask 2.2', 0, 2),
('Subtask 3.1', 'Description for subtask 3.1', 1, 3);

-- DROP TABLE IF EXISTS subtasks;
-- DROP TABLE IF EXISTS tasks;
-- DROP TABLE IF EXISTS users;
