package fr.camposcosta.springbootreactdemo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import fr.camposcosta.springbootreactdemo.model.Response;
import fr.camposcosta.springbootreactdemo.model.StudentCourse;
import fr.camposcosta.springbootreactdemo.service.CourseService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses/{studentId}")
    public ResponseEntity<Response<List<StudentCourse>>> getStudentCourses(@PathVariable UUID studentId) {

        log.info("getStudentCourses called");

        List<StudentCourse> studentCourses = courseService.findCoursesByStudentId(studentId);

        return ResponseEntity.ok(
                new Response<>(
                        LocalDateTime.now(),
                        HttpStatus.OK,
                        ImmutableMap.of("studentCourses", studentCourses)));
    }
}
