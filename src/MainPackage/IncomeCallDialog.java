package MainPackage;

public class IncomeCallDialog extends javax.swing.JDialog {

    private int clientID;
    private final MainForm mf;
    private final DATABASE db;
    private final ObjectResultSet incomeCallClient = new ObjectResultSet();
    private boolean phoneIsRecognized = false;
    
    public IncomeCallDialog(MainForm mf,DATABASE db) {
        super();
        initComponents();
        
        this.mf = mf;
        this.db = db;
        
        incomeCallClient.setColumnNames(new String[]{"ID","OFFICIAL_NAME","CONTACT1","CONTACT2","CONTACT3","PHONE1","PHONE2","PHONE3"});
    }
    
    public void initDialog(String incomingPhone) {
        phoneIsRecognized = false;
        jLabel2.setText(incomingPhone);
        
        String sql = "SELECT id,official_name,contact1,contact2,contact3,"+
                     "replace(replace(replace(replace(phone1,' ',''),'(',''),')',''),'-','') as p1,"+
                     "replace(replace(replace(replace(phone2,' ',''),'(',''),')',''),'-','') as p2,"+
                     "replace(replace(replace(replace(phone3,' ',''),'(',''),')',''),'-','') as p3 "+
                     " FROM client WHERE "+
                     "replace(replace(replace(replace(phone1,' ',''),'(',''),')',''),'-','') LIKE '%" + incomingPhone + "%' OR "+
                     "replace(replace(replace(replace(phone2,' ',''),'(',''),')',''),'-','') LIKE '%" + incomingPhone + "%' OR "+
                     "replace(replace(replace(replace(phone3,' ',''),'(',''),')',''),'-','') LIKE '%" + incomingPhone + "%' LIMIT 1";
        incomeCallClient.set(db.SelectSQL(sql, new Object[]{}));
        
        if (incomeCallClient.IsNull() || incomeCallClient.getLength()==0) {
            jLabel3.setText("неизвестный номер");
            jLabel4.setText("");
            clientID = -1;
        } else {
            incomeCallClient.setPosition(0);
            clientID = incomeCallClient.getInt("ID");
            jLabel3.setText(incomeCallClient.get("OFFICIAL_NAME")==null ? "" : incomeCallClient.getString("OFFICIAL_NAME"));
            String phone1 = incomeCallClient.get("PHONE1")==null ? "" : incomeCallClient.getString("PHONE1");
            String phone2 = incomeCallClient.get("PHONE2")==null ? "" : incomeCallClient.getString("PHONE2");
            String phone3 = incomeCallClient.get("PHONE3")==null ? "" : incomeCallClient.getString("PHONE3");
            String clientName1 = incomeCallClient.get("CONTACT1")==null ? "" : incomeCallClient.getString("CONTACT1");
            String clientName2 = incomeCallClient.get("CONTACT2")==null ? "" : incomeCallClient.getString("CONTACT2");
            String clientName3 = incomeCallClient.get("CONTACT3")==null ? "" : incomeCallClient.getString("CONTACT3");
            jLabel4.setText(phone1.contains(incomingPhone) ? clientName1 : (phone2.contains(incomingPhone) ? clientName2 : clientName3));
            phoneIsRecognized = true;
        }
        this.pack();
        this.setLocationRelativeTo(null);
        this.setLocation(mf.getWidth()-this.getWidth(), mf.getHeight()-this.getHeight());
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(255, 0, 0));
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 51, 51), new java.awt.Color(153, 0, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 100));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/phone.GIF"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
        });
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(260, 100));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("01234567890");
        jLabel2.setPreferredSize(new java.awt.Dimension(150, 24));
        jPanel2.add(jLabel2);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("ООО Тест");
        jLabel3.setPreferredSize(new java.awt.Dimension(250, 24));
        jPanel2.add(jLabel3);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Островерхов Николай Петрович");
        jLabel4.setPreferredSize(new java.awt.Dimension(250, 24));
        jPanel2.add(jLabel4);

        getContentPane().add(jPanel2, java.awt.BorderLayout.EAST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        if (phoneIsRecognized) {
            mf.showClientWhichIsCalling(clientID);
        }
    }//GEN-LAST:event_jLabel1MousePressed


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
//            java.util.logging.Logger.getLogger(IncomeCallDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(IncomeCallDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(IncomeCallDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(IncomeCallDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                IncomeCallDialog dialog = new IncomeCallDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
