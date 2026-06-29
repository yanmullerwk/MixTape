package br.com.mixtape.view;

import br.com.mixtape.model.Artist;
import br.com.mixtape.model.Song;
import br.com.mixtape.dao.ArtistDAO;
import br.com.mixtape.dao.SongDAO;

import java.util.List;
import java.util.Scanner;

public class SongMenu {

    private final Scanner scanner;
    private final SongDAO songDAO;
    private final ArtistDAO artistDAO;

    public SongMenu(Scanner scanner) {
        this.scanner = scanner;
        this.songDAO = new SongDAO();
        this.artistDAO = new ArtistDAO();
    }

    public void show() {
        int option = -1;
        while (option != 0) {
            System.out.println("\n===== MÚSICAS =====");
            System.out.println("1. Cadastrar música");
            System.out.println("2. Listar todas");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Editar música");
            System.out.println("5. Excluir música");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");

            option = Integer.parseInt(scanner.nextLine().trim());

            switch (option) {
                case 1 -> create();
                case 2 -> listAll();
                case 3 -> findById();
                case 4 -> edit();
                case 5 -> remove();
                case 0 -> System.out.println("Voltando...");
                default -> System.out.println("[!] Opção inválida.");
            }
        }
    }

    private void create() {
        System.out.print("Título da música: ");
        String title = scanner.nextLine();

        System.out.print("Duração em segundos: ");
        int duration = Integer.parseInt(scanner.nextLine());

        System.out.print("Link de reprodução (Spotify, YouTube, etc.): ");
        String link = scanner.nextLine();

        System.out.println("--- Artistas disponíveis ---");
        artistDAO.findAll().forEach(System.out::println);
        System.out.print("ID do artista (0 para nenhum): ");
        int artistId = Integer.parseInt(scanner.nextLine());

        Artist artist = (artistId > 0) ? artistDAO.findById(artistId) : null;

        songDAO.save(new Song(title, duration, link, artist));
    }

    private void listAll() {
        List<Song> list = songDAO.findAll();
        if (list.isEmpty()) System.out.println("[!] Nenhuma música cadastrada.");
        else list.forEach(System.out::println);
    }

    private void findById() {
        System.out.print("ID da música: ");
        int id = Integer.parseInt(scanner.nextLine());
        Song s = songDAO.findById(id);
        System.out.println(s != null ? s : "[!] Não encontrada.");
    }

    private void edit() {
        System.out.print("ID da música a editar: ");
        int id = Integer.parseInt(scanner.nextLine());
        Song s = songDAO.findById(id);
        if (s == null) { System.out.println("[!] Não encontrada."); return; }

        System.out.print("Novo título [" + s.getTitle() + "]: ");
        String title = scanner.nextLine();
        if (!title.isBlank()) s.setTitle(title);

        System.out.print("Nova duração em segundos [" + s.getDurationSeconds() + "]: ");
        String dur = scanner.nextLine();
        if (!dur.isBlank()) s.setDurationSeconds(Integer.parseInt(dur));

        System.out.print("Novo link [" + s.getPlaybackLink() + "]: ");
        String link = scanner.nextLine();
        if (!link.isBlank()) s.setPlaybackLink(link);

        songDAO.update(s);
    }

    private void remove() {
        System.out.print("ID da música a excluir: ");
        int id = Integer.parseInt(scanner.nextLine());
        songDAO.delete(id);
    }
}
