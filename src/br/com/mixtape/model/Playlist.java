package br.com.mixtape.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist extends Entity{

    private String name;
    private String description;
    private User user;
    private List<Music> musics;

    public Playlist() {
        this.musics = new ArrayList<>();
    }

    public Playlist(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.musics = new ArrayList<>();
    }

    public Playlist(int id, String name, String description, User user) {
        super(id);
        this.name = name;
        this.description = description;
        this.user = user;
        this.musics = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Music> getMusics() { return musics; }
    public void setMusics(List<Music> musics) { this.musics = musics; }

    public void addMusic(Music music) {
        if (!musics.contains(music)) {
            musics.add(music);
        }
    }

    public void removeMusic(Music musica) {
        musics.remove(musica);
    }

    @Override
    public String toString() {
        String nameUser = (user != null) ? user.getName() : "Desconhecido";
        return String.format("[ID: %d] %s — %s | Dono: %s | Músicas: %d",
                getId(), name, description, nameUser, musics.size());
    }
}
