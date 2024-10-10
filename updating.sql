ALTER TABLE students ADD COLUMN phone_number VARCHAR(15);
ALTER TABLE students DROP COLUMN phone_name;
ALTER TABLE professors MODIFY salary DOUBLE NOT NULL DEFAULT 1200;
ALTER TABLE departments ADD COLUMN head_name VARCHAR(100);
ALTER TABLE classrooms ADD COLUMN university_id BIGINT;
ALTER TABLE courses ADD COLUMN subject_name VARCHAR(255);
ALTER TABLE courses DROP COLUMN subject_name;
ALTER TABLE scholarships MODIFY scholarship_amount DOUBLE DEFAULT 500;
ALTER TABLE classrooms ADD COLUMN floor INT;
ALTER TABLE classrooms DROP COLUMN floor;

INSERT INTO universities (name, location, founder_year) VALUES ('Harvard University', 'Cambridge, MA', 1636);
INSERT INTO universities (name, location, founder_year) VALUES ('Stanford University', 'Stanford, CA', 1885);
INSERT INTO universities (name, location, founded_year) VALUES ('MIT', 'Cambridge, MA', 1861);
INSERT INTO universities (name, location, founded_year) VALUES ('University of California, Berkeley', 'Berkeley, CA', 1868);
INSERT INTO universities (name, location, founded_year) VALUES ('University of Oxford', 'Oxford, UK', 1096);

INSERT INTO classrooms (building, room_number, capacity) VALUES ('Main Building', '101', 30);
INSERT INTO classrooms (building, room_number, capacity) VALUES ('Science Block', '202A', 50);
INSERT INTO classrooms (building, room_number, capacity) VALUES ('Arts Center', '103', 40);

INSERT INTO students (first_name, last_name, email, enrollment_date, university_id) VALUES ('John', 'Doe', 'john.doe@example.com', NOW(), 1);
INSERT INTO professors (first_name, last_name, email, salary, department_id, university_id) VALUES ('Mike', 'Smith', 'mike.smith@example.com', 1300, 1, 1);

INSERT INTO students (first_name, last_name, email, enrollment_date, university_id) VALUES 
('Ivan', 'Ivanov', 'ivan.ivanov@example.com', NOW(), 2),
('Macej', 'Maj', 'macej.maj@example.com', NOW(), 4),
('Daniel', 'Wilson', 'daniel.wilson@example.com', NOW(), 5),
('Laura', 'Johnson', 'laura.johnson@example.com', NOW(), 1),
('Chris', 'Lee', 'chris.lee@example.com', NOW(), 2),
('Natalie', 'White', 'sarah.white@example.com', NOW(), 3);

UPDATE students SET email = 'john.newemail@example.com' WHERE id = 1;
UPDATE students SET university_id = 3 WHERE id = 2;
UPDATE professors SET salary = 1500 WHERE id = 1;
UPDATE universities SET location = 'Cambridge, Massachusetts' WHERE id = 1;
UPDATE courses SET credits = 4 WHERE id = 1;
UPDATE classrooms SET capacity = 35 WHERE id = 1;
UPDATE students SET last_name = 'Johnson' WHERE id = 3;
UPDATE departments SET name = 'Computer Engineering' WHERE id = 1;
UPDATE professors SET department_id = 2 WHERE id = 2;
UPDATE courses SET name = 'Advanced Machine Learning' WHERE id = 2;
