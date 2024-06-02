package com.commitfarm.farm.dto.ticket.request;

import com.commitfarm.farm.domain.Milestone;
import com.commitfarm.farm.domain.Users;
import com.commitfarm.farm.domain.enumClass.Component;
import com.commitfarm.farm.domain.enumClass.Priority;
import com.commitfarm.farm.domain.enumClass.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketDto {

    @Schema(description = "마일스톤 ")
    private Milestone milestone;

    @Schema(description = "할당한 사람")
    private Users reporter;

    @Schema(description = "할당된 사람")
    private Users developer;

    @Schema(description = "Status")
    private Status status;

    @Schema(description = "우선 순위")
    private Priority priority;

    @Schema(description = "티켓 생성 시간")
    private LocalDateTime createdTime;

    @Schema(description = "수정 시간")
    private LocalDateTime modifiedTime;

    @Schema(description = "컴포넌트 타입")
    private Component component;

    @Schema(description = "티켓 설명")
    private String description;

    @Schema(description = "티켓 제목")
    private String title;
}
