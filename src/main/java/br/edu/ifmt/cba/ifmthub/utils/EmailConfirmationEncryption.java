package br.edu.ifmt.cba.ifmthub.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EmailConfirmationEncryption {
	private static SecretKey fixedSecretKey = new SecretKeySpec("chaveSecretaFixa".getBytes(StandardCharsets.UTF_8),
			"AES");

	public static String encryptString(String email) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, fixedSecretKey);
		byte[] encryptedEmailBytes = cipher.doFinal(email.getBytes(StandardCharsets.UTF_8));
		return Base64.getUrlEncoder().encodeToString(encryptedEmailBytes);
	}

	public static String decryptString(String encryptedEmail) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, fixedSecretKey);
			byte[] encryptedEmailBytes = Base64.getUrlDecoder().decode(encryptedEmail.getBytes(StandardCharsets.UTF_8));
			byte[] decryptedEmailBytes = cipher.doFinal(encryptedEmailBytes);
			return new String(decryptedEmailBytes, StandardCharsets.UTF_8);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	public static byte[] encrypt(String email) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, fixedSecretKey);
		byte[] encryptedEmailBytes = cipher.doFinal(email.getBytes(StandardCharsets.UTF_8));
		return encryptedEmailBytes;
	}

	public static String decrypt(byte[] encryptedEmail) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, fixedSecretKey);
		byte[] decryptedEmailBytes = cipher.doFinal(encryptedEmail);
		return new String(decryptedEmailBytes, StandardCharsets.UTF_8);
	}
}
