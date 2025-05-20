import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.asynchttpclient.util.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Проверка содержания файлов разного расширения внутри zip архива")
public class ZipFilesCheck {

    private ClassLoader cl = ZipFilesCheck.class.getClassLoader();

    @Test
    @DisplayName("Проверка правильности строк в .pdf файле из архива")
    public void reedPDFFileInArchiveTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("samples.zip")) {
            assertNotNull(is, "Архив samples.zip не найден");
            ZipInputStream zis = new ZipInputStream(is);
            boolean foundPdf = false;
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith(".pdf")) {
                    foundPdf = true;
                    PDF pdf = new PDF(zis);
                    assertEquals("Проверка PDF файла. \r\n", pdf.text);
                }
            }
            assertTrue(foundPdf, "В архиве нет PDF-файла");
        }
    }

    @Test
    @DisplayName("Проверка правильности строк в .xls файле из архива")
    public void reedXLSFileInArchiveTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("samples.zip"); ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith(".xlsx")) {
                    XLS xls = new XLS(zis);
                    String actualResult1 = xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue();
                    assertEquals("Проверка файла Excel", actualResult1);
                }
            }
        }
    }

    @Test
    @DisplayName("Проверка правильности строк в .csv файле из архива")
    public void reedCSVFileInArchiveTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("samples.zip"); ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith(".csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> data = csvReader.readAll();
                    assertEquals(1, data.size());
                    Assertions.assertArrayEquals(new String[]{"\uFEFFПроверка csv файла."}, data.get(0));
                }
            }
        }
    }
}
