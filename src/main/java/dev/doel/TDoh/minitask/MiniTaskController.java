package dev.doel.TDoh.minitask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api-endpoint}/minitasks")
public class MiniTaskController {

    @Autowired
    private MiniTaskService miniTaskService;

    @PostMapping
    public ResponseEntity<MiniTaskDTO> createMiniTask(@RequestBody MiniTaskDTO miniTaskDTO) {
        MiniTaskDTO createdMiniTask = miniTaskService.createMiniTask(miniTaskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMiniTask);
    }

    @GetMapping("/subtask/{subtaskId}")
    public ResponseEntity<List<MiniTaskDTO>> getMiniTasksBySubTaskId(@PathVariable Long subtaskId) {
        List<MiniTaskDTO> miniTasks = miniTaskService.getMiniTasksBySubTaskId(subtaskId);
        return ResponseEntity.ok(miniTasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MiniTaskDTO> getMiniTaskById(@PathVariable Long id) {
        MiniTaskDTO miniTask = miniTaskService.getMiniTaskById(id);
        return ResponseEntity.ok(miniTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MiniTaskDTO> updateMiniTask(@PathVariable Long id, @RequestBody MiniTaskDTO miniTaskDTO) {
        MiniTaskDTO updatedMiniTask = miniTaskService.updateMiniTask(id, miniTaskDTO);
        return ResponseEntity.ok(updatedMiniTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMiniTask(@PathVariable Long id) {
        miniTaskService.deleteMiniTask(id);
        return ResponseEntity.noContent().build();
    }
}
