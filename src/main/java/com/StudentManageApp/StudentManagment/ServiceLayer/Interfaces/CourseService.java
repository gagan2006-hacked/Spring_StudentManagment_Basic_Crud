package com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces;

import com.StudentManageApp.StudentManagment.DTOs.CourseDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.entity.Course;

import java.util.List;

public interface CourseService {

    // CREATE
    CourseDTO createCourse(CourseDTO course);

    // READ
    CourseDTO getCourseById(Long courseId);
    CourseDTO getCourseByCourseNumber(String courseNumber);
    WebResponse<CourseDTO> getAllCourses(Integer pageNumber,Integer pageSize,String sortOrder,String sortBy);

    // UPDATE
    CourseDTO updateCourse(Long courseId, CourseDTO course);

    // DELETE
    boolean deleteCourse(Long courseId);

    // RELATIONS
    CourseDTO assignTeacher(Long courseId, Long teacherId);
}
