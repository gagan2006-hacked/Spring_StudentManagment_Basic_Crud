package com.StudentManageApp.StudentManagment.Repo;

import com.StudentManageApp.StudentManagment.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher,Long> {
    boolean existsByEmail(String email);
    Teacher findByEmail(String email);
}
