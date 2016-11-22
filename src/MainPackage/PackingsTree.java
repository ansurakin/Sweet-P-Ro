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

public class PackingsTree extends MyTree {

    private static final ImageIcon weightIcon = new javax.swing.ImageIcon(PackingsTree.class.getResource("/Images/weight-icon.png"));
    private static final ImageIcon packingIcon = new javax.swing.ImageIcon(PackingsTree.class.getResource("/Images/upakovka_146_2.png"));
    
    public PackingsTree () {
        super();
        this.setCellRenderer(new MyTreeCellRenderer());
        this.setRowHeight(60);

        this.getSelectionModel ().setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }

    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        private final Font fontBold = new Font("Tahoma",Font.BOLD,12);
        private final Font fontNotBold = new Font("Tahoma",Font.PLAIN,12);        
        
        private JPanel panel = new JPanel();
        private JLabel name = new JLabel();
        private JCheckBox marked = new JCheckBox();
        private JLabel capacity = new JLabel();
        
        public MyTreeCellRenderer() {
             this.setLayout(new BorderLayout());
             setBackgroundNonSelectionColor(this.getBackground());
             
             panel.setLayout(new FlowLayout());
             ((FlowLayout)panel.getLayout()).setHgap(0);
             ((FlowLayout)panel.getLayout()).setVgap(0);
             panel.setBackground(new Color(204,255,255));

             panel.add(marked);
             panel.add(name);
             panel.add(capacity);

             marked.setMinimumSize(new Dimension(30,60));
             marked.setMaximumSize(new Dimension(30,60));
             marked.setPreferredSize(new Dimension(30,60));
             name.setMinimumSize(new Dimension(250,60));
             name.setMaximumSize(new Dimension(250,60));
             name.setPreferredSize(new Dimension(250,60));
             capacity.setMinimumSize(new Dimension(80,18));
             capacity.setMaximumSize(new Dimension(80,18));
             capacity.setPreferredSize(new Dimension(80,18));

             name.setForeground(Color.BLUE);
             capacity.setForeground(new Color(200,0,0));

             name.setFont(new Font("Arial",Font.PLAIN,12));
             capacity.setFont(new Font("Arial",Font.PLAIN,12));

             setBackgroundNonSelectionColor(MainForm.LightInterfaceColor);
             setBackgroundSelectionColor(MainForm.LightInterfaceColor);
        }

        @Override
        public Component getTreeCellRendererComponent( JTree tree,Object value,boolean selected,boolean expanded, boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            try {
                Object[] obj;
                switch (node.getLevel()) {
                    case 1:
                        obj = (Object[]) node.getUserObject();
                        marked.setVisible(false);
                        name.setText((String)obj[0]);
                        name.setForeground(Color.red);
                        name.setFont(fontBold);
                        name.setIcon(packingIcon);
                        capacity.setText("");
                        capacity.setIcon(null);
                        break;
                    case 2:
                        obj = (Object[]) node.getUserObject();
                        marked.setSelected((Boolean)obj[4]);
                        marked.setVisible(true);
                        name.setText((String)obj[0]);
                        name.setFont(fontNotBold);
                        name.setIcon((ImageIcon)obj[3]);
                        if (obj[2] instanceof String) { //дерево для отображения на вкладке "упаковка"
                            capacity.setText((String)obj[2]);
                            capacity.setIcon(weightIcon);
                        } else { // дерево для выбора упаковки с набором
                            capacity.setText(Integer.toString((Integer)obj[2]));
                            capacity.setIcon(packingIcon);
                            capacity.setForeground((Integer)obj[2]>0 ? Color.BLUE : new Color(200,0,0));
                        }
                        break;
                }
            } catch (Exception ex) {
            }
            return panel;
        }
    }
}
