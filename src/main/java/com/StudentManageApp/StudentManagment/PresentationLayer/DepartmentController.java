package com.StudentManageApp.StudentManagment.PresentationLayer;

import com.StudentManageApp.StudentManagment.Configuration.AppConstConfig;
import com.StudentManageApp.StudentManagment.DTOs.DepartmentDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController  {
    private final DepartmentService service;

    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentDTO department){
       return new ResponseEntity<>(service.createDepartment(department), HttpStatus.CREATED);
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@Valid @PathVariable Long departmentId){
        return new ResponseEntity<>(service.getDepartmentById(departmentId),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<WebResponse<DepartmentDTO>> getAllDepartments(@RequestParam(name = "pageNumber",required = false,defaultValue = AppConstConfig.PAGE_NUMBER)Integer pageNumber,
                                                       @RequestParam(name = "pageSize",required = false,defaultValue = AppConstConfig.PAGE_SIZE)Integer pageSize,
                                                       @RequestParam(name = "sortOrder",required = false,defaultValue = AppConstConfig.SORT_ORDER)String sortOrder,
                                                       @RequestParam(name = "sortBy",required = false,defaultValue = AppConstConfig.SORT_BY_DEPARTMENT)String sortBy){
        return new ResponseEntity<>(service.getAllDepartments(pageNumber,pageSize,sortOrder,sortBy),HttpStatus.OK);
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@Valid @PathVariable Long departmentId){
        boolean b=service.deleteDepartment(departmentId);
        String s="Department Deleted Successfully";
        return new ResponseEntity<>(s, HttpStatus.OK);
    }
}
