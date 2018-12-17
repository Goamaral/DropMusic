package controllers;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import core.*;
import org.json.simple.JSONObject;
import services.RmiService;
import websocket.WebSocketService;
import org.json.simple.parser.*;
import uc.sd.apis.DropBoxApi2;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UserController extends Controller {
    User user = new User();
    int id;
    ArrayList<User> users = new ArrayList<>();
    String url;
    String code;


    OAuthService service = new ServiceBuilder()
            .provider(DropBoxApi2.class)
            .apiKey("lgyjmqsl7169pij")
            .apiSecret("9nin5ivu1d2a35q")
            .callback("http://localhost:8080/WebServer/dropboxoauth_redirect")
            .build();

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

    public String dropboxoauth() {
        url = service.getAuthorizationUrl(null);
        System.out.println("U " + url);
        return SUCCESS;
    }

    public String dropboxoauth_redirect() {
        JSONObject json = null;
        try {
            System.out.println("Fez redirect do dropbox");
            Verifier verifier = new Verifier(code);
            System.out.println(verifier);
            Token accessToken = service.getAccessToken(null, verifier);
            System.out.println(accessToken.getRawResponse());
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(accessToken.getRawResponse());
            User user = RmiService.getInstance().userInterface.findByUid((String)json.get("uid"));
            session.put("user_id", user.id);
            return SUCCESS;
        } catch (CustomException e) {
            session.put("uid", json.get("uid"));
            errors = e.errors;
            return ERROR;
        } catch (IOException | NotBoundException | ParseException e) {
            errors = internal_error;
            return ERROR;
        }
    }

    public String associate_dropbox() {
        try {
            ArrayList<Object> args = new ArrayList<>();
            args.add(current_user.id);
            args.add(getUid());
            RmiService.getInstance().userInterface.setUid(args);
            session.put("uid", null);
            return SUCCESS;
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
            session.put("uid", null);
            errors = internal_error;
            return ERROR;
        } catch (CustomException e) {
            session.put("uid", null);
            errors = e.errors;
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

    public String setUrl(){
        return this.url = url;
    }

    public String getUrl(){
        return this.url = url;
    }

    public String getCode(){
        return this.code;
    }

    public void setCode(String code){
        this.code = code;
    }



}


