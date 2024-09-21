package dev.doel.TDoh.minitask;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiniTaskRepository extends JpaRepository<MiniTask, Long> {
    
    List<MiniTask> findBySubTaskId(Long subTaskId);

    List<MiniTask> findBySubTask_IdAndSubTask_Task_User_Id(Long subTaskId, Long userId);

    List<MiniTask> findBySubTask_Task_IdAndSubTask_Task_User_Id(Long taskId, Long userId);
}
