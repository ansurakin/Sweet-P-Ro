package MainPackage;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboboxUsers extends JComboBox {

    public ComboboxUsers() {
        setRenderer(new MyRenderer());
    }

    public int getSelectedID() {
        return this.getSelectedItem()==null || this.getSelectedIndex()==0 ? -1 : (int)((Object[])this.getSelectedItem())[0]; 
    }
    
    public void setSelected(String userName) {
        for (int i = 1;i<this.getItemCount();i++) {
            Object[] obj = (Object[])this.getItemAt(i);
            if (obj[1].equals(userName)) {
                this.setSelectedIndex(i);
                return;
            }
        }
        this.setSelectedIndex(-1);
    } 
    
    public void setSelected(int userID) {
        for (int i = 1;i<this.getItemCount();i++) {
            Object[] obj = (Object[])this.getItemAt(i);
            if (obj[0].equals(userID)) {
                this.setSelectedIndex(i);
                return;
            }
        }
        this.setSelectedIndex(-1);
    }
    
    private class MyRenderer extends JLabel implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            try {
                setText((String)((Object[])value)[1]);
            } catch (Exception ex) {
                setText(" ");
            } 
            return this;
        }
        
    }
}
