package utilities;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class SHAHashGenerator {

	/**
	 * Generates a hash code to be served as an ID for articles in the JSON-LD file as well as 
	 * both the text and image files
	 * 
	 * @param input
	 * @return
	 */
	public static String generateHashCode(String input) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(input.getBytes(Charset.forName("UTF8")));
			return new String(Hex.encodeHex(md.digest()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}

}
