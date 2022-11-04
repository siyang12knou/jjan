package com.kailoslab.jjan.server.sometrend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class CollectorCommand {
    private final SomeTrendCollector collector;

    @ShellMethod("전체 수집")
    public void collectAll(@ShellOption(defaultValue = "") String fromIdWord, @ShellOption(defaultValue = "") String fromYmd, @ShellOption(defaultValue = "") String toYmd) {
        if(StringUtils.isEmpty(fromYmd)) {
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime beforeYear = current.minusYears(1);
            fromYmd = beforeYear.format(DateTimeFormatter.ofPattern("yyyyMM")) + "01";
        }

        if(StringUtils.isEmpty(toYmd)) {
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime beforeMonth = current.minusMonths(1);
            toYmd =beforeMonth.format(DateTimeFormatter.ofPattern("yyyyMM")) + YearMonth.from(beforeMonth).atEndOfMonth().getDayOfMonth();
        }

        log.info(collector.collect(fromIdWord, fromYmd, toYmd).getMessage());
    }

    @ShellMethod("키워드 지정 수집")
    public void collectWord(String word, @ShellOption(defaultValue = "") String sns, @ShellOption(defaultValue = "") String fromYmd, @ShellOption(defaultValue = "") String toYmd) {
        if(StringUtils.isEmpty(fromYmd)) {
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime beforeYear = current.minusYears(1);
            fromYmd = beforeYear.format(DateTimeFormatter.ofPattern("yyyyMM")) + "01";
        }

        if(StringUtils.isEmpty(toYmd)) {
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime beforeMonth = current.minusMonths(1);
            toYmd =beforeMonth.format(DateTimeFormatter.ofPattern("yyyyMM")) + YearMonth.from(beforeMonth).atEndOfMonth().getDayOfMonth();
        }

        log.info(collector.collect(word, sns, fromYmd, toYmd).getMessage());
    }
}
