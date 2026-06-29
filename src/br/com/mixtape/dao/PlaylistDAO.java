package br.com.mixtape.dao;

import br.com.mixtape.model.Playlist;
import br.com.mixtape.model.Song;
import br.com.mixtape.model.User;
import br.com.mixtape.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO implements IDao<Playlist> {

    private final UserDAO userDAO = new UserDAO();
    private final SongDAO songDAO = new SongDAO();

    @Override
    public void save(Playlist playlist) {
        String sql = "INSERT INTO playlists (name, description, user_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, playlist.getName());
            stmt.setString(2, playlist.getDescription());
            if (playlist.getOwner() != null) {
                stmt.setInt(3, playlist.getOwner().getId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) playlist.setId(keys.getInt(1));

            System.out.println("[✓] Playlist saved: " + playlist);

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    @Override
    public void update(Playlist playlist) {
        String sql = "UPDATE playlists SET name=?, description=?, user_id=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, playlist.getName());
            stmt.setString(2, playlist.getDescription());
            if (playlist.getOwner() != null) {
                stmt.setInt(3, playlist.getOwner().getId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, playlist.getId());
            stmt.executeUpdate();

            System.out.println("[✓] Playlist updated: " + playlist);

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    @Override
    public Playlist findById(int id) {
        String sql = "SELECT * FROM playlists WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Playlist p = mapRow(rs);
                p.setSongs(findSongsByPlaylist(p.getId()));
                return p;
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Playlist> findAll() {
        List<Playlist> list = new ArrayList<>();
        String sql = "SELECT * FROM playlists ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Playlist p = mapRow(rs);
                p.setSongs(findSongsByPlaylist(p.getId()));
                list.add(p);
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return list;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM playlists WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0
                    ? "[✓] Playlist ID " + id + " deleted."
                    : "[!] Playlist not found.");

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    public void addSong(int playlistId, int songId) {
        String sql = "INSERT IGNORE INTO playlist_song (playlist_id, song_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);
            stmt.executeUpdate();
            System.out.println("[✓] Song added to playlist.");

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    public void removeSong(int playlistId, int songId) {
        String sql = "DELETE FROM playlist_song WHERE playlist_id=? AND song_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0
                    ? "[✓] Song removed from playlist."
                    : "[!] Link not found.");

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    public List<Song> findSongsByPlaylist(int playlistId) {
        List<Song> list = new ArrayList<>();
        String sql = """
            SELECT s.* FROM songs s
            INNER JOIN playlist_song ps ON ps.song_id = s.id
            WHERE ps.playlist_id = ?
            ORDER BY s.title
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playlistId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(songDAO.findById(rs.getInt("id")));

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return list;
    }

    private Playlist mapRow(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        User owner = rs.wasNull() ? null : userDAO.findById(userId);

        return new Playlist(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                owner
        );
    }
}
