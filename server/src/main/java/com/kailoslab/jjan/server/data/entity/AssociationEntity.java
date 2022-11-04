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
@Table(name = "tb_association")
@EntityListeners(AuditingEntityListener.class)
public class AssociationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String sns;
    private Integer idWord;
    private String word;
    private String fromYmd;
    private String toYmd;
    private Integer cnt = 0;
    private Integer subCnt = 0;
    @CreatedDate
    private LocalDateTime createdDate;

    public AssociationEntity(String sns, Integer idWord, String word, String fromYmd, String toYmd, Integer cnt) {
        this.sns = sns;
        this.idWord = idWord;
        this.word = word;
        this.fromYmd = fromYmd;
        this.toYmd = toYmd;
        this.cnt = cnt;
    }
}
