package fr.camposcosta.springbootreactdemo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import fr.camposcosta.springbootreactdemo.model.StudentCourse;

@Service
public class CourseService {

    private static final RowMapper<StudentCourse> COURSE_ROW_MAPPER = (rs, rowNum) -> new StudentCourse(
            UUID.fromString(rs.getString("id")),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("department"),
            rs.getString("teacher_name"),
            LocalDate.parse(rs.getString("start_date")),
            LocalDate.parse(rs.getString("end_date")),
            rs.getInt("grade"));

    private static final String FIND_STUDENT_COURSES = "SELECT c.id, c.name, c.description, c.department, c.teacher_name, start_date, end_date, grade "
            + "FROM students s "
            + "INNER JOIN student_course ON s.id = student_id "
            + "INNER JOIN courses c ON course_id = c.id "
            + "WHERE s.id = ?";

    private final JdbcTemplate jdbcTemplate;

    public CourseService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<StudentCourse> findCoursesByStudentId(UUID studentId) {
        return jdbcTemplate.query(
                FIND_STUDENT_COURSES,
                COURSE_ROW_MAPPER,
                studentId);
    }
}
