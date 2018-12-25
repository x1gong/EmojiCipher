package com.emojicipher.app;

import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;
import com.google.gson.*;

public class Emoji {

    private static final String DEFAULT_DICT_PATH = "dict.json";
    private static final int BYTES_SIZE = 8;

    private int N;

    private String[] separators;
    private String sepRegx;

    private String[] negSymbols;
    private HashSet<String> negSymbolSet;

    private String[] emojiDict;            // map from int to emoji
    private HashMap<String, Integer> intMap;        // map from emoji to int

    public Emoji() {
        this(DEFAULT_DICT_PATH);
    }

    public Emoji(String dictPath) {
        try {
            Gson gson = new Gson();
            EmojiDict dict = gson.fromJson(new FileReader(dictPath), EmojiDict.class);

            // read the dictionary
            emojiDict = dict.cipherTexts;
            intMap = new HashMap<>();
            N = emojiDict.length;
            for (int i = 0; i < N; i++) {
                intMap.put(emojiDict[i], i);
            }

            separators = dict.separators;

            negSymbolSet = new HashSet<>();
            negSymbols = dict.negSymbols;
            Collections.addAll(negSymbolSet, negSymbols);

            // generate sepRegx
            StringBuilder regBuilder = new StringBuilder(separators[0]);
            for (int i = 1; i < separators.length; i++) {
                regBuilder.append("|").append(separators[i]);
            }
            sepRegx = regBuilder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Encrypt a message to emoji strings with a random key
     * @param message the text to be encoded
     * @return emoji cipher string
     */
    public String encryptToEmoji(String message) throws Exception {
        StringBuilder emoji = new StringBuilder();
        int key1 = (int)(Math.random() * N);
        int key2 = (int)(Math.random() * N);
        long keyVal = (long)key1 * N + key2;

        byte[] keyBytes = ByteBuffer.allocate(2 * BYTES_SIZE).putLong(keyVal).array();
        assert(keyBytes.length == 2 * BYTES_SIZE);
        byte[] bytes;

        emoji.append(emojiDict[key1]);
        emoji.append(emojiDict[key2]);

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        byte[] cipher = Util.encrypt(message, key);

        int i = 0;
        while (i < cipher.length) {
            bytes = new byte[BYTES_SIZE];
            System.arraycopy(cipher, i, bytes, 0, BYTES_SIZE);
            if (i > 0) {
                emoji.append(getRandomSeparator());
            }
            emoji.append(bytesToEmoji(bytes));
            i += BYTES_SIZE;
        }

        return emoji.toString();
    }


    /**
     * Decrypt emoji ciphertext to readable text
     * @param emoji emoji ciphertext
     * @return readable text string
     */
    public String decryptEmoji(String emoji) throws Exception {

        // extract the key from the first two emojis
        String key1 = emoji.substring(0, 2);
        String key2 = emoji.substring(2, 4);
        long keyVal = intMap.get(key1) * N + intMap.get(key2);
        byte[] keyBytes = ByteBuffer.allocate(2 * BYTES_SIZE).putLong(keyVal).array();
        SecretKeySpec  key = new SecretKeySpec(keyBytes, "AES");

        // translate emojis to binary array
        emoji = emoji.substring(4);
        String[] tokens = emoji.split(sepRegx);
        byte[] cipher = new byte[tokens.length * BYTES_SIZE];
        byte[] bytes;

        for (int i = 0; i < tokens.length; i++) {
            bytes = emojiToBytes(tokens[i]);
            System.arraycopy(bytes, 0, cipher, i * BYTES_SIZE, BYTES_SIZE);
        }

        return Util.decrypt(cipher, key);
    }


    /**
     * Select a random separator from the separators list
     * @return random separator
     */
    private String getRandomSeparator() {
        int n = separators.length;
        int i = (int)(Math.random() * n);
        return separators[i];
    }


    /**
     * Select a random negative symbol
     * @return random negative symbol
     */
    private String getRandomNegSymbol() {
        int n = negSymbols.length;
        int i = (int)(Math.random() * n);
        return negSymbols[i];
    }


    /**
     * Convert a byte array to emoji string
     * @param bytes byte array
     * @return emoji string
     */
    private String bytesToEmoji(byte[] bytes) {
        assert(bytes.length <= BYTES_SIZE);
        StringBuilder sb = new StringBuilder();
        long longVal = ByteBuffer.wrap(bytes).getLong();

        if (longVal < 0) {
            sb.append(getRandomNegSymbol());
            longVal = -longVal;
        }

        ArrayList<Integer> nBaseNum = Util.toBaseN(longVal, N);

        for (int i : nBaseNum) {
            sb.append(emojiDict[i]);
        }

        return sb.toString();
    }


    /**
     * Convert a emoji string to a byte array
     * @param emojiStr emoji string
     * @return byte array
     */
    private byte[] emojiToBytes(String emojiStr) {

        String[] emojis = new String[emojiStr.length() / 2];
        for (int i = 0; i < emojis.length; i++) {
            emojis[i] = emojiStr.charAt(2 * i) + "" + emojiStr.charAt(2 * i + 1);
        }

        int start;
        if (negSymbolSet.contains(emojis[0])) {
            start = 1;
        } else {
            start = 0;
        }

        ArrayList<Integer> nBaseNum = new ArrayList<>();

        for (int i = start; i < emojis.length; i++) {
            nBaseNum.add(intMap.get(emojis[i]));
        }

        long val = Util.toDecimal(nBaseNum, N);
        if (start == 1) {
            val = -val;
        }

        return ByteBuffer.allocate(BYTES_SIZE).putLong(val).array();
    }

}
