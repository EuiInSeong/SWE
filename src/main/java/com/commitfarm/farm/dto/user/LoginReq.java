package com.commitfarm.farm.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginReq {
    @Schema(description = "로그인 아이디(이메일)")
    private String id;
    @Schema(description = "로그인 비밀번호")
    private String password;
}
