import java.util.ArrayList;

public class Database {
    ArrayList<User> users = new ArrayList<>();
    int next_user_id = 0;

    ArrayList<Album> albums = new ArrayList<>();
    int next_album_id = 0;

    // USER
    User user_findByUsername(String username) throws CustomException {
        for (User user : this.users) {
            if (user.username.equals(username)) {
                return user;
            }
        }

        throw new CustomException("Username not found");
    }

    void user_create(User user) throws CustomException {
        for (User user_i : this.users) {
            if (user_i.username.equals(user.username)) {
                throw new CustomException("Username already exists");
            }
        }

        if  (this.users.size() == 0) user.becomeEditor();

        user.id = this.next_user_id;

        this.users.add(user);
        this.next_user_id += 1;
    }

    // ALBUM
    ArrayList<Album> album_all() {
        return this.albums;
    }

    void album_create(Album album) {
        album.id = this.next_album_id;

        this.albums.add(album);
        this.next_album_id += 1;
    }
}
