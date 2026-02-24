package com.StudentManageApp.StudentManagment.PresentationLayer;

import com.StudentManageApp.StudentManagment.Configuration.AppConstConfig;
import com.StudentManageApp.StudentManagment.DTOs.StudentDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    ResponseEntity<StudentDTO> createStudent(@RequestBody @Valid StudentDTO student){
        StudentDTO dto=studentService.createStudent(student);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<StudentDTO> getStudentById(@PathVariable(name = "id") Long studentId){
        return new ResponseEntity<>(studentService.getStudentById(studentId),HttpStatus.OK);
    }

    @GetMapping("/usn")
    ResponseEntity<StudentDTO> getStudentByUsn(@RequestParam(name = "usn") String usn){
        return new ResponseEntity<>(studentService.getStudentByUsn(usn),HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<WebResponse<StudentDTO>> getAllStudents(@RequestParam(name = "pageNumber",required = false,defaultValue = AppConstConfig.PAGE_NUMBER)Integer pageNumber,
                                                           @RequestParam(name = "pageSize",required = false,defaultValue = AppConstConfig.PAGE_SIZE)Integer pageSize,
                                                           @RequestParam(name = "sortOrder",required = false,defaultValue = AppConstConfig.SORT_ORDER)String sortOrder,
                                                           @RequestParam(name = "sortBy",required = false,defaultValue = AppConstConfig.SORT_BY_STUDENT)String sortBy){
        return new ResponseEntity<>(studentService.getAllStudents(pageNumber,pageSize,sortOrder,sortBy),HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    ResponseEntity<StudentDTO> updateStudent(@PathVariable(name = "id") Long studentId,@RequestBody @Valid  StudentDTO student){
        return new ResponseEntity<>(studentService.updateStudent(studentId,student), HttpStatus.OK);
    }

    @DeleteMapping("/del/{id}")
    ResponseEntity<String>  deleteStudent(@PathVariable(name = "id") Long studentId){
        String s="Student is deleted Successfully ";
        studentService.deleteStudent(studentId);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

}
