package MainPackage;

import static MainPackage.MainForm.DarkInterfaceColor;
import static MainPackage.MainForm.FormatKG;
import static MainPackage.MainForm.LightInterfaceColor;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class ChoosePackingWorkersDialog extends javax.swing.JDialog {

    private final MainForm mf;
    private final DATABASE db;
    
    public ChoosePackingWorkersDialog(MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.mf = mf;
        this.db = db;
        
        jTree12.setModel(null);
        jTree12.setCellRenderer(new CheckRenderer());
        jTree12.addMouseListener(new NodeSelectionListener(jTree12));
    }

    public void initDialog(ObjectResultSet Users) {
        CheckNode top = new CheckNode(new Object[]{"", 0});
        for (int i = 0; i < Users.getLength(); i++) {
            if (Users.getInt(i, "LEVEL") == 4) {
                top.add(new CheckNode(new Object[]{Users.getString(i, "NAME"), i}, false, false));
            }
        }       
        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree12.setModel(treeModel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel34 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTree12 = new CheckBoxTree();
        jPanel97 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton53 = new javax.swing.JButton();
        jButton54 = new javax.swing.JButton();

        setTitle("Выбор паковщиков");

        jPanel34.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel34.setLayout(new java.awt.BorderLayout());

        jScrollPane20.setMaximumSize(new java.awt.Dimension(250, 32767));
        jScrollPane20.setMinimumSize(new java.awt.Dimension(250, 23));
        jScrollPane20.setPreferredSize(new java.awt.Dimension(250, 275));

        jTree12.setBackground(new java.awt.Color(204, 255, 255));
        jTree12.setForeground(new java.awt.Color(255, 255, 0));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree12.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree12.setRootVisible(false);
        jTree12.setShowsRootHandles(true);
        jScrollPane20.setViewportView(jTree12);

        jPanel34.add(jScrollPane20, java.awt.BorderLayout.CENTER);

        jPanel97.setBackground(new java.awt.Color(153, 255, 255));

        jButton53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton53.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton53MousePressed(evt);
            }
        });
        jPanel97.add(jButton53);

        jButton54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton54.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton54MousePressed(evt);
            }
        });
        jPanel97.add(jButton54);

        jPanel34.add(jPanel97, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel34, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton53MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton53MousePressed
        ObjectResultSet gc = new ObjectResultSet();
        gc.setColumnNames(new String[]{"AMOUNT","CANDY_NAME","WEIGHT_ONE","FACTORY_NAME"});

        for (int i=0;i<mf.SubOrders.getLength();i++) {
            String table = "<table border='1'>";
            gc.set(db.SelectSQL("SELECT gift_candy.amount,candy.name,candy.box_weight/candy.amount_in_box,factory.name FROM gift_candy,candy,factory WHERE gift_candy.id_candy=candy.id AND candy.id_factory=factory.id AND gift_candy.id_gift=? ORDER BY candy.name", new Object[]{mf.SubOrders.getInt(i,"ID_GIFT")}));
            int Summ_amount = 0;
            double Summ_weight = 0;
            for (int j=0;j<gc.getLength();j++) {
                int amount = gc.getInt(j,"AMOUNT");
                table = table+"<tr><td width='60px'>"+gc.getString(j,"FACTORY_NAME")+"</td><td width='100px'>"+gc.getString(j,"CANDY_NAME")+"</td><td width='20px'>"+amount+"</td><td width='20px'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>";
                Summ_amount+=amount;
                Summ_weight+=amount*gc.getDouble(j,"WEIGHT_ONE");
            }
            table = table+"<tr><td colspan='2'>Количество конфет в подарке:</td><td>"+Summ_amount+"</td>";
            table = table+"</table>";

            String s = "<html><table border='0'><tr><td width='180px'><b>Клиент: "+mf.Orders.getString("CLIENT_NAME")+"</b></td><td><b>Упаковка: №"+mf.SubOrders.getInt(i,"PACKING_NUMBER")+" "+mf.SubOrders.getString(i,"PACKING_NAME")+"</b></td></tr>";
            s = s+"<tr><td><b>Дата: </b>"+new SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime())+"</td><td><b>Набор </b>"+mf.SubOrders.getString(i,"GIFT_NAME")+"</td></tr>";
            s = s+"<tr><td><b>Работники:</b><br>";
            for (int j=0;j<jTree12.getRowCount();j++) {
                TreePath tp = jTree12.getPathForRow(j);
                CheckNode node = (CheckNode)tp.getLastPathComponent();
                if (node.isSelected()) {
                    Object[] obj = (Object[])node.getUserObject();
                    s = s + mf.Users.getString((Integer)obj[1],"NAME")+"<br>";
                }
            }
            s = s+"</td><td><b>Количество: </b>"+mf.SubOrders.getInt(i,"AMOUNT")+"</td></tr>";
            s = s+"<tr><td><b>Сфасовано:</b></td><td></td></tr>";
            s = s+"<tr><td><b>Начало:</b></td><td><b>Конец:</b></td></tr>";
            s = s+"<tr><td><b>Вес подарка: </b>"+FormatKG.format(Summ_weight+mf.SubOrders.getDouble(i,"PACKING_WEIGHT"))+" кг</td><td><b>Количество в ящике:</b></td></tr>";
            s = s+"<tr><td><b>Пожелания:</b></td><td></td></tr></table><br><br>";
            String comm = mf.ExtendedOrder.getString("COMM_PACKING");
            if (comm!=null) {
                comm = comm.replace("\n", "<br>");
            } else {
                comm = "";
            }
            s = s+"<b>Комментарий: </b>"+comm+"<br><br>";
            s = s+table+"<br></html>";
            JEditorPane ep = new JEditorPane();
            ep.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            ep.setFont(new Font("Arial",Font.PLAIN,8));
            ep.setEditorKit(new HTMLEditorKit());
            ep.setText(s);
            try {
                ep.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Не удалось напечатать документ");
            }
        }
    }//GEN-LAST:event_jButton53MousePressed

    private void jButton54MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton54MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton54MousePressed

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
//            java.util.logging.Logger.getLogger(ChoosePackingWorkersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ChoosePackingWorkersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ChoosePackingWorkersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ChoosePackingWorkersDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ChoosePackingWorkersDialog dialog = new ChoosePackingWorkersDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton53;
    private javax.swing.JButton jButton54;
    private javax.swing.JPanel jPanel34;
    /*
    private javax.swing.JPanel jPanel97;
    */
    private GradientPanel jPanel97;
    private javax.swing.JScrollPane jScrollPane20;
    /*
    private javax.swing.JTree jTree12;
    */
    private CheckBoxTree jTree12;
    // End of variables declaration//GEN-END:variables
}
