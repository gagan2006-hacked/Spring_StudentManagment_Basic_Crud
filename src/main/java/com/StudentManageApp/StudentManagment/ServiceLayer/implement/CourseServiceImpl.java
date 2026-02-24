package com.StudentManageApp.StudentManagment.ServiceLayer.implement;

import com.StudentManageApp.StudentManagment.DTOs.CourseDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.Repo.CourseRepo;
import com.StudentManageApp.StudentManagment.Repo.DepartmentRepo;
import com.StudentManageApp.StudentManagment.Repo.TeacherRepo;
import com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces.CourseService;
import com.StudentManageApp.StudentManagment.entity.Course;
import com.StudentManageApp.StudentManagment.entity.Department;
import com.StudentManageApp.StudentManagment.entity.Teacher;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepo courseRepo;
    private final DepartmentRepo departmentRepo;
    private final TeacherRepo teacherRepo;
    private final ModelMapper mapper;

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) throws ResponseStatusException {
        if (courseRepo.existsByCourseNumber(courseDTO.getCourseNumber()))throw new APIException("Course already exists",HttpStatus.CONFLICT,
                "Given Request Body has course number that already exists");
        Course course=mapper.map(courseDTO,Course.class);
        manageEntityMapping(course,courseDTO);
        course=courseRepo.save(course);
        CourseDTO dto=mapper.map(course,CourseDTO.class);
        manageDtoMapping(course,dto);
        return dto;
    }

    @Override
    public CourseDTO getCourseById(Long courseId)throws ResponseStatusException {
        if (courseRepo.existsById(courseId)){
            Course course=courseRepo.findById(courseId).orElseThrow(()->new APIException("Course Not Found",
                    HttpStatus.NOT_FOUND,"Given id does not exists please check once"));
            CourseDTO dto=mapper.map(course,CourseDTO.class);
            manageDtoMapping(course,dto);
            return dto;
        }
        throw new APIException("Course Not Found",
                HttpStatus.NOT_FOUND,"Given id does not exists please check once");
    }

    @Override
    public CourseDTO getCourseByCourseNumber(String courseNumber) {
        if (!courseRepo.existsByCourseNumber(courseNumber))throw new APIException("Course does not exists",HttpStatus.NOT_FOUND,
                "Given Request Body has course number that does not  exists");
        Course course=courseRepo.findByCourseNumber(courseNumber).orElseThrow(()->new APIException("Course does not exists",HttpStatus.NOT_FOUND,
                "Given Request Body has course number that does not  exists"));
        CourseDTO dto=mapper.map(course,CourseDTO.class);
        manageDtoMapping(course,dto);
        return dto;
    }

    @Override
    public WebResponse<CourseDTO> getAllCourses(Integer pageNumber,Integer pageSize,String sortOrder,String sortBy) {
        Sort sort=(sortOrder.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Course> page=courseRepo.findAll(pageable);
        List<CourseDTO>list=page.stream().map(course ->{
           CourseDTO dto= mapper.map(course,CourseDTO.class);
           manageDtoMapping(course,dto);
           return dto;
        }
        ).toList();
        return new WebResponse<>(list,"Successful",pageNumber,pageSize,page.getTotalElements(),page.getTotalPages(),page.isLast());
    }

    @Override
    public CourseDTO updateCourse(Long courseId, CourseDTO courseDTO) {

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() ->
                        new APIException("Course Not Found",
                                HttpStatus.NOT_FOUND,"Given id does not exists please check once"));
        mapper.map(courseDTO, course);
        manageEntityMapping(course, courseDTO);
        course.setCid(courseId);
        Course savedCourse = courseRepo.save(course);
        CourseDTO dto = mapper.map(savedCourse, CourseDTO.class);
        manageDtoMapping(savedCourse, dto);

        return dto;
    }

    @Override
    public boolean deleteCourse(Long courseId) {
        if (!courseRepo.existsById(courseId))throw new APIException("Course Not Found",
                HttpStatus.NOT_FOUND,"Given id does not exists please check once");
        Course course=courseRepo.findById(courseId).orElseThrow();
        courseRepo.delete(course);
        return true;
    }

    @Override
    public CourseDTO assignTeacher(Long courseId, Long teacherId) {
        if (!courseRepo.existsById(courseId))throw new APIException("Course Not Found",
                HttpStatus.NOT_FOUND,"Given id does not exists please check once");
        if (!teacherRepo.existsById(teacherId))throw new APIException("Teacher Not Found",
                HttpStatus.NOT_FOUND,"Given id does not exists please check once");
        Course course=courseRepo.findById(courseId).orElseThrow();
        Teacher teacher=teacherRepo.findById(teacherId).orElseThrow();
        Set<Teacher> list=course.getTeachers();
        if (list==null){
            list=new HashSet<>();course.setTeachers(list);
        }
        list.add(teacher);
        Course c=courseRepo.save(course);
        CourseDTO dto=mapper.map(c,CourseDTO.class);manageDtoMapping(c,dto);
        return dto;
    }

    private void manageDtoMapping(Course course,CourseDTO courseDTO){
        if (course.getDepartment()!=null){
            courseDTO.setDepartmentId(course.getDepartment().getDepId());
        }

        if (!course.getTeachers().isEmpty()){
            courseDTO.setTeacherIds(new ArrayList<>());
            courseDTO.getTeacherIds().addAll(course.getTeachers().stream().map(Teacher::getTid).toList());
        }
    }
    private void manageEntityMapping(Course course,CourseDTO courseDTO){
        if (courseDTO.getDepartmentId()!=null){
            Department department=departmentRepo.findById(courseDTO.getDepartmentId()).orElseThrow(
                    ()->new APIException("Department Not Found",
                            HttpStatus.NOT_FOUND,"Given id does not exists please check once"));
            course.setDepartment(department);
        }
        if (courseDTO.getTeacherIds()!=null){
            course.getTeachers().addAll(courseDTO.getTeacherIds().stream().map(teacher -> {
                if (teacher!=null){
                    return teacherRepo.findById(teacher).orElseThrow(()->new APIException("Teacher Not Found",
                            HttpStatus.NOT_FOUND,"Given id does not exists please check once"));
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Null can't be teacher id");
            }).toList());
        }
    }
}
