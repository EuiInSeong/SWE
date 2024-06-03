package com.commitfarm.farm.dto.project;

import com.commitfarm.farm.domain.enumClass.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor


public class MemberListDto {

    @Schema(description = "멤버 email")
    private String email;
    @Schema(description = "멤버 역할")
    private UserType userType;

}
