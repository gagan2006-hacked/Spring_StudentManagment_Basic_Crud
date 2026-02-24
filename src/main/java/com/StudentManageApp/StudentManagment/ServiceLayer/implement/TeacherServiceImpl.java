package com.StudentManageApp.StudentManagment.ServiceLayer.implement;

import com.StudentManageApp.StudentManagment.DTOs.TeacherDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.Repo.CourseRepo;
import com.StudentManageApp.StudentManagment.Repo.DepartmentRepo;
import com.StudentManageApp.StudentManagment.Repo.TeacherRepo;
import com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces.TeacherService;
import com.StudentManageApp.StudentManagment.entity.Course;
import com.StudentManageApp.StudentManagment.entity.Teacher;
import com.StudentManageApp.StudentManagment.execption.APIException;
import jakarta.transaction.Transactional;
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
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepo teacherRepo;
    private final CourseRepo courseRepo;
    private final DepartmentRepo departmentRepo;
    private final ModelMapper mapper;

    @Transactional
    @Override
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        if (teacherRepo.existsByEmail(teacherDTO.getEmail()))throw new APIException("Teacher already exists",HttpStatus.CONFLICT,"Teacher already exists with email :"+teacherDTO.getEmail());
        Teacher teacher=mapper.map(teacherDTO,Teacher.class);
        dtoTOEntity(teacherDTO,teacher);
        teacher=teacherRepo.save(teacher);
        manageAllCourse(teacher);
        mapper.map(teacher,teacherDTO);
        entityToDTO(teacherDTO,teacher);
        return teacherDTO;
    }

    private void  manageAllCourse(Teacher teacher){
        if (!teacher.getCourses().isEmpty()){
            for (Course c:teacher.getCourses()){
                c.addTeachers(teacher);
                courseRepo.save(c);
            }
        }
    }

    private void dtoTOEntity(TeacherDTO teacherDTO, Teacher teacher) {
        if (teacherDTO.getDepartmentId()!=null){
            teacher.setDepartment(departmentRepo.findById(teacherDTO.getDepartmentId()).
                    orElseThrow(()->new APIException(" Department does not exits with id:"+teacherDTO.getDepartmentId(),HttpStatus.NOT_FOUND,"Department id is wrong pls check")));
        }
        if (teacherDTO.getCourseIds()!=null){
            teacher.setCourses(new ArrayList<>());
            teacher.getCourses().addAll(teacherDTO.getCourseIds().stream().map(id->{
                Course course=courseRepo.findById(id).orElseThrow(()->new APIException(" Course does not exits with id:"+id, HttpStatus.NOT_FOUND,"Course id is wrong pls check"));

                return course;
            }).toList());
        }
    }

    public void entityToDTO(TeacherDTO dto,Teacher teacher){
        if (teacher.getDepartment()!=null){
            dto.setDepartmentId(teacher.getDepartment().getDepId());
        }
        if (!teacher.getCourses().isEmpty()){
            dto.setCourseIds(new ArrayList<>());
            dto.getCourseIds().addAll(teacher.getCourses().stream().map(Course::getCid).toList());
        }
    }

    @Override
    public TeacherDTO getTeacherById(Long teacherId) {
        if (teacherRepo.existsById(teacherId)){
            Teacher teacher=teacherRepo.findById(teacherId).orElseThrow();
            TeacherDTO dto=mapper.map(teacher,TeacherDTO.class);
            entityToDTO(dto,teacher);
            return dto;
        }
        throw new APIException("Teacher Not Found",HttpStatus.NOT_FOUND,"Teacher id is wrong");
    }

    @Override
    public TeacherDTO getTeacherByEmail(String email) {
        if (teacherRepo.existsByEmail(email)){
            Teacher teacher=teacherRepo.findByEmail(email);
            TeacherDTO dto=mapper.map(teacher,TeacherDTO.class);
            entityToDTO(dto,teacher);
            return dto;
        }
        throw new APIException("Teacher Not Found",HttpStatus.NOT_FOUND,"Teacher Email is wrong");
    }

    @Override
    public WebResponse<TeacherDTO> getAllTeachers(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort=(sortOrder.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Teacher>page=teacherRepo.findAll(pageable);
        List<Teacher>list=page.toList();
        List<TeacherDTO>dtos=list.stream().map(teacher ->{
            TeacherDTO dto=mapper.map(teacher,TeacherDTO.class);
            entityToDTO(dto,teacher);
            return dto;
        }).toList();
        return new WebResponse<>(dtos,"Successfull",pageNumber,pageSize,page.getTotalElements(),page.getTotalPages(),page.isLast());
    }

    @Override
    public TeacherDTO updateTeacher(Long teacherId, TeacherDTO teacherDTO) {
        if (!teacherRepo.existsById(teacherId))throw new APIException("Teacher Not Found",HttpStatus.NOT_FOUND,"Teacher id is wrong");
        Teacher teacher=mapper.map(teacherDTO,Teacher.class);
        teacher.setTid(teacherId);
        dtoTOEntity(teacherDTO,teacher);
        teacher=teacherRepo.save(teacher);
        teacherDTO=mapper.map(teacher,TeacherDTO.class);
        entityToDTO(teacherDTO,teacher);
        return teacherDTO;
    }

    @Override
    public boolean deleteTeacher(Long teacherId) {
        if (!teacherRepo.existsById(teacherId))throw new APIException("Teacher Not Found",HttpStatus.NOT_FOUND,"Teacher id is wrong");
        Teacher t=teacherRepo.findById(teacherId).orElseThrow();
        deleteAllTeachers(t);
        teacherRepo.delete(t);
        return true;
    }

    private void deleteAllTeachers(Teacher t) {
        for (Course c:t.getCourses()){
            c.getTeachers().remove(t);
            courseRepo.save(c);
        }
    }

    @Override
    public TeacherDTO assignCourses(Long teacherId, Long courseId) {
        Course course=courseRepo.findById(courseId).orElseThrow();
        String no=course.getCourseNumber();
        return assignCourses(teacherId,no);
    }


    private TeacherDTO assignCourses(Long teacherId,String courseNumber) {
        if (courseNumber.isBlank())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Course Number Was Found");
        if (!teacherRepo.existsById(teacherId))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Teacher Not Found");
        Teacher teacher=teacherRepo.findById(teacherId).orElseThrow();
        if (!courseRepo.existsByCourseNumber(courseNumber))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Not Found");
        Course course = courseRepo.findByCourseNumber(courseNumber).orElseThrow();
        course.addTeachers(teacher);
        courseRepo.save(course);
        teacher.addCourses(course);
        Teacher teacher1=teacherRepo.save(teacher);
        TeacherDTO dto=mapper.map(teacher1,TeacherDTO.class);
        entityToDTO(dto,teacher1);
        return dto;
    }
}