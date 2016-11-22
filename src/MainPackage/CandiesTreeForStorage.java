package MainPackage;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class CandiesTreeForStorage extends MyTree {

    private static final ImageIcon iconCandy = new javax.swing.ImageIcon(CandiesTreeForStorage.class.getResource("/Images/candy-icon.png"));    
    private static final ImageIcon miscIcon = new javax.swing.ImageIcon(CandiesTreeForStorage.class.getResource("/Images/Misc-Box-icon.png"));
    private static final ImageIcon iconFactory = new javax.swing.ImageIcon(CandiesTreeForPackage.class.getResource("/Images/industry-icon.png"));    
    
    public CandiesTreeForStorage () {
        super();
        this.setCellRenderer(new MyTreeCellRenderer());
        this.getSelectionModel ().setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }

    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        private final Font fontBold = new Font("Tahoma",Font.BOLD,12);
        private final Font fontNotBold = new Font("Tahoma",Font.PLAIN,12);        
        
        private JPanel panel = new JPanel();
        private JLabel caption = new JLabel();
        private JLabel storage_1 = new JLabel();
        private JLabel storage_2 = new JLabel();
        private JLabel storage_3 = new JLabel();

        public MyTreeCellRenderer() {
             panel.setLayout(new FlowLayout());
             ((FlowLayout)panel.getLayout()).setHgap(0);
             ((FlowLayout)panel.getLayout()).setVgap(0);
             panel.setBackground(new Color(204,255,255));

             panel.add(caption);
             panel.add(storage_1);
             panel.add(storage_2);
             panel.add(storage_3);

             caption.setMinimumSize(new Dimension(180,18));
             caption.setMaximumSize(new Dimension(180,18));
             caption.setPreferredSize(new Dimension(180,18));
             storage_1.setMinimumSize(new Dimension(85,18));
             storage_1.setMaximumSize(new Dimension(85,18));
             storage_1.setPreferredSize(new Dimension(85,18));
             storage_2.setMinimumSize(new Dimension(70,18));
             storage_2.setMaximumSize(new Dimension(70,18));
             storage_2.setPreferredSize(new Dimension(70,18));             
             storage_3.setMinimumSize(new Dimension(70,18));
             storage_3.setMaximumSize(new Dimension(70,18));
             storage_3.setPreferredSize(new Dimension(70,18));             

             caption.setForeground(Color.BLUE);

             caption.setFont(new Font("Arial",Font.PLAIN,12));
             storage_1.setFont(new Font("Arial",Font.PLAIN,12));
             storage_2.setFont(new Font("Arial",Font.PLAIN,12));
             storage_3.setFont(new Font("Arial",Font.PLAIN,12));

             setBackgroundNonSelectionColor(MainForm.LightInterfaceColor);
             setBackgroundSelectionColor(MainForm.LightInterfaceColor);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree,Object value, boolean selected, boolean expanded, boolean leaf, int row,  boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            try {
                Object[] obj = (Object[]) node.getUserObject();
                switch (node.getLevel()) {
                    case 1:
                        caption.setText((String)obj[0]);
                        caption.setFont(fontBold);
                        caption.setForeground(new Color(200,0,0));
                        caption.setIcon(iconFactory);
                        storage_1.setText("");
                        storage_1.setIcon(null);
                        storage_2.setText("");
                        storage_3.setText("");                        
                        break;                     
                    case 2:
                        if ((Boolean)obj[4]) {
                            caption.setForeground(Color.BLUE);
                        } else {
                            caption.setForeground(Color.GRAY);
                        }
                        caption.setFont(fontNotBold);
                        caption.setText((String)obj[0]);
                        caption.setIcon(iconCandy);
                        storage_1.setText((String)obj[6]);
                        storage_1.setIcon(miscIcon);
                        storage_2.setText((String)obj[3]);
                        storage_3.setText((String)obj[2]);
                        if ((Boolean)obj[5]) {
                            storage_1.setForeground(new Color(200,0,0));
                            storage_2.setForeground(new Color(200,0,0));
                            storage_3.setForeground(new Color(200,0,0));
                        } else {
                            storage_1.setForeground(Color.BLUE);
                            storage_2.setForeground(Color.BLUE);
                            storage_3.setForeground(Color.BLUE);
                        }
                        break;
                }
            } catch (Exception ex) {
            }
            return panel;
        }
    }
}
