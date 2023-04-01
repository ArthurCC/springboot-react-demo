package fr.camposcosta.springbootreactdemo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import fr.camposcosta.springbootreactdemo.enumeration.Gender;
import fr.camposcosta.springbootreactdemo.exception.EmailAlreadyExistsException;
import fr.camposcosta.springbootreactdemo.model.Student;

@Service
public class StudentService {

    private static final RowMapper<Student> STUDENT_ROW_MAPPER = (rs, rowNum) -> new Student(
            UUID.fromString(rs.getString("id")),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            Gender.valueOf(rs.getString("gender")));

    private static final String FIND_ALL_STUDENTS_QUERY = "SELECT * FROM students";
    private static final String FIND_STUDENT_BY_EMAIL_QUERY = "SELECT * FROM students WHERE email = ?";
    private static final String COUNT_STUDENT_EMAIL_QUERY = "SELECT COUNT(*) FROM students WHERE email = ?";
    // Need to cast to ::gender in the query because Postgres doesn't know how to
    // convert String to the gender type
    private static final String CREATE_STUDENT_QUERY = "INSERT INTO students (id, first_name, last_name, email, gender) VALUES (?, ?, ?, ?, ?::gender)";

    private JdbcTemplate jdbcTemplate;

    public StudentService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
}
