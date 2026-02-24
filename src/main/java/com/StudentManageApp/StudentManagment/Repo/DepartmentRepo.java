package com.StudentManageApp.StudentManagment.Repo;

import com.StudentManageApp.StudentManagment.entity.Department;
import com.StudentManageApp.StudentManagment.entity.Enum.DepartmentName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long> {
    boolean existsByDepartmentName(DepartmentName departmentName);
    Optional<Department> findByDepartmentName(DepartmentName departmentName);
}
