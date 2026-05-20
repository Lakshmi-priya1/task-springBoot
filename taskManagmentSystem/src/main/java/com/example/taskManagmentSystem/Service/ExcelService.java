package com.example.taskManagmentSystem.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.taskManagmentSystem.Dto.Request.EmployeeRequest;
import com.example.taskManagmentSystem.Dto.Response.EmployeeResponse;
import com.example.taskManagmentSystem.Exception.BadRequestException;
import com.example.taskManagmentSystem.Payload.EmployeeStatus;

@Service
public class ExcelService {

    public List<EmployeeRequest> parseExcel(MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".xlsx")) {
            throw new BadRequestException("Only .xlsx files are supported");
        }
        List<EmployeeRequest> requests = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                EmployeeRequest req = new EmployeeRequest();
                req.setUsername(getCellValue(row, 0));
                req.setEmail(getCellValue(row, 1));
                req.setFirstName(getCellValue(row, 2));
                req.setLastName(getCellValue(row, 3));
                req.setDepartment(getCellValue(row, 4));
                req.setStatus(EmployeeStatus.valueOf(getCellValue(row, 5).toUpperCase()));
                req.setPhoneNumber(getCellValue(row, 6));
                req.setPassword(getCellValue(row, 7));
                String orgIdStr = getCellValue(row, 8);
                if (orgIdStr != null && !orgIdStr.isBlank()) {
                    req.setOrgId(Long.parseLong(orgIdStr));
                }
                requests.add(req);
            }
        } catch (IOException e) {
            throw new BadRequestException("Failed to parse Excel file: " + e.getMessage());
        }
        return requests;
    }

    public byte[] exportToExcel(List<EmployeeResponse> employees) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("Employees");

            // ── Freeze header rows ─────────────────────────────────
            sheet.createFreezePane(0, 2);

            // ── Column widths ──────────────────────────────────────
            sheet.setColumnWidth(0, 16 * 256); // employeeCode
            sheet.setColumnWidth(1, 18 * 256); // username
            sheet.setColumnWidth(2, 16 * 256); // firstName
            sheet.setColumnWidth(3, 16 * 256); // lastName
            sheet.setColumnWidth(4, 28 * 256); // email
            sheet.setColumnWidth(5, 18 * 256); // phoneNumber
            sheet.setColumnWidth(6, 18 * 256); // department
            sheet.setColumnWidth(7, 18 * 256); // role
            sheet.setColumnWidth(8, 22 * 256); // companyName

            // ── Title row ──────────────────────────────────────────
            Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(40);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("  Employee Report");
            titleCell.setCellStyle(makeTitleStyle(workbook));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

            // ── Header row ─────────────────────────────────────────
            String[] exportHeaders = {
                "Employee Code", "Username", "First Name", "Last Name",
                "Email", "Phone Number", "Department", "Role", "Company Name"
            };
            Row headerRow = sheet.createRow(1);
            headerRow.setHeightInPoints(24);
            CellStyle headerStyle = makeHeaderStyle(workbook);
            for (int i = 0; i < exportHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(exportHeaders[i]);
                cell.setCellStyle(headerStyle);
            }
            CellStyle evenStyle     = makeRowStyle(workbook, false);
            CellStyle oddStyle      = makeRowStyle(workbook, true);
            CellStyle activeStyle   = makeStatusStyle(workbook, true);
            CellStyle inactiveStyle = makeStatusStyle(workbook, false);
            CellStyle roleStyle     = makeRoleStyle(workbook);

            int rowIdx = 2;
            for (EmployeeResponse emp : employees) {
                Row row = sheet.createRow(rowIdx);
                row.setHeightInPoints(20);
                CellStyle base = (rowIdx % 2 == 0) ? evenStyle : oddStyle;

                // col 0 — Employee Code (bold)
                Cell codeCell = row.createCell(0);
                codeCell.setCellValue(emp.getEmployeeCode() != null ? emp.getEmployeeCode() : "");
                codeCell.setCellStyle(makeCodeStyle(workbook, rowIdx % 2 == 0));

                // col 1–4 — normal fields
                setCellStyled(row, 1, emp.getUsername(),    base);
                setCellStyled(row, 2, emp.getFirstName(),   base);
                setCellStyled(row, 3, emp.getLastName(),    base);
                setCellStyled(row, 4, emp.getEmail(),       base);
                setCellStyled(row, 5, emp.getPhoneNumber(), base);
                setCellStyled(row, 6, emp.getDepartment(),  base);

                // col 7 — Role (styled)
                Cell roleCell = row.createCell(7);
                roleCell.setCellValue(emp.getRole() != null ? emp.getRole().name() : "");
                roleCell.setCellStyle(roleStyle);

                // col 8 — Company Name
                setCellStyled(row, 8, emp.getCompanyName(), base);

                rowIdx++;
            }

            // ── Summary row ────────────────────────────────────────
            Row summaryRow = sheet.createRow(rowIdx + 1);
            summaryRow.setHeightInPoints(20);
            Cell summaryCell = summaryRow.createCell(0);
            summaryCell.setCellValue("Total Employees: " + employees.size());
            summaryCell.setCellStyle(makeSummaryStyle(workbook));
            sheet.addMergedRegion(new CellRangeAddress(rowIdx + 1, rowIdx + 1, 0, 8));

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file: " + e.getMessage());
        }
    }

    // ── Style Helpers ──────────────────────────────────────────────

    private CellStyle makeTitleStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setColor(new XSSFColor(new byte[]{(byte)255,(byte)255,(byte)255}, null));
        style.setFont(font);
        // Indigo #6366f1
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte)99,(byte)102,(byte)241}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle makeHeaderStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(new XSSFColor(new byte[]{(byte)255,(byte)255,(byte)255}, null));
        style.setFont(font);
        // Darker indigo #4f46e5
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte)79,(byte)70,(byte)229}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBottomBorderColor(new XSSFColor(new byte[]{(byte)199,(byte)210,(byte)254}, null));
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(new XSSFColor(new byte[]{(byte)199,(byte)210,(byte)254}, null));
        return style;
    }

    private CellStyle makeRowStyle(XSSFWorkbook wb, boolean odd) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(new XSSFColor(new byte[]{(byte)30,(byte)27,(byte)75}, null));
        style.setFont(font);
        // even = white, odd = light indigo #eef2ff
        style.setFillForegroundColor(new XSSFColor(
            odd ? new byte[]{(byte)238,(byte)242,(byte)255}
                : new byte[]{(byte)255,(byte)255,(byte)255}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setLeftBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        style.setRightBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        return style;
    }

    private CellStyle makeCodeStyle(XSSFWorkbook wb, boolean even) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        // Indigo text for code
        font.setColor(new XSSFColor(new byte[]{(byte)79,(byte)70,(byte)229}, null));
        style.setFont(font);
        style.setFillForegroundColor(new XSSFColor(
            even ? new byte[]{(byte)255,(byte)255,(byte)255}
                 : new byte[]{(byte)238,(byte)242,(byte)255}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setLeftBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        style.setRightBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        return style;
    }

    private CellStyle makeStatusStyle(XSSFWorkbook wb, boolean active) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        // green for active, red for inactive
        font.setColor(new XSSFColor(
            active ? new byte[]{(byte)22,(byte)163,(byte)74}
                   : new byte[]{(byte)185,(byte)28,(byte)28}, null));
        style.setFont(font);
        style.setFillForegroundColor(new XSSFColor(
            active ? new byte[]{(byte)220,(byte)252,(byte)231}
                   : new byte[]{(byte)254,(byte)226,(byte)226}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setLeftBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        style.setRightBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        return style;
    }

    private CellStyle makeRoleStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        // Purple text #7c3aed
        font.setColor(new XSSFColor(new byte[]{(byte)124,(byte)58,(byte)237}, null));
        style.setFont(font);
        // Light purple bg #f5f3ff
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte)245,(byte)243,(byte)255}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setLeftBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        style.setRightBorderColor(new XSSFColor(new byte[]{(byte)224,(byte)231,(byte)255}, null));
        return style;
    }

    private CellStyle makeSummaryStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(new XSSFColor(new byte[]{(byte)79,(byte)70,(byte)229}, null));
        style.setFont(font);
        // Very light indigo #eef2ff
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte)238,(byte)242,(byte)255}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setTopBorderColor(new XSSFColor(new byte[]{(byte)99,(byte)102,(byte)241}, null));
        return style;
    }

    private void setCellStyled(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    private String getCellValue(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default      -> "";
        };
    }
}