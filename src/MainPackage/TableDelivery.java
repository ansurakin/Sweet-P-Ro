package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableDelivery extends JTable {

    
    public void makeTable(Object data[][]) {

        this.setModel(new DefaultTableModel(data, new String[]{"№","Статус", "Дата", "Экипаж", "ТТН",
            "Клиент","Контакт","Тип доставки","Адрес","Состав доставки","Кто платит","Долг","Презент","Комментарий","Менеджер"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
               switch (column) {
                   case 4:
                       return true;
                   case 13:
                       return true;
                   default:
                       return false;
               }   
            }
        });
        
        this.setRowHeight(80);

        TableColumn column;
        
        column = this.getColumnModel().getColumn(0);
        column.setPreferredWidth(25);
        column.setMinWidth(25);
        column.setMaxWidth(25);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
        column = this.getColumnModel().getColumn(1);
        column.setPreferredWidth(50);
        column.setMinWidth(50);
        column.setMaxWidth(50);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer2());
        
        column = this.getColumnModel().getColumn(2);
        column.setPreferredWidth(90);
        column.setMinWidth(90);
        column.setMaxWidth(90);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
        column = this.getColumnModel().getColumn(3);
        column.setPreferredWidth(90);
        column.setMinWidth(90);
        column.setMaxWidth(90);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));
        
        column = this.getColumnModel().getColumn(4);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(5);
        column.setPreferredWidth(120);
        column.setMinWidth(120);
        column.setMaxWidth(120);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(6);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());

        column = this.getColumnModel().getColumn(7);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(8);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(9);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(10);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(11);
        column.setPreferredWidth(85);
        column.setMinWidth(85);
        column.setMaxWidth(85);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));        
        
        column = this.getColumnModel().getColumn(12);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(13);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());
        
        column = this.getColumnModel().getColumn(14);
        column.setPreferredWidth(70);
        column.setMinWidth(70);
        column.setMaxWidth(70);        
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new TextAreaCellRenderer());        
        
        getTableHeader().setReorderingAllowed(false);   
        getTableHeader().setResizingAllowed(false);        
    }

    private class CellRenderer extends JLabel implements TableCellRenderer {

        public CellRenderer(int align) {
            setFont(new Font("Arial", 0, 12));
            setOpaque(true);
            setForeground(Color.BLUE);
            setHorizontalAlignment(align);            
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? new Color(255,255,255) : new Color(204,255,255));
            setText(value==null ? "" : value.toString());            
            return this;
        }
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
            if (isSelected) {
                setBackground(new Color(255,255,255));
            } else {
                setBackground(new Color(204,255,255));
            }
            setText(value==null ? "" : value.toString());
            setSize(table.getColumnModel().getColumn(column).getWidth(),getPreferredSize().height);
            return this;
        }
    }
    
    private class CellRenderer2 extends JLabel implements TableCellRenderer {

        public CellRenderer2() {
            setText("");
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);            
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? Color.WHITE : new Color(204,255,255));
            setIcon(Order.getIconForState((Integer)value));
            return this;
        }
    }    
    
     
}