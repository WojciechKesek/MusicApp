import model.Artist;
import model.DataSource;
import model.SongArtist;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = new DataSource();
        if(!dataSource.open()){
            System.out.println("Can't open data source");
            return;
        }
        List<Artist> artists = dataSource.queryArtists(DataSource.ORDER_BY_ASC);
        if(artists == null){
            System.out.println("No artists");
            return;
        }
        artists.forEach(artist -> System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName()));

        List<String> albumsForArtists = dataSource.queryAlbumsForArtists("Pink Floyd", DataSource.ORDER_BY_ASC);
        albumsForArtists.forEach(album -> System.out.println(album));

        List<SongArtist> songArtists = dataSource.queryArtistsForSong("Go Your Own Way", DataSource.ORDER_BY_ASC);
        if(songArtists == null){
            System.out.println("Cant find artist for this song");
            return;
        }
        songArtists.forEach(artist -> System.out.println("Artist name = " + artist.getArtistName() +
                " ,album = " + artist.getAlbumName() +
                " ,track = " + artist.getTrack()));

        dataSource.close();
    }
}