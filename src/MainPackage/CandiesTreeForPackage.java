package MainPackage;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class CandiesTreeForPackage extends MyTree {

    private static final ImageIcon iconCandy = new javax.swing.ImageIcon(CandiesTreeForPackage.class.getResource("/Images/candy-icon.png"));
    private static final ImageIcon iconFactory = new javax.swing.ImageIcon(CandiesTreeForPackage.class.getResource("/Images/industry-icon.png"));
    private static final ImageIcon coinsIcon = new javax.swing.ImageIcon(CandiesTreeForPackage.class.getResource("/Images/coins-icon.png"));
    private static final ImageIcon weightIcon = new javax.swing.ImageIcon(CandiesTreeForPackage.class.getResource("/Images/weight-icon.png"));
    
    public CandiesTreeForPackage () {
        super();          
        this.setCellRenderer(new MyTreeCellRenderer());
        this.getSelectionModel ().setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }

    public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

        private final Font fontBold = new Font("Tahoma",Font.BOLD,12);
        private final Font fontNotBold = new Font("Tahoma",Font.PLAIN,12);        
        
        private JPanel panel = new JPanel();
        private JLabel cost = new JLabel();
        private JLabel caption = new JLabel();
        private JLabel weight = new JLabel();

        public MyTreeCellRenderer() {
             panel.setLayout(new FlowLayout());
             ((FlowLayout)panel.getLayout()).setHgap(0);
             ((FlowLayout)panel.getLayout()).setVgap(0);
             panel.setBackground(new Color(204,255,255));

             panel.add(caption);
             panel.add(cost);
             panel.add(weight);

             caption.setMinimumSize(new Dimension(170,18));
             caption.setMaximumSize(new Dimension(170,18));
             caption.setPreferredSize(new Dimension(170,18));
             cost.setMinimumSize(new Dimension(60,18));
             cost.setMaximumSize(new Dimension(60,18));
             cost.setPreferredSize(new Dimension(60,18));
             weight.setMinimumSize(new Dimension(60,18));
             weight.setMaximumSize(new Dimension(60,18));
             weight.setPreferredSize(new Dimension(60,18));

             caption.setForeground(Color.BLUE);
             weight.setForeground(new Color(200,0,0));
             cost.setForeground(new Color(200,0,0));

             caption.setFont(new Font("Arial",Font.PLAIN,12));
             weight.setFont(new Font("Arial",Font.PLAIN,12));
             cost.setFont(new Font("Arial",Font.PLAIN,12));

             setBackgroundNonSelectionColor(MainForm.LightInterfaceColor);
             setBackgroundSelectionColor(MainForm.LightInterfaceColor);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree,  Object value, boolean selected,  boolean expanded,  boolean leaf,  int row,  boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            try {
                Object[] obj = (Object[]) node.getUserObject();
                switch (node.getLevel()) {
                    case 1:
                        caption.setText((String)obj[0]);
                        caption.setFont(fontBold);
                        caption.setForeground(new Color(200,0,0));
                        caption.setIcon(iconFactory);
                        weight.setText("");
                        cost.setText("");
                        cost.setIcon(null);
                        weight.setIcon(null);
                        break;                     
                    case 2:
                        caption.setText((String)obj[0]);
                        caption.setIcon(iconCandy);
                        caption.setFont(fontNotBold);
                        cost.setText((String)obj[2]);
                        cost.setIcon(coinsIcon);
                        weight.setText((String)obj[3]);
                        weight.setIcon(weightIcon);
                        if ((Boolean)obj[4]) {
                            caption.setForeground(Color.BLUE);
                        } else {
                            caption.setForeground(Color.GRAY);
                        }
                        break;
                }
            } catch (Exception ex) {
            }
            return panel;
        }
    }
}
