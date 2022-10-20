package com.kailoslab.jjan.server.data;

import com.kailoslab.jjan.server.data.entity.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<WordEntity, Integer> {
    List<WordEntity> findAllByWordStartsWith(String word);
}
