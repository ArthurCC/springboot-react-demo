package fr.camposcosta.springbootreactdemo.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import fr.camposcosta.springbootreactdemo.enumeration.Gender;
import fr.camposcosta.springbootreactdemo.model.Student;
import fr.camposcosta.springbootreactdemo.model.StudentCourse;

@Component
public class StudentCoursesResultSetExtractor implements ResultSetExtractor<Optional<Student>> {

    @Override
    public Optional<Student> extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (!rs.next()) {
            return Optional.empty();
        }

        Student student = new Student(
                UUID.fromString(rs.getString("student_id")),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                Gender.valueOf(rs.getString("gender")),
                new ArrayList<>());

        // check if student has courses
        if (rs.getString("course_id") == null) {
            return Optional.of(student);
        }

        // build course list
        do {
            student.getStudentCourses()
                    .add(
                            new StudentCourse(
                                    UUID.fromString(rs.getString("course_id")),
                                    rs.getString("name"),
                                    rs.getString("description"),
                                    rs.getString("department"),
                                    rs.getString("teacher_name"),
                                    LocalDate.parse(rs.getString("start_date")),
                                    LocalDate.parse(rs.getString("end_date")),
                                    rs.getObject("grade", Integer.class)));
        } while (rs.next());

        return Optional.of(student);
    }
}
