package com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces;

import com.StudentManageApp.StudentManagment.DTOs.TeacherDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;

public interface TeacherService {

    // CREATE
    TeacherDTO createTeacher(TeacherDTO teacher);

    // READ
    TeacherDTO getTeacherById(Long teacherId);
    TeacherDTO getTeacherByEmail(String email);
    WebResponse<TeacherDTO> getAllTeachers(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);

    // UPDATE
    TeacherDTO updateTeacher(Long teacherId, TeacherDTO teacherDTO);

    // DELETE
    boolean deleteTeacher(Long teacherId);

    // RELATIONS
    TeacherDTO assignCourses(Long teacherId,Long courseId);
}

