import java.io.*;
import java.net.*;
import java.util.Scanner;

public class WalletClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 5555;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to Wallet Server.");

            while (true) {
                System.out.print("Enter Instruction (CR or DB): ");
                String instruction = scanner.nextLine().trim().toUpperCase();

                if (!instruction.equals("CR") && !instruction.equals("DB")) {
                    System.out.println("Invalid instruction. Use CR or DB.");
                    continue;
                }

                System.out.print("Enter Amount (0 - 65535): ");
                int amount = Integer.parseInt(scanner.nextLine());

                if (amount < 0 || amount > 65535) {
                    System.out.println("Amount out of range.");
                    continue;
                }

                // Send instruction
                out.write(instruction.getBytes("UTF-8")); // 2 bytes
                out.writeShort(amount); // 2 bytes
                out.flush();

                // Receive response
                byte[] responseCodeBytes = new byte[2];
                in.readFully(responseCodeBytes);
                String responseCode = new String(responseCodeBytes, "UTF-8");

                int responseValue = in.readUnsignedShort();

                if (responseCode.equals("BA")) {
                    System.out.println("Balance: " + responseValue);
                } else {
                    System.out.println("Error: Transaction failed.");
                }

                System.out.println("-------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
