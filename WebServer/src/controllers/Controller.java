package controllers;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

import core.*;

public class Controller extends ActionSupport implements SessionAware, Preparable {
    ArrayList<String> errors = new ArrayList<>();
    Map<String, Object> session;
    User current_user = new User();

    public void prepare() {
        current_user = (User)session.get("current_user");
    }

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public User getCurrent_user() {
        return current_user;
    }
}
