package stego.UI;

import stego.base.Stego;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ImageToPasswordUI {
	
    private static final int PANEL_H_GAP = 10;
    private static final int PANEL_V_GAP = 10;
    private static final int BORDER = 40;
    private static final int TEXTFIELD_WIDTH = 40;
    private static final int LENGTHFIELD_WIDTH = 5;
    private static final int PANEL_HEIGHT = 100;

    public static void launch() {
        JFrame frame = new JFrame("Image to Password Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setUndecorated(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
        
        

        // Image selection panel
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        imagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JLabel imageLabel = new JLabel("Select Image:");
        JTextField imageField = new JTextField(TEXTFIELD_WIDTH);
        JButton browseButton = new JButton("Browse");

        imagePanel.add(imageLabel);
        imagePanel.add(imageField);
        imagePanel.add(browseButton);
        
        // Password length panel
        JPanel lengthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        JLabel lengthLabel = new JLabel("Password Length (1-33):");
        lengthPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JTextField lengthField = new JTextField("12", LENGTHFIELD_WIDTH);
        JButton generateButton = new JButton("Generate");

        lengthPanel.add(lengthLabel);
        lengthPanel.add(lengthField);
        lengthPanel.add(generateButton);

        // Password display panel
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JLabel passwordLabel = new JLabel("Generated Password:");
        JTextField passwordField = new JTextField(TEXTFIELD_WIDTH);
        passwordField.setEditable(false);
        JButton copyButton = new JButton("Copy");


        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        passwordPanel.add(copyButton);
        
        // Back button panel
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JButton backButton = new JButton("Back");

        backPanel.add(backButton);


        // Browse button
        browseButton.addActionListener(e -> {
            FileDialog dialog = new FileDialog((Frame) null, "Select Image", FileDialog.LOAD);
            dialog.setVisible(true);
            String directory = dialog.getDirectory();
            String file = dialog.getFile();
            if (directory != null && file != null) {
                imageField.setText(directory + file);
            }
        });

        // Generate button
        generateButton.addActionListener(e -> {
            String imagePath = imageField.getText();
            int length;
            try {
                length = Integer.parseInt(lengthField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a valid number for password length.");
                return;
            }
            
            // Enforce 1–32 limit
            if (length < 1 || length > 32) {
                JOptionPane.showMessageDialog(frame, "Password length must be between 1 and 32.");
                return;
            }

            try {
                String password = Stego.generatePassword(imagePath, length);
                passwordField.setText(password);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });
        
        // Copy button action listener
        copyButton.addActionListener(e -> {
            String password = passwordField.getText();
            if (!password.isEmpty()) {
                StringSelection selection = new StringSelection(password);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                JOptionPane.showMessageDialog(frame, "Password copied to clipboard!");
            }
        });
        
        // Back button
        backButton.addActionListener(e -> {
            frame.dispose(); // close current UI
            StegoUI.launch(); // reopen main page
        });

        
        mainPanel.add(imagePanel);
        mainPanel.add(lengthPanel);
        mainPanel.add(passwordPanel);
        mainPanel.add(backPanel);

        // Use scroll pane for full-screen flexibility
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        frame.add(scrollPane);
        
        
        frame.setVisible(true);
    }
}