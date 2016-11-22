package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableBoxesForGifts extends JTable {

    public void makeTable(Object data[][]) {

        this.setModel(new MyTableModel(data));
        this.setRowHeight(20);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

        column = this.getColumnModel().getColumn(1);
        column.setPreferredWidth(65);
        column.setMinWidth(65);
        column.setMaxWidth(65);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(2);
        column.setPreferredWidth(65);
        column.setMinWidth(65);
        column.setMaxWidth(65);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(3);
        column.setPreferredWidth(90);
        column.setMinWidth(90);
        column.setMaxWidth(90);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
        getTableHeader().setReorderingAllowed(false);   
        getTableHeader().setResizingAllowed(false);        
    }

     private class MyTableModel extends AbstractTableModel {

        private String[] columnNames = new String[]{"Набор", "Кол-во", "В ящике","Всего ящиков"};
        private Object[][] data = null;

        public MyTableModel(Object[][] data) {
            this.data = data;
        }
        
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
            if ((col==0) || (col==1) || (col==2)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            if ((col==1) || (col==2)) {
                String s = (Integer)data[row][1]%(Integer)data[row][2]>0 ? " +1" : "";
                data[row][3] = (Integer)data[row][1]/(Integer)data[row][2]+s;
            }
            fireTableCellUpdated(row, col);
            fireTableCellUpdated(row, 3);
        }

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
            if ((column==0) || (column==1) || (column==2)) {
                setBackground(Color.GREEN);
            } else {
                setBackground(table.getBackground());
            }
            setText(value==null ? "" : value.toString());                
            return this;
        }
    }
}