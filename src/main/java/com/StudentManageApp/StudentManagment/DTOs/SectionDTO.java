package com.StudentManageApp.StudentManagment.DTOs;

import com.StudentManageApp.StudentManagment.entity.Enum.SectionName;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.aspectj.weaver.ast.Not;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class SectionDTO {
    private Long secId;
    @NotNull
    private SectionName sectionName;
    @NotNull
    private Long departmentId;
    private List<Long> studentIds;
}
