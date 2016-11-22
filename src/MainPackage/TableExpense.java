package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableExpense extends JTable {

    public void makeTable(Object data[][]) {

        this.setModel(new DefaultTableModel(data, new String[]{"Дата", "Наименование расхода", "Расход, грн"}));
        this.setRowHeight(20);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setMaxWidth(100);
        column.setMinWidth(100);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(1);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

        column = this.getColumnModel().getColumn(2);
        column.setMaxWidth(90);
        column.setMinWidth(90);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.RIGHT));
        
        getTableHeader().setReorderingAllowed(false);   
        getTableHeader().setResizingAllowed(false);        
    }

    private class CellRenderer extends JLabel implements TableCellRenderer {

        public CellRenderer(int align) {
            setOpaque(true);            
            setHorizontalAlignment(align);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? new Color(102,255,255) : new Color(204,255,255));
            if (row==table.getRowCount()-1) {
                setFont(new Font("Arial", Font.BOLD, 12));
                setForeground(new Color(200,0,0));
            } else {
                setFont(new Font("Arial", 0, 12));                
                setForeground(Color.BLUE);                
            }
            setText(value==null ? "" : value.toString());           
            return this;
        }
    }
}