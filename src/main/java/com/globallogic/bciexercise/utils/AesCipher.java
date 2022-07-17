package com.globallogic.bciexercise.utils;

import com.globallogic.bciexercise.errors.ApiError;
import com.globallogic.bciexercise.errors.ApiErrorCodes;
import com.globallogic.bciexercise.errors.CypherException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@Service
public class AesCipher implements com.globallogic.bciexercise.utils.Cipher {
    private static final String algorithm = "AES/CBC/PKCS5Padding";

    @Value(value = "${aesCipher.salt}")
    private String salt;

    @Value(value = "${aesCipher.secret}")
    private String secret;

    public AesCipher(String salt, String secret) {
        this.salt = salt;
        this.secret = secret;
    }

    public AesCipher() {
    }

    public String encrypt(String input) {
        try {
            SecretKey secretKey = getSecretKey();
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            byte[] result = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(cipherText, 0 , result, iv.length, cipherText.length);
                return Base64.getEncoder()
                        .encodeToString(result);
        } catch (Exception e) {
            ApiError error = new ApiError(ApiErrorCodes.ENCRYPTION_ERROR.getCode(), "error while encrypting: " + e.getMessage());
            throw new CypherException(error);
        }
    }

    public String decrypt(String input) {
        try {
            byte[] decode = Base64.getDecoder().decode(input);
            byte[] text = Arrays.copyOfRange(decode, 16, decode.length);
            String cypherText = Base64.getEncoder()
                    .encodeToString(text);
            byte[] iv = Arrays.copyOfRange(decode, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKey secretKey = getSecretKey();

            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(cypherText));
            return new String(plainText);
        } catch (Exception e) {
            ApiError error = new ApiError(ApiErrorCodes.DECRYPTION_ERROR.getCode(), "error while decrypting: " + e.getMessage());
            throw new CypherException(error);
        }
    }

    private SecretKey getSecretKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
    }
}
