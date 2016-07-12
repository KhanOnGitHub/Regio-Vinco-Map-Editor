/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.test_bed;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author eyb0s
 */
public class TestFile {
    
    public static void main(String[] args) throws IOException {
        File testFile = new File("./work/", "test.json");
        if(testFile.createNewFile())
            System.out.println("Success");
        else
            System.out.println("Failure");
        
    }
    
}
