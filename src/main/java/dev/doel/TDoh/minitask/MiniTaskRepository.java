package dev.doel.TDoh.minitask;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MiniTaskRepository extends JpaRepository<MiniTask, Long> {
    List<MiniTask> findBySubTaskId(Long subTaskId);

}
