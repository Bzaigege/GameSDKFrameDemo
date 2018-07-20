package com.bzai.gamesdk.common.utils_base.utils;

import java.security.Key;

import javax.crypto.Cipher;


/**
 * Utility for encrypting and decrypting Strings.
 *
 * @author David
 */
public class CryptUtils {

    protected static String DEFAULT_KEY = "alwaysbe";

    private Cipher encryptCipher = null;

    private Cipher decryptCipher = null;

    private static final char[] sHexCharactors = new char[] { '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * Convert byte array to string, byte[]{8, 16} will be converted to 0811.
     *
     * @param array
     * @return
     */
    public static String toHexString(byte[] array) {
        if (null == array) {
            return null;
        }
        int length = array.length;
        StringBuilder sb = new StringBuilder(length << 1);
        for (int i = 0; i < length; i++) {
            int b = array[i];

            int low = b & 0xf;
            b = b >> 4;
            int high = b & 0xf;

            sb.append(sHexCharactors[high]);
            sb.append(sHexCharactors[low]);
        }

        return sb.toString();
    }

    /**
     * Convert string to byte array.
     *
     * @param s
     * @return
     * @see #toHexString(byte[])
     */
    public static byte[] toByteArray(String s) {
        if (null == s) {
            return null;
        }
        String sub = s;
        int len = sub.length();

        byte[] array = new byte[len >> 1];
        for (int i = 0, j = 0; i < len; i += 2, j++) {
            char highChar = sub.charAt(i);
            char lowChar = sub.charAt(i + 1);

            String str = new String(new char[] { highChar, lowChar });

            int value = Integer.parseInt(str, 16);
            array[j] = (byte) value;
        }
        return array;
    }

    /**
     * Construct an instance using the default encryption and decryption key.
     */
    public CryptUtils() {
        this(DEFAULT_KEY);
    }

    /**
     *
     * @param keyStr
     *            the key for encryption and decryption.
     */
    public CryptUtils(String keyStr) {
        String keyString = keyStr;
        if (null == keyString) {
            keyString = DEFAULT_KEY;
        }
        Key key = null;
        try {
            key = getKey(keyString.getBytes());
            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
        }

    }

    /**
     * Encrypt a byte array.
     *
     * @param arrB
     * @return null if any exception is thrown.
     */
    public byte[] encrypt(byte[] arrB) {
        try {
            return encryptCipher.doFinal(arrB);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Encypt a string.
     *
     * @param strIn
     * @return
     */
    public String encrypt(String strIn) {
        if (null == strIn) {
            return null;
        }
        byte[] data = strIn.getBytes();
        byte[] encryptData = encrypt(data);
        return toHexString(encryptData);
    }

    /**
     * Decrypt a byte array.
     *
     * @param arrB
     * @return null if any exception is thrown.
     */
    public byte[] decrypt(byte[] arrB) {
        try {
            return decryptCipher.doFinal(arrB);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Decrypt the string.
     *
     * @param strIn
     * @return null if the string passed in is null or exception is thrown.
     */
    public String decrypt(String strIn) {
        byte[] data = toByteArray(strIn);
        if (null == data) {
            return null;
        }
        byte[] decryptData = decrypt(data);
        if (null == decryptData) {
            return null;
        }

        return new String(decryptData);
    }

    private Key getKey(byte[] data) throws Exception {
        byte[] keyArray = new byte[8];

        for (int i = 0; i < data.length && i < keyArray.length; i++) {
            keyArray[i] = data[i];
        }
        Key key = new javax.crypto.spec.SecretKeySpec(keyArray, "DES");
        return key;
    }
}
