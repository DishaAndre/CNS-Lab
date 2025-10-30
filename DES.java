import java.util.*;

public class DES {

    // ------------------------
    // DES Hardcoded Tables
    // ------------------------
    static int[] IP = {
        58,50,42,34,26,18,10,2,60,52,44,36,28,20,12,4,
        62,54,46,38,30,22,14,6,64,56,48,40,32,24,16,8,
        57,49,41,33,25,17,9,1,59,51,43,35,27,19,11,3,
        61,53,45,37,29,21,13,5,63,55,47,39,31,23,15,7
    };

    static int[] IP_INV = {
        40,8,48,16,56,24,64,32,39,7,47,15,55,23,63,31,
        38,6,46,14,54,22,62,30,37,5,45,13,53,21,61,29,
        36,4,44,12,52,20,60,28,35,3,43,11,51,19,59,27,
        34,2,42,10,50,18,58,26,33,1,41,9,49,17,57,25
    };

    static int[] E = {
        32,1,2,3,4,5,4,5,6,7,8,9,
        8,9,10,11,12,13,12,13,14,15,16,17,
        16,17,18,19,20,21,20,21,22,23,24,25,
        24,25,26,27,28,29,28,29,30,31,32,1
    };

    static int[] P = {
        16,7,20,21,29,12,28,17,
        1,15,23,26,5,18,31,10,
        2,8,24,14,32,27,3,9,
        19,13,30,6,22,11,4,25
    };

    static int[] PC1 = {
        57,49,41,33,25,17,9,
        1,58,50,42,34,26,18,
        10,2,59,51,43,35,27,
        19,11,3,60,52,44,36,
        63,55,47,39,31,23,15,
        7,62,54,46,38,30,22,
        14,6,61,53,45,37,29,
        21,13,5,28,20,12,4
    };

    static int[] PC2 = {
        14,17,11,24,1,5,3,28,15,6,21,10,
        23,19,12,4,26,8,16,7,27,20,13,2,
        41,52,31,37,47,55,30,40,51,45,33,48,
        44,49,39,56,34,53,46,42,50,36,29,32
    };

    static int[] SHIFTS = {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};

    static int[][][] S_BOX = {
        { {14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
          {0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
          {4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
          {15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13} },

        { {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
          {3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
          {0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
          {13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9} },

        { {10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
          {13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
          {13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
          {1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12} },

        { {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
          {13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
          {10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
          {3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14} },

        { {2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
          {14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
          {4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
          {11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3} },

        { {12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
          {10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
          {9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
          {4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13} },

        { {4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
          {13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
          {1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
          {6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12} },

        { {13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
          {1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
          {7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
          {2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11} }
    };

    // ------------------------
    // Helper Functions
    // ------------------------
    static String permute(String in, int[] table, int n) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < n; i++)
            out.append(in.charAt(table[i] - 1));
        return out.toString();
    }

    static String xorBits(String a, String b) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < a.length(); i++)
            out.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        return out.toString();
    }

    static String leftShift(String in, int n) {
        return in.substring(n) + in.substring(0, n);
    }

    // ------------------------
    // Key Generation (16 subkeys)
    // ------------------------
    static List<String> generateSubKeys(String key64) {
        String key56 = permute(key64, PC1, 56);
        String C = key56.substring(0, 28);
        String D = key56.substring(28, 56);
        List<String> subkeys = new ArrayList<>();

        for (int i = 0; i < 16; i++) {
            C = leftShift(C, SHIFTS[i]);
            D = leftShift(D, SHIFTS[i]);
            String CD = C + D;
            String Ki = permute(CD, PC2, 48);
            subkeys.add(Ki);
        }
        return subkeys;
    }

    // ------------------------
    // f-function
    // ------------------------
    static String fFunction(String R, String K) {
        String RE = permute(R, E, 48);
        String xorR = xorBits(RE, K);
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            String block = xorR.substring(i * 6, (i + 1) * 6);
            int row = (block.charAt(0) - '0') * 2 + (block.charAt(5) - '0');
            int col = (block.charAt(1) - '0') * 8 + (block.charAt(2) - '0') * 4 +
                      (block.charAt(3) - '0') * 2 + (block.charAt(4) - '0');
            int val = S_BOX[i][row][col];
            for (int j = 3; j >= 0; j--)
                out.append(((val >> j) & 1) == 1 ? '1' : '0');
        }

        return permute(out.toString(), P, 32);
    }

    // ------------------------
    // DES Encryption
    // ------------------------
    static String DES_encrypt(String plaintext64, String key64) {
        List<String> subkeys = generateSubKeys(key64);
        String ip = permute(plaintext64, IP, 64);
        String L = ip.substring(0, 32);
        String R = ip.substring(32, 64);

        for (int i = 0; i < 16; i++) {
            String tempR = R;
            String fR = fFunction(R, subkeys.get(i));
            R = xorBits(L, fR);
            L = tempR;
        }
        String preoutput = R + L;
        return permute(preoutput, IP_INV, 64);
    }

    // ------------------------
    // Pad 8-bit input to 64-bit
    // ------------------------
    static String expand8to64(String input8) {
        StringBuilder sb = new StringBuilder(input8);
        while (sb.length() < 64)
            sb.append('0');
        return sb.toString();
    }

    // ------------------------
    // MAIN
    // ------------------------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter 8-bit plaintext (binary): ");
        String plaintext8 = sc.next();
        System.out.print("Enter 8-bit key (binary): ");
        String key8 = sc.next();

        if (plaintext8.length() != 8 || key8.length() != 8) {
            System.out.println("Error: input must be 8 bits.");
            return;
        }

        String plaintext64 = expand8to64(plaintext8);
        String key64 = expand8to64(key8);

        String ciphertext = DES_encrypt(plaintext64, key64);
        System.out.println("Ciphertext (64-bit): " + ciphertext);

        System.out.println("\nConclusion: SDES demonstrates the basic principles of modern encryption but is not secure for real-world use due to its small key size.");

    }
}
