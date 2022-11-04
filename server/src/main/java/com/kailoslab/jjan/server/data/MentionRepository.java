package com.kailoslab.jjan.server.data;

import com.kailoslab.jjan.server.data.dto.MentionAggrDto;
import com.kailoslab.jjan.server.data.entity.MentionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentionRepository extends JpaRepository<MentionEntity, Long> {
    @Query(nativeQuery = true, value = """
SELECT sns, word, ym, SUM(cnt) AS cnt
  FROM (SELECT A.sns                    AS sns,
               B.word                   AS word,
               substr(A.from_ymd, 1, 6) AS ym,
               A.cnt                    AS cnt
        FROM tb_mention A
                 left join tb_word B
                           on A.id_word = B.id
        WHERE A.from_ymd BETWEEN :fromYmd AND :toYmd
          AND A.to_ymd BETWEEN :fromYmd AND :toYmd
          AND B.word = :word
          AND A.sns IN :sns) aggr_mention
 GROUP BY sns, word, ym
 ORDER BY ym
""")
    List<MentionAggrDto> findAggrByWordAndYear(String word, String fromYmd, String toYmd, List<String> sns);

    @Modifying
    @Query(nativeQuery = true, value = """
DELETE
  FROM tb_mention
 WHERE id_word = :idWord\040
   AND from_ymd BETWEEN :fromYmd AND :toYmd\040
   AND to_ymd BETWEEN :fromYmd AND :toYmd
   AND sns IN :sns
""")
    void deleteByIdWordAndFromYmdGreaterThanEqualAndToYmdLessThanEqualAndSnsIn(Integer idWord, String fromYmd, String toYmd, List<String> sns);
}
