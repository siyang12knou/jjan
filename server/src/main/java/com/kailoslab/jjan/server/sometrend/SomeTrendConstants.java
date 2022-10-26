package com.kailoslab.jjan.server.sometrend;

public interface SomeTrendConstants {

    String host = "some.co.kr";

    String mentionUrl = "https://some.co.kr/analysis/social/mention?keyword={0}&startDate={1}&endDate={2}&sources={3}&excludeRT=true";
    String associateUrl = "https://some.co.kr/analysis/social/association?keyword={0}&startDate={1}&endDate={2}&sources={3}&excludeRT=true";
    String youtubeUrl = "https://some.co.kr/analysis/youtube?keyword={0}&startDate={1}&endDate={2}";

    String mentionWeeklySelector = "#W-sensibility";
    String btnMentionDownloadSelector = "#mentionExcelDownloadButton";
    String associationWeeklySelector = "#W-sensibility";
    String btnAssociationDownloadSelector = "#rankingChangeListForAssociationVskeleton > div.layout-card-header > div.layout-card-header-buttons > button";

    String mentionDownloadFileName = "[썸트렌드] {0}_언급량_{1}-{2}.xlsx";
    String associationDownloadFileName = "[썸트렌드] {0}_연관어 순위 변화_{1}-{2}.xlsx";
    String youtubeMentionDownloadFileName = "[썸트렌드] {0} 콘텐츠조회수추이_{1}-{2}.xlsx";
    String youtubeAssociationDownloadFileName = "[썸트렌드] {0} 유튜브연관어_{1}-{2}.xlsx";

    String snsGroupId = "sns";
    String youtubeId = "youtube";

}
