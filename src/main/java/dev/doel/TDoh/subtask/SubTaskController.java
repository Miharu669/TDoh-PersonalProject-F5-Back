package dev.doel.TDoh.subtask;

import java.security.Principal;
import java.util.List;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import dev.doel.TDoh.subtask.subtask_exceptions.SubTaskNotFoundException;
import dev.doel.TDoh.users.User;

@RestController
@RequestMapping("${api-endpoint}/subtasks")
public class SubTaskController {

    @Autowired
    private SubTaskService subTaskService;

    @PostMapping()
    public ResponseEntity<SubTaskDTO> createSubTask(Principal connectedUser,
             Long taskId,
            @RequestBody SubTaskDTO subTaskDTO) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        SubTaskDTO createdSubTask = subTaskService.createSubTask(subTaskDTO, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubTask);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<List<SubTaskDTO>> getSubTasksByTaskId(Principal connectedUser,
            @PathVariable Long taskId) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        List<SubTaskDTO> subTasks = subTaskService.getSubTasksByTaskId(taskId, user.getId());
        return ResponseEntity.ok(subTasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubTaskDTO> getSubTaskById(Principal connectedUser,
            @PathVariable Long id,
            @PathVariable Long taskId) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        SubTaskDTO subTask = subTaskService.getSubTaskById(id, user.getId());
        return ResponseEntity.ok(subTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubTaskDTO> updateSubTask(Principal connectedUser,
            @PathVariable Long id,
            @RequestBody SubTaskDTO subTaskDTO,
            @PathVariable Long taskId) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        SubTaskDTO updatedSubTask = subTaskService.updateSubTask(id, subTaskDTO, user.getId());
        return ResponseEntity.ok(updatedSubTask);
    }

    @PatchMapping("/{id}/status")
public ResponseEntity<SubTaskDTO> updateSubtaskStatus(Principal connectedUser, @PathVariable Long taskId, @PathVariable Long Id, @RequestBody Map<String, Boolean> statusUpdate) {
    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    boolean isDone = statusUpdate.get("isDone");
    try {
        SubTaskDTO updatedSubtask = subTaskService.updateSubTaskStatusForUser(taskId, Id, isDone, user.getId());
        return ResponseEntity.ok(updatedSubtask);
    } catch (SubTaskNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubTask(Principal connectedUser,
            @PathVariable Long id,
            @PathVariable Long taskId) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        try {
            subTaskService.deleteSubTask(id, user.getId());
            return ResponseEntity.noContent().build();
        } catch (SubTaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
