import java.io.*;
import java.net.*;
import java.util.*;

public class Receiver {
    public Receiver() { }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(12548);
        System.out.println("Server is up and running. Awaiting client connections...");

        while (true) {
            Socket socket = server.accept();
            System.out.println("A client has connected.");

            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);

            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            int n = dis.readInt();
            System.out.println("Client requested " + n + " frames.");

            int[] frames = new int[n];
            for (int i = 0; i < n; i++) {
                frames[i] = i + 1;
            }

            for (int i = 0; i < n; i++) {
                dos.writeInt(frames[i]);
            }
            dos.flush();

            boolean loss = dis.readBoolean();
            if (loss) {
                int lostFrame = dis.readInt();
                System.out.println("Notice: Client reported a lost frame at number " + lostFrame);
                System.out.println("Resending frames starting from frame " + lostFrame + "...");

                for (int i = lostFrame - 1; i < n; i++) {
                    dos.writeInt(frames[i]);
                }
                dos.flush();
            }

            dis.close();
            dos.close();
            socket.close();
            System.out.println("Client connection terminated.\n");
        }
    }
}
