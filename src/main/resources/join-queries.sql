-- Select student names and course id for students that take a course
SELECT first_name, last_name, course_id FROM students
    INNER JOIN student_course ON id = student_id;

-- Select all students names and course taken, even if student has no course
SELECT first_name, last_name, course_id FROM students
    LEFT JOIN student_course ON id = student_id;

-- Select all courses with attending students
-- Basically same as SELECT FROM student_course but we also get data from student and course tables by joining them
SELECT name, first_name, last_name FROM students s
    INNER JOIN student_course ON s.id = student_id
    INNER JOIN courses c ON course_id = c.id;

-- Select all students that don't have a course
SELECT first_name, last_name FROM students
    LEFT JOIN student_course ON id = student_id
    WHERE course_id IS NULL;

-- TIP IF joined column have same name we can simplify query by using USING instead of ON
SELECT * FROM table_1 INNER JOIN table_2 USING (column_name);

-- Select all courses taken by a specific student
SELECT s.id AS student_id, s.first_name, s.last_name, s.email, s.gender, c.id AS course_id, c.name, c.description, c.department, c.teacher_name, start_date, end_date, grade
    FROM students s
    LEFT JOIN student_course ON s.id = student_id
    LEFT JOIN courses c ON course_id = c.id
    WHERE s.id = 'c638b3d4-9460-4a0e-a46b-ae77b1186e61';