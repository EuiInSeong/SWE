package com.commitfarm.farm.repository;

import com.commitfarm.farm.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectId(Long projectId);

    @Query("SELECT p FROM Project p JOIN p.members m WHERE m.user.userId = :userId")
    List<Project> findAllByUserId(@Param("userId") Long userId);// 5/29
}