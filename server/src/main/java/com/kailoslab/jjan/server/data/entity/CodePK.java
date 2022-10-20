package com.kailoslab.jjan.server.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodePK implements Serializable {
    private String groupId;
    private String codeId;
}