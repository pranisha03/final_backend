package com.example.Task_SpringBoot.services.admin;

import com.example.Task_SpringBoot.dto.CommentDTO;
import com.example.Task_SpringBoot.dto.ProjectDTO;
import com.example.Task_SpringBoot.dto.TaskDTO;
import com.example.Task_SpringBoot.dto.UserDto;
import com.example.Task_SpringBoot.entities.Comment;
import com.example.Task_SpringBoot.entities.Project;
import com.example.Task_SpringBoot.entities.Task;
import com.example.Task_SpringBoot.entities.User;
import com.example.Task_SpringBoot.enums.TaskStatus;
import com.example.Task_SpringBoot.enums.UserRole;
import com.example.Task_SpringBoot.repository.CommentRepository;
import com.example.Task_SpringBoot.repository.ProjectRepository;
import com.example.Task_SpringBoot.repository.TaskRepository;
import com.example.Task_SpringBoot.repository.UserRepository;
import com.example.Task_SpringBoot.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final JwtUtil jwtUtil;

    private final CommentRepository commentRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().filter(user -> user.getUserRole()== UserRole.EMPLOYEE)
                .map(User::getUserDto)
                .collect(Collectors.toList());

    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Optional<User> optionalUser=userRepository.findById(taskDTO.getEmployeeId());
        Optional<Project> optionalProject=projectRepository.findById(taskDTO.getProjectId());
        if(optionalUser.isPresent()){
            Task task=new Task();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setPriority(taskDTO.getPriority());
            task.setDueDate(taskDTO.getDueDate());
            task.setTaskStatus(TaskStatus.INPROGRESS);
            task.setUser(optionalUser.get());
            task.setProject(optionalProject.get());
            return taskRepository.save(task).getTaskDTO();

        }
        return null;
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);

    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Optional<Task> optionalTask=taskRepository.findById(id);
        return optionalTask.map(Task::getTaskDTO).orElse(null);
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> optionalTask=taskRepository.findById(id);
        Optional<User> optionalUser =userRepository.findById(taskDTO.getEmployeeId());
        if(optionalTask.isPresent()&& optionalUser.isPresent()){
            Task existingTask=optionalTask.get();
            existingTask.setTitle(taskDTO.getTitle());
            existingTask.setDescription(taskDTO.getDescription());
            existingTask.setDueDate(taskDTO.getDueDate());
            existingTask.setPriority(taskDTO.getPriority());
            existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDTO.getTaskStatus())));
            existingTask.setUser(optionalUser.get());
            return taskRepository.save(existingTask).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDTO> searchTaskByTitle(String title) {
        return taskRepository.findAllByTitleContaining(title)
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO createComment(Long taskId, String content) {
        Optional<Task> optionalTask = taskRepository.findById((taskId));
        User user = jwtUtil.getLoggedInUser();
        if(optionalTask.isPresent() && user != null){
            Comment comment = new Comment();
            comment.setCreatedAt(new Date());
            comment.setContent(content);
            comment.setTask(optionalTask.get());
            comment.setUser(user);
            return commentRepository.save(comment).getCommentDTO();
        }
        throw new EntityNotFoundException("User or Task not found");
    }

    @Override
    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        return commentRepository.findAllByTaskId(taskId).stream().map(Comment::getCommentDTO).collect(Collectors.toList());
    }
    private TaskStatus mapStringToTaskStatus(String status){
        return switch(status){
            case "PENDING" -> TaskStatus.PENDING;
            case "INPROGRESS" -> TaskStatus.INPROGRESS;
            case "COMPLETED" ->TaskStatus.COMPLETED;
            case "DEFERRED" ->TaskStatus.DEFERRED;
            default -> TaskStatus.CANCELLED;
        };
    }
    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow();
        return convertToDTO(project);
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {

            if (projectDTO == null) {
                throw new IllegalArgumentException("ProjectDTO cannot be null");
            }

            Long ownerId = projectDTO.getOwnerId();
            if (ownerId == null) {
                throw new IllegalArgumentException("Owner ID cannot be null");
            }

            Optional<User> optionalUser = userRepository.findById(ownerId);
            if (!optionalUser.isPresent()) {
                throw new EntityNotFoundException("User not found");
            }

            User owner = optionalUser.get();

            Project project = new Project();
            project.setTitle(projectDTO.getTitle());
            project.setDescription(projectDTO.getDescription());
            project.setStartDate(projectDTO.getStartDate());
            project.setEndDate(projectDTO.getEndDate());

            if (projectDTO.getOwnerId() != null) {
                project.setOwner(owner);
            }

            return convertToDTO(projectRepository.save(project));
        }

    @Override
    public ProjectDTO updateProject(ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectDTO.getId()).orElseThrow();
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        User owner = userRepository.findById(projectDTO.getOwnerId()).orElseThrow();
        project.setOwner(owner);
        project = projectRepository.save(project);
        return convertToDTO(project);
    }


    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setTitle(project.getTitle());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEndDate(project.getEndDate());
        projectDTO.setOwnerId(project.getOwner().getId());
        projectDTO.setOwnerName(project.getOwner().getName());
        return projectDTO;
    }
}
