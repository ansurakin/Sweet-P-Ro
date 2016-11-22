package MainPackage;

import static MainPackage.MainForm.DarkInterfaceColor;
import static MainPackage.MainForm.LightInterfaceColor;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ClientChooseDialog extends javax.swing.JDialog {

    private final ObjectResultSet ClientsForChooseInNewOrder = new ObjectResultSet();    
    private final DATABASE db;
    private final MainForm mf;
    
    public ClientChooseDialog(MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.db = db;
        this.mf = mf;
        
        ClientsForChooseInNewOrder.setColumnNames(new String[]{"ID", "OFFICIAL_NAME","ISUSED"});       
    }

    public void initDialog() {
        fillTableChooseClientForNewOrder();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    private boolean GetClientsForChooseInNewOrder(boolean OrderByAlphabet, String clientFilter) {
        Object[][] obj;
        String filterText = "'%"+clientFilter+"%'";
                
        if (OrderByAlphabet) {
            obj = db.SelectSQL(new StringBuilder().append("SELECT id as client_id,official_name,(SELECT id_client FROM orders WHERE id_client=client_id LIMIT 1),state FROM client WHERE (official_name LIKE ").append(filterText).append(" OR name LIKE ").append(filterText).append(" OR phone1 LIKE ").append(filterText).append(" OR phone2 LIKE ").append(filterText).append(" OR phone3 LIKE ").append(filterText).append(" OR contact1 LIKE ").append(filterText).append(" OR contact2 LIKE ").append(filterText).append(" OR contact3 LIKE ").append(filterText).append(" OR email1 LIKE ").append(filterText).append(" OR email2 LIKE ").append(filterText).append(" OR email3 LIKE ").append(filterText).append(")").append(" ORDER BY OFFICIAL_NAME").toString(),null);
        } else {
            obj = db.SelectSQL(new StringBuilder().append("SELECT id as client_id,official_name,(SELECT id_client FROM orders WHERE id_client=client_id LIMIT 1),state FROM client WHERE (official_name LIKE ").append(filterText).append(" OR name LIKE ").append(filterText).append(" OR phone1 LIKE ").append(filterText).append(" OR phone2 LIKE ").append(filterText).append(" OR phone3 LIKE ").append(filterText).append(" OR contact1 LIKE ").append(filterText).append(" OR contact2 LIKE ").append(filterText).append(" OR contact3 LIKE ").append(filterText).append(" OR email1 LIKE ").append(filterText).append(" OR email2 LIKE ").append(filterText).append(" OR email3 LIKE ").append(filterText).append(")").append(" ORDER BY date_time DESC").toString(),null);
        }
        ClientsForChooseInNewOrder.set(obj);
        return obj!=null;
    }     
    
    private void fillTableChooseClientForNewOrder() {
        mf.GetFolders();
        GetClientsForChooseInNewOrder(true,jTextField48.getText());
        
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new Object[]{"КЛИЕНТЫ",0});
        
        LinkedList<Integer> listAlreadyUsedClients = new LinkedList<>();
        
        for (int i = 0;i<mf.Folders.getLength();i++) {
            int folderID = mf.Folders.getInt(i,"ID");
            DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(new Object[]{folderID,mf.Folders.getString(i,"NAME")});
            top.add(folderNode);
            for (int k = 0;k<ClientsForChooseInNewOrder.getLength();k++) {
                int clientID = ClientsForChooseInNewOrder.getInt(k,"ID");
                for (int j = 0;j<mf.Clients_Folders.getLength();j++) {
                    if (folderID==mf.Clients_Folders.getInt(j,"ID_FOLDER") && clientID==mf.Clients_Folders.getInt(j,"ID_CLIENT")) {
                        folderNode.add(new DefaultMutableTreeNode(new Object[]{ClientsForChooseInNewOrder.getString(k, "OFFICIAL_NAME"), k, ClientsForChooseInNewOrder.getInt(k,"ISUSED")!=0, ClientState.getStateByValueDB(ClientsForChooseInNewOrder.getInt(k,"STATE"))}));        
                        listAlreadyUsedClients.add(ClientsForChooseInNewOrder.getInt(k,"ID"));
                        break;
                    }
                }
            }
        }
        
        DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(new Object[]{0,"без папки"});        
        top.add(folderNode);
        
        for (int i = 0;i<ClientsForChooseInNewOrder.getLength();i++) {
            if (!listAlreadyUsedClients.contains(ClientsForChooseInNewOrder.getInt(i,"ID"))) {
                folderNode.add(new DefaultMutableTreeNode(new Object[]{ClientsForChooseInNewOrder.getString(i, "OFFICIAL_NAME"), i, ClientsForChooseInNewOrder.getInt(i,"ISUSED")!=0, ClientState.getStateByValueDB(ClientsForChooseInNewOrder.getInt(i,"STATE"))}));
            }
        }
        
        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree9.setModel(treeModel);
        for (int i = 0; i <= jTree9.getRowCount() - 1; i++) {
            jTree9.expandRow(i);
        }
    }    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel66 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTree9 = new ClientsTree();
        jPanel184 = new javax.swing.JPanel();
        jLabel126 = new javax.swing.JLabel();
        jTextField48 = new javax.swing.JTextField();
        jPanel67 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();

        setTitle("Выбор клиента");

        jPanel66.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel66.setMaximumSize(new java.awt.Dimension(300, 400));
        jPanel66.setMinimumSize(new java.awt.Dimension(300, 400));
        jPanel66.setPreferredSize(new java.awt.Dimension(300, 400));
        jPanel66.setLayout(new javax.swing.BoxLayout(jPanel66, javax.swing.BoxLayout.Y_AXIS));

        jTree9.setBackground(new java.awt.Color(204, 255, 255));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree9.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree9.setRootVisible(false);
        jTree9.setShowsRootHandles(true);
        jTree9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree9MouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(jTree9);

        jPanel66.add(jScrollPane16);

        jPanel184.setBackground(new java.awt.Color(153, 255, 255));
        jPanel184.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel184.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel184.setPreferredSize(new java.awt.Dimension(270, 30));
        jPanel184.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 2));

        jLabel126.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Zoom-icon.png"))); // NOI18N
        jPanel184.add(jLabel126);

        jTextField48.setMinimumSize(new java.awt.Dimension(6, 26));
        jTextField48.setPreferredSize(new java.awt.Dimension(200, 26));
        jTextField48.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField48KeyReleased(evt);
            }
        });
        jPanel184.add(jTextField48);

        jPanel66.add(jPanel184);

        jPanel67.setBackground(new java.awt.Color(153, 255, 255));
        jPanel67.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel67.setMinimumSize(new java.awt.Dimension(113, 32));
        jPanel67.setPreferredSize(new java.awt.Dimension(113, 32));

        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton25MousePressed(evt);
            }
        });
        jPanel67.add(jButton25);

        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Sign-Forward-icon.png"))); // NOI18N
        jButton26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton26MousePressed(evt);
            }
        });
        jPanel67.add(jButton26);

        jPanel66.add(jPanel67);

        getContentPane().add(jPanel66, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTree9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree9MouseClicked
        if (evt.getClickCount()>1) {
            jButton26MousePressed(null);
        }
    }//GEN-LAST:event_jTree9MouseClicked

    private void jTextField48KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField48KeyReleased
        fillTableChooseClientForNewOrder();
    }//GEN-LAST:event_jTextField48KeyReleased

    private void jButton25MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton25MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton25MousePressed

    private void jButton26MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton26MousePressed
        if (jTree9.getSelectionCount()!=0) {
            DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode)jTree9.getLastSelectedPathComponent();
            if (SelectedNode.getLevel()!=2) {
                return;
            }

            Object[] obj = (Object[])SelectedNode.getUserObject();
            int id_client = ClientsForChooseInNewOrder.getInt((Integer)obj[1], "ID");
            long time = Calendar.getInstance().getTimeInMillis();
            
            String orderNumber = new SimpleDateFormat("ddMMyy-").format(new Date());
            boolean successfully = false;
            for (int i=1;i<100;i++) {
                if (db.UpdateSQL_justTry("INSERT INTO orders (date_time,id_client,type_pay,state,comm,date_pay,date_pack,prepay,user_creator_id,number) VALUES(?,?,-1,0,'',0,0,0,?,?)",new Object[]{time,id_client,mf.CurrentUser.getInt("ID"),orderNumber+i})) {
                    successfully = true;
                    mf.updateTableOrdersAndSelectLastAddedOrder();
                    break;
                }
            }
            if (!successfully) {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
            this.setVisible(false);
        }
    }//GEN-LAST:event_jButton26MousePressed

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
//            java.util.logging.Logger.getLogger(ClientChoose.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ClientChoose.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ClientChoose.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ClientChoose.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ClientChoose dialog = new ClientChoose(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JPanel jPanel184;
    private javax.swing.JPanel jPanel66;
    /*
    private javax.swing.JPanel jPanel67;
    */
    private GradientPanel jPanel67;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JTextField jTextField48;
    /*
    private javax.swing.JTree jTree9;
    */
    private ClientsTree jTree9;
    // End of variables declaration//GEN-END:variables
}
