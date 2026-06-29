package br.com.mixtape.view;

import br.com.mixtape.model.Playlist;
import br.com.mixtape.model.Song;
import br.com.mixtape.model.User;
import br.com.mixtape.dao.PlaylistDAO;
import br.com.mixtape.dao.SongDAO;
import br.com.mixtape.dao.UserDAO;

import java.util.List;
import java.util.Scanner;

public class PlaylistMenu {

    private final Scanner scanner;
    private final PlaylistDAO playlistDAO;
    private final UserDAO userDAO;
    private final SongDAO songDAO;

    public PlaylistMenu(Scanner scanner) {
        this.scanner = scanner;
        this.playlistDAO = new PlaylistDAO();
        this.userDAO = new UserDAO();
        this.songDAO = new SongDAO();
    }

    public void show() {
        int option = -1;
        while (option != 0) {
            System.out.println("\n===== PLAYLISTS =====");
            System.out.println("1. Criar playlist");
            System.out.println("2. Listar todas");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Editar playlist");
            System.out.println("5. Excluir playlist");
            System.out.println("6. Adicionar música à playlist");
            System.out.println("7. Remover música da playlist");
            System.out.println("8. Ver músicas de uma playlist");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");

            option = Integer.parseInt(scanner.nextLine().trim());

            switch (option) {
                case 1 -> create();
                case 2 -> listAll();
                case 3 -> findById();
                case 4 -> edit();
                case 5 -> remove();
                case 6 -> addSong();
                case 7 -> removeSong();
                case 8 -> listSongs();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("[!] Opção inválida.");
            }
        }
    }

    private void create() {
        System.out.print("Nome da playlist: ");
        String name = scanner.nextLine();
        System.out.print("Descrição: ");
        String description = scanner.nextLine();

        System.out.println("--- Usuários disponíveis ---");
        userDAO.findAll().forEach(System.out::println);
        System.out.print("ID do usuário dono (0 para nenhum): ");
        int uid = Integer.parseInt(scanner.nextLine());
        User owner = (uid > 0) ? userDAO.findById(uid) : null;

        playlistDAO.save(new Playlist(name, description, owner));
    }

    private void listAll() {
        List<Playlist> list = playlistDAO.findAll();
        if (list.isEmpty()) System.out.println("[!] Nenhuma playlist cadastrada.");
        else list.forEach(System.out::println);
    }

    private void findById() {
        System.out.print("ID da playlist: ");
        int id = Integer.parseInt(scanner.nextLine());
        Playlist p = playlistDAO.findById(id);
        System.out.println(p != null ? p : "[!] Não encontrada.");
    }

    private void edit() {
        System.out.print("ID da playlist a editar: ");
        int id = Integer.parseInt(scanner.nextLine());
        Playlist p = playlistDAO.findById(id);
        if (p == null) { System.out.println("[!] Não encontrada."); return; }

        System.out.print("Novo nome [" + p.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.isBlank()) p.setName(name);

        System.out.print("Nova descrição [" + p.getDescription() + "]: ");
        String description = scanner.nextLine();
        if (!description.isBlank()) p.setDescription(description);

        playlistDAO.update(p);
    }

    private void remove() {
        System.out.print("ID da playlist a excluir: ");
        int id = Integer.parseInt(scanner.nextLine());
        playlistDAO.delete(id);
    }

    private void addSong() {
        System.out.print("ID da playlist: ");
        int pid = Integer.parseInt(scanner.nextLine());

        System.out.println("--- Músicas disponíveis ---");
        songDAO.findAll().forEach(System.out::println);
        System.out.print("ID da música a adicionar: ");
        int sid = Integer.parseInt(scanner.nextLine());

        playlistDAO.addSong(pid, sid);
    }

    private void removeSong() {
        System.out.print("ID da playlist: ");
        int pid = Integer.parseInt(scanner.nextLine());
        System.out.print("ID da música a remover: ");
        int sid = Integer.parseInt(scanner.nextLine());

        playlistDAO.removeSong(pid, sid);
    }

    private void listSongs() {
        System.out.print("ID da playlist: ");
        int pid = Integer.parseInt(scanner.nextLine());
        Playlist p = playlistDAO.findById(pid);
        if (p == null) { System.out.println("[!] Playlist não encontrada."); return; }

        System.out.println("\n🎵 Músicas da playlist: " + p.getName());
        List<Song> songs = playlistDAO.findSongsByPlaylist(pid);
        if (songs.isEmpty()) {
            System.out.println("  [!] Nenhuma música nesta playlist ainda.");
        } else {
            songs.forEach(s -> System.out.println("  → " + s));
        }
    }
}
