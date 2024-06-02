package com.commitfarm.farm.repository;

import com.commitfarm.farm.domain.Comment;
import com.commitfarm.farm.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
