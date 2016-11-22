package MainPackage;

import static MainPackage.MainForm.LightInterfaceColor;
import java.awt.Color;

public class ChooseDeliveryContentsDialog extends javax.swing.JDialog {

    private final DeliveryDialog dd;
    private final MainForm mf;
    
    public ChooseDeliveryContentsDialog(DeliveryDialog dd,MainForm mf) {
        super();
        initComponents();
        
        this.dd = dd;
        this.mf = mf;
        
        jTable15.setShowGrid(true);
        jTable15.setGridColor(Color.GRAY);
        jScrollPane37.getViewport().setBackground(LightInterfaceColor);
    }
    
    public void initDialog() {
        MakeTableOfSubordersForSelectionInDelivery();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);        
    }
    
    private void MakeTableOfSubordersForSelectionInDelivery() {
        Object data[][] = new Object[0][3];
        if (!mf.SubOrders.IsNull()) {
            data = new Object[mf.SubOrders.getLength()][3];
            for (int i = 0; i < mf.SubOrders.getLength(); i++) {
                data[i][0] = mf.SubOrders.getString(i, "GIFT_NAME");
                data[i][1] = mf.SubOrders.getString(i, "PACKING_NAME") + "  №" + mf.SubOrders.getInt(i, "PACKING_NUMBER");
                data[i][2] = mf.SubOrders.getInt(i, "AMOUNT");
            }
        }
        jTable15.makeTable(data);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel146 = new javax.swing.JPanel();
        jScrollPane37 = new javax.swing.JScrollPane();
        jTable15 = new TableSubordersForSelectInDelivery();
        jPanel187 = new javax.swing.JPanel();
        jButton120 = new javax.swing.JButton();
        jButton121 = new javax.swing.JButton();
        jButton107 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel146.setBackground(new java.awt.Color(204, 255, 255));
        jPanel146.setPreferredSize(new java.awt.Dimension(450, 310));
        jPanel146.setLayout(new javax.swing.BoxLayout(jPanel146, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane37.setPreferredSize(new java.awt.Dimension(350, 275));

        jTable15.setBackground(new java.awt.Color(204, 255, 255));
        jTable15.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable15.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jTable15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable15MouseClicked(evt);
            }
        });
        jScrollPane37.setViewportView(jTable15);

        jPanel146.add(jScrollPane37);

        jPanel187.setBackground(new java.awt.Color(204, 255, 255));
        jPanel187.setPreferredSize(new java.awt.Dimension(450, 35));

        jButton120.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png"))); // NOI18N
        jButton120.setText("Добавить");
        jButton120.setMaximumSize(new java.awt.Dimension(120, 25));
        jButton120.setMinimumSize(new java.awt.Dimension(120, 25));
        jButton120.setPreferredSize(new java.awt.Dimension(120, 25));
        jButton120.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton120MousePressed(evt);
            }
        });
        jPanel187.add(jButton120);

        jButton121.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Add-icon.png"))); // NOI18N
        jButton121.setText("Добавить все");
        jButton121.setMaximumSize(new java.awt.Dimension(125, 25));
        jButton121.setMinimumSize(new java.awt.Dimension(125, 25));
        jButton121.setPreferredSize(new java.awt.Dimension(125, 25));
        jButton121.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton121MousePressed(evt);
            }
        });
        jPanel187.add(jButton121);

        jButton107.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton107.setText("Закрыть");
        jButton107.setMaximumSize(new java.awt.Dimension(120, 25));
        jButton107.setMinimumSize(new java.awt.Dimension(120, 25));
        jButton107.setPreferredSize(new java.awt.Dimension(120, 25));
        jButton107.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton107MousePressed(evt);
            }
        });
        jPanel187.add(jButton107);

        jPanel146.add(jPanel187);

        getContentPane().add(jPanel146, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable15MouseClicked

    }//GEN-LAST:event_jTable15MouseClicked

    private void jButton120MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton120MousePressed
        int row = jTable15.getSelectedRow();
        if (row!=-1) {
            dd.addContents(jTable15.getValueAt(row, 0)+"  "+jTable15.getValueAt(row, 1)+"  Х "+jTable15.getValueAt(row, 2)+"шт \n");
            this.setVisible(false);
        }
    }//GEN-LAST:event_jButton120MousePressed

    private void jButton107MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton107MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton107MousePressed

    private void jButton121MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton121MousePressed
        int rowCount = jTable15.getRowCount();
        for (int i=0; i<rowCount; i++) {
            dd.addContents(jTable15.getValueAt(i, 0)+"  "+jTable15.getValueAt(i, 1)+"  Х "+jTable15.getValueAt(i, 2)+"шт \n");
        }
        this.setVisible(false);
    }//GEN-LAST:event_jButton121MousePressed

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
//            java.util.logging.Logger.getLogger(ChooseDeliveryContentsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ChooseDeliveryContentsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ChooseDeliveryContentsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ChooseDeliveryContentsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ChooseDeliveryContentsDialog dialog = new ChooseDeliveryContentsDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton107;
    private javax.swing.JButton jButton120;
    private javax.swing.JButton jButton121;
    private javax.swing.JPanel jPanel146;
    private javax.swing.JPanel jPanel187;
    private javax.swing.JScrollPane jScrollPane37;
    /*
    private javax.swing.JTable jTable15;
    */
    private TableSubordersForSelectInDelivery jTable15;
    // End of variables declaration//GEN-END:variables
}
