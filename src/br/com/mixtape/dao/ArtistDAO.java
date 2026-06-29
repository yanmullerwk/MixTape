package br.com.mixtape.dao;

import br.com.mixtape.model.Artist;
import br.com.mixtape.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistDAO implements IDao<Artist> {

    @Override
    public void save(Artist artist) {
        String sql = "INSERT INTO artists (name, musical_genre) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getMusicalGenre());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) artist.setId(keys.getInt(1));

            System.out.println("[✓] Artist saved: " + artist);

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    @Override
    public void update(Artist artist) {
        String sql = "UPDATE artists SET name=?, musical_genre=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getMusicalGenre());
            stmt.setInt(3, artist.getId());
            stmt.executeUpdate();

            System.out.println("[✓] Artist updated: " + artist);

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    @Override
    public Artist findById(int id) {
        String sql = "SELECT * FROM artists WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Artist> findAll() {
        List<Artist> list = new ArrayList<>();
        String sql = "SELECT * FROM artists ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
        return list;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM artists WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0
                    ? "[✓] Artist ID " + id + " deleted."
                    : "[!] Artist not found.");

        } catch (SQLException e) {
            System.err.println("[ERROR] " + e.getMessage());
        }
    }

    private Artist mapRow(ResultSet rs) throws SQLException {
        return new Artist(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("musical_genre")
        );
    }
}
