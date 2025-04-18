import com.example.models.App;
import com.example.views.AppView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterTest {
    @Test
    void testInvalidUsername() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "register -u ; -p hdhsshjssj hdhsshjssj -n shsh -e ee -g a";
        App.getCurrMenuType().getMenu().handleMenu(input);

        String expectedOutput = "Username is invalid!"; // Adjust based on your actual output
        assertEquals(expectedOutput, outputStream.toString().trim());
    }

    @ParameterizedTest
    @CsvSource({
            "register -u ali -p .hdhss..hjssj hdhsshjssj -n shsh -e ee -g a\n,Password Format is invalid!",
            "register -u ali -p hdhsshjss hdhsshjssj -n shsh -e ee -g a\n,Password isn't secure! Password must contain a number",
            "register -u ali -p hdhsshjss hdhsshjssj -n shsh -e ee -g a\n,Password isn't secure! Password must contain a number",
            "register -u ali -p hdhsshjsS hdhsshjssj -n shsh -e ee -g a\n,Password isn't secure! Password must contain a number",
            "register -u ali -p 11222111 hdhsshjssj -n shsh -e ee -g a\n,Password isn't secure! Password must contain a lowercase letter",
            "register -u ali -p hdhsshjss12S hdhsshjssj -n shsh -e ee -g a\n,Password isn't secure! Password must contain a special character",
            "register -u ali -p hd hdhsshjssj -n shsh -e ee -g a\n,Password isn't secure! Password must be at least 8 characters",
    })
    void testInvalidPassword(String input , String output) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        App.getCurrMenuType().getMenu().handleMenu(input.trim());
        assertEquals(output, outputStream.toString().trim());
    }

}
