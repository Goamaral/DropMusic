import java.io.IOException;
import java.util.Scanner;

public class RmiClient {
    UserController userController;

    public static void RmiClient() {
        RmiClient rmiClient = new RmiClient();

        rmiClient.connect();
        rmiClient.clearScreen();
        rmiClient.displayLogin();
    }

    // Connect to RMI server
    void connect() {
        // TODO Connect to UserController
    }

    void displayLogin() {
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        System.out.println("Login");
        System.out.print("Username: ");
        user.username = scanner.nextLine();
        System.out.print("Password: ");
        user.password = scanner.nextLine();
        scanner.close();

        try {
            userController.login(user);
        } catch (CustomException e) {
            this.clearScreen();
            e.printErrors();
            this.displayLogin();
        }
    }

    void displayRegister() {
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        System.out.println("Register");
        System.out.print("Name: ");
        user.name = scanner.nextLine();
        System.out.print("Email: ");
        user.email = scanner.nextLine();
        System.out.print("Password: ");
        user.password = scanner.nextLine();
        scanner.close();
        try {
            userController.register(user);
        } catch (CustomException e) {
            this.clearScreen();
            e.printErrors();
            this.displayRegister();
        }
    }

    void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException e) {
            for (int i = 0; i < 50; ++i) System.out.println();
        }
    }
}
