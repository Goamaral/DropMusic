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

    Album album_find(int id) throws CustomException {
        for(Album album : this.albums) {
            if (album.id == id) return album;
        }

        throw new CustomException("Album not found");
    }

    void album_update(Album new_album) throws CustomException {
        int position = -1;

        for(Album album : this.albums) {
            position += 1;
            if (album.id == new_album.id) break;
        }

        if (position == -1) throw new CustomException("Album not found");

        this.albums.set(position, new_album);
    }

    void album_delete(int id) {
        int position = -1;

        for(Album album : this.albums) {
            position += 1;
            if(album.id == id) break;
        }

        if(position != -1) {
            this.albums.remove(position);
        }
    }

    ArrayList<Critic> album_critics(int album_id) throws CustomException {
        for(Album album : this.albums) {
            if (album.id == album_id) return album.critics;
        }

        throw new CustomException("Album not found");
    }
}
