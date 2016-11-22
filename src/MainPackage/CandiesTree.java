package MainPackage;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class CandiesTree extends MyTree {

    private static final ImageIcon iconFactory = new javax.swing.ImageIcon(CandiesTree.class.getResource("/Images/industry-icon.png"));
    private static final ImageIcon iconCandy = new javax.swing.ImageIcon(CandiesTree.class.getResource("/Images/candy-icon.png"));
    
    
    public CandiesTree () {
        super();        
        this.setCellRenderer(new MyTreeCellRenderer());
        this.getSelectionModel ().setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }
     
    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        private final Font fontBold = new Font("Tahoma",Font.BOLD,12);
        private final Font fontNotBold = new Font("Tahoma",Font.PLAIN,12);        
        
        public MyTreeCellRenderer() {
             this.setLayout(new BorderLayout());
             setBackgroundNonSelectionColor(MainForm.LightInterfaceColor);
             setBackgroundSelectionColor(MainForm.LightInterfaceColor);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            try {
                Object[] obj = (Object[]) node.getUserObject();
                switch (node.getLevel()) {
                    case 0:
                        setText((String)obj[0]);
                        setFont(fontBold);
                        setForeground(Color.RED);
                        setIcon(null);
                        break;
                    case 1:
                        setText((String)obj[0]);
                        setFont(fontBold);
                        setForeground(new Color(200,0,0));
                        setIcon(iconFactory);
                        setPreferredSize(new Dimension(150,18));                        
                        break;                     
                    case 2:
                        if ((Boolean)obj[2]) {
                            setForeground(Color.BLUE);
                        } else {
                            setForeground(Color.GRAY);
                        }
                        setFont(fontNotBold);
                        setText((String)obj[0]);
                        setIcon(iconCandy);
                        setPreferredSize(new Dimension(150,18));
                        break;
                }
            } catch (Exception ex) {
            }
            return this;
        }
    }
}
