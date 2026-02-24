package com.StudentManageApp.StudentManagment.Repo;

import com.StudentManageApp.StudentManagment.entity.Enum.SectionName;
import com.StudentManageApp.StudentManagment.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepo  extends JpaRepository<Section,Long> {
    boolean existsBySectionName(SectionName sectionName);
}
