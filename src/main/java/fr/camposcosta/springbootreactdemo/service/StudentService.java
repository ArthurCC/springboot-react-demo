package fr.camposcosta.springbootreactdemo.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import fr.camposcosta.springbootreactdemo.enumeration.Gender;
import fr.camposcosta.springbootreactdemo.exception.EmailAlreadyExistsException;
import fr.camposcosta.springbootreactdemo.exception.ResourceNotFoundException;
import fr.camposcosta.springbootreactdemo.model.Student;

@Service
public class StudentService {

        private static final RowMapper<Student> STUDENT_ROW_MAPPER = (rs, rowNum) -> new Student(
                        UUID.fromString(rs.getString("id")),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        Gender.valueOf(rs.getString("gender")),
                        Collections.emptyList());

        private static final String FIND_ALL_STUDENTS_QUERY = "SELECT * FROM students";
        private static final String FIND_STUDENT_BY_EMAIL_QUERY = "SELECT * FROM students WHERE email = ?";
        private static final String COUNT_STUDENT_EMAIL_QUERY = "SELECT COUNT(*) FROM students WHERE email = ?";
        // Need to cast to ::gender in the query because Postgres doesn't know how to
        // convert String to the gender type
        private static final String CREATE_STUDENT_QUERY = "INSERT INTO students (id, first_name, last_name, email, gender) VALUES (?, ?, ?, ?, ?::gender)";
        private static final String FIND_STUDENT_WITH_COURSES = "SELECT s.id AS student_id, s.first_name, s.last_name, s.email, s.gender, c.id AS course_id, c.name, c.description, c.department, c.teacher_name, start_date, end_date, grade "
                        + "FROM students s "
                        + "LEFT JOIN student_course ON s.id = student_id "
                        + "LEFT JOIN courses c ON course_id = c.id "
                        + "WHERE s.id = ?";
        private static final String DELETE_STUDENT_QUERY = "DELETE FROM students where id = ?";

        private JdbcTemplate jdbcTemplate;
        private StudentCoursesResultSetExtractor studentCoursesResultSetExtractor;

        public StudentService(
                        JdbcTemplate jdbcTemplate,
                        StudentCoursesResultSetExtractor studentCoursesResultSetExtractor) {
                this.jdbcTemplate = jdbcTemplate;
                this.studentCoursesResultSetExtractor = studentCoursesResultSetExtractor;
        }

        public List<Student> findAll() {

                return jdbcTemplate.query(FIND_ALL_STUDENTS_QUERY, STUDENT_ROW_MAPPER);
        }

        public Student findByEmail(String email) {
                return jdbcTemplate.queryForObject(
                                FIND_STUDENT_BY_EMAIL_QUERY,
                                STUDENT_ROW_MAPPER,
                                email);
        }

        /**
         * Get student by id with courses info
         * 
         * @param studentId
         * @return
         */
        public Student findStudentById(UUID studentId) {
                return jdbcTemplate.query(FIND_STUDENT_WITH_COURSES, studentCoursesResultSetExtractor, studentId)
                                .orElseThrow(
                                                () -> new ResourceNotFoundException(
                                                                String.format("Student with [uuid=%s] not found",
                                                                                studentId)));
        }

        public Student createStudent(Student student) {

                // Check email does not exist
                Integer rowCount = jdbcTemplate.queryForObject(
                                COUNT_STUDENT_EMAIL_QUERY,
                                Integer.class,
                                student.getEmail());

                if (rowCount > 0) {
                        throw new EmailAlreadyExistsException(
                                        String.format("Email %s already exists", student.getEmail()));
                }

                // Insert student
                student.setId(UUID.randomUUID());
                jdbcTemplate.update(
                                CREATE_STUDENT_QUERY,
                                student.getId(),
                                student.getFirstName(),
                                student.getLastName(),
                                student.getEmail(),
                                student.getGender().name());

                return student;
        }

        public void deleteStudent(UUID studentId) {
                int result = jdbcTemplate.update(DELETE_STUDENT_QUERY, studentId);

                // delete failed
                if (result == 0) {
                        throw new ResourceNotFoundException(
                                        String.format("Student with [uuid=%s] not found", studentId));
                }
        }
}
