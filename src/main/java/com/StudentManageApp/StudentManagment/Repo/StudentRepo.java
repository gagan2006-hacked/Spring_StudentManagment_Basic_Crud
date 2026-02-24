package com.StudentManageApp.StudentManagment.Repo;

import com.StudentManageApp.StudentManagment.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student,Long> {
    boolean existsByUsn(String usn);
    Student findByUsnIgnoreCase(String usn);
}
