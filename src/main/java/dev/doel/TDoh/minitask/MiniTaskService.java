// package dev.doel.TDoh.minitask;

// import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import dev.doel.TDoh.subtask.SubTask;
// import dev.doel.TDoh.subtask.SubTaskRepository;
// import dev.doel.TDoh.subtask.subtask_exceptions.SubTaskNotFoundException;
// import dev.doel.TDoh.users.UserRepository;
// import dev.doel.TDoh.users.user_exceptions.UserNotFoundException;
// import dev.doel.TDoh.minitask.minitask_exceptions.MiniTaskNotFoundException;

// @Service
// public class MiniTaskService {

//     @Autowired
//     private MiniTaskRepository miniTaskRepository;

//     @Autowired
//     private SubTaskRepository subTaskRepository;

//     @Autowired
//     private UserRepository userRepository;

//     @Transactional
//     public MiniTaskDTO createMiniTask(MiniTaskDTO miniTaskDTO, Long userId) {
//         SubTask subTask = subTaskRepository.findById(miniTaskDTO.getSubTaskId())
//                 .orElseThrow(() -> new SubTaskNotFoundException("SubTask not found"));

//         if (!subTask.getTask().getUser().getId().equals(userId)) {
//             throw new UserNotFoundException("User does not have permission to add a mini task to this subtask");
//         }

//         MiniTask miniTask = mapToEntity(miniTaskDTO, subTask);
//         MiniTask savedMiniTask = miniTaskRepository.save(miniTask);
//         return mapToDTO(savedMiniTask);
//     }

//     @Transactional(readOnly = true)
//     public List<MiniTaskDTO> getMiniTasksBySubTaskId(Long subTaskId, Long userId) {
//         SubTask subTask = subTaskRepository.findById(subTaskId)
//                 .orElseThrow(() -> new SubTaskNotFoundException("SubTask not found"));

//         if (!subTask.getTask().getUser().getId().equals(userId)) {
//             throw new UserNotFoundException("User does not have permission to access mini tasks of this subtask");
//         }

//         List<MiniTask> miniTasks = miniTaskRepository.findBySubTaskId(subTaskId);
//         return miniTasks.stream()
//                 .map(this::mapToDTO)
//                 .toList();
//     }

//     @Transactional(readOnly = true)
//     public MiniTaskDTO getMiniTaskById(Long id, Long userId) {
//         MiniTask miniTask = miniTaskRepository.findById(id)
//                 .orElseThrow(() -> new MiniTaskNotFoundException("MiniTask not found"));

//         if (!miniTask.getSubTask().getTask().getUser().getId().equals(userId)) {
//             throw new UserNotFoundException("User does not have permission to access this mini task");
//         }

//         return mapToDTO(miniTask);
//     }

//     // Update a mini task
//     @Transactional
//     public MiniTaskDTO updateMiniTask(Long id, MiniTaskDTO miniTaskDTO, Long userId) {
//         MiniTask miniTask = miniTaskRepository.findById(id)
//                 .orElseThrow(() -> new MiniTaskNotFoundException("MiniTask not found"));

//         if (!miniTask.getSubTask().getTask().getUser().getId().equals(userId)) {
//             throw new UserNotFoundException("User does not have permission to update this mini task");
//         }

//         boolean wasDone = miniTask.isDone();
//         miniTask.setTitle(miniTaskDTO.getTitle());
//         miniTask.setDescription(miniTaskDTO.getDescription());
//         miniTask.setDone(miniTaskDTO.isDone());
//         MiniTask updatedMiniTask = miniTaskRepository.save(miniTask);

//         if (!wasDone && miniTaskDTO.isDone()) {
//             addPointsToUser(miniTask.getSubTask().getTask().getUser().getId(), 5); 
//         }

//         return mapToDTO(updatedMiniTask);
//     }

//     // Delete a mini task
//     @Transactional
//     public void deleteMiniTask(Long id, Long userId) {
//         MiniTask miniTask = miniTaskRepository.findById(id)
//                 .orElseThrow(() -> new MiniTaskNotFoundException("MiniTask not found"));

//         if (!miniTask.getSubTask().getTask().getUser().getId().equals(userId)) {
//             throw new UserNotFoundException("User does not have permission to delete this mini task");
//         }

//         miniTaskRepository.delete(miniTask);
//     }

//     private void addPointsToUser(Long userId, int points) {
//         var user = userRepository.findById(userId)
//                 .orElseThrow(() -> new UserNotFoundException("User not found"));
//         user.setScore(user.getScore() + points);
//         userRepository.save(user);
//     }

//     private MiniTask mapToEntity(MiniTaskDTO miniTaskDTO, SubTask subTask) {
//         return MiniTask.builder()
//                 .id(miniTaskDTO.getId())
//                 .title(miniTaskDTO.getTitle())
//                 .description(miniTaskDTO.getDescription())
//                 .isDone(miniTaskDTO.isDone())
//                 .subTask(subTask)
//                 .build();
//     }

//     private MiniTaskDTO mapToDTO(MiniTask miniTask) {
//         return MiniTaskDTO.builder()
//                 .id(miniTask.getId())
//                 .title(miniTask.getTitle())
//                 .description(miniTask.getDescription())
//                 .isDone(miniTask.isDone())
//                 .subTaskId(miniTask.getSubTask().getId())
//                 .build();
//     }
// }
