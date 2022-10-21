package com.kailoslab.jjan.server.sometrend;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

@RequiredArgsConstructor
public class WebTabFactory extends BasePooledObjectFactory<WebDriver> {
    private final WebDriver rootWebDriver;
    private String tabName;

    @Override
    public WebDriver create() throws Exception {
        WebDriver firstTab = getFirstTabDriver();
        if(StringUtils.contains(firstTab.getCurrentUrl(), SomeTrendConstants.host)) {
            WebElement body = firstTab.findElement(By.tagName("body"));
            body.sendKeys(Keys.CONTROL + "t");
            ArrayList<String> tabs = new ArrayList<String>(firstTab.getWindowHandles());
            this.tabName = tabs.get(1);
            return firstTab.switchTo().window(this.tabName);
        } else {
            throw new IllegalStateException("This is site of " + SomeTrendConstants.host);
        }
    }

    @Override
    public PooledObject<WebDriver> wrap(WebDriver webDriver) {
        return new DefaultPooledObject<>(webDriver);
    }

    @Override
    public void passivateObject(PooledObject<WebDriver> p) throws Exception {
        p.getObject().get("https://" + SomeTrendConstants.host);
    }

    @Override
    public boolean validateObject(PooledObject<WebDriver> p) {
        return StringUtils.equals(p.getObject().getWindowHandle(), tabName);
    }

    private WebDriver getFirstTabDriver() {
        ArrayList<String> tabs = new ArrayList<String>(rootWebDriver.getWindowHandles());
        return rootWebDriver.switchTo().window(tabs.get(0));
    }
}
