package com.StudentManageApp.StudentManagment.entity;

import com.StudentManageApp.StudentManagment.entity.Enum.SectionName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long secId;

    @Enumerated(EnumType.STRING)
    private SectionName sectionName;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Department department;

    @OneToMany(mappedBy = "section",cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    private List<Student> students=new ArrayList<>();
}
