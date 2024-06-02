package com.commitfarm.farm.dto.user;

import com.commitfarm.farm.domain.enumClass.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 회원 가입
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserReq {
    @Schema(description = "사용자 이름")
    private String username;
    @Schema(description = "사용자 비밀번호")
    private String password;
    @Schema(description = "사용자 이메일")
    private String email;
    @Schema(description = "사용자 역할(pl,dev,tester)")
    private UserType userType;
}
