package dev.doel.TDoh.subtask;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.doel.TDoh.subtask.subtask_exceptions.SubTaskNotFoundException;
import dev.doel.TDoh.task.Task;
import dev.doel.TDoh.task.TaskRepository;
import dev.doel.TDoh.task.task_exceptions.TaskNotFoundException;

@Service
public class SubTaskService {
    
    @Autowired
    private SubTaskRepository subTaskRepository;
    @Autowired
    private TaskRepository taskRepository;

    

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
        Optional<SubTask> subTask = subTaskRepository.findById(id);
        return subTask.map(this::mapToDTO)
                .orElseThrow(() -> new SubTaskNotFoundException("SubTask not found"));
    }

    public SubTaskDTO updateSubTask(Long id, SubTaskDTO subTaskDTO) {
        SubTask subTask = subTaskRepository.findById(id)
                .orElseThrow(() -> new SubTaskNotFoundException("SubTask not found"));

        subTask.setTitle(subTaskDTO.getTitle());
        subTask.setDescription(subTaskDTO.getDescription());
        subTask.setDone(subTaskDTO.isDone()); 
        SubTask updatedSubTask = subTaskRepository.save(subTask);
        return mapToDTO(updatedSubTask);
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
