package com.StudentManageApp.StudentManagment.entity;

import com.StudentManageApp.StudentManagment.entity.Enum.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    @Column(nullable = false)
    private String teacherName;

    @Column(unique = true)
   private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phoneNo;

    @ManyToOne
    @JoinColumn(name = "department_id",nullable = false)
    private Department department;

    @ManyToMany(mappedBy = "teachers")
    private List<Course> courses=new ArrayList<>();

    public void addCourses(Course course){
        courses.add(course);
    }
}
