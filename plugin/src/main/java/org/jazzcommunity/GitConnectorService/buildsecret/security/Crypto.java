package org.jazzcommunity.GitConnectorService.buildsecret.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

public class Crypto {
    private final static String CHARSET = "UTF-8";
    private final static String STRING_ENCODING = "ISO-8859-1";
    private final static String MODE = "AES";
    private final static String PROVIDER = "IBMJCE";
    private final static String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    public static String encrypt(String plainText, String encryptionKey) throws GeneralSecurityException, UnsupportedEncodingException {
        SecureRandom random = new SecureRandom();
        byte iv[] = new byte[16];
        random.nextBytes(iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(CHARSET), MODE);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] byteCyperText = cipher.doFinal(plainText.getBytes(CHARSET));
        return (new String(iv, STRING_ENCODING) + new String(byteCyperText, STRING_ENCODING));
    }

    public static String decrypt(String cipherText, String encryptionKey) throws GeneralSecurityException, UnsupportedEncodingException {
        if (cipherText != null && cipherText.length() > 0) {
            byte[] combinedValue = cipherText.getBytes(STRING_ENCODING);
            byte[] iv = Arrays.copyOfRange(combinedValue, 0, 16);
            byte[] byteCyperText = Arrays.copyOfRange(combinedValue, 16, combinedValue.length);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(CHARSET), MODE);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return new String(cipher.doFinal(byteCyperText), CHARSET);
        } else {
            return "";
        }
    }
}
