package com.example.Task_SpringBoot.dto;

import com.example.Task_SpringBoot.enums.ProjectStatus;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@Data
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private ProjectStatus projectStatus;
    private Long ownerId;
    private String ownerName;

}
