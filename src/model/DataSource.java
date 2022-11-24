package model;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:D:\\Wojtek\\JAVA\\MusicDB\\music\\" + DB_NAME;
    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTISTS_ID = "_id";
    public static final String COLUMN_ARTISTS_NAME = "name";


    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_TRACK  = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC  = 2;
    public static final int ORDER_BY_DESC  = 3;

    public static final String QUERY_ALBUMS_BY_ARTSIT_START = "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " FROM " +
            TABLE_ALBUMS + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
            " = " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + " = \"";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT = " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";
    public static final String QUERY_ARTIST_FOR_SONG_START =
            "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
                    TABLE_SONGS + "." + COLUMN_SONG_TRACK + " FROM " + TABLE_SONGS +
                    " INNER JOIN " + TABLE_ALBUMS + " ON " +
                    TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID +
                    " WHERE " + TABLE_SONGS + "." + COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_ARTIST_FOR_SONG_SORT =
            " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";

    public static final String CREATE_ARTIST_FOR_SONG_VIEW = "CREATE VIEW IF NOT EXISTS " +
            TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " AS " + COLUMN_SONG_ALBUM + ", " +
            TABLE_SONGS + "." + COLUMN_SONG_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE +
            " FROM " + TABLE_SONGS +
            " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS +
            "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
            " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST +
            " = " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID +
            " ORDER BY " +
            TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + ", " +
            TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " +
            TABLE_SONGS + "." + COLUMN_SONG_TRACK;

    public static final String QUERY_VIEW_SONG_INFO_PREP = "SELECT " + COLUMN_ARTISTS_NAME + ", " +
            COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " + TABLE_ARTIST_SONG_VIEW +
            " WHERE " + COLUMN_SONG_TITLE + " = ?";


    private Connection conn;

    private PreparedStatement querySongInfoView;
    public boolean open(){
        try{
            conn = DriverManager.getConnection(CONNECTION_STRING);
            querySongInfoView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            return true;
        }catch (SQLException e){
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    public void close(){
        try{
            if(querySongInfoView != null){
                querySongInfoView.close();
            }
            if(conn != null){
                conn.close();
            }
        }catch (SQLException e){
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public List<Artist> queryArtists(int sortOrder){
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if(sortOrder != ORDER_BY_NONE){
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTISTS_NAME);
            sb.append(" COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_DESC){
                sb.append("DESC");
            }else {
                sb.append("ASC");
            }
        }

        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sb.toString())){
            List<Artist> artists = new ArrayList<>();
            while(results.next()){
                Artist artist = new Artist();
                artist.setId(results.getInt(COLUMN_ARTISTS_ID));
                artist.setName(results.getString(COLUMN_ARTISTS_NAME));
                artists.add(artist);
            }
            return artists;
        }catch (SQLException e){
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }
    public List<String> queryAlbumsForArtists(String artistName, int sortOrder){
        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTSIT_START);
        sb.append(artistName);
        sb.append("\"");
        if(sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }
        System.out.println("SQL statment = " + sb.toString());
        try(Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sb.toString())){
            List<String> albums = new ArrayList<>();
            while(results.next()){
                albums.add(results.getString(1));
            }
            return albums;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<SongArtist> queryArtistsForSong(String songName, int sortOrder){
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(songName);
        sb.append("\"");
        if(sortOrder != ORDER_BY_NONE){
            sb.append(QUERY_ARTIST_FOR_SONG_SORT);
            if(sortOrder == ORDER_BY_DESC){
                sb.append("DESC");
            }else {
                sb.append("ASC");
            }
        }
        System.out.println("SQL Statment: "+ sb.toString());
        return applyQuery(sb.toString());
    }

    //Method for showing columns info for tables
    public void querySongsMetadata(){
        String sql = "SELECT * FROM " + TABLE_SONGS;
        try(Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sql)){
            ResultSetMetaData meta = results.getMetaData();
            int numColumns = meta.getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                System.out.format(" Column %d int the songs table is names %s\n", i , meta.getColumnName(i));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public int getCount(String table){
        String sql = "SELECT COUNT(*) AS COUNT FROM " + table;
        try(Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sql)){
            int count = results.getInt("count");
            return count;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return -1;
        }
    }

    //Create View in DB
    public boolean createViewForSongArtists(){
        try(Statement statement = conn.createStatement())
        {
            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<SongArtist> querySongInfoView(String title){
        try{
            querySongInfoView.setString(1, title);
            ResultSet results = querySongInfoView.executeQuery();
            List<SongArtist> songArtists = new ArrayList<>();
            while(results.next()){
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);
            }
            return songArtists;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    //Method to remove duplicate code
    private List<SongArtist> applyQuery(String query){
        try(Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(query)){
            List<SongArtist> songArtists = new ArrayList<>();
            while(results.next()){
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);
            }
            return songArtists;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
