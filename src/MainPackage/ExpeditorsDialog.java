package MainPackage;

import static MainPackage.MainForm.FormatUAH;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ExpeditorsDialog extends javax.swing.JDialog {

    private final MainForm mf;
    private final DATABASE db;
    private final ObjectResultSet allExpeditors = new ObjectResultSet();
    private final ObjectResultSet expeditorsToDelivery = new ObjectResultSet();
    private final ArrayList<Object[]> newListOfExpeditorsToDelivery = new ArrayList();
    
    private final ArrayList<Object[]> newListOfDeliveryItemsToDelivery = new ArrayList<>();
    private final ObjectResultSet deliveryItemsToDelivery = new ObjectResultSet();
    private final ObjectResultSet deliveryItemsToDeliveryToPrint = new ObjectResultSet();
    
    private boolean editMode = false;
    private boolean canEditExpeditors = false;
    
    public ExpeditorsDialog(MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.mf = mf;
        this.db = db;
        
        allExpeditors.setColumnNames(new String[]{"ID","NAME"});
        expeditorsToDelivery.setColumnNames(new String[]{"ID","NAME","PHONE"});
        deliveryItemsToDelivery.setColumnNames(new String[]{"ID","DATE_TIME","CONTACT","ADDRESS","DELIVERY_ORDER"});
        deliveryItemsToDeliveryToPrint.setColumnNames(new String[]{"ID","DATE_TIME","CONTACT","ADDRESS","CONTENT","COMM","CLIENT","DEBT","USER_CREATOR_NAME","TYPE_PAY_ORDER"});
        jTable12.setShowGrid(true);
        jTable12.setGridColor(Color.GRAY);
    }

    public void initDialog(boolean canEditExpeditors) {
        mf.getExpeditors();
        makeTreeOfExpeditors();

        editMode = false;
        this.canEditExpeditors = canEditExpeditors;
        
        jButton38.setVisible(canEditExpeditors);
        jButton39.setVisible(canEditExpeditors);
        jButton68.setVisible(canEditExpeditors);
        jButton33.setVisible(canEditExpeditors);
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public void makeTreeOfExpeditors() {
        int[] SaveCurrentRow = jTree4.getSelectionRows();

        DefaultMutableTreeNode top = new DefaultMutableTreeNode();
        DefaultMutableTreeNode child;

        for (int i = 0; i < mf.Expeditors.getLength(); i++) {
            Object obj[] = new Object[]{mf.Expeditors.getString(i, "NAME"), i};
            child = new DefaultMutableTreeNode(obj);
            top.add(child);
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree4.setModel(treeModel);

        jTree4.setSelectionRows(SaveCurrentRow);
        selectNodeOfTreeExpeditors();
    }
    
    public void selectNodeOfTreeExpeditors() {
        Object[] obj;
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree4.getLastSelectedPathComponent();
        if (SelectedNode == null) {
            jPanel2.setVisible(false);
        } else {
            jPanel2.setVisible(true);
            
            obj = (Object[]) SelectedNode.getUserObject();
            mf.Expeditors.setPosition((Integer) obj[1]);
            
            jTextField2.setText(mf.Expeditors.getString("NAME"));
            jTextField1.setText(mf.Expeditors.getString("DRIVER"));
            
            expeditorsToDelivery.set(db.SelectSQL("SELECT user.id,user.name,user.phone FROM user,expeditors,expeditors_user WHERE expeditors.id=? AND expeditors_user.id_expeditors=expeditors.id AND expeditors_user.id_user=user.id ORDER BY user.name", new Object[]{mf.Expeditors.getInt("ID")}));
            newListOfExpeditorsToDelivery.clear();
            for (int i=0;i<expeditorsToDelivery.getLength();i++) {
                newListOfExpeditorsToDelivery.add(new Object[]{expeditorsToDelivery.getInt(i,"ID"),expeditorsToDelivery.getString(i,"NAME"),expeditorsToDelivery.getString(i,"PHONE")});
            }
            fillListExpeditorsToDelivery();
            
            deliveryItemsToDelivery.set(db.SelectSQL("SELECT id,date_time,contact,address,expeditors_delivery_order FROM delivery WHERE date_close=0 AND id_expeditors=? ORDER BY expeditors_delivery_order", new Object[]{mf.Expeditors.getInt("ID")}));            
            newListOfDeliveryItemsToDelivery.clear();
            for (int i = 0;i<deliveryItemsToDelivery.getLength();i++) {
                newListOfDeliveryItemsToDelivery.add(new Object[]{deliveryItemsToDelivery.getInt(i,"ID"),deliveryItemsToDelivery.getInt(i,"DATE_TIME"),deliveryItemsToDelivery.getString(i,"CONTACT"),deliveryItemsToDelivery.getString(i,"ADDRESS")});
            }
            MakeTableOfDeliveryItemsToDelivery();            
            
            if (editMode) {
                jTextField2.setBorder(BorderFactory.createEtchedBorder());
                jTextField1.setBorder(BorderFactory.createEtchedBorder());
                jTextField2.setBackground(Color.WHITE);
                jTextField1.setBackground(Color.WHITE);
                jTextField2.setEditable(true);
                jTextField1.setEditable(true);

                allExpeditors.set(db.SelectSQL("SELECT id,name FROM user WHERE id_position=? ORDER BY name", new Object[]{MainForm.STORAGE_WORKER_FORWARDER}));
                DefaultListModel listModel = (DefaultListModel)jList2.getModel();
                listModel.removeAllElements();
                for (int i=0;i<allExpeditors.getLength();i++) {
                    listModel.addElement(allExpeditors.getString(i,"NAME"));
                }
                
                jPanel3.setVisible(true);
                jScrollPane2.setVisible(true);
                jLabel3.setVisible(true);
                
                jScrollPane1.setBorder(BorderFactory.createEtchedBorder());
                jList1.setBackground(Color.WHITE);
                
                jButton33.setVisible(false);
                jButton40.setVisible(true);
                jButton41.setVisible(true);
                
                jLabel6.setVisible(true);
            } else {
                jTextField2.setBorder(BorderFactory.createEmptyBorder());
                jTextField1.setBorder(BorderFactory.createEmptyBorder());
                jTextField2.setBackground(jTextField2.getParent().getBackground());
                jTextField1.setBackground(jTextField1.getParent().getBackground());
                jTextField2.setEditable(false);
                jTextField1.setEditable(false);
                
                jPanel3.setVisible(false);
                jScrollPane2.setVisible(false);
                jLabel3.setVisible(false);
                
                jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
                jList1.setBackground(new Color(204,255,255));
                
                jButton33.setVisible(canEditExpeditors);
                jButton40.setVisible(false);
                jButton41.setVisible(false);
                
                jLabel6.setVisible(false);
            }
        }
    }    
    
    public void rowUp(int rowNo) {
        if (rowNo>0) {
            Object[] obj = newListOfDeliveryItemsToDelivery.get(rowNo);
            newListOfDeliveryItemsToDelivery.set(rowNo, newListOfDeliveryItemsToDelivery.get(rowNo-1));
            newListOfDeliveryItemsToDelivery.set(rowNo-1, obj);
            MakeTableOfDeliveryItemsToDelivery();
        }
    }
    
    public void rowDown(int rowNo) {
        if (rowNo<newListOfDeliveryItemsToDelivery.size()-1) {
            Object[] obj = newListOfDeliveryItemsToDelivery.get(rowNo);
            newListOfDeliveryItemsToDelivery.set(rowNo, newListOfDeliveryItemsToDelivery.get(rowNo+1));
            newListOfDeliveryItemsToDelivery.set(rowNo+1, obj);
            MakeTableOfDeliveryItemsToDelivery();
        }
    }
    
    public void removeRow(int rowNo) {
        if (!editMode) {
            return;
        }
        newListOfDeliveryItemsToDelivery.remove(rowNo);
        MakeTableOfDeliveryItemsToDelivery();
    }
    
    public void addRow(int rowNo) {
        if (!editMode) {
            return;
        }
        if (mf.Delivery.get(rowNo,"EXPEDITORS_NAME")==null && mf.Delivery.getInt(rowNo,"DATE_CLOSE")==0) {
            int id = mf.Delivery.getInt(rowNo,"ID");
            for (Object[] obj : newListOfDeliveryItemsToDelivery) { 
                if ((Integer)obj[0]==id) {
                    return;
                }
            }
            newListOfDeliveryItemsToDelivery.add(new Object[]{mf.Delivery.getInt(rowNo, "ID"),mf.Delivery.getInt(rowNo,"DATE_TIME"),mf.Delivery.getString(rowNo,"CONTACT"),mf.Delivery.getString(rowNo,"ADDRESS")});
            MakeTableOfDeliveryItemsToDelivery();
        }
    }
    
    public void MakeTableOfDeliveryItemsToDelivery() {
        Object data[][] = new Object[0][4];
        if (!newListOfDeliveryItemsToDelivery.isEmpty()) {
            data = new Object[newListOfDeliveryItemsToDelivery.size()][4];
            int i = 0;
            for (Object[] obj : newListOfDeliveryItemsToDelivery) {
                data[i][0] = " "+new SimpleDateFormat("dd-MMM-yyyy").format(new Date((Integer)obj[1]*1000L));
                data[i][1] = " "+obj[2];
                data[i][2] = " "+obj[3];
                data[i][3] = "buttons";
                i++;
            }
        }
        jTable12.makeTable(data,this,editMode); 
    }
    
    private void fillListExpeditorsToDelivery() {
        DefaultListModel listModel = (DefaultListModel)jList1.getModel();
        listModel.removeAllElements();
        for(Object[] obj : newListOfExpeditorsToDelivery) {
            listModel.addElement(obj[1]); //name of expeditor
        }
    }
    
    private void printExpeditorsList() {
        deliveryItemsToDeliveryToPrint.set(db.SelectSQL("SELECT D.id,D.date_time,D.contact,D.address,D.content,D.comm,client.official_name,D.debt,(SELECT name FROM user WHERE D.id_orders=orders.id AND orders.user_creator_id=user.id),orders.type_pay FROM delivery D,orders,client WHERE D.id_orders=orders.id AND orders.id_client=client.id AND D.date_close=0 AND D.id_expeditors=? ORDER BY D.expeditors_delivery_order",new Object[]{mf.Expeditors.getInt("ID")}));
        
        StringBuilder s = new StringBuilder();
        s.append("<html><center>ЛИСТ ДОСТАВКИ<br>для экипажа '").append(mf.Expeditors.getString("NAME")).append("'<br><br>");
        s.append("<p align='left'><b>Водитель, автомобиль: </b>").append(mf.Expeditors.getString("DRIVER")).append("<br><b>Экспедиторы:</b><br>");
        s.append("<table border='1' cellspacing='0'>");
        for(int i=0;i<expeditorsToDelivery.getLength();i++) {
            s.append("<tr><td>").append(expeditorsToDelivery.get(i,"NAME")).append("</td><td>").append(expeditorsToDelivery.getString(i,"PHONE")).append("</td></tr>");
        }
        s.append("</table><br></p><br>");
        s.append("<br><table border='1' cellspacing='0'>");
        s.append("<tr><td><b>Дата доставки</b></td><td><b>Клиент</b></td><td><b>Контактное лицо</b></td><td><b>Адрес</b></td><td><b>Содержание доставки</b></td><td><b>Долг</b></td><td><b>Комментарий</b></td><td><b>Менеджер</b></td></tr>");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        for(int i=0;i<deliveryItemsToDeliveryToPrint.getLength();i++) {
            s.append("<tr>");
            s.append("<td>").append(sdf.format(new Date(deliveryItemsToDeliveryToPrint.getInt(i,"DATE_TIME")*1000L))).append("</td>");
            s.append("<td>").append(deliveryItemsToDeliveryToPrint.getString(i,"CLIENT")).append("</td>");
            s.append("<td>").append(deliveryItemsToDeliveryToPrint.getString(i,"CONTACT")).append("</td>");
            s.append("<td>").append(deliveryItemsToDeliveryToPrint.getString(i,"ADDRESS")).append("</td>");
            s.append("<td>").append(deliveryItemsToDeliveryToPrint.getString(i,"CONTENT")).append("</td>");
            int typePay = deliveryItemsToDeliveryToPrint.getInt(i,"TYPE_PAY_ORDER"); //2 и 3 и 5 - безнал
            s.append("<td>").append(FormatUAH.format(deliveryItemsToDeliveryToPrint.getDouble(i,"DEBT"))).append(typePay==2 || typePay==3 ? " БН" : "").append("</td>");
            s.append("<td>").append(deliveryItemsToDeliveryToPrint.getString(i,"COMM")).append("</td>");
            s.append("<td>").append(deliveryItemsToDeliveryToPrint.getString(i,"USER_CREATOR_NAME")).append("</td>");
            s.append("</tr>");
        }
        s.append("</table><br><br></center></html>");
        
        this.setVisible(false);
        JEditorPane ep = new JEditorPane();
        ep.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        ep.setFont(new Font("Arial", Font.PLAIN, 8));
        ep.setEditorKit(new HTMLEditorKit());
        ep.setText(s.toString());
        try {
            ep.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Не удалось напечатать документ");
        }
        this.setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel11 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton68 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTree4 = new ExpeditorsTree();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(120, 0), new java.awt.Dimension(32767, 0));
        jButton33 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(120, 0), new java.awt.Dimension(32767, 0));
        jButton1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane29 = new javax.swing.JScrollPane();
        jTable12 = new MainPackage.TableDeliveryExpeditors();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();

        setTitle("Экипажи");
        setAlwaysOnTop(true);
        setPreferredSize(new java.awt.Dimension(900, 500));
        setResizable(false);

        jPanel11.setBackground(new java.awt.Color(204, 255, 255));
        jPanel11.setMinimumSize(new java.awt.Dimension(900, 122));
        jPanel11.setPreferredSize(new java.awt.Dimension(700, 500));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel7.setPreferredSize(new java.awt.Dimension(200, 400));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(153, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel8.setPreferredSize(new java.awt.Dimension(200, 40));

        jButton38.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton38.setForeground(new java.awt.Color(255, 0, 0));
        jButton38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delivery.png"))); // NOI18N
        jButton38.setText("+");
        jButton38.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton38.setMaximumSize(new java.awt.Dimension(75, 25));
        jButton38.setMinimumSize(new java.awt.Dimension(75, 25));
        jButton38.setOpaque(false);
        jButton38.setPreferredSize(new java.awt.Dimension(75, 25));
        jButton38.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton38MousePressed(evt);
            }
        });
        jPanel8.add(jButton38);

        jButton39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png"))); // NOI18N
        jButton39.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton39.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton39.setOpaque(false);
        jButton39.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton39.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton39MousePressed(evt);
            }
        });
        jPanel8.add(jButton39);

        jButton68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton68.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton68.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton68.setOpaque(false);
        jButton68.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton68.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton68MousePressed(evt);
            }
        });
        jPanel8.add(jButton68);

        jPanel7.add(jPanel8, java.awt.BorderLayout.NORTH);

        jTree4.setBackground(new java.awt.Color(204, 255, 255));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree4.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree4.setRootVisible(false);
        jTree4.setShowsRootHandles(true);
        jTree4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree4MousePressed(evt);
            }
        });
        jTree4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTree4KeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(jTree4);

        jPanel7.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel7, java.awt.BorderLayout.WEST);

        jPanel2.setBackground(new java.awt.Color(204, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(700, 32870));
        jPanel2.setMinimumSize(new java.awt.Dimension(700, 122));
        jPanel2.setPreferredSize(new java.awt.Dimension(700, 400));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setBackground(new java.awt.Color(153, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 36));
        jPanel4.setPreferredSize(new java.awt.Dimension(565, 40));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        jPanel4.add(filler3);

        jButton33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton33.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton33.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton33.setOpaque(false);
        jButton33.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton33MousePressed(evt);
            }
        });
        jPanel4.add(jButton33);

        jButton40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Save-icon.png"))); // NOI18N
        jButton40.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton40.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton40.setOpaque(false);
        jButton40.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton40MousePressed(evt);
            }
        });
        jPanel4.add(jButton40);

        jButton41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton41.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton41.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton41.setOpaque(false);
        jButton41.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton41MousePressed(evt);
            }
        });
        jPanel4.add(jButton41);
        jPanel4.add(filler4);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton1.setText("лист доставки");
        jButton1.setOpaque(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1);

        jPanel2.add(jPanel4);

        jPanel9.setBackground(new java.awt.Color(204, 255, 255));
        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 36));
        jPanel9.setMinimumSize(new java.awt.Dimension(0, 36));
        jPanel9.setPreferredSize(new java.awt.Dimension(671, 36));
        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Название:");
        jLabel4.setPreferredSize(new java.awt.Dimension(110, 14));
        jPanel9.add(jLabel4);

        jTextField2.setPreferredSize(new java.awt.Dimension(250, 26));
        jPanel9.add(jTextField2);

        jPanel2.add(jPanel9);

        jPanel10.setBackground(new java.awt.Color(153, 255, 255));
        jPanel10.setMaximumSize(new java.awt.Dimension(32767, 36));
        jPanel10.setMinimumSize(new java.awt.Dimension(0, 36));
        jPanel10.setPreferredSize(new java.awt.Dimension(671, 36));
        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Водитель, авто:");
        jLabel2.setPreferredSize(new java.awt.Dimension(110, 14));
        jPanel10.add(jLabel2);

        jTextField1.setPreferredSize(new java.awt.Dimension(250, 26));
        jPanel10.add(jTextField1);

        jPanel2.add(jPanel10);

        jPanel6.setBackground(new java.awt.Color(204, 255, 255));
        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel6.setPreferredSize(new java.awt.Dimension(565, 32));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 5));

        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Экспедиторы в экипаже");
        jLabel1.setPreferredSize(new java.awt.Dimension(250, 26));
        jPanel6.add(jLabel1);

        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Все экспедиторы");
        jLabel3.setPreferredSize(new java.awt.Dimension(250, 26));
        jPanel6.add(jLabel3);

        jPanel2.add(jPanel6);

        jPanel5.setBackground(new java.awt.Color(204, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(565, 180));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.setBackground(new java.awt.Color(204, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jScrollPane1.setPreferredSize(new java.awt.Dimension(232, 130));

        jList1.setBackground(new java.awt.Color(204, 255, 255));
        jList1.setModel(new DefaultListModel());
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        jPanel5.add(jScrollPane1);

        jPanel3.setBackground(new java.awt.Color(204, 255, 255));
        jPanel3.setMaximumSize(new java.awt.Dimension(35, 32767));
        jPanel3.setMinimumSize(new java.awt.Dimension(35, 43));
        jPanel3.setPreferredSize(new java.awt.Dimension(35, 229));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Actions-arrow-left-double-icon.png"))); // NOI18N
        jButton7.setOpaque(false);
        jButton7.setPreferredSize(new java.awt.Dimension(30, 33));
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton7MousePressed(evt);
            }
        });
        jPanel3.add(jButton7);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Sign-Error-icon.png"))); // NOI18N
        jButton8.setOpaque(false);
        jButton8.setPreferredSize(new java.awt.Dimension(30, 33));
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton8MousePressed(evt);
            }
        });
        jPanel3.add(jButton8);

        jPanel5.add(jPanel3);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(232, 130));

        jList2.setModel(new DefaultListModel());
        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jList2);

        jPanel5.add(jScrollPane2);

        jPanel2.add(jPanel5);

        jPanel13.setBackground(new java.awt.Color(204, 255, 255));
        jPanel13.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel13.setPreferredSize(new java.awt.Dimension(565, 32));
        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 5));

        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Список доставки");
        jLabel5.setPreferredSize(new java.awt.Dimension(250, 26));
        jPanel13.add(jLabel5);

        jPanel2.add(jPanel13);

        jPanel12.setBackground(new java.awt.Color(204, 255, 255));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jScrollPane29.setBackground(new java.awt.Color(153, 255, 255));

        jTable12.setBackground(new java.awt.Color(153, 255, 255));
        jTable12.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable12.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jTable12.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable12MousePressed(evt);
            }
        });
        jTable12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable12KeyReleased(evt);
            }
        });
        jScrollPane29.setViewportView(jTable12);

        jPanel12.add(jScrollPane29, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel12);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("щелчок мыши на пункте в таблице доставки для добавления его в список");
        jLabel6.setPreferredSize(new java.awt.Dimension(250, 26));
        jPanel1.add(jLabel6, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel1);

        jPanel11.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel11, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MousePressed
        int index = jList2.getSelectedIndex();
        if (index!=-1) {
            int id = allExpeditors.getInt(index,"ID");
            boolean idAlreadyAdded = false;
            for (Object[] obj : newListOfExpeditorsToDelivery) {
                if ((Integer)obj[0]==id) {
                    idAlreadyAdded = true;
                    break;
                }
            }
            if (!idAlreadyAdded) {
                newListOfExpeditorsToDelivery.add(new Object[]{id,allExpeditors.getString(index,"NAME")});
                fillListExpeditorsToDelivery();
            }
        }
    }//GEN-LAST:event_jButton7MousePressed

    private void jButton8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MousePressed
        int index = jList1.getSelectedIndex();
        if (index!=-1) {
            newListOfExpeditorsToDelivery.remove(index);
            fillListExpeditorsToDelivery();
        }
    }//GEN-LAST:event_jButton8MousePressed

    private void jTable12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable12MousePressed
        
    }//GEN-LAST:event_jTable12MousePressed

    private void jTable12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable12KeyReleased

    }//GEN-LAST:event_jTable12KeyReleased

    private void jButton38MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton38MousePressed
        db.UpdateSQL("INSERT INTO expeditors(name,driver) values('_НОВЫЙ_','')", new Object[]{});
        mf.getExpeditors();
        mf.fillComboboxExpeditorsInDeliveryFilter();
        makeTreeOfExpeditors();
    }//GEN-LAST:event_jButton38MousePressed

    private void jButton39MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton39MousePressed
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree4.getLastSelectedPathComponent();
        if (SelectedNode == null) {
            return;
        }
        this.setVisible(false);
        if (JOptionPane.showConfirmDialog(null, "Действительно удалить экипаж " + mf.Expeditors.getString("NAME") + " ?", "Удаление экипажа", JOptionPane.YES_NO_OPTION) == 0) {
            db.UpdateSQL("DELETE FROM expeditors_user WHERE id_expeditors=?", new Object[]{mf.Expeditors.getInt("ID")});
            if (db.UpdateSQL("DELETE FROM expeditors WHERE id=?", new Object[]{mf.Expeditors.getInt("ID")})) {
                mf.getExpeditors();
                mf.fillComboboxExpeditorsInDeliveryFilter();
                makeTreeOfExpeditors();
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        }      
        this.setVisible(true);
    }//GEN-LAST:event_jButton39MousePressed

    private void jButton68MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton68MousePressed
        mf.getExpeditors();
        makeTreeOfExpeditors();
    }//GEN-LAST:event_jButton68MousePressed

    private void jTree4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree4MousePressed
        selectNodeOfTreeExpeditors();
    }//GEN-LAST:event_jTree4MousePressed

    private void jTree4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTree4KeyReleased
        selectNodeOfTreeExpeditors();
    }//GEN-LAST:event_jTree4KeyReleased

    private void jButton33MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton33MousePressed
        editMode = true;
        selectNodeOfTreeExpeditors();
    }//GEN-LAST:event_jButton33MousePressed

    private void jButton40MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton40MousePressed
        String name = jTextField2.getText();
        if (name.isEmpty()) {
            this.setVisible(false);
            JOptionPane.showMessageDialog(null, "Заполните название экипажа");
            this.setVisible(true);
            return;
        }
        if (db.UpdateSQL("UPDATE expeditors SET name=?, driver=? WHERE id=?", new Object[]{name,jTextField1.getText(),mf.Expeditors.getInt("ID")})) {
            /////////////////////////////////////////////////
            LinkedList<Integer> idToAdd = new LinkedList<>();
            LinkedList<Integer> idToRemove = new LinkedList<>();
            
            for (Object[] obj : newListOfExpeditorsToDelivery) {
                int id = (Integer)obj[0];
                boolean exists = false;
                for (int i = 0; i<expeditorsToDelivery.getLength(); i++) {
                    if (id==expeditorsToDelivery.getInt(i,"ID")) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    idToAdd.add(id);
                }
            }
            for (int i = 0; i<expeditorsToDelivery.getLength(); i++) {
                int id = expeditorsToDelivery.getInt(i,"ID");
                boolean exists = false;
                for (Object[] obj : newListOfExpeditorsToDelivery) {                
                    if (id==(Integer)obj[0]) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    idToRemove.add(id);
                }
            }
            
            if (!idToAdd.isEmpty()) {
                StringBuilder sql = new StringBuilder("INSERT INTO expeditors_user(id_expeditors,id_user) VALUES ");
                for (Integer id : idToAdd) {
                    sql.append("(").append(mf.Expeditors.get("ID")).append(",").append(id).append("),");
                }
                sql.delete(sql.length()-1, sql.length());
                db.UpdateSQL(sql.toString(), new Object[]{});
            }
            
            if (!idToRemove.isEmpty()) {
                StringBuilder sql = new StringBuilder("DELETE FROM expeditors_user WHERE id_user IN (");
                for (Integer id : idToRemove) {
                    sql.append(id).append(",");
                }
                sql.delete(sql.length()-1, sql.length());
                sql.append(")");
                db.UpdateSQL(sql.toString(), new Object[]{});
            }
            ////////////////////////////////////
            idToAdd = new LinkedList<>();
            idToRemove = new LinkedList<>();
            
            for (Object[] obj : newListOfDeliveryItemsToDelivery) {
                int id = (Integer)obj[0];
                boolean exists = false;
                for (int i = 0; i<deliveryItemsToDelivery.getLength(); i++) {
                    if (id==deliveryItemsToDelivery.getInt(i,"ID")) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    idToAdd.add(id);
                }
            }
            for (int i = 0; i<deliveryItemsToDelivery.getLength(); i++) {
                int id = deliveryItemsToDelivery.getInt(i,"ID");
                boolean exists = false;
                for (Object[] obj : newListOfDeliveryItemsToDelivery) {                
                    if (id==(Integer)obj[0]) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    idToRemove.add(id);
                }
            }
            if (!idToAdd.isEmpty()) {
                for (int id : idToAdd) {
                    db.UpdateSQL("UPDATE delivery SET id_expeditors=? WHERE id=?", new Object[]{mf.Expeditors.getInt("ID"),id});
                }
            }
            if (!idToRemove.isEmpty()) {
                for (int id : idToRemove) {
                    db.UpdateSQL("UPDATE delivery SET id_expeditors=NULL,expeditors_delivery_order=NULL WHERE id=?", new Object[]{id});
                }
            }
            ////////////////////////////////////
            for (int i=0;i<newListOfDeliveryItemsToDelivery.size();i++) {
                Object[] obj = newListOfDeliveryItemsToDelivery.get(i);
                db.UpdateSQL("UPDATE delivery SET expeditors_delivery_order=? WHERE id=?", new Object[]{i+1,(Integer)obj[0]});
            }
            ////////////////////////////////////
            editMode = false;
            mf.getExpeditors();
            mf.fillComboboxExpeditorsInDeliveryFilter();
            makeTreeOfExpeditors();
            
            if (!idToAdd.isEmpty() || !idToRemove.isEmpty()) {
                mf.GetDelivery();
                mf.MakeTableOfDelivery();
            }
        } else {
            this.setVisible(false);
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            this.setVisible(true);
        }
    }//GEN-LAST:event_jButton40MousePressed

    private void jButton41MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton41MousePressed
        editMode = false;
        selectNodeOfTreeExpeditors();
    }//GEN-LAST:event_jButton41MousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        printExpeditorsList();
    }//GEN-LAST:event_jButton1ActionPerformed

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
//            java.util.logging.Logger.getLogger(ExpeditorsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ExpeditorsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ExpeditorsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ExpeditorsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                ExpeditorsDialog dialog = new ExpeditorsDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton68;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane5;
    /*
    private javax.swing.JTable jTable12;
    */
    private MainPackage.TableDeliveryExpeditors jTable12;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    /*
    private javax.swing.JTree jTree4;
    */
    private ExpeditorsTree jTree4;
    // End of variables declaration//GEN-END:variables
}
