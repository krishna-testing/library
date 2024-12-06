package org.clx.library.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.clx.library.model.Student;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {

    @NotEmpty(message = "emailId should not be empty")
    @Email
    @Column(unique = true)
    private String emailId;
    @NotEmpty(message = "name should not be empty")
    @NotBlank
    private String name;

    private Integer age;
    @NotEmpty(message = "country name should not be empty")
    @NotBlank
    private String country;

    public Student studentRequestToStudent() {
        Student student = new Student();
        student.setEmailId(emailId);
        student.setName(name);
        student.setAge(age);
        student.setCountry(country);
        return student;
    }

}
