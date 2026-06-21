package br.com.mixtape.database;

import org.mariadb.jdbc.Connection;
import org.mariadb.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    private static final String SCHEMA = "mixtape_db";

    private static final String URL_WOUT_SCHEMA =
            "jdbc:mariadb://" + HOST + ":" + PORT;

    private static final String URL_WITH_SCHEMA =
            "jdbc:mariadb://" + HOST + ":" + PORT + "/" + SCHEMA;


    public static Connection getConnection() throws SQLException {
        return (Connection) DriverManager.getConnection(URL_WITH_SCHEMA, USER, PASSWORD);
    }


    public static void startSchema() {
        System.out.println("[DB] Inicializando schema...");

        try (
                Connection conn = (Connection) DriverManager.getConnection(
                        URL_WOUT_SCHEMA,
                        USER,
                        PASSWORD
                );
                Statement stmt = conn.createStatement()
        ) {

            // Cria o banco
            stmt.executeUpdate("""
                CREATE DATABASE IF NOT EXISTS mixtape_db
                CHARACTER SET utf8mb4
                COLLATE utf8mb4_unicode_ci
            """);

            System.out.println("[DB] Banco verificado/criado.");

        } catch (SQLException e) {
            System.err.println("[DB] Erro ao criar banco: " + e.getMessage());
            throw new RuntimeException(e);
        }

        // Conecta ao banco criado e cria as tabelas
        try (
                Connection conn = getConnection();
                Statement stmt = conn.createStatement()
        ) {

            // Usuarios
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE,
                    senha VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                ) ENGINE=InnoDB
            """);

            // Artistas
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS artists (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    genero_musical VARCHAR(80) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                ) ENGINE=InnoDB
            """);

            // Musicas
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS musics (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    titulo VARCHAR(150) NOT NULL,
                    duracao_segundos INT NOT NULL DEFAULT 0,
                    link_reproducao VARCHAR(500) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    artista_id INT,
                    CONSTRAINT fk_musica_artista
                        FOREIGN KEY (artista_id)
                        REFERENCES artistas(id)
                        ON DELETE SET NULL
                ) ENGINE=InnoDB
            """);

            // Playlists
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS playlists (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nome VARCHAR(150) NOT NULL,
                    descricao TEXT,
                    usuario_id INT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                    CONSTRAINT fk_playlist_usuario
                        FOREIGN KEY (usuario_id)
                        REFERENCES usuarios(id)
                        ON DELETE CASCADE
                ) ENGINE=InnoDB
            """);

            // Relação Playlist x Música
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS playlist_music (
                    playlist_id INT NOT NULL,
                    musica_id INT NOT NULL,
                    PRIMARY KEY (playlist_id, musica_id),

                    CONSTRAINT fk_pm_playlist
                        FOREIGN KEY (playlist_id)
                        REFERENCES playlists(id)
                        ON DELETE CASCADE,

                    CONSTRAINT fk_pm_musica
                        FOREIGN KEY (musica_id)
                        REFERENCES musicas(id)
                        ON DELETE CASCADE
                ) ENGINE=InnoDB
            """);

            System.out.println("[DB] Schema '" + SCHEMA + "' e tabelas prontos!");

        } catch (SQLException e) {
            System.err.println("[DB] ERRO ao criar tabelas: " + e.getMessage());
            throw new RuntimeException(
                    "Falha na inicialização do banco de dados.",
                    e
            );
        }
    }
}
