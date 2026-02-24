package com.StudentManageApp.StudentManagment.DTOs;

import com.StudentManageApp.StudentManagment.entity.Enum.DepartmentName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DepartmentDTO {
    private Long depId;
    private DepartmentName departmentName;
    private List<Long> studentIds;
    private List<Long> courseIds;
    private List<Long> teacherIds;
    private List<Long> sectionIds;
}
