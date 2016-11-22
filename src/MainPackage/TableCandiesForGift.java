package MainPackage;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableCandiesForGift extends JTable {

    private static final Color colorFont = new Color(200,0,0);
    private Object[][] data;
    
    public void makeTable(Object data[][]) {

        this.data = data;
        this.setModel(new DefaultTableModel(data, new String[]{"Название", "Фабрика", "Вес", "Цена","",""}));
        this.setRowHeight(20);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

        column = this.getColumnModel().getColumn(1);
        column.setMaxWidth(65);
        column.setMinWidth(65);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

        column = this.getColumnModel().getColumn(2);
        column.setMaxWidth(45);
        column.setMinWidth(45);        
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(3);
        column.setMaxWidth(45);
        column.setMinWidth(45);        
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
        getColumnModel().getColumn(4).setPreferredWidth(0);
        getColumnModel().getColumn(4).setMinWidth(0);
        getColumnModel().getColumn(4).setMaxWidth(0);
        getColumnModel().getColumn(5).setPreferredWidth(0);
        getColumnModel().getColumn(5).setMinWidth(0);
        getColumnModel().getColumn(5).setMaxWidth(0);

        getTableHeader().setReorderingAllowed(false); 
        getTableHeader().setResizingAllowed(false);        
    }

    public int getIDselectedCandy() {
        return this.getSelectedRow()==-1 ? -1 : (Integer)this.getModel().getValueAt(this.getSelectedRow(),5);// ID in data[5];
    }
 
    private class CellRenderer extends JLabel implements TableCellRenderer {

        public CellRenderer(int align) {
            setOpaque(true);
            setHorizontalAlignment(align);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? new Color(102,255,255) : new Color(204,255,255));
            if (column==0) {
                if ((Boolean)data[row][4]) { // if "is used"
                    setForeground(Color.BLUE);
                } else {
                    setForeground(Color.GRAY);
                }
            } else {
                setForeground(colorFont);        
            }
            setText(value==null ? "" : value.toString());
            return this;
        }
    }
}