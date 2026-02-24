package com.StudentManageApp.StudentManagment.PresentationLayer;

import com.StudentManageApp.StudentManagment.Configuration.AppConstConfig;
import com.StudentManageApp.StudentManagment.DTOs.SectionDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.Repo.SectionRepo;
import com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces.SectionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/section")
@RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;
    private final SectionRepo sectionRepo;

    @PostMapping
    public ResponseEntity<SectionDTO> createSection(@Valid @RequestBody SectionDTO section){
        return new ResponseEntity<>(sectionService.createSection(section), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectionDTO> getSectionById(@NotNull @PathVariable(name = "id") Long sectionId){
        return new ResponseEntity<>(sectionService.getSectionById(sectionId),HttpStatus.OK);
    }

    @GetMapping
    public  ResponseEntity<WebResponse<SectionDTO>> getAllSections(@RequestParam(name = "pageNumber",required = false,defaultValue = AppConstConfig.PAGE_NUMBER)Integer pageNumber,
                                                  @RequestParam(name = "pageSize",required = false,defaultValue = AppConstConfig.PAGE_SIZE)Integer pageSize,
                                                  @RequestParam(name = "sortOrder",required = false,defaultValue = AppConstConfig.SORT_ORDER)String sortOrder,
                                                  @RequestParam(name = "sortBy",required = false,defaultValue = AppConstConfig.SORT_BY_SECTION)String sortBy){
        return new ResponseEntity<>(sectionService.getAllSections(pageNumber,pageSize,sortOrder,sortBy),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSection(@NotNull @PathVariable(name = "id") Long sectionId){
        boolean b =sectionService.deleteSection(sectionId);
        String s="Section is deleted with id ="+sectionId;
        return new ResponseEntity<>(s,HttpStatus.OK);
    }

    @PatchMapping("/{id}/student/{sid}")
    public ResponseEntity<SectionDTO> assignStudent(@NotNull @PathVariable(name = "id") Long sectionId,@NotNull @PathVariable(name = "sid") Long sid){
        return new ResponseEntity<>(sectionService.assignStudent(sectionId,sid),HttpStatus.OK);
    }

}
