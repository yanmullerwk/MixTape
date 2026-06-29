package br.com.mixtape.view;

import br.com.mixtape.model.User;
import br.com.mixtape.dao.UserDAO;

import java.util.List;
import java.util.Scanner;

public class UserMenu {

    private final Scanner scanner;
    private final UserDAO dao;

    public UserMenu(Scanner scanner) {
        this.scanner = scanner;
        this.dao = new UserDAO();
    }

    public void show() {
        int option = -1;
        while (option != 0) {
            System.out.println("\n===== USUÁRIOS =====");
            System.out.println("1. Cadastrar usuário");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Editar usuário");
            System.out.println("5. Excluir usuário");
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
        System.out.print("Nome: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String password = scanner.nextLine();

        dao.save(new User(name, email, password));
    }

    private void listAll() {
        List<User> list = dao.findAll();
        if (list.isEmpty()) System.out.println("[!] Nenhum usuário cadastrado.");
        else list.forEach(System.out::println);
    }

    private void findById() {
        System.out.print("ID do usuário: ");
        int id = Integer.parseInt(scanner.nextLine());
        User u = dao.findById(id);
        System.out.println(u != null ? u : "[!] Não encontrado.");
    }

    private void edit() {
        System.out.print("ID do usuário a editar: ");
        int id = Integer.parseInt(scanner.nextLine());
        User u = dao.findById(id);
        if (u == null) { System.out.println("[!] Não encontrado."); return; }

        System.out.print("Novo nome [" + u.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.isBlank()) u.setName(name);

        System.out.print("Novo email [" + u.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.isBlank()) u.setEmail(email);

        System.out.print("Nova senha (vazio para manter): ");
        String password = scanner.nextLine();
        if (!password.isBlank()) u.setPassword(password);

        dao.update(u);
    }

    private void remove() {
        System.out.print("ID do usuário a excluir: ");
        int id = Integer.parseInt(scanner.nextLine());
        dao.delete(id);
    }
}
