package controllers;

import com.opensymphony.xwork2.ActionSupport;

import core.User;

public class UserController extends ActionSupport {
    User user;

    // Methods
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
    public void setUser(User user) {
        this.user = user;
    }


    public User getUser() {
        return this.user;
    }
}
