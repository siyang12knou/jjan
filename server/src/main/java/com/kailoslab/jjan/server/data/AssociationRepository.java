package com.kailoslab.jjan.server.data;

import com.kailoslab.jjan.server.data.dto.AssociationAggrDto;
import com.kailoslab.jjan.server.data.entity.AssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssociationRepository extends JpaRepository<AssociationEntity, Long> {
    @Query(nativeQuery = true, value = """
SELECT A.sns      AS sns,
       B.word     AS word,
       A.word     AS associatedWord,
       SUM(A.cnt) AS cnt
  FROM tb_association A
         left join tb_word B
                   on A.id_word = B.id
 WHERE A.from_ymd BETWEEN :fromYmd AND :toYmd
   AND A.to_ymd BETWEEN :fromYmd AND :toYmd
   AND B.word = :word
   AND A.sns IN :sns
GROUP BY sns, word, associatedWord
ORDER BY cnt DESC 
""")
    List<AssociationAggrDto> findAggrByWordAndYear(String word, String fromYmd, String toYmd, List<String> sns);
    void deleteByFromYmdAnAndToYmd(String fromYmd, String toYmd);
}
