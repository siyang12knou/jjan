package com.kailoslab.jjan.server.data.entity;

import com.kailoslab.jjan.server.data.converter.MapConverter;
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
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@IdClass(CodePK.class)
@Table(name = "tb_code")
@EntityListeners(AuditingEntityListener.class)
public class CodeEntity {
    @Id
    @Column(name = "group_id", nullable = false, length = 10)
    private String groupId;
    @Id
    @Column(name = "code_id", nullable = false, length = 10)
    private String codeId;
    private String name;
    private Integer ordinal;
    @Convert(converter = MapConverter.class)
    private Map<String, Object> properties;
    private Boolean deleted;
    private String createdUser;
    private String updatedUser;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

    public CodeEntity(String codeId, String name) {
        this.groupId = Constants.DEFAULT_GROUP_ID;
        this.codeId = codeId;
        this.name = name;
        this.ordinal = 0;
    }

    public CodeEntity(String groupId, String codeId, String name, int ordinal) {
        this.groupId = groupId;
        this.codeId = codeId;
        this.name = name;
        this.ordinal = ordinal;
    }
}