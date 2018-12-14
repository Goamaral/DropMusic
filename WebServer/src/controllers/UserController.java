package controllers;

import core.*;
import services.Service;

import java.io.IOException;
import java.lang.reflect.Array;
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
        System.out.println("Action user (" + user.username + ") login");

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
        System.out.println("Action user (" + user.username + ") register");

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

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
