/*L
 * Copyright Leidos
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */
package gov.nih.nci.cadsr.cdecurate.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.log4j.Logger;
/**
 * 
 * @author asafievan
 *
 */
public class SecureCacheAlgorithm {
	private static final Logger logger = Logger.getLogger(SecureCacheAlgorithm.class);

	private static final int NUM_ITERATIONS = 2000;
	private static final String HASH_ALG = "PBKDF2WithHmacSHA256";
	//FIXME generate a separate salt for each password
	private static final byte[] SALT_BYTES =  {'C', 'u', 'R', 'a', 't', 'i', 'o', 'N'};

	public static byte[] generateSalt(int length) {
		byte[] bytes = null;
		SecureRandom random = new SecureRandom();
		bytes = new byte[length];
		random.nextBytes(bytes);
		return bytes;
	}
	/**
	 * 
	 * @param bytes
	 * @return String
	 */
	public static String encodeToBase64(byte[] bytes) {
		Base64.Encoder encoder = Base64.getEncoder();
		return encoder.withoutPadding().encodeToString(bytes);
	}
	/**
	 * 
	 * @param base64String
	 * @return byte[]
	 */
	public static byte[] decodeFromBase64(String base64String) {
		Base64.Decoder decoder = Base64.getDecoder();
		return decoder.decode(base64String);
	}
	private static final int DERIVED_KEY_LENGTH = (256*8*3)/4;//in bits, we want 256 bytes cache length in the response

	public static String cachePrepare(String pwd) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeySpec spec = new PBEKeySpec(pwd.toCharArray(), SALT_BYTES, NUM_ITERATIONS, DERIVED_KEY_LENGTH);
		SecretKeyFactory secretFactory = null;
		byte[] hashedPwd = null;
		try {
			secretFactory = SecretKeyFactory.getInstance(HASH_ALG);
		}
		catch(NoSuchAlgorithmException e) {
			logger.error("Error in credential cache implementation: " + HASH_ALG, e);
			throw new NoSuchAlgorithmException(e);	
		}
		
		try {
			hashedPwd = secretFactory.generateSecret(spec).getEncoded();
		}
		catch(InvalidKeySpecException e) {
			logger.error("Error in credential cache implementation secretFactory.generateSecret", e);
			throw e;	
		}
		return encodeToBase64(hashedPwd);
		
	}

}