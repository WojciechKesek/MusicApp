package model;

public class SongArtist {

    private String artistName;
    private String albumName;
    private int track;

    public String getArtistName() {
        return artistName;
    }

    protected void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    protected void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getTrack() {
        return track;
    }

    protected void setTrack(int track) {
        this.track = track;
    }
}
