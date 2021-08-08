CREATE TABLE student (
student_id varchar(100) NOT NULL PRIMARY KEY,
first_name varchar(45),
last_name varchar(45),
student_class varchar(15),
student_section varchar(15),
);

INSERT INTO student (student_id, first_name, last_name, student_class, student_section)
VALUES ('a0ec2a02-af74-4da1-a44b-d65b70a7bb82', 'test', 'someone','playgroup', 'lollipops');
