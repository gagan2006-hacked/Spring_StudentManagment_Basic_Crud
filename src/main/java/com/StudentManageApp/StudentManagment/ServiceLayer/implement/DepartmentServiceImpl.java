package com.StudentManageApp.StudentManagment.ServiceLayer.implement;

import com.StudentManageApp.StudentManagment.DTOs.DepartmentDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.Repo.*;
import com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces.DepartmentService;
import com.StudentManageApp.StudentManagment.entity.*;
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
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepo departmentRepo;
    private final StudentRepo studentRepo;
    private final SectionRepo sectionRepo;
    private final TeacherRepo teacherRepo;
    private final CourseRepo courseRepo;
    private final ModelMapper mapper;
    private String notFound="Department not Found with ";
    @Override
    public DepartmentDTO createDepartment(DepartmentDTO department) {
        if (departmentRepo.existsByDepartmentName(department.getDepartmentName()))throw  new APIException("Department Already Exists",HttpStatus.CONFLICT,"department with name = "+department.getDepartmentName()+" already exists");
        Department d=mapper.map(department,Department.class);
        manageEnitiyMapping(d,department);
        d=departmentRepo.save(d);
        DepartmentDTO dto= mapper.map(d,DepartmentDTO.class);
        manageDtoMapping(d,dto);
        return dto;
    }

    private void manageDtoMapping(Department d, DepartmentDTO dto) {
        dto.setCourseIds(new ArrayList<>());
        dto.setTeacherIds(new ArrayList<>());
        dto.setStudentIds(new ArrayList<>());
        dto.setSectionIds(new ArrayList<>());
        if (!d.getStudents().isEmpty()){
            dto.getStudentIds().addAll(d.getStudents().stream().map(Student::getSId).toList());
        }
        if (!d.getTeachers().isEmpty()){
            dto.getTeacherIds().addAll(d.getTeachers().stream().map(Teacher::getTid).toList());
        }
        if (!d.getSections().isEmpty()){
            dto.getSectionIds().addAll(d.getSections().stream().map(Section::getSecId).toList());
        }
        if (!d.getCourses().isEmpty()){
            dto.getCourseIds().addAll(d.getCourses().stream().map(Course::getCid).toList());
        }
    }

    @Override
    public DepartmentDTO getDepartmentById(Long departmentId) {
        if (departmentRepo.existsById(departmentId)){
            Department department=departmentRepo.findById(departmentId).orElseThrow(()->new APIException(notFound+"departmentId = "+departmentId,HttpStatus.NOT_FOUND,"Given id does not exists please check once"));
            DepartmentDTO dto=mapper.map(department,DepartmentDTO.class);
            manageDtoMapping(department,dto);
            return dto;
        }
        throw new APIException(notFound+"departmentId = "+departmentId,HttpStatus.NOT_FOUND,"Given id does not exists please check once");
    }

    @Override
    public WebResponse<DepartmentDTO> getAllDepartments(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort=(sortOrder.equalsIgnoreCase("desc"))?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Department>page=departmentRepo.findAll(pageable);
        List<Department>list=page.toList();
        if (list.isEmpty())throw new APIException("No Department Exists",HttpStatus.BAD_REQUEST,"No Department is in Data base pls create Department");
        List<DepartmentDTO>dtos=list.stream().map(department -> {
            DepartmentDTO dto=mapper.map(department,DepartmentDTO.class);
            manageDtoMapping(department,dto);
            return dto;
        }).toList();
        return new WebResponse<>(dtos,"Successful",pageNumber,pageSize,page.getTotalElements(),page.getTotalPages(),page.isLast());
    }

    @Override
    public boolean deleteDepartment(Long departmentId) {
        if (departmentRepo.existsById(departmentId)){
            departmentRepo.delete(departmentRepo.findById(departmentId).orElseThrow());return true;
        }
        throw new APIException(notFound+"departmentId = "+departmentId,HttpStatus.NOT_FOUND,"Given id does not exists please check once");
    }

    @Override
    public DepartmentDTO addStudentToDepartment(Long departmentId, Long sId) {
        Student student=studentRepo.findById(sId).orElseThrow(()->new APIException("Student Not Found id = "+sId,HttpStatus.NOT_FOUND,"Given id does not exists please check once"));
        Department department=departmentRepo.findById(sId).orElseThrow(()->new APIException(notFound+"departmentId = "+departmentId,HttpStatus.NOT_FOUND,"Given id does not exists please check once"));
        student.setDepartment(department);
        List<Student>list=department.getStudents();
        if (list==null){
            list=new ArrayList<>();
            department.setStudents(list);
        }
        list.add(student);
        departmentRepo.save(department);
        studentRepo.save(student);
        DepartmentDTO dto=mapper.map(department,DepartmentDTO.class);
        manageDtoMapping(department,dto);
        return dto;
    }

    @Override
    public DepartmentDTO addSectionToDepartment(Long departmentId, Long secId) {
        Department department=departmentRepo.findById(departmentId).orElseThrow(()->new APIException
                (notFound+"departmentId = "+departmentId,HttpStatus.NOT_FOUND,"Given id does not exists please check once"));
        Section section=sectionRepo.findById(secId).orElseThrow(()->new APIException
                ("Section Not Found",HttpStatus.NOT_FOUND,"Given id does not exists please check once"));
        section.setDepartment(department);
        List<Section>list=department.getSections();
        if (list==null){
            list=new ArrayList<>();
            department.setSections(list);
        }
        list.add(section);
        departmentRepo.save(department);
        sectionRepo.save(section);
        DepartmentDTO dto=mapper.map(department,DepartmentDTO.class);
        manageDtoMapping(department,dto);
        return dto;
    }

    @Override
    public DepartmentDTO addCourseToDepartment(Long departmentId, Long cid) {
        if (!courseRepo.existsById(cid))throw new APIException
                ("Course Not Found",HttpStatus.NOT_FOUND,"Given id does not exists please check once");
        if (!departmentRepo.existsById(departmentId))throw new APIException
                (notFound+"departmentId = "+departmentId,HttpStatus.NOT_FOUND,"Given id does not exists please check once");
        Department department=departmentRepo.findById(departmentId).orElseThrow();
        Course course=courseRepo.findById(cid).orElseThrow();
        course.setDepartment(department);
        List<Course>list=department.getCourses();
        if (list==null){
            list=new ArrayList<>();
            department.setCourses(list);
        }
        list.add(course);
        departmentRepo.save(department);
        courseRepo.save(course);
        DepartmentDTO dto=mapper.map(department,DepartmentDTO.class);
        manageDtoMapping(department,dto);
        return dto;
    }

    @Override
    public DepartmentDTO addTeacherToDepartment(Long departmentId, Long tid) {
        if (!departmentRepo.existsById(departmentId))throw new APIException
                (notFound+"departmentId = "+departmentId,HttpStatus.NOT_FOUND,"Given id does not exists please check once");
        if (!teacherRepo.existsById(tid))throw new APIException
                ("Teacher Not Found",HttpStatus.NOT_FOUND,"Given id does not exists please check once");
        Department department=departmentRepo.findById(departmentId).orElseThrow();
        Teacher teacher=teacherRepo.findById(tid).orElseThrow();
        teacher.setDepartment(department);
        List<Teacher>list=department.getTeachers();
        if (list==null){
            list=new ArrayList<>();
            department.setTeachers(list);
        }
        list.add(teacher);
        departmentRepo.save(department);
        teacherRepo.save(teacher);
        DepartmentDTO dto=mapper.map(department,DepartmentDTO.class);
        manageDtoMapping(department,dto);
        return dto;
    }

    public void manageEnitiyMapping(Department department, DepartmentDTO departmentDTO){
        if (departmentDTO.getStudentIds() != null) {
            department.setStudents(
                    departmentDTO.getStudentIds().stream()
                            .map(id -> studentRepo.findById(id)
                                    .orElseThrow(() -> new APIException
                                            ("Student Not Found",HttpStatus.NOT_FOUND,"Given id does not exists please check once")))
                            .toList()
            );
        }

        if (departmentDTO.getSectionIds() != null) {
            department.setSections(
                    departmentDTO.getSectionIds().stream()
                            .map(id -> sectionRepo.findById(id)
                                    .orElseThrow(() -> new APIException
                                            ("Section Not Found",HttpStatus.NOT_FOUND,"Given id does not exists please check once")))
                            .toList()
            );
        }

        if (departmentDTO.getCourseIds() != null) {
            department.setCourses(
                    departmentDTO.getCourseIds().stream()
                            .map(id -> courseRepo.findById(id)
                                    .orElseThrow(() -> new APIException
                                            ("Course Not Found",HttpStatus.NOT_FOUND,"Given id does not exists please check once")))
                            .toList()
            );
        }

        if (departmentDTO.getTeacherIds() != null) {
            department.setTeachers(
                    departmentDTO.getTeacherIds().stream()
                            .map(id -> teacherRepo.findById(id)
                                    .orElseThrow(() -> new APIException
                                            ("Teacher Not Found",HttpStatus.NOT_FOUND,"Given id does not exists please check once")))
                            .toList()
            );
        }
    }
}
