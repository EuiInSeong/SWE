package com.commitfarm.farm.dto.ticket.response;

import com.commitfarm.farm.domain.enumClass.Priority;
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
public class AssignedTicketListRes {

    @Schema(description = "티켓 이름")
    private String title;

    @Schema(description = "티켓 우선 순위")
    private Priority priority;

    @Schema(description = "티켓 생성 시간")
    private String createdTime;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignedTicketList {
        private List<AssignedTicketListRes> assignedTickets;
    }
}
