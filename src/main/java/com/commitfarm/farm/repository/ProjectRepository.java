package com.commitfarm.farm.repository;

import com.commitfarm.farm.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByProjectId(Long projectId);
}
