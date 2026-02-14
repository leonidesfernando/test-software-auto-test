package br.com.home.lab.softwaretesting.automation.util;

import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public final class ExcelUtil {

    private static final int REASONABLE_SIZE = 4_000;
    private ExcelUtil() {}

    enum HEADER {
        DESCRIPTION("Description"),
        CATEGORY("Category"),
        ENTRY_TYPE("Entry type"),
        ENTRY_DATE("Entry date"),
        VALUE("Value");

        private final String headerName;
        HEADER(String headerName) {
            this.headerName = headerName;
        }
    }

    public static void validateExcelIsEmpty(byte[] excelBytes) {
        try (ReadableWorkbook wb = new ReadableWorkbook(new ByteArrayInputStream(excelBytes))) {
            Sheet sheet = wb.getFirstSheet();
            try (Stream<Row> rows = sheet.openStream()) {
                var rowIterator = rows.iterator();
                assertThat(rowIterator.hasNext()).isTrue(); // At least one row
                Row singleRow = rowIterator.next();
                assertThat(singleRow.getCellAsString(0)).contains("No data to export");
                // No more rows
                assertThat(rowIterator.hasNext()).isFalse();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void validateExcelFileHeader(byte[] excelBytes) {
        try (ReadableWorkbook wb = new ReadableWorkbook(new ByteArrayInputStream(excelBytes))) {
            Sheet sheet = wb.getFirstSheet(); // First (and only) sheet
            try (Stream<Row> rows = sheet.openStream()) { // Streaming rows
                var rowIterator = rows.iterator();
                // Header row (row 0)
                Row headerRow = rowIterator.next();
                List<HEADER> headers = List.of(HEADER.values());
                headers.forEach(h -> {
                    int index = h.ordinal();
                    assertThat(headerRow.getCellAsString(index)).contains(h.headerName);
                });
            }
        }catch (Exception e) {
            throw new IllegalStateException("Fail to read excel file",e);
        }
    }


    public static void validateExcelFileHeader(Path excelFile) {
        try{
            assertThat(excelFile).isNotNull();
            assertThat(Files.size(excelFile)).isGreaterThan(REASONABLE_SIZE);
            byte[] excelBytes = Files.readAllBytes(excelFile);
            assertThat(excelBytes).hasSizeGreaterThan(REASONABLE_SIZE); // reasonable size
            validateExcelFileHeader(excelBytes);
        }catch (Exception e) {
            throw new IllegalStateException("Fail to read excel file",e);
        }finally {
            if(excelFile!=null) {
                excelFile.toFile().delete();
            }
        }
    }

    public static void validateEmptyExcelFileHeader(Path excelFile) {
        try{
            assertThat(excelFile).isNotNull();
            byte[] excelBytes = Files.readAllBytes(excelFile);
            validateExcelIsEmpty(excelBytes);
        }catch (Exception e) {
            throw new IllegalStateException("Fail to read excel file",e);
        }finally {
            if(excelFile!=null) {
                excelFile.toFile().delete();
            }
        }
    }
}
