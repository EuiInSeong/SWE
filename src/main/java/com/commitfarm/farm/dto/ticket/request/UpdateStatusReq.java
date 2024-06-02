package com.commitfarm.farm.dto.ticket.request;

import com.commitfarm.farm.domain.enumClass.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusReq {

    @Schema(description = "새로운 티켓 상태")
    private Status status;
}
