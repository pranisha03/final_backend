package com.example.Task_SpringBoot.services.admin;

import com.example.Task_SpringBoot.dto.CommentDTO;
import com.example.Task_SpringBoot.dto.ProjectDTO;
import com.example.Task_SpringBoot.dto.TaskDTO;
import com.example.Task_SpringBoot.dto.UserDto;

import java.util.List;

public interface AdminService {

    List<UserDto> getUsers();

    TaskDTO createTask(TaskDTO taskDTO);

    List<TaskDTO>getAllTasks();

    void deleteTask(Long id);

    TaskDTO updateTask(Long id,TaskDTO taskDTO);

    List<TaskDTO>searchTaskByTitle(String title);

    TaskDTO getTaskById(Long id);

    CommentDTO createComment(Long taskId, String content);

    List<CommentDTO> getCommentsByTaskId(Long taskId);

    List<ProjectDTO> getAllProjects();
    ProjectDTO getProjectById(Long id);
    ProjectDTO createProject(ProjectDTO projectDTO);
    ProjectDTO updateProject(ProjectDTO projectDTO);
    void deleteProject(Long id);

}
