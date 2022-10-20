package com.kailoslab.jjan.server.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_association")
public class AssociationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Integer idWord;
    private String word;
    private String fromYmd;
    private String toYmd;
    private Integer cnt;
    private Integer subCnt;
    @CreatedDate
    private LocalDateTime createdDate;
}
