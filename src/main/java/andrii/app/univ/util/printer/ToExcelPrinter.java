package andrii.app.univ.util.printer;

import andrii.app.univ.entity.expr.Executable;
import andrii.app.univ.entity.expr.Expression;
import andrii.app.univ.entity.expr.ExpressionConstructItem;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ToExcelPrinter {

    public static String printMap(List<Map<String, String>> data, List<String> errorTab, List<Executable> expressions) {
        File saveTo = null;
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        try {
            if (!data.isEmpty()) {
                firstTab(data, workbook);
                identifiersTab(data, workbook);
                constantsTab(data, workbook);
            }
            if (!errorTab.isEmpty()) {
                errorTab(errorTab, workbook);
            }
            if (!expressions.isEmpty()) {
                reverseExprBuildingTab(expressions, workbook);
            }

            int addIdx = 0;
            while (saveTo == null) {
                File temp = new File("report" + String.valueOf(addIdx++) + ".xlsx");
                if (!temp.exists()) {
                    saveTo = temp;
                }
            }

            FileOutputStream out = new FileOutputStream(saveTo);
            workbook.write(out);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            workbook.dispose();
        }
        return saveTo.getAbsolutePath();
    }

    private static void errorTab(List<String> errorData, SXSSFWorkbook workbook) {
        SXSSFSheet sheet4 = workbook.createSheet("Помилки");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFont(headerFont);
        CellStyle generalStyle = workbook.createCellStyle();
        generalStyle.setAlignment(HorizontalAlignment.CENTER);
        sheet4.trackAllColumnsForAutoSizing();

        SXSSFRow headerRow = sheet4.createRow(0);
        headerRow.createCell(0).setCellValue("Помилки");
        headerRow.getCell(0).setCellStyle(headerStyle);

        for (int i = 0; i < errorData.size(); i++) {
            SXSSFRow row = sheet4.createRow(i + 1);
            row.createCell(0).setCellValue(errorData.get(i));
            row.getCell(0).setCellStyle(generalStyle);
        }

        sheet4.autoSizeColumn(0);
    }

    private static void firstTab(List<Map<String, String>> data, SXSSFWorkbook workbook) {
        SXSSFSheet sheet1 = workbook.createSheet("Вихідна таблиця лексем");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFont(headerFont);
        CellStyle generalStyle = workbook.createCellStyle();
        generalStyle.setAlignment(HorizontalAlignment.CENTER);

        SXSSFRow headerRow = sheet1.createRow(0);
        List<String> headers = new ArrayList<>(data.get(0).keySet());

        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        for (int i = 0; i < data.size(); i++) {
            SXSSFRow row = sheet1.createRow(i + 1);
            for (int j = 0; j < headers.size(); j++) {
                row.createCell(j).setCellValue(data.get(i).get(headers.get(j)));
                row.getCell(j).setCellStyle(generalStyle);
            }
        }

        for (int i = 0; i <= headers.size(); i++) {
            sheet1.setColumnWidth(i, 5000);
        }
    }

    private static void identifiersTab(List<Map<String, String>> data, SXSSFWorkbook workbook) {
        SXSSFSheet sheet2 = workbook.createSheet("Таблиця ідентифікаторів");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFont(headerFont);
        CellStyle generalStyle = workbook.createCellStyle();
        generalStyle.setAlignment(HorizontalAlignment.CENTER);

        SXSSFRow headerRow = sheet2.createRow(0);
        List<String> headers = Arrays.asList("Ідентифікатор", "Індекс", "Тип");

        List<Map<String, String>> goldData = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get("Код").equals(String.valueOf(LexemaClass.Identifier.getCode()))) {
                Map<String, String> item = new HashMap<>();
                item.put("Ідентифікатор", data.get(i).get("Підрядок"));
                item.put("Індекс", data.get(i).get("Індекс"));
                item.put("Тип", type(data.get(i), data));

                goldData.add(item);
            }
        }

        goldData = goldData.stream().distinct().collect(Collectors.toList());
        List<Map<String, String>> filterList = new ArrayList<>(goldData);
        goldData = goldData.stream()
                .filter(itm -> {
                    if (itm.get("Тип") != null) {
                        return true;
                    }
                    if (filterList.stream()
                            .filter(item -> item.get("Ідентифікатор").equals(itm.get("Ідентифікатор")) && item.get("Тип") != null)
                            .count() != 0) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        for (int i = 0; i < goldData.size(); i++) {
            SXSSFRow row = sheet2.createRow(i + 1);
            for (int j = 0; j < headers.size(); j++) {
                row.createCell(j).setCellValue(goldData.get(i).get(headers.get(j)));
                row.getCell(j).setCellStyle(generalStyle);
            }
        }

        for (int i = 0; i <= headers.size(); i++) {
            sheet2.setColumnWidth(i, 5000);
        }
    }

    private static void constantsTab(List<Map<String, String>> data, SXSSFWorkbook workbook) {
        SXSSFSheet sheet3 = workbook.createSheet("Таблиця констант");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFont(headerFont);
        CellStyle generalStyle = workbook.createCellStyle();
        generalStyle.setAlignment(HorizontalAlignment.CENTER);

        SXSSFRow headerRow = sheet3.createRow(0);
        List<String> headers = Arrays.asList("Константа", "Індекс");

        List<Map<String, String>> goldData = new ArrayList<>();
        List<String> constantCodeList = Arrays.asList(LexemaClass.ConstantInt, LexemaClass.ConstantReal, LexemaClass.True, LexemaClass.False)
                .stream()
                .map(lexemaClass -> String.valueOf(lexemaClass.getCode()))
                .collect(Collectors.toList());
        for (int i = 0; i < data.size(); i++) {
            if (constantCodeList.contains(data.get(i).get("Код"))) {
                Map<String, String> item = new HashMap<>();
                item.put("Константа", data.get(i).get("Підрядок"));
                item.put("Індекс", data.get(i).get("Індекс"));

                goldData.add(item);
            }
        }

        goldData = goldData.stream().distinct().collect(Collectors.toList());

        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        for (int i = 0; i < goldData.size(); i++) {
            SXSSFRow row = sheet3.createRow(i + 1);
            for (int j = 0; j < headers.size(); j++) {
                row.createCell(j).setCellValue(goldData.get(i).get(headers.get(j)));
                row.getCell(j).setCellStyle(generalStyle);
            }
        }

        for (int i = 0; i < headers.size(); i++) {
            sheet3.setColumnWidth(i, 5000);
        }

    }

    private static void reverseExprBuildingTab(List<Executable> expressions, SXSSFWorkbook workbook) {
        SXSSFSheet exprTab = workbook.createSheet("Таблиця побудови ПОЛІЗ");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFont(headerFont);
        CellStyle generalStyle = workbook.createCellStyle();
        generalStyle.setAlignment(HorizontalAlignment.CENTER);

        // public CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol) {
        int rowNumber = 0;
        for (int i = 0; i < expressions.size(); i++) {
            Executable expression = expressions.get(i);
            Row row = exprTab.createRow(rowNumber);
            Stream.of(0, 1, 2).forEach(row::createCell);
            Cell cell = row.getCell(0);

            cell.setCellValue("Вираз номер " + (i + 1));
            cell.setCellStyle(headerStyle);
            exprTab.addMergedRegion(new CellRangeAddress(rowNumber, rowNumber, 0, 2));

            row = exprTab.createRow(++rowNumber);
            List<String> headers = Arrays.asList("Вхід", "Стек", "ПОЛІЗ");
            int headCellIdx = 0;
            for (String head : headers) {
                cell = row.createCell(headCellIdx++);
                cell.setCellValue(head);
                cell.setCellStyle(headerStyle);
            }
            List<ExpressionConstructItem> conItems = expression.getConstructItems();
            for (int j = 0; j < conItems.size(); j++) {
                row = exprTab.createRow(++rowNumber);
                List<String> toPrintItems = Arrays.asList(conItems.get(j).getEnter().getValue(),
                        conItems.get(j).getStack().stream().map(Lexema::getValue).reduce("", (s, s2) -> s + s2 + " ").trim(),
                        conItems.get(j).getReverseExpr().stream().map(Lexema::getValue).reduce("", (s, s2) -> s + s2 + " ").trim());
                int toPrintIdx = 0;
                for (String toPrint : toPrintItems) {
                    cell = row.createCell(toPrintIdx++);
                    cell.setCellValue(toPrint);
                    cell.setCellStyle(generalStyle);
                }
            }
            rowNumber++;

        }

        for (int i = 0; i < 3; i++) {
            exprTab.setColumnWidth(i, 8000);
        }

    }

    private static String type(Map<String, String> item, List<Map<String, String>> data) {
        Map<LexemaClass, String> classToTypeMapping = new HashMap<LexemaClass, String>() {
            {
                put(LexemaClass.IntegerType, "INTEGER");
                put(LexemaClass.RealType, "REAL");
                put(LexemaClass.BooleanType, "BOOLEAN");
            }
        };

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).equals(item)) {
                return classToTypeMapping.get(LexemaClass.getLexemaByCode(Integer.parseInt(data.get(i - 1).get("Код"))));
            }
        }

        throw new IllegalArgumentException();
    }
}

