package br.com.mixtape.view;

import br.com.mixtape.model.Artist;
import br.com.mixtape.dao.ArtistDAO;

import java.util.List;
import java.util.Scanner;

public class ArtistMenu {

    private final Scanner scanner;
    private final ArtistDAO dao;

    public ArtistMenu(Scanner scanner) {
        this.scanner = scanner;
        this.dao = new ArtistDAO();
    }

    public void show() {
        int option = -1;
        while (option != 0) {
            System.out.println("\n===== ARTISTAS =====");
            System.out.println("1. Cadastrar artista");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Editar artista");
            System.out.println("5. Excluir artista");
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
        System.out.print("Nome do artista: ");
        String name = scanner.nextLine();
        System.out.print("Gênero musical: ");
        String genre = scanner.nextLine();
        dao.save(new Artist(name, genre));
    }

    private void listAll() {
        List<Artist> list = dao.findAll();
        if (list.isEmpty()) System.out.println("[!] Nenhum artista cadastrado.");
        else list.forEach(System.out::println);
    }

    private void findById() {
        System.out.print("ID do artista: ");
        int id = Integer.parseInt(scanner.nextLine());
        Artist a = dao.findById(id);
        System.out.println(a != null ? a : "[!] Não encontrado.");
    }

    private void edit() {
        System.out.print("ID do artista a editar: ");
        int id = Integer.parseInt(scanner.nextLine());
        Artist a = dao.findById(id);
        if (a == null) { System.out.println("[!] Não encontrado."); return; }

        System.out.print("Novo nome [" + a.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.isBlank()) a.setName(name);

        System.out.print("Novo gênero [" + a.getMusicalGenre() + "]: ");
        String genre = scanner.nextLine();
        if (!genre.isBlank()) a.setMusicalGenre(genre);

        dao.update(a);
    }

    private void remove() {
        System.out.print("ID do artista a excluir: ");
        int id = Integer.parseInt(scanner.nextLine());
        dao.delete(id);
    }
}
