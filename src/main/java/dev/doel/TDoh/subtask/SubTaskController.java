package dev.doel.TDoh.subtask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api-endpoint}/subtasks")
public class SubTaskController {

    @Autowired
    private SubTaskService subTaskService;

    @PostMapping
    public ResponseEntity<SubTaskDTO> createSubTask(@RequestBody SubTaskDTO subTaskDTO) {
        SubTaskDTO createdSubTask = subTaskService.createSubTask(subTaskDTO);
        return ResponseEntity.ok(createdSubTask);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<SubTaskDTO>> getSubTasksByTaskId(@PathVariable Long taskId) {
        List<SubTaskDTO> subTasks = subTaskService.getSubTasksByTaskId(taskId);
        return ResponseEntity.ok(subTasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubTaskDTO> getSubTaskById(@PathVariable Long id) {
        SubTaskDTO subTask = subTaskService.getSubTaskById(id);
        return ResponseEntity.ok(subTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubTaskDTO> updateSubTask(@PathVariable Long id, @RequestBody SubTaskDTO subTaskDTO) {
        SubTaskDTO updatedSubTask = subTaskService.updateSubTask(id, subTaskDTO);
        return ResponseEntity.ok(updatedSubTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubTask(@PathVariable Long id) {
        subTaskService.deleteSubTask(id);
        return ResponseEntity.noContent().build();
    }
}
