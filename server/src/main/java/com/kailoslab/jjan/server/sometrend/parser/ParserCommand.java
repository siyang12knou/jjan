package com.kailoslab.jjan.server.sometrend.parser;

import com.kailoslab.jjan.server.data.AssociationRepository;
import com.kailoslab.jjan.server.data.CodeRepository;
import com.kailoslab.jjan.server.data.MentionRepository;
import com.kailoslab.jjan.server.data.WordRepository;
import com.kailoslab.jjan.server.data.entity.AssociationEntity;
import com.kailoslab.jjan.server.data.entity.MentionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class ParserCommand {
    @Value("${kailoslab.download-dir}")
    private String downloadDir;
    private final WordRepository wordRepository;
    private final CodeRepository codeRepository;
    private final SomeTrendExcelParser someTrendExcelParser;
    private final AssociationRepository associationRepository;
    private final MentionRepository mentionRepository;

    @ShellMethod("연관어 파일 파싱")
    public void parseAssociation(String word, String sns, String fileName) {
        wordRepository.findByWordAndDeletedFalse(word).ifPresent(wordEntity -> {
            codeRepository.findByGroupIdAndCodeIdAndDeletedFalse("sns", sns).ifPresent(codeEntity -> {
                List<AssociationEntity> list = someTrendExcelParser.parseAssociation(wordEntity.getId(), codeEntity.getCodeId(), downloadDir, fileName);
                associationRepository.saveAllAndFlush(list);
            });
        });
    }

    @ShellMethod("언급량 파일 파싱")
    public void parseMention(String word, String sns, String fileName) {
        wordRepository.findByWordAndDeletedFalse(word).ifPresent(wordEntity -> {
            codeRepository.findByGroupIdAndCodeIdAndDeletedFalse("sns", sns).ifPresent(codeEntity -> {
                List<MentionEntity> list = someTrendExcelParser.parseMention(wordEntity.getId(), codeEntity.getCodeId(), downloadDir, fileName);
                mentionRepository.saveAllAndFlush(list);
            });
        });
    }
}
