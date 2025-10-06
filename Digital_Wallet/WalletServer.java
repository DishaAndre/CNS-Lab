import java.io.*;
import java.net.*;

public class WalletServer {
    private static final int PORT = 5555;
    private static final int MAX_BALANCE = 65535;
    private int balance = 0;

    public static void main(String[] args) {
        new WalletServer().startServer();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Wallet Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream())
        ) {
            while (true) {
                // Read 2-byte instruction
                byte[] instrBytes = new byte[2];
                in.readFully(instrBytes);
                String instruction = new String(instrBytes, "UTF-8");

                // Read 2-byte amount
                int amount = in.readUnsignedShort();

                System.out.println("Received: " + instruction + " " + amount);

                String responseCode;
                int responseValue;

                switch (instruction) {
                    case "CR": // Credit
                        if (balance + amount <= MAX_BALANCE) {
                            balance += amount;
                            responseCode = "BA";
                            responseValue = balance;
                        } else {
                            responseCode = "ER";
                            responseValue = 0;
                        }
                        break;

                    case "DB": // Debit
                        if (balance >= amount) {
                            balance -= amount;
                            responseCode = "BA";
                            responseValue = balance;
                        } else {
                            responseCode = "ER";
                            responseValue = 0;
                        }
                        break;

                    default: // Invalid instruction
                        responseCode = "ER";
                        responseValue = 0;
                        break;
                }

                // Send response (2-byte code + 2-byte value)
                out.write(responseCode.getBytes("UTF-8"));
                out.writeShort(responseValue);
                out.flush();
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
