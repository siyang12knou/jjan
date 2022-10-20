package com.kailoslab.jjan.server.service;

import com.kailoslab.jjan.server.data.AssociationRepository;
import com.kailoslab.jjan.server.data.CodeRepository;
import com.kailoslab.jjan.server.data.MentionRepository;
import com.kailoslab.jjan.server.data.WordRepository;
import com.kailoslab.jjan.server.data.dto.AssociationAggrDto;
import com.kailoslab.jjan.server.data.dto.MentionAggrDto;
import com.kailoslab.jjan.server.data.entity.CodeEntity;
import com.kailoslab.jjan.server.data.entity.WordEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Service
public class JJanService {
    private final WordRepository wordRepository;
    private final CodeRepository codeRepository;
    private final MentionRepository mentionRepository;
    private final AssociationRepository associationRepository;

    public List<WordEntity> getWordList() {
        return getWordList(null);
    }

    public List<WordEntity> getWordList(String prefix) {
        if(StringUtils.isAllBlank(prefix)) {
            return wordRepository.findAll();
        } else {
            return wordRepository.findAllByWordStartsWith(prefix);
        }
    }

    public List<CodeEntity> getCodeList(String groupId) {
        return codeRepository.findByGroupIdAndDeletedFalseOrderByOrdinal(groupId);
    }

    public List<MentionAggrDto> getMentionList(String word, String fromYm, String toYm, List<String> sns) {
        return mentionRepository.findAggrByWordAndYear(word, fromYm + "01", toYm + "31", sns);
    }

    public Map<String, List<AssociationAggrDto>> getAssociationList(String word, String fromYm, String toYm, List<String> sns) {
        List<AssociationAggrDto> list = associationRepository.findAggrByWordAndYear(word, fromYm + "01", toYm + "31", sns);
        Map<String, List<AssociationAggrDto>> result = new HashMap<>(sns.size());
        for(AssociationAggrDto associationAggrDto: list) {
            List<AssociationAggrDto> snsList = result.computeIfAbsent(associationAggrDto.getSns(), k -> new ArrayList<>());
            snsList.add(associationAggrDto);
        }

        result.forEach((snsCodeId, snsResult) -> {
            snsResult.sort((aggrDto1, aggrDto2) -> aggrDto2.getCnt() - aggrDto1.getCnt());
        });

        return result;
    }

    public List<MentionAggrDto> getDummyMentionList(String word, String fromYm, String toYm, List<String> sns) {
        LocalDate indexDateTime = LocalDate.parse(fromYm + "01", DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate toDateTime = LocalDate.parse(toYm + "31", DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<MentionAggrDto> result = new ArrayList<>();
        while(indexDateTime.getYear() != toDateTime.getYear() || indexDateTime.getMonth() != toDateTime.getMonth()) {
            String ym = indexDateTime.format(DateTimeFormatter.ofPattern("yyyyMM"));
            for (String snsCode :
                    sns) {
                if(StringUtils.isEmpty(snsCode)) {
                    continue;
                }

                int cnt = ThreadLocalRandom.current().nextInt(100, 400 + 1);
                result.add(new MentionAggrDto() {
                    @Override
                    public String getSns() {
                        return snsCode;
                    }

                    @Override
                    public String getWord() {
                        return word;
                    }

                    @Override
                    public Integer getCnt() {
                        return cnt;
                    }

                    @Override
                    public String getYm() {
                        return ym;
                    }
                });
            }

            indexDateTime = indexDateTime.plusMonths(1);
        }

        return result;
    }

    public Map<String, List<AssociationAggrDto>> getDummyAssociationList(String word, String fromYm, String toYm, List<String> sns) {
        Map<String, List<AssociationAggrDto>> result = new HashMap<>();
        for (String snsCode :
                sns) {
            if(StringUtils.isEmpty(snsCode)) {
                continue;
            }

            List<AssociationAggrDto> snsResult = result.computeIfAbsent(snsCode, (k) -> new ArrayList<>());
            for (int i = 0; i < 10; i++) {
                int cnt = ThreadLocalRandom.current().nextInt(100, 400 + 1);
                String associatedWord = generateRandomWord();
                snsResult.add(new AssociationAggrDto() {
                    @Override
                    public String getSns() {
                        return snsCode;
                    }

                    @Override
                    public String getWord() {
                        return word;
                    }

                    @Override
                    public String getAssociatedWord() {
                        return associatedWord;
                    }

                    @Override
                    public Integer getCnt() {
                        return cnt;
                    }
                });
            }

            snsResult.sort((aggrDto1, aggrDto2) -> aggrDto2.getCnt() - aggrDto1.getCnt());
        }

        return result;
    }

    private String generateRandomWord()
    {
        Random random = new Random();
        char[] word = new char[random.nextInt(5)+3];
        for(int i = 0; i < word.length; i++)
        {
            word[i] = (char)(0xAC00 + (Math.random() * 11172));
        }
        return new String(word);
    }
}
