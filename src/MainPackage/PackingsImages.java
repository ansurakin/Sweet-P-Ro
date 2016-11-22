package MainPackage;

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class PackingsImages {
    
    private static HashMap<String,ImageIcon> images = new HashMap<>();
    
    public static void removeImages() {
        images.clear();
    }
    
    public static void addImage(Image image, String name) {
        images.put(name, image==null ? null : new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_FAST)));
    }
    
    public static ImageIcon getImage(String name) {
        return images.get(name);
    }

    public static boolean existsImageWithName(String name) {
        return images.containsKey(name);
    }
    
    public static int getSize() {
        return images.size();
    }
}
