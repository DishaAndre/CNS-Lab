import java.util.Scanner;

public class CRC {
    
   
    static int[] xorDivision(int[] dividend, int[] divisor) {
        int n = dividend.length;
        int m = divisor.length;
        int[] temp = dividend.clone();

        for (int i = 0; i <= n - m; i++) {
            if (temp[i] == 1) {
                for (int j = 0; j < m; j++) {
                    temp[i + j] ^= divisor[j];
                }
            }
        }
        return temp;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

       
        System.out.print("Enter number of data bits: ");
        int n = sc.nextInt();
        int[] data = new int[n];
        System.out.println("Enter data bits:");
        for (int i = 0; i < n; i++) data[i] = sc.nextInt();

        
        System.out.print("Enter number of divisor bits: ");
        int m = sc.nextInt();
        int[] divisor = new int[m];
        System.out.println("Enter divisor bits:");
        for (int i = 0; i < m; i++) divisor[i] = sc.nextInt();

        
        int[] dividend = new int[n + m - 1];
        for (int i = 0; i < n; i++) dividend[i] = data[i];

        int[] remainderArray = xorDivision(dividend, divisor);

       
        int[] remainder = new int[m - 1];
        for (int i = 0; i < m - 1; i++) {
            remainder[i] = remainderArray[n + i];
        }

       
        int[] codeword = new int[n + m - 1];
        for (int i = 0; i < n; i++) codeword[i] = data[i];
        for (int i = 0; i < m - 1; i++) codeword[n + i] = remainder[i];

        System.out.print("CRC Remainder: ");
        for (int bit : remainder) System.out.print(bit);
        System.out.println();

        System.out.print("Transmitted Codeword: ");
        for (int bit : codeword) System.out.print(bit);
        System.out.println();

   
        int[] checkRemainder = xorDivision(codeword, divisor);
        boolean errorFree = true;
        for (int i = n; i < n + m - 1; i++) {
            if (checkRemainder[i] != 0) {
                errorFree = false;
                break;
            }
        }

        if (errorFree) {
            System.out.println("Status: Error-Free ");
        } else {
            System.out.println("Status: Error Detected");
        }

        sc.close();
    }
}
