import com.fasterxml.jackson.databind.ObjectMapper;
import model.Library;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class JsonFileTest {
    private final ClassLoader cl = JsonFileTest.class.getClassLoader();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Проверка json файла библиотеки")
    void jsonFileParsingTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("jsonSample.json")) {
            Library library = mapper.readValue(is, Library.class);

            assertEquals("City Central Library", library.getName());
            assertEquals(7, library.getTotalBooks());

            Library.Author orwell = library.getAuthors().get(0);
            assertEquals("George Orwell", orwell.getName());
            assertArrayEquals(new String[]{"1984", "Animal Farm"}, orwell.getBooks().toArray());

            Library.Author rowling = library.getAuthors().get(1);
            assertEquals("J.K. Rowling", rowling.getName());
            assertArrayEquals(
                    new String[]{
                            "Harry Potter and the Philosopher's Stone",
                            "Harry Potter and the Chamber of Secrets",
                            "Fantastic Beasts"
                    },
                    rowling.getBooks().toArray()
            );

            Library.Author christie = library.getAuthors().get(2);
            assertEquals("Agatha Christie", christie.getName());
            assertArrayEquals(
                    new String[]{"Murder on the Orient Express", "And Then There Were None"},
                    christie.getBooks().toArray()
            );
        }
    }
}

