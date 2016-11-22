package MainPackage;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.tree.*;

public class CheckBoxTree extends MyTree {

    public CheckBoxTree() {
        super();
        this.getSelectionModel ().setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
    }
}

class NodeSelectionListener extends MouseAdapter {

        JTree tree;

        NodeSelectionListener(JTree tree) {
            this.tree = tree;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int row = tree.getRowForLocation(x, y);
            TreePath path = tree.getPathForRow(row);
            if (path != null) {
                CheckNode node = (CheckNode) path.getLastPathComponent();
                boolean isSelected = !(node.isSelected());
                node.setSelected(isSelected);
                ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
                tree.repaint();
            }
        }
    }


class CheckRenderer extends JPanel implements TreeCellRenderer {

    private static final ImageIcon userIcon = new javax.swing.ImageIcon(CheckBoxTree.class.getResource("/Images/User-icon.png"));
    
    protected JCheckBox check;
    protected TreeLabel label;

    public CheckRenderer() {
        setLayout(null);
        add(check = new JCheckBox());
        add(label = new TreeLabel());
        check.setBackground(MainForm.LightInterfaceColor);
        label.setForeground(UIManager.getColor("Tree.textForeground"));
        label.setBackground(MainForm.LightInterfaceColor);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,  boolean isSelected, boolean expanded, boolean leaf, int row,  boolean hasFocus) {
        setEnabled(tree.isEnabled());
        check.setSelected(((CheckNode) value).isSelected());
        label.setFont(tree.getFont());
        label.setSelected(isSelected);
        label.setFocus(hasFocus);
        Object[] obj = (Object[])((CheckNode) value).getUserObject();
        label.setText((String)obj[0]);
        label.setBackground(new Color(153,255,255));
        label.setIcon(userIcon);
        return this;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d_check = check.getPreferredSize();
        Dimension d_label = label.getPreferredSize();
        return new Dimension(d_check.width + d_label.width, (d_check.height < d_label.height ? d_label.height : d_check.height));
    }

    @Override
    public void doLayout() {
        Dimension d_check = check.getPreferredSize();
        Dimension d_label = label.getPreferredSize();
        int y_check = 0;
        int y_label = 0;
        if (d_check.height < d_label.height) {
            y_check = (d_label.height - d_check.height) / 2;
        } else {
            y_label = (d_check.height - d_label.height) / 2;
        }
        check.setLocation(0, y_check);
        check.setBounds(0, y_check, d_check.width, d_check.height);
        label.setLocation(d_check.width, y_label);
        label.setBounds(d_check.width, y_label, d_label.width, d_label.height);
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(MainForm.LightInterfaceColor);
    }

    public class TreeLabel extends JLabel {

        boolean isSelected;
        boolean hasFocus;

        public TreeLabel() {
        }

        @Override
        public void setBackground(Color color) {
            super.setBackground(MainForm.LightInterfaceColor);
        }

        @Override
        public void paint(Graphics g) {
            String str;
            if ((str = getText()) != null) {
                if (0 < str.length()) {
                    g.setColor(MainForm.LightInterfaceColor);
                    Dimension d = getPreferredSize();
                    int imageOffset = 0;
                    Icon currentI = getIcon();
                    if (currentI != null) {
                        imageOffset = currentI.getIconWidth()
                                + Math.max(0, getIconTextGap() - 1);
                    }
                    g.fillRect(imageOffset, 0, d.width - 1 - imageOffset,
                            d.height);
                    if (hasFocus) {
                        g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
                        g.drawRect(imageOffset, 0, d.width - 1 - imageOffset,
                                d.height - 1);
                    }
                }
            }
            super.paint(g);
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension retDimension = super.getPreferredSize();
            if (retDimension != null) {
                retDimension = new Dimension(retDimension.width + 3,
                        retDimension.height);
            }
            return retDimension;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public void setFocus(boolean hasFocus) {
            this.hasFocus = hasFocus;
        }
    }
}

final class CheckNode extends DefaultMutableTreeNode {

    public final static int SINGLE_SELECTION = 0;
    public final static int DIG_IN_SELECTION = 4;
    protected int selectionMode;
    protected boolean isSelected;
    protected boolean SourceState;


    public CheckNode() {
        this(null);
    }

    public CheckNode(Object userObject) {
        this(userObject, true, false);
    }

    public CheckNode(Object userObject, boolean allowsChildren, boolean isSelected) {
        super(userObject, allowsChildren);
        this.isSelected = isSelected;
        this.SourceState = isSelected;
        setSelectionMode(DIG_IN_SELECTION);
    }

    public void setSelectionMode(int mode) {
        selectionMode = mode;
    }

    public int getSelectionMode() {
        return selectionMode;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;

        if ((selectionMode == DIG_IN_SELECTION) && (children != null)) {
            Enumeration e = children.elements();
            while (e.hasMoreElements()) {
                CheckNode node = (CheckNode) e.nextElement();
                node.setSelected(isSelected);
            }
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean WasAdded() {
        return (isSelected!=SourceState) && (isSelected);
    }

    public boolean WasRemoved() {
        return (isSelected!=SourceState) && (!isSelected);
    }
}

