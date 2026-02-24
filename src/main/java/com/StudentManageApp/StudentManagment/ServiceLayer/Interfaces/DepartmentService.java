package com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces;

import com.StudentManageApp.StudentManagment.DTOs.DepartmentDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;

public interface DepartmentService {

    // CREATE
    DepartmentDTO createDepartment(DepartmentDTO department);

    // READ
    DepartmentDTO getDepartmentById(Long departmentId);
    WebResponse<DepartmentDTO> getAllDepartments(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);

    // DELETE
    boolean deleteDepartment(Long departmentId);

    DepartmentDTO addStudentToDepartment(Long departmentId,Long sId);

    DepartmentDTO addSectionToDepartment(Long departmentId,Long secId);

    DepartmentDTO addCourseToDepartment(Long departmentId,Long cid);

    DepartmentDTO addTeacherToDepartment(Long departmentId,Long tid);

}
