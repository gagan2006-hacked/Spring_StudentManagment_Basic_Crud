package com.StudentManageApp.StudentManagment.PresentationLayer;

import com.StudentManageApp.StudentManagment.Configuration.AppConstConfig;
import com.StudentManageApp.StudentManagment.DTOs.TeacherDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody @Valid TeacherDTO teacher){
        return new ResponseEntity<>(teacherService.createTeacher(teacher), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable(name = "id") Long teacherId){
        return new ResponseEntity<>(teacherService.getTeacherById(teacherId),HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<TeacherDTO> getTeacherByEmail(@RequestParam String email){
        return new ResponseEntity<>(teacherService.getTeacherByEmail(email), HttpStatus.OK);
    }

//
    @GetMapping
    public ResponseEntity<WebResponse<TeacherDTO>> getAllTeachers(@RequestParam(name = "pageNumber",required = false,defaultValue = AppConstConfig.PAGE_NUMBER)Integer pageNumber,
                                      @RequestParam(name = "pageSize",required = false,defaultValue = AppConstConfig.PAGE_SIZE)Integer pageSize,
                                      @RequestParam(name = "sortOrder",required = false,defaultValue = AppConstConfig.SORT_ORDER)String sortOrder,
                                      @RequestParam(name = "sortBy",required = false,defaultValue = AppConstConfig.SORT_BY_TEACHER)String sortBy){
        return new ResponseEntity<>(teacherService.getAllTeachers(pageNumber,pageSize,sortOrder,sortBy),HttpStatus.OK);
    }

    @PatchMapping("/update/{teacherId}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable Long teacherId, @RequestBody @Valid TeacherDTO teacher){
        return new ResponseEntity<>(teacherService.updateTeacher(teacherId,teacher),HttpStatus.OK);
    }

    @DeleteMapping("/delete/{teacherId}")
    public ResponseEntity<String> deleteTeacher(@PathVariable Long teacherId){
        teacherService.deleteTeacher(teacherId);
        return new ResponseEntity<>("Deleted SuccessFully",HttpStatus.OK);
    }

    //
    @PutMapping("/addCourse/{tid}/{cid}")
    public ResponseEntity<TeacherDTO> assignCourses(@PathVariable(name = "tid") Long teacherId,@PathVariable(name = "cid") Long courseId){
        return new ResponseEntity<>(teacherService.assignCourses(teacherId,courseId),HttpStatus.OK);
    }
}
