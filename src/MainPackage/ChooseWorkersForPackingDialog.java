package MainPackage;

import static MainPackage.MainForm.DarkInterfaceColor;
import static MainPackage.MainForm.LightInterfaceColor;
import static MainPackage.MainForm.STORAGE_WORKER;
import static MainPackage.MainForm.STORAGE_WORKER_BRIGADIER;
import java.awt.Color;
import java.util.Calendar;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class ChooseWorkersForPackingDialog extends javax.swing.JDialog {

    private final MainForm mf;
    private final DATABASE db;
    
    public ChooseWorkersForPackingDialog(MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.mf = mf;
        this.db = db;
        
        jScrollPane27.getViewport().setBackground(LightInterfaceColor);
        jScrollPane28.getViewport().setBackground(LightInterfaceColor);

        jTree13.setModel(null);
        jTree13.setCellRenderer(new CheckRenderer());
        jTree13.addMouseListener(new NodeSelectionListener(jTree13));
        
        jTable11.setShowGrid(true);
        jTable11.setGridColor(Color.GRAY);                    

    }

    public void initDialog() {
        CheckNode top = new CheckNode(new Object[]{"", 0});
        for (int i = 0; i < mf.Users.getLength(); i++) {
            if (mf.Users.getInt(i,"LEVEL")==STORAGE_WORKER && mf.Users.getInt(i,"ID_POSITION")==STORAGE_WORKER_BRIGADIER) {
                CheckNode parent = new CheckNode(new Object[]{mf.Users.getString(i, "NAME"), i}, true, false);
                int workerId = mf.Users.getInt(i, "ID");
                for (int k = 0; k < mf.Users.getLength(); k++) {
                    if (mf.Users.getInt(k,"ID_USER_BOSS")==workerId) { 
                        CheckNode child = new CheckNode(new Object[]{mf.Users.getString(k, "NAME"), k}, false, false);
                        parent.add(child); 
                    } 
                }
                top.add(parent);                
            }
        }
        
        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree13.setModel(treeModel);
        Object[][] data = new Object[mf.SubOrders.getLength()][3];
        for (int i = 0; i < mf.SubOrders.getLength(); i++) {
            data[i][0] = mf.SubOrders.getString(i, "GIFT_NAME");
            data[i][1] = Integer.toString(mf.SubOrders.getInt(i, "PACKED")) + "  из  " + Integer.toString(mf.SubOrders.getInt(i, "AMOUNT"));
            data[i][2] = 0;
        }

        jTable11.makeTable(data);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);     
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel53 = new javax.swing.JPanel();
        jScrollPane27 = new javax.swing.JScrollPane();
        jTree13 = new CheckBoxTree();
        jScrollPane28 = new javax.swing.JScrollPane();
        jTable11 = new TableOrderPacked();
        jPanel106 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton76 = new javax.swing.JButton();
        jButton77 = new javax.swing.JButton();

        setTitle("Выбор паковщиков");

        jPanel53.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel53.setMaximumSize(new java.awt.Dimension(350, 400));
        jPanel53.setMinimumSize(new java.awt.Dimension(350, 400));
        jPanel53.setPreferredSize(new java.awt.Dimension(350, 400));
        jPanel53.setLayout(new javax.swing.BoxLayout(jPanel53, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane27.setMaximumSize(new java.awt.Dimension(1000, 200));
        jScrollPane27.setMinimumSize(new java.awt.Dimension(19, 200));
        jScrollPane27.setPreferredSize(new java.awt.Dimension(19, 200));

        jTree13.setBackground(new java.awt.Color(204, 255, 255));
        jTree13.setForeground(new java.awt.Color(255, 255, 0));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree13.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree13.setRootVisible(false);
        jTree13.setShowsRootHandles(true);
        jScrollPane27.setViewportView(jTree13);

        jPanel53.add(jScrollPane27);

        jTable11.setBackground(new java.awt.Color(204, 255, 255));
        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable11.setSelectionBackground(new java.awt.Color(153, 255, 255));
        jScrollPane28.setViewportView(jTable11);

        jPanel53.add(jScrollPane28);

        jPanel106.setBackground(new java.awt.Color(153, 255, 255));
        jPanel106.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel106.setMinimumSize(new java.awt.Dimension(113, 32));
        jPanel106.setPreferredSize(new java.awt.Dimension(113, 32));
        jPanel106.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 3));

        jButton76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Save-icon.png"))); // NOI18N
        jButton76.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton76MousePressed(evt);
            }
        });
        jPanel106.add(jButton76);

        jButton77.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton77.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton77MousePressed(evt);
            }
        });
        jPanel106.add(jButton77);

        jPanel53.add(jPanel106);

        getContentPane().add(jPanel53, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton76MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton76MousePressed
        for (int i=0;i<mf.SubOrders.getLength();i++) {
            if ((Integer)jTable11.getValueAt(i, 2)>mf.SubOrders.getInt(i,"AMOUNT")-mf.SubOrders.getInt(i,"PACKED")) {
                JOptionPane.showMessageDialog(null, "Указано большее количество упакованного товара, чем необходимо для заказа!");
                return;
            }
        }
        String s = "(";
        int count = 0;
        for (int i = 0; i < jTree13.getRowCount(); i++) {
            TreePath tp = jTree13.getPathForRow(i);
            CheckNode node = (CheckNode) tp.getLastPathComponent();
            if (node.isSelected()) {
                Object[] obj = (Object[]) node.getUserObject();
                s = s + mf.Users.getInt((Integer) obj[1], "ID") + ",";
                count++;
            }
        }
        if (count==0) {
            JOptionPane.showMessageDialog(null, "Не выбран ни один паковщик");
            return;
        }
        s = s.substring(0, s.length() - 1) + ")";

        int date = ((Long)(Calendar.getInstance().getTimeInMillis()/1000)).intValue();
        int id_orders = mf.Orders.getInt("ID");

        double summ_cost = 0;
        for (int i=0;i<mf.SubOrders.getLength();i++) {
            summ_cost += mf.SubOrders.getDouble(i,"GIFT_COST_PACKING")*(Integer)jTable11.getValueAt(i, 2);
        }

        boolean result = db.DoSQL("START TRANSACTION");
        if (result) {
            result = db.UpdateSQL("INSERT INTO user_pack_orders(date_time,id_user,id_orders,cost) SELECT ?,id,?,? FROM user WHERE id IN " + s, new Object[]{date, id_orders, summ_cost/count});
            if (result) {
                for (int i=0;i<mf.SubOrders.getLength();i++) {
                    int amount = (Integer)jTable11.getValueAt(i, 2);
                    if (amount>0) {
                        int id_suborder = mf.SubOrders.getInt(i,"ID");
                        result = result && db.UpdateSQL("UPDATE suborder SET packed=packed+? WHERE id=?",new Object[]{amount,id_suborder});
                        result = result && db.UpdateSQL("UPDATE candy, gift_candy, suborder SET candy.reserved = candy.reserved - ?*gift_candy.amount,candy.storage = candy.storage - ?*gift_candy.amount WHERE gift_candy.ID_CANDY = candy.ID AND gift_candy.ID_GIFT=suborder.ID_GIFT AND suborder.ID=?",new Object[]{amount,amount,id_suborder});
                        result = result && db.UpdateSQL("UPDATE packing SET reserved = reserved - ?, storage = storage-? WHERE id=?", new Object[]{amount,amount,mf.SubOrders.getInt(i,"ID_PACKING")});
                        result = result && db.UpdateSQL("INSERT INTO storage_orders(type,id_object,date_time,amount,id_orders) (SELECT ?, gift_candy.id_candy,?, - (? * gift_candy.amount),? FROM gift_candy, suborder WHERE suborder.id_gift = gift_candy.ID_GIFT AND suborder.id=? GROUP BY gift_candy.id_candy)", new Object[]{1, date, amount, id_orders, id_suborder});
                        result = result && db.UpdateSQL("INSERT INTO storage_orders(type,id_object,date_time,amount,id_orders) (SELECT ?, id_packing,?, - (?),? FROM suborder WHERE id = ? GROUP BY id_packing)", new Object[]{2, date, amount, id_orders, id_suborder});
                    }
                }
            }
        }
        if (result) {
            db.DoSQL("COMMIT");
            mf.GetOrders();
            mf.MakeTableOfOrders();
            this.setVisible(false);
        } else {
            db.DoSQL("ROLLBACK");
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }

    }//GEN-LAST:event_jButton76MousePressed

    private void jButton77MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton77MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton77MousePressed

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
//            java.util.logging.Logger.getLogger(ChooseWorkersForPackingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ChooseWorkersForPackingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ChooseWorkersForPackingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ChooseWorkersForPackingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ChooseWorkersForPackingDialog dialog = new ChooseWorkersForPackingDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton76;
    private javax.swing.JButton jButton77;
    /*
    private javax.swing.JPanel jPanel106;
    */
    private GradientPanel jPanel106;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    /*
    private javax.swing.JTable jTable11;
    */
    private TableOrderPacked jTable11;
    /*
    private javax.swing.JTree jTree13;
    */
    private CheckBoxTree jTree13;
    // End of variables declaration//GEN-END:variables
}
