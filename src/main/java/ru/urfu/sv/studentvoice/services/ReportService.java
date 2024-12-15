package ru.urfu.sv.studentvoice.services;

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
import ru.urfu.sv.studentvoice.model.domain.dto.report.ReviewReport;
import ru.urfu.sv.studentvoice.model.query.ReportQuery;
import ru.urfu.sv.studentvoice.utils.excel.ExcelUtil;
import ru.urfu.sv.studentvoice.utils.formatters.CsvFormatter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static ru.urfu.sv.studentvoice.utils.formatters.DateFormatter.DATE_FORMATTER_WITH_HOURS_AND_MINUTES;

@Slf4j
@Service
public class ReportService {

    @Autowired
    private ReportQuery reportQuery;

    /**
     * Создаем отчет CSV по отзывам
     */
    public String getCvsReport() {
        final List<ReviewReport> reviewInfoList = reportQuery.findDataForReportReviews();
        return CsvFormatter.toCsv(reviewInfoList,
                "Предмет;Пара;Дата начала;Дата окончания;Институт;ФИО студента;Дата создания отзыва\n");
    }

    /**
     * Создаем отчет Excel по отзывам
     */
    @Transactional
    public byte[] getReportByReviews() throws IOException {

        final List<ReviewReport> reviewInfoList = reportQuery.findDataForReportReviews();

        if (reviewInfoList.isEmpty()) {
            return null;
        }

        try (final SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            final SXSSFSheet sheet = workbook.createSheet("Отзывы");

            ExcelUtil.addTitleStyles(workbook);
            final CellStyle titleStyle = workbook.getCellStyleAt(1);

            final Row rowTitle = sheet.createRow(0);
            int cell = 0;
            rowTitle.createCell(cell++).setCellValue("Предмет");
            rowTitle.createCell(cell++).setCellValue("Пара");
            rowTitle.createCell(cell++).setCellValue("Дата начала");
            rowTitle.createCell(cell++).setCellValue("Дата окончания");
            rowTitle.createCell(cell++).setCellValue("Институт");
            rowTitle.createCell(cell++).setCellValue("ФИО студента");
            rowTitle.createCell(cell).setCellValue("Дата создания отзыва");

            final int columnLastCellNum = rowTitle.getLastCellNum();

            int rowNumber = 1;
            for (final ReviewReport reviewInfo : reviewInfoList) {

                final Row rowData = sheet.createRow(rowNumber++);
                int cellData = 0;

                rowData.createCell(cellData++).setCellValue(reviewInfo.getCourseName());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getLessonName());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getStartDateTime().format(DATE_FORMATTER_WITH_HOURS_AND_MINUTES));
                rowData.createCell(cellData++).setCellValue(reviewInfo.getEndDateTime().format(DATE_FORMATTER_WITH_HOURS_AND_MINUTES));
                rowData.createCell(cellData++).setCellValue(reviewInfo.getInstituteName());
                rowData.createCell(cellData++).setCellValue(reviewInfo.getStudentName());
                rowData.createCell(cellData).setCellValue(reviewInfo.getCreateReview().format(DATE_FORMATTER_WITH_HOURS_AND_MINUTES));
            }

            sheet.trackAllColumnsForAutoSizing();
            sheet.setAutoFilter(CellRangeAddress.valueOf("A" + ":" + "G"));
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