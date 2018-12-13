package controllers;

import core.CustomException;
import core.User;
import services.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class UserController extends Controller {
    User user = new User();

    // Methods
    public String login() {
        return SUCCESS;
    }

    public String login_post() {
        System.out.print("Action user(" + user.username + ") register: ");

        try {
            user.validate();
        } catch (CustomException ce) {
            errors = ce.errors;
            return ERROR;
        } catch (NoSuchAlgorithmException e) {
            return ERROR;
        }

        try {
            Object response_object = Service.request("user_create", user);
            Service.catchException(response_object);
            session.put("current_user", user);
            return SUCCESS;
        } catch (CustomException ce) {
            errors = ce.errors;
            return ERROR;
        } catch (IOException ioe) {
            return ERROR;
        }
    }

    public String register() {
        return SUCCESS;
    }

    public String register_post() {
        return SUCCESS;
    }

    public String dashboard() {
        return SUCCESS;
    }

    public String logout() {
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

    // Beans
    public User getUser() {
        return this.user;
    }
}
