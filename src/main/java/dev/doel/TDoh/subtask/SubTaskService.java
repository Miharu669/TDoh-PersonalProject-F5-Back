package dev.doel.TDoh.subtask;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.doel.TDoh.minitask.MiniTask;
import dev.doel.TDoh.minitask.MiniTaskDTO;
import dev.doel.TDoh.subtask.subtask_exceptions.SubTaskNotFoundException;
import dev.doel.TDoh.task.Task;
import dev.doel.TDoh.task.TaskRepository;
import dev.doel.TDoh.task.task_exceptions.TaskNotFoundException;
import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserRepository;
import dev.doel.TDoh.users.user_exceptions.UserNotFoundException;

@Service
public class SubTaskService {

    @Autowired
    private SubTaskRepository subTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public SubTaskDTO createSubTask(SubTaskDTO subTaskDTO) {
        Task task = taskRepository.findById(subTaskDTO.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        SubTask subTask = mapToEntity(subTaskDTO, task);
        SubTask savedSubTask = subTaskRepository.save(subTask);
        return mapToDTO(savedSubTask);
    }

    public List<SubTaskDTO> getSubTasksByTaskId(Long taskId) {
        List<SubTask> subTasks = subTaskRepository.findByTaskId(taskId);
        return subTasks.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public SubTaskDTO getSubTaskById(Long id) {
        SubTask subTask = subTaskRepository.findById(id)
                .orElseThrow(() -> new SubTaskNotFoundException("SubTask not found"));
        return mapToDTO(subTask);
    }

    public SubTaskDTO updateSubTask(Long id, SubTaskDTO subTaskDTO) {
        SubTask subTask = subTaskRepository.findById(id)
                .orElseThrow(() -> new SubTaskNotFoundException("SubTask not found"));

        subTask.setTitle(subTaskDTO.getTitle());
        subTask.setDescription(subTaskDTO.getDescription());
        boolean wasDone = subTask.isDone();
        subTask.setDone(subTaskDTO.isDone());
        SubTask updatedSubTask = subTaskRepository.save(subTask);

        if (!wasDone && subTaskDTO.isDone()) {
            addPointsToUser(subTask.getTask().getUser().getId(), 25);
        }
        return mapToDTO(updatedSubTask);
    }

    private void addPointsToUser(Long userId, int points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setScore(user.getScore() + points);
        userRepository.save(user);
    }

    public void deleteSubTask(Long id) {
        SubTask subTask = subTaskRepository.findById(id)
                .orElseThrow(() -> new SubTaskNotFoundException("SubTask not found"));

        subTaskRepository.delete(subTask);
    }

    // Method to map SubTask to SubTaskDTO and include nested MiniTasks
    private SubTaskDTO mapToDTO(SubTask subTask) {
        // Fetch and map miniTasks related to this subTask
        List<MiniTaskDTO> miniTaskDTOs = subTask.getMiniTasks() != null
                ? subTask.getMiniTasks().stream()
                    .map(this::mapMiniTaskToDTO)
                    .toList()
                : List.of(); // Return an empty list if there are no miniTasks

        // Return the SubTaskDTO with nested miniTasks
        return SubTaskDTO.builder()
                .id(subTask.getId())
                .title(subTask.getTitle())
                .description(subTask.getDescription())
                .isDone(subTask.isDone())
                .taskId(subTask.getTask().getId())
                .miniTasks(miniTaskDTOs) // Include nested miniTasks
                .build();
    }

    // Helper method to map MiniTask entity to MiniTaskDTO
    private MiniTaskDTO mapMiniTaskToDTO(MiniTask miniTask) {
        return MiniTaskDTO.builder()
                .id(miniTask.getId())
                .title(miniTask.getTitle())
                .description(miniTask.getDescription())
                .isDone(miniTask.isDone())
                .subTaskId(miniTask.getSubTask().getId())
                .build();
    }

    // Method to map SubTaskDTO to SubTask entity
    private SubTask mapToEntity(SubTaskDTO subTaskDTO, Task task) {
        return SubTask.builder()
                .id(subTaskDTO.getId())
                .title(subTaskDTO.getTitle())
                .description(subTaskDTO.getDescription())
                .isDone(subTaskDTO.isDone())
                .task(task)
                .build();
    }
}
