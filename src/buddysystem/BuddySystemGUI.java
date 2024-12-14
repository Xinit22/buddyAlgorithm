/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buddysystem;

/**
 *
 * @author hiruna
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class BuddySystemGUI extends JFrame {
    
    private JTextField txtMemorySize, txtFileSize;
    
    private JComboBox<String> cmbFiles;
    
    private JTextArea txtLog;
    
    private JPanel pnlMemoryVisualization;

    private BuddySystem buddySystem;

    public BuddySystemGUI() {
        
        setTitle("Buddy System Memory Allocation Simulator");
        
        setSize(800, 600);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setLayout(new BorderLayout());

        // Initialize Components
        initializeComponents();
    }

    private void initializeComponents() {
        
        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        
        inputPanel.setBorder(BorderFactory.createTitledBorder("Controls"));

        inputPanel.add(new JLabel("Memory Size (KB):"));
        
        txtMemorySize = new JTextField();
        
        inputPanel.add(txtMemorySize);
        
        JButton btnSetMemory = new JButton("Set Memory");
        
        inputPanel.add(btnSetMemory);

        inputPanel.add(new JLabel("File Size (KB):"));
        
        txtFileSize = new JTextField();
        
        inputPanel.add(txtFileSize);
        
        JButton btnAllocateFile = new JButton("Allocate File");
        
        inputPanel.add(btnAllocateFile);

        inputPanel.add(new JLabel("Deallocate File:"));
        
        cmbFiles = new JComboBox<>();
        
        inputPanel.add(cmbFiles);
        
        JButton btnDeallocateFile = new JButton("Deallocate");
        
        inputPanel.add(btnDeallocateFile);

        add(inputPanel, BorderLayout.NORTH);
        
        

        // Memory Visualization Panel
        pnlMemoryVisualization = new JPanel() {
            
            @Override
            
            protected void paintComponent(Graphics g) {
                
                super.paintComponent(g);
                renderMemoryBlocks(g);
            }
            
        };
        
        
        pnlMemoryVisualization.setPreferredSize(new Dimension(800, 300));
        
        pnlMemoryVisualization.setBorder(BorderFactory.createTitledBorder("Memory Blocks Visualization"));
        
        add(pnlMemoryVisualization, BorderLayout.CENTER);

        // Log Panel
        txtLog = new JTextArea();
        
        txtLog.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(txtLog);
        
        scrollPane.setBorder(BorderFactory.createTitledBorder("Logs"));
        
        add(scrollPane, BorderLayout.SOUTH);

        // Event Listeners
        btnSetMemory.addActionListener(this::setMemory);
        
        btnAllocateFile.addActionListener(this::allocateFile);
        
        btnDeallocateFile.addActionListener(this::deallocateFile);
        
    }

    private void setMemory(ActionEvent e) {
        try {
            
            int size = Integer.parseInt(txtMemorySize.getText());
            
            if (size <= 0) throw new NumberFormatException();

            buddySystem = new BuddySystem(size);
            
            txtLog.append("Memory initialized with " + size + " KB.\n");
            
            cmbFiles.removeAllItems();
            
            pnlMemoryVisualization.repaint();
            
            
        } catch (NumberFormatException ex) {
            
            txtLog.append("Error: Invalid memory size.\n");
            
        }
    }

    private void allocateFile(ActionEvent e) {
        
        if (buddySystem == null) {
            
            txtLog.append("Error: Memory not initialized.\n");
            
            return;
        }

        try {
            
            int fileSize = Integer.parseInt(txtFileSize.getText());
            
            if (fileSize <= 0) throw new NumberFormatException();

            
            String fileName = "File" + (cmbFiles.getItemCount() + 1);
            
            boolean success = buddySystem.allocateFile(fileSize, fileName);

            if (success) {
                
                txtLog.append("Allocated " + fileSize + " KB to " + fileName + ".\n");
                
                cmbFiles.addItem(fileName);
                
            } else {
                
                txtLog.append("Error: Insufficient memory for file of size " + fileSize + " KB.\n");
                
            }
            
            pnlMemoryVisualization.repaint();
            
        } catch (NumberFormatException ex) {
            
            txtLog.append("Error: Invalid file size.\n");
            
        }
    }

    private void deallocateFile(ActionEvent e) {
        
        if (buddySystem == null) {
            
            txtLog.append("Error: Memory not initialized.\n");
            
            return;
        }

        
        String fileName = (String) cmbFiles.getSelectedItem();
        
        if (fileName == null) {
            
            txtLog.append("Error: No file selected for deallocation.\n");
            
            return;
            
        }

        boolean success = buddySystem.deallocateFile(fileName);

        if (success) {
            
            txtLog.append("Deallocated " + fileName + ".\n");
            
            cmbFiles.removeItem(fileName);
            
        } else {
            
            txtLog.append("Error: File not found in memory.\n");
            
        }
        
        pnlMemoryVisualization.repaint();
        
    }

    private void renderMemoryBlocks(Graphics g) {
        
    if (buddySystem == null) return;
    
    

    List<BuddyBlock> blocks = buddySystem.getMemoryBlocks();
    
    int panelWidth = pnlMemoryVisualization.getWidth();
    int panelHeight = pnlMemoryVisualization.getHeight();
    int x = 10;
    int y = 10;
    int totalWidth = panelWidth - 20;
    

    // Iterate through each memory block
    for (int i = 0; i < blocks.size(); i++) {
        
        BuddyBlock block = blocks.get(i);
        
        int width = (int) (block.getSize() / (double) buddySystem.getTotalMemorySize() * totalWidth);

        
        // Choose a color based on block status
        if (block.isFree()) {
            
            g.setColor(new Color(144, 238, 144)); // Light green for free blocks
            
        } else
        {
            
            // Assign different shades of red based on the block index (to differentiate buddies visually)
            g.setColor(new Color(255 - (i * 15) % 100, 69, 69)); // Slight variation of red
            
        }

        // Draw the block
        g.fillRect(x, y, width, panelHeight - 20);
        g.setColor(Color.BLACK); // Outline color
        g.drawRect(x, y, width, panelHeight - 20);

        
        // Add label for the block size
        String label = block.getSize() + " KB" + (block.isFree() ? "" : " (" + block.getFileName() + ")");
        g.drawString(label, x + 5, y + 15);

        
        // Move to the next block
        x += width;
    }
}

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> new BuddySystemGUI().setVisible(true));
        
    }
}
