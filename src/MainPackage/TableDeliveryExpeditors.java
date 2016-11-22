package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TableDeliveryExpeditors extends JTable {

    private ExpeditorsDialog ed;
    private boolean editMode = false;
    
    public void makeTable(Object data[][], ExpeditorsDialog ed, boolean editMode) {

        this.ed = ed;
        
        this.setModel(new DefaultTableModel(data, new String[]{"Дата","Клиент","Адрес","Порядок"}));
        this.setRowHeight(25);
        
        TableColumn column;
        
        column = this.getColumnModel().getColumn(0);
        column.setPreferredWidth(90);
        column.setMinWidth(90);
        column.setMaxWidth(90);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.CENTER));
        
        column = this.getColumnModel().getColumn(1);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));
        
        column = this.getColumnModel().getColumn(2);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new CellRenderer(JLabel.LEFT));
        
        column = this.getColumnModel().getColumn(3);
        column.setPreferredWidth(90);
        column.setMinWidth(90);
        column.setMaxWidth(90);
        column.setHeaderRenderer(new HeaderRenderer());
        column.setCellRenderer(new PanelRenderer());
        column.setCellEditor(new PanelEditor());
        
        if (!editMode) {
            this.removeColumn(column);
        }

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
            setBackground(new Color(204,255,255));
            setText(value==null ? "" : value.toString());            
            return this;
        }
    }    
    
    private class PanelRenderer extends JPanel implements TableCellRenderer {

        private final JButton buttonUp;
        private final JButton buttonDown;
        private final JButton buttonRemove;
        
        public PanelRenderer() { 
            setOpaque(true);
            this.setLayout(new FlowLayout(JLabel.CENTER, 5, 5));
            buttonUp = new JButton();
            buttonDown = new JButton();
            buttonRemove = new JButton();
            buttonUp.setPreferredSize(new Dimension(16,16));
            buttonDown.setPreferredSize(new Dimension(16,16));
            buttonRemove.setPreferredSize(new Dimension(16,16));
            buttonUp.setIcon(new ImageIcon(getClass().getResource("/Images/arrow-up.png")));
            buttonUp.setBorderPainted(false);
            buttonDown.setIcon(new ImageIcon(getClass().getResource("/Images/arrow-down.gif")));
            buttonDown.setBorderPainted(false);
            buttonRemove.setIcon(new ImageIcon(getClass().getResource("/Images/Delete.png")));
            buttonRemove.setBorderPainted(false);
            buttonUp.setOpaque(false);
            buttonDown.setOpaque(false);            
            buttonRemove.setOpaque(false);
            this.add(buttonUp);
            this.add(buttonDown);
            this.add(new Box.Filler(new Dimension(16,16),new Dimension(16,16),new Dimension(16,16)));
            this.add(buttonRemove);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(new Color(204,255,255));
            return this;
        }
        
    }
    
    private class PanelEditor extends JPanel implements TableCellEditor {

        private final JButton buttonUp;
        private final JButton buttonDown;
        private final JButton buttonRemove;
        private int rowNo;
        
        public PanelEditor() { 
            setOpaque(true);
            this.setLayout(new FlowLayout(JLabel.CENTER, 5, 5));
            buttonUp = new JButton();
            buttonDown = new JButton();
            buttonRemove = new JButton();
            buttonUp.setPreferredSize(new Dimension(16,16));
            buttonDown.setPreferredSize(new Dimension(16,16));
            buttonRemove.setPreferredSize(new Dimension(16,16));
            buttonUp.setIcon(new ImageIcon(getClass().getResource("/Images/arrow-up.png")));
            buttonUp.setBorderPainted(false);
            buttonDown.setIcon(new ImageIcon(getClass().getResource("/Images/arrow-down.gif")));
            buttonDown.setBorderPainted(false);
            buttonRemove.setIcon(new ImageIcon(getClass().getResource("/Images/Delete.png")));
            buttonRemove.setBorderPainted(false);
            buttonUp.setOpaque(false);
            buttonDown.setOpaque(false);            
            buttonRemove.setOpaque(false);
            this.add(buttonUp);
            this.add(buttonDown);
            this.add(new Box.Filler(new Dimension(16,16),new Dimension(16,16),new Dimension(16,16)));
            this.add(buttonRemove);
            
            buttonUp.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent me) {
                   ed.rowUp(rowNo);
                }
            });
            buttonDown.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent me) {
                   ed.rowDown(rowNo);
                }
            });
            buttonRemove.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent me) {
                   ed.removeRow(rowNo);
                }
            });
        }        
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            rowNo = row;
            setBackground(new Color(204,255,255));
            return this;
        }

        @Override
        public Object getCellEditorValue() {
            return this;
        }

        @Override
        public boolean isCellEditable(EventObject eo) {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject eo) {
            return false;
        }

        @Override
        public boolean stopCellEditing() {
            return true;
        }

        @Override
        public void cancelCellEditing() {
        }

        @Override
        public void addCellEditorListener(CellEditorListener cl) {
        }

        @Override
        public void removeCellEditorListener(CellEditorListener cl) {
        }
    }
   
     
}