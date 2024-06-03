package com.commitfarm.farm.controller;

import com.commitfarm.farm.domain.Users;
import com.commitfarm.farm.dto.user.CreateUserReq;
import com.commitfarm.farm.dto.user.LoginReq;
import com.commitfarm.farm.dto.user.LoginRes;
import com.commitfarm.farm.service.UsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/users")
@Tag(name = "유저 API", description = "로그인과 회원 가입")// grouping by tag name
public class UserController {

    @Autowired
    private UsersService userService;


    @PostMapping("/login")
    public Result<LoginRes> login(@RequestBody LoginReq loginReq) {
            LoginRes loginRes = userService.login(loginReq);
            return new Result<>(loginRes);

    }


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody CreateUserReq createUserReq) {
        try {
            userService.createUser(createUserReq);
            return ResponseEntity.ok("회원가입 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
}