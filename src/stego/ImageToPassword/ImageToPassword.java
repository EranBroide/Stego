package stego.ImageToPassword;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ImageToPassword {
	
	/* Idea
	 * Load a bitmap (.png, .bmp, .jpg) as a BufferedImage.
	 * Extract pixel data (RGB values).
	 * Convert it into a hash → deterministic password.
	 * Output could be a hex string or Base64 string.
	 */
	
	public static String getPasswordFromImage(BufferedImage image, int length) throws IOException, NoSuchAlgorithmException {
        

        // Collect pixel data
        StringBuilder pixelData = new StringBuilder();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                pixelData.append(pixel);
            }
        }
        

        // SHA-256 hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(pixelData.toString().getBytes());

        // Character sets for guaranteed inclusion
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String symbols = "!@#$%^&*()-_=+[]{};:,.<>?/";

        boolean hasLetter = false;
        boolean hasSymbol = false;
        
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int val = Byte.toUnsignedInt(hash[i]) % 94; // 0-93
            char c = (char) (33 + val); // printable ASCII 33-126

            password.append(c);
        }
        
        return password.toString();
    }

}
