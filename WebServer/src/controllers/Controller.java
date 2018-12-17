package controllers;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

import core.*;
import services.RmiService;

public class Controller extends ActionSupport implements SessionAware, Preparable {
    ArrayList<String> errors = new ArrayList<>();
    Map<String, Object> session;
    User current_user = new User();
    ArrayList<String> internal_error = new CustomException("Internal Error").errors;

    public void prepare() {
        if (!session.containsKey("user_id")) return;

        int user_id = (int)session.get("user_id");
        try {
            current_user = RmiService.getInstance().userInterface.read(user_id);
        } catch (CustomException e) {
            errors = e.errors;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            errors = internal_error;
        }
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
