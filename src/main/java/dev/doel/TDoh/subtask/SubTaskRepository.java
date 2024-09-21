package dev.doel.TDoh.subtask;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
    List<SubTask> findByTaskIdAndUserId(Long taskId, Long userId);
    List<SubTask> findByUserId(Long userId);
    List<SubTask> findByTaskId(Long taskId);

}
