package dev.doel.TDoh.subtask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subtasks")
public class SubTaskController {

    @Autowired
    private SubTaskService subTaskService;

    @PostMapping
    public ResponseEntity<SubTaskDTO> createSubTask(@RequestBody SubTaskDTO subTaskDTO, @RequestParam Long userId) {
        SubTaskDTO createdSubTask = subTaskService.createSubTask(subTaskDTO, userId);
        return ResponseEntity.ok(createdSubTask);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<SubTaskDTO>> getSubTasksByTaskId(@PathVariable Long taskId, @RequestParam Long userId) {
        List<SubTaskDTO> subTasks = subTaskService.getSubTasksByTaskId(taskId, userId);
        return ResponseEntity.ok(subTasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubTaskDTO> getSubTaskById(@PathVariable Long id, @RequestParam Long userId) {
        SubTaskDTO subTask = subTaskService.getSubTaskById(id, userId);
        return ResponseEntity.ok(subTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubTaskDTO> updateSubTask(@PathVariable Long id, @RequestBody SubTaskDTO subTaskDTO, @RequestParam Long userId) {
        SubTaskDTO updatedSubTask = subTaskService.updateSubTask(id, subTaskDTO, userId);
        return ResponseEntity.ok(updatedSubTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubTask(@PathVariable Long id, @RequestParam Long userId) {
        subTaskService.deleteSubTask(id, userId);
        return ResponseEntity.noContent().build();
    }
}
