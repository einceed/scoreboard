package com.example.itask.persistance.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.itask.persistance.model.Score;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    Optional<Score> getFirstByEventIdOrderByMinuteDesc(Long eventId);

}
