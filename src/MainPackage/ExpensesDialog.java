package MainPackage;

import static MainPackage.MainForm.DarkInterfaceColor;
import static MainPackage.MainForm.FormatUAH;
import static MainPackage.MainForm.LightInterfaceColor;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

public class ExpensesDialog extends javax.swing.JDialog {

    private final MainForm mf;
    private final DATABASE db;
    
    public ExpensesDialog(MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.mf = mf;
        this.db = db;
        
        jDateChooser12.setCalendar(Calendar.getInstance());
        jScrollPane42.getViewport().setBackground(LightInterfaceColor);              
    }
    
    public void initDialog(boolean isThisEditing) {
        if (isThisEditing) {
            int row = mf.getExpensesTableSelectedRow();
            if ((row!=-1) && (row!=mf.getExpensesTableRowCount()-1)) {
                jDateChooser12.setDate(new Date(mf.Expenses.getInt(row,"DATE_TIME")*1000L));
                jTextField46.setText(FormatUAH.format(mf.Expenses.getDouble(row,"COST")));
                jTextArea10.setText(mf.Expenses.getString(row,"NAME"));
                jLabel124.setText("1"); //edit record
                jLabel124.setVisible(false);
                this.pack();
                this.setLocationRelativeTo(null);
                this.setVisible(true);            
            }
        } else {
            jDateChooser12.setCalendar(Calendar.getInstance());
            jTextField46.setText("0");
            jTextArea10.setText("");
            jLabel124.setText("0"); //add new record
            jLabel124.setVisible(false);
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);        
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel180 = new javax.swing.JPanel();
        jLabel121 = new javax.swing.JLabel();
        jDateChooser12 = new com.toedter.calendar.JDateChooser();
        jLabel123 = new javax.swing.JLabel();
        jTextField46 = new javax.swing.JTextField();
        jPanel181 = new javax.swing.JPanel();
        jLabel122 = new javax.swing.JLabel();
        jScrollPane42 = new javax.swing.JScrollPane();
        jTextArea10 = new javax.swing.JTextArea();
        jLabel124 = new javax.swing.JLabel();
        jPanel173 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton109 = new javax.swing.JButton();
        jButton110 = new javax.swing.JButton();

        setTitle("Расходы");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jPanel180.setBackground(new java.awt.Color(204, 255, 255));
        jPanel180.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel180.setMinimumSize(new java.awt.Dimension(118, 32));
        jPanel180.setPreferredSize(new java.awt.Dimension(330, 32));
        jPanel180.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jLabel121.setForeground(new java.awt.Color(0, 0, 255));
        jLabel121.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel121.setText("Дата:");
        jLabel121.setPreferredSize(new java.awt.Dimension(50, 14));
        jPanel180.add(jLabel121);

        jDateChooser12.setBackground(new java.awt.Color(204, 255, 255));
        jDateChooser12.setDateFormatString("dd MMM yyyy");
        jDateChooser12.setPreferredSize(new java.awt.Dimension(140, 24));
        jPanel180.add(jDateChooser12);

        jLabel123.setForeground(new java.awt.Color(0, 0, 255));
        jLabel123.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel123.setText("Сумма:");
        jLabel123.setPreferredSize(new java.awt.Dimension(45, 14));
        jPanel180.add(jLabel123);

        jTextField46.setPreferredSize(new java.awt.Dimension(60, 26));
        jPanel180.add(jTextField46);

        getContentPane().add(jPanel180);

        jPanel181.setBackground(new java.awt.Color(204, 255, 255));
        jPanel181.setMaximumSize(new java.awt.Dimension(32767, 80));
        jPanel181.setMinimumSize(new java.awt.Dimension(78, 80));
        jPanel181.setPreferredSize(new java.awt.Dimension(330, 80));
        jPanel181.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jLabel122.setForeground(new java.awt.Color(0, 0, 255));
        jLabel122.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel122.setText("Расход:");
        jLabel122.setPreferredSize(new java.awt.Dimension(50, 14));
        jPanel181.add(jLabel122);

        jScrollPane42.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane42.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane42.setPreferredSize(new java.awt.Dimension(255, 70));

        jTextArea10.setColumns(20);
        jTextArea10.setRows(5);
        jScrollPane42.setViewportView(jTextArea10);

        jPanel181.add(jScrollPane42);

        jLabel124.setText("0");
        jLabel124.setToolTipText("");
        jPanel181.add(jLabel124);

        getContentPane().add(jPanel181);

        jPanel173.setBackground(new java.awt.Color(153, 255, 255));
        jPanel173.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel173.setMinimumSize(new java.awt.Dimension(113, 32));
        jPanel173.setPreferredSize(new java.awt.Dimension(113, 32));
        jPanel173.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 3));

        jButton109.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png"))); // NOI18N
        jButton109.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton109ActionPerformed(evt);
            }
        });
        jPanel173.add(jButton109);

        jButton110.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton110.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton110ActionPerformed(evt);
            }
        });
        jPanel173.add(jButton110);

        getContentPane().add(jPanel173);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton109ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton109ActionPerformed
        if (jLabel124.getText().equals("0")) { //add new record
            try {
                double cost = Double.parseDouble((jTextField46.getText()).replaceAll(" ", "").replace(',', '.'));
                db.UpdateSQL("INSERT INTO expense(date_time,cost,name) VALUES(?,?,?)",new Object[]{jDateChooser12.getCalendar().getTimeInMillis()/1000,cost,jTextArea10.getText()});
                this.setVisible(false);
                mf.GetExpenses();
                mf.MakeTableOfExpenses();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Неверно введена сумма!");
            }
        } else {
            try {
                int row = mf.getExpensesTableSelectedRow();
                double cost = Double.parseDouble((jTextField46.getText()).replaceAll(" ", "").replace(',', '.'));
                db.UpdateSQL("UPDATE expense SET date_time=?,cost=?,name=? WHERE id=?",new Object[]{jDateChooser12.getCalendar().getTimeInMillis()/1000,cost,jTextArea10.getText(),mf.Expenses.getInt(row,"ID")});
                this.setVisible(false);
                mf.GetExpenses();
                mf.MakeTableOfExpenses();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Неверно введена сумма!");
            }
        }
    }//GEN-LAST:event_jButton109ActionPerformed

    private void jButton110ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton110ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton110ActionPerformed

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
//            java.util.logging.Logger.getLogger(ExpensesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ExpensesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ExpensesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ExpensesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ExpensesDialog dialog = new ExpensesDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton109;
    private javax.swing.JButton jButton110;
    private com.toedter.calendar.JDateChooser jDateChooser12;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    /*
    private javax.swing.JPanel jPanel173;
    */
    private GradientPanel jPanel173;
    private javax.swing.JPanel jPanel180;
    private javax.swing.JPanel jPanel181;
    private javax.swing.JScrollPane jScrollPane42;
    private javax.swing.JTextArea jTextArea10;
    private javax.swing.JTextField jTextField46;
    // End of variables declaration//GEN-END:variables
}
