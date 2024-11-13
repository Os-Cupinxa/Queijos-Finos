package com.queijos_finos.main.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtils {
    private static final Logger logger = LoggerFactory.getLogger(PasswordUtils.class);

    // Encripta a senha
    public static String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(password.getBytes());

            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Erro ao criptografar a senha: Algoritmo não encontrado", e);
            return null;
        }
    }

    // Verifica a senha
    public static boolean verifyPassword(String password, String hashedPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(password.getBytes());

            String hashedInputPassword = Base64.getEncoder().encodeToString(hashBytes);

            return hashedInputPassword.equals(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Erro ao verificar a senha: Algoritmo não encontrado", e);
            return false;
        }
    }
}
