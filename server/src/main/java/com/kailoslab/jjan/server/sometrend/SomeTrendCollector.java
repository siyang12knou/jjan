package com.kailoslab.jjan.server.sometrend;

import com.kailoslab.jjan.server.data.WordRepository;
import com.kailoslab.jjan.server.data.dto.ResultMessageDto;
import com.kailoslab.jjan.server.data.entity.WordEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Slf4j
@Component
public class SomeTrendCollector {

    @Value("${webdriver.chrome.driver}")
    private String driverPath;
    @Getter
    @Value("${kailoslab.download-dir}")
    private String downloadDir;
    private WebDriver webDriver;
    private GenericObjectPool<SomeTrendCrawler> pool;

    private final WordRepository wordRepository;
    private final SomeTrendCrawlerFactory someTrendCrawlerFactory;
    private final ExecutorService executorService;

    @PostConstruct
    private void init() {
        if(StringUtils.isNotEmpty(driverPath)) {
            System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverPath);
            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("download.default_directory", downloadDir);
            ChromeOptions options = new ChromeOptions();
            options.setExperimentalOption("prefs", chromePrefs);
            webDriver = new ChromeDriver(options);
            someTrendCrawlerFactory.setFirstTabDriver(webDriver);
            pool = new GenericObjectPool<>(someTrendCrawlerFactory);
        }
    }

    @PreDestroy
    private void quit() {
        webDriver.quit();
    }

    public ResultMessageDto collect(String fromYmd, String toYmd) {
        if(webDriver == null) {
            log.error("Cannot start because webdriver is not initialized.");
            return new ResultMessageDto(false, "Cannot start because webdriver is not initialized.");
        }

        if(login()) {
            List<WordEntity> wordList = wordRepository.findAllByDeletedFalse();
            List<Future<ResultMessageDto>> futureList = new ArrayList<>();
            wordList.forEach(word -> {
                futureList.add(executorService.submit(() -> collect(word, fromYmd, toYmd, null)));
            });
            boolean result = true;
            String message = ResultMessageDto.MESSAGE_OK;
            List<Object> data = new ArrayList<>();
            for(Future<ResultMessageDto> future: futureList) {
                try {
                    ResultMessageDto resultMessageDto = future.get();
                    if(!resultMessageDto.isResult()) {
                        result = false;
                        message = "Cannot collect some data.";
                        data.add(resultMessageDto.getMessage());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    result = false;
                    message = "Cannot get a result of collector";
                    log.error(message, e);
                }
            }

            return new ResultMessageDto(result, message, data);
        } else {
            return new ResultMessageDto(false, "Cannot login the sometrend.");
        }
    }

    public ResultMessageDto collect(String word, String snsCodeId, String fromYmd, String toYmd) throws Exception {
        Optional<WordEntity> wordEntityOptional = wordRepository.findByWordAndDeletedFalse(word);
        WordEntity wordEntity = wordEntityOptional.orElseGet(() -> wordRepository.saveAndFlush(new WordEntity(word)));
        return collect(wordEntity, fromYmd, toYmd, Collections.singletonList(snsCodeId));
    }

    public ResultMessageDto collect(WordEntity wordEntity, String fromYmd, String toYmd, List<String> snsCodeIds) throws Exception {
        SomeTrendCrawler crawler = pool.borrowObject();
        ResultMessageDto resultMessageDto = crawler.call(wordEntity, fromYmd, toYmd, snsCodeIds);
        pool.returnObject(crawler);
        return resultMessageDto;
    }

    private boolean login() {
        if(webDriver == null) {
            return false;
        }

        WebElement btnLogout = webDriver.findElement(By.cssSelector(".btn-logout"));
        if(ObjectUtils.isNotEmpty(btnLogout)) {
            return true;
        }

        try {
            webDriver.get(MessageFormat.format("https://{0}/login", SomeTrendConstants.host));
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            WebElement username = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
            username.sendKeys("kailoslab@gmail.com");
            WebElement password = webDriver.findElement(By.name("password"));
            password.sendKeys("kailos0601!");
            WebElement btnLogin = webDriver.findElement(By.id("kc-login"));
            btnLogin.click();

            WebElement modalMsg = webDriver.findElement(By.cssSelector("#j-popup-9907605 > article > div.vmodal-body > div > div"));
            if(!ObjectUtils.isEmpty(modalMsg) && StringUtils.contains(modalMsg.getText(), "잘못 입력")){
                WebElement btnClose = webDriver.findElement(By.cssSelector("#j-popup-9907605 > article > div.vmodal-footer > div:nth-child(1) > div > div > button"));
                btnClose.click();
                log.error(modalMsg.getText());
                return false;
            }

            WebElement alertDuplicatedLogin = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h5.confirmPop_title")));
            if (!ObjectUtils.isEmpty(alertDuplicatedLogin) &&
                StringUtils.startsWith(alertDuplicatedLogin.getText(), "중복")) {
                WebElement btnConfirm = webDriver.findElement(By.cssSelector("a[href='/logout/others']"));
                btnConfirm.click();
            }

            return true;
        } catch (Throwable ex) {
            log.error("Cannot login", ex);
            return false;
        }
    }
}
