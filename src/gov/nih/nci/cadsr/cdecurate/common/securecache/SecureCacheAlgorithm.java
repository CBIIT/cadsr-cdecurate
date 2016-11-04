/*L
 * Copyright Leidos
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://ncip.github.com/cadsr-cdecurate/LICENSE.txt for details.
 */
package gov.nih.nci.cadsr.cdecurate.common.securecache;

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
    public static final int CACHE_ENCODED_LENGTH = 256;
    public static final int SALT_ENCODED_LENGTH = 8;
	private static final int NUM_ITERATIONS = 2000;
	private static final int SALT_LENGTH = 6;
	private static final String HASH_ALG = "PBKDF2WithHmacSHA256";

	/**
	 * 
	 * @return encoded Salt string ready to store in DB
	 */
	public static String generateSalt() {
		byte[] bytes = null;
		SecureRandom random = new SecureRandom();
		bytes = new byte[SALT_LENGTH];
		random.nextBytes(bytes);
		return encodeToBase64(bytes);
	}
	/**
	 * 
	 * @param bytes
	 * @return String ready encoded to store salt in DB
	 */
	public static String encodeToBase64(final byte[] bytes) {
		Base64.Encoder encoder = Base64.getEncoder();
		return encoder.withoutPadding().encodeToString(bytes);
	}
	/**
	 * 
	 * @param base64String
	 * @return byte[]
	 */
	public static byte[] decodeFromBase64(final String base64String) {
		Base64.Decoder decoder = Base64.getDecoder();
		return decoder.decode(base64String);
	}
	private static final int DERIVED_KEY_LENGTH = (256*8*3)/4;//in bits, we want 256 bytes cache length in the response
	/**
	 * 
	 * @param pwd raw string received from a user
	 * @param encodedSalt as stored in DB in encoded form
	 * @return String ready to store in DB
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static String cacheCalculate(final String pwd, final String encodedSalt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] saltBytes = decodeFromBase64(encodedSalt);
		KeySpec spec = new PBEKeySpec(pwd.toCharArray(), saltBytes, NUM_ITERATIONS, DERIVED_KEY_LENGTH);
		SecretKeyFactory secretFactory = null;
		byte[] hashedPwd = null;
		try {
			secretFactory = SecretKeyFactory.getInstance(HASH_ALG);
		}
		catch(NoSuchAlgorithmException e) {
			logger.error("cacheCalculate Error in credential cache implementation: " + HASH_ALG, e);
			throw e;	
		}
		
		try {
			hashedPwd = secretFactory.generateSecret(spec).getEncoded();
		}
		catch(InvalidKeySpecException e) {
			logger.error("cacheCalculate Error in credential cache implementation secretFactory.generateSecret", e);
			throw e;	
		}
		
		return encodeToBase64(hashedPwd);		
	}
	public static CacheHashData cacheGenerateNew(final String rawCredential) throws InvalidKeySpecException, NoSuchAlgorithmException {
		CacheHashData cacheHashData = new CacheHashData();
		
		String encodedSalt = generateSalt();
		cacheHashData.setSalt(encodedSalt);
		try {
			cacheHashData.setCredential(cacheCalculate(rawCredential, encodedSalt));
		}
		catch(NoSuchAlgorithmException e) {
			logger.error("cacheGenerateNew Error in credential cache generation: " + HASH_ALG, e);
			throw e;	
		}
		

		return cacheHashData;
	}

}