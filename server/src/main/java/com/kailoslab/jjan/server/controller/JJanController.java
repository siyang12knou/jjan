package com.kailoslab.jjan.server.controller;

import com.kailoslab.jjan.server.data.dto.AggrDto;
import com.kailoslab.jjan.server.data.dto.AssociationAggrDto;
import com.kailoslab.jjan.server.data.dto.MentionAggrDto;
import com.kailoslab.jjan.server.data.dto.ResultMessageDto;
import com.kailoslab.jjan.server.data.entity.CodeEntity;
import com.kailoslab.jjan.server.data.entity.WordEntity;
import com.kailoslab.jjan.server.service.JJanService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class JJanController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

    private final JJanService jjanService;

    @GetMapping(path="/word")
    public List<WordEntity> getWordList(@RequestParam(name = "prefix", required = false) String prefix) {
        return jjanService.getWordList(prefix);
    }

    @GetMapping(path="/code/{groupId}")
    public List<CodeEntity> getCodeList(@PathVariable("groupId") String groupId) {
        return jjanService.getCodeList(groupId);
    }

    @GetMapping(path = "/search")
    public ResultMessageDto searchAggregation(@RequestParam("word") String word,
                                     @RequestParam(name = "fromYm", required = false) String fromYm,
                                     @RequestParam(name = "toYm", required = false) String toYm,
                                     @RequestParam("sns") List<String> sns) {
        if(StringUtils.isEmpty(fromYm)) {
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime beforeYear = current.minusYears(1);
            fromYm = beforeYear.format(DateTimeFormatter.ofPattern("yyyyMM"));
        }

        if(StringUtils.isEmpty(toYm)) {
            LocalDateTime current = LocalDateTime.now();
            toYm = current.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
        }

        List<String> snsList = sns.stream().filter((snsCode) -> StringUtils.isNotEmpty(snsCode)).toList();
        AggrDto aggrDto;
        if(snsList.isEmpty()) {
            aggrDto = new AggrDto(word, fromYm, toYm, sns, Collections.emptyList(), Collections.emptyMap());
        } else {
            List<MentionAggrDto> mentionList = jjanService.getMentionList(word, fromYm, toYm, sns);
            Map<String, List<AssociationAggrDto>> associationList = jjanService.getAssociationList(word, fromYm, toYm, sns);
            aggrDto = new AggrDto(word, fromYm, toYm, sns, mentionList, associationList);
        }

        return new ResultMessageDto(aggrDto);
    }
}
