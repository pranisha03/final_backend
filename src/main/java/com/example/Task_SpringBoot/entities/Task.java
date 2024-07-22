package com.example.Task_SpringBoot.entities;

import com.example.Task_SpringBoot.dto.TaskDTO;
import com.example.Task_SpringBoot.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Date dueDate;
    private String priority;
    private TaskStatus taskStatus;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="user_id",nullable=false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="project_id",nullable=false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JsonIgnore
    private Project project;

    // getters and setters

    public Task(String title, String description, Date dueDate, String priority, TaskStatus taskStatus, User user, Project project) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.taskStatus = taskStatus;
        this.user = user;
        this.project = project;
    }

    public Task() {}

    public TaskDTO getTaskDTO(){
        TaskDTO taskDto=new TaskDTO();
        taskDto.setId(id);
        taskDto.setTitle(title);
        taskDto.setDescription(description);
        taskDto.setEmployeeName(user.getName());
        taskDto.setEmployeeId(user.getId());
        taskDto.setTaskStatus(taskStatus);
        taskDto.setDueDate(dueDate);
        taskDto.setPriority(priority);
        taskDto.setProjectId(project.getId());

        return taskDto;
    }
}



