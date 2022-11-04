package com.kailoslab.jjan.server.sometrend;

import com.kailoslab.jjan.server.data.CodeRepository;
import com.kailoslab.jjan.server.data.WordRepository;
import com.kailoslab.jjan.server.data.dto.ResultMessageDto;
import com.kailoslab.jjan.server.data.entity.CodeEntity;
import com.kailoslab.jjan.server.data.entity.WordEntity;
import com.kailoslab.jjan.server.sometrend.parser.SomeTrendExcelParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
@Slf4j
@Component
public class SomeTrendCollector {

    @Value("${webdriver.browser.driver}")
    private String driverPath;
    @Value("${webdriver.browser.path}")
    private String chromePath;

    private final WordRepository wordRepository;
    @Getter
    private final ExecutorService executorService;
    @Getter
    @Value("${kailoslab.download-dir}")
    private String downloadDir;
    @Getter
    private WebDriver webDriver;
    @Getter
    private final CodeRepository codeRepository;
    @Getter
    private final SomeTrendExcelParser someTrendExcelParser;
    @Getter
    private final CollectorDataService collectorDataService;

    public ResultMessageDto collect(String fromIdWord, String fromYmd, String toYmd) {
        createWebDriver();
        if(webDriver == null) {
            log.error("Cannot start because webdriver is not initialized.");
            return new ResultMessageDto(false, "Cannot start because webdriver is not initialized.");
        }

        List<WordEntity> wordList;
        if(StringUtils.isEmpty(fromIdWord)) {
            wordList = wordRepository.findAllByDeletedFalse();
        } else {
            try {
                wordList = wordRepository.findAllByIdGreaterThanEqualAndDeletedFalse(Integer.parseInt(fromIdWord));
            } catch (NumberFormatException e) {
                wordList = wordRepository.findAllByDeletedFalse();
            }
        }

        List<ResultMessageDto> resultMessageDtoList = new ArrayList<>();
        wordList.forEach(word -> resultMessageDtoList.add(collect(word, getSnsCodeIds(), fromYmd, toYmd)));
        boolean result = true;
        String message = ResultMessageDto.MESSAGE_OK;
        List<Object> data = new ArrayList<>();
        for(ResultMessageDto resultMessageDto: resultMessageDtoList) {
            if(!resultMessageDto.isResult()) {
                result = false;
                message = "Cannot collect some data.";
                data.add(resultMessageDto.getMessage());
            }
        }

        quit();

        return new ResultMessageDto(result, message, data);
    }

    public ResultMessageDto collect(String word, String snsCodeId, String fromYmd, String toYmd) {
        createWebDriver();
        WordEntity wordEntity;
        try{
            int wordId = Integer.parseInt(word);
            wordEntity = wordRepository.findByIdAndDeletedFalse(wordId).get();
        } catch (NumberFormatException e) {
            Optional<WordEntity> wordEntityOptional = wordRepository.findByWordAndDeletedFalse(word);
            wordEntity = wordEntityOptional.orElseGet(() -> wordRepository.saveAndFlush(new WordEntity(word)));
        }

        ResultMessageDto result = collect(wordEntity,
                StringUtils.isEmpty(snsCodeId) ? getSnsCodeIds() : Collections.singletonList(snsCodeId),
                fromYmd, toYmd);

        quit();

        return result;
    }

    private ResultMessageDto collect(WordEntity wordEntity, List<String> snsCodeIds, String fromYmd, String toYmd) {
        if(ObjectUtils.anyNull(wordEntity, snsCodeIds, fromYmd, toYmd)) {
            return new ResultMessageDto(false, "Invalid parameter to collect");
        }

        if(login()) {
            collectorDataService.clearData(wordEntity, snsCodeIds, fromYmd, toYmd);
            try {
                SomeTrendCrawler crawler = new SomeTrendCrawler(this);
                return crawler.call(wordEntity, fromYmd, toYmd, snsCodeIds);
            } catch (Exception e) {
                log.error("", e);
                return new ResultMessageDto(false, String.format("Cannot collect: %s, %s, %s, %s", wordEntity.getWord(), snsCodeIds, fromYmd, toYmd));
            }
        } else {
            return new ResultMessageDto(false, "Cannot login the sometrend.");
        }
    }

    private void createWebDriver() {
        if(StringUtils.isNotEmpty(driverPath)) {
            if(StringUtils.contains(driverPath, "chrome")) {
                if(StringUtils.isEmpty(chromePath)) {
                    System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverPath);
                    HashMap<String, Object> chromePrefs = new HashMap<>();
                    chromePrefs.put("profile.default_content_settings.popups", 0);
                    chromePrefs.put("download.default_directory", downloadDir);
                    ChromeOptions options = new ChromeOptions();
                    options.setExperimentalOption("prefs", chromePrefs);
                    webDriver = new ChromeDriver(options);
                    webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
                } else {
                    System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverPath);
                    ChromeOptions options = new ChromeOptions();
                    options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
                    webDriver = new ChromeDriver(options);
                    webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
                }
            } else if(StringUtils.contains(driverPath, "edge")) {
                System.setProperty(EdgeDriverService.EDGE_DRIVER_EXE_PROPERTY, driverPath);
                HashMap<String, Object> edgePrefs = new HashMap<>();
                edgePrefs.put("profile.default_content_settings.popups", 0);
                edgePrefs.put("download.default_directory", downloadDir);
                EdgeOptions options = new EdgeOptions();
                options.setExperimentalOption("prefs", edgePrefs);
                webDriver = new EdgeDriver(options);
                webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
            }
        }
    }

    private boolean login() {
        if(webDriver == null) {
            return false;
        }

        webDriver.get(MessageFormat.format("https://{0}", SomeTrendConstants.host));
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".topMenu-list")));
        try {
            WebElement btnLogout = webDriver.findElement(By.cssSelector(".btn-logout"));
            if (ObjectUtils.isNotEmpty(btnLogout)) {
                return true;
            }
        } catch (NoSuchElementException e) {
            log.info("Start login of " + SomeTrendConstants.host);
        }

        try {
            webDriver.get(MessageFormat.format("https://{0}/login", SomeTrendConstants.host));
            WebElement username = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
            username.sendKeys("kailoslab@gmail.com");
            WebElement password = webDriver.findElement(By.name("password"));
            password.sendKeys("kailos0601!");
            WebElement btnLogin = webDriver.findElement(By.id("kc-login"));
            btnLogin.click();

            List<WebElement> modalMsg = webDriver.findElements(By.cssSelector("#j-popup-9907605 > article > div.vmodal-body > div > div"));
            if(!ObjectUtils.isEmpty(modalMsg) && StringUtils.contains(modalMsg.get(0).getText(), "잘못 입력")){
                WebElement btnClose = webDriver.findElement(By.cssSelector("#j-popup-9907605 > article > div.vmodal-footer > div:nth-child(1) > div > div > button"));
                btnClose.click();
                log.error(modalMsg.get(0).getText());
                return false;
            }

            List<WebElement> alertDuplicatedLogin = webDriver.findElements(By.cssSelector("h5.confirmPop_title"));
            if (!ObjectUtils.isEmpty(alertDuplicatedLogin) &&
                StringUtils.startsWith(alertDuplicatedLogin.get(0).getText(), "중복")) {
                WebElement btnConfirm = webDriver.findElement(By.cssSelector("a[href='/logout/others']"));
                btnConfirm.click();
            }

            log.info("Login "+ SomeTrendConstants.host);
            return true;
        } catch (Throwable ex) {
            log.error("Cannot login", ex);
            return false;
        }
    }

    private List<String> getSnsCodeIds() {
        List<CodeEntity> snsCodeList = codeRepository.findByGroupIdAndDeletedFalseOrderByOrdinal(SomeTrendConstants.snsGroupId);
        return snsCodeList.stream().map(CodeEntity::getCodeId).toList();
    }

    private void quit() {
        if(webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }
}
