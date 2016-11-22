package MainPackage;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class HeaderRenderer extends JLabel implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(Color.BLUE);
        setOpaque(true);
        setForeground(Color.YELLOW);
        setFont(new Font("Arial", Font.BOLD, 12));
        setHorizontalAlignment(JLabel.CENTER);
        setText(value == null ? "" : value.toString());
        setBorder(BorderFactory.createBevelBorder(0));
        return this;
    }
}
