import java.util.*;

public class PlayfairCipher {

    static String[][] matrix = new String[5][5];
    static String key, plaintext;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Accept key
        System.out.print("\nEnter key: ");
        key = sc.nextLine().toLowerCase().replaceAll("[^a-z]", "").replace('j', 'i');

        // Accept plaintext
        System.out.print("\nEnter plaintext: ");
        plaintext = sc.nextLine().toLowerCase().replaceAll("[^a-z]", "").replace('j', 'i');

        // Create Playfair matrix
        createMatrix(key);

        System.out.println("\n5x5 Playfair Matrix:");
        displayMatrix();

        // Prepare plaintext pairs
        String[] pairs = createPairs(plaintext);

        System.out.println("\nPlaintext pairs:");
        for (String pair : pairs) {
            System.out.print(pair + " ");
        }

        // Encrypt plaintext
        String cipherText = encrypt(pairs);

        System.out.println("\n\nCipher Text: " + cipherText);

        // Conclusion
        System.out.println("\nConclusion: ");
        System.out.println("Playfair Cipher encrypts pairs of letters, making it stronger than simple substitution ciphers by reducing frequency patterns. However, it is still not secure against modern cryptanalysis techniques.");
    }

    // Create 5x5 matrix with key
    static void createMatrix(String key) {
        boolean[] used = new boolean[26]; // Track used letters
        int k = 0;

        // Fill matrix with key letters first
        for (char c : key.toCharArray()) {
            int idx = c - 'a';
            if (!used[idx]) {
                if (c == 'i') {
                    matrix[k / 5][k % 5] = "i/j"; // i/j combined
                    used['i' - 'a'] = true;
                    used['j' - 'a'] = true;
                } else {
                    matrix[k / 5][k % 5] = String.valueOf(c);
                    used[idx] = true;
                }
                k++;
            }
        }

        // Fill remaining letters
        for (char c = 'a'; c <= 'z'; c++) {
            if (c == 'j') continue; // Skip 'j' (already included with i)
            int idx = c - 'a';
            if (!used[idx]) {
                if (c == 'i') {
                    matrix[k / 5][k % 5] = "i/j";
                    used['i' - 'a'] = true;
                    used['j' - 'a'] = true;
                } else {
                    matrix[k / 5][k % 5] = String.valueOf(c);
                    used[idx] = true;
                }
                k++;
            }
        }
    }

    // Display the matrix nicely
    static void displayMatrix() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.printf("%-4s", matrix[i][j]);
            }
            System.out.println();
        }
    }

    // Create plaintext pairs
    static String[] createPairs(String text) {
        StringBuilder sb = new StringBuilder(text);
        ArrayList<String> pairs = new ArrayList<>();

        for (int i = 0; i < sb.length(); i += 2) {
            char first = sb.charAt(i);
            char second = 'x';
            if (i + 1 < sb.length()) {
                second = sb.charAt(i + 1);
                if (first == second) {
                    second = 'x';
                    i--; // so next letter forms next pair
                }
            }
            pairs.add("" + first + second);
        }

        return pairs.toArray(new String[0]);
    }

    // Encrypt pairs using Playfair Cipher rules
    static String encrypt(String[] pairs) {
        StringBuilder cipher = new StringBuilder();
        for (String pair : pairs) {
            int[] pos1 = findPosition(pair.charAt(0));
            int[] pos2 = findPosition(pair.charAt(1));

            if (pos1[0] == pos2[0]) { // Same row
                cipher.append(matrix[pos1[0]][(pos1[1] + 1) % 5].replace("/j",""));
                cipher.append(matrix[pos2[0]][(pos2[1] + 1) % 5].replace("/j",""));
            } else if (pos1[1] == pos2[1]) { // Same column
                cipher.append(matrix[(pos1[0] + 1) % 5][pos1[1]].replace("/j",""));
                cipher.append(matrix[(pos2[0] + 1) % 5][pos2[1]].replace("/j",""));
            } else { // Rectangle
                cipher.append(matrix[pos1[0]][pos2[1]].replace("/j",""));
                cipher.append(matrix[pos2[0]][pos1[1]].replace("/j",""));
            }
        }
        return cipher.toString();
    }

    // Find position of character in matrix
    static int[] findPosition(char c) {
        String target = (c == 'i' || c == 'j') ? "i/j" : String.valueOf(c);
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                if (matrix[i][j].equals(target)) return new int[]{i, j};
        return null;
    }
}
