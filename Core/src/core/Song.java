package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Song implements Serializable {
    public int id;
    public String name;
    public String info;
    String artists = "";
    String genres = "";
    public int album_id;

    // Relationships
    public ArrayList<Integer> artist_ids = new ArrayList<>();
    public ArrayList<Integer> genres_ids = new ArrayList<>();

    Song(String name, String info) {
        this.name = name;
        this.info = info;
    }

    void validate() throws CustomException { this.nameValidator(); }

    void nameValidator() throws CustomException {
        this.name.replaceAll("\\s","");

        if (this.name.length() == 0)
            throw new CustomException("Name can't be empty");
    }

    public void addArtist(Artist artist) throws CustomException {
        if (this.artist_ids.contains(artist.id)) throw new CustomException("Artist already exists");

        if (this.artists.length() != 0) this.artists += ", ";
        this.artists += artist.name;

        this.artist_ids.add(artist.id);
    }

    public void addGenre(Genre genre) throws CustomException {
        if (this.genres_ids.contains(genre.id)) throw new CustomException("Genre already exists");

        if (this.genres.length() != 0) this.genres += ", ";
        this.genres += genre.name;

        this.genres_ids.add(genre.id);
    }

    public void removeGenre(Genre genre) {
        String[] genre_parts;
        List<String> genre_parts_list;

        int index = this.genres_ids.indexOf(genre.id);

        if (index != -1) {
            genre_parts = this.genres.split(", ");
            genre_parts_list = Arrays.asList(genre_parts);

            this.genres = "";

            for (int i = 0; i < genre_parts_list.size(); ++i) {
                if (i == index) continue;

                if (this.genres.equals("")) {
                    this.genres = genre_parts_list.get(i);
                } else {
                    this.genres += ", " + genre_parts_list.get(i);
                }
            }

            this.genres_ids.remove(index);
        }
    }

    public void removeArtist(Artist artist) {
        String[] artist_parts;
        List<String> artist_parts_list;

        int index = this.artist_ids.indexOf(artist.id);

        if (index != -1) {
            artist_parts = this.artists.split(", ");
            artist_parts_list = Arrays.asList(artist_parts);

            this.artists = "";

            for (int i = 0; i < artist_parts_list.size(); ++i) {
                if (i == index) continue;

                if (this.artists.equals("")) {
                    this.artists = artist_parts_list.get(i);
                } else {
                    this.artists += ", " + artist_parts_list.get(i);
                }
            }

            this.artist_ids.remove(index);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
