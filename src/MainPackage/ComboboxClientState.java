package MainPackage;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboboxClientState extends JComboBox {

    private boolean withEmptyValue = false;
            
    public ComboboxClientState(boolean withEmptyValue) {
        this.withEmptyValue = withEmptyValue;
        
        if (withEmptyValue) {
            this.addItem(new ImageIcon("/Images/empty.png"));
        }
        for (ClientState.STATE state : ClientState.STATE.values()) {
            this.addItem(state);
        }
        this.setRenderer(new MyRenderer());
    }
    
    public ClientState.STATE getSelectedState() {
        if (withEmptyValue) {
           return this.getSelectedIndex()==0 ? null : (ClientState.STATE)this.getSelectedItem();
        } else {
            return (ClientState.STATE)this.getSelectedItem();
        }
    }
    
    private class MyRenderer extends JLabel implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            try {
                setIcon(value instanceof ImageIcon ? (ImageIcon)value : ClientState.getIcon((ClientState.STATE)value));
                setHorizontalAlignment(JLabel.CENTER);
                setPreferredSize(new Dimension(30,20));
                setText(" ");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            return this;
        }
        
    }
}
