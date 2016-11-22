package MainPackage;

public class ContactChooseDialog extends javax.swing.JDialog {

    private final DeliveryDialog dd;
    private final DATABASE db;
    private final MainForm mf;
    
    public ContactChooseDialog(DeliveryDialog dd, MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.dd = dd;
        this.db = db;
        this.mf = mf;
    }
    
    public void initDialog() {
        Object[][] obj = db.SelectSQL("SELECT contact1,phone1,contact2,phone2,contact3,phone3 FROM client WHERE client.id=? LIMIT 1",new Object[]{mf.Orders.getInt("ID_CLIENT")});
        String n1 = obj[0][0]==null ? "" : (String)obj[0][0];
        String p1 = obj[0][1]==null ? "" : (String)obj[0][1];
        String n2 = obj[0][2]==null ? "" : (String)obj[0][2];
        String p2 = obj[0][3]==null ? "" : (String)obj[0][3];
        String n3 = obj[0][4]==null ? "" : (String)obj[0][4];
        String p3 = obj[0][5]==null ? "" : (String)obj[0][5];

        jButton98.setText("<html>"+n1+"<br>"+p1+"</html>");
        jButton99.setText("<html>"+n2+"<br>"+p2+"</html>");
        jButton101.setText("<html>"+n3+"<br>"+p3+"</html>");

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel144 = new javax.swing.JPanel();
        jButton98 = new javax.swing.JButton();
        jButton99 = new javax.swing.JButton();
        jButton101 = new javax.swing.JButton();
        jButton102 = new javax.swing.JButton();

        setTitle("Выбор контакта");

        jPanel144.setBackground(new java.awt.Color(204, 255, 255));
        jPanel144.setLayout(new javax.swing.BoxLayout(jPanel144, javax.swing.BoxLayout.Y_AXIS));

        jButton98.setMaximumSize(new java.awt.Dimension(300, 70));
        jButton98.setMinimumSize(new java.awt.Dimension(300, 70));
        jButton98.setPreferredSize(new java.awt.Dimension(300, 70));
        jButton98.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton98MousePressed(evt);
            }
        });
        jPanel144.add(jButton98);

        jButton99.setMaximumSize(new java.awt.Dimension(300, 70));
        jButton99.setMinimumSize(new java.awt.Dimension(300, 70));
        jButton99.setPreferredSize(new java.awt.Dimension(300, 70));
        jButton99.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton99MousePressed(evt);
            }
        });
        jPanel144.add(jButton99);

        jButton101.setMaximumSize(new java.awt.Dimension(300, 70));
        jButton101.setMinimumSize(new java.awt.Dimension(300, 70));
        jButton101.setPreferredSize(new java.awt.Dimension(300, 70));
        jButton101.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton101MousePressed(evt);
            }
        });
        jPanel144.add(jButton101);

        jButton102.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton102.setText("Закрыть");
        jButton102.setMaximumSize(new java.awt.Dimension(300, 25));
        jButton102.setMinimumSize(new java.awt.Dimension(300, 25));
        jButton102.setPreferredSize(new java.awt.Dimension(300, 25));
        jButton102.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton102MousePressed(evt);
            }
        });
        jPanel144.add(jButton102);

        getContentPane().add(jPanel144, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton98MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton98MousePressed
        String s = jButton98.getText();
        dd.setContactPerson(s);
        this.setVisible(false);
    }//GEN-LAST:event_jButton98MousePressed

    private void jButton99MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton99MousePressed
        String s = jButton99.getText();
        dd.setContactPerson(s);
        this.setVisible(false);
    }//GEN-LAST:event_jButton99MousePressed

    private void jButton101MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton101MousePressed
        String s = jButton101.getText();
        dd.setContactPerson(s);
        this.setVisible(false);
    }//GEN-LAST:event_jButton101MousePressed

    private void jButton102MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton102MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton102MousePressed

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
//            java.util.logging.Logger.getLogger(ContactChooseDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ContactChooseDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ContactChooseDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ContactChooseDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ContactChooseDialog dialog = new ContactChooseDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton101;
    private javax.swing.JButton jButton102;
    private javax.swing.JButton jButton98;
    private javax.swing.JButton jButton99;
    private javax.swing.JPanel jPanel144;
    // End of variables declaration//GEN-END:variables
}
