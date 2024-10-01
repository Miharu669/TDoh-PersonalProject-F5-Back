package dev.doel.TDoh.task;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import dev.doel.TDoh.task.task_exceptions.TaskNotFoundException;
import dev.doel.TDoh.users.User;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api-endpoint}/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks(Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<TaskDTO> tasks = taskService.getAllTasksForUser(user.getId());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id, @RequestParam Long userId) {
        try {
            TaskDTO task = taskService.getTaskByIdForUser(id, userId);
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO, @RequestParam Long userId) {
        TaskDTO createdTask = taskService.createTaskForUser(taskDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO, @RequestParam Long userId) {
        try {
            TaskDTO updatedTask = taskService.updateTaskForUser(id, taskDTO, userId);
            return ResponseEntity.ok(updatedTask);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @RequestParam Long userId) {
        try {
            taskService.deleteTaskForUser(id, userId);
            return ResponseEntity.noContent().build();
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
