package br.com.mixtape.dao;

import br.com.mixtape.model.Artist;
import br.com.mixtape.model.Song;
import br.com.mixtape.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO implements IDao<Song> {

    private final ArtistDAO artistDAO = new ArtistDAO();

    @Override
    public void save(Song song) {
        String sql = "INSERT INTO songs (title, duration_seconds, playback_link, artist_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, song.getTitle());
            stmt.setInt(2, song.getDurationSeconds());
            stmt.setString(3, song.getPlaybackLink());
            if (song.getArtist() != null) {
                stmt.setInt(4, song.getArtist().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) song.setId(keys.getInt(1));

            System.out.println("Musica salva: " + song);

        } catch (SQLException e) {
            System.err.println("erro" + e.getMessage());
        }
    }

    @Override
    public void update(Song song) {
        String sql = "UPDATE songs SET title=?, duration_seconds=?, playback_link=?, artist_id=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, song.getTitle());
            stmt.setInt(2, song.getDurationSeconds());
            stmt.setString(3, song.getPlaybackLink());
            if (song.getArtist() != null) {
                stmt.setInt(4, song.getArtist().getId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, song.getId());
            stmt.executeUpdate();

            System.out.println("Musica atualizada: " + song);

        } catch (SQLException e) {
            System.err.println("erro" + e.getMessage());
        }
    }

    @Override
    public Song findById(int id) {
        String sql = "SELECT * FROM songs WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("erro" + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Song> findAll() {
        List<Song> list = new ArrayList<>();
        String sql = "SELECT * FROM songs ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

            if (list.isEmpty()) {
                System.out.println("Nenhuma música cadastrada.");
            }

        } catch (SQLException e) {
            System.err.println("erro" + e.getMessage());
        }

        return list;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM songs WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0
                    ? "Musica de ID =" + id + " deletada."
                    : "Musica não encontrada.");

        } catch (SQLException e) {
            System.err.println("erro" + e.getMessage());
        }
    }

    private Song mapRow(ResultSet rs) throws SQLException {
        int artistId = rs.getInt("artist_id");
        Artist artist = rs.wasNull() ? null : artistDAO.findById(artistId);

        return new Song(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getInt("duration_seconds"),
                rs.getString("playback_link"),
                artist
        );
    }
}
