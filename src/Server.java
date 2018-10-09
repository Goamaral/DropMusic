public class Server implements UserInterface {
    public static void main() {
        Server server = new Server();
        UserInterface
        Naming.rebind("Server");

    }

    public Server() throws RemoteException {}

    // UserController
    public void login(User user) throws RemoteException, CustomException {
        user.prepare();
        User fetched_user = this.findByEmail(user.email);
        if (fetched_user == null) {
            ArrayList<String> errors = new ArrayList<>(1);
            errors.add("Invalid credentials");
            throw new CustomException(errors);
        }

        if (!fetched_user.password.equals(user.password)) {
            ArrayList<String> errors = new ArrayList<>(1);
            errors.add("Invalid credentials");
            throw new CustomException(errors);
        }
    }

    public void register(User user) throws RemoteException, CustomException {

    }

    // ORM
    private User findByEmail(String email) {
        // Query database
        return null;
    }
}