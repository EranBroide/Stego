package stego.base;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import stego.ImageEncoder.ImageEncoder;
import stego.ImageToPassword.*;
import stego.UI.*;

public class Stego {
	
	
	private static  BufferedImage img;
	private static BufferedImage encodedImg;
	
	public static BufferedImage getImg() { return img; }
	public static BufferedImage getEncodedImg() { return encodedImg; }
	

	public static void main(String[] args) {

		// Launch UI
		System.out.println("Launching Stego UI...");
		StegoUI.launch();
	}
	
	
	// Controller method called by ImageToPasswodUI
    public static String generatePassword(String imagePath, int length) {
    	
    	System.out.println("Received request:");
        System.out.println("Image Path: " + imagePath);
        System.out.println("Password Length: " + length);
    	
        try {
        	img = loadImg(imagePath);
        	img = toPNG(img);
            String password = ImageToPassword.getPasswordFromImage(img, length);
            System.out.println("  Generated Password: " + password);
            return password;
        } catch (Exception e) {
            System.err.println("  Error generating password: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
    
    
    // Controller method called by ImageEncoderUI
    public static BufferedImage encodeImage(String imagePath, String message, String savePath) throws Exception {
    	
    	System.out.println("Received request:");
        System.out.println("Image Path: " + imagePath);
    	
        img = loadImg(imagePath);
        img = toPNG(img);
        encodedImg = ImageEncoder.getEncodedImage(img, message);
        System.out.println("Encoded image");
        
        saveImage(encodedImg, savePath);
        
        return encodedImg;
    }
    
    
    // Save image
    
    public static void saveImage(BufferedImage img, String savePath) throws Exception {
    	
        ImageIO.write(img, "png", new File(savePath));
        System.out.println("Saved Image");
    	
    }
    
    
	// Scale image for UI
	public static ImageIcon getScaledImg(BufferedImage img, int maxWidth, int maxHeight) {
		
	    ImageIcon icon = new ImageIcon(img);
	    int width = icon.getIconWidth();
	    int height = icon.getIconHeight();

	    double widthRatio = (double) maxWidth / width;
	    double heightRatio = (double) maxHeight / height;
	    double scale = Math.min(widthRatio, heightRatio);

	    int newWidth = (int) (width * scale);
	    int newHeight = (int) (height * scale);

	    Image scaled = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
	    return new ImageIcon(scaled);
	}
	
    
	
    // HELPER METHODS
    
	
	public static BufferedImage loadImg(String imagePath) throws IOException {
		
		img = ImageIO.read(new File(imagePath));
		
		if (img == null) {
			throw new IOException("Unsupported image format or file not found.");
		}
		
		return img;
	}
	
	
	private static BufferedImage toPNG(BufferedImage origional) throws IOException {
		
        // Force into a PNG-compatible format (ARGB ensures transparency support)
        img = new BufferedImage(
        		origional.getWidth(),
        		origional.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        img.getGraphics().drawImage(origional, 0, 0, null);

        return img;
    }
	
	
}
