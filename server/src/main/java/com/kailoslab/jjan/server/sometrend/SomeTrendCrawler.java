package com.kailoslab.jjan.server.sometrend;

import com.kailoslab.jjan.server.data.dto.ResultMessageDto;
import com.kailoslab.jjan.server.data.entity.WordEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
@Slf4j
public class SomeTrendCrawler {

    private final SomeTrendCrawlerFactory someTrendCrawlerFactory;
    private WordEntity wordEntity;
    private String fromYmd;
    private String toYmd;
    private List<String> snsCodeIds;

    public ResultMessageDto call(WordEntity wordEntity, String fromYmd, String toYmd, List<String> snsCodeIds) {
        this.wordEntity = wordEntity;
        this.fromYmd = fromYmd;
        this.toYmd = toYmd;
        this.snsCodeIds = snsCodeIds;

        if(ObjectUtils.isEmpty(new Object[]{wordEntity, fromYmd, toYmd}) || snsCodeIds.isEmpty()) {
            return new ResultMessageDto(false, "Invalid parameters to collect");
        } else {
            WebDriver tabDriver = null;
            try {
                tabDriver = createNewTab();
                collectMention(tabDriver);
                collectAssociation(tabDriver);
                collectYoutube(tabDriver);
                return new ResultMessageDto(true, String.format("Successful collect data: %s, %s, %s, %s", wordEntity.getWord(), fromYmd, toYmd, snsCodeIds));
            } catch (Throwable e) {
                return new ResultMessageDto(false, String.format("Cannot collect data: %s, %s, %s, %s", wordEntity.getWord(), fromYmd, toYmd, snsCodeIds));
            }finally {
                if(ObjectUtils.isNotEmpty(tabDriver)) {
                    tabDriver.close();
                }
            }
        }
    }

    private void collectMention(WebDriver tabDriver) throws Throwable {
        for (String codeId :
                snsCodeIds) {
            if(!StringUtils.equals(codeId, SomeTrendConstants.youtubeId)) {
                // TODO 언급량 분석 화면으로 이동
                // TODO 주별 버튼 선택
                // TODO 분석데이터 다운로드
                // TODO 분석데이터 엑셀 파일 파싱
                // TODO 언급량 데이터 저장
            }
        }
    }

    private void collectAssociation(WebDriver tabDriver) throws Throwable {
        for (String codeId :
                snsCodeIds) {
            if(!StringUtils.equals(codeId, SomeTrendConstants.youtubeId)) {
                // TODO 연관어 분석 화면으로 이동
                // TODO 주별 버튼 선택
                // TODO 분석데이터 다운로드
                // TODO 분석데이터 엑셀 파일 파싱
                // TODO 연관어 데이터 저장
            }
        }
    }

    private void collectYoutube(WebDriver tabDriver) throws Throwable {
        if(this.snsCodeIds.contains("youtube")) {
            // TODO 유투브 분석 화면으로 이동
            // TODO 주별 버튼 선택
            // TODO 분석데이터 다운로드
            // TODO 분석데이터 엑셀 파일 파싱
            // TODO 유튜브 데이터 저장
        }
    }

    private File getDownloadFile(String downloadFileName) {
        File downloadFile = new File(someTrendCrawlerFactory.getDownloadDir(), downloadFileName);
        if(downloadFile.exists()) {
            return downloadFile;
        } else {
            return null;
        }
    }

    public WebDriver createNewTab() {
        WebDriver firstTab = someTrendCrawlerFactory.getFirstTabDriver();
        if (StringUtils.contains(someTrendCrawlerFactory.getFirstTabDriver().getCurrentUrl(), SomeTrendConstants.host)) {
            WebElement body = firstTab.findElement(By.tagName("body"));
            body.sendKeys(Keys.chord(Keys.SHIFT, Keys.CONTROL, "t"));
            List<String> names = firstTab.getWindowHandles().stream().toList();
            for (int i = 0; i < names.size(); i++) {
                if(StringUtils.equals(names.get(i), firstTab.getWindowHandle())) {
                    if (names.size() >= i + 1) {
                        String newTabName = names.get(i + 1);
                        WebDriver newTab = firstTab.switchTo().window(newTabName);
                        if(StringUtils.contains(newTab.getCurrentUrl(), SomeTrendConstants.host)) {
                            return newTab;
                        }
                    }

                    break;
                }
            }
            throw new IllegalStateException("Cannot create a new tab: " + SomeTrendConstants.host);
        } else {
            throw new IllegalStateException("This is site of " + SomeTrendConstants.host);
        }
    }
}
