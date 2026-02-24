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
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sId;

    @Column(nullable = false)
    private String studentName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String usn;

    @ManyToMany
    @Column(nullable = false)
    private List<Course> courses=new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false)
    private Section section;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Department department;
}
