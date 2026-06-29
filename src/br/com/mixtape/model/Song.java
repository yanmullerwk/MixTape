package br.com.mixtape.model;

public class Song extends Entity {

    private String title;
    private int durationSeconds;
    private String playbackLink;
    private Artist artist;

    public Song() {}

    public Song(String title, int durationSeconds, String playbackLink, Artist artist) {
        this.title = title;
        this.durationSeconds = durationSeconds;
        this.playbackLink = playbackLink;
        this.artist = artist;
    }

    public Song(int id, String title, int durationSeconds, String playbackLink, Artist artist) {
        super(id);
        this.title = title;
        this.durationSeconds = durationSeconds;
        this.playbackLink = playbackLink;
        this.artist = artist;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public String getPlaybackLink() { return playbackLink; }
    public void setPlaybackLink(String playbackLink) { this.playbackLink = playbackLink; }

    public Artist getArtist() { return artist; }
    public void setArtist(Artist artist) { this.artist = artist; }

    public String getFormattedDuration() {
        int min = durationSeconds / 60;
        int sec = durationSeconds % 60;
        return String.format("%d:%02d", min, sec);
    }

    @Override
    public String toString() {
        String artistName = (artist != null) ? artist.getName() : "Unknown";
        return String.format("[ID: %d] %s — %s (%s) | Link: %s",
                getId(), title, artistName, getFormattedDuration(), playbackLink);
    }
}
