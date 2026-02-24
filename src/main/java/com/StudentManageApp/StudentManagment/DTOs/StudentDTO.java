package com.StudentManageApp.StudentManagment.DTOs;

import com.StudentManageApp.StudentManagment.entity.Enum.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class StudentDTO {
    private Long sId;
    @NotBlank
    @NotNull
    private String studentName;
    @NotBlank
    @NotNull
    @Email
    private String email;
    @NotBlank
    @NotNull
    private String usn;
    @NotNull
    private Gender gender;
    @NotNull
    private Long departmentId;
    @NotNull
    private Long sectionId;
    @NotNull
    private List<Long> courseIds;
}
