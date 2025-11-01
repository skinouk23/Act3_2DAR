package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import Process.Operation;

public class client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 8888;
        
        if (args.length >= 1) {
            serverAddress = args[0];
        }
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        }

        try (
            Socket socket = new Socket(serverAddress, port);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in)
        ) {
            String welcomeMessage = br.readLine();
            System.out.println("Serveur: " + welcomeMessage);

            boolean continueCalculations = true;
            
            while (continueCalculations) {
                System.out.println("\n=== CALCULATRICE DISTANTE ===");
                System.out.println("Format: nombre opérateur nombre");
                System.out.println("Opérateurs supportés: +, -, *, /");
                System.out.println("Tapez 'quit' pour quitter");
                System.out.print("Entrez votre opération: ");
                
                String input = scanner.nextLine().trim();
                
                if (input.equalsIgnoreCase("quit")) {
                    break;
                }

                try {
                    // Parser l'opération
                    String[] parts = input.split(" ");
                    if (parts.length != 3) {
                        System.out.println("❌ Format invalide. Utilisez: nombre opérateur nombre");
                        continue;
                    }

                    double operand1 = Double.parseDouble(parts[0]);
                    char operator = parts[1].charAt(0);
                    double operand2 = Double.parseDouble(parts[2]);

                    // Créer l'objet Operation
                    Operation operation = new Operation(operand1, operand2, operator);

                    // Envoyer l'opération au serveur
                    oos.writeObject(operation);
                    oos.flush();

                    // prendre réponse
                    Operation resultOperation = (Operation) ois.readObject();

                    if (!Double.isNaN(resultOperation.getResult())) {
                        System.out.println(" Résultat: " + resultOperation);
                        System.out.println(" ID de l'opération: " + resultOperation.getOperationId());
                    } else {
                        System.out.println("❌ Erreur lors du calcul");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("❌ Nombre invalide");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("❌ Format invalide");
                }

                // Demander si l'utilisateur veut continuer
                System.out.print("Voulez-vous faire un autre calcul? (o/n): ");
                String response = scanner.nextLine().trim();
                continueCalculations = response.equalsIgnoreCase("o");
            }

            System.out.println("👋 Déconnexion...");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Erreur client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}