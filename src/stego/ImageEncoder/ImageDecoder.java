package stego.ImageEncoder;

import stego.base.Stego;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;

public class ImageDecoder {

    private static final String KEY = "Stego"; // must match encoder

    public static String decodeImage(String inputImagePath) throws Exception {
        BufferedImage image = Stego.loadImg(inputImagePath);
        HeaderInfo header = readHeader(image);

        if (header.messageLength <= 0 || header.messageLength > (image.getWidth() * image.getHeight() * 3 / 8)) {
            throw new IllegalArgumentException("This image does not appear to contain a valid hidden message.");
        }

        byte[] messageBytes = extractMessage(image, header);
        return new String(messageBytes, StandardCharsets.UTF_8);
    }

    // Extract message bytes starting at header.nextX/nextY/header.nextChannel
    private static byte[] extractMessage(BufferedImage image, HeaderInfo header) {
        int width = image.getWidth();
        int height = image.getHeight();

        long totalCapacityBits = (long) width * height * 3L;
        long neededBits = (long) header.messageLength * 8L;

        if (neededBits > totalCapacityBits - 32L) {
            throw new IllegalArgumentException("Image does not contain enough embedded data for length " + header.messageLength);
        }

        byte[] result = new byte[header.messageLength];
        int byteIndex = 0;
        int bitsCollected = 0;

        int x = header.nextX;
        int y = header.nextY;
        int channel = header.nextChannel; // 0 = R, 1 = G, 2 = B

        while (bitsCollected < neededBits) {
            if (y >= height) {
                throw new IllegalStateException("Ran out of pixels while reading message.");
            }

            int pixel = image.getRGB(x, y);
            int r = (pixel >> 16) & 0xFF;
            int g = (pixel >> 8) & 0xFF;
            int b = (pixel) & 0xFF;

            int[] channels = new int[] { r, g, b };

            // read channels starting at 'channel' for this pixel
            for (int c = channel; c < 3 && bitsCollected < neededBits; c++) {
                int bit = channels[c] & 1;

                result[byteIndex] = (byte) ((result[byteIndex] << 1) | bit); // shift left, OR new bit
                bitsCollected++;

                if (bitsCollected % 8 == 0) {
                    byteIndex++;
                }
            }

            // advance pixel
            channel = 0;
            x++;
            if (x == width) {
                x = 0;
                y++;
            }
        }

        return result;
    }

    // Container for header read results
    public static class HeaderInfo {
        public final int messageLength; // number of bytes encoded
        public final int nextX;
        public final int nextY;
        public final int nextChannel;

        public HeaderInfo(int messageLength, int nextX, int nextY, int nextChannel) {
            this.messageLength = messageLength;
            this.nextX = nextX;
            this.nextY = nextY;
            this.nextChannel = nextChannel;
        }
    }

    // Read KEY (5 bytes) + 32-bit length
    public static HeaderInfo readHeader(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // At least 40 + 32 bits needed
        if ((long) width * height * 3 < (KEY.length() * 8 + 32L)) {
            throw new IllegalArgumentException("Image too small to contain header.");
        }

        int bitsRead = 0;
        byte[] keyBytes = new byte[KEY.length()];
        int keyByteIndex = 0;

        int length = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                int[] channels = new int[] { r, g, b };

                for (int c = 0; c < 3; c++) {
                    int bit = channels[c] & 1;

                    if (bitsRead < KEY.length() * 8) {
                        // building key bytes
                        keyBytes[keyByteIndex] = (byte) ((keyBytes[keyByteIndex] << 1) | bit);
                        if ((bitsRead + 1) % 8 == 0) {
                            keyByteIndex++;
                        }
                    } else {
                        // reading length
                        length = (length << 1) | bit;
                    }

                    bitsRead++;

                    if (bitsRead == KEY.length() * 8 + 32) {
                        // validate key
                        String foundKey = new String(keyBytes, StandardCharsets.UTF_8);
                        if (!KEY.equals(foundKey)) {
                            throw new IllegalArgumentException("Image does not contain the expected key.");
                        }

                        // compute next pixel position
                        int nextChannel = c + 1;
                        int nextX = x;
                        int nextY = y;
                        if (nextChannel == 3) {
                            nextChannel = 0;
                            nextX++;
                            if (nextX == width) {
                                nextX = 0;
                                nextY++;
                            }
                        }

                        return new HeaderInfo(length, nextX, nextY, nextChannel);
                    }
                }
            }
        }

        throw new IllegalStateException("Failed to read header from image.");
    }
}
