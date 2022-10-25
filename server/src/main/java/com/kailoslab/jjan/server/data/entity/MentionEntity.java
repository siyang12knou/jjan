package com.kailoslab.jjan.server.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_mention")
@EntityListeners(AuditingEntityListener.class)
public class MentionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String sns;
    private Integer idWord;
    private String fromYmd;
    private String toYmd;
    private Integer cnt;
    private Integer subCnt;
    @CreatedDate
    private LocalDateTime createdDate;

    public MentionEntity(String sns, Integer idWord, String fromYmd, String toYmd, Integer cnt) {
        this(sns, idWord, fromYmd, toYmd, cnt, null);
    }

    public MentionEntity(String sns, Integer idWord, String fromYmd, String toYmd, Integer cnt, Integer subCnt) {
        this.sns = sns;
        this.idWord = idWord;
        this.fromYmd = fromYmd;
        this.toYmd = toYmd;
        this.cnt = cnt;
        this.subCnt = subCnt;
    }
}
