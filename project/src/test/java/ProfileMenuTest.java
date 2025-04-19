import com.example.controllers.SignInMenuController;
import com.example.models.App;
import com.example.models.User;
import com.example.models.enums.types.MenuTypes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileMenuTest {
    private static User user;
    private static String password;

    @Test
    @Order(1)
    void enterProfileMenu() throws IOException {
        register();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "menu enter profilemenu";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Going to profile menu...";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    @Order(2)
    void showCurrMenu() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "show current menu";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Profile Menu";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    @Order(3)
    void showUserInfo() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "user info";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = String.format("Username: %s\n",user.getUsername()) +
                String.format("nickname: %s\n" , user.getNickname()) +
                String.format("moneyHighScore: %d\n" , user.getMoneyHighScore()) +
                String.format("numberOfGames: %d", user.getNumberOfGames());
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    @Order(4)
    void invalidChangeUsername() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "change username -u @…shshsj";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Username is invalid!";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    @Order(5)
    void validChangeUsername() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "change username -u soroosh";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Username has been changed!";
        user.setUsername("soroosh");
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    @Order(6)
    void changeNickname() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "change nickname -u shhm";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Nickname has been changed!";
        assertEquals(output, outputStream.toString().trim());
    }

    @ParameterizedTest
    @CsvSource({
            "change email -e email@gmail.c, Email is invalid!",
            "change email -e a@gmail.com, Enter a new email address!"
    })
    @Order(7)
    void invalidChangeEmail(String input , String output) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        App.getCurrMenuType().getMenu().handleMenu(input);
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    @Order(8)
    void validChangeEmail() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "change email -e asghar@gmail.com";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Email has been changed!";
        user.setEmail("asghar@gmail.com");
        assertEquals(output, outputStream.toString().trim());
    }

    @ParameterizedTest
    @CsvSource({
            "change password -p swdd@S8aasd7 -o ienfoas, Old password is wrong!",
            "change password -p hdhsshjss@Al12 -o hdhsshjss@Al12, New password is the same as the old password!"
    })
    @Order(9)
    void invalidChangePassword(String input , String output) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        App.getCurrMenuType().getMenu().handleMenu(input);
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    @Order(10)
    void validChangePassword() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "change password -p sdsd@dS8s7 -o hdhsshjss@Al12";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Password changed!";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    @Order(11)
    void invalidChangeToGameMenu() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "menu enter gamemenU";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Invalid target menu.";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    @Order(12)
    void menuExit() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "menu exit";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Exiting to Main Menu...";
        assertEquals(output, outputStream.toString().trim());
    }

    @Test
    @Order(13)
    void enterGameMenu() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        String input = "menu enter gamemenu";
        App.getCurrMenuType().getMenu().handleMenu(input);
        String output = "Going to game menu...";
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
    public static void register() {
        logout();
        App.getCurrMenuType().getMenu().handleMenu("register -u ali -p hdhsshjss@Al12 hdhsshjss@Al12 -n shsh -e a@gmail.com -g a");
        App.getCurrMenuType().getMenu().handleMenu("pick question -q 2 -a hasan -c hasan");
        user = App.getLoggedInUser();
        password = "hdhsshjss@Al12";
    }
}
