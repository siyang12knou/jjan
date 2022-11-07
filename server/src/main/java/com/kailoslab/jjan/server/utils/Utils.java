package com.kailoslab.jjan.server.utils;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Function;

public class Utils {
    public static WebElement waitUntil(WebDriverWait wait, Function<? super WebDriver, WebElement> isTrue) {
        try {
            return wait.until(isTrue);
        } catch (TimeoutException e) {
            return null;
        }
    }

    public static Boolean waitUntilBoolean(WebDriverWait wait, Function<? super WebDriver, Boolean> isTrue) {
        try {
            return wait.until(isTrue);
        } catch (TimeoutException e) {
            return false;
        }
    }
}
