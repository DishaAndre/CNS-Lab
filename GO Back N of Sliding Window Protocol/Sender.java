import java.io.*;
import java.net.*;
import java.util.*;

public class Sender {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12548);
        System.out.println("Successfully connected to the server.");

        OutputStream out = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        InputStream in = socket.getInputStream();
        DataInputStream dis = new DataInputStream(in);

        Scanner sc = new Scanner(System.in);

        System.out.print("Please enter the total number of frames you want: ");
        int n = sc.nextInt();
        dos.writeInt(n);
        dos.flush();

        int[] frames = new int[n];
        for (int i = 0; i < n; i++) {
            frames[i] = dis.readInt();
            System.out.println("Frame received: " + frames[i]);
        }

        System.out.print("Would you like to simulate a lost frame? (true/false): ");
        boolean loss = sc.nextBoolean();
        dos.writeBoolean(loss);
        dos.flush();

        if (loss) {
            System.out.print("Enter the frame number that was lost: ");
            int lostFrame = sc.nextInt();
            dos.writeInt(lostFrame);
            dos.flush();

            System.out.println("Waiting for retransmission of lost frames...");
            for (int i = lostFrame - 1; i < n; i++) {
                frames[i] = dis.readInt();
                System.out.println("Retransmitted frame received: " + frames[i]);
            }
        }

        dis.close();
        dos.close();
        socket.close();
        System.out.println("Disconnected from server.");
        sc.close();
    }
}
