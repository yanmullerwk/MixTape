package br.com.mixtape.model;

public class Artist extends Entity{

    private String name;
    private String musicalGenre;

    public Artist() {}

    public Artist(String name, String musicalGenre) {
        this.name = name;
        this.musicalGenre = musicalGenre;
    }

    public Artist(int id, String name, String musicalGenre) {
        super(id);
        this.name = name;
        this.musicalGenre = musicalGenre;
    }

    public String getName() { return name; }
    public void setname(String name) { this.name = name; }

    public String getMusicalGenre() { return musicalGenre; }
    public void setMusicalGenre(String musicalGenre) { this.musicalGenre = musicalGenre; }

    @Override
    public String toString() {
        return String.format("[ID: %d] %s — Gênero: %s", getId(), name, musicalGenre);
    }
}
