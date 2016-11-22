package MainPackage;

import static MainPackage.MainForm.DarkInterfaceColor;
import static MainPackage.MainForm.FormatUAH;
import static MainPackage.MainForm.LightInterfaceColor;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

public class DeliveryDialog extends javax.swing.JDialog {

    private final MainForm mf;
    private final DATABASE db;
    
    private final ContactChooseDialog contactChooseDialog;
    private final ChooseDeliveryAddressDialog chooseDeliveryAddressDialog;
    private final ChooseDeliveryContentsDialog chooseDeliveryContentsDialog;
    
    public DeliveryDialog(MainForm mf, DATABASE db) {
        super();
        initComponents();
        
        this.mf = mf;
        this.db = db;
        
        jDateChooser6.setCalendar(Calendar.getInstance());
        contactChooseDialog = new ContactChooseDialog(this,mf,db);
        chooseDeliveryAddressDialog = new ChooseDeliveryAddressDialog(this,mf,db);
        chooseDeliveryContentsDialog = new ChooseDeliveryContentsDialog(this,mf);
    }
    
    
    
    public void initDialog(boolean isThisEditing) {
        jComboBox1.removeAllItems();
        jComboBox1.addItem(" ");
        jComboBox1.addItem("По Киеву и обл.");
        jComboBox1.addItem("По Украине");
        jComboBox1.addItem("Самовывоз");
        jComboBox1.addItem("Самовывоз Склад");
        jComboBox1.addItem("Самовывоз Офис");
        jComboBox2.removeAllItems();
        jComboBox2.addItem(" ");
        jComboBox2.addItem("Мы");
        jComboBox2.addItem("Клиент");
        jComboBox3.removeAllItems();
        jComboBox3.addItem("Выберите презент");
        jComboBox3.addItem("Нет презента");
        jComboBox3.addItem("Презент - маленький");
        jComboBox3.addItem("Презент - средний");
        jComboBox3.addItem("Презент - большой");
        jComboBox3.addItem("Презент - VIP");
        jPanel138.setVisible(false);
        
        String comboBoxSetter1;
        String comboBoxSetter2;
        String comboBoxSetter3;
        
        int indexForIf = mf.getTableOrderSelectedRow();
        if ((mf.CurrentUser.getInt("LEVEL") == 1) && (mf.Orders.getInt(indexForIf, "STATE") >= Order.ORDER_PACK)) {
            jTextField37.setEditable(false);
        } else {
            jTextField37.setEditable(true);
        }
        
        if (isThisEditing) { //editing
            int index = mf.getOrderDeliverySelectedRow();
            if (index != -1) {
                jDateChooser6.setDate(new Date(mf.DeliveryOrder.getInt(index, "DATE_TIME") * 1000L));
                jDateChooser7.setDate(new Date(mf.DeliveryOrder.getInt(index, "DATE_DELIVERY_COUNTRY") * 1000L));
                jTextArea1.setText(mf.DeliveryOrder.getString(index, "CONTACT"));
                comboBoxSetter1 = mf.DeliveryOrder.getString(index, "DELIVERY_TYPE");
                switch (comboBoxSetter1) {
                    case "По Киеву и обл.":
                        jComboBox1.setSelectedIndex(1);
                        break;
                    case "По Украине":
                        jComboBox1.setSelectedIndex(2);
                        jPanel138.setVisible(true);
                        break;
                    case "Самовывоз":
                        jComboBox1.setSelectedIndex(3);
                        break;
                    case "Самовывоз Склад":
                        jComboBox1.setSelectedIndex(4);
                        break;
                    case "Самовывоз Офис":
                        jComboBox1.setSelectedIndex(5);
                        break;
                    default:
                        jComboBox1.setSelectedIndex(0);
                }
                comboBoxSetter2 = mf.DeliveryOrder.getString(index, "WHO_PAYS");
                switch (comboBoxSetter2) {
                    case "Мы":
                        jComboBox2.setSelectedIndex(1);
                        break;
                    case "Клиент":
                        jComboBox2.setSelectedIndex(2);
                        break;
                    default:
                        jComboBox2.setSelectedIndex(0);
                }
                comboBoxSetter3 = mf.DeliveryOrder.getString(index, "PRESENT");
                switch (comboBoxSetter3) {
                    case "Нет презента":
                        jComboBox3.setSelectedIndex(1);
                        break;
                    case "Презент - маленький":
                        jComboBox3.setSelectedIndex(2);
                        break;
                    case "Презент - средний":
                        jComboBox3.setSelectedIndex(3);
                        break;
                    case "Презент - большой":
                        jComboBox3.setSelectedIndex(4);
                        break;
                    case "Презент - VIP":
                        jComboBox3.setSelectedIndex(5);
                        break;
                    default:
                        jComboBox3.setSelectedIndex(0);
                }
                jTextArea2.setText(mf.DeliveryOrder.getString(index, "ADDRESS"));
                jTextArea3.setText(mf.DeliveryOrder.getString(index, "CONTENT"));
                jTextArea4.setText(mf.DeliveryOrder.getString(index, "COMM"));
                jTextField37.setText(FormatUAH.format(mf.DeliveryOrder.getDouble(index, "DEBT")));
                
                this.setTitle("Редактировать доставку");
                this.pack();
                this.setLocationRelativeTo(null);
                this.setVisible(true);
            }         
        } else { //adding
            jDateChooser6.setDate(null);
            jTextArea1.setText("");
            jTextArea2.setText("");
            jTextArea3.setText("");
            jTextArea4.setText("");
            jTextField37.setText("");
            this.setTitle("Добавить доставку");
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);        
        }
    }
    
    public void setContactPerson(String s) {
        s = s.replace("<html>", "");
        s = s.replace("</html>", "");
        s = s.replace("<br>", "\n");        
        jTextArea1.setText(s);
    }
    
    public void setDeliveryAddress(String s) {
        jTextArea2.setText(s);
    }
    
    public void addContents(String s) {
        jTextArea3.setText(jTextArea3.getText()+s);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel135 = new javax.swing.JPanel();
        jPanel137 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jPanel139 = new javax.swing.JPanel();
        jDateChooser6 = new com.toedter.calendar.JDateChooser();
        jLabel105 = new javax.swing.JLabel();
        jTextField37 = new javax.swing.JTextField();
        jButton104 = new javax.swing.JButton();
        jPanel145 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel140 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jScrollPane33 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton94 = new javax.swing.JButton();
        jPanel144 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel138 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jPanel147 = new javax.swing.JPanel();
        jDateChooser7 = new com.toedter.calendar.JDateChooser();
        jPanel141 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane34 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton95 = new javax.swing.JButton();
        jPanel142 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jScrollPane35 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jButton96 = new javax.swing.JButton();
        jPanel146 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jPanel143 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        jScrollPane36 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jPanel136 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton92 = new javax.swing.JButton();
        jButton93 = new javax.swing.JButton();

        setTitle("Доставка");

        jPanel135.setBackground(new java.awt.Color(204, 255, 255));
        jPanel135.setPreferredSize(new java.awt.Dimension(413, 514));
        jPanel135.setLayout(new javax.swing.BoxLayout(jPanel135, javax.swing.BoxLayout.Y_AXIS));

        jPanel137.setBackground(new java.awt.Color(204, 255, 255));
        jPanel137.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel30.setForeground(new java.awt.Color(0, 0, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Дата доставки:");
        jLabel30.setPreferredSize(new java.awt.Dimension(110, 20));
        jPanel137.add(jLabel30);

        jPanel139.setPreferredSize(new java.awt.Dimension(130, 24));
        jPanel139.setLayout(new java.awt.BorderLayout());

        jDateChooser6.setDateFormatString("dd MMM yyyy");
        jDateChooser6.setPreferredSize(new java.awt.Dimension(100, 24));
        jPanel139.add(jDateChooser6, java.awt.BorderLayout.CENTER);

        jPanel137.add(jPanel139);

        jLabel105.setForeground(new java.awt.Color(0, 0, 255));
        jLabel105.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel105.setText("Долг:");
        jLabel105.setPreferredSize(new java.awt.Dimension(40, 20));
        jPanel137.add(jLabel105);

        jTextField37.setPreferredSize(new java.awt.Dimension(70, 24));
        jPanel137.add(jTextField37);

        jButton104.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton104.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton104.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton104.setOpaque(false);
        jButton104.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton104.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton104MousePressed(evt);
            }
        });
        jPanel137.add(jButton104);

        jPanel135.add(jPanel137);

        jPanel145.setBackground(new java.awt.Color(204, 255, 255));
        jPanel145.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel56.setForeground(new java.awt.Color(0, 0, 255));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("Кто платит:");
        jLabel56.setPreferredSize(new java.awt.Dimension(110, 20));
        jPanel145.add(jLabel56);

        jPanel145.add(jComboBox2);

        jPanel135.add(jPanel145);

        jPanel140.setBackground(new java.awt.Color(153, 255, 255));
        jPanel140.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel51.setForeground(new java.awt.Color(0, 0, 255));
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel51.setText("Контактное лицо:");
        jLabel51.setPreferredSize(new java.awt.Dimension(110, 20));
        jPanel140.add(jLabel51);

        jScrollPane33.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane33.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane33.setPreferredSize(new java.awt.Dimension(250, 50));

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane33.setViewportView(jTextArea1);

        jPanel140.add(jScrollPane33);

        jButton94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton94.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton94.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton94.setOpaque(false);
        jButton94.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton94.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton94MousePressed(evt);
            }
        });
        jPanel140.add(jButton94);

        jPanel135.add(jPanel140);

        jPanel144.setBackground(new java.awt.Color(204, 255, 255));
        jPanel144.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel55.setForeground(new java.awt.Color(0, 0, 255));
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel55.setText("Вид доставки:");
        jLabel55.setPreferredSize(new java.awt.Dimension(110, 20));
        jPanel144.add(jLabel55);

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel144.add(jComboBox1);

        jPanel135.add(jPanel144);

        jPanel138.setBackground(new java.awt.Color(153, 255, 255));
        jPanel138.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel31.setForeground(new java.awt.Color(0, 0, 255));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Дата доставки клиенту:");
        jLabel31.setPreferredSize(new java.awt.Dimension(140, 20));
        jPanel138.add(jLabel31);

        jPanel147.setPreferredSize(new java.awt.Dimension(130, 24));
        jPanel147.setLayout(new java.awt.BorderLayout());

        jDateChooser7.setDateFormatString("dd MMM yyyy");
        jDateChooser7.setPreferredSize(new java.awt.Dimension(100, 24));
        jPanel147.add(jDateChooser7, java.awt.BorderLayout.CENTER);

        jPanel138.add(jPanel147);

        jPanel135.add(jPanel138);

        jPanel141.setBackground(new java.awt.Color(204, 255, 255));
        jPanel141.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel54.setForeground(new java.awt.Color(0, 0, 255));
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel54.setText("Адрес доставки:");
        jLabel54.setPreferredSize(new java.awt.Dimension(110, 20));
        jPanel141.add(jLabel54);

        jScrollPane34.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane34.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane34.setPreferredSize(new java.awt.Dimension(250, 50));

        jTextArea2.setColumns(20);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setWrapStyleWord(true);
        jScrollPane34.setViewportView(jTextArea2);

        jPanel141.add(jScrollPane34);

        jButton95.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton95.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton95.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton95.setOpaque(false);
        jButton95.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton95.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton95MousePressed(evt);
            }
        });
        jPanel141.add(jButton95);

        jPanel135.add(jPanel141);

        jPanel142.setBackground(new java.awt.Color(153, 255, 255));
        jPanel142.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel63.setForeground(new java.awt.Color(0, 0, 255));
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel63.setText("Содержимое:");
        jLabel63.setPreferredSize(new java.awt.Dimension(110, 20));
        jPanel142.add(jLabel63);

        jScrollPane35.setPreferredSize(new java.awt.Dimension(250, 100));

        jTextArea3.setColumns(20);
        jTextArea3.setLineWrap(true);
        jTextArea3.setRows(5);
        jTextArea3.setWrapStyleWord(true);
        jScrollPane35.setViewportView(jTextArea3);

        jPanel142.add(jScrollPane35);

        jButton96.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton96.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton96.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton96.setOpaque(false);
        jButton96.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton96.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton96MousePressed(evt);
            }
        });
        jPanel142.add(jButton96);

        jPanel135.add(jPanel142);

        jPanel146.setBackground(new java.awt.Color(204, 255, 255));
        jPanel146.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel57.setForeground(new java.awt.Color(0, 0, 255));
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel57.setText("Презент:");
        jLabel57.setPreferredSize(new java.awt.Dimension(110, 20));
        jPanel146.add(jLabel57);

        jPanel146.add(jComboBox3);

        jPanel135.add(jPanel146);

        jPanel143.setBackground(new java.awt.Color(204, 255, 255));
        jPanel143.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel67.setForeground(new java.awt.Color(0, 0, 255));
        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel67.setText("Комментарий:");
        jLabel67.setPreferredSize(new java.awt.Dimension(110, 20));
        jPanel143.add(jLabel67);

        jScrollPane36.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane36.setPreferredSize(new java.awt.Dimension(250, 50));

        jTextArea4.setColumns(20);
        jTextArea4.setLineWrap(true);
        jTextArea4.setRows(5);
        jTextArea4.setWrapStyleWord(true);
        jScrollPane36.setViewportView(jTextArea4);

        jPanel143.add(jScrollPane36);

        jPanel135.add(jPanel143);

        jPanel136.setBackground(new java.awt.Color(153, 255, 255));

        jButton92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png"))); // NOI18N
        jButton92.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton92MousePressed(evt);
            }
        });
        jPanel136.add(jButton92);

        jButton93.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton93.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton93MousePressed(evt);
            }
        });
        jPanel136.add(jButton93);

        jPanel135.add(jPanel136);

        getContentPane().add(jPanel135, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton104MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton104MousePressed
        jTextField37.setText(mf.getOrderDebtValueFromItsLabel());
    }//GEN-LAST:event_jButton104MousePressed

    private void jButton94MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton94MousePressed
        contactChooseDialog.initDialog();
    }//GEN-LAST:event_jButton94MousePressed

    private void jButton95MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton95MousePressed
        chooseDeliveryAddressDialog.initDialog();
    }//GEN-LAST:event_jButton95MousePressed

    private void jButton96MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton96MousePressed
        chooseDeliveryContentsDialog.initDialog();
    }//GEN-LAST:event_jButton96MousePressed

    private void jButton92MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton92MousePressed
        double debt;
        try {
            debt = jTextField37.getText().equals("") ? 0 : (Double.parseDouble(jTextField37.getText().replaceAll(" ", "").replace(',', '.')));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Неверно введена сумма долга");
            return;
        }
        if (jComboBox3.getSelectedItem().toString().equalsIgnoreCase("Выберите презент")) {
            JOptionPane.showMessageDialog(null, "Необходимо выбрать тип презента");
        } else {
            if (jComboBox1.getSelectedItem().toString().equalsIgnoreCase("По Украине")) {
                if (this.getTitle().equals("Добавить доставку")) {
                    int state = mf.Orders.getInt("STATE");
                    if (db.UpdateSQL("INSERT INTO delivery(id_orders,date_time,date_delivery_country,date_send,"
                            + "date_close,contact,who_pays,delivery_type,address,content,present,comm,state,"
                            + "cost,payment,debt,courier) VALUES(?,?,?,0,0,?,?,?,?,?,?,?,?,0,0,?,0)",
                            new Object[]{mf.Orders.getInt("ID"),jDateChooser6.getCalendar().getTimeInMillis()/1000,
                                jDateChooser7.getCalendar().getTimeInMillis()/1000,
                                jTextArea1.getText(),jComboBox2.getSelectedItem().toString(), 
                                jComboBox1.getSelectedItem().toString(), jTextArea2.getText(),
                                jTextArea3.getText(), jComboBox3.getSelectedItem().toString(),
                                jTextArea4.getText(),state,debt})) {
                        this.setVisible(false);
                        mf.SelectNodeOfTableOrders();
                    } else {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                    }
                } else {
                    int id_delivery = mf.DeliveryOrder.getInt(mf.getOrderDeliverySelectedRow(),"ID");
                    if (db.UpdateSQL("UPDATE delivery SET date_time=?,date_delivery_country=?,contact=?,who_pays=?,delivery_type=?,"
                            + "address=?,content=?,present=?,comm=?,debt=? WHERE id=?",
                            new Object[]{jDateChooser6.getCalendar().getTimeInMillis()/1000,jDateChooser7.getCalendar().getTimeInMillis()/1000,
                                jTextArea1.getText(),jComboBox2.getSelectedItem().toString(),jComboBox1.getSelectedItem().toString(),
                                jTextArea2.getText(),jTextArea3.getText(),jComboBox3.getSelectedItem().toString(),jTextArea4.getText(),
                                debt,id_delivery})) {
                        this.setVisible(false);
                        mf.SelectNodeOfTableOrders();
                    } else {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                    }
                }
            } else {
                if (this.getTitle().equals("Добавить доставку")) {
                    int state = mf.Orders.getInt("STATE");
                    if (db.UpdateSQL("INSERT INTO delivery(id_orders,date_time,date_send,"
                            + "date_close,contact,who_pays,delivery_type,address,content,present,comm,state,"
                            + "cost,payment,debt,courier) VALUES(?,?,0,0,?,?,?,?,?,?,?,?,0,0,?,0)",
                            new Object[]{mf.Orders.getInt("ID"),jDateChooser6.getCalendar().getTimeInMillis()/1000,
                                jTextArea1.getText(),jComboBox2.getSelectedItem().toString(), 
                                jComboBox1.getSelectedItem().toString(), jTextArea2.getText(),
                                jTextArea3.getText(), jComboBox3.getSelectedItem().toString(),
                                jTextArea4.getText(),state,debt})) {
                        this.setVisible(false);
                        mf.SelectNodeOfTableOrders();
                    } else {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                    }
                } else {
                    int id_delivery = mf.DeliveryOrder.getInt(mf.getOrderDeliverySelectedRow(),"ID");
                    if (db.UpdateSQL("UPDATE delivery SET date_time=?,contact=?,who_pays=?,delivery_type=?,"
                            + "address=?,content=?,present=?,comm=?,debt=? WHERE id=?",
                            new Object[]{jDateChooser6.getCalendar().getTimeInMillis()/1000,
                                jTextArea1.getText(),jComboBox2.getSelectedItem().toString(),jComboBox1.getSelectedItem().toString(),
                                jTextArea2.getText(),jTextArea3.getText(),jComboBox3.getSelectedItem().toString(),jTextArea4.getText(),
                                debt,id_delivery})) {
                        this.setVisible(false);
                        mf.SelectNodeOfTableOrders();
                    } else {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                    }
                }
            }
            
        }
    }//GEN-LAST:event_jButton92MousePressed

    private void jButton93MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton93MousePressed
        this.setVisible(false);
    }//GEN-LAST:event_jButton93MousePressed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (jComboBox1.getSelectedIndex()==2) {
            JOptionPane.showMessageDialog(null, "Не забудьте указать ФИО клиента и ориентировочную дату доставки!");
            jLabel30.setText("Дата отгрузки:");
            jPanel138.setVisible(true);
        } else {
            jLabel30.setText("Дата доставки:");
            jPanel138.setVisible(false);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

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
//            java.util.logging.Logger.getLogger(DeliveryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(DeliveryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(DeliveryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(DeliveryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                DeliveryDialog dialog = new DeliveryDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton104;
    private javax.swing.JButton jButton92;
    private javax.swing.JButton jButton93;
    private javax.swing.JButton jButton94;
    private javax.swing.JButton jButton95;
    private javax.swing.JButton jButton96;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser6;
    private com.toedter.calendar.JDateChooser jDateChooser7;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JPanel jPanel135;
    /*
    private javax.swing.JPanel jPanel136;
    */
    private GradientPanel jPanel136;
    private javax.swing.JPanel jPanel137;
    private javax.swing.JPanel jPanel138;
    private javax.swing.JPanel jPanel139;
    private javax.swing.JPanel jPanel140;
    private javax.swing.JPanel jPanel141;
    private javax.swing.JPanel jPanel142;
    private javax.swing.JPanel jPanel143;
    private javax.swing.JPanel jPanel144;
    private javax.swing.JPanel jPanel145;
    private javax.swing.JPanel jPanel146;
    private javax.swing.JPanel jPanel147;
    private javax.swing.JScrollPane jScrollPane33;
    private javax.swing.JScrollPane jScrollPane34;
    private javax.swing.JScrollPane jScrollPane35;
    private javax.swing.JScrollPane jScrollPane36;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextField jTextField37;
    // End of variables declaration//GEN-END:variables
}
