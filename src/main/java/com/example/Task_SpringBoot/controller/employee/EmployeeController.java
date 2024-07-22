package com.example.Task_SpringBoot.controller.employee;

import com.example.Task_SpringBoot.dto.CommentDTO;
import com.example.Task_SpringBoot.dto.ProjectDTO;
import com.example.Task_SpringBoot.dto.TaskDTO;
import com.example.Task_SpringBoot.services.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByUserId() {
        return ResponseEntity.ok(employeeService.getTasksByUserId());
    }

    @GetMapping("/task/{id}/{status}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @PathVariable String status){
        TaskDTO updatedTaskDTO=employeeService.updateTask(id, status);
        if(updatedTaskDTO == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @GetMapping("task/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getTaskById(id));
    }

    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long taskId, @RequestParam String content){
        CommentDTO createdCommentDTO =employeeService.createComment(taskId, content);
        if(createdCommentDTO==null)return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDTO);
    }

    @GetMapping("/comments/{taskId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTaskId(@PathVariable Long taskId){
        return ResponseEntity.ok(employeeService.getCommentsByTaskId(taskId));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getProjectsByUserId() {
        return ResponseEntity.ok(employeeService.getProjectsByUserId());
    }

    @GetMapping("/project/{projectId}/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(employeeService.getTasksByProjectId(projectId));
    }
}