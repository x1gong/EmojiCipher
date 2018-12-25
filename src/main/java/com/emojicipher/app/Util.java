package com.emojicipher.app;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;


class Util {

    private static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * Use AES algorithm to encrypt a string to byte array
     * @param content the string to encode
     * @param key the key
     * @return the encoded byte array
     */
    static byte[] encrypt(String content, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(content.getBytes(CHARSET));
    }


    /**
     * Use AES algorithm to decrypt a byte array to string
     * @param contentArray the encoded byte array
     * @param key the key
     * @return the decoded string
     */
    static String decrypt(byte[] contentArray, SecretKey key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(contentArray);
        return new String(result, CHARSET);

    }


    /**
     * Convert a decimal number to n-base number. The output is represented as an ArrayList
     * @param num decimal number
     * @param n base
     * @return n-base number
     */
    static ArrayList<Integer> toBaseN(long num, int n) {
        ArrayList<Integer> baseN = new ArrayList<>();

        if (num == 0) {
            baseN.add(0);
            return baseN;
        }

        while (num != 0) {
            int r = (int)(num % n);
            num = num / n;
            baseN.add(r);
        }

        Collections.reverse(baseN);

        return baseN;
    }


    /**
     * Convert a n-base number to decimal
     * @param baseN n-base number represented in ArrayList
     * @param n base
     * @return decimal number
     */
    static long toDecimal(ArrayList<Integer> baseN, int n) {
        long decimal = 0;

        for (int bit : baseN) {
            decimal = decimal * n + bit;
        }

        return decimal;
    }

}

