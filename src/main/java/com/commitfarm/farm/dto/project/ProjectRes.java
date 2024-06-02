package com.commitfarm.farm.dto.project;

import lombok.*;

import java.util.Date;

//projectList의 프로젝트들 in AllProjectRes
@Getter
@Data
public class ProjectRes {

    private String name;
    private Date startDate;
    private Date endDate;

}
