package MainPackage;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class PackingsTreeForStorage extends MyTree {

    private static final ImageIcon packingIcon = new javax.swing.ImageIcon(PackingsTreeForStorage.class.getResource("/Images/upakovka_146_2.png"));
    
    public PackingsTreeForStorage () {
        super();
        this.setCellRenderer(new MyTreeCellRenderer());
        this.setRowHeight(60);
        this.getSelectionModel ().setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }

    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        private final Font fontBold = new Font("Tahoma",Font.BOLD,12);
        private final Font fontNotBold = new Font("Tahoma",Font.PLAIN,12);        
        
        private JPanel panel = new JPanel();
        private JCheckBox marked = new JCheckBox();
        private JLabel caption = new JLabel();
        private JLabel storage1 = new JLabel();        
        private JLabel storage2 = new JLabel();        
        private JLabel storage3 = new JLabel();        
        
        public MyTreeCellRenderer() {
             panel.setLayout(new FlowLayout());
             ((FlowLayout)panel.getLayout()).setHgap(0);
             ((FlowLayout)panel.getLayout()).setVgap(0);
             panel.setBackground(new Color(204,255,255));
             panel.add(marked);
             panel.add(caption);
             panel.add(storage1);
             panel.add(storage2);
             panel.add(storage3);
             marked.setMinimumSize(new Dimension(30,60));
             marked.setMaximumSize(new Dimension(30,60));
             marked.setPreferredSize(new Dimension(30,60));
             caption.setMinimumSize(new Dimension(200,60));
             caption.setMaximumSize(new Dimension(200,60));
             caption.setPreferredSize(new Dimension(200,60));
             storage1.setMinimumSize(new Dimension(60,18));
             storage1.setMaximumSize(new Dimension(60,18));
             storage1.setPreferredSize(new Dimension(60,18));
             storage2.setMinimumSize(new Dimension(60,18));
             storage2.setMaximumSize(new Dimension(60,18));
             storage2.setPreferredSize(new Dimension(60,18));             
             storage3.setMinimumSize(new Dimension(60,18));
             storage3.setMaximumSize(new Dimension(60,18));
             storage3.setPreferredSize(new Dimension(60,18));             
             caption.setForeground(Color.BLUE);
             caption.setFont(new Font("Arial",Font.PLAIN,12));
             storage1.setFont(new Font("Arial",Font.PLAIN,12));
             storage2.setFont(new Font("Arial",Font.PLAIN,12));
             storage3.setFont(new Font("Arial",Font.PLAIN,12));
             
             setBackgroundNonSelectionColor(MainForm.LightInterfaceColor);
             setBackgroundSelectionColor(MainForm.LightInterfaceColor);
       }

        @Override
        public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected,boolean expanded, boolean leaf, int row,  boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            try {
                Object[] obj = (Object[]) node.getUserObject();
                switch (node.getLevel()) {
                    case 1:
                        marked.setVisible(false);
                        caption.setText((String)obj[0]);
                        caption.setFont(fontBold);
                        caption.setForeground(Color.RED);
                        caption.setIcon(packingIcon);
                        storage1.setText("");
                        storage1.setIcon(null);
                        storage2.setText("");
                        storage2.setIcon(null);
                        storage3.setText("");
                        storage3.setIcon(null);
                        break;     
                    case 2:
                        marked.setSelected((Boolean)obj[5]);
                        marked.setVisible(true);
                        caption.setText((String) obj[0]);
                        caption.setIcon((ImageIcon)obj[2]);
                        caption.setForeground(Color.BLACK);
                        caption.setFont(fontNotBold);
                        storage1.setText(Integer.toString((Integer) obj[3]-(Integer) obj[4]));
                        storage2.setText(Integer.toString((Integer) obj[4]));
                        storage3.setText(Integer.toString((Integer) obj[3]));
                        if ((Integer) obj[4]>(Integer)obj[3]) {
                            storage1.setForeground(new Color(200, 0, 0));
                            storage2.setForeground(new Color(200, 0, 0));
                            storage3.setForeground(new Color(200, 0, 0));
                        } else {
                            storage1.setForeground(Color.blue);
                            storage2.setForeground(Color.blue);
                            storage3.setForeground(Color.blue);
                        }
                        break;
                }
            } catch (Exception ex) {
            }
            return panel;
        }
    }
}
