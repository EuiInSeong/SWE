package com.commitfarm.farm.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectListDto {

    @Schema(description = "프로젝트 아이디")
    private Long projectId;
    @Schema(description = "프로젝트 이름")
    private String name;

    @Schema(description = "프로젝트 마감일")
    private Date dueDate;
}