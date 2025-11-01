package Process;

import java.io.*;
import java.net.Socket;

public class ClientProcess extends Thread {
    private Socket socket;
    private int numClient;
    private static int globalOperationCounter = 0;
    private static final Object counterLock = new Object();

    public ClientProcess(Socket s, int num) {
        this.socket = s;
        this.numClient = num;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)
        ) {
            pw.println("Vous êtes le client n°" + numClient);

            Operation operation = (Operation) ois.readObject();
            System.out.println("Client " + numClient + " a envoyé : " + 
                             operation.getOperand1() + " " + operation.getOperator() + " " + operation.getOperand2());

            try {
                operation.calculate();
                
                int operationId;
                synchronized (counterLock) {
                    globalOperationCounter++;
                    operationId = globalOperationCounter;
                }
                operation.setOperationId(operationId);
                
                System.out.println(" Opération " + operationId + " traitée: " + operation);
                
            } catch (ArithmeticException e) {
                operation.setResult(Double.NaN);
                System.out.println("❌ Erreur calcul Client " + numClient + ": " + e.getMessage());
            } catch (IllegalArgumentException e) {
                operation.setResult(Double.NaN);
                System.out.println("❌ Erreur opérateur Client " + numClient + ": " + e.getMessage());
            }

            // Envoyer le résultat au client
            oos.writeObject(operation);
            oos.flush();

            System.out.println("V Résultat envoyé au client " + numClient);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Erreur avec le client " + numClient + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println(" Client " + numClient + " déconnecté.");
                }
            } catch (IOException e) {
                System.err.println("❌ Erreur fermeture socket client " + numClient);
            }
        }
    }

    public static int getGlobalOperationCounter() {
        synchronized (counterLock) {
            return globalOperationCounter;
        }
    }
}