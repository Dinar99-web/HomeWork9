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

@DisplayName("Проверка содержания файлов разного расширения внутри zip архива")
public class ZipFilesCheck {

    private ClassLoader cl = ZipFilesCheck.class.getClassLoader();

    @Test
    @DisplayName("Проверка правильности строк в .pdf файле из архива")
    public void reedPDFFileInArchiveTest() throws Exception {

        try (InputStream is = cl.getResourceAsStream("samples.zip"); ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith(".pdf")) {
                    PDF pdf = new PDF(zis);
                    Assertions.assertEquals("Проверка PDF файла. \r\n", pdf.text);
                }
            }
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
                    Assertions.assertEquals("Проверка файла Excel", actualResult1);
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
                    Assertions.assertEquals(1, data.size());
                    Assertions.assertArrayEquals(new String[]{"\uFEFFПроверка csv файла."}, data.get(0));
                }
            }
        }
    }
}
