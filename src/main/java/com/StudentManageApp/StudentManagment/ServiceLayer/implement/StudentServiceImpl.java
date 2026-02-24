package com.StudentManageApp.StudentManagment.ServiceLayer.implement;

import com.StudentManageApp.StudentManagment.DTOs.StudentDTO;
import com.StudentManageApp.StudentManagment.DTOs.WebResponse;
import com.StudentManageApp.StudentManagment.Repo.CourseRepo;
import com.StudentManageApp.StudentManagment.Repo.DepartmentRepo;
import com.StudentManageApp.StudentManagment.Repo.SectionRepo;
import com.StudentManageApp.StudentManagment.Repo.StudentRepo;
import com.StudentManageApp.StudentManagment.ServiceLayer.Interfaces.StudentService;
import com.StudentManageApp.StudentManagment.entity.Course;
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
public class StudentServiceImpl implements StudentService {
    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;
    private final DepartmentRepo departmentRepo;
    private final SectionRepo sectionRepo;
    private final ModelMapper mapper;

    @Override
    public StudentDTO createStudent(StudentDTO student) {
        Long id=student.getSId();
        if (id!=null&&studentRepo.existsById(id))throw new APIException("Student Already Exists",HttpStatus.CONFLICT,"student with id already exists");
        Student s=mapper.map(student,Student.class);
        dtoToEntity(s,student);
        s=studentRepo.save(s);
        student=mapper.map(s,StudentDTO.class);
        entityToDto(s,student);
        return student;
    }

    private void entityToDto(Student s, StudentDTO student) {
//        entity -> Dto
        if (s.getSection()!=null){
            student.setSectionId(s.getSection().getSecId());
        }
        if (!s.getCourses().isEmpty()){
            student.setCourseIds(new ArrayList<>());
            student.getCourseIds().addAll(s.getCourses().stream().map(Course::getCid).toList());
        }
        if (s.getDepartment()!=null){
            student.setDepartmentId(s.getDepartment().getDepId());
        }
    }

    private void dtoToEntity(Student s, StudentDTO student) {
//        dto -> entity
        if (student.getDepartmentId()!=null){
            s.setDepartment(departmentRepo.findById(student.getDepartmentId()).orElseThrow(()->new APIException("Department not Found with "+"departmentId = "+student.getDepartmentId(),HttpStatus.NOT_FOUND,"Given id does not exists please check once")));
        }
        Long sec=student.getSectionId();
        if (sec!=null){
            s.setSection(sectionRepo.findById(sec).orElseThrow(()->new APIException
                    ("Section Not Found id = "+sec,HttpStatus.NOT_FOUND,"Given id does not exists please check once")));
        }
        List<Long>c=student.getCourseIds();
        if (c!=null){
            s.getCourses().addAll(c.stream().map(id-> courseRepo.findById(id).orElseThrow(
                    ()->new APIException("Course Not Found",
                            HttpStatus.NOT_FOUND,"Given id does not exists please check once"))
            ).toList());
        }
    }

    @Override
    public StudentDTO getStudentById(Long studentId) {
        if (studentRepo.existsById(studentId)){
             Student student=studentRepo.findById(studentId).orElseThrow(()->new APIException("Student Not Found",HttpStatus.NOT_FOUND,"Given id does not exists please check once"));
             StudentDTO dto=mapper.map(student,StudentDTO.class);
             entityToDto(student,dto);
             return dto;
        }
        throw new APIException("Student Not Found",HttpStatus.NOT_FOUND,"Given id does not exists please check once");
    }

    @Override
    public StudentDTO getStudentByUsn(String usn) {
        if (studentRepo.existsByUsn(usn)){
            Student student=studentRepo.findByUsnIgnoreCase(usn);
            StudentDTO dto=mapper.map(student,StudentDTO.class);
            entityToDto(student,dto);
            return dto;
        }
        throw new APIException("Student Not Found",HttpStatus.NOT_FOUND,"Given usn does not exists please check once");
    }

    @Override
    public WebResponse<StudentDTO> getAllStudents(Integer pageNumber,Integer pageSize,String sortOrder,String sortBy) {
        Sort sort=(sortOrder.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Student>page=studentRepo.findAll(pageable);
        List<Student>list=page.toList();
        if (list.isEmpty())throw new APIException("NO Student is Created",HttpStatus.BAD_REQUEST,"No Student is in Data base pls create Student");
        List<StudentDTO>dtos= list.stream().map((element) ->{
            StudentDTO dto=mapper.map(element, StudentDTO.class);
            entityToDto(element,dto);
            return dto;
        }).toList();
        return new WebResponse<>(dtos,"Successful",pageNumber,pageSize,page.getTotalElements(),page.getTotalPages(),page.isLast());
    }

    @Override
    public StudentDTO updateStudent(Long studentId, StudentDTO student) {
        if (!studentRepo.existsById(studentId))throw new APIException("Student Not Found",HttpStatus.NOT_FOUND,"Given id does not exists please check once");
        student.setSId(studentId);
        Student s=mapper.map(student,Student.class);
        dtoToEntity(s,student);
        s=studentRepo.save(s);
        student=mapper.map(s,StudentDTO.class);
        entityToDto(s,student);
        return student;
    }

    @Override
    public boolean deleteStudent(Long studentId) {
        if (!studentRepo.existsById(studentId))throw new APIException("Student Not Found",HttpStatus.NOT_FOUND,"Given id does not exists please check once");
        studentRepo.delete(studentRepo.findById(studentId).orElseThrow());
        return true;
    }

    @Override
    public StudentDTO assignCourseByCourseNumber(Long studentId,String... courseNumber) {
        if (courseNumber.length<1)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Course Number Was Found");
        for (int i = 0; i <courseNumber.length; i++) {
            if (!courseRepo.existsByCourseNumber(courseNumber[i]))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Course Not Found");
            if (!studentRepo.existsById(studentId))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Student Not Found");
            Course course=courseRepo.findByCourseNumber(courseNumber[i]).orElseThrow();
            Student student=studentRepo.findById(studentId).orElseThrow();
            List<Course>list=student.getCourses();
            if (list==null){
                list=new ArrayList<>();
                student.setCourses(list);
            }
            list.add(course);
        }
         Student s=studentRepo.findById(studentId).orElseThrow();
         StudentDTO student=mapper.map(s,StudentDTO.class);
         entityToDto(s,student);
        return student;
    }

}
