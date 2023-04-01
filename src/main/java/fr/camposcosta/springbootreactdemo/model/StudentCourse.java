package fr.camposcosta.springbootreactdemo.model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represent a course taken by a student by joining courses and student_course
 * table
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentCourse {

    private UUID id;
    private String name;
    private String description;
    private String department;
    private String teacherName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer grade;
}
