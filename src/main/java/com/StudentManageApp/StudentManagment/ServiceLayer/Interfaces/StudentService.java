package com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces;

import com.StudentManageApp.StudentManagment.DTOs.StudentDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.entity.Student;

import java.util.List;

public interface StudentService {

    // CREATE
    StudentDTO createStudent(StudentDTO student);

    // READ
    StudentDTO getStudentById(Long studentId);
    StudentDTO getStudentByUsn(String usn);
    WebResponse<StudentDTO> getAllStudents(Integer pageNumber,Integer pageSize,String sortOrder,String sortBy);

    // UPDATE
    StudentDTO updateStudent(Long studentId, StudentDTO student);

    // DELETE
    boolean deleteStudent(Long studentId);
    StudentDTO assignCourseByCourseNumber(Long studentId,String ...courseNumber);
}
