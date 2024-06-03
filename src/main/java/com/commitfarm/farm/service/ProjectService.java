package com.commitfarm.farm.service;


import com.commitfarm.farm.domain.Member;
import com.commitfarm.farm.domain.Project;
import com.commitfarm.farm.domain.Users;

import com.commitfarm.farm.dto.project.CreateProjectReq;
import com.commitfarm.farm.dto.project.ManageUserAccountReq;
import com.commitfarm.farm.dto.project.MemberListDto;
import com.commitfarm.farm.dto.project.ProjectListDto;
import com.commitfarm.farm.repository.MemberRepository;
import com.commitfarm.farm.repository.ProjectRepository;
import com.commitfarm.farm.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import java.util.Date;



@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UsersRepository usersRepository;

    @Transactional
    public void createProject(CreateProjectReq createProjectReq) throws Exception {

        // 현재 시간과 한 달 후 시간을 설정
        Date start = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(LocalDate.now().plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        Project project = new Project();
        project.setName(createProjectReq.getName());
        project.setStartDate(start);
        project.setEndDate(end);
        project.setDescription(createProjectReq.getDescription());

        projectRepository.save(project);

        for (ManageUserAccountReq manageUserAccountReq : createProjectReq.getManageUserAccounts()) {
            Users user = usersRepository.findByEmail(manageUserAccountReq.getUserEmail())
                    .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));

            Member member = new Member();
            member.setProject(project);
            member.setUser(user);
            member.setUserType(manageUserAccountReq.getUserType()); //추가
            memberRepository.save(member);
        }
    }
    @Transactional
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }


    @Transactional
    public List<ProjectListDto> readProjectList(Long userId) throws Exception {
        // 사용자가 존재하는지 확인
        if (!usersRepository.existsById(userId)) {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }


        List<Project> projects = projectRepository.findAllByUserId(userId);
        List<ProjectListDto> projectList = projects.stream()
                .map(project -> new ProjectListDto(project.getProjectId(),project.getName(), project.getEndDate()))
                .collect(Collectors.toList());
        return projectList;
    }
    @Transactional
    public List<MemberListDto> readMemberList(Long projectId) throws Exception {
        // 프로젝트가 존재하는지 확인

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new Exception("프로젝트를 찾을 수 없습니다."));

        List<Member> members = memberRepository.findAllByProject(project);

        List<MemberListDto> memberList = members.stream()
                .map(member -> new MemberListDto(member.getUser().getEmail(), member.getUserType()))
                .collect(Collectors.toList());
        return memberList;
    }

}