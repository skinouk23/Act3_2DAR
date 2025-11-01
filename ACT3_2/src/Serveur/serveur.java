package Serveur;

import java.io.*;
import java.net.*;
import Process.ClientProcess;

public class serveur {
    public static void main(String[] args) {
        int port = 8888;
        
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // Afficher l'IP de la machine
            String localIP = InetAddress.getLocalHost().getHostAddress();
            System.out.println("🟢 Serveur de calculatrice démarré!");
            System.out.println("📍 IP locale: " + localIP);
            System.out.println("🔌 Port: " + port);
            System.out.println("⏳ En attente de connexions clients...");
            System.out.println("🌐 Les clients doivent se connecter à: " + localIP + ":" + port);
            System.out.println("==========================================");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                int numClient = getNextClientNumber();
                
                InetAddress clientAddress = clientSocket.getInetAddress();
                
                System.out.println("\n=== NOUVEAU CLIENT CONNECTÉ ===");
                System.out.println("🔢 Client n°" + numClient);
                System.out.println("🌐 Adresse IP: " + clientAddress.getHostAddress());
                System.out.println("💻 Nom d'hôte: " + clientAddress.getHostName());
                System.out.println("📊 Opérations traitées total: " + ClientProcess.getGlobalOperationCounter());
                System.out.println("===============================");
                
                ClientProcess clientProcess = new ClientProcess(clientSocket, numClient);
                clientProcess.start();
            }
            
        } catch (IOException e) {
            System.err.println("❌ Erreur du serveur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int clientCounter = 0;
    private static synchronized int getNextClientNumber() {
        return ++clientCounter;
    }
}