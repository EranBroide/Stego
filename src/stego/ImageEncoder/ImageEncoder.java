package stego.ImageEncoder;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;

public class ImageEncoder {

    private static final String KEY = "Stego"; // 5 bytes marker

    // 1 bit per pixel per channel (LSB)
    public static BufferedImage getEncodedImage(BufferedImage image, String message) throws Exception {
    	
    	return encodeImage(image, message);

    }
    
    
    private static BufferedImage encodeImage (BufferedImage image, String message) {
    	
    	byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = KEY.getBytes(StandardCharsets.UTF_8);

        // Total bits = key (fixed 5 bytes = 40 bits) + length (32 bits) + message (messageBytes.length * 8)
        int totalLength = (keyBytes.length * 8) + 32 + (messageBytes.length * 8);

        int width = image.getWidth();
        int height = image.getHeight();

        if (totalLength > width * height * 3) {
            throw new IllegalArgumentException("Message too long for this image. Max bits: " + (width * height * 3));
        }

        embedMessage(image, keyBytes, messageBytes);

        return image;
    }
    
    

    private static void embedMessage(BufferedImage image, byte[] keyBytes, byte[] messageBytes) {

        int messageLength = messageBytes.length;

        int totalLength = (keyBytes.length * 8) + 32 + (messageLength * 8);
        int[] totalBits = new int[totalLength];

        int bitIndex = 0;

        // --- Step 1: write KEY ---
        for (byte b : keyBytes) {
            for (int bit = 7; bit >= 0; bit--) {
                totalBits[bitIndex++] = (b >> bit) & 1;
            }
        }

        // --- Step 2: write message length (32 bits MSB-first) ---
        for (int i = 0; i < 32; i++) {
            totalBits[bitIndex++] = (messageLength >> (31 - i)) & 1;
        }

        // --- Step 3: write actual message bytes ---
        for (byte b : messageBytes) {
            for (int bit = 7; bit >= 0; bit--) {
                totalBits[bitIndex++] = (b >> bit) & 1;
            }
        }

        int width = image.getWidth();
        int height = image.getHeight();

        bitIndex = 0;

        outer:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitIndex >= totalLength) break outer;

                int pixel = image.getRGB(x, y);

                int red   = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue  = pixel & 0xFF;

                if (bitIndex < totalLength) {
                    red = (red & 0xFE) | totalBits[bitIndex++];
                }
                if (bitIndex < totalLength) {
                    green = (green & 0xFE) | totalBits[bitIndex++];
                }
                if (bitIndex < totalLength) {
                    blue = (blue & 0xFE) | totalBits[bitIndex++];
                }

                int newRgb = (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, newRgb);
            }
        }
    }
}
