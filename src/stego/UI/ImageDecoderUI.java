package stego.UI;

import stego.ImageEncoder.ImageDecoder;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;

public class ImageDecoderUI {
	
	private static final int PANEL_H_GAP = 10;
    private static final int PANEL_V_GAP = 10;
    private static final int BORDER = 40;
    private static final int TEXTFIELD_WIDTH = 40;
    private static final int PANEL_HEIGHT = 100;

    public static void launch() {
    	
        JFrame frame = new JFrame("Decode Secret from Image");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 600));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));

        // Image selection panel
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        imagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JLabel imageLabel = new JLabel("Select Encoded Image:");
        JTextField imageField = new JTextField(TEXTFIELD_WIDTH);
        JButton browseButton = new JButton("Browse");

        imagePanel.add(imageLabel);
        imagePanel.add(imageField);
        imagePanel.add(browseButton);

        // Decode button panel
        JPanel decodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        decodePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JButton decodeButton = new JButton("Decode");
        decodePanel.add(decodeButton);

        // Message display panel
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JLabel messageLabel = new JLabel("Decoded Message:");
        JTextField messageField = new JTextField(TEXTFIELD_WIDTH);
        messageField.setEditable(false);
        JButton copyButton = new JButton("Copy");

        messagePanel.add(messageLabel);
        messagePanel.add(messageField);
        messagePanel.add(copyButton);

        // Back button panel
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, PANEL_H_GAP, PANEL_V_GAP));
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));
        JButton backButton = new JButton("Back");
        backPanel.add(backButton);

        // Browse button action
        browseButton.addActionListener(e -> {
        	
            FileDialog dialog = new FileDialog((Frame) null, "Select Encoded Image", FileDialog.LOAD);
            dialog.setVisible(true);
            String directory = dialog.getDirectory();
            String file = dialog.getFile();
            if (directory != null && file != null) {
                imageField.setText(directory + file);
            }
        });

        // Decode button action
        decodeButton.addActionListener(e -> {
        	
            String imagePath = imageField.getText();
            if (imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select an encoded image first.");
                return;
            }

            try {
                String decodedMessage = ImageDecoder.decodeImage(imagePath);
                messageField.setText(decodedMessage);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error decoding image: " + ex.getMessage());
            }
        });

        // Copy button action
        copyButton.addActionListener(e -> {
        	
            String text = messageField.getText();
            
            if (!text.isEmpty()) {
                StringSelection selection = new StringSelection(text);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                JOptionPane.showMessageDialog(frame, "Decoded message copied to clipboard!");
            }
        });

        // Back button action
        backButton.addActionListener(e -> {
            frame.dispose();
            StegoUI.launch();
        });

        // Add panels
        mainPanel.add(imagePanel);
        mainPanel.add(decodePanel);
        mainPanel.add(messagePanel);
        mainPanel.add(backPanel);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

}
