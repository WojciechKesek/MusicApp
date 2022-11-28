package model;

public class Song {
    private int id;
    private int track;
    private String name;
    private int albumId;

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public int getTrack() {
        return track;
    }

    protected void setTrack(int track) {
        this.track = track;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public int getAlbumId() {
        return albumId;
    }

    protected void setAlbumId(int albumId) {
        this.albumId = albumId;
    }
}
