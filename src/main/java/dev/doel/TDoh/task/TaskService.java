package dev.doel.TDoh.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import dev.doel.TDoh.minitask.MiniTaskDTO;
import dev.doel.TDoh.security.SecurityUser;
import dev.doel.TDoh.subtask.SubTaskDTO;
import dev.doel.TDoh.task.task_exceptions.TaskNotFoundException;
import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.user_exceptions.UserNotFoundException;
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

        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt) {
            String googleUserId = ((Jwt) principal).getClaim("sub");
            User user = userRepository.findByGoogleId(googleUserId)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            return user.getId();
        } else if (principal instanceof SecurityUser) {
            return ((SecurityUser) principal).getUser().getId();
        } else {
            throw new AccessDeniedException("Invalid authentication principal");
        }
    }

    public List<TaskDTO> getTasksForCurrentUser(Authentication authentication) {
        Long currentUserId = getCurrentAuthenticatedUserId(authentication);
        return taskRepository.findByUserId(currentUserId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskByIdForCurrentUser(Long taskId, Authentication authentication) {
        Task task = validateTaskOwnership(taskId, authentication);
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
        Task task = validateTaskOwnership(taskId, authentication);
        boolean wasDone = task.isDone();

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDone(taskDTO.isDone());

        Task updatedTask = taskRepository.save(task);

        if (!wasDone && taskDTO.isDone()) {
            addPointsToUser(task.getUser().getId(), 250);
        }

        return convertToDTO(updatedTask);
    }

    private void addPointsToUser(Long userId, int points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setScore(user.getScore() + points);
        userRepository.save(user);
    }

    public void deleteTask(Long taskId, Authentication authentication) {
        Task task = validateTaskOwnership(taskId, authentication);
        taskRepository.delete(task);
    }

    private Task validateTaskOwnership(Long taskId, Authentication authentication) {
        Long currentUserId = getCurrentAuthenticatedUserId(authentication);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (!Long.valueOf(task.getUser().getId()).equals(currentUserId)) {
            throw new AccessDeniedException("This task does not belong to the user");
        }

        return task;
    }

   private TaskDTO convertToDTO(Task task) {
    List<SubTaskDTO> subTaskDTOs = task.getSubTasks() != null
            ? task.getSubTasks().stream()
                    .map(subTask -> SubTaskDTO.builder()
                            .id(subTask.getId())
                            .title(subTask.getTitle())
                            .description(subTask.getDescription())
                            .isDone(subTask.isDone())
                            .taskId(task.getId())
                            .miniTasks(subTask.getMiniTasks() != null
                                    ? subTask.getMiniTasks().stream()
                                            .map(miniTask -> MiniTaskDTO.builder()
                                                    .id(miniTask.getId())
                                                    .title(miniTask.getTitle())
                                                    .description(miniTask.getDescription())
                                                    .isDone(miniTask.isDone())
                                                    .subTaskId(subTask.getId())
                                                    .build())
                                            .toList()
                                    : List.of()) 
                            .build())
                    .toList()
            : List.of();

    return TaskDTO.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .isDone(task.isDone())
            .userId(task.getUser().getId())
            .subTasks(subTaskDTOs)
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
