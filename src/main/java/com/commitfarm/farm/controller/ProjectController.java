package com.commitfarm.farm.controller;

import com.commitfarm.farm.dto.project.CreateProjectReq;
import com.commitfarm.farm.dto.project.MemberListDto;
import com.commitfarm.farm.dto.project.ProjectListDto;
import com.commitfarm.farm.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "프로젝트 API", description = "프로젝트 관련 API")// grouping by tag name
@RestController
@RequestMapping(value = "/api")
public class ProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create/project")
    @Operation(summary =  "프로젝트 생성 ", description = "멤버 리스트 추가 로직도 한번에, ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "200 으로 ok"
                    )
            }
    )
    public ResponseEntity<String> createProject(@RequestBody CreateProjectReq createProjectReq) {
        try {
            projectService.createProject(createProjectReq);
            return ResponseEntity.ok("프로젝트가 생성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }




    @GetMapping("/read/project-members/{projectId}")
    @Operation(summary = "For FrontEnd: 프로젝트 멤버 리스트 조회", description = "projectId로 프로젝트 내 멤버 리스트 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "프로젝트 내 멤버 리스트를 반환함."
                    )
            }
    )
    public ResponseEntity<List<MemberListDto>> readProjectMembers(@PathVariable Long projectId) {
        try {
            List<MemberListDto> memberListDto = projectService.readMemberList(projectId);
            return ResponseEntity.ok(memberListDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping("/read/project-list/{userId}")
    @Operation(summary =  "나 한테 할당된 플젝 리스트 별로 이름,Due Date만 반환 ", description = "프로젝트 세부 정보 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "200으로 ok"
                    )
            }
    )
    public ResponseEntity<List<ProjectListDto>> readProjectList(@PathVariable Long userId) {
        try {
            List<ProjectListDto> projectListDto = projectService.readProjectList(userId);
            return ResponseEntity.ok(projectListDto);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }



    // don't make project Delete APi -> not Used, Need

//    @DeleteMapping("/delete/project/{projectId}")
//    public Result<?> deleteProject(@PathVariable Long projectId){
//        return new Result<>("deleteProject");
//    }






    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
}