package com.StudentManageApp.StudentManagment.entity;

import com.StudentManageApp.StudentManagment.entity.Enum.DepartmentName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private DepartmentName departmentName;

    @OneToMany(mappedBy = "department",cascade = {CascadeType.MERGE,CascadeType.REMOVE})
   private List<Student> students=new ArrayList<>();

    @OneToMany(mappedBy = "department",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
   private List<Course> courses=new ArrayList<>();

    @OneToMany(mappedBy = "department",cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    private List<Teacher> teachers=new ArrayList<>();

    @OneToMany(mappedBy = "department",cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE})
    private List<Section> sections=new ArrayList<>();
}
