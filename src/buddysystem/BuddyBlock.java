/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package buddysystem;

/**
 *
 * @author hiruna
 */


public class BuddyBlock {
    
    private int size;
    
    private boolean isFree;
    
    private String fileName;
    
    private int memoryAddress; // Simulated memory address
    

    public BuddyBlock(int size, boolean isFree, int memoryAddress) {
        
        this.size = size;
        this.isFree = isFree;
        this.fileName = null;
        this.memoryAddress = memoryAddress;
    }

    public int getSize() {
        return size;
    }

    public boolean isFree() {
        return isFree;
    }

    public String getFileName() {
        return fileName;
    }

    public int getMemoryAddress() {
        return memoryAddress;
    }

    public void allocate(String fileName) {
        this.isFree = false;
        this.fileName = fileName;
    }

    public void deallocate() {
        this.isFree = true;
        this.fileName = null;
    }

    public void split() {
        size /= 2;
    }

    public void merge(BuddyBlock buddy) {
        size += buddy.getSize();
    }
}
