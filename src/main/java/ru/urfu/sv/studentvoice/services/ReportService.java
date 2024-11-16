package ru.urfu.sv.studentvoice.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.sv.studentvoice.utils.excel.ExcelUtil;
import ru.urfu.sv.studentvoice.utils.formatters.CsvFormatter;
import ru.urfu.sv.studentvoice.utils.model.ReviewInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.List;

import static ru.urfu.sv.studentvoice.utils.formatters.DateFormatter.DATE_FORMATTER_WITH_HOURS_AND_MINUTES;

@Slf4j
@Service
public class ReportService {

    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    public String getCvsReport() {
        List<ReviewInfo> reviewInfos = entityManager.createNamedQuery("findAllReviewInfo", ReviewInfo.class).getResultList();
        return CsvFormatter.toCsv(reviewInfos,
                "id;review_value;student_name;review_comment;session_name;course_name;professors;professor_name;institute_name;institute_address;room_name;create_timestamp\n");
    }

    private List<ReviewInfo> getXlsxReport() {

        //To Do

        return entityManager.createNamedQuery("findAllReviewInfo", ReviewInfo.class).getResultList();
    }

    /**
     * Создаем отчет Excel по выгрузке пар
     */
    @Transactional
    public byte[] getReport() throws IOException {

        final List<ReviewInfo> reviewInfoList = getXlsxReport();
        if (reviewInfoList.isEmpty()) {
            return null;
        }

        try (final SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            final SXSSFSheet sheet = workbook.createSheet("Лист1");

            ExcelUtil.addTitleStyles(workbook);
            final CellStyle titleStyle = workbook.getCellStyleAt(1);

            final Row rowTitle = sheet.createRow(0);
            int cell = 0;
            rowTitle.createCell(cell++).setCellValue("id");
            rowTitle.createCell(cell++).setCellValue("review_value");
            rowTitle.createCell(cell++).setCellValue("student_name");
            rowTitle.createCell(cell++).setCellValue("review_comment");
            rowTitle.createCell(cell++).setCellValue("session_name");
            rowTitle.createCell(cell++).setCellValue("course_name");
            rowTitle.createCell(cell++).setCellValue("professors");
            rowTitle.createCell(cell++).setCellValue("professor_name");
            rowTitle.createCell(cell++).setCellValue("institute_name");
            rowTitle.createCell(cell++).setCellValue("institute_address");
            rowTitle.createCell(cell++).setCellValue("room_name");
            rowTitle.createCell(cell).setCellValue("create_timestamp");

            final int columnLastCellNum = rowTitle.getLastCellNum();

            int rowNumber = 1;
            for (final ReviewInfo reviewInfo : reviewInfoList) {

                final Row rowData = sheet.createRow(rowNumber++);
                int cellData = 0;

                rowData.createCell(cellData++).setCellValue(reviewInfo.getId().toString());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getValue());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getStudentName());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getComment());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getSessionName());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getCourseName());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getProfessors());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getProfessorName());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getInstituteName());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getInstituteAddress());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getRoomName());
                rowData.createCell(cellData).setCellValue(reviewInfo.getTimestamp().atZone(ZoneId.systemDefault()).format(DATE_FORMATTER_WITH_HOURS_AND_MINUTES));
            }

            sheet.trackAllColumnsForAutoSizing();
            sheet.setAutoFilter(CellRangeAddress.valueOf("A" + ":" + "L"));
            for (int i = 0; i < columnLastCellNum; i++) {
                final Cell cellTitle = rowTitle.getCell(i);
                cellTitle.setCellStyle(titleStyle);
                sheet.autoSizeColumn(i);
            }
            sheet.untrackAllColumnsForAutoSizing();

            try (final ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                workbook.write(stream);
                return stream.toByteArray();
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}