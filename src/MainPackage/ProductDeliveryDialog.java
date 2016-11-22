package MainPackage;

import static MainPackage.MainForm.DarkInterfaceColor;
import static MainPackage.MainForm.LightInterfaceColor;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class ProductDeliveryDialog extends javax.swing.JDialog {

    private final MainForm mf;
    private final DATABASE db;
    
    public ProductDeliveryDialog(MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.mf = mf;
        this.db = db;
        
        jTree14.setModel(null);
        jTree14.setCellRenderer(new CheckRenderer());
        jTree14.addMouseListener(new NodeSelectionListener(jTree14));

    }
    
    public void initDialog() {
        CheckNode top = new CheckNode(new Object[]{"", 0});
        for (int i = 0; i < mf.Users.getLength(); i++) {
            if (mf.Users.getInt(i, "LEVEL") == 4) {
                top.add(new CheckNode(new Object[]{mf.Users.getString(i, "NAME"), i}, false, false));
            }
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree14.setModel(treeModel);
        jTextField24.setText("0");
        jTextField35.setText("0");
        jCheckBox2.setSelected(false);
        jTextField43.setText("0");
        jTextField43.setEnabled(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel100 = new javax.swing.JPanel();
        jScrollPane30 = new javax.swing.JScrollPane();
        jTree14 = new CheckBoxTree();
        jPanel30 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jPanel152 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jPanel133 = new javax.swing.JPanel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField43 = new javax.swing.JTextField();
        jPanel127 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton79 = new javax.swing.JButton();
        jButton83 = new javax.swing.JButton();

        setTitle("Доставка товара");

        jPanel100.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel100.setLayout(new javax.swing.BoxLayout(jPanel100, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane30.setMaximumSize(new java.awt.Dimension(300, 32767));
        jScrollPane30.setMinimumSize(new java.awt.Dimension(300, 23));
        jScrollPane30.setPreferredSize(new java.awt.Dimension(300, 275));

        jTree14.setBackground(new java.awt.Color(204, 255, 255));
        jTree14.setForeground(new java.awt.Color(255, 255, 0));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree14.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree14.setRootVisible(false);
        jTree14.setShowsRootHandles(true);
        jScrollPane30.setViewportView(jTree14);

        jPanel100.add(jScrollPane30);

        jPanel30.setBackground(new java.awt.Color(204, 255, 255));
        jPanel30.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jLabel10.setForeground(new java.awt.Color(0, 0, 255));
        jLabel10.setText("Зарплата работникам:");
        jLabel10.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel30.add(jLabel10);

        jTextField24.setMaximumSize(new java.awt.Dimension(60, 2147483647));
        jTextField24.setMinimumSize(new java.awt.Dimension(60, 20));
        jTextField24.setPreferredSize(new java.awt.Dimension(60, 24));
        jPanel30.add(jTextField24);

        jPanel100.add(jPanel30);

        jPanel152.setBackground(new java.awt.Color(204, 255, 255));
        jPanel152.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel152.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jLabel68.setForeground(new java.awt.Color(0, 0, 255));
        jLabel68.setText("Транспортные расходы:");
        jLabel68.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel152.add(jLabel68);

        jTextField35.setMaximumSize(new java.awt.Dimension(60, 2147483647));
        jTextField35.setMinimumSize(new java.awt.Dimension(60, 20));
        jTextField35.setPreferredSize(new java.awt.Dimension(60, 24));
        jPanel152.add(jTextField35);

        jPanel100.add(jPanel152);

        jPanel133.setBackground(new java.awt.Color(204, 255, 255));
        jPanel133.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel133.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jCheckBox2.setForeground(new java.awt.Color(0, 0, 255));
        jCheckBox2.setText("курьерская компания");
        jCheckBox2.setOpaque(false);
        jCheckBox2.setPreferredSize(new java.awt.Dimension(150, 23));
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jPanel133.add(jCheckBox2);

        jTextField43.setMaximumSize(new java.awt.Dimension(60, 2147483647));
        jTextField43.setMinimumSize(new java.awt.Dimension(60, 20));
        jTextField43.setPreferredSize(new java.awt.Dimension(60, 24));
        jPanel133.add(jTextField43);

        jPanel100.add(jPanel133);

        jPanel127.setBackground(new java.awt.Color(153, 255, 255));

        jButton79.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png"))); // NOI18N
        jButton79.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton79MousePressed(evt);
            }
        });
        jPanel127.add(jButton79);

        jButton83.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton83.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton83MousePressed(evt);
            }
        });
        jPanel127.add(jButton83);

        jPanel100.add(jPanel127);

        getContentPane().add(jPanel100, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        jTextField43.setEnabled(jCheckBox2.isSelected());
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jButton79MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton79MousePressed
        int row = mf.getDeliveryTableSelectedRow();
        int state = mf.Delivery.getInt(row,"STATE");
        int id_orders = mf.Delivery.getInt(row,"ID_ORDERS");
        int id_delivery = mf.Delivery.getInt(row,"ID");

        int date = (int)(System.currentTimeMillis()/1000);
        double payment_workers;
        double cost_transport;
        double cost_courier = 0d;
        try {
            payment_workers = Double.parseDouble(jTextField24.getText().replaceAll(" ", "").replace(',', '.'));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Неверно введена зарплата работникам!");
            return;
        }
        try {
            cost_transport = Double.parseDouble(jTextField35.getText().replaceAll(" ", "").replace(',', '.'));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Неверно введены транспортные расходы!");
            return;
        }
        if (jCheckBox2.isSelected()) {
            try {
                cost_courier = Double.parseDouble(jTextField43.getText().replaceAll(" ", "").replace(',', '.'));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Неверно введена оплата курьерской компании!");
                return;
            }
        }

        String s = "(";
        int count_workers = 0;
        for (int i = 0; i < jTree14.getRowCount(); i++) {
            TreePath tp = jTree14.getPathForRow(i);
            CheckNode node = (CheckNode) tp.getLastPathComponent();
            if (node.isSelected()) {
                Object[] obj = (Object[]) node.getUserObject();
                s = s + mf.Users.getInt((Integer) obj[1], "ID") + ",";
                count_workers++;
            }
        }
        if ((count_workers == 0) && (!jCheckBox2.isSelected())) {
            JOptionPane.showMessageDialog(null, "Нужно выбрать хотя бы одного работника или курьерскую службу!");
            return;
        }
        s = s.substring(0, s.length() - 1) + ")";

        boolean result = db.DoSQL("START TRANSACTION");
        if (result) {
            if (count_workers!=0) {
                result = db.UpdateSQL("INSERT INTO user_delivery_orders(date_time,id_user,id_orders,cost) SELECT ?,id,?,? FROM user WHERE id IN " + s, new Object[]{date, id_orders, payment_workers / count_workers});
            }
            if (result) {
                result = db.UpdateSQL("UPDATE delivery SET cost=?,payment=?,courier=?,date_send=?,state=? WHERE id=?",new Object[]{cost_transport,payment_workers,cost_courier,date,state+1,id_delivery});
                Object[][] obj = db.SelectSQL("SELECT count(id) FROM delivery WHERE state<? AND id_orders=?",new Object[]{Order.ORDER_SEND,id_orders});
                if ((Long)obj[0][0]==0) {
                    result = db.UpdateSQL("UPDATE orders SET state=? WHERE id=? LIMIT 1",new Object[]{Order.ORDER_SEND,id_orders});
                }
            }
        }

        if (result) {
            db.DoSQL("COMMIT");
            mf.GetDelivery();
            mf.MakeTableOfDelivery();
            this.setVisible(false);
        } else {
            db.DoSQL("ROLLBACK");
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }
    }//GEN-LAST:event_jButton79MousePressed

    private void jButton83MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton83MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton83MousePressed

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
//            java.util.logging.Logger.getLogger(ProductDeliveryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ProductDeliveryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ProductDeliveryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ProductDeliveryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ProductDeliveryDialog dialog = new ProductDeliveryDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton79;
    private javax.swing.JButton jButton83;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JPanel jPanel100;
    /*
    private javax.swing.JPanel jPanel127;
    */
    private GradientPanel jPanel127;
    private javax.swing.JPanel jPanel133;
    private javax.swing.JPanel jPanel152;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField43;
    /*
    private javax.swing.JTree jTree14;
    */
    private CheckBoxTree jTree14;
    // End of variables declaration//GEN-END:variables
}
