package com.kailoslab.jjan.server.data;

import com.kailoslab.jjan.server.data.entity.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<WordEntity, Integer> {
    List<WordEntity> findAllByWordStartsWithAndDeletedFalse(String word);
    List<WordEntity> findAllByDeletedFalse();
    List<WordEntity> findAllByIdGreaterThanEqualAndDeletedFalse(Integer id);
    Optional<WordEntity> findByIdAndDeletedFalse(Integer id);
    Optional<WordEntity> findByWordAndDeletedFalse(String word);
}
