package com.steamext.steam.api.logic.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Objects;

@Slf4j
public final class SteamPasswordRSAEncode {
    public static String encodePassword(String publicKey, String publicExp, String password) {
        RSAPublicKeySpec spec = new RSAPublicKeySpec(
                new BigInteger(publicKey, 16),
                new BigInteger(publicExp, 16));

        try {
            return base64encode(
                    rsaEncrypt(
                            password,
                            (RSAPublicKey) getPublicKey(getRsaKeyFactory(), spec)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String rsaEncrypt(String password, RSAPublicKey publicKey) {
        BigInteger pw = rsaEncryptInternal(password, (publicKey.getModulus().bitLength() + 7) >> 3);
        pw = pw.modPow(publicKey.getPublicExponent(), publicKey.getModulus());
        String encrypted = pw.toString(16);
        if ((encrypted.length() & 1) == 1) {
            encrypted = "0" + encrypted;
        }
        return toHexString(encrypted);
    }

    private static String toHexString(String toEncode) {
        String hexString = "0123456789abcdef";
        String output = "";
        int i = 0;
        do {
            char a = toEncode.charAt(i++);
            char b = toEncode.charAt(i++);
            output += (char) (((hexString.indexOf(a) << 4) & 0xf0) + (hexString.indexOf(b) & 0xf));
        } while (i < toEncode.length());
        return output;
    }

    private static BigInteger rsaEncryptInternal(String password, int keySize) {
        int i = password.length() - 1;
        byte[] buffer = new byte[keySize];
        while (i >= 0 && keySize > 0) {
            buffer[--keySize] = (byte) password.charAt(i--);
        }
        buffer[--keySize] = 0;
        while (keySize > 2) {
            buffer[--keySize] = (byte) (Math.floor(Math.random() * 254) + 1);
        }
        buffer[--keySize] = 2;
        buffer[--keySize] = 0;
        return new BigInteger(buffer);
    }

    private static KeyFactory getRsaKeyFactory() throws NoSuchAlgorithmException {
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            String errorMessage = "Getting RSA key factory failed";
            log.error(errorMessage, e);
            throw new NoSuchAlgorithmException(errorMessage, e);
        }
        return keyFactory;
    }

    private static PublicKey getPublicKey(KeyFactory factory, RSAPublicKeySpec spec)
            throws InvalidKeySpecException {
        PublicKey pub;
        try {
            pub = factory.generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            String errorMessage = "Generating RSA public key failed";
            log.error(errorMessage, e);
            throw new InvalidKeySpecException(errorMessage, e);
        }
        return pub;
    }

    private static String base64encode(String input) {
        Objects.requireNonNull(input);
        final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        String output = "";
        int i = 0;
        do {
            boolean isChr1NaN = false;
            boolean isChr2NaN = false;
            boolean isChr3NaN = false;

            int chr1 = 0;
            try {
                chr1 = input.charAt(i++);
            } catch (IndexOutOfBoundsException ignored) {
                isChr1NaN = true;
            }
            int chr2 = 0;
            try {
                chr2 = input.charAt(i++);
            } catch (IndexOutOfBoundsException ignored) {
                isChr2NaN = true;
            }
            int chr3 = 0;
            try {
                chr3 = input.charAt(i++);
            } catch (IndexOutOfBoundsException ignored) {
                isChr3NaN = true;
            }
            int enc1 = isChr1NaN ? 0 : (chr1 >> 2);
            int enc2_a = isChr1NaN ? 0 : (chr1 & 3) << 4;
            int enc2_b = isChr2NaN ? 0 : (chr2 >> 4);
            int enc2 = enc2_a | enc2_b;
            int enc3_a = isChr2NaN ? 0 : ((chr2 & 15) << 2);
            int enc3_b = isChr3NaN ? 0 : ((chr3 >> 6));
            int enc3 = enc3_a | enc3_b;
            int enc4 = isChr2NaN ? 0 : chr3 & 63;
            if (isChr2NaN) {
                enc3 = enc4 = 64;
            } else if (isChr3NaN) {
                enc4 = 64;
            }
            String part1 = enc1 < BASE64.length() ? BASE64.charAt(enc1) + "" : "";
            String part2 = enc2 < BASE64.length() ? BASE64.charAt(enc2) + "" : "";
            String part3 = enc3 < BASE64.length() ? BASE64.charAt(enc3) + "" : "";
            String part4 = enc4 < BASE64.length() ? BASE64.charAt(enc4) + "" : "";
            output += part1 + part2 + part3 + part4;
        } while (i < input.length());
        return output;
    }
}
