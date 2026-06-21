package br.com.mixtape;

import br.com.mixtape.database.DBConnection;

import static br.com.mixtape.database.DBConnection.startSchema;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        startSchema();
    }
}
