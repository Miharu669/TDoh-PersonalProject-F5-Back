package dev.doel.TDoh.minitask;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.doel.TDoh.subtask.SubTask;
import dev.doel.TDoh.subtask.SubTaskRepository;
import dev.doel.TDoh.task.task_exceptions.TaskNotFoundException;
import dev.doel.TDoh.users.UserRepository;
import dev.doel.TDoh.users.user_exceptions.UserNotFoundException;
import dev.doel.TDoh.minitask.minitask_exceptions.MiniTaskNotFoundException;

@Service
public class MiniTaskService {

    @Autowired
    private MiniTaskRepository miniTaskRepository;

    @Autowired
    private SubTaskRepository subTaskRepository;


    @Autowired
    private UserRepository userRepository;

    public MiniTaskDTO createMiniTask(MiniTaskDTO miniTaskDTO) {
        SubTask subTask = subTaskRepository.findById(miniTaskDTO.getSubTaskId())
                .orElseThrow(() -> new TaskNotFoundException("SubTask not found"));

        MiniTask miniTask = mapToEntity(miniTaskDTO, subTask);
        MiniTask savedMiniTask = miniTaskRepository.save(miniTask);
        return mapToDTO(savedMiniTask);
    }

    public List<MiniTaskDTO> getMiniTasksBySubTaskId(Long subTaskId) {
        List<MiniTask> miniTasks = miniTaskRepository.findBySubTaskId(subTaskId);
        return miniTasks.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public MiniTaskDTO getMiniTaskById(Long id) {
        MiniTask miniTask = miniTaskRepository.findById(id)
                .orElseThrow(() -> new MiniTaskNotFoundException("MiniTask not found"));
        return mapToDTO(miniTask);
    }

    public MiniTaskDTO updateMiniTask(Long id, MiniTaskDTO miniTaskDTO) {
        MiniTask miniTask = miniTaskRepository.findById(id)
                .orElseThrow(() -> new MiniTaskNotFoundException("MiniTask not found"));

        boolean isDone = miniTask.isDone();
        miniTask.setTitle(miniTaskDTO.getTitle());
        miniTask.setDescription(miniTaskDTO.getDescription());
        miniTask.setDone(miniTaskDTO.isDone());
        MiniTask updatedMiniTask = miniTaskRepository.save(miniTask);

        if (!isDone && miniTaskDTO.isDone()) {
            addPointsToUser(miniTask.getSubTask().getTask().getUser().getId(), 5); 
        }

        return mapToDTO(updatedMiniTask);
    }

    public void deleteMiniTask(Long id) {
        MiniTask miniTask = miniTaskRepository.findById(id)
                .orElseThrow(() -> new MiniTaskNotFoundException("MiniTask not found"));
        miniTaskRepository.delete(miniTask);
    }

    private void addPointsToUser(Long userId, int points) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setScore(user.getScore() + points);
        userRepository.save(user);
    }

    private MiniTask mapToEntity(MiniTaskDTO miniTaskDTO, SubTask subTask) {
        return MiniTask.builder()
                .id(miniTaskDTO.getId())
                .title(miniTaskDTO.getTitle())
                .description(miniTaskDTO.getDescription())
                .isDone(miniTaskDTO.isDone())
                .subTask(subTask)
                .build();
    }

    private MiniTaskDTO mapToDTO(MiniTask miniTask) {
        return MiniTaskDTO.builder()
                .id(miniTask.getId())
                .title(miniTask.getTitle())
                .description(miniTask.getDescription())
                .isDone(miniTask.isDone())
                .subTaskId(miniTask.getSubTask().getId())
                .build();
    }
}
