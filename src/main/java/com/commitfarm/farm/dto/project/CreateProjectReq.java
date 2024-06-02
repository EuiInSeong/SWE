package com.commitfarm.farm.dto.project;

import com.commitfarm.farm.dto.project.ManageUserAccountReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@Data
public class CreateProjectReq {

    @Schema(description = "프로젝트 이름")
    @NotBlank(message = "Project name is required")
    private String name;

    @Schema(description = "프로젝트 시작일")
    @NotNull(message = "Start date is required")
    private Date startDate;

    @Schema(description = "프로젝트 마감일")
    @NotNull(message = "End date is required")
    private Date endDate;

    @Schema(description = "프로젝트 설명")
    @NotBlank(message = "Description is required")
    private String description;

    @Schema(description = "admin이 추가한 사용자 목록")
    @NotNull(message = "User accounts list is required")
    private List<ManageUserAccountReq> manageUserAccounts;
}
