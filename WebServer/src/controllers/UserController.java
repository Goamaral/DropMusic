package controllers;

import com.opensymphony.xwork2.ActionSupport;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import core.UserInterface;

public class UserController extends ActionSupport {
    UserInterface userInterface;

    UserController() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 8000);
        this.userInterface = (UserInterface) registry.lookup("UserInterface");
    }

    public String login() {
        return SUCCESS;
    }

    public String login_post() {
        return SUCCESS;
    }

    public String register() {
        return SUCCESS;
    }

    public String register_post() {
        return SUCCESS;
    }

    public String normal_users() {
        return SUCCESS;
    }

    public String promote() {
        return SUCCESS;
    }

    public String read() {
        return SUCCESS;
    }

    public String index() {
        return SUCCESS;
    }
}
