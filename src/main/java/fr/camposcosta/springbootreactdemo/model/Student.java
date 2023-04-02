package fr.camposcosta.springbootreactdemo.model;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import fr.camposcosta.springbootreactdemo.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student {

    private UUID id;

    @NotBlank(message = "cannot be blank")
    private String firstName;

    @NotBlank(message = "cannot be blank")
    private String lastName;

    @NotBlank(message = "cannot be blank")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "invalid")
    private String email;

    @NotNull(message = "cannot be null")
    private Gender gender;

    @JsonInclude(Include.NON_EMPTY)
    private List<StudentCourse> studentCourses;
}
