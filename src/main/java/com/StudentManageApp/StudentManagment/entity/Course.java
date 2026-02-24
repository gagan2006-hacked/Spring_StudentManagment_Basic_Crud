package com.StudentManageApp.StudentManagment.entity;

import com.StudentManageApp.StudentManagment.entity.Enum.CourseType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;

    @Column(nullable = false, unique = true)
    private String courseNumber;

    @Column(nullable = false)
    private String courseName;

    @Enumerated(EnumType.STRING)
    private CourseType valueOfCourse;

    @Column(nullable = false)
    private Integer courseCredits;

    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.DETACH},fetch = FetchType.EAGER)
    @JoinTable(
            name = "course_teacher_relation",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    ) 
   private Set<Teacher> teachers=new HashSet<>();

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "department_Id",nullable = false)
    private Department department;

    public void addTeachers(Teacher teacher){
        teachers.add(teacher);
    }
}
