package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableGiftCandy extends JTable {

    private MainForm mf;
    private boolean editable;

    public void makeTable(Object data[][], MainForm mf,boolean editable) {

        this.mf = mf;
        this.editable = editable;

        MyTableModel tm = new MyTableModel();
        tm.SetColumnNames(new String[]{"Фабрика", "Конфета", "Кол-во","<html>&Sigma; Цена, грн</html>","<html>&Sigma; Вес, кг</html>"});
        tm.SetData(data);
        this.setModel(tm);
        this.setRowHeight(20);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(1);
        column.setMaxWidth(120);
        column.setMinWidth(120);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

        column = this.getColumnModel().getColumn(2);
        column.setMaxWidth(50);
        column.setMinWidth(50);        
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(3);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(4);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
        getTableHeader().setReorderingAllowed(false); 
        getTableHeader().setResizingAllowed(false);        
    }

     private class MyTableModel extends AbstractTableModel {

        private String[] columnNames = null;
        private Object[][] data = null;

        public void SetColumnNames(String[] columnNames) {
            this.columnNames = columnNames;
        }

        public void SetData(Object[][] data) {
            this.data = data;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            if ((editable) && (col==2) && (row!=data.length-1)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
            mf.SaveChangesInTableGiftsCandy(col, row, value);
        }

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
            setForeground(column>1 ? Color.BLUE : new Color(200,0,0));
            if (row==table.getRowCount()-1) {
                setForeground(Color.red);
                setFont(new Font("Arial",Font.BOLD,12));
            }
            setText(value==null ? "" : value.toString());
            return this;
        }
    }
}