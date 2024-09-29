package dev.doel.TDoh.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.doel.TDoh.minitask.MiniTaskDTO;
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

    // Obtiene todas las tareas (sin autenticación)
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtiene una tarea por ID (sin autenticación)
    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return convertToDTO(task);
    }

    // Crea una nueva tarea (sin autenticación)
    public TaskDTO createTask(TaskDTO taskDTO) {
        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Task task = convertToEntity(taskDTO, user);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    // Actualiza una tarea existente (sin autenticación)
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

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

    // Agrega puntos a un usuario
    private void addPointsToUser(Long userId, int points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setScore(user.getScore() + points);
        userRepository.save(user);
    }

    // Elimina una tarea por ID (sin autenticación)
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        taskRepository.delete(task);
    }

    // Conversión de Task a TaskDTO
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

    // Conversión de TaskDTO a Task
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
