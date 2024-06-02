package com.commitfarm.farm.repository;

import com.commitfarm.farm.domain.Ticket;
import com.commitfarm.farm.domain.Users;
import com.commitfarm.farm.domain.enumClass.Component;
import com.commitfarm.farm.domain.enumClass.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countByComponentAndDeveloper(Component component, Users developer);
    long countByDeveloperAndStatus(Users developer, Status status);
    List<Ticket> findByProject_ProjectIdAndDeveloper_UserIdAndStatus(Long projectId, Long userId, Status status);
    // used to get ticket List api
    List<Ticket> findByProject_ProjectIdAndCreatedTimeBetween(Long projectId, LocalDateTime start, LocalDateTime end);

    List<Ticket> findByProject_ProjectIdAndStatus(Long projectId, Status status);
    List<Ticket> findByProject_ProjectIdAndDeveloper_UserId(Long projectId, Long userId);
    List<Ticket> findByProject_ProjectId(Long projectId);


}
