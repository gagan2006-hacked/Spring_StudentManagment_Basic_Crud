package com.StudentManageApp.StudentManagment.PresentationLayer;

import com.StudentManageApp.StudentManagment.Configuration.AppConstConfig;
import com.StudentManageApp.StudentManagment.DTOs.CourseDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/public/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService service;

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO){
        return new ResponseEntity<>(service.createCourse(courseDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable("id") Long courseId){
        return new ResponseEntity<CourseDTO>(service.getCourseById(courseId),HttpStatus.valueOf(200));
    }

    @GetMapping("/courseNo")
    public ResponseEntity<CourseDTO> getCourseByCourseNumber(@RequestParam String courseNumber){
        return new ResponseEntity<>(service.getCourseByCourseNumber(courseNumber), HttpStatus.valueOf(200));
    }
    @GetMapping
    public ResponseEntity<WebResponse<CourseDTO>> getAllCourses(@RequestParam(name = "pageNumber",required = false,defaultValue = AppConstConfig.PAGE_NUMBER)Integer pageNumber,
                                                                      @RequestParam(name = "pageSize",required = false,defaultValue = AppConstConfig.PAGE_SIZE)Integer pageSize,
                                                                      @RequestParam(name = "sortOrder",required = false,defaultValue = AppConstConfig.SORT_ORDER)String sortOrder,
                                                                      @RequestParam(name = "sortBy",required = false,defaultValue = AppConstConfig.SORT_BY_COURSE)String sortBy){
        return new ResponseEntity<>(service.getAllCourses(pageNumber,pageSize,sortOrder,sortBy), HttpStatus.valueOf(200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable("id") Long courseId, @RequestBody CourseDTO courseDTO){
        return new ResponseEntity<>(service.updateCourse(courseId,courseDTO),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable("id") Long courseId){
        if (service.deleteCourse(courseId))
            return new ResponseEntity<>("Deleted Successfully ",HttpStatus.OK);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{id}/teacher/{tid}")
    public ResponseEntity<CourseDTO> assignTeacher(@PathVariable("id") Long courseId,@PathVariable("tid") Long teacherId){
        return new ResponseEntity<>(service.assignTeacher(courseId,teacherId),HttpStatus.OK);
    }
}
