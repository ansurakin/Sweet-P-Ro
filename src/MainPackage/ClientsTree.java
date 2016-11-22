package MainPackage;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class ClientsTree extends MyTree {

    private static final ImageIcon folderIcon = new javax.swing.ImageIcon(ClientsTree.class.getResource("/Images/Folder-Open-icon.png"));
    
    public ClientsTree () {
        super();        
        this.setCellRenderer(new MyTreeCellRenderer());
        this.setRowHeight(20);

        this.getSelectionModel ().setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }

    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        public MyTreeCellRenderer() {
            setLayout(new BorderLayout());
            setBackgroundNonSelectionColor(MainForm.LightInterfaceColor);
            setBackgroundSelectionColor(MainForm.LightInterfaceColor);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree,Object value,boolean selected, boolean expanded, boolean leaf,int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            try {
                if (node.getLevel()==2) {
                    Object[] obj = (Object[]) node.getUserObject();
                    setText((String)obj[0]);
                    if ((Boolean)obj[2]) {
                        setForeground(Color.blue);
                    } else {
                        setForeground(Color.gray);
                    }
                    setIcon(ClientState.getIcon((ClientState.STATE)obj[3]));
                } else if (node.getLevel()==0) {
                    setIcon(null);
                    setForeground(Color.red);
                    Object[] obj = (Object[]) node.getUserObject();
                    setText((String)obj[0]);
                } else if (node.getLevel()==1) {
                    setIcon(folderIcon);
                    setForeground(Color.RED);
                    Object[] obj = (Object[]) node.getUserObject();
                    setText((String)obj[1]);
                }
            } catch (Exception ex) {  }
            return this;
        }
    }
    
}
