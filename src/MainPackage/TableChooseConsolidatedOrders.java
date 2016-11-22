package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableChooseConsolidatedOrders extends JTable {

    
    public void makeTable(String[] columnNames, Object data[][]) {

        this.setModel(new DefaultTableModel(data, columnNames));
        this.setRowHeight(20);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setMaxWidth(40);
        column.setMinWidth(40);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new MyRenderer());
        column.setCellEditor(new MyEditor());        
        
        column = this.getColumnModel().getColumn(1);
        column.setMaxWidth(90);
        column.setMinWidth(90);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));

        column = this.getColumnModel().getColumn(2);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

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
            setHorizontalAlignment(JLabel.CENTER);
            setForeground(Color.BLUE);
            setHorizontalAlignment(align);            
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? new Color(102,255,255) : new Color(204,255,255));
            if (column==3) {
                setText("");
                setIcon(Order.getIconForState((Integer)value));
            } else {
                setText(value==null ? "" : value.toString());                
            }
            return this;
        }
    }
    
    private class MyRenderer implements TableCellRenderer {
        
        private JCheckBox checkBox = new JCheckBox();
        
        public MyRenderer() {
            checkBox.setText("");
            checkBox.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            checkBox.setSelected((Boolean)table.getValueAt(row, 0));
            return checkBox;
        }
        
    }
    
    private class MyEditor implements TableCellEditor {

        private JCheckBox checkBox = new JCheckBox();
        
        public MyEditor() {
            checkBox.setText("");
            checkBox.setHorizontalAlignment(JLabel.CENTER);
            checkBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    TableChooseConsolidatedOrders.this.setValueAt(checkBox.isSelected(), TableChooseConsolidatedOrders.this.getSelectedRow(), 0);            
                }
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            checkBox.setSelected((Boolean)table.getValueAt(row, 0));
            return checkBox;
        }

        @Override
        public Object getCellEditorValue() {
            return checkBox.isSelected();
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        @Override
        public void cancelCellEditing() {
        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {
        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {
        }

    }
}