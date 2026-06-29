package br.com.mixtape.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String SCHEMA   = "mixtape_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "1234";

    private static final String URL_WITHOUT_SCHEMA =
            "jdbc:mariadb://" + HOST + ":" + PORT + "?useSSL=false&serverTimezone=UTC";

    private static final String URL =
            "jdbc:mariadb://" + HOST + ":" + PORT + "/" + SCHEMA + "?useSSL=false&serverTimezone=UTC";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeSchema() {
        System.out.println("[DB] Initializing schema...");

        try (Connection conn = DriverManager.getConnection(URL_WITHOUT_SCHEMA, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + SCHEMA
                    + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            stmt.executeUpdate("USE " + SCHEMA);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id       INT AUTO_INCREMENT PRIMARY KEY,
                    name     VARCHAR(100) NOT NULL,
                    email    VARCHAR(150) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS artists (
                    id             INT AUTO_INCREMENT PRIMARY KEY,
                    name           VARCHAR(100) NOT NULL,
                    musical_genre  VARCHAR(80)  NOT NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS songs (
                    id               INT AUTO_INCREMENT PRIMARY KEY,
                    title            VARCHAR(150) NOT NULL,
                    duration_seconds INT          NOT NULL DEFAULT 0,
                    playback_link    VARCHAR(500) NOT NULL,
                    artist_id        INT,
                    FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE SET NULL
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS playlists (
                    id          INT AUTO_INCREMENT PRIMARY KEY,
                    name        VARCHAR(150) NOT NULL,
                    description TEXT,
                    user_id     INT,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS playlist_song (
                    playlist_id INT NOT NULL,
                    song_id     INT NOT NULL,
                    PRIMARY KEY (playlist_id, song_id),
                    FOREIGN KEY (playlist_id) REFERENCES playlists(id) ON DELETE CASCADE,
                    FOREIGN KEY (song_id)     REFERENCES songs(id)     ON DELETE CASCADE
                )
            """);

            System.out.println("[DB] Schema '" + SCHEMA + "' ready!");

        } catch (SQLException e) {
            System.err.println("[DB] Error initializing schema: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database.", e);
        }
    }
}
