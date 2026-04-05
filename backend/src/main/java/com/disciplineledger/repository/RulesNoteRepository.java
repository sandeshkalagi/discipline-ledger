package com.disciplineledger.repository;

import com.disciplineledger.entity.RulesNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RulesNoteRepository extends JpaRepository<RulesNote, Long> {
    Optional<RulesNote> findByUserId(Long userId);
}
