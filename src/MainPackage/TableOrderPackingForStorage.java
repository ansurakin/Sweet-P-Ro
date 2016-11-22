package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableOrderPackingForStorage extends JTable {

    public void makeTable(Object data[][]) {

        this.setModel(new DefaultTableModel(data, new String[]{"","Упаковка", "Заказ, шт.","Цена 1 шт","<html>&Sigma; Цена, грн</html>",""}));
        this.setRowHeight(50);
        getTableHeader().setReorderingAllowed(false);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setMinWidth(50);
        column.setMaxWidth(50);        
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new MyCellRenderer());

        column = this.getColumnModel().getColumn(1);        
        column.setMinWidth(140);
        column.setMaxWidth(140);        
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

        column = this.getColumnModel().getColumn(2);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(3);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(4);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
        column = this.getColumnModel().getColumn(5);        
        column.setMinWidth(0);
        column.setMaxWidth(0);        
        
        getTableHeader().setReorderingAllowed(false);   
        getTableHeader().setResizingAllowed(false);        
    }

    private class CellRenderer extends JLabel implements TableCellRenderer {

        public CellRenderer(int align) {
            setOpaque(true);            
            setForeground(Color.BLUE);
            setFont(new Font("Arial", 0, 12));
            setHorizontalAlignment(align);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? new Color(102,255,255) : new Color(204,255,255));
            setText(value==null ? "" : value.toString());
            if ((column==2) || (column==3)) {
                setBackground(Color.GREEN);
            }
            return this;
        }
    }
    
    private class MyCellRenderer extends JLabel implements TableCellRenderer {

        public MyCellRenderer() {
            setOpaque(true);  
            setText("");
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setIcon(value==null ? null : (ImageIcon)value);
            return this;
        }
    }
}