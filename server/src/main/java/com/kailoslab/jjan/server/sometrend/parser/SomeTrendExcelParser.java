package com.kailoslab.jjan.server.sometrend.parser;

import com.kailoslab.jjan.server.data.entity.AssociationEntity;
import com.kailoslab.jjan.server.data.entity.MentionEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class SomeTrendExcelParser {

    public List<MentionEntity> parseMention(int idWord, String snsCodeId, String downloadPath, String fileName) {
        return parseMention(idWord, snsCodeId, Paths.get(downloadPath, fileName));
    }

    public List<MentionEntity> parseMention(int idWord, String snsCodeId, Path filePath) {
        if(StringUtils.equals(snsCodeId, "youtube")) {
            return parseMentionYoutube(idWord, filePath);
        } else {
            try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
                XSSFSheet sheet = workbook.getSheetAt(0);
                List<MentionEntity> result = new ArrayList<>();
                int rowNum = 14;
                XSSFRow row = sheet.getRow(rowNum);
                XSSFCell cell = row.getCell(0);
                while (cell != null && StringUtils.isNotEmpty(cell.getRawValue())) {
                    String[] ymd = cell.getStringCellValue().split("~");
                    if (ymd.length != 2) {
                        continue;
                    }

                    String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                    String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));
                    cell = row.getCell(2);
                    int cnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                    MentionEntity mentionEntity = new MentionEntity(snsCodeId, idWord, fromYmd, toYmd, cnt);
                    result.add(mentionEntity);
                    rowNum++;
                    row = sheet.getRow(rowNum);
                    cell = row.getCell(0);
                }
                return result;
            } catch (IOException | InvalidFormatException e) {
                log.error("Cannot parse a excel of mention of sometrend: " + filePath.toAbsolutePath());
                return Collections.emptyList();
            }
        }
    }

    public List<MentionEntity> parseMentionYoutube(int idWord, Path filePath) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<MentionEntity> result = new ArrayList<>();
            int rowNum = 14;
            XSSFRow row = sheet.getRow(rowNum);
            XSSFCell cell = row.getCell(0);
            while(cell != null && StringUtils.isNotEmpty(cell.getRawValue())) {
                String[] ymd = cell.getStringCellValue().split("~");
                if(ymd.length != 2) {
                    continue;
                }

                String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));
                cell = row.getCell(2);
                int subCnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                cell = row.getCell(3);
                int cnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                MentionEntity mentionEntity = new MentionEntity("youtube", idWord, fromYmd, toYmd, cnt, subCnt);
                result.add(mentionEntity);
                rowNum ++;
                row = sheet.getRow(rowNum);
                cell = row.getCell(0);
            }
            return result;
        } catch (IOException | InvalidFormatException e) {
            log.error("Cannot parse a excel of mention of sometrend: " + filePath.toAbsolutePath());
            return Collections.emptyList();
        }
    }

    public List<AssociationEntity> parseAssociation(int idWord, String snsCodeId, String downloadPath, String fileName) {
        return parseAssociation(idWord, snsCodeId, Paths.get(downloadPath, fileName));
    }

    public List<AssociationEntity> parseAssociation(int idWord, String snsCodeId, Path filePath) {
        if(StringUtils.equals(snsCodeId, "youtube")) {
            return parseAssociationYoutube(idWord, filePath);
        } else {
            try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
                XSSFSheet sheet = workbook.getSheetAt(0);
                List<AssociationEntity> result = new ArrayList<>();
                int ymdRowNum = 13;
                XSSFRow ymdRow = sheet.getRow(ymdRowNum);
                int cells = ymdRow.getPhysicalNumberOfCells();
                for (int i = 0; i < cells; i++) {
                    XSSFCell ymdCell = ymdRow.getCell(i);
                    if (ymdCell != null && StringUtils.isNotEmpty(ymdCell.getRawValue())) {
                        String[] ymd = ymdCell.getStringCellValue().split("~");
                        if (ymd.length != 2) {
                            continue;
                        }

                        String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                        String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));
                        int j = ymdRowNum + 2;
                        XSSFRow wordRow = sheet.getRow(j);
                        XSSFCell wordCell = wordRow.getCell(i);
                        while (wordCell != null && StringUtils.isNotEmpty(wordCell.getRawValue())) {
                            String word = wordCell.getStringCellValue();
                            int cnt = Double.valueOf(wordRow.getCell(i + 1).getNumericCellValue()).intValue();
                            AssociationEntity associationEntity = new AssociationEntity(snsCodeId, idWord, word, fromYmd, toYmd, cnt);
                            result.add(associationEntity);
                            j++;
                            wordRow = sheet.getRow(j);
                            wordCell = wordRow.getCell(i);
                        }
                    }
                }
                return result;
            } catch (IOException | InvalidFormatException e) {
                log.error("Cannot parse a excel of association of sometrend: " + filePath.toAbsolutePath());
                return Collections.emptyList();
            }
        }
    }

    public List<AssociationEntity> parseAssociationYoutube(int idWord, Path filePath) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<AssociationEntity> result = new ArrayList<>();
            XSSFRow ymdRow = sheet.getRow(8);
            XSSFCell ymdCell = ymdRow.getCell(1);
            if(ymdCell != null && StringUtils.isNotEmpty(ymdCell.getRawValue())) {
                String[] ymd = ymdCell.getStringCellValue().split("~");
                if (ymd.length != 2) {
                    return Collections.emptyList();
                }

                String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));

                int rowNum = 14;
                XSSFRow row = sheet.getRow(rowNum);
                XSSFCell cell = row.getCell(1);
                while (cell != null && StringUtils.isNotEmpty(cell.getRawValue())) {
                    String word = cell.getStringCellValue();
                    cell = row.getCell(2);
                    int cnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                    AssociationEntity associationEntity = new AssociationEntity("youtube", idWord, word, fromYmd, toYmd, cnt);
                    result.add(associationEntity);
                    rowNum++;
                    row = sheet.getRow(rowNum);
                    cell = row.getCell(1);
                }
            }
            return result;
        } catch (IOException | InvalidFormatException e) {
            log.error("Cannot parse a excel of mention of sometrend: " + filePath.toAbsolutePath());
            return Collections.emptyList();
        }
    }
}
