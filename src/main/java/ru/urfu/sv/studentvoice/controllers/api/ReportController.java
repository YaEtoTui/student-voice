package ru.urfu.sv.studentvoice.controllers.api;

import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.urfu.sv.studentvoice.controllers.links.Links;
import ru.urfu.sv.studentvoice.services.ReportService;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Контроллер, принимающий запросы по выгрузке отчетов
 *
 * @author Egor Sazhin
 * @since 16.11.2024
 */
@RestController
@RequestMapping(Links.BASE_API + Links.REPORTS)
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(path = "/download-report", method = RequestMethod.GET)
    public ResponseEntity<StreamingResponseBody> downloadReport() {
        final String content = reportService.getCvsReport();
        final String fileName = "reviews_report".concat("_")
                .concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm")))
                .concat(".csv");

        final StreamingResponseBody responseBody = outputStream -> {
            try (final PrintWriter writer = new PrintWriter(outputStream)) {
                writer.write(content);
            }
        };

        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8");

        return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
    }

    @RequestMapping(path = "/download-report-xslx", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadReportXSLX(HttpServletResponse response) throws IOException {

        final String fileName = "reviews_report".concat("_")
                .concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm")))
                .concat(".xlsx");

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        response.setContentType(HttpHeaders.CONTENT_TYPE);
        final byte[] report = reportService.getReport();

        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}