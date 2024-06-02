package com.commitfarm.farm.dto.comment.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "댓글 작성 시간")
    private String timeStamp;
}

