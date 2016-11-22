package MainPackage;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.plaf.metal.MetalTreeUI;
import javax.swing.tree.TreePath;

public class MyTree extends JTree {
    
    public MyTree() {
        this.setUI ( new MetalTreeUI() {
            @Override
            public void paint ( Graphics g, JComponent c )  {
                super.paint ( g, c );
                if (tree.getSelectionPath () != null ) {
                    TreePath path = tree.getSelectionPath ();
                    Rectangle b = getPathBounds ( tree, path );
                    Graphics2D g2d = ( Graphics2D ) g;
                    g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
                    g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, 0.3f ) );
                    g2d.setPaint ( new GradientPaint ( 0, b.y, new Color(200,0,0), 0, b.y + b.height - 1, Color.WHITE ) );
                    g2d.fillRoundRect ( 0, b.y, tree.getWidth () - 1, b.height - 1, 6, 6 );
                }
            }
        } );        
    }
}
