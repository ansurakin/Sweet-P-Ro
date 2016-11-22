package MainPackage;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class GiftsTree extends MyTree {

    private static final ImageIcon giftIcon = new javax.swing.ImageIcon(GiftsTree.class.getResource("/Images/gift-icon2.png"));
    
    public GiftsTree () {
        super();
        this.setCellRenderer(new MyTreeCellRenderer());
        this.setRowHeight(20);

        this.getSelectionModel ().setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }

    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        public MyTreeCellRenderer() {
             this.setLayout(new BorderLayout());
             setBackgroundNonSelectionColor(MainForm.LightInterfaceColor);
             setBackgroundSelectionColor(MainForm.LightInterfaceColor);
        }

        @Override
        public Component getTreeCellRendererComponent( JTree tree,Object value,boolean selected, boolean expanded,  boolean leaf, int row,  boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            try {
                Object[] obj = (Object[]) node.getUserObject();
                setText((String)obj[0]);
                setForeground(Color.blue);
                setIcon(giftIcon);
            } catch (Exception ex) {  }
            return this;
        }
    }
}
