package com.StudentManageApp.StudentManagment.ServiceLayer.implement;


import com.StudentManageApp.StudentManagment.DTOs.SectionDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.Repo.DepartmentRepo;
import com.StudentManageApp.StudentManagment.Repo.SectionRepo;
import com.StudentManageApp.StudentManagment.Repo.StudentRepo;
import com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces.SectionService;
import com.StudentManageApp.StudentManagment.entity.Section;
import com.StudentManageApp.StudentManagment.entity.Student;
import com.StudentManageApp.StudentManagment.execption.APIException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final StudentRepo studentRepo;
    private final DepartmentRepo departmentRepo;
    private final SectionRepo sectionRepo;
    private final ModelMapper mapper;


    @Override
    public SectionDTO createSection(SectionDTO sectionDTO) {
        if (sectionRepo.existsBySectionName(sectionDTO.getSectionName()))
            throw new APIException("Section Already Exists",HttpStatus.CONFLICT,"given section name already exists pls check once");
        Section section=mapper.map(sectionDTO,Section.class);
        mappingToEntity(section,sectionDTO);
        section=sectionRepo.save(section);
        sectionDTO=mapper.map(section,SectionDTO.class);
        mappingToDto(section,sectionDTO);
        return sectionDTO;
    }

    private void mappingToEntity(Section section, SectionDTO sectionDTO) {
        section.setDepartment(
                departmentRepo.findById(sectionDTO.getDepartmentId())
                        .orElseThrow(() ->
                                new APIException("Department not Found with "+"departmentId = "+sectionDTO.getDepartmentId(),HttpStatus.NOT_FOUND,"Given id does not exists please check once")
            )
        );

        if (sectionDTO.getStudentIds() != null && !sectionDTO.getStudentIds().isEmpty()) {
            if (sectionDTO.getStudentIds().contains(null)) {
                throw new APIException(
                        "Student ID list contains null value",
                        HttpStatus.BAD_REQUEST,
                        "Check the list once"
                );
            }

            List<Student> students =
                    studentRepo.findAllById(sectionDTO.getStudentIds());

            if (students.size() != sectionDTO.getStudentIds().size()) {
                throw new APIException(
                        "One or more students not found",
                        HttpStatus.NOT_FOUND,
                        "all student id in section are not found "
                );
            }
            students.forEach(s -> s.setSection(section));

            section.getStudents().addAll(students);
        }
    }


    private void mappingToDto(Section section,SectionDTO sectionDTO){
        sectionDTO.setDepartmentId(section.getDepartment().getDepId());
        sectionDTO.setStudentIds(new ArrayList<>());
        if (!section.getStudents().isEmpty()){
            sectionDTO.getStudentIds().addAll(section.getStudents().stream().map(Student::getSId).toList());
        }
    }

    @Override
    public SectionDTO getSectionById(Long sectionId) {
        if (sectionRepo.existsById(sectionId)){
           Section section=sectionRepo.findById(sectionId).orElseThrow(()->{return new APIException
                   ("Section Not Found id = "+sectionId,HttpStatus.NOT_FOUND,"Given id does not exists please check once");});
            SectionDTO dto=mapper.map(section,SectionDTO.class);
            mappingToDto(section,dto);
            return dto;
        }
        throw  new APIException
                ("Section Not Found id = "+sectionId,HttpStatus.NOT_FOUND,"Given id does not exists please check once");
    }

    @Override
    public WebResponse<SectionDTO> getAllSections(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort=(sortOrder.equalsIgnoreCase("desc"))?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Section>page=sectionRepo.findAll(pageable);
        List<Section>list=page.toList();
        if (list.isEmpty())throw new APIException("No Section Exists",HttpStatus.BAD_REQUEST,"No Section is in Data base pls create Section");
        List<SectionDTO>dtoList=list.stream().map(section -> {
            SectionDTO dto=mapper.map(section,SectionDTO.class);
            mappingToDto(section,dto);
            return dto;
        }).toList();

        return new WebResponse<>(dtoList,"Successful",pageNumber,pageSize,page.getTotalElements(),page.getTotalPages(),page.isLast());
    }

    @Override
    public boolean deleteSection(Long sectionId) {
        if (sectionRepo.existsById(sectionId)) {
            sectionRepo.delete(sectionRepo.findById(sectionId).orElseThrow());
            return true;
        }
        throw new APIException
                ("Section Not Found id = "+sectionId,HttpStatus.NOT_FOUND,"Given id does not exists please check once");
    }

    @Override
    public SectionDTO assignStudent(Long sectionId, Long sid) {

        Section section = sectionRepo.findById(sectionId)
                .orElseThrow(() -> new APIException
                        ("Section Not Found id = "+sectionId,HttpStatus.NOT_FOUND,"Given id does not exists please check once"));

        Student student = studentRepo.findById(sid)
                .orElseThrow(() -> new APIException
                        ("Student Not Found id = "+sid,HttpStatus.NOT_FOUND,"Given id does not exists please check once"));

        section.getStudents().add(student);
        student.setSection(section);

        sectionRepo.save(section);
        SectionDTO dto=mapper.map(section, SectionDTO.class);
        mappingToDto(section,dto);
        return dto;
    }

}
