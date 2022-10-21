package com.kailoslab.jjan.server.sometrend;

import com.kailoslab.jjan.server.data.AssociationRepository;
import com.kailoslab.jjan.server.data.MentionRepository;
import com.kailoslab.jjan.server.data.entity.CodeEntity;
import com.kailoslab.jjan.server.data.entity.WordEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.ObjectPool;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class SomeTrendCrawler implements Runnable {

    private final MentionRepository mentionRepository;
    private final AssociationRepository associationRepository;
    private final ObjectPool<WebDriver> pool;
    private final WordEntity word;
    private final List<CodeEntity> snsCodeList;

    @Override
    public void run() {
        WebDriver webDriver = null;
        try {
            webDriver = pool.borrowObject();
        } catch (Exception e) {
            log.error("Cannot collect a data of sometrend.");
        } finally {
            if (null != webDriver) {
                try {
                    pool.returnObject(webDriver);
                } catch (Exception ignored) {}
            }
        }
    }

    private void collectMention(WebDriver webDriver) {
        // TODO 언급량 분석 화면으로 이동
        for (CodeEntity code :
                snsCodeList) {
            // TODO youtube skip
            // TODO 주별 버튼 선택
            // TODO 분석데이터 다운로드
            // TODO 분석데이터 엑셀 파일 파싱
            // TODO 언급량 데이터 저장
        }
    }

    private void collectAssociation(WebDriver webDriver) {
        // TODO 연관어 분석 화면으로 이동
        for (CodeEntity code :
                snsCodeList) {
            // TODO youtube skip
            // TODO 주별 버튼 선택
            // TODO 분석데이터 다운로드
            // TODO 분석데이터 엑셀 파일 파싱
            // TODO 연관어 데이터 저장
        }
    }

    private void collectYoutube(WebDriver webDriver) {
        // TODO 유투브 분석 화면으로 이동
        // TODO 주별 버튼 선택
        // TODO 분석데이터 다운로드
        // TODO 분석데이터 엑셀 파일 파싱
        // TODO 유튜브 데이터 저장
    }

    private File getDownloadFile(String downloadFileName) {
        File folder = new File(System.getProperty("user.dir"));
        //List the files on that folder
        File[] listOfFiles = folder.listFiles();
        File downloadFile = null;
        //Look for the file in the files
        // You should write smart REGEX according to the filename
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                String fileName = listOfFile.getName();
                System.out.println("File " + listOfFile.getName());
                if (fileName.matches(downloadFileName)) {
                    downloadFile = new File(fileName);
                }
            }
        }

        return downloadFile;
    }
}
