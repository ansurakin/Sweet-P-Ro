package MainPackage;

import static MainPackage.MainForm.FormatUAH;
import static MainPackage.MainForm.LightInterfaceColor;
import java.awt.Color;
import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ChooseConsolidatedOrdersDialog extends javax.swing.JDialog {

    private final MainForm mf;
    private final DATABASE db;
    
    public ChooseConsolidatedOrdersDialog(MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.mf = mf;
        this.db = db;
        
        tableChooseConsolidatedOrders.setShowGrid(true);
        tableChooseConsolidatedOrders.setGridColor(Color.GRAY);
        jScrollPane48.getViewport().setBackground(LightInterfaceColor);             
    }
    
    public void initDialog() {
        if (JOptionPane.showConfirmDialog(null, "Хотите консолидировать список с другими заказами?", "Уточнение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            MakeTableOfChooseConsolidatedOrders();
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
//        } else if (JOptionPane.showConfirmDialog(null, "Печатать лист расхода на весь заказ?", "Уточнение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
//            java.util.List<Integer> list = new LinkedList<>();
//            list.add(mf.Orders.getInt("ID"));
//            printUseList(list);
        } else {
            // --------------------------------
            // add choose dialog for positions
            // --------------------------------
            java.util.List<Integer> list = new LinkedList<>();
            list.add(mf.Orders.getInt("ID"));
            printUseList(list);
        }
    }
    
    private void printUseList(java.util.List<Integer> ordersIDs) {
        StringBuilder ids = new StringBuilder();
        for(int id : ordersIDs) {
            ids.append(Integer.toString(id)).append(",");
        }
        ids.deleteCharAt(ids.length()-1);
        
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(mf.ChoosedDirectory);
        chooser.setDialogTitle("Выберите папку для сохранения файла");
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            mf.ChoosedDirectory = chooser.getCurrentDirectory();
            String filename = chooser.getSelectedFile().getPath();

            ObjectResultSet ListOrderCandies = new ObjectResultSet();
            ListOrderCandies.setColumnNames(new String[]{"NAME", "FACTORY", "AMOUNT", "AMOUNT_IN_BOX"});
//            ListOrderCandies.set(db.SelectSQL("SELECT A.name,factory.name, (SELECT sum(suborder.AMOUNT * gift_candy.amount) FROM gift_candy, suborder WHERE gift_candy.id_candy = A.id AND suborder.id_gift = gift_candy.ID_GIFT AND suborder.id_orders = ? GROUP BY A.id), A.amount_in_box FROM candy A,factory WHERE A.id_factory=factory.id AND A.id IN (SELECT gift_candy.id_candy FROM gift_candy, suborder WHERE suborder.id_gift = gift_candy.ID_GIFT AND suborder.id_orders = ? GROUP BY gift_candy.id_candy)", new Object[]{id_orders, id_orders}));
            ListOrderCandies.set(db.SelectSQL("SELECT A.name,factory.name, (SELECT sum(suborder.AMOUNT * gift_candy.amount) FROM gift_candy, suborder WHERE gift_candy.id_candy = A.id AND suborder.id_gift = gift_candy.ID_GIFT AND suborder.id_orders IN ("+ids+") GROUP BY A.id), A.amount_in_box FROM candy A,factory WHERE A.id_factory=factory.id AND A.id IN (SELECT gift_candy.id_candy FROM gift_candy, suborder WHERE suborder.id_gift = gift_candy.ID_GIFT AND suborder.id_orders IN ("+ids+") GROUP BY gift_candy.id_candy)", new Object[]{}));            
            ObjectResultSet ListOrderPackings = new ObjectResultSet();
            ListOrderPackings.setColumnNames(new String[]{"AMOUNT", "NAME", "NUMBER", "FILENAME"});
//            ListOrderPackings.set(db.SelectSQL("SELECT (SELECT SUM(suborder.AMOUNT) FROM suborder WHERE suborder.id_packing=A.ID AND id_orders = ? GROUP BY A.id),A.name,A.number,A.filename FROM packing A WHERE A.ID IN (SELECT id_packing FROM suborder WHERE id_orders = ? GROUP BY id_packing)", new Object[]{id_orders, id_orders}));
            ListOrderPackings.set(db.SelectSQL("SELECT (SELECT SUM(suborder.AMOUNT) FROM suborder WHERE suborder.id_packing=A.ID AND id_orders IN ("+ids+") GROUP BY A.id),A.name,A.number,A.filename FROM packing A WHERE A.ID IN (SELECT id_packing FROM suborder WHERE id_orders IN ("+ids+") GROUP BY id_packing)", new Object[]{}));            

            String s = "<html><center>Лист расхода<br><br><table border='1'>";
            s = s + "<tr>";
            s = s + "<td width='60' align='center'>Фабрика</td>";
            s = s + "<td width='60' align='center'>Конфета</td>";
            s = s + "<td width='60' align='center'>Кол-во(шт.)</td>";
            s = s + "<td width='60' align='center'>Кол-во(ящ.)</td>";
            s = s + "</tr>";
            for (int i = 0; i < ListOrderCandies.getLength(); i++) {
                s = s + "<tr>";
                s = s + "<td>" + ListOrderCandies.getString(i, "FACTORY") + "</td>";
                s = s + "<td>" + ListOrderCandies.getString(i, "NAME") + "</td>";
                s = s + "<td>" + Long.toString(ListOrderCandies.getBigDecimalAsLong(i, "AMOUNT")) + "</td>";
                s = s + "<td>" + FormatUAH.format(ListOrderCandies.getBigDecimalAsLong(i, "AMOUNT") * 1.0 / ListOrderCandies.getInt(i, "AMOUNT_IN_BOX")) + "</td>";
                s = s + "</tr>";
            }
            s = s + "</table><br><br><br><table border='1'>";
            s = s + "<tr>";
            s = s + "<td width='60' align='center'>Упаковка</td>";
            s = s + "<td width='60' align='center'>Изображение</td>";
            s = s + "<td width='60' align='center'>Кол-во(шт.)</td>";
            s = s + "</tr>";
            for (int i = 0; i < ListOrderPackings.getLength(); i++) {
                s = s + "<tr>";
                s = s + "<td>№" + Integer.toString(ListOrderPackings.getInt(i, "NUMBER")) + " " + ListOrderPackings.getString(i, "NAME") + "</td>";
                s = s + "<td><img src='file://localhost/c:/temp/" + ListOrderPackings.getString(i, "FILENAME") + "' width='150px' height='150px'></td>";
                s = s + "<td>" + Long.toString(ListOrderPackings.getBigDecimalAsLong(i, "AMOUNT")) + "</td>";
                s = s + "</tr>";
            }
            s = s + "</table><br></center></html>";

            try {
                FileWriter fstream = new FileWriter(filename + "\\print_list_packing.html");
                try (BufferedWriter out = new BufferedWriter(fstream)) {
                    out.write(s);
                }
                JOptionPane.showMessageDialog(null, "Файл для печати сохранен в " + filename + "\\print_list_packing.html");
            } catch (IOException | HeadlessException ex) {
                System.out.println(ex.getMessage());
                JOptionPane.showMessageDialog(null, "Не удалось напечатать документ");
            }
        }        
    }    
    
    private void MakeTableOfChooseConsolidatedOrders() {
        String[] columnNames;
        if (mf.areOrdersSortedByDateOfOrder()) {
            columnNames = new String[]{"","Дата заказа", "Клиент","Статус"};
        } else {
            columnNames = new String[]{"","Дата доставки", "Клиент","Статус"};
        }
        Object data[][] = new Object[0][4];
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        if (!mf.Orders.IsNull()) {
            data = new Object[mf.Orders.getLength()][4];
            for (int i = 0; i < mf.Orders.getLength(); i++) {
                data[i][0] = false;
                if (mf.areOrdersSortedByDateOfOrder()) {
                    data[i][1] = sdf.format(new Date(mf.Orders.getLong(i,"DATE_TIME")));
                } else {
                    data[i][1] = sdf.format(new Date(mf.Orders.getInt(i,"MIN_DATE_DELIVERY")*1000L));
                }
                data[i][2] = mf.Orders.getString(i,"CLIENT_NAME");
                data[i][3] = mf.Orders.getInt(i,"STATE");
            }
        }
        tableChooseConsolidatedOrders.makeTable(columnNames, data);
    }    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel190 = new javax.swing.JPanel();
        jScrollPane48 = new javax.swing.JScrollPane();
        tableChooseConsolidatedOrders = new TableChooseConsolidatedOrders();
        jPanel191 = new javax.swing.JPanel();
        jButton124 = new javax.swing.JButton();
        jButton125 = new javax.swing.JButton();

        setTitle("Консолидация листа расходов");
        setAlwaysOnTop(true);
        setResizable(false);

        jPanel190.setBackground(new java.awt.Color(204, 255, 255));
        jPanel190.setLayout(new java.awt.BorderLayout());

        jScrollPane48.setBackground(new java.awt.Color(204, 255, 255));

        tableChooseConsolidatedOrders.setBackground(new java.awt.Color(153, 255, 255));
        tableChooseConsolidatedOrders.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableChooseConsolidatedOrders.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane48.setViewportView(tableChooseConsolidatedOrders);

        jPanel190.add(jScrollPane48, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel190, java.awt.BorderLayout.CENTER);

        jPanel191.setBackground(new java.awt.Color(150, 255, 255));
        jPanel191.setMaximumSize(new java.awt.Dimension(32767, 35));
        jPanel191.setMinimumSize(new java.awt.Dimension(0, 35));
        jPanel191.setPreferredSize(new java.awt.Dimension(400, 35));
        jPanel191.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 5));

        jButton124.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png"))); // NOI18N
        jButton124.setText("готово");
        jButton124.setOpaque(false);
        jButton124.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton124ActionPerformed(evt);
            }
        });
        jPanel191.add(jButton124);

        jButton125.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton125.setText("отмена");
        jButton125.setOpaque(false);
        jButton125.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton125ActionPerformed(evt);
            }
        });
        jPanel191.add(jButton125);

        getContentPane().add(jPanel191, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton124ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton124ActionPerformed
        java.util.List<Integer> list = new LinkedList<>();
        list.add(mf.Orders.getInt("ID"));
        for (int i = 0;i<tableChooseConsolidatedOrders.getRowCount();i++) {
            if ((Boolean)tableChooseConsolidatedOrders.getValueAt(i, 0)) {
                list.add(mf.Orders.getInt(i,"ID"));
            }
        }
        this.setVisible(false);
        printUseList(list);
    }//GEN-LAST:event_jButton124ActionPerformed

    private void jButton125ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton125ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton125ActionPerformed

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ChooseConsolidatedOrdersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ChooseConsolidatedOrdersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ChooseConsolidatedOrdersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ChooseConsolidatedOrdersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ChooseConsolidatedOrdersDialog dialog = new ChooseConsolidatedOrdersDialog(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton124;
    private javax.swing.JButton jButton125;
    private javax.swing.JPanel jPanel190;
    private javax.swing.JPanel jPanel191;
    private javax.swing.JScrollPane jScrollPane48;
    /*
    private javax.swing.JTable tableChooseConsolidatedOrders;
    */
    private TableChooseConsolidatedOrders tableChooseConsolidatedOrders;
    // End of variables declaration//GEN-END:variables
}
