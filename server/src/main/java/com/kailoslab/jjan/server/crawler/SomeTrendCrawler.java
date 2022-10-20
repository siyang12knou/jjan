package com.kailoslab.jjan.server.crawler;

import com.kailoslab.jjan.server.data.WordRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Slf4j
@Component
public class SomeTrendCrawler {

    @Value("${webdriver.chrome.driver}")
    private String driverPath;
    private WebDriver webDriver;

    private final WordRepository wordRepository;

    @PostConstruct
    private void init() {
        if(StringUtils.isNotEmpty(driverPath)) {
            System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driverPath);
            webDriver = new ChromeDriver();
        }
    }

    public void start() {

    }
}
