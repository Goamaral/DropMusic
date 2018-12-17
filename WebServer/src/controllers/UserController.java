package controllers;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;
import core.*;
import net.projectmonkey.object.mapper.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import services.Service;
import uc.sd.apis.DropBoxApi2;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

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
            .callback("http://localhost:8080/webserver/dropboxoauth_redirect")
            .build();

    // Methods
    public String login() {
        return SUCCESS;
    }

    // TODO OPEN WEBSOCKET
    // TODO SEND NOTIFICATIONS
    public String login_post() {
        try {
            user.encrypt_password();
        } catch (NoSuchAlgorithmException e) {
            return ERROR;
        }

        try {
            Object response_object = Service.request("user_findByUsername", user.username);
            Service.catchException(response_object);
            User fetched_user = (User) response_object;

            if (!fetched_user.password.equals(user.password)) {
                CustomException ce = new CustomException("Invalid credentials");
                errors = ce.errors;
                return ERROR;
            }

            /*
            Socket socket = new Socket();
            try {
                String ipString = RemoteServer.getClientHost();
                InetAddress ip = InetAddress.getByName(ipString);
                socket = new Socket(ip, tcp);
            } catch (ServerNotActiveException | IOException e) {}
            */

            session.put("current_user", fetched_user);

            /*
            ArrayList<Job> jobs_to_perform = new ArrayList<>();
            synchronized (this.server.jobLock) {
                for (Job job : this.server.jobs) {
                    if (job.user_id == fetched_user.id) {
                        jobs_to_perform.add(job);
                    }
                }
            }

            for (Job job : jobs_to_perform) {
                this.server.send_notifications(job);
            }
            */

            return SUCCESS;
        } catch (IOException e) {
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }
    }

    public String register() {
        return SUCCESS;
    }

    public String register_post() {
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
            return SUCCESS;
        } catch (CustomException ce) {
            errors = ce.errors;
            return ERROR;
        } catch (IOException ioe) {
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
            Object response_object  = Service.request("normal_users", true);
            Service.catchException(response_object);
            this.users = (ArrayList<User>) response_object;
            return SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            return ERROR;

        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        }
    }

    public String promote_post() {
        try {
            Object response_object = Service.request("user_promote", id);
            Service.catchException(response_object);
            //this.server.send_notifications(new Job(user_id, "You have been promoted.\nYou are now an editor."));
            return SUCCESS;
        } catch (CustomException e) {
            errors = e.errors;
            return ERROR;
        } catch (IOException e) {
            return ERROR;
        }
    }

    public String dropboxoauth() {
        url = service.getAuthorizationUrl(null);
        System.out.println(url);
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
            Object response_object = Service.request("user_findByUid", json.get("uid"));
            Service.catchException(response_object);
            session.put("current_user", (User)response_object);
            return SUCCESS;
        } catch (CustomException e) {
            session.put("uid", json.get("uid"));
            errors = e.errors;
            return ERROR;
        } catch (IOException | ParseException e) {
            return ERROR;
        }
    }

    public String associate_dropbox() {
        try {
            ArrayList<Object> args = new ArrayList<>();
            args.add(current_user.id);
            args.add(getUid());
            Object response_object = Service.request("user_setUid", args);
            Service.catchException(response_object);
            session.put("uid", null);
            return SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            session.put("uid", null);
            return ERROR;
        } catch (CustomException e) {
            errors = e.errors;
            session.put("uid", null);
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


