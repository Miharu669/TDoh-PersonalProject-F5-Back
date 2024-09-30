package dev.doel.TDoh.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("${api-endpoint}/tasks")
@PreAuthorize("hasRole('USER')")

@Validated  
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(Authentication authentication) {
        Long userId = taskService.getCurrentAuthenticatedUserId(authentication);  
        List<TaskDTO> tasks = taskService.getAllTasksForUser(userId);  
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id, Authentication authentication) {
        Long userId = taskService.getCurrentAuthenticatedUserId(authentication);  
        TaskDTO task = taskService.getTaskByIdForUser(id, userId);  
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO, Authentication authentication) {
        Long userId = taskService.getCurrentAuthenticatedUserId(authentication);  
        TaskDTO createdTask = taskService.createTaskForUser(taskDTO, userId); 
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO, Authentication authentication) {
        Long userId = taskService.getCurrentAuthenticatedUserId(authentication);  
        TaskDTO updatedTask = taskService.updateTaskForUser(id, taskDTO, userId);  
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
        Long userId = taskService.getCurrentAuthenticatedUserId(authentication);  
        taskService.deleteTaskForUser(id, userId);  
        return ResponseEntity.noContent().build();
    }
}
