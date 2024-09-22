package dev.doel.TDoh.subtask;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, Long> {

    List<SubTask> findByTaskId(Long taskId);

    List<SubTask> findByTaskIdAndIsDoneTrue(Long taskId);

    List<SubTask> findByTaskIdAndIsDoneFalse(Long taskId);

    List<SubTask> findByTaskIdAndTitleContainingIgnoreCase(Long taskId, String title);
}
