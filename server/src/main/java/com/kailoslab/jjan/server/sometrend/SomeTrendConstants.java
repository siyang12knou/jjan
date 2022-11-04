package com.kailoslab.jjan.server.sometrend;

public interface SomeTrendConstants {

    String host = "some.co.kr";

    String mentionUrl = "https://some.co.kr/analysis/social/mention?keyword=%s&startDate=%s&endDate=%s&sources=%s&excludeRT=true";
    String associateUrl = "https://some.co.kr/analysis/social/association?keyword=%s&startDate=%s&endDate=%s&sources=%s&excludeRT=true";
    String youtubeUrl = "https://some.co.kr/analysis/youtube?keyword=%s&startDate=%s&endDate=%s";

    String mentionWeeklySelector = "label[for=W-sensibility]";
    String btnMentionDownloadSelector = "#mentionExcelDownloadButton";
    String associationWeeklySelector = "label[for=W-sensibility]";
    String btnAssociationDownloadSelector = "#rankingChangeListForAssociationVskeleton > div.layout-card-header > div.layout-card-header-buttons > button";
    String youtubeWeeklySelector = "label[for=W-contents]";
    String btnYoutubeMentionDownloadSelector = "#wrap > div:nth-child(1) > div > aside > div.analysis-section.section-contents-wrap.youtube-transition-image > section > div.card-header > div.card-header-buttons > a";
    String btnYoutubeAssociationDownloadSelector = "#wrap > div:nth-child(1) > div > aside > div.analysis-section.serction-relation-wrap.youtube-association-image > section > div.card-header > div.card-header-buttons > a";
    String mentionDownloadFileName = "[썸트렌드] %s_언급량_%s-%s.xlsx";
    String associationDownloadFileName = "[썸트렌드] %s_연관어 순위 변화_%s-%s.xlsx";
    String youtubeMentionDownloadFileName = "[썸트렌드] %s 콘텐츠조회수추이_%s-%s.xlsx";
    String youtubeAssociationDownloadFileName = "[썸트렌드] %s 유튜브연관어_%s-%s.xlsx";

    String snsGroupId = "sns";
    String youtubeId = "youtube";

}
