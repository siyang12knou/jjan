package com.kailoslab.jjan.server.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class AggrDto {
    private String word;
    private String fromYm;
    private String toYm;
    private List<String> sns;
    private List<MentionAggrDto> mentionList;
    private Map<String, List<AssociationAggrDto>> associationList;
}
