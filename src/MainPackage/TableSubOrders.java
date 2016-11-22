package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableSubOrders extends JTable {

    private MainForm mf;
    private boolean editable;
    private int userLevel;
    
    public void makeTable(Object data[][], MainForm mf, boolean editable, int userLevel) {

        this.mf = mf;
        this.editable = editable;
        this.userLevel = userLevel;
        
        MyTableModel tm = new MyTableModel();
        tm.SetColumnNames(new String[]{"Набор", "Упаковка","Себест.,грн","Цена,грн","Кол-во","ИТОГО,грн"});
        tm.SetData(data);
        this.setModel(tm);
        this.setRowHeight(20);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

        column = this.getColumnModel().getColumn(1);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

        column = this.getColumnModel().getColumn(2);
        column.setMaxWidth(70);
        column.setMinWidth(70);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(3);
        column.setMaxWidth(70);
        column.setMinWidth(70);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
        column = this.getColumnModel().getColumn(4);
        column.setMaxWidth(50);
        column.setMinWidth(50);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
        column = this.getColumnModel().getColumn(5);
        column.setMaxWidth(70);
        column.setMinWidth(70);
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
            if (editable && (row < data.length - 1) && ((col == 3) || (col == 4))) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
            mf.SaveChangesInTableSubOrders(col, row, value);
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
            if (isSelected) {
                setBackground(new Color(102,255,255));
            } else {
                setBackground(new Color(204,255,255));
            }
            if (userLevel==MainForm.STORAGE_HEAD) {
                if ((column==2) || (column==3) || (column==5)) {
                    setText("");
                } else {
                    setText(value==null ? "" : value.toString());              
                }
            } else if (userLevel!=MainForm.DIRECTOR) {
                if (column==2) {
                    setText("");
                } else {
                    setText(value==null ? "" : value.toString());              
                }
            } else {
                setText(value==null ? "" : value.toString());                 
            }
            if ((column==3) || (column==4)) {
                setBackground(Color.green);
            } else if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }
}