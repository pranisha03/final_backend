package com.example.Task_SpringBoot.entities;

import com.example.Task_SpringBoot.dto.ProjectDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    // Custom getter for DTO conversion
    public ProjectDTO getProjectDTO() {
        ProjectDTO projectDto = new ProjectDTO();
        projectDto.setId(this.id);
        projectDto.setTitle(this.title);
        projectDto.setDescription(this.description);
        projectDto.setStartDate(this.startDate);
        projectDto.setEndDate(this.endDate);
        projectDto.setOwnerName(this.owner.getName());
        projectDto.setOwnerId(this.owner.getId());
        return projectDto;
    }
}
