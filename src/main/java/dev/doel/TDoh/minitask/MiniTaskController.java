package dev.doel.TDoh.minitask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/minitasks")
public class MiniTaskController {

    @Autowired
    private MiniTaskService miniTaskService;

    @PostMapping
    public ResponseEntity<MiniTaskDTO> createMiniTask(@RequestBody MiniTaskDTO miniTaskDTO, @RequestParam Long userId) {
        MiniTaskDTO createdMiniTask = miniTaskService.createMiniTask(miniTaskDTO, userId);
        return ResponseEntity.ok(createdMiniTask);
    }

    @GetMapping("/subtask/{subTaskId}")
    public ResponseEntity<List<MiniTaskDTO>> getMiniTasksBySubTaskId(@PathVariable Long subTaskId, @RequestParam Long userId) {
        List<MiniTaskDTO> miniTasks = miniTaskService.getMiniTasksBySubTaskId(subTaskId, userId);
        return ResponseEntity.ok(miniTasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MiniTaskDTO> getMiniTaskById(@PathVariable Long id, @RequestParam Long userId) {
        MiniTaskDTO miniTask = miniTaskService.getMiniTaskById(id, userId);
        return ResponseEntity.ok(miniTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MiniTaskDTO> updateMiniTask(@PathVariable Long id, @RequestBody MiniTaskDTO miniTaskDTO, @RequestParam Long userId) {
        MiniTaskDTO updatedMiniTask = miniTaskService.updateMiniTask(id, miniTaskDTO, userId);
        return ResponseEntity.ok(updatedMiniTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMiniTask(@PathVariable Long id, @RequestParam Long userId) {
        miniTaskService.deleteMiniTask(id, userId);
        return ResponseEntity.noContent().build();
    }
}
