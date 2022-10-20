package com.kailoslab.jjan.server.data;

import com.kailoslab.jjan.server.data.entity.CodeEntity;
import com.kailoslab.jjan.server.data.entity.CodePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<CodeEntity, CodePK> {

    List<CodeEntity> findByGroupIdAndDeletedFalseOrderByOrdinal(String groupId);
    Optional<CodeEntity> findFirstByGroupIdAndDeletedFalseOrderByOrdinalDesc(String groupId);
}