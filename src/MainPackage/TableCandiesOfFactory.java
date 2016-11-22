package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableCandiesOfFactory extends JTable {

    public void makeTable(Object data[][]) {

        this.setModel(new DefaultTableModel(data, new String[]{"Название", "Вес ящ.", "Штук в ящ.","Вес 1 шт.","Цена 1 кг","Цена 1 шт.","Цена ящика"}));
        this.setRowHeight(20);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setMaxWidth(120);
        column.setMinWidth(120);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

        for (int i=1;i<this.getColumnModel().getColumnCount();i++) {
            column = this.getColumnModel().getColumn(i);
            column.setHeaderRenderer(new HeaderRenderer());
            column.setCellRenderer(new CellRenderer(JLabel.CENTER));            
        }
        
        getTableHeader().setReorderingAllowed(false);  
        getTableHeader().setResizingAllowed(false);
    }
    
    public void makeTableAllFactories(Object data[][]) {

        this.setModel(new DefaultTableModel(data, new String[]{"Фабрика","Название", "Вес ящ.", "Штук в ящ.","Вес 1 шт.","Цена 1 кг","Цена 1 шт.","Цена ящика"}));
        this.setRowHeight(20);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setMaxWidth(140);
        column.setMinWidth(140);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        for (int i=0;i<this.getColumnModel().getColumnCount();i++) {
            column = this.getColumnModel().getColumn(i);
            column.setHeaderRenderer(new HeaderRenderer());
            column.setCellRenderer(new CellRenderer(JLabel.CENTER));            
        }
        
        column = this.getColumnModel().getColumn(1);
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));            
        column.setMinWidth(140);
        column.setMaxWidth(140);
        column.setPreferredWidth(140);
        
        getTableHeader().setReorderingAllowed(false);       
    }

    private class CellRenderer extends JLabel implements TableCellRenderer {

        public CellRenderer(int align) {
            setOpaque(true);            
            setFont(new Font("Arial", 0, 12));            
            setHorizontalAlignment(align);            
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? new Color(102,255,255) : new Color(204,255,255));
            if (table.getColumnCount()==7) {
                if (column>0) {
                    setForeground(Color.BLUE);
                } else {
                    setForeground(new Color(200,0,0));
                }
                if ((column==3) || (column==5)) {
                    setBackground(Color.GREEN);
                }
            } else {
                if (column>1) {
                    setForeground(Color.BLUE);
                } else {
                    setForeground(new Color(200,0,0));
                }
                if ((column==4) || (column==6)) {
                    setBackground(Color.GREEN);
                }
            }
            setText(value==null ? "" : value.toString());
            return this;
        }
    }
}