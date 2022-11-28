import model.Artist;
import model.DataSource;
import model.SongArtist;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = new DataSource();
        if(!dataSource.open()){
            System.out.println("Can't open data source");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to music collection app");
        boolean on = true;
        while(on){
            System.out.println("\nOptions:");
            System.out.println("\tShow list of artists -> Press 1");
            System.out.println("\tShow albumns of specific artist -> Press 2");
            System.out.println("\tShow albumns which contain song -> Press 3");
            System.out.println("\tShow how many... -> Press 4");
            System.out.println("\tAdd song-> Press 5");
            System.out.println("\tExit -> Press 6");

            System.out.println();
            switch (scanner.nextLine()){
                case "1": List<Artist> artists = dataSource.queryArtists(DataSource.ORDER_BY_NONE);
                    if(artists == null){
                        System.out.println("No artists");
                        return;
                    }
                    artists.forEach(artist -> System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName()));
                    break;
                case "2":
                    System.out.println("Please enter artist name: ");
                    List<String> albumsForArtists = dataSource.queryAlbumsForArtists(scanner.nextLine(), DataSource.ORDER_BY_ASC);
                    albumsForArtists.forEach(album -> System.out.println(album));
                    break;
                case "3":
                    System.out.println("Enter song title: ");
                    String title =scanner.nextLine();
                    List<SongArtist> songArtists = dataSource.queryArtistsForSong(title, DataSource.ORDER_BY_ASC);
                    if(songArtists == null){
                        System.out.println("Cant find artist for this song");
                        return;
                    }
                    songArtists = dataSource.querySongInfoView(title);
                    songArtists.forEach(artist -> System.out.println(" Atrist: " + artist.getArtistName() +
                            ", album: " + artist.getAlbumName() + ", track: " + artist.getTrack()));
                    break;
                case "4":
                    System.out.println("Artists -> Press 1");
                    System.out.println("Albums -> Press 2");
                    System.out.println("Songs -> Press 3");
                    switch(scanner.nextLine()){
                        case "1": System.out.println(dataSource.getCount(DataSource.TABLE_ARTISTS));
                        break;
                        case "2": System.out.println(dataSource.getCount(DataSource.TABLE_ALBUMS));
                        break;
                        case "3": System.out.println(dataSource.getCount(DataSource.TABLE_SONGS));
                        break;
                        default:
                            System.out.println("Wrong input");
                            break;
                    }
                    break;
                case "5":
                    System.out.println("Please enter album artist");
                    String artist = scanner.nextLine();
                    System.out.println("Please enter song album");
                    String album = scanner.nextLine();
                    System.out.println("Please enter song name");
                    String songName = scanner.nextLine();
                    System.out.println("Please enter song track number");
                    int trackNumber = scanner.nextInt();
                    scanner.nextLine();
                    dataSource.insertSong(songName, artist, album, trackNumber);
                    break;
                case "6": System.out.println("Good bye");
                    on = false;
                    break;
                default:
                    System.out.println("Wrong input");
            }
        }
        scanner.close();
//        List<SongArtist> songArtists = dataSource.queryArtistsForSong("Go Your Own Way", DataSource.ORDER_BY_ASC);
//        if(songArtists == null){
//            System.out.println("Cant find artist for this song");
//            return;
//        }
//        songArtists.forEach(artist -> System.out.println("Artist name = " + artist.getArtistName() +
//                " ,album = " + artist.getAlbumName() +
//                " ,track = " + artist.getTrack()));
//        dataSource.querySongsMetadata();
//        dataSource.createViewForSongArtists();



        dataSource.close();
    }
}