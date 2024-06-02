package com.commitfarm.farm.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

//admin이 project를 만들 때 추가할 계정
@Getter
@Data
public class ManageUserAccountReq {
    private String projectName;
    private String userEmail;
    private String userRole;
}
