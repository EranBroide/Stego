package stego.UI;

import javax.swing.*;
import java.awt.*;

public class StegoUI {

    public static void launch() {
    	
        JFrame frame = new JFrame("Stego");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 600));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Stego: Hidden in plain sight");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel heading = new JLabel("Conceal and generate data through any image");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton passwordButton = new JButton("Generate a password from any image");
        passwordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton encodeButton = new JButton("Encode a message in any image");
        encodeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton decodeButton = new JButton("Decode a message from your image");
        decodeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(heading);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(passwordButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(encodeButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(decodeButton);

        frame.add(mainPanel);
        frame.setVisible(true);

        // Action listeners
        passwordButton.addActionListener(e -> ImageToPasswordUI.launch());
        encodeButton.addActionListener(e -> ImageEncoderUI.launch());
        decodeButton.addActionListener(e -> ImageDecoderUI.launch());
    }
}
