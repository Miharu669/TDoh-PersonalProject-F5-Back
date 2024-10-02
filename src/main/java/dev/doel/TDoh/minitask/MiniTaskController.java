// package dev.doel.TDoh.minitask;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.web.bind.annotation.*;

// import dev.doel.TDoh.users.User;

// import java.security.Principal;
// import java.util.List;

// @RestController
// @RequestMapping("${api-endpoint}/minitasks")
// public class MiniTaskController {

//     @Autowired
//     private MiniTaskService miniTaskService;

//     @PostMapping
//     public ResponseEntity<MiniTaskDTO> createMiniTask(Principal connectedUser,
//                                                         Long taskId, 
//                                                         Long subtaskId,
//                                                        @RequestBody MiniTaskDTO miniTaskDTO) {
//         User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        
//         MiniTaskDTO createdMiniTask = miniTaskService.createMiniTask(miniTaskDTO, user.getId());
//         return ResponseEntity.status(HttpStatus.CREATED).body(createdMiniTask);
//     }

//     @GetMapping
//     public ResponseEntity<List<MiniTaskDTO>> getMiniTasksBySubTaskId(Principal connectedUser,
//                                                                       @PathVariable Long taskId,
//                                                                       @PathVariable Long subtaskId) {
//         User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

//         List<MiniTaskDTO> miniTasks = miniTaskService.getMiniTasksBySubTaskId(subtaskId, user.getId());
//         return ResponseEntity.ok(miniTasks);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<MiniTaskDTO> getMiniTaskById(Principal connectedUser,
//                                                         @PathVariable Long taskId,
//                                                         @PathVariable Long subtaskId,
//                                                         @PathVariable Long id) {
//         User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

//         MiniTaskDTO miniTask = miniTaskService.getMiniTaskById(id, user.getId());
//         return ResponseEntity.ok(miniTask);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<MiniTaskDTO> updateMiniTask(Principal connectedUser,
//                                                        @PathVariable Long taskId,
//                                                        @PathVariable Long subtaskId,
//                                                        @PathVariable Long id,
//                                                        @RequestBody MiniTaskDTO miniTaskDTO) {
//         User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

//         MiniTaskDTO updatedMiniTask = miniTaskService.updateMiniTask(id, miniTaskDTO, user.getId());
//         return ResponseEntity.ok(updatedMiniTask);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteMiniTask(Principal connectedUser,
//                                                 @PathVariable Long taskId,
//                                                 @PathVariable Long subtaskId,
//                                                 @PathVariable Long id) {
//         User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

//         miniTaskService.deleteMiniTask(id, user.getId());
//         return ResponseEntity.noContent().build();
//     }
// }
