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

import java.util.Arrays;
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
    public void parseAssociation() {
        String[][] params = {
//                {"짠", "insta", "[썸트렌드] 짠_insta_연관어 순위 변화_211001-220930.xlsx"},
//                {"소주", "insta", "[썸트렌드] 소주_insta_연관어 순위 변화_211001-220930.xlsx"},
//                {"짠", "blog", "[썸트렌드] 짠_blog_연관어 순위 변화_211001-220930.xlsx"},
//                {"소주", "blog", "[썸트렌드] 소주_blog_연관어 순위 변화_211001-220930.xlsx"},
//                {"짠", "twitter", "[썸트렌드] 짠_twitter_연관어 순위 변화_211001-220930.xlsx"},
//                {"소주", "twitter", "[썸트렌드] 소주_twitter_연관어 순위 변화_211001-220930.xlsx"},
                {"짠", "youtube", "[썸트렌드] 소주 유튜브연관어_211001-220930.xlsx"},
                {"소주", "youtube", "[썸트렌드] 짠 유튜브연관어_211001-220930.xlsx"},
        };
        Arrays.stream(params).forEach(param -> {
            String word = param[0];
            String sns = param[1];
            String fileName = param[2];
            wordRepository.findByWordAndDeletedFalse(word).ifPresent(wordEntity -> {
                codeRepository.findByGroupIdAndCodeIdAndDeletedFalse("sns", sns).ifPresent(codeEntity -> {
                    List<AssociationEntity> list = someTrendExcelParser.parseAssociation(wordEntity.getId(), codeEntity.getCodeId(), downloadDir, fileName);
                    associationRepository.saveAllAndFlush(list);
                    log.info(String.format("Saved %s, %s, %s", word, sns, fileName));
                });
            });
        });
    }

    @ShellMethod("언급량 파일 파싱")
    public void parseMention() {
        String[][] params = {
                {"짠", "youtube", "[썸트렌드] 짠 콘텐츠조회수추이_211001-220930.xlsx"},
                {"소주", "youtube", "[썸트렌드] 소주 콘텐츠조회수추이_211001-220930.xlsx"},
        };
        Arrays.stream(params).forEach(param -> {
            String word = param[0];
            String sns = param[1];
            String fileName = param[2];
            wordRepository.findByWordAndDeletedFalse(word).ifPresent(wordEntity -> {
                codeRepository.findByGroupIdAndCodeIdAndDeletedFalse("sns", sns).ifPresent(codeEntity -> {
                    List<MentionEntity> list = someTrendExcelParser.parseMention(wordEntity.getId(), codeEntity.getCodeId(), downloadDir, fileName);
                    mentionRepository.saveAllAndFlush(list);
                });
            });
        });
    }
}
