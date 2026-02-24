package com.StudentManageApp.StudentManagment.DTOs;

import com.StudentManageApp.StudentManagment.entity.Enum.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class TeacherDTO {

    private Long tid;
    @NotNull
    private String teacherName;
    @NotNull
    private String email;
    private String phoneNo;
    private Gender gender;

    @NotNull
    private Long departmentId;
    private List<Long> courseIds;
}
