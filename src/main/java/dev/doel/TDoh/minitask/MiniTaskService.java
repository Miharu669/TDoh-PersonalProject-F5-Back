package dev.doel.TDoh.minitask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.doel.TDoh.minitask.minitask_exceptions.MiniTaskNotFoundException;
import dev.doel.TDoh.subtask.SubTask;
import dev.doel.TDoh.subtask.SubTaskRepository;
import dev.doel.TDoh.task.Task;

@Service
public class MiniTaskService {

    @Autowired
    private MiniTaskRepository miniTaskRepository;

    @Autowired
    private SubTaskRepository subTaskRepository;

    public MiniTaskDTO createMiniTask(MiniTaskDTO miniTaskDTO, Long userId) {
        SubTask subTask = subTaskRepository.findById(miniTaskDTO.getSubTaskId())
                .orElseThrow(() -> new MiniTaskNotFoundException(
                        "SubTask with ID " + miniTaskDTO.getSubTaskId() + " not found"));

        Task task = subTask.getTask();

        if (task.getUser().getId() != userId) {
            throw new MiniTaskNotFoundException("Task does not belong to the user");
        }

        MiniTask miniTask = mapToEntity(miniTaskDTO, subTask, task);
        MiniTask savedMiniTask = miniTaskRepository.save(miniTask);
        return mapToDTO(savedMiniTask);
    }

    public List<MiniTaskDTO> getMiniTasksBySubTaskId(Long subTaskId, Long userId) {
        List<MiniTask> miniTasks = miniTaskRepository.findBySubTask_IdAndSubTask_Task_User_Id(subTaskId, userId);
        return miniTasks.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<MiniTaskDTO> getMiniTasksByTaskId(Long taskId, Long userId) {
        List<MiniTask> miniTasks = miniTaskRepository.findBySubTask_Task_IdAndSubTask_Task_User_Id(taskId, userId);
        return miniTasks.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public MiniTaskDTO getMiniTaskById(Long id, Long userId) {
        MiniTask miniTask = miniTaskRepository.findById(id)
                .orElseThrow(() -> new MiniTaskNotFoundException("MiniTask with ID " + id + " not found"));

        if (miniTask.getTask().getUser().getId() != userId) { 
            throw new MiniTaskNotFoundException("MiniTask does not belong to the user");
        }

        return mapToDTO(miniTask);
    }

    public MiniTaskDTO updateMiniTask(Long id, MiniTaskDTO miniTaskDTO, Long userId) {
        MiniTask miniTask = miniTaskRepository.findById(id)
                .orElseThrow(() -> new MiniTaskNotFoundException("MiniTask with ID " + id + " not found"));

       
        if (miniTask.getTask().getUser().getId() != userId) { 
            throw new MiniTaskNotFoundException("MiniTask does not belong to the user");
        }

        miniTask.setTitle(miniTaskDTO.getTitle());
        miniTask.setDescription(miniTaskDTO.getDescription());
        miniTask.setDone(miniTaskDTO.isDone());
        MiniTask updatedMiniTask = miniTaskRepository.save(miniTask);
        return mapToDTO(updatedMiniTask);
    }

    public void deleteMiniTask(Long id, Long userId) {
        MiniTask miniTask = miniTaskRepository.findById(id)
                .orElseThrow(() -> new MiniTaskNotFoundException("MiniTask with ID " + id + " not found"));

        if (miniTask.getTask().getUser().getId() != userId) {
            throw new MiniTaskNotFoundException("MiniTask does not belong to the user");
        }

        miniTaskRepository.delete(miniTask);
    }

    private MiniTask mapToEntity(MiniTaskDTO miniTaskDTO, SubTask subTask, Task task) {
        return MiniTask.builder()
                .id(miniTaskDTO.getId())
                .title(miniTaskDTO.getTitle())
                .description(miniTaskDTO.getDescription())
                .isDone(miniTaskDTO.isDone())
                .subTask(subTask)
                .task(task)
                .build();
    }

    private MiniTaskDTO mapToDTO(MiniTask miniTask) {
        return MiniTaskDTO.builder()
                .id(miniTask.getId())
                .title(miniTask.getTitle())
                .description(miniTask.getDescription())
                .isDone(miniTask.isDone())
                .subTaskId(miniTask.getSubTask().getId())
                .taskId(miniTask.getTask().getId())
                .build();
    }

}
