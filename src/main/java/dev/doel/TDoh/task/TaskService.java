package dev.doel.TDoh.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// import dev.doel.TDoh.minitask.MiniTaskDTO;
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

    public List<TaskDTO> getAllTasksForUser(Long userId) {
        return taskRepository.findAllByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO getTaskByIdForUser(Long taskId, Long userId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found for the user"));
        return convertToDTO(task);
    }

    public TaskDTO createTaskForUser(TaskDTO taskDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Task task = convertToEntity(taskDTO, user);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    public TaskDTO updateTaskForUser(Long taskId, TaskDTO taskDTO, Long userId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found for the user"));

        boolean wasDone = task.isDone();

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDone(taskDTO.isDone());

        Task updatedTask = taskRepository.save(task);

        if (!wasDone && taskDTO.isDone()) {
            addPointsToUser(userId, 250);
        }

        return convertToDTO(updatedTask);
    }

    public void deleteTaskForUser(Long taskId, Long userId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found for the user"));
        taskRepository.delete(task);
    }

    private void addPointsToUser(Long userId, int points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setScore(user.getScore() + points);
        userRepository.save(user);
    }
    public TaskDTO updateTaskStatusForUser(Long taskId, boolean isDone, Long userId) throws TaskNotFoundException {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found or not authorized"));
        task.setDone(isDone);
        taskRepository.save(task);
        return new TaskDTO();
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
                                .build())
                        .collect(Collectors.toList())
                : List.of();

//     private TaskDTO convertToDTO(Task task) {
//         List<SubTaskDTO> subTaskDTOs = task.getSubTasks() != null
//                 ? task.getSubTasks().stream()
//                         .map(subTask -> SubTaskDTO.builder()
//                                 .id(subTask.getId())
//                                 .title(subTask.getTitle())
//                                 .description(subTask.getDescription())
//                                 .isDone(subTask.isDone())
//                                 .taskId(task.getId())
//                                 .miniTasks(subTask.getMiniTasks() != null
//                                         ? subTask.getMiniTasks().stream()
//                                                 .map(miniTask -> MiniTaskDTO.builder()
//                                                         .id(miniTask.getId())
//                                                         .title(miniTask.getTitle())
//                                                         .description(miniTask.getDescription())
//                                                         .isDone(miniTask.isDone())
//                                                         .subTaskId(subTask.getId())
//                                                         .build())
//                                                 .collect(Collectors.toList())
//                                         : List.of())
//                                 .build())
//                         .collect(Collectors.toList())
//                 : List.of();

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
