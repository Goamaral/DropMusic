package controllers;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.Map;

public class Controller extends ActionSupport implements SessionAware {
    ArrayList<String> errors = new ArrayList<>();
    Map<String, Object> session;

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }
}
