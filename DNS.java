import java.net.InetAddress;
import java.net.UnknownHostException;

public class DNS {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java DNSLookup <hostname|IP address>");
            System.exit(-1);
        }

        try {
            InetAddress inetAddress;

            // Check if input is an IP address (starts with digit)
            if (Character.isDigit(args[0].charAt(0))) {
                // Convert IP string to byte array
                byte[] b = new byte[4];
                String[] bytes = args[0].split("\\.");

                if (bytes.length != 4) {
                    System.err.println("Invalid IP address format.");
                    System.exit(-1);
                }

                for (int i = 0; i < 4; i++) {
                    int intVal = Integer.parseInt(bytes[i]);
                    if (intVal < 0 || intVal > 255) {
                        System.err.println("Invalid IP address segment: " + bytes[i]);
                        System.exit(-1);
                    }
                    b[i] = (byte) intVal;
                }

                inetAddress = InetAddress.getByAddress(b);
            } else {
                // Input is a hostname
                inetAddress = InetAddress.getByName(args[0]);
            }

            // Output the hostname and IP address
            System.out.println("Host Name: " + inetAddress.getHostName());
            System.out.println("IP Address: " + inetAddress.getHostAddress());

        } catch (UnknownHostException e) {
            System.err.println("Host not found: " + e.getMessage());
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.err.println("Invalid IP address format.");
            System.exit(-1);
        }
    }
}