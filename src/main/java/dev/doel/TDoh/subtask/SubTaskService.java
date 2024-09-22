package dev.doel.TDoh.subtask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        subTask.setDone(subTaskDTO.isDone());
        SubTask updatedSubTask = subTaskRepository.save(subTask);

        if (!subTask.isDone() && subTaskDTO.isDone()) {
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

    private SubTask mapToEntity(SubTaskDTO subTaskDTO, Task task) {
        return SubTask.builder()
                .id(subTaskDTO.getId())
                .title(subTaskDTO.getTitle())
                .description(subTaskDTO.getDescription())
                .isDone(subTaskDTO.isDone())
                .task(task)
                .build();
    }

    private SubTaskDTO mapToDTO(SubTask subTask) {
        return SubTaskDTO.builder()
                .id(subTask.getId())
                .title(subTask.getTitle())
                .description(subTask.getDescription())
                .isDone(subTask.isDone())
                .taskId(subTask.getTask().getId())
                .build();
    }
}
