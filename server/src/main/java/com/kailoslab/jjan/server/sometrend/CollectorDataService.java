package com.kailoslab.jjan.server.sometrend;

import com.kailoslab.jjan.server.data.entity.AssociationEntity;
import com.kailoslab.jjan.server.data.entity.MentionEntity;
import com.kailoslab.jjan.server.data.entity.WordEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CollectorDataService {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public void clearData(WordEntity wordEntity, List<String> snsCodeIds, String fromYmd, String toYmd) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("idWord", wordEntity.getId());
        parameterSource.addValue("fromYmd", fromYmd);
        parameterSource.addValue("toYmd", toYmd);
        parameterSource.addValue("sns", snsCodeIds);
        String deleteMention = """
                DELETE
                 FROM tb_mention
                WHERE id_word = :idWord
                  AND from_ymd BETWEEN :fromYmd AND :toYmd
                  AND to_ymd BETWEEN :fromYmd AND :toYmd
                  AND sns IN (:sns)
                """;
        namedParameterJdbcTemplate.update(deleteMention, parameterSource);
        String deleteAssociation = """
                DELETE
                 FROM tb_association
                WHERE id_word = :idWord
                  AND from_ymd BETWEEN :fromYmd AND :toYmd
                  AND to_ymd BETWEEN :fromYmd AND :toYmd
                  AND sns IN (:sns)
                """;
        namedParameterJdbcTemplate.update(deleteAssociation, parameterSource);
        log.info(String.format("deleted collect data: %s, %s, %s, %s", wordEntity.getWord(), fromYmd, toYmd, snsCodeIds));
    }

    public void saveMentionList(List<MentionEntity> mentionEntityList) {
        String insertMention = """
                INSERT INTO tb_mention(sns, id_word, cnt, sub_cnt, from_ymd, to_ymd) VALUES (?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.batchUpdate(insertMention, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                MentionEntity mentionEntity = mentionEntityList.get(i);
                ps.setString(1, mentionEntity.getSns());
                ps.setInt(2, mentionEntity.getIdWord());
                ps.setInt(3, mentionEntity.getCnt());
                ps.setInt(4, mentionEntity.getSubCnt());
                ps.setString(5, mentionEntity.getFromYmd());
                ps.setString(6, mentionEntity.getToYmd());
            }

            @Override
            public int getBatchSize() {
                return mentionEntityList.size();
            }
        });
    }

    public void saveAssociationList(List<AssociationEntity> associationEntityList) {
        String insertAssociation = """
                INSERT INTO tb_association(sns, id_word, word, cnt, sub_cnt, from_ymd, to_ymd) VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.batchUpdate(insertAssociation, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                AssociationEntity associationEntity = associationEntityList.get(i);
                ps.setString(1, associationEntity.getSns());
                ps.setInt(2, associationEntity.getIdWord());
                ps.setString(3, associationEntity.getWord());
                ps.setInt(4, associationEntity.getCnt());
                ps.setInt(5, associationEntity.getSubCnt());
                ps.setString(6, associationEntity.getFromYmd());
                ps.setString(7, associationEntity.getToYmd());
            }

            @Override
            public int getBatchSize() {
                return associationEntityList.size();
            }
        });
    }
}
