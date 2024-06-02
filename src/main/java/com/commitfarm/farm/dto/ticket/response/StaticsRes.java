package com.commitfarm.farm.dto.ticket.response;


import com.commitfarm.farm.domain.enumClass.Priority;
import com.commitfarm.farm.domain.enumClass.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor

public class StaticsRes {

    @Schema(description = "오늘 생성된 티켓의 상태별 개수")
    private Map<Status, Long> todayStatusCount;

    @Schema(description = "오늘 생성된 티켓의 우선순위별 개수")
    private Map<Priority, Long> todayPriorityCount;

    @Schema(description = "이번 달 생성된 티켓의 상태별 개수")
    private Map<Status, Long> monthStatusCount;

    @Schema(description = "이번 달 생성된 티켓의 우선순위별 개수")
    private Map<Priority, Long> monthPriorityCount;
}
