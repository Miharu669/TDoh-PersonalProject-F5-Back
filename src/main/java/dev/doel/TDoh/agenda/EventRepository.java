package dev.doel.TDoh.agenda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDate(LocalDate date);

    List<Event> findByDateAndUserId(LocalDate date, Long userId);

    List<Event> findByUserId(Long userId);
    
    Optional<Event> findByIdAndUserId(Long id, Long userId);

     @Modifying
    @Transactional
    @Query("DELETE FROM Event e WHERE e.date = :date AND e.user.id = :userId")
    int deleteByDateAndUserId(@Param("date") LocalDate date, @Param("userId") Long userId);
}
