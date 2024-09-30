package dev.doel.TDoh.task;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserId(Long userId);

    Optional<Task> findByIdAndUserId(Long id, Long userId);

    
}
