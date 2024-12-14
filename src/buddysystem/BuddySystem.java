/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buddysystem;

/**
 *
 * @author hiruna
 */

import java.util.ArrayList;
import java.util.List;

public class BuddySystem {
    
    private int totalMemorySize;
    
    private List<BuddyBlock> memoryBlocks;

    public BuddySystem(int size) {
        
    totalMemorySize = size;
    
    memoryBlocks = new ArrayList<>();
    
    memoryBlocks.add(new BuddyBlock(size, true, 0)); // Start with one large block
    
}


    public int getTotalMemorySize() {
        
        return totalMemorySize;
        
    }

    public List<BuddyBlock> getMemoryBlocks() {
        
        return memoryBlocks;
        
    }

    public boolean allocateFile(int fileSize, String fileName) {
        
    for (int i = 0; i < memoryBlocks.size(); i++) {
        
        BuddyBlock block = memoryBlocks.get(i);

        // Find a free block large enough for the file
        if (block.isFree() && block.getSize() >= fileSize) {
            
            // Split the block until it's just big enough
            while (block.getSize() / 2 >= fileSize) {
                
                block.split();
                
                BuddyBlock buddy = new BuddyBlock(block.getSize(), true, block.getMemoryAddress() + block.getSize());
                
                memoryBlocks.add(i + 1, buddy); // Add buddy immediately after
                
            }
            block.allocate(fileName);
            
            return true;
        }
        
    }
    return false; // No suitable block found
}



    public boolean deallocateFile(String fileName) {
        
        for (BuddyBlock block : memoryBlocks) {
            
            if (!block.isFree() && block.getFileName().equals(fileName)) {
                
                block.deallocate();
                
                mergeBlocks();
                
                return true;
                
            }
        }
        
        return false;
        
    }

    private void mergeBlocks() {
        
    for (int i = 0; i < memoryBlocks.size() - 1; i++) {
        
        BuddyBlock current = memoryBlocks.get(i);
        
        BuddyBlock next = memoryBlocks.get(i + 1);

        // Check if blocks are buddies (same size, free)
        if (current.isFree() && next.isFree() && current.getSize() == next.getSize() && current.getMemoryAddress() + current.getSize() == next.getMemoryAddress()) {
            
            current.merge(next); // Merge sizes
            
            memoryBlocks.remove(next); // Remove buddy
            
            i--; // Recheck for further merging
            
        }
    }
}


}
