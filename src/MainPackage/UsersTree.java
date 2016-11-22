package MainPackage;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class UsersTree extends MyTree {

    private static final ImageIcon userGroupIcon = new javax.swing.ImageIcon(UsersTree.class.getResource("/Images/user-group-icon2.png"));
    private static final ImageIcon userIcon = new javax.swing.ImageIcon(UsersTree.class.getResource("/Images/User-icon.png"));
    
    public UsersTree () {
        super();
        this.setCellRenderer(new MyTreeCellRenderer());
        this.setRowHeight(20);

        this.getSelectionModel ().setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }

    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        private final Font fontBold = new Font("Tahoma",Font.BOLD,12);
        private final Font fontNotBold = new Font("Tahoma",Font.PLAIN,12);
        private final Border leftMargin = BorderFactory.createEmptyBorder(0, 30, 0, 0);
        private final Border noMargin = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        
        public MyTreeCellRenderer() {
             this.setLayout(new BorderLayout());
             setBackgroundNonSelectionColor(MainForm.LightInterfaceColor);
             setBackgroundSelectionColor(MainForm.LightInterfaceColor);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree,Object value,boolean selected,boolean expanded, boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            try {
                Object[] obj = (Object[]) node.getUserObject();
                switch (node.getLevel()) {
                    case 1:
                        setText((String)obj[0]);
                        setForeground(Color.RED);
                        setFont(fontBold);
                        setIcon(userGroupIcon);
                        break;
                    case 2:
                        setText((String)obj[0]);
                        setFont(fontNotBold);
                        setForeground(Color.BLUE);
                        setIcon(userIcon);
                        if ((Boolean)obj[2]) { //if subordered
                            setBorder(leftMargin);
                        } else {
                            setBorder(noMargin);
                        }
                        break;
                }
            } catch (Exception ex) { 
            }
            return this;
        }
    }
}
