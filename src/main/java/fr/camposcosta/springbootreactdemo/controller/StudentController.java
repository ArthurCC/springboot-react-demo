package fr.camposcosta.springbootreactdemo.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import fr.camposcosta.springbootreactdemo.model.Response;
import fr.camposcosta.springbootreactdemo.model.Student;
import fr.camposcosta.springbootreactdemo.service.StudentService;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
public class StudentController {

        private StudentService studentService;

        public StudentController(StudentService studentService) {
                this.studentService = studentService;
        }

        @GetMapping("/students")
        public ResponseEntity<Response<List<Student>>> getAllStudents() {

                log.info("getAllStudents called");
                // Thread.sleep(1000);

                List<Student> students = studentService.findAll();

                return ResponseEntity.ok(
                                new Response<>(
                                                LocalDateTime.now(),
                                                HttpStatus.OK,
                                                ImmutableMap.of("students", students)));
        }

        @GetMapping("/student")
        public ResponseEntity<Response<Student>> getByEmail(@RequestParam String email) {
                Student student = studentService.findByEmail(email);

                return ResponseEntity.ok(
                                new Response<>(
                                                LocalDateTime.now(),
                                                HttpStatus.OK,
                                                ImmutableMap.of("student", student)));
        }

        @PostMapping("/students")
        public ResponseEntity<Response<Student>> createStudent(@RequestBody @Valid Student student) {

                log.info("createStudent called");

                Student createdStudent = studentService.createStudent(student);

                return new ResponseEntity<>(
                                new Response<>(
                                                LocalDateTime.now(),
                                                HttpStatus.CREATED,
                                                ImmutableMap.of("student", createdStudent)),
                                HttpStatus.CREATED);
        }
}
