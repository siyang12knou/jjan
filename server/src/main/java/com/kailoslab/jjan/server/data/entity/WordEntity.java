package com.kailoslab.jjan.server.data.entity;

import com.kailoslab.jjan.server.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_word")
@EntityListeners(AuditingEntityListener.class)
public class WordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String word;
    private Boolean deleted;
    private String createdUser;
    private String updatedUser;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

    public WordEntity(String word) {
        this.word = word;
        this.deleted = false;
        this.createdUser = Constants.SYSTEM_ADMIN;
        this.updatedUser = Constants.SYSTEM_ADMIN;
    }
}
