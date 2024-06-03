package com.commitfarm.farm.repository;

import com.commitfarm.farm.domain.Member;
import com.commitfarm.farm.domain.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
}
