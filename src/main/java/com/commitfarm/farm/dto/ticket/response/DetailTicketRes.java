package com.commitfarm.farm.dto.ticket.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailTicketRes {

    @Schema(description = "티켓 설명")
    private String description;

    @Schema(description = "티켓 상태")
    private String status;

    @Schema(description = "티켓 우선순위")
    private String priority;

    @Schema(description = "티켓 생성 시간")
    private String createdTime;

    @Schema(description = "티켓 수정 시간")
    private String modifiedTime;

    @Schema(description = "티켓 컴포넌트")
    private String component;

    @Schema(description = "티켓 담당자")
    private String developer;

    @Schema(description = "티켓 작성자")
    private String reporter;

    @Schema(description = "티켓 마일스톤")
    private String milestone;

    @Schema(description = "티켓 댓글 리스트")
    private List<CommentResponse> comments;

    @Getter
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentResponse {

        @Schema(description = "댓글 내용")
        private String content;

        @Schema(description = "댓글 작성 시간")
        private String timeStamp;
    }
}
