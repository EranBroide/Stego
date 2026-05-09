package stego.UI;

import stego.base.Stego;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageEncoderUI {

    private static final int PANEL_H_GAP = 10;
    private static final int PANEL_V_GAP = 10;
    private static final int BORDER = 40;
    private static final int TEXTFIELD_WIDTH = 40;
    private static final int PANEL_HEIGHT = 100;

    public static void launch() {
        JFrame frame = new JFrame("Encode Secret in Image");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 600));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));

        
        // Input image panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JLabel inputLabel = new JLabel("Input Image:");
        JTextField inputField = new JTextField(TEXTFIELD_WIDTH);
        JButton browseBtn1 = new JButton("Browse");
        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(browseBtn1);

        
        // Message input panel
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JLabel textLabel = new JLabel("Message:");
        JTextField messageField = new JTextField(TEXTFIELD_WIDTH);
        JScrollPane textScroll = new JScrollPane(messageField);
        JButton encodeButton = new JButton("Encode");
        messagePanel.add(textLabel);
        messagePanel.add(messageField);
        messagePanel.add(textScroll);
        messagePanel.add(encodeButton);
        

        // Output image panel
        JPanel outputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        outputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JLabel outputLabel = new JLabel("Output Image:");
        JTextField outputField = new JTextField(TEXTFIELD_WIDTH);
        JButton saveButton = new JButton("Save As");
        outputPanel.add(outputLabel);
        outputPanel.add(outputField);
        outputPanel.add(saveButton);
        
        
        // Preview panel
        JPanel previewPanel = new JPanel(new GridLayout(1, 2, PANEL_H_GAP, PANEL_V_GAP));
        previewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        
        JPanel originalImagePanel = new JPanel(new BorderLayout());
        JLabel originalImageLabel = new JLabel("Original Image", SwingConstants.CENTER);
        JLabel originalImageDisplay = new JLabel();
        originalImageDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        originalImageDisplay.setVerticalAlignment(SwingConstants.CENTER);
        originalImageDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        originalImagePanel.add(originalImageLabel, BorderLayout.NORTH);
        originalImagePanel.add(originalImageDisplay, BorderLayout.CENTER);
        
        JPanel encodedImagePanel = new JPanel(new BorderLayout());
        JLabel encodedImageLabel = new JLabel("Encoded Image", SwingConstants.CENTER);
        JLabel encodedImageDisplay = new JLabel();
        encodedImageDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        encodedImageDisplay.setVerticalAlignment(SwingConstants.CENTER);
        encodedImageDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        encodedImagePanel.add(encodedImageLabel, BorderLayout.NORTH);
        encodedImagePanel.add(encodedImageDisplay, BorderLayout.CENTER);
        
        previewPanel.add(originalImagePanel);
        previewPanel.add(encodedImagePanel);
        
        
        // Browse button
        browseBtn1.addActionListener(e -> {
        	
            FileDialog dialog = new FileDialog((Frame) null, "Select Image", FileDialog.LOAD);
            dialog.setVisible(true);
           
            if (dialog.getFile() != null) {
            	
            	String imagePath = dialog.getDirectory() + dialog.getFile();
                inputField.setText(imagePath);
                
				try {
					BufferedImage img = Stego.loadImg(imagePath);
	                originalImageDisplay.setIcon(Stego.getScaledImg(img, 350, 350));
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
				}
            }
        });
        
        
        // Save button
        saveButton.addActionListener(e -> {
        	
            FileDialog dialog = new FileDialog((Frame) null, "Save Image As", FileDialog.SAVE);
            dialog.setFile("encoded image.png");
            dialog.setVisible(true);

            String directory = dialog.getDirectory();
            String file = dialog.getFile();
            if (directory != null && file != null) {
                outputField.setText(directory + file);
            }
        });
        

        // Encode button
        encodeButton.addActionListener(e -> {
        	
            String inputPath = inputField.getText();
            String message = messageField.getText();

            if (inputPath.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select and image.");
                return;
            }
            
            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your message.");
                return;
            }

            try {
            	
            	String savePath = outputField.getText();
                BufferedImage encodedImg = Stego.encodeImage(inputPath, message, savePath);
                JOptionPane.showMessageDialog(frame, "Message encoded successfully!");
                
                encodedImageDisplay.setIcon(Stego.getScaledImg(encodedImg, 350, 350));
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });
        

        // Back button panel
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JButton backButton = new JButton("Back");
        backPanel.add(backButton);

        backButton.addActionListener(e -> {
            frame.dispose(); // close current UI
            StegoUI.launch(); // reopen main page
        });

        
        // Add panels to main panel
        mainPanel.add(inputPanel);
        mainPanel.add(messagePanel);
        mainPanel.add(outputPanel);
        mainPanel.add(encodeButton);
        mainPanel.add(backPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // spacing
        mainPanel.add(previewPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        frame.add(scrollPane);

        frame.setVisible(true);
    }
}
