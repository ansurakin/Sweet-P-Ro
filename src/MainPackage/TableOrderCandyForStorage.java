package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableOrderCandyForStorage extends JTable {

    public void makeTable(Object data[][]) {

        this.setModel(new DefaultTableModel(data, new String[]{"Фабрика", "Конфета",
            "Заказ, ящ.","<html>&Sigma; Вес, кг</html>","Цена 1 кг","<html>&Sigma; Цена, грн</html>"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
               switch (column) {
                   case 2:
                       return true;
                   default:
                       return false;
               }   
            }
        });
        
        this.setRowHeight(20);
        getTableHeader().setReorderingAllowed(false);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(1);
        column.setMinWidth(150);
        column.setMaxWidth(150);
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
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
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
            if ((column==3) || (column==5)) {
                setBackground(Color.GREEN);
            }
            return this;
        }
    }
}