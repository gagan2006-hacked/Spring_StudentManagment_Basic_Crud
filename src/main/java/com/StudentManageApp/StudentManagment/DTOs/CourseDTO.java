package com.StudentManageApp.StudentManagment.DTOs;

import com.StudentManageApp.StudentManagment.entity.Enum.CourseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDTO {
    private Long cid;

    @NotBlank
    @NotNull
    private String courseNumber;

    @NotNull
    private String courseName;

    @NotNull
    private CourseType valueOfCourse;

    @NotNull
    private Integer courseCredits;

    @NotNull
    private Long departmentId;
    private List<Long> teacherIds;
}
