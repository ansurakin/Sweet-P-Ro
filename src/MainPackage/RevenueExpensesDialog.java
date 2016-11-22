package MainPackage;

import java.awt.Color;
import java.awt.HeadlessException;

public class RevenueExpensesDialog extends javax.swing.JDialog {

    private final DATABASE db;
    private final MainForm mf;
    
    public RevenueExpensesDialog(MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.mf = mf;
        this.db = db;
    }
    
    public void initDialog(boolean thisIsExpenses) {
        this.pack();
        this.setLocationRelativeTo(null);
        if (thisIsExpenses) {
            jLabel64.setText("Расход, единиц:");
            jPanel92.setBackground(Color.BLUE);
            jLabel64.setForeground(Color.red);
            jLabel78.setForeground(Color.red);
            jTextField22.setText("0");
        } else {
            jLabel64.setText("Приход, единиц:");
            jPanel92.setBackground(Color.RED);
            jLabel64.setForeground(Color.BLUE);
            jLabel78.setForeground(Color.BLUE);
            jTextField22.setText("0");
        }
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel92 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jButton47 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jLabel78 = new javax.swing.JLabel();
        jTextField25 = new javax.swing.JTextField();

        setTitle("Приход/расход");
        setResizable(false);

        jPanel92.setBackground(new java.awt.Color(255, 0, 0));
        jPanel92.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel92.setLayout(new java.awt.GridBagLayout());

        jLabel64.setForeground(new java.awt.Color(0, 0, 255));
        jLabel64.setText("Приход, единиц:");
        jPanel92.add(jLabel64, new java.awt.GridBagConstraints());

        jTextField22.setMaximumSize(new java.awt.Dimension(80, 2147483647));
        jTextField22.setMinimumSize(new java.awt.Dimension(80, 24));
        jTextField22.setPreferredSize(new java.awt.Dimension(110, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel92.add(jTextField22, gridBagConstraints);

        jButton47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png"))); // NOI18N
        jButton47.setOpaque(false);
        jButton47.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton47MousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 11, 0, 0);
        jPanel92.add(jButton47, gridBagConstraints);

        jButton48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton48.setOpaque(false);
        jButton48.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton48MousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        jPanel92.add(jButton48, gridBagConstraints);

        jLabel78.setForeground(new java.awt.Color(0, 0, 255));
        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel78.setText("№ накладной:");
        jLabel78.setPreferredSize(new java.awt.Dimension(86, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel92.add(jLabel78, gridBagConstraints);

        jTextField25.setMaximumSize(new java.awt.Dimension(80, 2147483647));
        jTextField25.setMinimumSize(new java.awt.Dimension(80, 24));
        jTextField25.setPreferredSize(new java.awt.Dimension(110, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        jPanel92.add(jTextField25, gridBagConstraints);

        getContentPane().add(jPanel92, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton47MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton47MousePressed
        try {
            boolean AddToStorage = jPanel92.getBackground().equals(Color.RED);
            double value = Double.parseDouble(jTextField22.getText().replaceAll(" ", "").replace(',', '.'));
            String number_factura = jTextField25.getText();
            if ((!number_factura.equals("")) && (value>0)) {
                if (!AddToStorage) {
                    value = -value;
                }
                mf.makeRevenueExpenses(value,number_factura);
                this.setVisible(false);
            }
        } catch (NumberFormatException | HeadlessException ex) {
            this.setVisible(false);
        }
    }//GEN-LAST:event_jButton47MousePressed

    private void jButton48MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton48MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton48MousePressed

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
//            java.util.logging.Logger.getLogger(RevenueExpensesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(RevenueExpensesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(RevenueExpensesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(RevenueExpensesDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                RevenueExpensesDialog dialog = new RevenueExpensesDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JPanel jPanel92;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField25;
    // End of variables declaration//GEN-END:variables
}
