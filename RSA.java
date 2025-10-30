import java.util.Scanner;

public class RSA {
    
    static boolean checkPrime(int num) {
        if (num < 2) return false;
        for (int i = 2; i * i <= num; i++)
            if (num % i == 0) return false;
        return true;
    }

    static int computeGCD(int a, int b) {
        return (b == 0) ? a : computeGCD(b, a % b);
    }

    static int findModInverse(int e, int phi) {
        for (int d = 1; d < phi; d++)
            if ((d * e) % phi == 1) return d;
        return -1;
    }

    static int modPower(int base, int exponent, int mod) {
        int result = 1;
        base = base % mod;
        while (exponent > 0) {
            if (exponent % 2 == 1)
                result = (result * base) % mod;
            exponent = exponent >> 1;
            base = (base * base) % mod;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter first prime number: ");
        int prime1 = sc.nextInt();
        if (!checkPrime(prime1)) {
            System.out.println("This is not a prime!");
            return;
        }

        System.out.print("Enter second prime number: ");
        int prime2 = sc.nextInt();
        if (!checkPrime(prime2)) {
            System.out.println("This is not a prime!");
            return;
        }

        System.out.println("\nStep 1: Calculate n");
        int n = prime1 * prime2;
        System.out.println("n = " + prime1 + " * " + prime2 + " = " + n);

        System.out.println("\nStep 2: Calculate phi(n)");
        int phi = (prime1 - 1) * (prime2 - 1);
        System.out.println("phi(n) = (" + (prime1 - 1) + ") * (" + (prime2 - 1) + ") = " + phi);

        System.out.println("\nStep 3: Select public key e");
        int publicKey = 0;
        for (int i = 2; i < phi; i++) {
            if (computeGCD(i, phi) == 1) {
                publicKey = i;
                break;
            }
        }
        System.out.println("Selected e = " + publicKey);

        System.out.println("\nStep 4: Compute private key d");
        int privateKey = findModInverse(publicKey, phi);
        System.out.println("Calculated d = " + privateKey);

        System.out.println("\nPublic Key (e, n) = (" + publicKey + ", " + n + ")");
        System.out.println("Private Key (d, n) = (" + privateKey + ", " + n + ")");

        System.out.print("\nEnter a number to encrypt (less than n): ");
        int message = sc.nextInt();
        if (message >= n) {
            System.out.println("Number must be less than n!");
            return;
        }

        System.out.println("\nStep 5: Encrypt the number");
        int cipher = modPower(message, publicKey, n);
        System.out.println("Ciphertext = " + message + "^" + publicKey + " mod " + n + " = " + cipher);

        System.out.println("\nStep 6: Decrypt the number");
        int decrypted = modPower(cipher, privateKey, n);
        System.out.println("Decrypted plaintext = " + cipher + "^" + privateKey + " mod " + n + " = " + decrypted);

        System.out.println("\nRSA Demonstration Complete");
	System.out.println("\nConclusion: RSA is a strong encryption algorithm as its security relies on the difficulty of factoring large prime numbers.");


        sc.close();
    }
}
