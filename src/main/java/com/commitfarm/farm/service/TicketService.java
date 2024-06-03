package com.commitfarm.farm.service;

import com.commitfarm.farm.domain.*;
import com.commitfarm.farm.domain.enumClass.*;
import com.commitfarm.farm.dto.ticket.request.CreateTicketDto;
import com.commitfarm.farm.dto.ticket.request.UpdateAssignReq;
import com.commitfarm.farm.dto.ticket.request.UpdateStatusReq;
import com.commitfarm.farm.dto.ticket.response.*;
import com.commitfarm.farm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.commitfarm.farm.domain.enumClass.UserType.Developer;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MilestoneRepository milestoneRepository;


    private static final EnumSet<Status> PROJECT_LEADER_STATUSES = EnumSet.of(Status.Assigned, Status.Closed);
    private static final EnumSet<Status> DEVELOPER_STATUSES = EnumSet.of(Status.Resolved);
    private static final EnumSet<Status> TESTER_STATUSES = EnumSet.of(Status.New, Status.Reopened);
    @Transactional
    public void createTicket(Long projectId, Long userId, CreateTicketDto ticketDTO) {
        // Find the reporter user by email
        Users reporter = usersRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        // Set the milestone
        Milestone milestone = new Milestone();
        LocalDate startDate = LocalDateTime.now().toLocalDate();
        LocalDate endDate = LocalDate.now().plusMonths(1);
        milestone.setStartDate(startDate);
        milestone.setDueDate(endDate);
        milestone.setDescription("milestone description");
        milestone.setName(ticketDTO.getMilestoneName());

        // Save the milestone
        Milestone savedMilestone = milestoneRepository.save(milestone);

        // Set the ticket
        Ticket ticket = new Ticket();
        ticket.setMilestone(savedMilestone);
        ticket.setReporter(reporter);
        ticket.setStatus(Status.New); // Always set to New when created
        ticket.setPriority(ticketDTO.getPriority());
        ticket.setCreatedTime(LocalDateTime.now());
        ticket.setModifiedTime(LocalDateTime.now());
        ticket.setComponent(ticketDTO.getComponent());
        ticket.setDescription(ticketDTO.getTicketDescription());
        ticket.setTitle(ticketDTO.getTicketTitle());
        ticket.setProject(projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 프로젝트를 찾을 수 없습니다.")));

        // Find the developers in the project
        Project project = ticket.getProject();
        List<Member> developers = memberRepository.findAllByProjectAndUserType(project, UserType.Developer);
        if (developers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 프로젝트에 개발자가 없습니다.");
        }

        // Find the developer to assign the ticket to
        Optional<Users> assignedDeveloper = developers.stream()
                .collect(Collectors.groupingBy(Member::getUser, Collectors.summingInt(dev -> (int) ticketRepository.countByComponentAndDeveloper(ticketDTO.getComponent(), dev.getUser()))))
                .entrySet().stream()
                .sorted((entry1, entry2) -> {
                    int cmp = entry2.getValue().compareTo(entry1.getValue());
                    if (cmp == 0) {
                        long dev1AssignedCount = ticketRepository.countByDeveloperAndStatus(entry1.getKey(), Status.Assigned);
                        long dev2AssignedCount = ticketRepository.countByDeveloperAndStatus(entry2.getKey(), Status.Assigned);
                        cmp = Long.compare(dev1AssignedCount, dev2AssignedCount);
                    }
                    if (cmp == 0) {
                        cmp = entry1.getKey().getUserId().compareTo(entry2.getKey().getUserId());
                    }
                    return cmp;
                })
                .map(Map.Entry::getKey)
                .findFirst();

        // Assign the developer to the ticket
        assignedDeveloper.ifPresent(ticket::setDeveloper);

        // Save the ticket
        ticketRepository.save(ticket);
    }
    private static class DeveloperStats {
        Member member;
        int hitCount;
        int busyCount;

        public DeveloperStats(Member member, int hitCount, int busyCount) {
            this.member = member;
            this.hitCount = hitCount;
            this.busyCount = busyCount;
        }
    }


//    @Transactional(readOnly = true)
//    public TicketListRes readTicketList(Long projectId, Long userId) {
//        // join MemberTable to check usertype (-오슬희)
//        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
//
//
//        UserType actor = member.getUserType();
//
//        List<TicketListRes.TicketInfo> tickets = switch (actor) {
//            case ProjectLeader -> ticketRepository.findByProject_ProjectId(projectId)
//                    .stream()
//                    .map(ticket -> new TicketListRes.TicketInfo(
//                            ticket.getTitle(),
//                            ticket.getStatus().toString(),
//                            ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                    ))
//                    .collect(Collectors.toList());
//            case Developer -> ticketRepository.findByProject_ProjectIdAndDeveloper_UserId(projectId, userId)
//                    .stream()
//                    .map(ticket -> new TicketListRes.TicketInfo(
//                            ticket.getTitle(),
//                            ticket.getStatus().toString(),
//                            ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                    ))
//                    .collect(Collectors.toList());
//            case Tester -> ticketRepository.findByProject_ProjectIdAndDeveloper_UserId(projectId, userId)
//                    .stream()
//                    .map(ticket -> new TicketListRes.TicketInfo(
//                            ticket.getTitle(),
//                            ticket.getStatus().toString(),
//                            ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                    ))
//                    .collect(Collectors.toList());
//        };
//
//
//        // Get: Assigned ticket List - (오슬희)
//        List<TicketListRes.TicketInfo> assignedTickets =
//                ticketRepository.findByProject_ProjectIdAndDeveloper_UserIdAndStatus(projectId, userId, Status.Assigned)
//                .stream()
//                .map(ticket -> new TicketListRes.TicketInfo(
//                        ticket.getTitle(),
//                        ticket.getStatus().toString(),
//                        ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                ))
//                .collect(Collectors.toList());
//
//        // Get: Closed ticket List -(오슬희)
//        List<TicketListRes.TicketInfo> closedTickets =
//                ticketRepository.findByProject_ProjectIdAndDeveloper_UserIdAndStatus(projectId, userId, Status.Closed)
//                .stream()
//                .map(ticket -> new TicketListRes.TicketInfo(
//                        ticket.getTitle(),
//                        ticket.getStatus().toString(),
//                        ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                ))
//                .collect(Collectors.toList());
//
//        return new TicketListRes(assignedTickets, closedTickets);
//    }

    @Transactional(readOnly = true)
    public List<AssignedTicketListRes> readAssignedTicketList(Long projectId, Long userId) {
        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        UserType userType = member.getUserType();
        if (userType != Developer && userType != UserType.ProjectLeader ) {
            throw new IllegalArgumentException("해당 사용자는 권한이 없습니다.");
        }

        List<Ticket> assignedTickets = ticketRepository.findByProject_ProjectIdAndStatus(projectId, Status.Assigned);
        List <AssignedTicketListRes> ticketListRes = assignedTickets.stream()
                .map(ticket -> new AssignedTicketListRes(
                        ticket.getTicketId(),
                        ticket.getTitle(),
                        ticket.getPriority(),
                        ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
                .collect(Collectors.toList());


        return ticketListRes;
    }
    @Transactional(readOnly = true)
    public List<NewTicketListRes> readNewTicketList(Long projectId, Long userId) {
        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        UserType userType = member.getUserType();
        if (userType != Developer && userType != UserType.ProjectLeader ) {
            throw new IllegalArgumentException("해당 사용자는 권한이 없습니다.");
        }

        List<Ticket> newTickets = ticketRepository.findByProject_ProjectIdAndStatus(projectId, Status.New);

        List<NewTicketListRes> newTicketList = newTickets.stream()
                .map(ticket -> new NewTicketListRes(
                        ticket.getTicketId(),
                        ticket.getTitle(),
                        ticket.getPriority(),
                        ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
                .collect(Collectors.toList());

        return newTicketList;
    }

    @Transactional(readOnly = true)
    public List<ResolvedTicketListRes> readResolvedTicketList(Long projectId, Long userId) {
        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        UserType userType = member.getUserType();
        if (userType != Developer && userType != UserType.ProjectLeader ) {
            throw new IllegalArgumentException("해당 사용자는 권한이 없습니다.");
        }

        List<Ticket> ticketList = ticketRepository.findByProject_ProjectIdAndStatus(projectId, Status.Resolved);
        List<ResolvedTicketListRes> resolvedTicketList = ticketList.stream()
                .map(ticket -> new ResolvedTicketListRes(
                        ticket.getTicketId(),
                        ticket.getTitle(),
                        ticket.getPriority(),
                        ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
                .collect(Collectors.toList());

        return resolvedTicketList;
    }
    @Transactional(readOnly = true)
    public List<ReopenedTicketListRes> readReopenedTicketList(Long projectId, Long userId) {
        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        UserType userType = member.getUserType();
        if (userType != Developer && userType != UserType.ProjectLeader ) {
            throw new IllegalArgumentException("해당 사용자는 권한이 없습니다.");
        }

        List<Ticket> ticketList = ticketRepository.findByProject_ProjectIdAndStatus(projectId, Status.Reopened);
        List<ReopenedTicketListRes> reopenedTicketListRes = ticketList.stream()
                .map(ticket -> new ReopenedTicketListRes(
                        ticket.getTicketId(),
                        ticket.getTitle(),
                        ticket.getPriority(),
                        ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
                .collect(Collectors.toList());

        return reopenedTicketListRes;
    }
    @Transactional(readOnly = true)
    public List<ClosedTicketListRes> readClosedTicketList(Long projectId, Long userId) {
        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        UserType userType = member.getUserType();
        if (userType != Developer && userType != UserType.ProjectLeader ) {
            throw new IllegalArgumentException("해당 사용자는 권한이 없습니다.");
        }

        List<Ticket> ticketList = ticketRepository.findByProject_ProjectIdAndStatus(projectId, Status.Closed);
        List<ClosedTicketListRes> closedTicketListRes = ticketList.stream()
                .map(ticket -> new ClosedTicketListRes(
                        ticket.getTicketId(),
                        ticket.getTitle(),
                        ticket.getPriority(),
                        ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
                .collect(Collectors.toList());

        return closedTicketListRes;
    }

    @Transactional
    public DetailTicketRes readDetailTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓을 찾을 수 없습니다. ID: " + ticketId));

        List<DetailTicketRes.CommentResponse> comments = ticket.getComments().stream()
                .map(comment -> new DetailTicketRes.CommentResponse(
                        comment.getContent(),
                        comment.getTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
                .collect(Collectors.toList());


        return new DetailTicketRes(
                ticket.getDescription(),
                ticket.getStatus().toString(),
                ticket.getPriority().toString(),
                ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                ticket.getModifiedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                ticket.getComponent().toString(),
                ticket.getDeveloper().getUsername(),
                ticket.getReporter().getUsername(),
                ticket.getMilestone().getName(),
                comments
        );
    }

    @Transactional(readOnly = true)
    public StaticsRes readTicketStatics(Long projectId) {
        //read ticket that created today and this month per List -(오슬희)
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1);
        //get this month info from now()-(오슬희)
        YearMonth thisMonth = YearMonth.now();
        LocalDateTime startOfMonth = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = thisMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Ticket> todayTickets = ticketRepository.findByProject_ProjectIdAndCreatedTimeBetween(projectId, startOfToday, endOfToday);
        List<Ticket> monthTickets = ticketRepository.findByProject_ProjectIdAndCreatedTimeBetween(projectId, startOfMonth, endOfMonth);

        Map<Status, Long> todayStatusCount = todayTickets.stream().collect(Collectors.groupingBy(Ticket::getStatus, Collectors.counting()));
        Map<Priority, Long> todayPriorityCount = todayTickets.stream().collect(Collectors.groupingBy(Ticket::getPriority, Collectors.counting()));

        Map<Status, Long> monthStatusCount = monthTickets.stream().collect(Collectors.groupingBy(Ticket::getStatus, Collectors.counting()));
        Map<Priority, Long> monthPriorityCount = monthTickets.stream().collect(Collectors.groupingBy(Ticket::getPriority, Collectors.counting()));

        return new StaticsRes(todayStatusCount, todayPriorityCount, monthStatusCount, monthPriorityCount);
    }
    @Transactional
    public String updateTicketStatus(Long projectId, Long ticketId, Long userId, UpdateStatusReq dto) {
        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        //have to join member table to get user type so, you can check the user Authority -(오슬희)
        UserType userType = member.getUserType();
        Status newStatus = dto.getStatus();

        boolean isStatusChangeAllowed = switch (userType) {
            case ProjectLeader -> PROJECT_LEADER_STATUSES.contains(newStatus);
            case Developer -> DEVELOPER_STATUSES.contains(newStatus);
            case Tester -> TESTER_STATUSES.contains(newStatus);
        };

        if (!isStatusChangeAllowed) {
            return "권한이 없습니다.";
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓을 찾을 수 없습니다."));

        ticket.setStatus(newStatus);
        ticketRepository.save(ticket);

        return "상태 변경 성공";
    }
    @Transactional
    public String updateAssignedDeveloper(Long projectId, Long ticketId, Long userId, UpdateAssignReq dto) {
        Member projectLeader = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        UserType userType = projectLeader.getUserType();
        if (userType != UserType.ProjectLeader) {
            throw new IllegalArgumentException("해당 사용자는 권한이 없습니다.");
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓을 찾을 수 없습니다."));

        Users assignedUser = usersRepository.findByEmail(dto.getDevEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        Member developer = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, assignedUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        if (developer.getUserType() != UserType.Developer) {
            throw new IllegalArgumentException("해당 사용자는 개발자가 아닙니다.");
        }

        ticket.setDeveloper(assignedUser);
        ticketRepository.save(ticket);

        return "담당 개발자 변경 성공";
    }


    @Transactional
    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }





}
