package MainPackage;

public class ChooseDeliveryAddressDialog extends javax.swing.JDialog {

    private final DeliveryDialog dd;
    private final MainForm mf;
    private final DATABASE db;
    
    public ChooseDeliveryAddressDialog(DeliveryDialog dd, MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.dd = dd;
        this.mf = mf;
        this.db = db;
    }

    public void initDialog() {
        Object[][] obj = db.SelectSQL("SELECT address FROM client WHERE client.id=? LIMIT 1",new Object[]{mf.Orders.getInt("ID_CLIENT")});
        jButton103.setText(obj[0][0]==null ? "" : (String)obj[0][0]);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel145 = new javax.swing.JPanel();
        jButton103 = new javax.swing.JButton();
        jButton106 = new javax.swing.JButton();

        setTitle("Выбор адреса доставки");

        jPanel145.setBackground(new java.awt.Color(204, 255, 255));
        jPanel145.setLayout(new javax.swing.BoxLayout(jPanel145, javax.swing.BoxLayout.Y_AXIS));

        jButton103.setMaximumSize(new java.awt.Dimension(300, 70));
        jButton103.setMinimumSize(new java.awt.Dimension(300, 70));
        jButton103.setPreferredSize(new java.awt.Dimension(300, 70));
        jButton103.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton103MousePressed(evt);
            }
        });
        jPanel145.add(jButton103);

        jButton106.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton106.setText("Закрыть");
        jButton106.setMaximumSize(new java.awt.Dimension(300, 25));
        jButton106.setMinimumSize(new java.awt.Dimension(300, 25));
        jButton106.setPreferredSize(new java.awt.Dimension(300, 25));
        jButton106.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton106MousePressed(evt);
            }
        });
        jPanel145.add(jButton106);

        getContentPane().add(jPanel145, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton103MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton103MousePressed
        dd.setDeliveryAddress(jButton103.getText());
        this.setVisible(false);
    }//GEN-LAST:event_jButton103MousePressed

    private void jButton106MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton106MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton106MousePressed

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
//            java.util.logging.Logger.getLogger(ChooseDeliveryAddressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ChooseDeliveryAddressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ChooseDeliveryAddressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ChooseDeliveryAddressDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ChooseDeliveryAddressDialog dialog = new ChooseDeliveryAddressDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton103;
    private javax.swing.JButton jButton106;
    private javax.swing.JPanel jPanel145;
    // End of variables declaration//GEN-END:variables
}
