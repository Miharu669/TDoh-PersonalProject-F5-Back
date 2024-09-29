// package dev.doel.TDoh.task;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.*;

// import jakarta.validation.Valid;

// import java.util.List;

// @RestController
// @RequestMapping("${api-endpoint}/tasks")
// @Validated  
// public class TaskControllerAuth {

//     @Autowired
//     private TaskService taskService;

//     @GetMapping
//     public ResponseEntity<List<TaskDTO>> getAllTasks(Authentication authentication) {
//         List<TaskDTO> tasks = taskService.getTasksForCurrentUser(authentication);
//         return ResponseEntity.ok(tasks);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id, Authentication authentication) {
//         TaskDTO task = taskService.getTaskByIdForCurrentUser(id, authentication);
//         return ResponseEntity.ok(task);
//     }

//     @PostMapping
//     public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO, Authentication authentication) {
//         TaskDTO createdTask = taskService.createTask(taskDTO, authentication);
//         return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO,
//                                               Authentication authentication) {
//         TaskDTO updatedTask = taskService.updateTask(id, taskDTO, authentication);
//         return ResponseEntity.ok(updatedTask);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
//         taskService.deleteTask(id, authentication);
//         return ResponseEntity.noContent().build();
//     }
// }
