package com.commitfarm.farm.controller;



import com.commitfarm.farm.dto.comment.request.CreateCommentDTO;
import com.commitfarm.farm.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// Swagger read annotation @Schema @Parameter
@Tag(name = "댓글 API", description = "댓글 관련 API: 댓글은 티켓(이슈)에만 달 수 있다.")// grouping by tag name
@RestController
@RequestMapping(value = "/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create/comment/{usersId}/{ticketId}")
    @Operation(summary =  "댓글 생성 ", description = "userId : reporter , ticketId : ticketId로 해당 티켓에 댓글 생성",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "생성됨 메세지"
                    )
            }
    )
    public void createComment(@PathVariable Long usersId, @PathVariable Long ticketId , @RequestBody @RequestParam CreateCommentDTO dto){ //
       // leave comment -> only ticket not pro, milestone
        commentService.createComment(usersId, ticketId, dto);

    }




    @DeleteMapping("/delete/{commentId}")
    @Operation(summary =  "댓글 삭제 ", description = "userId : reporter (only reporter can delete), ticketId : ticketId로 해당 티켓에 댓글 생성",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "삭제 확인"
                    )
            }
    )

    public void deleteCommentResult(@PathVariable Long userId, @PathVariable Long commentId){
        commentService.deleteComment(userId,commentId);
    }










    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

}
