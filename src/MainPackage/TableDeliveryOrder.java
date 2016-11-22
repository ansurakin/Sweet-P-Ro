package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableDeliveryOrder extends JTable {

    public void makeTable(Object data[][]) {

        this.setModel(new DefaultTableModel(data, new String[]{"Статус","Дата", 
            "Контакты","Вид доставки","Адрес","Содержимое","Кто платит","Долг","Презент","Комментарий"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        this.setRowHeight(100);

        TableColumn column = this.getColumnModel().getColumn(0);
        column.setMinWidth(50);
        column.setMaxWidth(50);
        column.setHeaderRenderer(new HeaderRenderer());        
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));

        column = this.getColumnModel().getColumn(1);
        column.setMinWidth(140);
        column.setMaxWidth(140);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(2);
        column.setMinWidth(150);
        column.setMaxWidth(150);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(3);
        column.setMinWidth(90);
        column.setMaxWidth(90);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(4);
        column.setMinWidth(130);
        column.setMaxWidth(130);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(5);
        column.setMinWidth(200);
        column.setMaxWidth(200);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(6);
        column.setMinWidth(60);
        column.setMaxWidth(60);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());

        column = this.getColumnModel().getColumn(7);
        column.setMinWidth(70);
        column.setMaxWidth(70);
        column.setHeaderRenderer(new HeaderRenderer());        
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));
              
        
        column = this.getColumnModel().getColumn(9);
        column.setMinWidth(80);
        column.setMaxWidth(80);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(8);
        column.setMinWidth(150);
        column.setMaxWidth(150);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        getTableHeader().setReorderingAllowed(false);   
        getTableHeader().setResizingAllowed(false);     
    }

    private class TextAreaCellRenderer extends JTextArea implements TableCellRenderer {

        public TextAreaCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setForeground(Color.BLUE);            
        }        
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? Color.WHITE : new Color(204,255,255));
            setText((String)value);
            setSize(table.getColumnModel().getColumn(column).getWidth(),getPreferredSize().height);
            return this;
        }
    }
    
    private class CellRenderer extends JLabel implements TableCellRenderer {

        private int align;

        public CellRenderer(int align) {
            this.align = align;
            setOpaque(true);            
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? Color.WHITE : new Color(204,255,255));
            if (column==0) {
                setHorizontalAlignment(JLabel.CENTER);                
                setText("");
                setIcon(Order.getIconForState((Integer)value));
            } else {
                setIcon(null);
                setFont(new Font("Arial", 0, 12));
                setForeground(Color.BLUE);
                setText(value==null ? "" : value.toString());            
                setHorizontalAlignment(align);            
            }
            return this;
        }
    }
}