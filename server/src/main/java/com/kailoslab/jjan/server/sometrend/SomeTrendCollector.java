package com.kailoslab.jjan.server.sometrend;

import com.kailoslab.jjan.server.data.WordRepository;
import com.kailoslab.jjan.server.data.entity.WordEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class SomeTrendCollector {

    @Value("${webdriver.chrome.driver}")
    private String driverPath;
    @Value("${kailoslab.download-dir}")
    private String downloadDir;
    private WebDriver webDriver;

    private final WordRepository wordRepository;

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
        }
    }

    public void start() {
        if(webDriver == null) {
            log.error("Cannot start because webdriver is not initialized.");
            return;
        }

        if(login()) {
            List<WordEntity> wordList = wordRepository.findAllByDeletedFalse();
            wordList.forEach(word -> {
                
            });
        }
    }

    private boolean login() {
        if(webDriver == null) {
            return false;
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
