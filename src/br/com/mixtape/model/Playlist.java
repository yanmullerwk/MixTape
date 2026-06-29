package br.com.mixtape.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist extends Entity {

    private String name;
    private String description;
    private User owner;
    private List<Song> songs;

    public Playlist() {
        this.songs = new ArrayList<>();
    }

    public Playlist(String name, String description, User owner) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.songs = new ArrayList<>();
    }

    public Playlist(int id, String name, String description, User owner) {
        super(id);
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.songs = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public List<Song> getSongs() { return songs; }
    public void setSongs(List<Song> songs) { this.songs = songs; }

    public void addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
        }
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }

    @Override
    public String toString() {
        String ownerName = (owner != null) ? owner.getName() : "Unknown";
        return String.format("ID: %d %s — %s | Dono: %s | Musicas: %d",
                getId(), name, description, ownerName, songs.size());
    }
}
