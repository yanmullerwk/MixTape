package br.com.mixtape.model;

public class Music extends Entity {

        private String title;
        private int durationInSeconds;
        private String link;
        private Artist artist;

        public Music() {}

        public Music(String title, int durationInSeconds, String link, Artist artist) {
            this.title = title;
            this.durationInSeconds = durationInSeconds;
            this.link = link;
            this.artist = artist;
        }

        public Music(int id, String title, int durationInSeconds, String link, Artist artist) {
            super(id);
            this.title = title;
            this.durationInSeconds = durationInSeconds;
            this.link = link;
            this.artist = artist;
        }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getDurationInSeconds() { return durationInSeconds; }
    public void setDurationInSeconds(int durationInSeconds) { this.durationInSeconds = durationInSeconds; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public Artist getArtist() { return artist; }
    public void setArtist(Artist artist) { this.artist = artist; }


    public String getFormatedDuration() {
        int min = durationInSeconds / 60;
        int seg = durationInSeconds % 60;
        return String.format("%d:%02d", min, seg);
    }

    @Override
    public String toString() {
        String nameArtist = (artist != null) ? artist.getName() : "Desconhecido";
        return String.format("[ID: %d] %s — %s (%s) | Link: %s",
                getId(), title, nameArtist, getFormatedDuration(), link);
    }
}
