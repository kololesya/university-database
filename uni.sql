CREATE SCHEMA IF NOT EXISTS `university`;
USE `university`;

CREATE TABLE IF NOT EXISTS `university`.`universities` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `location` VARCHAR(255) NOT NULL,
  `founded_year` YEAR NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name`));

CREATE TABLE IF NOT EXISTS `university`.`classrooms` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `building` VARCHAR(45) NOT NULL,
  `room_number` VARCHAR(45) NOT NULL,
  `capacity` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `university`.`students` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `enrollment_date` TIMESTAMP NOT NULL,
  `university_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `university_id` (`university_id`),
  CONSTRAINT `students_ibfk_1`
    FOREIGN KEY (`university_id`)
    REFERENCES `university`.`universities` (`id`));

CREATE TABLE IF NOT EXISTS `university`.`professors` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `salary` DOUBLE NULL DEFAULT 1000,
  `department_id` BIGINT NOT NULL,
  `university_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `university_id` (`university_id`),
  CONSTRAINT `professors_ibfk_1`
    FOREIGN KEY (`university_id`)
    REFERENCES `university`.`universities` (`id`));
    
CREATE TABLE IF NOT EXISTS `university`.`courses` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `credits` TINYINT NOT NULL,
  `professor_id` BIGINT NOT NULL,
  `university_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name`),
  INDEX `university_id` (`university_id`),
  INDEX `professor_id` (`professor_id`),
  CONSTRAINT `courses_ibfk_1`
    FOREIGN KEY (`university_id`)
    REFERENCES `university`.`universities` (`id`),
  CONSTRAINT `courses_ibfk_2`
    FOREIGN KEY (`professor_id`)
    REFERENCES `university`.`professors` (`id`));

CREATE TABLE IF NOT EXISTS `university`.`departments` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `university_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `university_id` (`university_id`),
  CONSTRAINT `departments_ibfk_1`
    FOREIGN KEY (`university_id`)
    REFERENCES `university`.`universities` (`id`));
    
 CREATE TABLE IF NOT EXISTS `university`.`schedule` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `course_id` BIGINT NOT NULL,
  `classroom_id` BIGINT NOT NULL,
  `start_time` TIMESTAMP NOT NULL,
  `end_time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `course_id` (`course_id` ASC) VISIBLE,
  INDEX `classroom_id` (`classroom_id` ASC) VISIBLE,
  CONSTRAINT `schedule_ibfk_1`
    FOREIGN KEY (`course_id`)
    REFERENCES `university`.`courses` (`id`),
  CONSTRAINT `schedule_ibfk_2`
    FOREIGN KEY (`classroom_id`)
    REFERENCES `university`.`classrooms` (`id`));
    
CREATE TABLE IF NOT EXISTS `university`.`enrollments`(
`id` BIGINT NOT NULL AUTO_INCREMENT,
`student_id` BIGINT NOT NULL,
`course_id` BIGINT NOT NULL,
`enrollment_date` TIMESTAMP NOT NULL,
PRIMARY KEY (`id`),
INDEX `student_id`(`student_id`),
INDEX `course_id` (`course_id`),
  CONSTRAINT `enrollments_ibfk_1`
    FOREIGN KEY (`student_id`)
    REFERENCES `university`.`students` (`id`),
  CONSTRAINT `enrollments_ibfk_2`
    FOREIGN KEY (`course_id`)
    REFERENCES `university`.`courses` (`id`));
    
CREATE TABLE IF NOT EXISTS `university`.`scholarships` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `student_id` BIGINT NOT NULL,
  `scholarship_amount` DOUBLE NOT NULL,
  `award_date` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `scholarships_ibfk_1` (`student_id`) ,
  CONSTRAINT `scholarships_ibfk_1`
    FOREIGN KEY (`student_id`)
    REFERENCES `university`.`students` (`id`));
    
CREATE TABLE IF NOT EXISTS `university`.`grades` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `student_id` BIGINT NOT NULL,
  `course_id` BIGINT NOT NULL,
  `grade` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_student` (`student_id`),
  INDEX `fk_course` (`course_id`) ,
  CONSTRAINT `fk_course`
    FOREIGN KEY (`course_id`)
    REFERENCES `university`.`courses` (`id`),
  CONSTRAINT `fk_student`
    FOREIGN KEY (`student_id`)
    REFERENCES `university`.`students` (`id`));    

CREATE TABLE IF NOT EXISTS `university`.`clubs` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `president` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name`),
  INDEX `clubs_ibfk_1` (`president`),
  CONSTRAINT `clubs_ibfk_1`
    FOREIGN KEY (`president`)
    REFERENCES `university`.`students` (`id`));

CREATE TABLE IF NOT EXISTS `university`.`club_memberships` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `student_id` BIGINT NULL DEFAULT NULL,
  `club_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `student_id` (`student_id`),
  INDEX `club_id` (`club_id`),
  CONSTRAINT `club_memberships_ibfk_1`
    FOREIGN KEY (`student_id`)
    REFERENCES `university`.`students` (`id`),
  CONSTRAINT `club_memberships_ibfk_2`
    FOREIGN KEY (`club_id`)
    REFERENCES `university`.`clubs` (`id`));