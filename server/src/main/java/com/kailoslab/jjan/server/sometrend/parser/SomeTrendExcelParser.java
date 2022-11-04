package com.kailoslab.jjan.server.sometrend.parser;

import com.kailoslab.jjan.server.data.AssociationRepository;
import com.kailoslab.jjan.server.data.MentionRepository;
import com.kailoslab.jjan.server.data.entity.AssociationEntity;
import com.kailoslab.jjan.server.data.entity.MentionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
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
@RequiredArgsConstructor
@Component
public class SomeTrendExcelParser {

    private final MentionRepository mentionRepository;
    private final AssociationRepository associationRepository;

    public List<MentionEntity> parseMention(int idWord, String snsCodeId, String downloadPath, String fileName) {
        return parseMention(idWord, snsCodeId, Paths.get(downloadPath, fileName));
    }

    public List<MentionEntity> parseMention(int idWord, String snsCodeId, Path filePath) {
        if(StringUtils.equals(snsCodeId, "youtube")) {
            return parseMentionYoutube(idWord, filePath);
        } else {
            try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
                log.info("Start to parse a mention: " + filePath.toAbsolutePath());
                XSSFSheet sheet = workbook.getSheetAt(0);
                List<MentionEntity> result = new ArrayList<>();
                int rowNum = 14;
                XSSFRow row = sheet.getRow(rowNum);
                if(ObjectUtils.isNotEmpty(row)) {
                    XSSFCell cell = row.getCell(0);
                    while (cell != null && StringUtils.isNotEmpty(cell.getRawValue())) {
                        String[] ymd = cell.getStringCellValue().split("~");
                        if (ymd.length != 2) {
                            log.error("Cannot parse a period of week: " + filePath);
                            break;
                        }

                        String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                        String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));
                        cell = row.getCell(2);
                        int cnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                        MentionEntity mentionEntity = new MentionEntity(snsCodeId, idWord, fromYmd, toYmd, cnt);
                        result.add(mentionEntity);
                        rowNum++;
                        row = sheet.getRow(rowNum);
                        if(ObjectUtils.isNotEmpty(row)) {
                            cell = row.getCell(0);
                        } else {
                            break;
                        }
                    }
                }
                log.info("End to parse a mention: " + filePath.toAbsolutePath());
                return result;
            } catch (IOException | InvalidFormatException e) {
                log.error("Cannot parse a excel of mention: " + filePath.toAbsolutePath());
                return Collections.emptyList();
            }
        }
    }

    public void parseAndSaveMention(int idWord, String snsCodeId, Path filePath) {
        if(StringUtils.equals(snsCodeId, "youtube")) {
            parseAndSaveMentionYoutube(idWord, filePath);
        } else {
            try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
                log.info("Start to parse a mention: " + filePath.toAbsolutePath());
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowNum = 14;
                XSSFRow row = sheet.getRow(rowNum);
                if(ObjectUtils.isNotEmpty(row)) {
                    XSSFCell cell = row.getCell(0);
                    while (cell != null && StringUtils.isNotEmpty(cell.getRawValue())) {
                        String[] ymd = cell.getStringCellValue().split("~");
                        if (ymd.length != 2) {
                            log.error("Cannot parse a period of week: " + filePath);
                            break;
                        }

                        String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                        String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));
                        cell = row.getCell(2);
                        int cnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                        MentionEntity mentionEntity = new MentionEntity(snsCodeId, idWord, fromYmd, toYmd, cnt);
                        mentionRepository.saveAndFlush(mentionEntity);
                        rowNum++;
                        row = sheet.getRow(rowNum);
                        if(ObjectUtils.isNotEmpty(row)) {
                            cell = row.getCell(0);
                        } else {
                            break;
                        }
                    }
                }
                log.info("End to parse a mention: " + filePath.toAbsolutePath());
            } catch (IOException | InvalidFormatException e) {
                log.error("Cannot parse a excel of mention: " + filePath.toAbsolutePath());
            }
        }
    }

    public List<MentionEntity> parseMentionYoutube(int idWord, Path filePath) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
            log.info("Start to parse a mention of youtube: " + filePath.toAbsolutePath());
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<MentionEntity> result = new ArrayList<>();
            int rowNum = 14;
            XSSFRow row = sheet.getRow(rowNum);
            if(ObjectUtils.isNotEmpty(row)) {
                XSSFCell cell = row.getCell(0);
                while (cell != null && StringUtils.isNotEmpty(cell.getRawValue())) {
                    String[] ymd = cell.getStringCellValue().split("~");
                    if (ymd.length != 2) {
                        log.error("Cannot parse a period of week: " + filePath);
                        break;
                    }

                    String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                    String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));
                    cell = row.getCell(2);
                    int cnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                    cell = row.getCell(3);
                    int subCnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                    MentionEntity mentionEntity = new MentionEntity("youtube", idWord, fromYmd, toYmd, cnt, subCnt);
                    result.add(mentionEntity);
                    rowNum++;
                    row = sheet.getRow(rowNum);
                    if(ObjectUtils.isNotEmpty(row)) {
                        cell = row.getCell(0);
                    } else {
                        break;
                    }
                }
            }
            log.info("End to parse a mention of youtube: " + filePath.toAbsolutePath());
            return result;
        } catch (IOException | InvalidFormatException e) {
            log.error("Cannot parse a excel of mention of youtube: " + filePath.toAbsolutePath());
            return Collections.emptyList();
        }
    }

    public void parseAndSaveMentionYoutube(int idWord, Path filePath) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
            log.info("Start to parse a mention of youtube: " + filePath.toAbsolutePath());
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowNum = 14;
            XSSFRow row = sheet.getRow(rowNum);
            if(ObjectUtils.isNotEmpty(row)) {
                XSSFCell cell = row.getCell(0);
                while (cell != null && StringUtils.isNotEmpty(cell.getRawValue())) {
                    String[] ymd = cell.getStringCellValue().split("~");
                    if (ymd.length != 2) {
                        log.error("Cannot parse a period of week: " + filePath);
                        break;
                    }

                    String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                    String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));
                    cell = row.getCell(2);
                    int cnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                    cell = row.getCell(3);
                    int subCnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                    MentionEntity mentionEntity = new MentionEntity("youtube", idWord, fromYmd, toYmd, cnt, subCnt);
                    mentionRepository.saveAndFlush(mentionEntity);
                    rowNum++;
                    row = sheet.getRow(rowNum);
                    if(ObjectUtils.isNotEmpty(row)) {
                        cell = row.getCell(0);
                    } else {
                        break;
                    }
                }
            }
            log.info("End to parse a mention of youtube: " + filePath.toAbsolutePath());
        } catch (IOException | InvalidFormatException e) {
            log.error("Cannot parse a excel of mention of youtube: " + filePath.toAbsolutePath());
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
                log.info("Start to parse a association: " + filePath.toAbsolutePath());
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
                            log.error("Cannot parse a period of week: " + filePath);
                            break;
                        }

                        String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                        String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));
                        int j = ymdRowNum + 2;
                        XSSFRow wordRow = sheet.getRow(j);
                        if(ObjectUtils.isNotEmpty(wordRow)) {
                            XSSFCell wordCell = wordRow.getCell(i);
                            while (wordCell != null && StringUtils.isNotEmpty(wordCell.getRawValue())) {
                                String word = wordCell.getStringCellValue();
                                int cnt = Double.valueOf(wordRow.getCell(i + 1).getNumericCellValue()).intValue();
                                AssociationEntity associationEntity = new AssociationEntity(snsCodeId, idWord, word, fromYmd, toYmd, cnt);
                                result.add(associationEntity);
                                j++;
                                wordRow = sheet.getRow(j);
                                if(ObjectUtils.isNotEmpty(wordRow)) {
                                    wordCell = wordRow.getCell(i);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
                log.info("End to parse a association: " + filePath.toAbsolutePath());
                return result;
            } catch (IOException | InvalidFormatException e) {
                log.error("Cannot parse a excel of association: " + filePath.toAbsolutePath());
                return Collections.emptyList();
            }
        }
    }

    public void parseAndSaveAssociation(int idWord, String snsCodeId, Path filePath) {
        if(StringUtils.equals(snsCodeId, "youtube")) {
            parseAndSaveAssociationYoutube(idWord, filePath);
        } else {
            try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
                log.info("Start to parse a association: " + filePath.toAbsolutePath());
                XSSFSheet sheet = workbook.getSheetAt(0);
                int ymdRowNum = 13;
                XSSFRow ymdRow = sheet.getRow(ymdRowNum);
                int cells = ymdRow.getPhysicalNumberOfCells();
                for (int i = 0; i < cells; i++) {
                    XSSFCell ymdCell = ymdRow.getCell(i);
                    if (ymdCell != null && StringUtils.isNotEmpty(ymdCell.getRawValue())) {
                        String[] ymd = ymdCell.getStringCellValue().split("~");
                        if (ymd.length != 2) {
                            log.error("Cannot parse a period of week: " + filePath);
                            break;
                        }

                        String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                        String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));
                        int j = ymdRowNum + 2;
                        XSSFRow wordRow = sheet.getRow(j);
                        if(ObjectUtils.isNotEmpty(wordRow)) {
                            XSSFCell wordCell = wordRow.getCell(i);
                            while (wordCell != null && StringUtils.isNotEmpty(wordCell.getRawValue())) {
                                String word = wordCell.getStringCellValue();
                                int cnt = Double.valueOf(wordRow.getCell(i + 1).getNumericCellValue()).intValue();
                                AssociationEntity associationEntity = new AssociationEntity(snsCodeId, idWord, word, fromYmd, toYmd, cnt);
                                associationRepository.saveAndFlush(associationEntity);
                                j++;
                                wordRow = sheet.getRow(j);
                                if(ObjectUtils.isNotEmpty(wordRow)) {
                                    wordCell = wordRow.getCell(i);
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
                log.info("End to parse a association: " + filePath.toAbsolutePath());
            } catch (IOException | InvalidFormatException e) {
                log.error("Cannot parse a excel of association: " + filePath.toAbsolutePath());
            }
        }
    }

    public List<AssociationEntity> parseAssociationYoutube(int idWord, Path filePath) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
            log.info("Start to parse a association of youtube: " + filePath.toAbsolutePath());
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<AssociationEntity> result = new ArrayList<>();
            XSSFRow ymdRow = sheet.getRow(8);
            XSSFCell ymdCell = ymdRow.getCell(1);
            if(ymdCell != null && StringUtils.isNotEmpty(ymdCell.getRawValue())) {
                String[] ymd = ymdCell.getStringCellValue().split("~");
                if (ymd.length != 2) {
                    log.error("Cannot parse a period of week: " + filePath);
                    return Collections.emptyList();
                }

                String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));

                int rowNum = 14;
                XSSFRow row = sheet.getRow(rowNum);
                if(ObjectUtils.isNotEmpty(row)) {
                    XSSFCell cell = row.getCell(1);
                    while (cell != null && StringUtils.isNotEmpty(cell.getRawValue())) {
                        String word = cell.getStringCellValue();
                        cell = row.getCell(2);
                        int cnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                        AssociationEntity associationEntity = new AssociationEntity("youtube", idWord, word, fromYmd, toYmd, cnt);
                        result.add(associationEntity);
                        rowNum++;
                        row = sheet.getRow(rowNum);
                        if(ObjectUtils.isNotEmpty(row)) {
                            cell = row.getCell(1);
                        } else {
                            break;
                        }
                    }
                }
            }
            log.info("End to parse a association of youtube: " + filePath.toAbsolutePath());
            return result;
        } catch (IOException | InvalidFormatException e) {
            log.error("Cannot parse a excel of mention of youtube: " + filePath.toAbsolutePath());
            return Collections.emptyList();
        }
    }

    public void parseAndSaveAssociationYoutube(int idWord, Path filePath) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(filePath.toFile())) {
            log.info("Start to parse a association of youtube: " + filePath.toAbsolutePath());
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow ymdRow = sheet.getRow(8);
            XSSFCell ymdCell = ymdRow.getCell(1);
            if(ymdCell != null && StringUtils.isNotEmpty(ymdCell.getRawValue())) {
                String[] ymd = ymdCell.getStringCellValue().split("~");
                if (ymd.length != 2) {
                    log.error("Cannot parse a period of week: " + filePath);
                    return;
                }

                String fromYmd = StringUtils.trimToEmpty(ymd[0].replace(".", ""));
                String toYmd = StringUtils.trimToEmpty(ymd[1].replace(".", ""));

                int rowNum = 14;
                XSSFRow row = sheet.getRow(rowNum);
                if(ObjectUtils.isNotEmpty(row)) {
                    XSSFCell cell = row.getCell(1);
                    while (cell != null && StringUtils.isNotEmpty(cell.getRawValue())) {
                        String word = cell.getStringCellValue();
                        cell = row.getCell(2);
                        int cnt = Double.valueOf(cell.getNumericCellValue()).intValue();
                        AssociationEntity associationEntity = new AssociationEntity("youtube", idWord, word, fromYmd, toYmd, cnt);
                        associationRepository.saveAndFlush(associationEntity);
                        rowNum++;
                        row = sheet.getRow(rowNum);
                        if(ObjectUtils.isNotEmpty(row)) {
                            cell = row.getCell(1);
                        } else {
                            break;
                        }
                    }
                }
            }
            log.info("End to parse a association of youtube: " + filePath.toAbsolutePath());
        } catch (IOException | InvalidFormatException e) {
            log.error("Cannot parse a excel of mention of youtube: " + filePath.toAbsolutePath());
        }
    }
}
