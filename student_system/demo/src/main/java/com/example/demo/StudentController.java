package com.example.demo;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentService studentService;
//    Hooking up the Student service to the constructor
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents(){
        return studentService.getStudents();

    }
    @PostMapping
    public void registerNewStudent(Student student){
        studentService.addNewStudent(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long id){
        studentService.deleteStudent(id);
    }
    @PutMapping(path = "{studentId}")

    public void updateStudent(@PathVariable("studentId") Long id,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String email){
        Scanner sc = new Scanner(System.in);
        System.out.println("Would you like to change name and email? [Y/N]");
        String answer = sc.next();

        if(answer.equalsIgnoreCase("y")){
            studentService.updateStudent(id, name, email);
        } else if (answer.equalsIgnoreCase("n")) {
            System.out.println("Goodbye!");
        }else{
            System.out.println("Invalid answer");
        }
    }
}
