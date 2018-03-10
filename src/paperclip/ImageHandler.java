/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paperclip;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;

/**
 *
 * @author zainul
 */
public class ImageHandler {
    public static Image loadImages(String file_path) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file_path);
        Image image = new Image(fileInputStream);
        return image;
    }
    public static Image exportImage() {
        System.out.print("Exporting image");
        return null;
    }
    
    public static Image resizeImage() {
        System.out.print("Exporting image");
        return null;
    }
}
