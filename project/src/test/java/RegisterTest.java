import com.example.controllers.SignInMenuController;
import com.example.models.App;
import com.example.models.enums.types.MenuTypes;
import com.example.utilities.Connection;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterTest {
    private static final Logger log = LoggerFactory.getLogger(RegisterTest.class);

    @BeforeAll
    public static void init() {
        Dotenv.configure()
                .directory(System.getProperty("user.dir") + "/src/main/java/com/example/configs")
                .filename("env." + System.getenv("APP_MODE").toLowerCase())
                .systemProperties()
                .load();
        Connection.getDatabase();
    }

    @Test
    void testInvalidUsername() throws IOException {
        logout();
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
            "register -u ali -p .hdhss..hjssj hdhsshjssj -n shsh -e ee -g a, Password Format is invalid!",
            "register -u ali -p hdhsshjss hdhsshjssj -n shsh -e ee -g a, Password isn't secure! Password must contain a number",
            "register -u ali -p hdhsshjss hdhsshjssj -n shsh -e ee -g a, Password isn't secure! Password must contain a number",
            "register -u ali -p hdhsshjsS hdhsshjssj -n shsh -e ee -g a, Password isn't secure! Password must contain a number",
            "register -u ali -p 11222111 hdhsshjssj -n shsh -e ee -g a, Password isn't secure! Password must contain a lowercase letter",
            "register -u ali -p hdhsshjss12S hdhsshjssj -n shsh -e ee -g a, Password isn't secure! Password must contain a special character",
            "register -u ali -p hd hdhsshjssj -n shsh -e ee -g a, Password isn't secure! Password must be at least 8 characters",
            "register -u ali -p hdhsshjss@Al12 hdhsshjssj -n shsh -e ee -g a, Passwords do not match!"
    })
    void testInvalidPassword(String input, String output) throws IOException {
        logout();
        SignInMenuController.isProgramWaitingForQuestion = false;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        App.getCurrMenuType().getMenu().handleMenu(input.trim());
        assertEquals(output, outputStream.toString().trim());
    }

    @ParameterizedTest
    @CsvSource({
            "register -u ali -p hdhsshjss@Al12 hdhsshjss@Al12 -n shsh -e aa@@gmail.com -g a, Email is invalid!",
            "register -u ali -p hdhsshjss@Al12 hdhsshjss@Al12 -n shsh -e .a@gmail.com -g a, Email is invalid!",
            "register -u ali -p hdhsshjss@Al12 hdhsshjss@Al12 -n shsh -e aagmail.com -g a, Email is invalid!",
            "register -u ali -p hdhsshjss@Al12 hdhsshjss@Al12 -n shsh -e aa:@gmail.com -g a, Email is invalid!",
            "register -u ali -p hdhsshjss@Al12 hdhsshjss@Al12 -n shsh -e aa@.com -g a, Email is invalid!",
            "register -u ali -p hdhsshjss@Al12 hdhsshjss@Al12 -n shsh -e aa@gmail. -g a, Email is invalid!",
    })
    void testInvalidEmail(String input, String output) throws IOException {
        logout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        App.getCurrMenuType().getMenu().handleMenu(input);
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    void validRegister() throws IOException {
        logout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "register -u ali -p hdhsshjss@Al12 hdhsshjss@Al12 -n shsh -e a@gmail.com -g a\n";
        App.getCurrMenuType().getMenu().handleMenu(input.trim());
        String output = "User created! Password is: hdhsshjss@Al12\n" +
                "Enter 'pick question -q <question number> -a <answer> -c <confirm answer>' to choose security question\n" +
                "You can enter 'list questions' command to see possible security questions";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    void listOfQuestions() throws IOException {
        register();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        App.getCurrMenuType().getMenu().handleMenu("list questions");
        String output = "List of questions:\n" +
                "1- What is your favorite pet?\n" +
                "2- What is your favorite game?\n" +
                "3- What is your favorite car?\n" +
                "4- What is your favorite color?";
        assertEquals(output, outputStream.toString().trim());
    }

    @ParameterizedTest
    @CsvSource({
            "pick question -q 16252526 -a dhdjdjdjdjdhs -c jfjdjddjdndn, Invalid question number!",
            "pick question -q 2 -a hasan -c hasans, Answer doesn't match!"
    })
    void invalidPickQuestion(String input, String output) throws IOException {
        register();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        App.getCurrMenuType().getMenu().handleMenu(input.trim());
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    void validPickQuestion() throws IOException {
        register();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        App.getCurrMenuType().getMenu().handleMenu("pick question -q 2 -a hasan -c hasan");
        String output = "Question Picked! Logging in...";
        assertEquals(output, outputStream.toString().trim());
    }


    @Test
    void invalidRandomPassRegister() throws IOException {
        logout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "register -u ali -p random hdhsshjss@Al12 -n shsh -e a@@gmail.com -g a";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Password isn't secure! Password must be at least 8 characters";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    void validRandomPassRegister() throws IOException {
        logout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "register -u mamad -p random Random -n shsh -e a@gmail.com -g a";
        App.getCurrMenuType().getMenu().handleMenu(input.trim());
        String output = "User created! Password is: " + SignInMenuController.getUserPassword() + "\nEnter 'pick question -q <question number> -a <answer> -c <confirm answer>' to choose security question\n" +
                "You can enter 'list questions' command to see possible security questions";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    void userLogout() throws IOException {
        App.getCurrMenuType().getMenu().handleMenu("register -u ali -p hdhsshjss@Al12 hdhsshjss@Al12 -n shsh -e a@gmail.com -g a");
        App.getCurrMenuType().getMenu().handleMenu("pick question -q 2 -a hasan -c hasan");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        App.getCurrMenuType().getMenu().handleMenu("user logout");
        String output = "You are now logged out!";
        assertEquals(output, outputStream.toString().trim());
    }

    public static void logout() {
        if (App.getCurrMenuType() == MenuTypes.MainMenu) {
            App.getCurrMenuType().getMenu().handleMenu("user logout");
        }
        SignInMenuController.isProgramWaitingForQuestion = false;
    }

    public static void register() {
        logout();
        App.getCurrMenuType().getMenu().handleMenu("register -u ali -p hdhsshjss@Al12 hdhsshjss@Al12 -n shsh -e a@gmail.com -g a");
    }
}
