package MainPackage;

import static MainPackage.MainForm.DarkInterfaceColor;
import static MainPackage.MainForm.LightInterfaceColor;
import java.awt.Color;
import javax.swing.JOptionPane;

public class LabelPrintDialog extends javax.swing.JDialog {

    private final MainForm mf;
    private final DATABASE db;
    
    public LabelPrintDialog(MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.mf = mf;
        this.db = db;
        
        jTable21.setShowGrid(true);
        jTable21.setGridColor(Color.GRAY);
        jScrollPane44.getViewport().setBackground(LightInterfaceColor);
    }
    
    public void initDialog() {
        Object[][] data = new Object[mf.SubOrders.getLength()][4];
        for (int i = 0; i < mf.SubOrders.getLength(); i++) {
            data[i][0] = mf.SubOrders.getString(i, "GIFT_NAME");
            data[i][1] = mf.SubOrders.getInt(i, "AMOUNT");
            data[i][2] = 1;
            data[i][3] = mf.SubOrders.getInt(i, "AMOUNT");
        }
        jTable21.makeTable(data);
        jTextField49.setText(mf.Orders.getString("CLIENT_NAME"));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel178 = new javax.swing.JPanel();
        jScrollPane44 = new javax.swing.JScrollPane();
        jTable21 = new MainPackage.TableBoxesForGifts();
        jPanel186 = new javax.swing.JPanel();
        jLabel127 = new javax.swing.JLabel();
        jTextField49 = new javax.swing.JTextField();
        jPanel179 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton112 = new javax.swing.JButton();
        jButton113 = new javax.swing.JButton();

        setTitle("Печать этикетки");

        jPanel178.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel178.setMaximumSize(new java.awt.Dimension(450, 400));
        jPanel178.setMinimumSize(new java.awt.Dimension(450, 400));
        jPanel178.setPreferredSize(new java.awt.Dimension(450, 400));
        jPanel178.setLayout(new javax.swing.BoxLayout(jPanel178, javax.swing.BoxLayout.Y_AXIS));

        jTable21.setBackground(new java.awt.Color(204, 255, 255));
        jTable21.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable21.setSelectionBackground(new java.awt.Color(153, 255, 255));
        jScrollPane44.setViewportView(jTable21);

        jPanel178.add(jScrollPane44);

        jPanel186.setBackground(new java.awt.Color(153, 255, 255));
        jPanel186.setPreferredSize(new java.awt.Dimension(444, 40));
        jPanel186.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 7));

        jLabel127.setText("Название клиента:");
        jPanel186.add(jLabel127);

        jTextField49.setMinimumSize(new java.awt.Dimension(6, 26));
        jTextField49.setPreferredSize(new java.awt.Dimension(300, 26));
        jPanel186.add(jTextField49);

        jPanel178.add(jPanel186);

        jPanel179.setBackground(new java.awt.Color(153, 255, 255));
        jPanel179.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel179.setMinimumSize(new java.awt.Dimension(113, 32));
        jPanel179.setPreferredSize(new java.awt.Dimension(113, 32));
        jPanel179.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 3));

        jButton112.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton112.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton112.setOpaque(false);
        jButton112.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton112ActionPerformed(evt);
            }
        });
        jPanel179.add(jButton112);

        jButton113.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton113.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton113.setOpaque(false);
        jButton113.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton113ActionPerformed(evt);
            }
        });
        jPanel179.add(jButton113);

        jPanel178.add(jPanel179);

        getContentPane().add(jPanel178, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton112ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton112ActionPerformed
        String clientName = jTextField49.getText();
        for (int i=0;i<mf.SubOrders.getLength();i++) {
            if ((Integer)jTable21.getValueAt(i, 1)!=0) {
                int gifts = (Integer)jTable21.getValueAt(i, 1);
                int gifts_in_box = (Integer)jTable21.getValueAt(i, 2);
                int labelsCount = gifts/gifts_in_box;
                EZPLprint.STATUS status = EZPLprint.print(clientName, (String)jTable21.getValueAt(i, 0), gifts_in_box, labelsCount);
                if (status==EZPLprint.STATUS.NO_PRINTER) {
                    this.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Не найден подходящий принтер");
                    return;
                } else if (status==EZPLprint.STATUS.ERROR) {
                    this.setVisible(false);
                    JOptionPane.showMessageDialog(null, "Ошибка в процессе печати");
                    return;
                }
                if (gifts%gifts_in_box!=0) { //есть неполностью заполненная коробка
                    EZPLprint.print(clientName, (String)jTable21.getValueAt(i, 0), gifts%gifts_in_box, 1);
                }
                this.setVisible(false);
            }
        }
                        
//        String s;
//        JEditorPane ep = new JEditorPane();
//        ep.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
//        ep.setFont(new Font("Arial",Font.PLAIN,17));
//        ep.setEditorKit(new HTMLEditorKit());
//        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//        aset.add(OrientationRequested.PORTRAIT);
//        aset.add(MediaSizeName.ISO_A4);
//
//        String clientName = jTextField49.getText();
//
//        for (int i=0;i<mf.SubOrders.getLength();i++) {
//            if ((Integer)jTable21.getValueAt(i, 1)!=0) {
//                s = "<html><center>";
//                int gifts = (Integer)jTable21.getValueAt(i, 1);
//                int gifts_in_box = (Integer)jTable21.getValueAt(i, 2);
//                int repeat = gifts/gifts_in_box/ 2;
//                String text1 = "Клиент: <b>"+clientName+"</b><br><br>Набор: <b>"+jTable21.getValueAt(i, 0)+"</b><br><br>Кол-во в ящике: <b>"+gifts_in_box+"</b><br>";
//                String text2 = "Клиент: <b>"+clientName+"</b><br><br>Набор: <b>"+jTable21.getValueAt(i, 0)+"</b><br><br>Кол-во в ящике: <b>"+gifts%gifts_in_box+"</b><br>";
//                for (int j=0;j<repeat;j++) {
//                    s = s+"<table cellspacing='10' width=100% border='0'><tr><td width=50% height='95px'>"+text1+"</td><td width=50% height='95px'>"+text1+"</td></tr></table><br>";
//                }
//                if (gifts%gifts_in_box!=0) {
//                    s = s+"<table cellspacing='10' width=100% border='0'><tr><td width=50% height='95px'>"+text2+"</td><td width=50% height='95px'></td></tr></table>";
//                }
//                s = s+"<br></center></html>";
//                ep.setText(s);
//                try {
//                    ep.print(new MessageFormat(""),new MessageFormat(""),false,null,aset,false);
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(null, "Не удалось напечатать документ");
//                }
//            }
//        }
//        this.setVisible(false);
    }//GEN-LAST:event_jButton112ActionPerformed

    private void jButton113ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton113ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton113ActionPerformed

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
//            java.util.logging.Logger.getLogger(LabelPrintDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(LabelPrintDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(LabelPrintDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(LabelPrintDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                LabelPrintDialog dialog = new LabelPrintDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton112;
    private javax.swing.JButton jButton113;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JPanel jPanel178;
    /*
    private javax.swing.JPanel jPanel179;
    */
    private GradientPanel jPanel179;
    private javax.swing.JPanel jPanel186;
    private javax.swing.JScrollPane jScrollPane44;
    /*
    private javax.swing.JTable jTable21;
    */
    private MainPackage.TableBoxesForGifts jTable21;
    private javax.swing.JTextField jTextField49;
    // End of variables declaration//GEN-END:variables
}
