package controllers;

import core.*;
import services.RmiService;
import websocket.WebSocketService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UserController extends Controller {
    User user = new User();
    int id;
    ArrayList<User> users = new ArrayList<>();

    // Methods
    public String login() {
        return SUCCESS;
    }

    public String login_post() {
        try {
            user = RmiService.getInstance().userInterface.login(user, WebSocketService.port, true);
            session.put("user_id", user.id);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String register() {
        return SUCCESS;
    }

    public String register_post() {
        try {
            RmiService.getInstance().userInterface.register(user);
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String dashboard() {
        return SUCCESS;
    }

    public String logout() {
        session.clear();
        return SUCCESS;
    }

    public String promote() {
        try {
            users = RmiService.getInstance().userInterface.normal_users();
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String promote_post() {
        try {
            RmiService.getInstance().userInterface.promote(id);
            users = RmiService.getInstance().userInterface.normal_users();
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
            return ERROR;
        }
    }

    public String read() {
        return SUCCESS;
    }

    public String index() {
        return SUCCESS;
    }

    // Beans
    public User getUser() {
        return this.user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
