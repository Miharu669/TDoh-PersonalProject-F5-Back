package dev.doel.TDoh.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;

import dev.doel.TDoh.task.task_exceptions.TaskNotFoundException;
import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserNotFoundException;
import dev.doel.TDoh.users.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Long getCurrentAuthenticatedUserId(Authentication authentication) {
        if (authentication == null) {
            throw new AccessDeniedException("No authenticated user");
        }

        String googleUserId = ((Jwt) authentication.getPrincipal()).getClaim("sub");
        User user = userRepository.findByGoogleId(googleUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user.getId();
    }

    public List<TaskDTO> getTasksForCurrentUser(Authentication authentication) {
        Long currentUserId = getCurrentAuthenticatedUserId(authentication);
        List<Task> tasks = taskRepository.findByUserId(currentUserId);

        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskByIdForCurrentUser(Long taskId, Authentication authentication) {
        Long currentUserId = getCurrentAuthenticatedUserId(authentication);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (task.getUser().getId() != currentUserId) {
            throw new AccessDeniedException("This task does not belong to the user");
        }

        return convertToDTO(task);
    }

    public TaskDTO createTask(TaskDTO taskDTO, Authentication authentication) {
        Long currentUserId = getCurrentAuthenticatedUserId(authentication);
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Task task = convertToEntity(taskDTO, user);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO, Authentication authentication) {
        Long currentUserId = getCurrentAuthenticatedUserId(authentication);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (task.getUser().getId() != currentUserId) {
            throw new AccessDeniedException("This task does not belong to the user");
        }

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDone(taskDTO.isDone());
        Task updatedTask = taskRepository.save(task);

        return convertToDTO(updatedTask);
    }

    public void deleteTask(Long taskId, Authentication authentication) {
        Long currentUserId = getCurrentAuthenticatedUserId(authentication);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (task.getUser().getId() != currentUserId) {
            throw new AccessDeniedException("This task does not belong to the user");
        }

        taskRepository.delete(task);
    }

    private TaskDTO convertToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .isDone(task.isDone())
                .build();
    }

    private Task convertToEntity(TaskDTO taskDTO, User user) {
        return Task.builder()
                .id(taskDTO.getId())
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .isDone(taskDTO.isDone())
                .user(user)
                .build();
    }
}
