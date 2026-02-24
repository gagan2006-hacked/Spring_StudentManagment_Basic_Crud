package com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces;

import com.StudentManageApp.StudentManagment.DTOs.SectionDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.entity.Section;

import java.util.List;

public interface SectionService {

    // CREATE
    SectionDTO createSection(SectionDTO section);

    // READ
    SectionDTO getSectionById(Long sectionId);
    WebResponse<SectionDTO> getAllSections(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);

    // DELETE
    boolean deleteSection(Long sectionId);

    // RELATIONS
    SectionDTO assignStudent(Long sectionId, Long sid);
}

