package MainPackage;

import static MainPackage.MainForm.DarkInterfaceColor;
import static MainPackage.MainForm.LightInterfaceColor;
import java.awt.CardLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class OrderComplectationDialog extends javax.swing.JDialog {

    private final ObjectResultSet FilteredPackingsForNewSuborder = new ObjectResultSet();    
    private final DATABASE db;
    private final MainForm mf;
    
    public OrderComplectationDialog(MainForm mf, DATABASE db) {
        super();
        //super(parent, modal);
        initComponents();
        
        this.db = db;
        this.mf = mf;
        FilteredPackingsForNewSuborder.setColumnNames(new String[]{"ID","NAME","TYPE","NUMBER","CAPACITY","STORAGE","RESERVED","FILENAME","COST","MARKED"});        
    }
    
    public void initDialog(ObjectResultSet Gifts) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new Object[]{"",0});

        for (int i = 0; i < Gifts.getLength(); i++) {
            top.add(new DefaultMutableTreeNode(new Object[]{Gifts.getString(i, "NAME"), i}));
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree7.setModel(treeModel);

        GetFilteredPackingsForNewSuborder();
        makeTreePackingsForNewSuborder();
        
        CardLayout cl = (CardLayout)jPanel60.getLayout();
        cl.show(jPanel60, "card2");
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);    
    }
    
    private void GetFilteredPackingsForNewSuborder() {
        Integer value = null;
        try {
            value = Integer.parseInt(jTextField51.getText());
        } catch (Exception ex) {
        }
        String filterText = value!=null ? " WHERE number="+value : (jTextField51.getText().isEmpty() ? "" : " WHERE name LIKE '%"+jTextField51.getText()+"%'");
        Object[][] obj = db.SelectSQL("SELECT ID,NAME,TYPE,NUMBER,CAPACITY,STORAGE,RESERVED,FILENAME,COST,MARKED FROM packing "+filterText+" ORDER BY number,name",null);
        FilteredPackingsForNewSuborder.set(obj);
    }     
    
    public void makeTreePackingsForNewSuborder() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new Object[]{"",0});
        DefaultMutableTreeNode child_0 = new DefaultMutableTreeNode(new Object[]{"Картон", 0});
        DefaultMutableTreeNode child_1 = new DefaultMutableTreeNode(new Object[]{"Пакет", 1});
        DefaultMutableTreeNode child_2 = new DefaultMutableTreeNode(new Object[]{"Туба", 2});
        DefaultMutableTreeNode child_3 = new DefaultMutableTreeNode(new Object[]{"Жесть", 3});
        DefaultMutableTreeNode child_4 = new DefaultMutableTreeNode(new Object[]{"Игрушка", 4});
        top.add(child_0);
        top.add(child_1);
        top.add(child_2);
        top.add(child_3);
        top.add(child_4);

        boolean thereWereProblemsWithFTP = false;        
        
        for (int i = 0; i < FilteredPackingsForNewSuborder.getLength(); i++) {
            int type = FilteredPackingsForNewSuborder.getInt(i, "TYPE");
            String filename = FilteredPackingsForNewSuborder.getString(i, "FILENAME").trim();
            
            if (!PackingsImages.existsImageWithName(filename)) {
                if ((filename != null) && (!"".equals(filename))) {
                    try {
                        if (!thereWereProblemsWithFTP) {
                            Image image = FTP.readImageFromFileOrDownloadFromFTP(filename);
                            PackingsImages.addImage(image, filename);                    
                        }
                    } catch (Exception ex) {
                        thereWereProblemsWithFTP = true;
                    }
                }                
            }          
            
            DefaultMutableTreeNode dftn = new DefaultMutableTreeNode(new Object[]{"№"+Integer.toString(FilteredPackingsForNewSuborder.getInt(i, "NUMBER"))+"  "+FilteredPackingsForNewSuborder.getString(i, "NAME"), i,FilteredPackingsForNewSuborder.getInt(i,"STORAGE")-FilteredPackingsForNewSuborder.getInt(i,"RESERVED"),PackingsImages.getImage(filename),FilteredPackingsForNewSuborder.getBoolean(i, "MARKED")});
            switch (type) {
                case 0:
                    child_0.add(dftn);
                    break;
                case 1:
                    child_1.add(dftn);
                    break;
                case 2:
                    child_2.add(dftn);
                    break;
                case 3:
                    child_3.add(dftn);
                    break;
                case 4:
                    child_4.add(dftn);
                    break;
            }
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree8.setModel(treeModel);
        for (int i = 0; i <= jTree8.getRowCount() - 1; i++) {
            jTree8.expandRow(i);
        }
        
        if (thereWereProblemsWithFTP) {
           // JOptionPane.showMessageDialog(null, "Проблемы с загрузкой изображений с сервера");
        }
    }    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel60 = new javax.swing.JPanel();
        jPanel54 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTree7 = new GiftsTree();
        jPanel56 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jPanel55 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTree8 = new PackingsTree();
        jPanel59 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton20 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jPanel189 = new javax.swing.JPanel();
        jLabel137 = new javax.swing.JLabel();
        jTextField51 = new javax.swing.JTextField();
        jPanel58 = new javax.swing.JPanel();
        jPanel61 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton86 = new javax.swing.JButton();
        jButton87 = new javax.swing.JButton();
        jButton88 = new javax.swing.JButton();
        jPanel93 = new javax.swing.JPanel();
        jPanel95 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jTextField36 = new javax.swing.JTextField();

        setTitle("Комплектация заказа");
        setMinimumSize(new java.awt.Dimension(420, 400));

        jPanel60.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel60.setMaximumSize(new java.awt.Dimension(380, 400));
        jPanel60.setMinimumSize(new java.awt.Dimension(380, 400));
        jPanel60.setPreferredSize(new java.awt.Dimension(380, 400));
        jPanel60.setLayout(new java.awt.CardLayout());

        jPanel54.setLayout(new java.awt.BorderLayout());

        jTree7.setBackground(new java.awt.Color(204, 255, 255));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree7.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree7.setRootVisible(false);
        jTree7.setShowsRootHandles(true);
        jTree7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree7MouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(jTree7);

        jPanel54.add(jScrollPane14, java.awt.BorderLayout.CENTER);

        jPanel56.setBackground(new java.awt.Color(153, 255, 255));

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton18MousePressed(evt);
            }
        });
        jPanel56.add(jButton18);

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Sign-Forward-icon.png"))); // NOI18N
        jButton19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton19MousePressed(evt);
            }
        });
        jPanel56.add(jButton19);

        jPanel54.add(jPanel56, java.awt.BorderLayout.PAGE_END);

        jPanel60.add(jPanel54, "card2");

        jPanel55.setLayout(new java.awt.BorderLayout());

        jTree8.setBackground(new java.awt.Color(204, 255, 255));
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree8.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree8.setRootVisible(false);
        jTree8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree8MouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(jTree8);

        jPanel55.add(jScrollPane15, java.awt.BorderLayout.CENTER);

        jPanel59.setBackground(new java.awt.Color(153, 255, 255));

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton20MousePressed(evt);
            }
        });
        jPanel59.add(jButton20);

        jButton46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Sign-Forward-icon2.png"))); // NOI18N
        jButton46.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton46MousePressed(evt);
            }
        });
        jPanel59.add(jButton46);

        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Sign-Forward-icon.png"))); // NOI18N
        jButton21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton21MousePressed(evt);
            }
        });
        jPanel59.add(jButton21);

        jPanel55.add(jPanel59, java.awt.BorderLayout.PAGE_END);

        jPanel189.setBackground(new java.awt.Color(153, 255, 255));
        jPanel189.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel189.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel189.setPreferredSize(new java.awt.Dimension(270, 30));
        jPanel189.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        jLabel137.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Zoom-icon.png"))); // NOI18N
        jLabel137.setText("номер или имя упаковки");
        jPanel189.add(jLabel137);

        jTextField51.setMinimumSize(new java.awt.Dimension(6, 26));
        jTextField51.setPreferredSize(new java.awt.Dimension(150, 26));
        jTextField51.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField51KeyReleased(evt);
            }
        });
        jPanel189.add(jTextField51);

        jPanel55.add(jPanel189, java.awt.BorderLayout.PAGE_START);

        jPanel60.add(jPanel55, "card3");

        jPanel58.setLayout(new java.awt.BorderLayout());

        jPanel61.setBackground(new java.awt.Color(153, 255, 255));

        jButton86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton86.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton86MousePressed(evt);
            }
        });
        jPanel61.add(jButton86);

        jButton87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Sign-Forward-icon2.png"))); // NOI18N
        jButton87.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton87MousePressed(evt);
            }
        });
        jPanel61.add(jButton87);

        jButton88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Sign-Forward-icon.png"))); // NOI18N
        jButton88.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton88MousePressed(evt);
            }
        });
        jPanel61.add(jButton88);

        jPanel58.add(jPanel61, java.awt.BorderLayout.PAGE_END);

        jPanel93.setBackground(new java.awt.Color(153, 255, 255));
        jPanel93.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel93.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 100));

        jPanel95.setBackground(new java.awt.Color(204, 255, 255));
        jPanel95.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel95.setPreferredSize(new java.awt.Dimension(294, 135));

        jLabel26.setPreferredSize(new java.awt.Dimension(80, 14));

        jLabel6.setForeground(new java.awt.Color(0, 0, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Набор:");

        jLabel32.setForeground(new java.awt.Color(0, 0, 255));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("Упаковка:");

        jLabel33.setPreferredSize(new java.awt.Dimension(80, 14));

        jTextField34.setPreferredSize(new java.awt.Dimension(65, 24));

        jLabel41.setForeground(new java.awt.Color(0, 0, 255));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Количество:");

        jLabel89.setForeground(new java.awt.Color(0, 0, 255));
        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel89.setText("Себестоим.:");

        jLabel94.setPreferredSize(new java.awt.Dimension(80, 14));

        jLabel95.setForeground(new java.awt.Color(0, 0, 255));
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel95.setText("Цена, грн:");

        jTextField36.setPreferredSize(new java.awt.Dimension(65, 24));

        javax.swing.GroupLayout jPanel95Layout = new javax.swing.GroupLayout(jPanel95);
        jPanel95.setLayout(jPanel95Layout);
        jPanel95Layout.setHorizontalGroup(
            jPanel95Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel95Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel95Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel95Layout.createSequentialGroup()
                        .addGroup(jPanel95Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel95Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel95Layout.createSequentialGroup()
                        .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel95Layout.createSequentialGroup()
                        .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        jPanel95Layout.setVerticalGroup(
            jPanel95Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel95Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel95Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel95Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel95Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel95Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel95Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel95)
                    .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel93.add(jPanel95);

        jPanel58.add(jPanel93, java.awt.BorderLayout.CENTER);

        jPanel60.add(jPanel58, "card4");

        getContentPane().add(jPanel60, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTree7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree7MouseClicked
        if (evt.getClickCount()>1) {
            jButton19MousePressed(null);
        }
    }//GEN-LAST:event_jTree7MouseClicked

    private void jButton18MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton18MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton18MousePressed

    private void jButton19MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton19MousePressed
        if (jTree7.getSelectionCount()!=0) {
            CardLayout cl = (CardLayout)jPanel60.getLayout();
            cl.show(jPanel60, "card3");
        }
    }//GEN-LAST:event_jButton19MousePressed

    private void jTree8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree8MouseClicked
        if (evt.getClickCount()>1) {
            jButton21MousePressed(null);
        }
    }//GEN-LAST:event_jTree8MouseClicked

    private void jButton20MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton20MousePressed

    private void jButton46MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton46MousePressed
        CardLayout cl = (CardLayout)jPanel60.getLayout();
        cl.show(jPanel60, "card2");
    }//GEN-LAST:event_jButton46MousePressed

    private void jButton21MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton21MousePressed
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree8.getLastSelectedPathComponent();
        DefaultMutableTreeNode SelectedNode2 = (DefaultMutableTreeNode) jTree7.getLastSelectedPathComponent();
        if ((SelectedNode != null) && (SelectedNode.getLevel() != 1)) {
            jLabel26.setText((String)((Object[])SelectedNode2.getUserObject())[0]);
            jLabel33.setText((String)((Object[])SelectedNode.getUserObject())[0]);

            Object[] obj = (Object[]) SelectedNode.getUserObject();
            double packing_price = FilteredPackingsForNewSuborder.getDouble((Integer) obj[1], "COST");

            obj = (Object[]) SelectedNode2.getUserObject();
            int id_gift = mf.Gifts.getInt((Integer) obj[1], "ID");

            Object[][] objobj = db.SelectSQL("SELECT SUM(gift_candy.amount*candy.cost_kg*candy.box_weight/candy.amount_in_box),gift.cost_packing FROM gift_candy,candy,gift WHERE gift_candy.id_candy=candy.id AND gift_candy.id_gift=gift.id AND gift.id=?", new Object[]{id_gift});
            double gift_cost = (Double) objobj[0][0];
            double gift_packing_cost = (Double) objobj[0][1];
            double self_cost = gift_cost + packing_price + gift_packing_cost + mf.Constants.getDouble("STICK_COST")+mf.Constants.getDouble("COST_BOX_FOR_1_GIFT");
            
            
            if (mf.CurrentUser.getInt("LEVEL") == 1) {
                jLabel94.setVisible(false);
                jLabel89.setVisible(false);
            } else {
                jLabel94.setText(MainForm.FormatUAH.format(self_cost));
            }
            jTextField34.setText("");
            jTextField36.setText("");
            CardLayout cl = (CardLayout)jPanel60.getLayout();
            cl.show(jPanel60, "card4");
        }
    }//GEN-LAST:event_jButton21MousePressed

    private void jTextField51KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField51KeyReleased
        GetFilteredPackingsForNewSuborder();
        makeTreePackingsForNewSuborder();
    }//GEN-LAST:event_jTextField51KeyReleased

    private void jButton86MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton86MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton86MousePressed

    private void jButton87MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton87MousePressed
        CardLayout cl = (CardLayout)jPanel60.getLayout();
        cl.show(jPanel60, "card3");
    }//GEN-LAST:event_jButton87MousePressed

    private void jButton88MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton88MousePressed
        try {
            int amount = Integer.parseInt(jTextField34.getText());
            double cost = Double.parseDouble(jTextField36.getText().replaceAll(" ", "").replace(',', '.'));
            DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree8.getLastSelectedPathComponent();
            Object[] obj = (Object[]) SelectedNode.getUserObject();
            int id_packing = FilteredPackingsForNewSuborder.getInt((Integer) obj[1], "ID");
            double packing_price = FilteredPackingsForNewSuborder.getDouble((Integer) obj[1], "COST");

            SelectedNode = (DefaultMutableTreeNode) jTree7.getLastSelectedPathComponent();
            obj = (Object[]) SelectedNode.getUserObject();
            int id_gift = mf.Gifts.getInt((Integer) obj[1], "ID");

            Object[][] objobj = db.SelectSQL("SELECT SUM(gift_candy.amount*candy.cost_kg*candy.box_weight/candy.amount_in_box),gift.cost_packing FROM gift_candy,candy,gift WHERE gift_candy.id_candy=candy.id AND gift_candy.id_gift=gift.id AND gift.id=?", new Object[]{id_gift});
            double gift_cost = (Double) objobj[0][0];
            double gift_packing_cost = (Double) objobj[0][1];
            double self_cost = gift_cost + packing_price + gift_packing_cost + mf.Constants.getDouble("STICK_COST")+mf.Constants.getDouble("COST_BOX_FOR_1_GIFT");

            if (db.UpdateSQL("INSERT INTO suborder(id_orders,id_gift,id_packing,amount,cost,packed,self_cost) VALUES(?,?,?,?,?,?,?)", new Object[]{mf.Orders.getInt("ID"), id_gift, id_packing, amount, cost,0,self_cost})) {
                
                mf.GetSubOrders();
                mf.MakeTableOfSubOrders();
                double Summ = 0;
                for (int i=0;i<mf.SubOrders.getLength();i++) {
                    Summ+=mf.SubOrders.getDouble(i,"COST")*mf.SubOrders.getInt(i,"AMOUNT");
                }
                mf.setDeliveryCostLabelText(MainForm.FormatUAH.format(Summ - mf.ExtendedOrder.getDouble("PREPAY")));
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
            this.setVisible(false);
        } catch (NumberFormatException | HeadlessException ex) {
        }
    }//GEN-LAST:event_jButton88MousePressed

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
//            java.util.logging.Logger.getLogger(OrderComplectationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(OrderComplectationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(OrderComplectationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(OrderComplectationDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                OrderComplectationDialog dialog = new OrderComplectationDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton86;
    private javax.swing.JButton jButton87;
    private javax.swing.JButton jButton88;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JPanel jPanel189;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    /*
    private javax.swing.JPanel jPanel56;
    */
    private GradientPanel jPanel56;
    private javax.swing.JPanel jPanel58;
    /*
    private javax.swing.JPanel jPanel59;
    */
    private GradientPanel jPanel59;
    private javax.swing.JPanel jPanel60;
    /*
    private javax.swing.JPanel jPanel61;
    */
    private GradientPanel jPanel61;
    private javax.swing.JPanel jPanel93;
    private javax.swing.JPanel jPanel95;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField51;
    /*
    private javax.swing.JTree jTree7;
    */
    private GiftsTree jTree7;
    /*
    private javax.swing.JTree jTree8;
    */
    private PackingsTree jTree8;
    // End of variables declaration//GEN-END:variables
}
