import com.example.controllers.SignInMenuController;
import com.example.models.App;
import com.example.models.User;
import com.example.models.enums.types.MenuTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {
    private static User user;
    private static String password;

    @BeforeAll
    public static void setUp() {
        logout();
        register();
    }

    @Test
    void invalidUsername() throws IOException {
        logout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "login -u    mamad -p hdjddjdjdn";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "User not found!";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    void invalidPassword() throws IOException {
        logout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "login  -u ali -p hdhsshjss@Al1";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Password doesn't match!";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    void validLogin() throws IOException {
        logout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "login -u " + user.getUsername() + " -p " + password;
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Login Successful. Going to Main Menu!";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    void forgetPasswordInvalidUsername() throws IOException {
        logout();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "forget password -u hasan";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "User not found!";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    void forgetPasswordWrongSecurityAnswer() throws IOException {
        logout();
        forgetPass();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "answer -a test";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Answer doesn't match!";
        assertEquals(output, outputStream.toString().trim());
    }

    @ParameterizedTest
    @CsvSource({
            "dhdhsjsj, Password isn't secure! Password must contain a number",
            "ksjd6562, Password isn't secure! Password must contain a uppercase letter",
            "Kkjaff273, Password isn't secure! Password must contain a special character"
    })
    void forgetPasswordWrongPassword(String input, String output) throws IOException {
        logout();
        forgetPass();
        App.getCurrMenuType().getMenu().handleMenu("answer -a hasan");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        App.getCurrMenuType().getMenu().handleMenu(input.trim());
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    void validForgetPassword() throws IOException {
        logout();
        forgetPass();
        App.getCurrMenuType().getMenu().handleMenu("answer -a hasan");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        App.getCurrMenuType().getMenu().handleMenu("adHD12@9");
        String output = "Successfully logged in! Password updated to: adHD12@9";
        assertEquals(output, outputStream.toString().trim());
    }

    public static void logout() {
        if (App.getCurrMenuType() == MenuTypes.MainMenu) {
            App.getCurrMenuType().getMenu().handleMenu("user logout");
        }
        SignInMenuController.isProgramWaitingForQuestion = false;
        SignInMenuController.isProgramWaitingForAnswer = false;
        SignInMenuController.setUserOfForgetPassword(null);
    }

    public static void forgetPass() {
        String input = "forget password -u " + user.getUsername();
        App.getCurrMenuType().getMenu().handleMenu(input);
    }

    public static void register() {
        logout();
        App.getCurrMenuType().getMenu().handleMenu("register -u ali -p hdhsshjss@Al12 hdhsshjss@Al12 -n shsh -e a@gmail.com -g a");
        App.getCurrMenuType().getMenu().handleMenu("pick question -q 2 -a hasan -c hasan");
        user = App.getLoggedInUser();
        password = "hdhsshjss@Al12";
    }
}
