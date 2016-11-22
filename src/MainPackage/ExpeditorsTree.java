package MainPackage;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class ExpeditorsTree extends MyTree {

    private static final ImageIcon icon = new javax.swing.ImageIcon(ExpeditorsTree.class.getResource("/Images/delivery_small.png"));
    
    
    public ExpeditorsTree () {
        super();        
        this.setCellRenderer(new MyTreeCellRenderer());
        this.getSelectionModel ().setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }
     
    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        public MyTreeCellRenderer() {
             this.setLayout(new BorderLayout());
             setBackgroundNonSelectionColor(MainForm.LightInterfaceColor);
             setBackgroundSelectionColor(MainForm.LightInterfaceColor);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            try {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object[] obj = (Object[]) node.getUserObject();
                setText((String)obj[0]);
                setIcon(icon);
            } catch (Exception ex) {}
            return this;
        }
    }
}
