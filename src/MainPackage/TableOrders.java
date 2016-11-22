package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableOrders extends JTable {

    
    public void makeTable(String[] columnNames, Object data[][]) {

        DefaultTableModel dtm = new DefaultTableModel(data, columnNames) {
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.setModel(dtm);
        this.setRowHeight(20);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setMaxWidth(75);
        column.setMinWidth(75);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
        column = this.getColumnModel().getColumn(1);
        column.setMaxWidth(65);
        column.setMinWidth(65);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));        

        column = this.getColumnModel().getColumn(2);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

        column = this.getColumnModel().getColumn(3);
        column.setMaxWidth(25);
        column.setMinWidth(25);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
//        column = this.getColumnModel().getColumn(4);
//        column.setMaxWidth(0);
//        column.setMinWidth(0);
//        column.setPreferredWidth(0);
        
        getTableHeader().setReorderingAllowed(false);   
        getTableHeader().setResizingAllowed(false);        
    }

    private class CellRenderer extends JLabel implements TableCellRenderer {

        private final Color selectedBackground = new Color(102,255,255);
        private final Color nonSelectedBackground = new Color(204,255,255);
        private final Font boldOrderFont = new Font("Arial", Font.BOLD, 11);
        private final Font usualOrderFont = new Font("Arial", 0, 11);
        
        public CellRenderer(int align) {
            setOpaque(true);            
            setHorizontalAlignment(JLabel.CENTER);
            setForeground(Color.BLUE);
            setHorizontalAlignment(align);            
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int thisOrderIsYoung = (int)table.getValueAt(row, 3);
            setFont(thisOrderIsYoung == 2 ? boldOrderFont : usualOrderFont);
            setBackground(isSelected ? selectedBackground : nonSelectedBackground);
            if (column==3) {
                setText("");
                setIcon(Order.getIconForState((Integer)value));
            } 
//            else if (column==4) {
//                setText("");
//            } 
            else {
                setText(value==null ? "" : value.toString());                
            }
            return this;
        }
    }
}