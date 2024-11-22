package org.clx.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.clx.library.model.Card;
import org.clx.library.model.Student;

import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {


    private int id;
    private String emailId;
    private String name;
    //Future scope adult books filter
    private Integer age;
    private String country;

    private CardDto cardDto;

    private Date createdOn;

    private Date updatedOn;


    public Student studentDtoToStudent(){
        Student student = new Student();
        student.setId(id);
        student.setEmailId(emailId);
        student.setName(name);
        student.setAge(age);
        student.setCountry(country);
        student.setCard(cardDto.cardDtoToCard());
        student.setCreatedOn(createdOn);
        student.setUpdatedOn(updatedOn);
        return student;
    }

    public StudentDto studentToStudentDto(Student student){
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setEmailId(student.getEmailId());
        studentDto.setName(student.getName());
        studentDto.setAge(student.getAge());
        studentDto.setCountry(student.getCountry());
        studentDto.setCardDto(new CardDto().cardToCardDto(student.getCard()));
        studentDto.setCreatedOn(student.getCreatedOn());
        studentDto.setUpdatedOn(student.getUpdatedOn());
        return studentDto;
    }


}
