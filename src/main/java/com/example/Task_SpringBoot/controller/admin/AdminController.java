package com.example.Task_SpringBoot.controller.admin;

import com.example.Task_SpringBoot.dto.CommentDTO;
import com.example.Task_SpringBoot.dto.ProjectDTO;
import com.example.Task_SpringBoot.dto.TaskDTO;
import com.example.Task_SpringBoot.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
           return ResponseEntity.ok(adminService.getUsers());
    }
    @PostMapping("/task")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO){
        TaskDTO createdTaskDto =adminService.createTask(taskDTO);
        if(createdTaskDto==null)return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDto);
    }
    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(adminService.getAllTasks());
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        adminService.deleteTask(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("task/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id){
     return ResponseEntity.ok(adminService.getTaskById(id));
    }
    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id,@RequestBody TaskDTO taskDTO)
    {
        TaskDTO updatedTask=adminService.updateTask(id,taskDTO);
        if (updatedTask==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/tasks/search/{title}")
    public ResponseEntity<List<TaskDTO>> searchTask(@PathVariable String title){
        return ResponseEntity.ok(adminService.searchTaskByTitle(title));
    }

    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long taskId, @RequestParam String content){
        CommentDTO createdCommentDTO =adminService.createComment(taskId, content);
        if(createdCommentDTO==null)return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDTO);
    }

    @GetMapping("/comments/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable Long taskId){
        return ResponseEntity.ok(adminService.getCommentsByTaskId(taskId));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = adminService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("project/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = adminService.getProjectById(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PostMapping("/project")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        ProjectDTO createdProject = adminService.createProject(projectDTO);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("project/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        projectDTO.setId(id);
        ProjectDTO updatedProject = adminService.updateProject(projectDTO);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }
    @DeleteMapping("/project/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        adminService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
