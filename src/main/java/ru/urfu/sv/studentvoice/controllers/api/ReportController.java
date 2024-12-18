package ru.urfu.sv.studentvoice.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
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
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping(Links.BASE_API + Links.REPORTS)
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Operation(summary = "Выгрузка отчета по отзывам типа .csv")
    @RequestMapping(path = Links.CSV + Links.DOWNLOAD_REPORT + "/reviews", method = RequestMethod.GET)
    public ResponseEntity<StreamingResponseBody> downloadReport() {
        final String content = reportService.getCvsReport();
        final String fileName = "reviews_report".concat("_")
                .concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
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

    @Operation(summary = "Выгрузка отчета по отзывам типа .xslx")
    @RequestMapping(path = Links.XSLX + Links.DOWNLOAD_REPORT + "/reviews", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadReportXSLXByReviews(HttpServletResponse response) throws IOException {

        final String fileName = "reviews_report".concat("_")
                .concat(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .concat(".xlsx");

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        response.setContentType(HttpHeaders.CONTENT_TYPE);
        final byte[] report = reportService.getReportByReviews();

        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}