package br.com.mixtape;

import br.com.mixtape.view.ArtistMenu;
import br.com.mixtape.view.PlaylistMenu;
import br.com.mixtape.view.SongMenu;
import br.com.mixtape.view.UserMenu;
import br.com.mixtape.database.DatabaseConnection;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        DatabaseConnection.initializeSchema();

        Scanner scanner = new Scanner(System.in);

        UserMenu     userMenu     = new UserMenu(scanner);
        ArtistMenu   artistMenu   = new ArtistMenu(scanner);
        SongMenu     songMenu     = new SongMenu(scanner);
        PlaylistMenu playlistMenu = new PlaylistMenu(scanner);

        int option = -1;
        while (option != 0) {
            System.out.println("\n╔══════════════════════════╗");
            System.out.println("║       🎵 MixTape         ║");
            System.out.println("╠══════════════════════════╣");
            System.out.println("║  1. Usuários             ║");
            System.out.println("║  2. Artistas             ║");
            System.out.println("║  3. Músicas              ║");
            System.out.println("║  4. Playlists            ║");
            System.out.println("║  0. Sair                 ║");
            System.out.println("╚══════════════════════════╝");
            System.out.print("Opção: ");

            try {
                option = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[!] Digite apenas números.");
                continue;
            }

            switch (option) {
                case 1 -> userMenu.show();
                case 2 -> artistMenu.show();
                case 3 -> songMenu.show();
                case 4 -> playlistMenu.show();
                case 0 -> System.out.println("Até logo! 🎶");
                default -> System.out.println("[!] Opção inválida.");
            }
        }

        scanner.close();
    }
}
