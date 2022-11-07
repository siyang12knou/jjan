package com.kailoslab.jjan.server.sometrend;

import com.kailoslab.jjan.server.data.dto.ResultMessageDto;
import com.kailoslab.jjan.server.data.entity.AssociationEntity;
import com.kailoslab.jjan.server.data.entity.MentionEntity;
import com.kailoslab.jjan.server.data.entity.WordEntity;
import com.kailoslab.jjan.server.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class SomeTrendCrawler {

    private final SomeTrendCollector someTrendCollector;
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
            try {
                WebDriver webDriver = someTrendCollector.getWebDriver();
                log.info(String.format("Start to collect data: %s, %s, %s, %s", wordEntity.getWord(), fromYmd, toYmd, snsCodeIds));

                collectMention(webDriver);
                collectAssociation(webDriver);
                collectYoutube(webDriver);

                log.info(String.format("End to collect data: %s, %s, %s, %s", wordEntity.getWord(), fromYmd, toYmd, snsCodeIds));
                return new ResultMessageDto(true, String.format("Successful collect data: %s, %s, %s, %s", wordEntity.getWord(), fromYmd, toYmd, snsCodeIds));
            } catch (Throwable e) {
                log.error(String.format("Raised a exception while collect data: %s, %s, %s, %s", wordEntity.getWord(), fromYmd, toYmd, snsCodeIds), e);
                return new ResultMessageDto(false, String.format("Cannot collect data: %s, %s, %s, %s", wordEntity.getWord(), fromYmd, toYmd, snsCodeIds));
            }
        }
    }

    private void collectMention(WebDriver tabDriver) {
        for (String codeId :
                snsCodeIds) {
            if(!StringUtils.equals(codeId, SomeTrendConstants.youtubeId)) {
                // 언급량 분석 화면으로 이동
                String url = String.format(SomeTrendConstants.mentionUrl, wordEntity.getWord(), fromYmd, toYmd, codeId);
                tabDriver.get(url);
                closeAllPopup(tabDriver);
                // 주별 버튼 선택
                WebDriverWait wait = new WebDriverWait(tabDriver, Duration.ofSeconds(20));
                WebElement radioWeek = Utils.waitUntil(wait, ExpectedConditions.elementToBeClickable(By.cssSelector(SomeTrendConstants.mentionWeeklySelector)));
                if(radioWeek != null) {
                    radioWeek.click();
                    closeAllPopup(tabDriver);
                    waitTime(tabDriver);
                    if(isNotModal(tabDriver)) {
                        // 분석데이터 다운로드
                        WebElement btnDownload = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(SomeTrendConstants.btnMentionDownloadSelector)));
                        btnDownload.click();
                        // 분석데이터 엑셀 파일 파싱
                        String fileName = String.format(SomeTrendConstants.mentionDownloadFileName, wordEntity.getWord(), fromYmd.substring(2), toYmd.substring(2));
                        Path downloadFile = getDownloadFile(tabDriver, fileName, codeId);
                        if (downloadFile != null) {
                            someTrendCollector.getExecutorService().submit(() -> {
                                List<MentionEntity> mentionEntityList = someTrendCollector.getSomeTrendExcelParser().parseMention(wordEntity.getId(), codeId, downloadFile);
                                someTrendCollector.getCollectorDataService().saveMentionList(mentionEntityList);
                                log.info("Saved mentions: " + downloadFile);
                                try {
                                    Files.delete(downloadFile);
                                } catch (IOException e) {
                                    log.error("Cannot delete a file:" + downloadFile, e);
                                }
                            });
                        } else {
                            log.warn("Cannot find a download file of mention: " + fileName);
                        }
                    } else {
                        log.warn("No found data of mention: " + wordEntity.getWord());
                    }
                } else {
                    log.warn("Invalid page of mention: " + url);
                }
            }
        }
    }

    private void collectAssociation(WebDriver tabDriver) {
        for (String codeId :
                snsCodeIds) {
            if(!StringUtils.equals(codeId, SomeTrendConstants.youtubeId)) {
                // 연관어 분석 화면으로 이동
                String url = String.format(SomeTrendConstants.associateUrl, wordEntity.getWord(), fromYmd, toYmd, codeId);
                tabDriver.get(url);
                closeAllPopup(tabDriver);
                // 주별 버튼 선택
                WebDriverWait wait = new WebDriverWait(tabDriver, Duration.ofSeconds(20));
                WebElement radioWeek = Utils.waitUntil(wait, ExpectedConditions.elementToBeClickable(By.cssSelector(SomeTrendConstants.associationWeeklySelector)));
                if(radioWeek != null) {
                    radioWeek.click();
                    closeAllPopup(tabDriver);
                    waitTime(tabDriver);
                    if(isNotModal(tabDriver)) {
                        // 분석데이터 다운로드
                        WebElement btnDownload = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(SomeTrendConstants.btnAssociationDownloadSelector)));
                        btnDownload.click();
                        // 분석데이터 엑셀 파일 파싱
                        String fileName = String.format(SomeTrendConstants.associationDownloadFileName, wordEntity.getWord(), fromYmd.substring(2), toYmd.substring(2));
                        Path downloadFile = getDownloadFile(tabDriver, fileName, codeId);
                        if (downloadFile != null) {
                            someTrendCollector.getExecutorService().submit(() -> {
                                // 연관어 데이터 저장
                                List<AssociationEntity> associationEntityList = someTrendCollector.getSomeTrendExcelParser().parseAssociation(wordEntity.getId(), codeId, downloadFile);
                                someTrendCollector.getCollectorDataService().saveAssociationList(associationEntityList);
                                log.info("Saved associations: " + downloadFile);
                                try {
                                    Files.delete(downloadFile);
                                } catch (IOException e) {
                                    log.error("Cannot delete da file:" + downloadFile, e);
                                }
                            });
                        } else {
                            log.warn("Cannot find a download file of association: " + fileName);
                        }
                    } else {
                        log.warn("No found data of association: " + wordEntity.getWord());
                    }
                } else {
                    log.warn("Invalid page of association: " + url);
                }
            }
        }
    }

    private void collectYoutube(WebDriver tabDriver) {
        if(this.snsCodeIds.contains(SomeTrendConstants.youtubeId)) {
            // 유투브 분석 화면으로 이동
            tabDriver.get("https://some.co.kr/analysis/youtube");
            closeAllPopup(tabDriver);
            String url = String.format(SomeTrendConstants.youtubeUrl, wordEntity.getWord(), fromYmd, toYmd);
            tabDriver.get(url);
            closeAllPopup(tabDriver);
            // 주별 버튼 선택
            WebDriverWait wait = new WebDriverWait(tabDriver, Duration.ofSeconds(20));
            WebElement radioWeek = Utils.waitUntil(wait, ExpectedConditions.elementToBeClickable(By.cssSelector(SomeTrendConstants.youtubeWeeklySelector)));
            if(radioWeek != null) {
                radioWeek.click();
                closeAllPopup(tabDriver);
                waitTime(tabDriver);
                if(isNotModal(tabDriver)) {
                    // 언급량 분석데이터 다운로드
                    WebElement btnMentionDownload = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(SomeTrendConstants.btnYoutubeMentionDownloadSelector)));
                    btnMentionDownload.click();
                    // 언급량 분석데이터 엑셀 파일 파싱
                    String mentionFileName = String.format(SomeTrendConstants.youtubeMentionDownloadFileName, wordEntity.getWord(), fromYmd.substring(2), toYmd.substring(2));
                    Path mentionDownloadFile = getDownloadFile(tabDriver, mentionFileName, SomeTrendConstants.youtubeId);
                    if (mentionDownloadFile != null) {
                        someTrendCollector.getExecutorService().submit(() -> {
                            List<MentionEntity> mentionEntityList = someTrendCollector.getSomeTrendExcelParser().parseMentionYoutube(wordEntity.getId(), mentionDownloadFile);
                            someTrendCollector.getCollectorDataService().saveMentionList(mentionEntityList);
                            log.info("Saved mentions: " + mentionDownloadFile);
                            try {
                                Files.delete(mentionDownloadFile);
                            } catch (IOException e) {
                                log.error("Cannot delete da file:" + mentionDownloadFile, e);
                            }
                        });
                    } else {
                        log.warn("Cannot find a download file of mention: " + mentionFileName);
                    }

                    // 연관어 분석데이터 다운로드
                    WebElement btnAssociationDownload = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(SomeTrendConstants.btnYoutubeAssociationDownloadSelector)));
                    btnAssociationDownload.click();
                    // 연관어 분석데이터 엑셀 파일 파싱
                    String associationFileName = String.format(SomeTrendConstants.youtubeAssociationDownloadFileName, wordEntity.getWord(), fromYmd.substring(2), toYmd.substring(2));
                    Path associationDownloadFile = getDownloadFile(tabDriver, associationFileName, SomeTrendConstants.youtubeId);
                    if (associationDownloadFile != null) {
                        someTrendCollector.getExecutorService().submit(() -> {
                            List<AssociationEntity> associationEntityList = someTrendCollector.getSomeTrendExcelParser().parseAssociationYoutube(wordEntity.getId(), associationDownloadFile);
                            someTrendCollector.getCollectorDataService().saveAssociationList(associationEntityList);
                            log.info("Saved associations: " + associationFileName);
                            try {
                                Files.delete(associationDownloadFile);
                            } catch (IOException e) {
                                log.error("Cannot delete da file:" + associationDownloadFile, e);
                            }
                        });
                    } else {
                        log.warn("Cannot find a download file of association: " + associationFileName);
                    }
                } else {
                    log.warn("No found data of youtube: " + wordEntity.getWord());
                }
            } else {
                log.warn("Invalid page of youtube: " + url);
            }
        }
    }

    private Path getDownloadFile(WebDriver tabDriver, String downloadFileName, String snsCodeId) {
        WebDriverWait wait = new WebDriverWait(tabDriver, Duration.ofSeconds(20));
        boolean isExist = Utils.waitUntilBoolean(wait, (ExpectedCondition<Boolean>) webDriver -> Files.exists(Paths.get(someTrendCollector.getDownloadDir(), downloadFileName)));
        if(isExist) {
            Path sourcePath = Paths.get(someTrendCollector.getDownloadDir(), downloadFileName);
            try {
                return Files.move(
                        sourcePath,
                        Paths.get(someTrendCollector.getDownloadDir(), snsCodeId + downloadFileName),
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                log.error("Cannot rename: " + downloadFileName);
                return null;
            }
        } else {
            return null;
        }
    }

    private void closeAllPopup(WebDriver webDriver) {
        List<WebElement> popupList = webDriver.findElements(By.cssSelector(".popup_banner_container"));
        popupList.forEach(popup -> {
            try {
                WebElement btnClose = popup.findElement(By.cssSelector(".close-button-for-period"));
                btnClose.click();
            } catch (Throwable ignore) {}
        });

        List<WebElement> guidePopupList = webDriver.findElements(By.cssSelector(".vguide-pop"));
        guidePopupList.forEach(popup -> {
            try {
                WebElement btnClose = popup.findElement(By.tagName("a"));
                btnClose.click();
                WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.invisibilityOf(btnClose));
            } catch (Throwable ignore) {}
        });
    }

    private void waitTime(WebDriver tabDriver) {
        LocalDateTime current = LocalDateTime.now();
        WebDriverWait wait = new WebDriverWait(tabDriver, Duration.ofSeconds(5));
        wait.until((ExpectedCondition<Boolean>) webDriver -> Duration.between(current, LocalDateTime.now()).getSeconds() > 3);
    }

    private boolean isNotModal(WebDriver webDriver) {
        List<WebElement> popupModalContent = webDriver.findElements(By.cssSelector(".popup_container"));
        popupModalContent.addAll(webDriver.findElements(By.cssSelector(".vmodal.effect-scale.popup_content")));
        popupModalContent.forEach(popup -> {
            try {
                WebElement btnClose = popup.findElement(By.cssSelector(".vmodal-close"));
                btnClose.click();
            } catch (Throwable ignore) {}
        });

        return popupModalContent.isEmpty();
    }
}
