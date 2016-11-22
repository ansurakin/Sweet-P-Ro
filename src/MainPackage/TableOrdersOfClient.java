package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableOrdersOfClient extends JTable {

    public void makeTable(Object data[][]) {

        this.setModel(new DefaultTableModel(data, new String[]{"Дата заказа", "Заказано кол-во", "Сумма заказа", "Статус"}));
        this.setRowHeight(20);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(1);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(2);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(3);
        column.setMaxWidth(50);
        column.setMinWidth(50);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
        getTableHeader().setReorderingAllowed(false);   
        getTableHeader().setResizingAllowed(false);        
    }

    private class CellRenderer extends JLabel implements TableCellRenderer {

        public CellRenderer(int align) {
            setOpaque(true);            
            setFont(new Font("Arial", 0, 12));
            setForeground(Color.BLUE);
            setHorizontalAlignment(align);            
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? new Color(102,255,255) : new Color(204,255,255));
            if (column==3) {
                setText("");
                try {
                    setIcon(Order.getIconForState((Integer)value));
                } catch (Exception ex) {
                    setIcon(null);
                }
            } else {
                setText(value==null ? "" : value.toString());                
            }
            return this;
        }
    }
}