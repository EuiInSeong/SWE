package com.commitfarm.farm.dto.project;

import com.commitfarm.farm.domain.enumClass.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

//admin이 project를 만들 때 추가할 계정
@Getter
@Data
public class ManageUserAccountReq {

    private String userEmail;
    private UserType userType;
}


