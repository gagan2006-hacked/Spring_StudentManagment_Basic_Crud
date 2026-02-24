package com.StudentManageApp.StudentManagment.Repo;

import com.StudentManageApp.StudentManagment.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepo extends JpaRepository<Course,Long> {
    boolean existsByCourseNumber(String courseNumber);
    Optional<Course> findByCourseNumber(String courseNumber);
}
