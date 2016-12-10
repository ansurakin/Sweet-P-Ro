package MainPackage;

import java.awt.*;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.*;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class MainForm extends javax.swing.JApplet {
    
    private static enum CandiesOrder {ALPHABET, COST, RELATIVE_COST};

    public ObjectResultSet Candies = new ObjectResultSet();
    public ObjectResultSet Users = new ObjectResultSet();
    public ObjectResultSet Clients = new ObjectResultSet();
    public ObjectResultSet ClientsToCheck = new ObjectResultSet();
    public ObjectResultSet ExtendedClient = new ObjectResultSet();
    public ObjectResultSet CurrentUser = new ObjectResultSet();
    public ObjectResultSet Gifts = new ObjectResultSet();
//    public ObjectResultSet GiftFolders = new ObjectResultSet();
    public ObjectResultSet Gift_Candy = new ObjectResultSet();
    public ObjectResultSet Packings = new ObjectResultSet();
    public ObjectResultSet FilteredPackings = new ObjectResultSet();
    public ObjectResultSet Orders = new ObjectResultSet();
    public ObjectResultSet ExtendedOrder = new ObjectResultSet();
    public ObjectResultSet SubOrders = new ObjectResultSet();
    public ObjectResultSet OrdersCount = new ObjectResultSet();
    public ObjectResultSet Storage = new ObjectResultSet();
    public ObjectResultSet Constants = new ObjectResultSet();
    public ObjectResultSet Delivery = new ObjectResultSet();
    public ObjectResultSet DeliveryOrder = new ObjectResultSet();
    public ObjectResultSet Finances = new ObjectResultSet();
    public ObjectResultSet Expenses = new ObjectResultSet();
    public ObjectResultSet Folders = new ObjectResultSet();
    public ObjectResultSet Clients_Folders = new ObjectResultSet();
    public ObjectResultSet Expeditors = new ObjectResultSet();
    
    public static final DecimalFormat FormatUAH = new DecimalFormat("0.00");
    public static final DecimalFormat FormatKG = new DecimalFormat("0.000");
    public static final DecimalFormat FormatPercent = new DecimalFormat("0");
    
    public static final Color LightInterfaceColor =  new Color(204,255,255);
    public static final Color DarkInterfaceColor =  new Color(153,255,255);
    
    public static final int ACCESS_DENIED = -1;
    public static final int DIRECTOR = 0;
    public static final int MANAGER = 1;
    public static final int STORAGE_HEAD = 2;
    public static final int LOGIST = 3;
    public static final int STORAGE_WORKER = 4;
    
    public static final int STORAGE_WORKER_BRIGADIER = 0;
    public static final int STORAGE_WORKER_FORWARDER = 1;
    public static final int STORAGE_WORKER_PACKER = 2;    
    public static final int STORAGE_WORKER_LOADER = 3;
    
    private int userLevel = ACCESS_DENIED;        
    public File ChoosedDirectory = null; 
    private boolean sessionIsBlocked = false;
    
    private boolean CanSeeCandies = false;
    private boolean CanEditCandies = false;
    private boolean CanDeleteCandies = false;
    private boolean CanSeePackings = false;
    private boolean CanEditPackings = false;
    private boolean CanDeletePackings = false;
    private boolean CanSeeGifts = false;
    private boolean CanEditGifts = false;
    private boolean CanDeleteGifts = false;
    private boolean CanSeeClients = false;
    private boolean CanEditClients = false;
    private boolean CanDeleteClients = false;
    private boolean CanSeeStorage = false;
    private boolean CanEditStorage = false;
    private boolean CanSeeUsers = false;
    private boolean CanEditUsers = false;
    private boolean CanDeleteUsers = false;
    private boolean CanSeeConst = false;
    private boolean CanEditConst = false;
    private boolean CanSeeOrders = false;
    private boolean CanMakeOrders = false;
    private boolean CanEditOrders = false;
    private boolean CanPayOrders = false;
    private boolean CanPackOrders = false;
    private boolean CanDeleteOrders = false;
    private boolean CanSeeDelivery = false;
    private boolean CanSeeFinances = false;
    private boolean CanEditExpeditors = false;
    private boolean CanSeeOrdersCount = false;
    private boolean newClientMode = false;
    
    private boolean clientDraggedInClientsTree = false;
    private int draggedClientID;
    private int draggedFromFolderID;
    private int draggedToFolderID;
    private String draggedClientName;
    private String draggedFromFolderName;
    String filterName = "";

    DATABASE db;
    OrderComplectationDialog orderComplectationDialog;
    ClientChooseDialog clientChooseDialog;
    RevenueExpensesDialog revenueExpensesDialog;
    ChoosePackingWorkersDialog choosePackingWorkersDialog;
    DeliveryDialog deliveryDialog;
    ChooseWorkersForPackingDialog chooseWorkersForPackingDialog;
    ProductDeliveryDialog productDeliveryDialog;
    ExpensesDialog expensesDialog;
    LabelPrintDialog labelPrintDialog;
    ChooseConsolidatedOrdersDialog chooseConsolidatedOrdersDialog;
    ExpeditorsDialog expeditorsDialog;
    IncomeCallDialog incomeCallDialog;
    
    @Override
    public void init() {
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    
                    try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                            if ("Nimbus".equals(info.getName())) {
                                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                break;
                            }
                        }
                    } catch ( ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }                                           

                    initComponents();
                    
                    String host = "localhost";
                    try {
                        host = MainForm.this.getDocumentBase().getHost();
                        host = host.isEmpty() ? "localhost" : host;
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        try {
                            BasicService bs = (BasicService)ServiceManager.lookup("javax.jnlp.BasicService");
                            URL codeBase = bs.getCodeBase();
                            host = codeBase.getHost();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    System.out.println(host);
                    db = new DATABASE(MainForm.this,host);

                    jComboBox4.removeAllItems();
                    int startYear = 2010;
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    for (int i = startYear; i<=currentYear; i++) {
                        jComboBox4.addItem(i);
                    }
                    jComboBox4.setSelectedIndex(jComboBox4.getItemCount()-1);
                    
                    jComboBox5.removeAllItems();
                    startYear = 2010;
                    currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    for (int i = startYear; i<=currentYear; i++) {
                        jComboBox5.addItem(i);
                    }
                    jComboBox5.setSelectedIndex(jComboBox5.getItemCount()-1);
                    
                    jTable1.setShowGrid(true);
                    jTable1.setGridColor(Color.GRAY);
                    jTable2.setShowGrid(true);
                    jTable2.setGridColor(Color.GRAY);
                    jTable3.setShowGrid(true);
                    jTable3.setGridColor(Color.GRAY);
                    jTable4.setShowGrid(true);
                    jTable4.setGridColor(Color.GRAY);
                    jTable5.setShowGrid(true);
                    jTable5.setGridColor(Color.GRAY);
                    jTable6.setShowGrid(true);
                    jTable6.setGridColor(Color.GRAY);
                    jTable7.setShowGrid(true);
                    jTable7.setGridColor(Color.GRAY);
                    jTable8.setShowGrid(true);
                    jTable8.setGridColor(Color.GRAY);                    
                    jTable9.setShowGrid(true);
                    jTable9.setGridColor(Color.GRAY);
//                    jTable12.getDefaultEditor(String.class).addCellEditorListener(EditConditers);
                    jTable10.setShowGrid(true);
                    jTable10.setGridColor(Color.GRAY);
                    jTable11.setShowGrid(true);
                    jTable11.setGridColor(Color.GRAY);
                    jTable12.setShowGrid(true);
                    jTable12.setGridColor(Color.GRAY);
                    jTable12.getDefaultEditor(String.class).addCellEditorListener(ChangeNotification);
                    jTable13.setShowGrid(true);
                    jTable13.setGridColor(Color.GRAY);
                    jTable14.setShowGrid(true);
                    jTable14.setGridColor(Color.GRAY);
                    jTable16.setShowGrid(true);
                    jTable16.setGridColor(Color.GRAY);
                    jTable17.setShowGrid(true);
                    jTable17.setGridColor(Color.GRAY);
                    jTable19.setShowGrid(true);
                    jTable19.setGridColor(Color.GRAY);
                    jTable20.setShowGrid(true);
                    jTable20.setGridColor(Color.GRAY);
                    jTable22.setShowGrid(true);
                    jTable22.setGridColor(Color.GRAY);
                    
                    jDateChooser1.setCalendar(Calendar.getInstance());
                    jDateChooser2.setCalendar(Calendar.getInstance());
                    jDateChooser3.setCalendar(Calendar.getInstance());
                    jDateChooser4.setCalendar(Calendar.getInstance());
                    jDateChooser5.setCalendar(Calendar.getInstance());
                    jDateChooser7.setCalendar(Calendar.getInstance());
                    jDateChooser8.setCalendar(Calendar.getInstance());
                    jDateChooser9.setCalendar(Calendar.getInstance());
                    jDateChooser10.setCalendar(Calendar.getInstance());
                    jDateChooser11.setCalendar(Calendar.getInstance());
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MONTH, Calendar.JANUARY);
                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    jDateChooser13.setCalendar(cal);
                    jDateChooser14.setCalendar(Calendar.getInstance());
                    
                    
                    jScrollPane6.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane7.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane10.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane11.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane12.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane17.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane13.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane21.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane23.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane24.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane26.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane29.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane31.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane39.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane40.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane41.getViewport().setBackground(LightInterfaceColor);
                    jScrollPane43.getViewport().setBackground(LightInterfaceColor);              

                    JLabel lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("КОНФЕТЫ ");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/candy-cane-icon.png")));
                    jTabbedPane1.setTabComponentAt(0, lbl);
                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("УПАКОВКА");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/upakovka_146.png")));
                    jTabbedPane1.setTabComponentAt(1, lbl);                    
                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("НАБОРЫ  ");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/gift-icon.png")));
                    jTabbedPane1.setTabComponentAt(2, lbl);  
                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("КЛИЕНТЫ ");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/Office-Client-Male-Dark-icon.png")));
                    jTabbedPane1.setTabComponentAt(3, lbl);  
                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("ЗАКАЗЫ  ");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/order-history-icon.png")));
                    jTabbedPane1.setTabComponentAt(4, lbl);  
                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("СКЛАД   ");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/lock-icon.png")));
                    jTabbedPane1.setTabComponentAt(5, lbl);  
                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("ДОСТАВКА");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/Lorry-icon2.png")));
                    jTabbedPane1.setTabComponentAt(6, lbl);  
                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("КАДРЫ   ");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/user-group-icon.png")));
                    jTabbedPane1.setTabComponentAt(7, lbl);  
                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("CONST   ");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/gear-icon.png")));
                    jTabbedPane1.setTabComponentAt(8, lbl);  
                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("ФИНАНСЫ ");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/coins-icon_big.png")));
                    jTabbedPane1.setTabComponentAt(9, lbl);  

                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("Конфеты");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/candy-icon.png")));
                    jTabbedPane2.setTabComponentAt(0, lbl);  
                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("Упаковка");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/upakovka_146_2.png")));
                    jTabbedPane2.setTabComponentAt(1, lbl); 
                    lbl = new JLabel();
                    lbl.setForeground(Color.BLUE);
                    lbl.setIconTextGap(5);
                    lbl.setHorizontalTextPosition(SwingConstants.RIGHT);                    
                    lbl.setText("Заказ для склада");
                    lbl.setIcon(new ImageIcon(getClass().getResource("/Images/shopping-cart-icon.png")));
                    jTabbedPane2.setTabComponentAt(2, lbl);                     
                    
                    Authorization();
                }
            });
        } catch (InterruptedException | InvocationTargetException ex) {
            System.out.println(ex.getMessage() + " 363");
        }
    }

    public void ShowStartScreen() {
        CardLayout cl = (CardLayout)jPanel1.getParent().getLayout();
        cl.show(jPanel1.getParent(), "card2");
    }
    

    
    
    
    private void Authorization() {
        CardLayout cl = (CardLayout) jPanel1.getParent().getLayout();
        cl.show(jPanel1.getParent(), "card2");
        jPanel73.setVisible(false);
        jLabel72.setVisible(false);
        jLabel73.setVisible(false);
        jLabel70.setVisible(false);
        jPasswordField2.setVisible(false);
        jLabel71.setVisible(false);
        jPasswordField3.setVisible(false);
        if (!db.setConnectionToServer()) {
            JOptionPane.showMessageDialog(null, "Нет связи с сервером");
        } else {
            jLabel72.setVisible(false);
            jLabel73.setVisible(false);
            jLabel70.setVisible(false);
            jPasswordField2.setVisible(false);
            jLabel71.setVisible(false);
            jPasswordField3.setVisible(false);
            jPanel73.setVisible(true);
        }
    }

   public void Start() throws Exception {
       
       orderComplectationDialog = new OrderComplectationDialog(MainForm.this,db);
       clientChooseDialog = new ClientChooseDialog(MainForm.this,db);
       revenueExpensesDialog = new RevenueExpensesDialog(MainForm.this, db);
       choosePackingWorkersDialog = new ChoosePackingWorkersDialog(MainForm.this, db);
       deliveryDialog = new DeliveryDialog(MainForm.this,db);
       chooseWorkersForPackingDialog = new ChooseWorkersForPackingDialog(MainForm.this,db);
       productDeliveryDialog = new ProductDeliveryDialog(MainForm.this,db);
       expensesDialog = new ExpensesDialog(MainForm.this,db);
       labelPrintDialog = new LabelPrintDialog(MainForm.this,db);
       chooseConsolidatedOrdersDialog = new ChooseConsolidatedOrdersDialog(MainForm.this,db);
       expeditorsDialog = new ExpeditorsDialog(MainForm.this,db);
       incomeCallDialog = new IncomeCallDialog(MainForm.this,db);
       
       db.DoSQL("ROLLBACK");
       
       Constants.setColumnNames(new String[]{"ID","STICK_COST","COST_BOX_FOR_1_GIFT","CURRENT_NUMBER","FTPaddress","FTPpass"});
       GetConstants();
       
       FTP.setParameters(Constants.getString("FTPaddress"),Constants.getString("FTPpass"));
       if (!FTP.setConnectionToFTP()) {
            JOptionPane.showMessageDialog(null, "Невозможно соединиться с FTP-сервером");
       }
       
       try {
            File file = new File("c:\\temp\\temp.tmp");
            file.getParentFile().mkdirs();
            file.delete();
            file.createNewFile();
       } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Нет доступа к c:\\temp\\ , работа с файлами будет невозможна!"); 
            System.out.println(ex.getMessage() + " 431");
       }
       
       jDateChooser7.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    GetDelivery();
                    MakeTableOfDelivery();
                }
            }
       });

       jDateChooser8.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    GetFinances();
                    MakeTableOfFinances();
                }
            }
       });
       
       jDateChooser9.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    GetFinances();
                    MakeTableOfFinances();
                }
            }
       });       

       jDateChooser10.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    GetExpenses();
                    MakeTableOfExpenses();
                }
            }
       });        

       jDateChooser11.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("date".equals(e.getPropertyName())) {
                    GetExpenses();
                    MakeTableOfExpenses();
                }
            }
       });        
       
       userLevel = CurrentUser.getInt("LEVEL");
       
       switch (userLevel) {
           case DIRECTOR: //директор
               jLabel43.setText("Директор");
               CanSeeCandies = true;
               CanEditCandies = true;
               CanDeleteCandies = true;
               CanSeePackings = true;
               CanEditPackings = true;
               CanDeletePackings = true;
               CanSeeGifts = true;
               CanEditGifts = true;
               CanDeleteGifts = true;
               CanSeeClients = true;
               CanEditClients = true;
               CanDeleteClients = true;
               CanSeeStorage = true;
               CanEditStorage = true;
               CanSeeUsers = true;
               CanEditUsers = true;
               CanDeleteUsers = true;
               CanSeeConst = true;
               CanEditConst = true;
               CanSeeOrders = true;
               CanMakeOrders = true;
               CanEditOrders = true;
               CanPayOrders = true;
               CanPackOrders = true;
               CanDeleteOrders = true;
               CanSeeDelivery = true;
               CanSeeFinances = true;
               CanEditExpeditors = true;
               CanSeeOrdersCount = true;
               break;
           case MANAGER: //менеджер по продажам
               jLabel43.setText("Менеджер по продажам");
               CanSeeCandies = true;
               CanEditCandies = true;
               CanDeleteCandies = true;
               CanSeePackings = true;
               CanEditPackings = true;
               CanDeletePackings = true;
               CanSeeGifts = true;
               CanEditGifts = true;
               CanDeleteGifts = true;
               CanSeeClients = true;
               CanEditClients = true;
               CanDeleteClients = false;
               CanSeeStorage = true;
               CanEditStorage = false;
               CanSeeUsers = false;
               CanEditUsers = false;
               CanDeleteUsers = false;
               CanSeeConst = false;
               CanEditConst = false;
               CanSeeOrders = true;
               CanMakeOrders = true;
               CanEditOrders = true;
               CanPayOrders = false;
               CanPackOrders = false;
               CanDeleteOrders = false;    
               CanSeeDelivery = true;
               CanSeeFinances = false;
               CanEditExpeditors = true;
               break;
           case STORAGE_HEAD: //начальник склада
               jLabel43.setText("Начальник склада");
               CanSeeCandies = true;
               CanEditCandies = true;
               CanDeleteCandies = false;
               CanSeePackings = true;
               CanEditPackings = true;
               CanDeletePackings = false;
               CanSeeGifts = true;
               CanEditGifts = false;
               CanDeleteGifts = false;
               CanSeeClients = false;
               CanEditClients = false;
               CanDeleteClients = false;
               CanSeeStorage = true;
               CanEditStorage = true;
               CanSeeUsers = true;
               CanEditUsers = true;
               CanDeleteUsers = true;
               CanSeeConst = false;
               CanEditConst = false;
               CanSeeOrders = true;
               CanMakeOrders = false;
               CanEditOrders = false;
               CanPayOrders = false;
               CanPackOrders = true;
               CanDeleteOrders = false;    
               CanSeeDelivery = true;
               CanSeeFinances = false;
               CanEditExpeditors = true;
               break;
           case LOGIST: //логист
               jLabel43.setText("Логист");
               CanSeeCandies = false;
               CanEditCandies = false;
               CanDeleteCandies = false;
               CanSeePackings = false;
               CanEditPackings = false;
               CanDeletePackings = false;
               CanSeeGifts = false;
               CanEditGifts = false;
               CanDeleteGifts = false;
               CanSeeClients = false;
               CanEditClients = false;
               CanDeleteClients = false;
               CanSeeStorage = false;
               CanEditStorage = false;
               CanSeeUsers = false;
               CanEditUsers = false;
               CanDeleteUsers = false;
               CanSeeConst = false;
               CanEditConst = false;
               CanSeeOrders = false;
               CanMakeOrders = false;
               CanEditOrders = false;
               CanPayOrders = false;
               CanPackOrders = false;
               CanDeleteOrders = false;    
               CanSeeDelivery = true;
               CanSeeFinances = false;
               CanEditExpeditors = false;
               break;
           case STORAGE_WORKER: //работник склада
               jLabel43.setText("Работник склада");
               CanSeeCandies = false;
               CanEditCandies = false;
               CanDeleteCandies = false;
               CanSeePackings = false;
               CanEditPackings = false;
               CanDeletePackings = false;
               CanSeeGifts = false;
               CanEditGifts = false;
               CanDeleteGifts = false;
               CanSeeClients = false;
               CanEditClients = false;
               CanDeleteClients = false;
               CanSeeStorage = false;
               CanEditStorage = false;
               CanSeeUsers = false;
               CanEditUsers = false;
               CanDeleteUsers = false;
               CanSeeConst = false;
               CanEditConst = false;
               CanSeeOrders = false;
               CanMakeOrders = false;
               CanEditOrders = false;
               CanPayOrders = false;
               CanPackOrders = false;
               CanDeleteOrders = false;    
               CanSeeDelivery = false;
               CanSeeFinances = false;
               CanEditExpeditors = false;
               break;               
       }

       if (!CanSeeConst) {
           jTabbedPane1.remove(jPanel45);
       }
       if (!CanSeeUsers) {
           jTabbedPane1.remove(jPanel5);
       }
       if (!CanSeeDelivery) {
           jTabbedPane1.remove(jPanel19);
       }
       if (!CanSeeStorage) {
           jTabbedPane1.remove(jPanel8);
       }
       if (!CanSeeOrders) {
           jTabbedPane1.remove(jPanel9);
       }
       if (!CanSeeClients) {
           jTabbedPane1.remove(jPanel7);
       }
       if (!CanSeeGifts) {
           jTabbedPane1.remove(jPanel37);
       }
       if (!CanSeePackings) {
           jTabbedPane1.remove(jPanel41);
       }
       if (!CanSeeCandies) {
           try {
               jTabbedPane1.remove(jPanel4);
           } catch (Exception ex) {}
       }
       if (!CanSeeFinances) {
           try {
               jTabbedPane1.remove(jPanel174);
           } catch (Exception ex) {}
       }
       
       jButton30.setVisible(CanEditCandies);
       jButton31.setVisible(CanEditCandies);
       jButton34.setVisible(CanEditPackings);
       jButton38.setVisible(CanEditGifts);
       jButton63.setVisible(CanEditGifts);
       jButton36.setVisible(CanEditClients);
       jPanel192.setVisible(CanEditStorage);
       jPanel193.setVisible(CanEditStorage);
       jButton42.setVisible(CanEditUsers);
       jPanel101.setVisible(CanEditConst);
       jButton22.setVisible(CanMakeOrders);
       jButton23.setVisible(CanDeleteOrders);
       jLabel144.setVisible(false);
       jLabel145.setVisible(false);
       jLabel146.setVisible(false);
       jTextField57.setVisible(false);
       jTextField58.setVisible(false);
       
       jLabel4.setText((String) CurrentUser.get("NAME"));
 
       
       changeView_Table_Tree_CandiesForPackage();

       Candies.setColumnNames(new String[]{"ID", "CANDY_NAME", "ID_FACTORY", "BOX_WEIGHT", "AMOUNT_IN_BOX", "COST_KG", "FACTORY_NAME","CANDY_COST","STORAGE","RESERVED","COMM","LAST_CHANGE_COST","ISUSED","DISCOUNT"});
       Users.setColumnNames(new String[]{"ID", "NAME", "LOGIN", "PASS", "LEVEL", "CAN_ENTER","FINANCE_PASS","PHONE","COMMENT","ID_POSITION","ID_USER_BOSS"});
       Clients.setColumnNames(new String[]{"ID", "OFFICIAL_NAME","ISUSED","STATE"});
       ClientsToCheck.setColumnNames(new String[]{"PHONE1","PHONE2","PHONE3","EMAIL1","EMAIL2","EMAIL3","EDRPOU"});
       Folders.setColumnNames(new String[]{"ID","NAME"});
       Clients_Folders.setColumnNames(new String[]{"ID_CLIENT","ID_FOLDER"});
       ExtendedClient.setColumnNames(new String[]{"NAME","DATE_TIME","CONTACT1", "CONTACT2","CONTACT3","PHONE1","ADDITIONAL_PHONE1","FAX","PHONE2","ADDITIONAL_PHONE2","PHONE3","ADDITIONAL_PHONE3","EMAIL1","EMAIL2","EMAIL3", "SITE", "ADDRESS", "COMM","USER_CREATOR_NAME","EDRPOU"});
       Gifts.setColumnNames(new String[]{"ID", "NAME","COST_PACKING"});
//       GiftFolders.setColumnNames(new String[]{"FOLDER_NAME"});
       Gift_Candy.setColumnNames(new String[]{"ID","ID_CANDY","AMOUNT","CANDY_NAME","BOX_WEIGHT","AMOUNT_IN_BOX","COST_KG","FACTORY_NAME"});
       Packings.setColumnNames(new String[]{"ID","NAME","TYPE","NUMBER","CAPACITY","STORAGE","RESERVED","FILENAME","COST","MARKED"});
       FilteredPackings.setColumnNames(new String[]{"ID","NAME","TYPE","NUMBER","CAPACITY","STORAGE","RESERVED","FILENAME","COST","MARKED"});
       Orders.setColumnNames(new String[]{"ID","DATE_TIME","ID_CLIENT","CLIENT_NAME","STATE","MIN_DATE_DELIVERY","NUMBER"});
       ExtendedOrder.setColumnNames(new String[]{"TYPE_PAY","DATE_PAY","DATE_PACK","PREPAY","DISCOUNT","COMM_PACKING","COMM","DELIVERY_COST","USER_CREATOR_NAME"});    
       SubOrders.setColumnNames(new String[]{"ID","ID_GIFT","GIFT_NAME","GIFT_COST_PACKING","ID_PACKING","PACKING_NAME","PACKING_NUMBER","AMOUNT","COST","SELF_COST","PACKED","PACKING_WEIGHT"});
       OrdersCount.setColumnNames(new String[]{"AMOUNT","COST"});
       Storage.setColumnNames(new String[]{"DATE_TIME","AMOUNT","USER","CLIENT","FACTURA"});
       Delivery.setColumnNames(new String[]{"ID","DATE_TIME","DATE_SEND","DATE_CLOSE","TTN","CONTACT","WHO_PAYS","DELIVERY_TYPE","ADDRESS","CONTENT","PRESENT","COMM","STATE","CLIENT","ID_ORDERS","DEBT","USER_CREATOR_NAME","TYPE_PAY_ORDER","EXPEDITORS_NAME"});
       DeliveryOrder.setColumnNames(new String[]{"ID","DATE_TIME","DATE_DELIVERY_COUNTRY","DATE_SEND","DATE_CLOSE","CONTACT","WHO_PAYS","DELIVERY_TYPE","ADDRESS","CONTENT","PRESENT","COMM","DEBT","STATE","TYPE_PAY_ORDER"});
       Finances.setColumnNames(new String[]{"DATE_TIME","DELIVERY_COST","CLIENT_NAME","SELF_COST","COST","DELIVERY_TRANSPORT_COST","DELIVERY_PAYMENT","DELIVERY_COURIER_COST"});
       Expenses.setColumnNames(new String[]{"ID","DATE_TIME","NAME","COST"});
       Expeditors.setColumnNames(new String[]{"ID", "NAME","DRIVER"});
       //////////////////////////
       Timer t = new Timer();
       TimerTask tt = new TimerTask() {

           private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
           int Counter = 600;
           Long value = 0L;

           @Override
           public void run() {
               if (Counter >= 600) {
                   value = Calendar.getInstance().getTimeInMillis();
                   Counter = 0;
               }
               jLabel3.setText(sdf.format(new Date(value)));
               value += 1000;
               Counter++;
           }
       };
       t.schedule(tt, 1000, 1000);

       /////////////////////////
       Timer waitingForIncomingCall = new Timer();
       TimerTask waitingForIncomingCallTask = new TimerTask() {

           private boolean gotCall = false;
           
           @Override
           public void run() {
               try {
                   Object[][] obj = db.SelectSQL("SELECT generalCallID,income_call_number FROM user WHERE id=?", new Object[]{CurrentUser.get("ID")});
                   int generalCallID = (Integer)obj[0][0];
                   //System.out.println(generalCallID);
                   if (generalCallID==-1 && gotCall) { //значит звонок был и уже окончен
                       gotCall = false;
                       incomeCallDialog.setVisible(false);
                   } else if (generalCallID>-1 && !gotCall) { //значит звонка не было и он только появился
                       gotCall = true;
                       incomeCallDialog.initDialog((String)obj[0][1]);
                   }
               } catch (Exception ex) {
                   System.out.println(ex.getMessage() + " 763");
               }
           }
       };
       waitingForIncomingCall.schedule(waitingForIncomingCallTask, 5000, 2000);
       //////////////////////////
       jComboBox1.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/empty.png")));
       jComboBox1.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/exclamation-red-icon.png")));
       jComboBox1.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/coins-icon.png")));
       jComboBox1.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/upakovka_146_2.png")));
       jComboBox1.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/lock-icon2.png")));
       jComboBox1.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/Lorry-icon.png")));
//       jComboBox1.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/gift-icon2.png")));
       jComboBox1.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png")));
       jComboBox1.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png")));
       
       jComboBox3.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/empty.png")));
       jComboBox3.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/exclamation-red-icon.png")));
       jComboBox3.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/coins-icon.png")));
       jComboBox3.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/upakovka_146_2.png")));
       jComboBox3.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/lock-icon2.png")));
       jComboBox3.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/Lorry-icon.png")));
//       jComboBox3.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/gift-icon2.png")));
       jComboBox3.addItem(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png")));
       
       switch (userLevel) {
           case DIRECTOR:
           case MANAGER:
               jComboBox1.setSelectedIndex(0);
               break;
           case STORAGE_HEAD:
               //jLabel93.setVisible(false);
               //jPanel107.setVisible(false);
               jComboBox1.setSelectedIndex(0);
               break;
           case LOGIST:
           case STORAGE_WORKER:
               jLabel93.setVisible(false);
               jPanel107.setVisible(false);
               break;
       }
       
       MakePanelOfConstants();
       
       jTabbedPane1StateChanged(null);
       
       GetUsers();
       jComboBox6.removeAllItems();
       jComboBox7.removeAllItems();
       jComboBox10.removeAllItems(); 
       jComboBox11.removeAllItems(); 
       jComboBox6.addItem(" ");
       jComboBox7.addItem(" ");
       jComboBox10.addItem(" ");
       jComboBox11.addItem(" ");
       for (int i = 0; i < Users.getLength(); i++) {
           if (Users.getInt(i, "LEVEL") == MANAGER || Users.getInt(i, "LEVEL") == DIRECTOR) {
               jComboBox6.addItem(new Object[]{Users.getInt(i, "ID"), Users.getString(i, "NAME")});
               jComboBox7.addItem(new Object[]{Users.getInt(i, "ID"), Users.getString(i, "NAME")});
               jComboBox10.addItem(new Object[]{Users.getInt(i, "ID"), Users.getString(i, "NAME")});
               jComboBox11.addItem(new Object[]{Users.getInt(i, "ID"), Users.getString(i, "NAME")});
           }
       }
       getExpeditors();
       fillComboboxExpeditorsInDeliveryFilter();
       fillComboboxDeliveryTypesInDeliveryFilter();
 
       CardLayout cl = (CardLayout) jPanel1.getParent().getLayout();
       cl.show(jPanel1.getParent(), "card3");
        
       
    }

    private boolean GetCandies(CandiesOrder order) {
        Object[][] obj = null;
        
        if (order==CandiesOrder.ALPHABET) {
            obj = db.SelectSQL("SELECT candy.id as candy_id, candy.name as candy_name, "
                    + "candy.id_factory AS id_factory, candy.box_weight, candy.amount_in_box, "
                    + "candy.cost_kg, factory.name as factory_name,candy.cost_kg*candy.box_weight/candy.amount_in_box "
                    + "as candy_cost,candy.storage,candy.reserved,candy.comm,candy.last_change_cost,"
                    + "(SELECT id_candy FROM gift_candy WHERE id_candy=candy_id LIMIT 1),factory.discount FROM candy, "
                    + "factory WHERE candy.id_factory = factory.id" + filterName + " GROUP BY id_factory, candy_id "+
                             "UNION SELECT null,null,factory.id as id_factory,null,null,null,factory.name as "
                    + "factory_name,null,null,null,null,null,null,factory.discount FROM factory WHERE factory.id NOT IN "
                    + "(SELECT candy.id_factory FROM candy) "+
                             "ORDER BY factory_name, candy_name",null);
        } else if (order==CandiesOrder.COST) {
            obj = db.SelectSQL("SELECT candy.id as candy_id, candy.name as candy_name, candy.id_factory AS id_factory, candy.box_weight,candy.amount_in_box, candy.cost_kg, factory.name as factory_name,candy.cost_kg*candy.box_weight/candy.amount_in_box as candy_cost,candy.storage,candy.reserved,candy.comm,candy.last_change_cost,(SELECT id_candy FROM gift_candy WHERE id_candy=candy_id" + filterName + " LIMIT 1),factory.discount FROM candy, factory WHERE candy.id_factory = factory.id GROUP BY id_factory, candy_id "+
                             "UNION SELECT null,null,factory.id as id_factory,null,null,null,factory.name as factory_name,null,null,null,null,null,null,factory.discount FROM factory WHERE factory.id NOT IN (SELECT candy.id_factory FROM candy) "+
                             "ORDER BY factory_name, candy_cost",null);
        } else if (order==CandiesOrder.RELATIVE_COST) {
            obj = db.SelectSQL("SELECT candy.id as candy_id, candy.name as candy_name, candy.id_factory AS id_factory, candy.box_weight as box_weight,candy.amount_in_box as amount_in_box, candy.cost_kg as cost_kg, factory.name as factory_name,candy.cost_kg*candy.box_weight/candy.amount_in_box as candy_cost,candy.storage,candy.reserved,candy.comm,candy.last_change_cost,(SELECT id_candy FROM gift_candy WHERE id_candy=candy_id LIMIT 1),factory.discount FROM candy, factory WHERE candy.id_factory = factory.id" + filterName + " GROUP BY id_factory, candy_id "+
                             "UNION SELECT null,null,factory.id as id_factory,null,null,null,factory.name as factory_name,null,null,null,null,null,null,factory.discount FROM factory WHERE factory.id NOT IN (SELECT candy.id_factory FROM candy) "+
                             "ORDER BY factory_name, cost_kg",null);
        }
        if (obj == null) {
            JOptionPane.showMessageDialog(null, "Нет совпадений названия");
            jTextField53.setText("");
        } else {
            Candies.set(obj);
        }
        return obj!=null;
    }

    private void MakeTreeOfCandiesForGift() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new Object[]{"",0});
        DefaultMutableTreeNode child1 = null;
        DefaultMutableTreeNode child2;
        int last_factory = 0;
        int id_factory;

        for (int i = 0; i < Candies.getLength(); i++) {
            id_factory = Candies.getInt(i, "ID_FACTORY");
            if (id_factory != last_factory) {
                Object obj[] = new Object[]{Candies.getString(i, "FACTORY_NAME"), i};
                child1 = new DefaultMutableTreeNode(obj);
                top.add(child1);
                last_factory = id_factory;
            }
            if (Candies.getInt(i, "ID") != 0) {
                double weight_one = Candies.getDouble(i, "BOX_WEIGHT")/Candies.getInt(i, "AMOUNT_IN_BOX");
                String name = Candies.getString(i, "CANDY_NAME");
                String cost = FormatUAH.format(Candies.getDouble(i, "CANDY_COST"));
                String weight = FormatKG.format(weight_one);
                boolean IsUsed = Candies.getInt(i,"ISUSED")!=0;
                Object obj[] = new Object[]{name, i, cost, weight, IsUsed};
                child2 = new DefaultMutableTreeNode(obj);
                child1.add(child2);
            }
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree5.setModel(treeModel);
        for (int i = 0; i <= jTree5.getRowCount() - 1; i++) {
            jTree5.expandRow(i);
        }
    }
    
    private void MakeTableOfCandiesForGift() {
        Object[][] candies = new Object[Candies.getLength()][6];
        
        for (int i=0;i<Candies.getLength();i++) {
                candies[i][0] = Candies.getString(i, "CANDY_NAME");
                candies[i][1] = Candies.getString(i, "FACTORY_NAME");
                candies[i][2] = Candies.getDouble(i, "BOX_WEIGHT")/Candies.getInt(i, "AMOUNT_IN_BOX");
                candies[i][3] = Candies.getDouble(i, "CANDY_COST");
                candies[i][4] = Candies.getInt(i,"ISUSED")!=0;
                candies[i][5] = Candies.getInt(i, "ID");            
        }
        
        if (jToggleButton3.isSelected()) { //sort by alphabet
            Arrays.sort(candies, new Comparator() {

                private String s1;
                private String s2;
                
                @Override
                public int compare(Object o1, Object o2) {
                    s1 = (String)((Object[])o1)[0];
                    s2 = (String)((Object[])o2)[0];
                    if (s1==null) return 1;
                    if (s2==null) return -1;
                    return s1.compareTo(s2);
                }
            });
        } else if (jToggleButton4.isSelected()) { //sort by cost
            Arrays.sort(candies, new Comparator() {

                private Double d1;
                private Double d2;
                
                @Override
                public int compare(Object o1, Object o2) {
                    d1 = (Double)((Object[])o1)[3];
                    d2 = (Double)((Object[])o2)[3];
                    return d1.compareTo(d2);
                }
            });
        } else { // sort by cost/weight
            Arrays.sort(candies, new Comparator() {
                
                private Double cost1;
                private Double cost2;
                private Double weight1;
                private Double weight2;
                
                @Override
                public int compare(Object o1, Object o2) {
                    cost1 = (Double)((Object[])o1)[3];
                    weight1 = (Double)((Object[])o1)[2];
                    cost2 = (Double)((Object[])o2)[3];
                    weight2 = (Double)((Object[])o2)[2];
                    return ((Double)(cost1/weight1)).compareTo((Double)(cost2/weight2));
                }
            });
        }
        
        //cast Double cost to String cost and Double weight to String weight
        for (int i=0;i<candies.length;i++) {
            candies[i][2] = FormatKG.format(candies[i][3]);            
            candies[i][3] = FormatUAH.format(candies[i][3]);
        }
        
        jTable18.makeTable(candies);
    }    

    private void MakeTreeOfCandies() {
        int[] SaveCurrentRow = jTree1.getSelectionRows();

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new Object[]{"ВСЕ ФАБРИКИ",0});
        DefaultMutableTreeNode child1 = null;
        DefaultMutableTreeNode child2;
        int last_factory = 0;
        int id_factory;

        for (int i = 0; i < Candies.getLength(); i++) {
            id_factory = Candies.getInt(i, "ID_FACTORY");
            if (id_factory != last_factory) {
                Object obj[] = new Object[]{Candies.getString(i, "FACTORY_NAME"), i};
                child1 = new DefaultMutableTreeNode(obj);
                top.add(child1);
                last_factory = id_factory;
            }
            if (Candies.getInt(i, "ID") != 0) {
                String name = Candies.getString(i, "CANDY_NAME");
                boolean IsUsed = Candies.getInt(i, "ISUSED")!=0;
                Object obj[] = new Object[]{name, i, IsUsed};
                child2 = new DefaultMutableTreeNode(obj);
                child1.add(child2);
            }
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree1.setModel(treeModel);
        for (int i = 0; i <= jTree1.getRowCount() - 1; i++) {
            jTree1.expandRow(i);
        }

        jTree1.setSelectionRows(SaveCurrentRow);
        SelectNodeOfTreeCandies();

        MakeTreeOfCandiesForGift();
        MakeTableOfCandiesForGift();
    }

    private void MakeTableOfCandiesOfFactory(DefaultMutableTreeNode SelectedNode) {
        if (SelectedNode.getLevel()==0) {
            Object data[][] = new Object[jTree1.getRowCount()-1][8];
            DefaultMutableTreeNode child1;
            DefaultMutableTreeNode child2;
            int count = 0;
            for (int j=0;j<SelectedNode.getChildCount();j++,count++) {
                child1 = (DefaultMutableTreeNode)SelectedNode.getChildAt(j);
                for (int i=0;i<child1.getChildCount();i++,count++) {
                    child2 = (DefaultMutableTreeNode)child1.getChildAt(i);
                    Object[] obj = (Object[])child2.getUserObject();
                    int id = (Integer)obj[1];
                    data[count][0] = Candies.getString(id, "FACTORY_NAME");
                    String color= Candies.getInt(id, "ISUSED")==0 ? "gray" : "blue";
                    data[count][1] = "<html><font color='"+color+"'>"+Candies.getString(id, "CANDY_NAME")+"</font></html>";
                    data[count][2] = FormatKG.format(Candies.getDouble(id, "BOX_WEIGHT"));
                    data[count][3] = Candies.getInt(id, "AMOUNT_IN_BOX");
                    data[count][4] = FormatKG.format(Candies.getDouble(id, "BOX_WEIGHT")/Candies.getInt(id, "AMOUNT_IN_BOX"));
                    data[count][5] = FormatUAH.format(Candies.getDouble(id, "COST_KG"));
                    data[count][6] = FormatUAH.format(Candies.get(id,"CANDY_COST"));
                    data[count][7] = FormatUAH.format(Candies.getDouble(id, "BOX_WEIGHT")*Candies.getDouble(id, "COST_KG"));
                }
            }
            jTable2.makeTableAllFactories(data);
        } else {
            Object data[][] = new Object[SelectedNode.getChildCount()][7];
            DefaultMutableTreeNode child;
            for (int i=0;i<SelectedNode.getChildCount();i++) {
                child = (DefaultMutableTreeNode)SelectedNode.getChildAt(i);
                Object[] obj = (Object[])child.getUserObject();
                int id = (Integer)obj[1];
                String color= Candies.getInt(id, "ISUSED")==0 ? "gray" : "blue";
                data[i][0] = "<html><font color='"+color+"'>"+Candies.getString(id, "CANDY_NAME")+"</font></html>";
                data[i][1] = FormatKG.format(Candies.getDouble(id, "BOX_WEIGHT"));
                data[i][2] = Candies.getInt(id, "AMOUNT_IN_BOX");
                data[i][3] = FormatKG.format(Candies.getDouble(id, "BOX_WEIGHT")/Candies.getInt(id, "AMOUNT_IN_BOX"));
                data[i][4] = FormatUAH.format(Candies.getDouble(id, "COST_KG"));
                data[i][5] = FormatUAH.format(Candies.get(id,"CANDY_COST"));
                data[i][6] = FormatUAH.format(Candies.getDouble(id, "BOX_WEIGHT")*Candies.getDouble(id, "COST_KG"));
            }
            jTable2.makeTable( data);
        }
    }

    private void SelectNodeOfTreeCandies() {
        Object[] obj;
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        if (SelectedNode == null) {
            Candies.setPosition(-1);
            jPanel12.setVisible(false);
            jPanel14.setVisible(false);
            jPanel15.setVisible(false);
            jPanel16.setVisible(false);
            jPanel17.setVisible(false);
            jPanel18.setVisible(false);
            jPanel20.setVisible(false);
            jPanel21.setVisible(false);
            jPanel103.setVisible(false);
            jPanel104.setVisible(false);
            jScrollPane7.setVisible(false);
            jPanel123.setVisible(false);
            jPanel59.setVisible(true);
            jButton31.setVisible(false);
            jButton32.setVisible(false);
        } else {
            switch (SelectedNode.getLevel()) {
                case 0:
                    jButton31.setVisible(true);
                    jButton32.setVisible(false);
                    
                    jPanel12.setVisible(false);
                    
                    jPanel17.setVisible(true);
                    jButton1.setVisible(false);
                    jButton2.setVisible(false);
                    jButton3.setVisible(false);
                    jButton59.setVisible(true);
                    
                    jPanel14.setVisible(false);
                    jPanel15.setVisible(false);
                    jPanel16.setVisible(false);
                    jPanel18.setVisible(false);
                    jPanel20.setVisible(false);
                    jPanel21.setVisible(false);
                    jPanel103.setVisible(false);
                    jPanel104.setVisible(false);
                    jScrollPane7.setVisible(true);
                    jPanel123.setVisible(false);
                    jButton30.setVisible(CanEditCandies);
                    MakeTableOfCandiesOfFactory(SelectedNode);
                    break;
                case 1:
                    obj = (Object[]) SelectedNode.getUserObject();
                    Candies.setPosition((Integer) obj[1]);
                    
                    jButton30.setVisible(CanEditCandies);
                    jButton31.setVisible(CanEditCandies);
                    jButton32.setVisible(CanDeleteCandies);

                    jPanel12.setVisible(true);
                    jTextField1.setEditable(false);
                    jTextField1.setBorder(null);
                    jTextField1.setBackground(jTextField1.getParent().getBackground());
                    jTextField1.setForeground(new Color(200,0,0));
                    jTextField1.setText(Candies.getString("FACTORY_NAME"));
                    jTextField31.setEditable(false);
                    jTextField31.setBorder(null);
                    jTextField31.setBackground(jTextField31.getParent().getBackground());
                    jTextField31.setForeground(new Color(200,0,0));
                    jTextField31.setText(FormatKG.format(Candies.getDouble("DISCOUNT")));

                    jPanel17.setVisible(true);
                    
                    jButton1.setVisible(CanEditCandies);
                    
                    jButton2.setVisible(false);
                    jButton3.setVisible(false);
                    jButton59.setVisible(true);
                    
                    jPanel14.setVisible(false);
                    jPanel15.setVisible(false);
                    jPanel16.setVisible(false);
                    jPanel18.setVisible(false);
                    jPanel20.setVisible(false);
                    jPanel21.setVisible(false);
                    jPanel103.setVisible(false);
                    jPanel104.setVisible(false);
                    jPanel123.setVisible(true);

                    jScrollPane7.setVisible(true);
                    MakeTableOfCandiesOfFactory(SelectedNode);
                    break;
                case 2:
                    obj = (Object[]) SelectedNode.getUserObject();
                    Candies.setPosition((Integer) obj[1]);
                    
                    jButton30.setVisible(CanEditCandies);
                    jButton31.setVisible(CanEditCandies);
                    jButton32.setVisible(CanDeleteCandies);

                    jScrollPane7.setVisible(false);

                    jPanel12.setVisible(true);
                    JTextField[] mas = new JTextField[]{jTextField1,jTextField2,jTextField3,jTextField4};
                    for (JTextField ma : mas) {
                        ma.setEditable(false);
                        ma.setBorder(null);
                        ma.setBackground(ma.getParent().getBackground());
                        ma.setForeground(new Color(200, 0, 0));
                    }
                    jTextField1.setText(Candies.getString("CANDY_NAME"));
                    double box_weight = Candies.getDouble("BOX_WEIGHT");
                    jTextField2.setText(FormatKG.format(box_weight));
                    int amount_in_box = Candies.getInt("AMOUNT_IN_BOX");
                    jTextField3.setText(Integer.toString(amount_in_box));
                    jPanel16.setVisible(true);
                    double cost_kg = Candies.getDouble("COST_KG");
                    jTextField4.setText(FormatUAH.format(cost_kg));
                    jLabel14.setText(FormatKG.format(box_weight / amount_in_box));
                    double cost_box = cost_kg * box_weight;
                    jLabel18.setText(FormatUAH.format(cost_box));
                    jLabel16.setText(FormatUAH.format(cost_box / amount_in_box));
                    
                    jPanel17.setVisible(true);
                    jPanel14.setVisible(true);
                    jPanel15.setVisible(true);
                    jPanel18.setVisible(true);
                    jPanel21.setVisible(true);
                    jPanel20.setVisible(true);
                    jPanel103.setVisible(true);
                    jPanel123.setVisible(false);

                    jTextArea5.setBorder(BorderFactory.createEmptyBorder());
                    jTextArea5.setBackground(jTextArea5.getParent().getParent().getParent().getBackground());
                    jTextArea5.setForeground(new Color(200, 0, 0));
                    jTextArea5.setEditable(false);
                    jTextArea5.setText(Candies.getString("COMM"));

                    jPanel104.setVisible(true);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                    int date = Candies.getInt("LAST_CHANGE_COST");
                    if (date == 0) {
                        jLabel99.setText("");
                    } else {
                        jLabel99.setText(sdf.format(new Date(date * 1000L)));
                    }

                    jButton1.setVisible(CanEditCandies);
                    jButton2.setVisible(false);
                    jButton3.setVisible(false); 
                    jButton59.setVisible(false);
                    break;
            }
        }

    }

    private boolean GetUsers() {
        Object[][] obj;
        if (CurrentUser.getInt("LEVEL")==STORAGE_HEAD) {
            obj = db.SelectSQL("SELECT * FROM user WHERE level="+STORAGE_WORKER+" ORDER BY name",null);
        } else {
            obj = db.SelectSQL("SELECT * FROM user GROUP BY level,name,id",null);            
        }
        Users.set(obj);
        return obj!=null;
    }

    private void MakeTreeOfUsers() {
        int[] SaveCurrentRow = jTree2.getSelectionRows();

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new Object[]{"",0});
        DefaultMutableTreeNode child_0 = new DefaultMutableTreeNode(new Object[]{"Директор", 0});
        DefaultMutableTreeNode child_1 = new DefaultMutableTreeNode(new Object[]{"Менеджер по продажам", 1});
        DefaultMutableTreeNode child_2 = new DefaultMutableTreeNode(new Object[]{"Начальник склада", 2});
        DefaultMutableTreeNode child_3 = new DefaultMutableTreeNode(new Object[]{"Логист", 3});
        DefaultMutableTreeNode child_4 = new DefaultMutableTreeNode(new Object[]{"Работник склада", 4});
        
        if (CurrentUser.getInt("level")!=STORAGE_HEAD) { //для начальника склада будет виден лишь раздел "работники склада"
            top.add(child_0);
            top.add(child_1);
            top.add(child_2);
            top.add(child_3);
        }
        top.add(child_4);

        List<Object[]> listSuborderedWorkers = new LinkedList<>();//[] = id of boss, node        
        if (jCheckBox2.isSelected()) { //группировать по подчинению
            //вначале вносим в хеш-таблицу всех подчиненных
            for (int i = 0; i < Users.getLength(); i++) {
                if (Users.getInt(i,"LEVEL")==STORAGE_WORKER && Users.getInt(i,"ID_USER_BOSS")!=-1) { //работник склада и имеет босса
                    int idBoss = Users.getInt(i,"ID_USER_BOSS");
                    DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(new Object[]{Users.getString(i, "NAME"), i, true});
                    listSuborderedWorkers.add(new Object[]{idBoss, userNode});
                }
            }
            //
            //далее заполняем дерево, и для каждого сотрудника добавляем его подчиненных из хеш-таблицы
        }
        
        for (int i = 0; i < Users.getLength(); i++) {
            switch (Users.getInt(i, "LEVEL")) {
                case DIRECTOR:
                    if (userLevel!=STORAGE_HEAD) {
                        child_0.add(new DefaultMutableTreeNode(new Object[]{Users.getString(i, "NAME"), i, false})); //имя, индекс, true если subordered - имеет босса
                    }
                    break;
                case MANAGER:
                    if (userLevel!=STORAGE_HEAD) {                    
                        child_1.add(new DefaultMutableTreeNode(new Object[]{Users.getString(i, "NAME"), i, false}));
                    }
                    break;
                case STORAGE_HEAD:
                    if (userLevel!=STORAGE_HEAD) {
                        child_2.add(new DefaultMutableTreeNode(new Object[]{Users.getString(i, "NAME"), i, false}));
                    }
                    break;
                case LOGIST:
                    if (userLevel!=STORAGE_HEAD) {
                        child_3.add(new DefaultMutableTreeNode(new Object[]{Users.getString(i, "NAME"), i, false}));
                    }
                    break;
                case STORAGE_WORKER:
                    if (jCheckBox2.isSelected()) { //группировать по подчинению
                        if (Users.getInt(i,"ID_POSITION")==STORAGE_WORKER_BRIGADIER) { //работник - бригадир, добавляем в дерево, иначе не добавляем, поскольку подчиненных Фасовщиков добавим для каждого бригадира отдельно
                            child_4.add(new DefaultMutableTreeNode(new Object[]{Users.getString(i, "NAME"), i, false}));                                            
                            int thisWorkerID = Users.getInt(i, "ID");
                            //находим всех сотрудников, принадлежащих этому боссу и когда находим - помечаем на удаление из списка
                            List<Object[]> toRemove = new LinkedList<>();
                            for (Object[] obj : listSuborderedWorkers) {
                                int idBoss = (Integer) obj[0];
                                if (thisWorkerID == idBoss) {
                                    DefaultMutableTreeNode userNode = (DefaultMutableTreeNode) obj[1];
                                    child_4.add(userNode);
                                    toRemove.add(obj);
                                }
                            }
                            listSuborderedWorkers.removeAll(toRemove);
                        } else if (Users.getInt(i,"ID_USER_BOSS")==-1) { //если это не бригадир, но и начальник ему не назначен - добавляем в дерево
                            child_4.add(new DefaultMutableTreeNode(new Object[]{Users.getString(i, "NAME"), i, false}));                                            
                        }
                    } else {
                        child_4.add(new DefaultMutableTreeNode(new Object[]{Users.getString(i, "NAME"), i, false}));                                            
                    }
                    break;
            }
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree2.setModel(treeModel);
        for (int i = 0; i <= jTree2.getRowCount() - 1; i++) {
            jTree2.expandRow(i);
        }
        jTree2.setSelectionRows(SaveCurrentRow);
        SelectNodeOfTreeUsers();
    }

    private void MakeTableOfUsersStorage() {
        Calendar cal = (Calendar)jDateChooser1.getCalendar().clone();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        int Start = ((Long)(cal.getTimeInMillis()/1000)).intValue();
        cal = (Calendar)jDateChooser2.getCalendar().clone();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        int End = ((Long)(cal.getTimeInMillis()/1000)).intValue();
        
        ObjectResultSet UsersStorage = new ObjectResultSet();
        UsersStorage.setColumnNames(new String[]{"DATE_TIME","CLIENT","COST"});
        UsersStorage.set(db.SelectSQL("SELECT user_pack_orders.date_time,client.official_name,user_pack_orders.cost FROM user_pack_orders,client,orders WHERE user_pack_orders.id_orders=orders.id AND orders.id_client=client.id AND user_pack_orders.id_user=? AND user_pack_orders.date_time BETWEEN ? AND ? ORDER BY user_pack_orders.date_time DESC", new Object[]{Users.getInt("ID"),Start,End}));
        Object data[][] = new Object[0][3];
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        if (!UsersStorage.IsNull()) {
            data = new Object[UsersStorage.getLength() + 1][3];
            double Summ_cost = 0;
            for (int i = 0; i < UsersStorage.getLength(); i++) {
                data[i][0] = sdf.format(new Date(UsersStorage.getInt(i, "DATE_TIME")*1000L));
                data[i][1] = UsersStorage.getString(i,"CLIENT");
                double cost = UsersStorage.getDouble(i, "COST");
                data[i][2] = FormatUAH.format(cost);
                Summ_cost+=cost;
            }
            data[UsersStorage.getLength()][1] = "ИТОГО, грн.:";
            data[UsersStorage.getLength()][2] = FormatUAH.format(Summ_cost);
        }
        jTable7.makeTable(data);        
    }
    
    private void MakeTableOfUsersDelivery() {
        Calendar cal = (Calendar)jDateChooser1.getCalendar().clone();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        int Start = ((Long)(cal.getTimeInMillis()/1000)).intValue();
        cal = (Calendar)jDateChooser2.getCalendar().clone();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        int End = ((Long)(cal.getTimeInMillis()/1000)).intValue();
        
        ObjectResultSet UsersDelivery = new ObjectResultSet();
        UsersDelivery.setColumnNames(new String[]{"DATE_TIME","CLIENT","COST"});
        UsersDelivery.set(db.SelectSQL("SELECT user_delivery_orders.date_time,client.official_name,user_delivery_orders.cost FROM user_delivery_orders,orders,client WHERE user_delivery_orders.id_orders=orders.id AND orders.id_client=client.id AND user_delivery_orders.id_user=? AND user_delivery_orders.date_time BETWEEN ? AND ? ORDER BY user_delivery_orders.date_time DESC", new Object[]{Users.getInt("ID"),Start,End}));
        Object data[][] = new Object[0][3];
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        if (!UsersDelivery.IsNull()) {
            data = new Object[UsersDelivery.getLength() + 1][3];
            double Summ_cost = 0;
            for (int i = 0; i < UsersDelivery.getLength(); i++) {
                data[i][0] = sdf.format(new Date(UsersDelivery.getInt(i, "DATE_TIME")*1000L));
                data[i][1] = UsersDelivery.getString(i,"CLIENT");
                double cost = UsersDelivery.getDouble(i, "COST");
                data[i][2] = FormatUAH.format(cost);
                Summ_cost+=cost;
            }
            data[UsersDelivery.getLength()][1] = "ИТОГО, грн.:";
            data[UsersDelivery.getLength()][2] = FormatUAH.format(Summ_cost);
        }
        jTable13.makeTable(data);        
    }    
    
    private void fillListBrigadiers() {
        jComboBox13.removeAllItems();
        jComboBox13.addItem("");
        for(int i=0;i<Users.getLength();i++) {
            if (Users.getInt(i,"ID_POSITION")==STORAGE_WORKER_BRIGADIER) {
                jComboBox13.addItem(new Object[]{Users.getInt(i, "ID"), Users.getString(i, "NAME")});
            }
        }
    }
    
    private void SelectNodeOfTreeUsers() {
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree2.getLastSelectedPathComponent();
        if (SelectedNode == null) {
            Users.setPosition(-1);
            jPanel11.setVisible(false);
            jButton42.setVisible(CanEditUsers);
            jButton43.setVisible(false);
        } else {
            if (SelectedNode.getLevel()==1) {
                Object[] obj = (Object[]) SelectedNode.getUserObject();
                Users.setPosition((Integer) obj[1]);
                jButton43.setVisible(false);
                jButton42.setVisible(CanEditUsers);
                jPanel11.setVisible(false);
            } else {
                Object[] obj = (Object[]) SelectedNode.getUserObject();
                Users.setPosition((Integer) obj[1]);
                jButton43.setVisible(CanDeleteUsers);
                jButton42.setVisible(CanEditUsers);
                
                jPanel11.setVisible(true);

                JTextField[] mas = new JTextField[]{jTextField9,jTextField10,jTextField12,jTextField24};
                for (JTextField ma : mas) {
                    ma.setEditable(false);
                    ma.setBorder(null);
                    ma.setBackground(ma.getParent().getBackground());
                    ma.setForeground(new Color(200,0,0));
                }
                jCheckBox1.setEnabled(false);
                jComboBox12.setEnabled(false);
                jTextArea10.setEditable(false);
                jTextArea10.setForeground(new Color(200, 0, 0));
                jTextArea10.setBorder(BorderFactory.createEmptyBorder());
                jTextArea10.setBackground(jTextArea10.getParent().getParent().getParent().getBackground());
                jTextField9.setText(Users.getString("NAME"));
                jTextField10.setText(Users.getString("LOGIN"));
                jTextField12.setText(Users.getString("PASS"));
                jCheckBox1.setSelected((Boolean)Users.get("CAN_ENTER"));
                jTextField24.setText(Users.getString("PHONE"));
                jLabel129.setEnabled(false);
                jLabel129.setText("зашифрован");
                jTextArea10.setText(Users.getString("COMMENT"));
                jComboBox12.setSelectedIndex(Users.getInt("ID_POSITION"));
                
                if (Users.getInt("LEVEL")==STORAGE_WORKER && Users.getInt("ID_POSITION")==STORAGE_WORKER_PACKER) {
                    jLabel89.setVisible(true);
                    jComboBox13.setVisible(true);
                    jComboBox13.setEnabled(false);
                    fillListBrigadiers();
                    int idBoss = Users.getInt("ID_USER_BOSS");
                    jComboBox13.setSelected(idBoss);
                } else {
                    jLabel89.setVisible(false);
                    jComboBox13.setVisible(false);
                }

                jButton17.setVisible(CanEditUsers);
                jButton24.setVisible(false);
                jButton51.setVisible(false);
                
                if (Users.getInt("LEVEL")==STORAGE_WORKER) {
                    jScrollPane21.setVisible(true);
                    jScrollPane31.setVisible(true);
                    jPanel22.setVisible(true);
                    jPanel69.setVisible(true);
                    jPanel128.setVisible(true);
                    MakeTableOfUsersStorage();
                    MakeTableOfUsersDelivery();
                } else {
                    jScrollPane21.setVisible(false);
                    jScrollPane31.setVisible(false);
                    jPanel69.setVisible(false);
                    jPanel128.setVisible(false);
                    jPanel22.setVisible(false);                    
                }

            }
        }
        
    }

    private boolean GetClients(boolean OrderByAlphabet) {
        Object[][] obj;
        String filterText = "'%"+jTextField38.getText()+"%'";
        ClientState.STATE state = ((ComboboxClientState)jComboBox9).getSelectedState();
        String filterState = state==null ? "" : " AND state="+ClientState.getValueForDB(state);
        String filterUserCreator = jComboBox10.getSelectedID() ==-1 ? "" : " AND user_creator_id="+jComboBox10.getSelectedID();
        String currentUserId = "";
        
//        if (CurrentUser.getInt("LEVEL")==1) {
//            currentUserId = " AND user_creator_id="+CurrentUser.getInt("ID");
//        }
//         && CurrentUser.getInt("ID")!=185
                
        if (OrderByAlphabet) {
            obj = db.SelectSQL(new StringBuilder().append("SELECT id as client_id,official_name,"
                    + "(SELECT id_client FROM orders WHERE id_client=client_id LIMIT 1),"
                    + "state FROM client WHERE (official_name LIKE ")
                    .append(filterText).append(" OR name LIKE ").append(filterText)
                    .append(" OR phone1 LIKE ").append(filterText).append(" OR phone2 LIKE ")
                    .append(filterText).append(" OR phone3 LIKE ").append(filterText)
                    .append(" OR contact1 LIKE ").append(filterText).append(" OR contact2 LIKE ")
                    .append(filterText).append(" OR contact3 LIKE ").append(filterText)
                    .append(" OR email1 LIKE ").append(filterText).append(" OR email2 LIKE ")
                    .append(filterText).append(" OR email3 LIKE ").append(filterText)
                    .append(" OR edrpou LIKE ").append(filterText).append(")").append(currentUserId)
                    .append(filterState).append(filterUserCreator)
                    .append(" ORDER BY OFFICIAL_NAME").toString(),null);
        } else {
            obj = db.SelectSQL(new StringBuilder().append("SELECT id as client_id,official_name,"
                    + "(SELECT id_client FROM orders WHERE id_client=client_id LIMIT 1),"
                    + "state FROM client WHERE (official_name LIKE ").append(filterText)
                    .append(" OR name LIKE ").append(filterText).append(" OR phone1 LIKE ")
                    .append(filterText).append(" OR phone2 LIKE ").append(filterText)
                    .append(" OR phone3 LIKE ").append(filterText).append(" OR contact1 LIKE ")
                    .append(filterText).append(" OR contact2 LIKE ").append(filterText)
                    .append(" OR contact3 LIKE ").append(filterText).append(" OR email1 LIKE ")
                    .append(filterText).append(" OR email2 LIKE ").append(filterText)
                    .append(" OR email3 LIKE ").append(filterText).append(" OR edrpou LIKE ")
                    .append(filterText).append(")").append(filterState).append(currentUserId)
                    .append(filterUserCreator).append(" ORDER BY date_time DESC").toString(),null);
        }
        Clients.set(obj);
        return obj!=null;
    }
    
    public boolean GetFolders() {
        Folders.set(db.SelectSQL("SELECT id,name FROM folder ORDER BY name",new Object[]{}));
        Clients_Folders.set(db.SelectSQL("SELECT id_client,id_folder FROM client_folder",new Object[]{}));
        return !Folders.IsNull() && !Clients_Folders.IsNull();
    }
    
    private void MakeTreeOfClients() {
        
        String filterText = jTextField38.getText();

        LinkedList<Integer> expandedFolders = new LinkedList<>();        
        if (filterText.isEmpty()) { //если мы делаем фильтр по имени клиента, должны открыться все папки с отфильтрованными данными, а не только те, которые были открыты
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) jTree3.getModel().getRoot();
            for (int i = 0; i < root.getChildCount(); i++) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
                if (child.getChildCount() > 0) {
                    if (jTree3.isVisible(new TreePath(((DefaultMutableTreeNode) child.getChildAt(0)).getPath()))) {
                        expandedFolders.add((int) ((Object[]) child.getUserObject())[0]); //add folderID
                    }
                }
            }
        }
        
        int[] SaveCurrentRow = jTree3.getSelectionRows();
        
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new Object[]{"КЛИЕНТЫ",0});
        
        LinkedList<DefaultMutableTreeNode> foldersToExpand = new LinkedList<>();
        LinkedList<Integer> listAlreadyUsedClients = new LinkedList<>();
        
        for (int i = 0;i<Folders.getLength();i++) {
            int folderID = Folders.getInt(i,"ID");
            DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(new Object[]{folderID,Folders.getString(i,"NAME")});
            top.add(folderNode);
            for (int k = 0;k<Clients.getLength();k++) {
                int clientID = Clients.getInt(k,"ID");
                for (int j = 0;j<Clients_Folders.getLength();j++) {
                    if (folderID==Clients_Folders.getInt(j,"ID_FOLDER") && clientID==Clients_Folders.getInt(j,"ID_CLIENT")) {
                        folderNode.add(new DefaultMutableTreeNode(new Object[]{Clients.getString(k, "OFFICIAL_NAME"), k, Clients.getInt(k,"ISUSED")!=0, ClientState.getStateByValueDB(Clients.getInt(k,"STATE"))}));        
                        listAlreadyUsedClients.add(Clients.getInt(k,"ID"));
                        break;
                    }
                }
            }
            if (expandedFolders.contains(folderID) || !filterText.isEmpty()) {
                foldersToExpand.add(folderNode);
            }
        }
        
        DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(new Object[]{0,"без папки"});        
        top.add(folderNode);
        
        for (int i = 0;i<Clients.getLength();i++) {
            if (!listAlreadyUsedClients.contains(Clients.getInt(i,"ID"))) {
                folderNode.add(new DefaultMutableTreeNode(new Object[]{Clients.getString(i, "OFFICIAL_NAME"), i, Clients.getInt(i,"ISUSED")!=0, ClientState.getStateByValueDB(Clients.getInt(i,"STATE"))}));
            }
        }
        
        if (expandedFolders.contains(0) || !filterText.isEmpty()) {
            foldersToExpand.add(folderNode);
        }
         
        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree3.setModel(treeModel);
        
        jTree3.setSelectionRows(SaveCurrentRow);
        
        for (DefaultMutableTreeNode node : foldersToExpand) {
            jTree3.expandPath(new TreePath(node.getPath()));
        }
        
        SelectNodeOfTreeClients();  
    }
    
    private void SelectNodeOfTreeClients() {
        Object[] obj;
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree3.getLastSelectedPathComponent();
        if (SelectedNode == null) {
            CardLayout cl = (CardLayout)jPanel122.getLayout();
            cl.show(jPanel122, "card2");
            Clients.setPosition(-1);
            jPanel24.setVisible(false);
            jPanel172.setVisible(false);
            jPanel105.setVisible(false);
            jPanel25.setVisible(false);
            jPanel26.setVisible(false);
            jPanel55.setVisible(false);
            
            jPanel60.setVisible(false);
            jPanel32.setVisible(false);
            jPanel28.setVisible(false);
            jPanel31.setVisible(false);
            jPanel58.setVisible(false);
            jPanel56.setVisible(false);
            jPanel108.setVisible(false);
            jPanel109.setVisible(false);
            jPanel110.setVisible(false);
            jPanel111.setVisible(false);
            jPanel35.setVisible(false);
            jPanel33.setVisible(false);
            jPanel34.setVisible(false);
            jPanel36.setVisible(false);
            jPanel29.setVisible(false);
            jScrollPane11.setVisible(false);
            jButton36.setVisible(CanEditClients);
            jButton37.setVisible(false);
            jButton129.setVisible(false);
            jButton65.setVisible(false);
            jButton115.setVisible(false);
            
        } else {
           Calendar c = (Calendar) jDateChooser13.getCalendar().clone();
           c.set(Calendar.SECOND, 0);
           c.set(Calendar.MINUTE, 0);
           c.set(Calendar.HOUR_OF_DAY, 0);
           long startDate = c.getTimeInMillis();
           c = (Calendar) jDateChooser14.getCalendar().clone();
           c.set(Calendar.SECOND, 59);
           c.set(Calendar.MINUTE, 59);
           c.set(Calendar.HOUR_OF_DAY, 23);
           long endDate = c.getTimeInMillis();
            
            
            CardLayout cl;
            switch (SelectedNode.getLevel()) {
                case 0: //корень дерева
                    cl = (CardLayout)jPanel122.getLayout();
                    cl.show(jPanel122, "card3");
                    jButton36.setVisible(false);
                    jButton37.setVisible(false);
                    jButton129.setVisible(false);
                    if (userLevel == 1) {
                        jButton115.setVisible(false);
                        jButton65.setVisible(false);
                    } else {
                        jButton115.setVisible(true);
                        jButton65.setVisible(true);
                    }
                    Object dataa[][] = new Object[0][6];
                    SimpleDateFormat sdff = new SimpleDateFormat("dd MMM yyyy");
                    if (!Clients.IsNull()) {
                        dataa = new Object[Clients.getLength()][6];
                        
                        ObjectResultSet AllExtendedClients = new ObjectResultSet();
                        AllExtendedClients.setColumnNames(new String[]{"ID","DATE_TIME","OFFICIAL_NAME","CONTACT1","PHONE1","EMAIL1","ORDERED_AMOUNT","ISUSED"});                        
                        String filterText = "'%"+jTextField38.getText()+"%'";
                        ClientState.STATE state = ((ComboboxClientState)jComboBox9).getSelectedState();
                        String filterState = state==null ? "" : " AND state="+ClientState.getValueForDB(state);
                        String filterUserCreator = jComboBox10.getSelectedID() ==-1 ? "" : " AND user_creator_id="+jComboBox10.getSelectedID();

                        if (jToggleButton5.isSelected()) {
                            AllExtendedClients.set(db.SelectSQL("SELECT id as client_id,date_time,official_name,contact1,phone1,email1,(select sum(suborder.AMOUNT) from orders,suborder WHERE suborder.ID_ORDERS=orders.ID AND orders.ID_CLIENT=client_id AND orders.date_time BETWEEN ? AND ?),(SELECT id_client FROM orders WHERE id_client=client_id LIMIT 1) FROM client WHERE (name LIKE "+filterText+" OR phone1 LIKE "+filterText+" OR phone2 LIKE "+filterText+" OR phone3 LIKE "+filterText+" ) "+filterState+filterUserCreator+" ORDER BY NAME",new Object[]{startDate,endDate}));
                        } else {
                            AllExtendedClients.set(db.SelectSQL("SELECT id as client_id,date_time,official_name,contact1,phone1,email1,(select sum(suborder.AMOUNT) from orders,suborder WHERE suborder.ID_ORDERS=orders.ID AND orders.ID_CLIENT=client_id AND orders.date_time BETWEEN ? AND ?),(SELECT id_client FROM orders WHERE id_client=client_id LIMIT 1) FROM client WHERE (name LIKE "+filterText+" OR phone1 LIKE "+filterText+" OR phone2 LIKE "+filterText+" OR phone3 LIKE "+filterText+" ) "+filterState+filterUserCreator+" ORDER BY date_time DESC",new Object[]{startDate,endDate}));
                        }
                        for (int i = 0; i < AllExtendedClients.getLength(); i++) {
                            String color = AllExtendedClients.getInt(i,"ISUSED")!=0 ? "blue" : "gray";
                            dataa[i][0] = "<html><font color='"+color+"'>"+AllExtendedClients.getString(i, "OFFICIAL_NAME")+"</font></html>";
                            dataa[i][1] = sdff.format(new Date(AllExtendedClients.getInt(i,"DATE_TIME")*1000L));
                            dataa[i][2] = AllExtendedClients.getBigDecimalAsInt(i,"ORDERED_AMOUNT");
                            dataa[i][3] = AllExtendedClients.getString(i,"CONTACT1");
                            dataa[i][4] = AllExtendedClients.getString(i,"PHONE1");
                            dataa[i][5] = AllExtendedClients.getString(i,"EMAIL1");
                        }
                    }
                    jTable10.makeTable(dataa);
                    break;
                case 1: //папка
                    cl = (CardLayout)jPanel122.getLayout();
                    cl.show(jPanel122, "card3");
                    jButton36.setVisible(CanEditClients);
                    jButton37.setVisible(CanEditClients);
                    jButton129.setVisible(CanEditClients);
                    if (userLevel == 1) {
                        jButton115.setVisible(false);
                        jButton65.setVisible(false);
                    } else {
                        jButton115.setVisible(true);
                        jButton65.setVisible(true);
                    }
                    Object dataaa[][] = new Object[0][6];
                    SimpleDateFormat sdfff = new SimpleDateFormat("dd MMM yyyy");
                    if (!Clients.IsNull()) {
                        LinkedList<Object[]> list = new LinkedList<>();
                        
                        ObjectResultSet AllExtendedClients = new ObjectResultSet();
                        AllExtendedClients.setColumnNames(new String[]{"ID","DATE_TIME","OFFICIAL_NAME","CONTACT1","PHONE1","EMAIL1","ORDERED_AMOUNT","ISUSED"});                        
                        String filterText = "'%"+jTextField38.getText()+"%'";
                        ClientState.STATE state = ((ComboboxClientState)jComboBox9).getSelectedState();
                        String filterState = state==null ? "" : " AND state="+ClientState.getValueForDB(state);
                        String filterUserCreator = jComboBox10.getSelectedID() ==-1 ? "" : " AND user_creator_id="+jComboBox10.getSelectedID();

                        if (jToggleButton5.isSelected()) {
                            AllExtendedClients.set(db.SelectSQL("SELECT id as client_id,date_time,official_name,contact1,phone1,email1,(select sum(suborder.AMOUNT) from orders,suborder WHERE suborder.ID_ORDERS=orders.ID AND orders.ID_CLIENT=client_id AND orders.date_time BETWEEN ? AND ?),(SELECT id_client FROM orders WHERE id_client=client_id LIMIT 1) FROM client WHERE (name LIKE "+filterText+" OR phone1 LIKE "+filterText+" OR phone2 LIKE "+filterText+" OR phone3 LIKE "+filterText+" ) "+filterState+filterUserCreator+" ORDER BY NAME",new Object[]{startDate,endDate}));
                        } else {
                            AllExtendedClients.set(db.SelectSQL("SELECT id as client_id,date_time,official_name,contact1,phone1,email1,(select sum(suborder.AMOUNT) from orders,suborder WHERE suborder.ID_ORDERS=orders.ID AND orders.ID_CLIENT=client_id AND orders.date_time BETWEEN ? AND ?),(SELECT id_client FROM orders WHERE id_client=client_id LIMIT 1) FROM client WHERE (name LIKE "+filterText+" OR phone1 LIKE "+filterText+" OR phone2 LIKE "+filterText+" OR phone3 LIKE "+filterText+" ) "+filterState+filterUserCreator+" ORDER BY date_time DESC",new Object[]{startDate,endDate}));
                        }
                        
                        //костыль! сначала выбрали всех клиентов, а потом выбираем из них тех которые принадлежат выбранной папке
                        for (int i = 0; i < AllExtendedClients.getLength(); i++) {
                            int idClient = AllExtendedClients.getInt(i,"ID");
                            int idFolder = (int)(((Object[])SelectedNode.getUserObject())[0]);
                            boolean clientInThisFolder = false;
                            for (int j = 0;j<Clients_Folders.getLength();j++) {
                                if (idFolder==Clients_Folders.getInt(j,"ID_FOLDER") && idClient==Clients_Folders.getInt(j,"ID_CLIENT")) {
                                    clientInThisFolder = true;
                                    break;
                                }
                            }
                            if (clientInThisFolder || idFolder == 0) {
                                Object[] ob = new Object[6];
                                String color = AllExtendedClients.getInt(i, "ISUSED") != 0 ? "blue" : "gray";
                                ob[0] = "<html><font color='" + color + "'>" + AllExtendedClients.getString(i, "OFFICIAL_NAME") + "</font></html>";
                                ob[1] = sdfff.format(new Date(AllExtendedClients.getInt(i, "DATE_TIME") * 1000L));
                                ob[2] = AllExtendedClients.getBigDecimalAsInt(i, "ORDERED_AMOUNT");
                                ob[3] = AllExtendedClients.getString(i, "CONTACT1");
                                ob[4] = AllExtendedClients.getString(i, "PHONE1");
                                ob[5] = AllExtendedClients.getString(i, "EMAIL1");
                                list.add(ob);
                            }
                        }
                        
                        dataaa = list.toArray(new Object[][]{});
                    }
                    if (userLevel != 1) {
                        jTable10.makeTable(dataaa);
                    }   
                    break;
                case 2: //лист дерева - клиент
                    cl = (CardLayout)jPanel122.getLayout();
                    cl.show(jPanel122, "card2");                    
                    obj = (Object[]) SelectedNode.getUserObject();
                    Clients.setPosition((Integer) obj[1]);

                    ExtendedClient.set(db.SelectSQL("SELECT name,date_time,contact1,contact2,contact3,phone1,additional_phone1,fax,phone2,additional_phone2,phone3,additional_phone3,email1,email2,email3,site,address,comm,(SELECT name FROM user WHERE id=C.user_creator_id),edrpou FROM client C WHERE id=?",new Object[]{Clients.getInt("ID")}));                    
                    
                    jButton36.setVisible(CanEditClients);
                    jButton37.setVisible(CanDeleteClients);
                    jButton129.setVisible(false);
                    jButton65.setVisible(false);
                    jButton115.setVisible(false);
                    jPanel24.setVisible(true);
                    jPanel172.setVisible(true);
                    jPanel105.setVisible(true);
                    jPanel25.setVisible(true);
                    jPanel26.setVisible(true);
                    jPanel55.setVisible(true);
                    jPanel60.setVisible(true);
                    jPanel32.setVisible(true);
                    jPanel28.setVisible(true);
                    jPanel31.setVisible(true);
                    jPanel56.setVisible(true);
                    jPanel58.setVisible(true);
                    jPanel108.setVisible(true);
                    jPanel109.setVisible(true);
                    jPanel110.setVisible(true);
                    jPanel111.setVisible(true);
                    jPanel35.setVisible(true);
                    jPanel33.setVisible(true);
                    jPanel34.setVisible(true);
                    jPanel36.setVisible(true);
                    jPanel29.setVisible(true);
                    jScrollPane11.setVisible(true);
                    
                    jButton4.setVisible(CanEditClients);
                    jButton5.setVisible(false);
                    jButton6.setVisible(false);

                    JTextField[] mas = new JTextField[]{jTextField47, jTextField6, 
                        jTextField7, jTextField25,jTextField52, jTextField13, jTextField8, 
                        jTextField11, jTextField34, jTextField26, jTextField35,
                        jTextField27, jTextField28, jTextField29, jTextField15, jTextField14, jTextField22};
                    String[] values = new String[mas.length];
                    values[0] = Clients.getString("OFFICIAL_NAME");
                    values[1] = ExtendedClient.getString("CONTACT1");
                    values[2] = ExtendedClient.getString("PHONE1");
                    values[3] = ExtendedClient.getString("ADDITIONAL_PHONE1");
                    values[4] = ExtendedClient.getString("FAX");
                    values[5] = ExtendedClient.getString("EMAIL1");
                    values[6] = ExtendedClient.getString("CONTACT2");
                    values[7] = ExtendedClient.getString("PHONE2");
                    values[8] = ExtendedClient.getString("ADDITIONAL_PHONE2");
                    values[9] = ExtendedClient.getString("EMAIL2");
                    values[10] = ExtendedClient.getString("CONTACT3");
                    values[11] = ExtendedClient.getString("PHONE3");
                    values[12] = ExtendedClient.getString("ADDITIONAL_PHONE3");
                    values[13] = ExtendedClient.getString("EMAIL3");
                    values[14] = ExtendedClient.getString("ADDRESS");
                    values[15] = ExtendedClient.getString("SITE");
                    values[16] = ExtendedClient.getString("EDRPOU");

                    Long date = ExtendedClient.getInt("DATE_TIME") * 1000L;
                    jLabel74.setVisible(true);
                    jLabel74.setText((new SimpleDateFormat("dd MMM yyyy")).format(new Date(date)));
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date(date));
                    jDateChooser5.setVisible(false);
                    jDateChooser5.setCalendar(cal);

                    for (int i = 0; i < mas.length; i++) {
                        mas[i].setEditable(false);
                        mas[i].setBorder(null);
                        mas[i].setBackground(mas[i].getParent().getBackground());
                        mas[i].setForeground(new Color(200, 0, 0));
                        mas[i].setText(values[i]);
                    }

                    jTextArea7.setEditable(false);
                    jTextArea7.setBorder(null);
                    jTextArea7.setBackground(new Color(204,255,255));
                    jTextArea7.setForeground(new Color(200, 0, 0));
                    jTextArea7.setText(ExtendedClient.getString("COMM"));
                    
                    jComboBox8.setEnabled(false);
                    jComboBox8.setSelectedItem(ClientState.getStateByValueDB(Clients.getInt("STATE")));
                    
                    jComboBox11.setEnabled(false);
                    jComboBox11.setSelected(ExtendedClient.get("USER_CREATOR_NAME")==null ? "" : ExtendedClient.getString("USER_CREATOR_NAME"));

                    try {
                        ObjectResultSet OrdersOfClient = new ObjectResultSet();
                        OrdersOfClient.setColumnNames(new String[]{"ID", "DATE_TIME", "STATE","AMOUNT_GIFTS","SUMM_COST"});
                        Object[][] object = db.SelectSQL("SELECT orders.id,orders.date_time,orders.state,SUM(suborder.amount),SUM(suborder.amount*suborder.cost) FROM orders,suborder WHERE id_client=? AND suborder.id_orders=orders.id GROUP BY orders.id ORDER BY date_time DESC", new Object[]{Clients.getInt("ID")});
                        OrdersOfClient.set(object);
                        Object data[][] = new Object[0][4];
                        
                        if (!OrdersOfClient.IsNull()) {
                            data = new Object[OrdersOfClient.getLength()+1][4];
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                            
                            int sumAmount = 0;
                            double sumCost = 0;
                            
                            for (int i = 0; i < OrdersOfClient.getLength(); i++) {
                                data[i][0] = sdf.format(new Date(OrdersOfClient.getLong(i, "DATE_TIME")));
                                data[i][1] = OrdersOfClient.getBigDecimalAsLong(i,"AMOUNT_GIFTS");
                                data[i][2] = FormatUAH.format(OrdersOfClient.getDouble(i,"SUMM_COST"));
                                data[i][3] = OrdersOfClient.getInt(i, "STATE");
                                sumAmount+=OrdersOfClient.getBigDecimalAsLong(i,"AMOUNT_GIFTS");
                                sumCost+=OrdersOfClient.getDouble(i,"SUMM_COST");
                            }
                            
                            data[OrdersOfClient.getLength()][0] = "ИТОГО:";
                            data[OrdersOfClient.getLength()][1] = sumAmount;
                            data[OrdersOfClient.getLength()][2] = FormatUAH.format(sumCost);
                        }
                        jTable4.makeTable(data);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
            }
        }
    }
    

    private boolean GetGifts() {
        Object[][] obj = db.SelectSQL("SELECT * FROM gift ORDER BY name",null);
        Gifts.set(obj);
//        GetGiftFolders();
        return obj!=null;
    }
    
//    private boolean GetGiftFolders() {
//        Object[][] obj = db.SelectSQL("SELECT DISTINCT folder_name FROM gift ORDER BY name",null);
//        GiftFolders.set(obj);
//        return obj!=null;
//    }

    private void MakeTreeOfGifts() {
//
//        
//        System.out.println("Make Tree of Gifts - 1883");
        int[] SaveCurrentRow = jTree4.getSelectionRows();

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new Object[]{"",0});
//        for (int i=0; i<GiftFolders.getLength(); i++) {
//            DefaultMutableTreeNode child_0 = new DefaultMutableTreeNode(new Object[]{"Картон", 0}); // -------HOW?!
//        }
//        DefaultMutableTreeNode child_0 = new DefaultMutableTreeNode(new Object[]{"Картон", 0});
//        DefaultMutableTreeNode child_1 = new DefaultMutableTreeNode(new Object[]{"Пакет", 1});
//        DefaultMutableTreeNode child_2 = new DefaultMutableTreeNode(new Object[]{"Туба", 2});
//        DefaultMutableTreeNode child_3 = new DefaultMutableTreeNode(new Object[]{"Жесть", 3});
//        DefaultMutableTreeNode child_4 = new DefaultMutableTreeNode(new Object[]{"Игрушка", 4});
//        top.add(child_0);
//        top.add(child_1);
//        top.add(child_2);
//        top.add(child_3);
//        top.add(child_4);
        for (int i = 0; i < Gifts.getLength(); i++) {
            top.add(new DefaultMutableTreeNode(new Object[]{Gifts.getString(i, "NAME"), i}));
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree4.setModel(treeModel);
        jTree4.setSelectionRows(SaveCurrentRow);
        SelectNodeOfTreeGifts();
    }

    private void SelectNodeOfTreeGifts() {
        if (jTree4.getSelectionCount()==0) {
            jPanel81.setVisible(false);
            Gifts.setPosition(-1);
            jButton39.setVisible(false);
            jButton63.setVisible(false);
            jButton38.setVisible(CanEditGifts);
            jButton7.setVisible(false);
            jButton8.setVisible(false);
        } else {
            jButton38.setVisible(CanEditGifts);
            jButton39.setVisible(CanDeleteGifts);
            jButton63.setVisible(CanEditGifts);
            jPanel81.setVisible(true);
            
            DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree4.getLastSelectedPathComponent();
            Object[] obj = (Object[])SelectedNode.getUserObject();
            Gifts.setPosition((Integer)obj[1]);
            GetGiftsCandy();
            MakeTableOfGiftsCandy();
            
            
            jTextField21.setText(Gifts.getString("NAME"));
            jTextField21.setBorder(null);
            jTextField21.setEditable(false);
            jTextField21.setBackground(jTextField21.getParent().getBackground());
            jTextField21.setForeground(new Color(200,0,0));
            
            jTextField32.setText(FormatUAH.format(Gifts.getDouble("COST_PACKING")));
            jTextField32.setBorder(null);
            jTextField32.setEditable(false);
            jTextField32.setBackground(jTextField32.getParent().getBackground());
            jTextField32.setForeground(new Color(200,0,0));            
            
            jButton33.setVisible(CanEditGifts);
            jButton40.setVisible(false);
            jButton41.setVisible(false);
            jButton7.setVisible(CanEditGifts);
            jButton8.setVisible(CanEditGifts);
        }
    }

    private boolean GetGiftsCandy() {
        Object[][] obj = db.SelectSQL("SELECT gift_candy.id,gift_candy.id_candy,gift_candy.amount,candy.name,candy.box_weight,candy.amount_in_box,candy.cost_kg,factory.name FROM gift_candy,candy,factory WHERE gift_candy.id_candy=candy.id AND candy.id_factory=factory.id AND gift_candy.id_gift=? ORDER BY gift_candy.id", new Object[]{Gifts.getInt("ID")});
        Gift_Candy.set(obj);
        return obj!=null;
    }

    private void MakeTableOfGiftsCandy() {
        Object data[][] = new Object[0][5];
        if (!Gift_Candy.IsNull()) {
            double weight;
            double cost;
            int SummAmount = 0;
            double SummWeight = 0;
            double SummCost = 0;
            data = new Object[Gift_Candy.getLength() + 1][5];
            for (int i = 0; i < Gift_Candy.getLength(); i++) {
                data[i][0] = Gift_Candy.getString(i, "FACTORY_NAME");
                data[i][1] = Gift_Candy.getString(i, "CANDY_NAME");
                int amount = Gift_Candy.getInt(i, "AMOUNT");
                data[i][2] = amount;
                SummAmount+=amount;
                weight = Gift_Candy.getDouble(i, "BOX_WEIGHT") / Gift_Candy.getInt(i, "AMOUNT_IN_BOX");
                SummWeight+=amount*weight;
                cost = Gift_Candy.getDouble(i, "COST_KG")*weight;
                SummCost+=amount*cost;
                data[i][3] = FormatUAH.format(amount * cost);                
                data[i][4] = FormatKG.format(amount * weight);
            }
            data[data.length-1][1] = "ИТОГО:";
            data[data.length-1][2] = SummAmount;
            data[data.length-1][3] = FormatUAH.format(SummCost);
            data[data.length-1][4] = FormatKG.format(SummWeight);
        }
        jTable1.makeTable(data, this,CanEditGifts);
    }

    public void SaveChangesInTableGiftsCandy(int col, int row, Object value) {
        if (db.UpdateSQL("UPDATE gift_candy SET amount=? WHERE id=?",new Object[]{value,Gift_Candy.getInt(row, "ID")})) {
            SelectNodeOfTreeGifts();
        } else {
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }
    }
    
    public void SaveChangesInTableSubOrders(int col, int row, Object value) {
        boolean result = false;
        if (col==4) {
            if (db.UpdateSQL("UPDATE suborder SET amount=? WHERE id=? LIMIT 1",new Object[]{value,SubOrders.getInt(row, "ID")})) {
                SelectNodeOfTableOrders();
                result=true;
            }
        } else if (col==3) {
            try {
                double d = Double.parseDouble(((String)value).replaceAll(" ", "").replace(',', '.'));
                if (db.UpdateSQL("UPDATE suborder SET cost=? WHERE id=? LIMIT 1",new Object[]{d,SubOrders.getInt(row,"ID")})) {
                    SelectNodeOfTableOrders();
                    result = true;
                }
            } catch (Exception ex) {
                result = false;
            }
        }

        if (!result) {
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }
    }

    private void GetFilteredPackings() {
        Integer value = null;
        try {
            value = Integer.parseInt(jTextField50.getText());
        } catch (Exception ex) {
        }
        String filterText = value!=null ? " WHERE number="+value : (jTextField50.getText().isEmpty() ? "" : " WHERE name LIKE '%"+jTextField50.getText()+"%'");
        Object[][] obj = db.SelectSQL("SELECT ID,NAME,TYPE,NUMBER,CAPACITY,STORAGE,RESERVED,FILENAME,COST,MARKED FROM packing "+filterText+" ORDER BY number,name",null);
        FilteredPackings.set(obj);
    }    
    
    private void GetPackings() {
        String filterNumberPack = "";
        try {
            filterNumberPack = jTextField56.getText().isEmpty() ? "" : " WHERE number LIKE '"+Integer.parseInt(jTextField56.getText())+"%'";
        } catch (NumberFormatException nfe) {
            System.out.println("Only number of package is allowed! " + nfe.getMessage());
        }
        Object[][] obj = db.SelectSQL("SELECT ID,NAME,TYPE,NUMBER,CAPACITY,STORAGE,RESERVED,FILENAME,COST,MARKED FROM packing" +filterNumberPack+ " ORDER BY number,name",null);
        Packings.set(obj);
    }
    
    private void MakeTreeOfPackings() {
        int[] SaveCurrentRow = jTree6.getSelectionRows();

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
        
        for (int i = 0; i < Packings.getLength(); i++) {
            int type = Packings.getInt(i, "TYPE");
            String filename = Packings.getString(i, "FILENAME").trim();
            if (!PackingsImages.existsImageWithName(filename)) {
                if ((filename != null) && (!"".equals(filename))) {
                    try {
                        if (!thereWereProblemsWithFTP) {
                            Image image = FTP.readImageFromFileOrDownloadFromFTP(filename);
                            PackingsImages.addImage(image, filename);                    
                        }
                    } catch (Exception ex) {
                        thereWereProblemsWithFTP = true;
                        //JOptionPane.showMessageDialog(null, "Проблемы с загрузкой изображений с сервера:\n" + ex.getMessage());
                        //break;
                    }
                }
            }
            
            DefaultMutableTreeNode dftn = new DefaultMutableTreeNode(new Object[]{"№"+Integer.toString(Packings.getInt(i, "NUMBER"))+"  "+Packings.getString(i, "NAME"), i,FormatKG.format(Packings.getDouble(i,"CAPACITY"))+" кг",PackingsImages.getImage(filename),Packings.getBoolean(i,"MARKED")});
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
        jTree6.setModel(treeModel);
        for (int i = 0; i <= jTree6.getRowCount() - 1; i++) {
            jTree6.expandRow(i);
        }
        jTree6.setSelectionRows(SaveCurrentRow);
        SelectNodeOfTreePackings(); 
        
        if (thereWereProblemsWithFTP) {
            JOptionPane.showMessageDialog(null, "Проблемы с загрузкой изображений с сервера");
        }
    }
    
    private void SelectNodeOfTreePackings() {
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree6.getLastSelectedPathComponent();
        if (SelectedNode==null) {
            jPanel112.setVisible(false);
            jPanel43.setVisible(false);
            jPanel46.setVisible(false);
            jPanel47.setVisible(false);
            jPanel48.setVisible(false);
            jPanel49.setVisible(false);
            jPanel52.setVisible(false);
            jPanel120.setVisible(false);
            jPanel185.setVisible(false);
            jButton35.setVisible(false);
            jButton34.setVisible(CanEditPackings);
            Packings.setPosition(-1);
        } else {
            Object[] obj = (Object[]) SelectedNode.getUserObject();
            Packings.setPosition((Integer) obj[1]);            
            switch (SelectedNode.getLevel()) {
                case 1:
                    jPanel112.setVisible(false);
                    jPanel43.setVisible(false);
                    jPanel46.setVisible(false);
                    jPanel47.setVisible(false);
                    jPanel48.setVisible(false);
                    jPanel49.setVisible(false);
                    jPanel52.setVisible(false);
                    jPanel120.setVisible(false);
                    jPanel185.setVisible(false);
                    jButton35.setVisible(CanDeletePackings);
                    jButton34.setVisible(CanEditPackings);
                    break;
                case 2:
                    ObjectResultSet ExtendedPacking = new ObjectResultSet();
                    ExtendedPacking.setColumnNames(new String[]{"WEIGHT","COST","COMM"});
                    ExtendedPacking.set(db.SelectSQL("SELECT weight,cost,comm FROM packing WHERE id=?",new Object[]{Packings.getInt("ID")}));
                    
                    jPanel112.setVisible(true);
                    jPanel43.setVisible(true);
                    jPanel46.setVisible(true);
                    jPanel47.setVisible(true);
                    jPanel48.setVisible(true);
                    jPanel49.setVisible(true);
                    jPanel120.setVisible(true);
                    jPanel185.setVisible(true);
                    jPanel52.setVisible(true);

                    JTextField[] mas = new JTextField[]{jTextField17,jTextField30,jTextField18,jTextField19,jTextField20};
                    for (JTextField ma : mas) {
                        ma.setEditable(false);
                        ma.setBorder(null);
                        ma.setBackground(ma.getParent().getBackground());
                        ma.setForeground(new Color(200, 0, 0));
                    }
                    jTextField17.setText(Packings.getString("NAME"));
                    jTextField30.setText(Integer.toString(Packings.getInt("NUMBER")));
                    jTextField18.setText(FormatUAH.format(ExtendedPacking.getDouble("COST")));
                    jTextField19.setText(FormatKG.format(ExtendedPacking.getDouble("WEIGHT")));
                    jTextField20.setText(FormatKG.format(Packings.getDouble("CAPACITY")));
                    jTextArea6.setText(ExtendedPacking.getString("COMM"));
                    jTextArea6.setBackground(jTextArea6.getParent().getParent().getParent().getBackground());
                    jTextArea6.setForeground(new Color(200,0,0));
                    jTextArea6.setEditable(false);
                    jTextArea6.setBorder(null);
                    jCheckBox4.setSelected(Packings.getBoolean("MARKED"));
                    jCheckBox4.setEnabled(false);
                    
                    Image image = null;
                    String filename = Packings.getString("FILENAME").trim();
                    if ((filename != null) && (!"".equals(filename))) {
                        try {
                            File imagefile = new java.io.File("c:\\temp\\" + filename);
                            image = ImageIO.read(imagefile);
                        } catch (Exception ex) {
                        }
                    }
                    if (image!=null) {
                        jLabel60.setIcon(new ImageIcon(image.getScaledInstance(150, 150, Image.SCALE_FAST)));
                    } else {
                        jLabel60.setIcon(null);
                    }
                    
                    jButton9.setVisible(false);
                    jButton45.setVisible(false);
                    jButton13.setVisible(CanEditPackings);
                    jButton14.setVisible(false);
                    jButton15.setVisible(false);
                    jButton35.setVisible(CanDeletePackings);
                    jButton34.setVisible(CanEditPackings);
                    break;
            }
        }
    }
    
    public boolean GetOrders() {        
        String filter = "";
        if (userLevel==STORAGE_HEAD) { //начальник склада
            int selectedState = jComboBox1.getSelectedIndex()-1;
            if (selectedState==-1) {
                filter = " AND (orders.state=1 OR orders.state=2 OR orders.state=3) ";
            } else {
                filter = selectedState==1 || selectedState==2 || selectedState==3 ? (" AND orders.state="+selectedState+" ") : " AND orders.state=-100";
            }
        } else if (userLevel==LOGIST) { //логист
            int selectedState = jComboBox1.getSelectedIndex()-1;
            if (selectedState==-1) {
                filter = " AND (orders.state=3 OR orders.state=4) ";
            } else {
                filter = selectedState==3 || selectedState==4 ? (" AND orders.state="+selectedState+" ") : " AND orders.state=-100";
            }
        } else {
            switch (jComboBox1.getSelectedIndex()) {
                case 0:
                    filter = " ";
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    filter = " AND orders.state="+(jComboBox1.getSelectedIndex()-1)+" ";
                    break;
                case 8:
                    filter = " AND orders.state=-1 ";
                    break;
            }
        }
        
        try {
            int year = (int)jComboBox4.getSelectedItem();
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 1, 0, 0, 0);
            long StartTime = cal.getTimeInMillis();
            cal.set(year+1, 0, 1, 0, 0, 0);
            long EndTime = cal.getTimeInMillis();
            
            String filterUserCreator="";
//            if (userLevel==MANAGER) {
//                filterUserCreator = " AND orders.user_creator_id="+CurrentUser.getInt("ID");
//            } else {
            filterUserCreator = jComboBox6.getSelectedID()==-1 ? "" : " AND orders.user_creator_id="+jComboBox6.getSelectedID();
//            }
            String filterEdrpou = jTextField46.getText().isEmpty() ? "" : " AND client.EDRPOU LIKE '%"+jTextField46.getText()+"%'";
            String filterOrderNumber = jTextField43.getText().isEmpty() ? "" : " AND orders.number LIKE '%"+jTextField43.getText()+"%'";
            String filterGiftName = " AND suborder.id_orders=orders.id AND suborder.id_gift=gift.id AND gift.name LIKE '%"+jTextField48.getText()+"%'";
//            String filterTypePay = jComboBox15.getSelectedIndex()<=0 ? "" : (jComboBox15.getSelectedIndex()==2 ? " AND orders.type_pay IN (2,3)" : " AND orders.type_pay NOT IN (2,3)");
            String filterTypePay = jComboBoxPaymentTypesOrderList.getSelectedIndex()<=0 ? "" : " AND orders.type_pay = '" + (jComboBoxPaymentTypesOrderList.getSelectedIndex()-1) + "'";
            
            
            String sql;
            if (jRadioButton6.isSelected()) {
                if (jTextField48.getText().isEmpty()) {
                    sql = "SELECT DISTINCT orders.id,orders.date_time,orders.id_client,"
                            + "client.official_name,orders.state,0,orders.number FROM orders,"
                            + "client, suborder WHERE orders.id_client=client.id "+filter+
                            " AND client.official_name LIKE '%"+jTextField39.getText()
                            +"%' AND orders.date_time BETWEEN "+StartTime+" AND "
                            +EndTime+filterUserCreator+filterEdrpou+filterOrderNumber
                            +filterTypePay+" ORDER BY orders.date_time DESC";
                } else { //запрос с фильтром по номеру подарка
                    sql = "SELECT DISTINCT orders.id,orders.date_time,orders.id_client,"
                            + "client.official_name,orders.state,0,orders.number FROM orders,"
                            + "client,gift,suborder WHERE orders.id_client=client.id "
                            +filter+" AND client.official_name LIKE '%"+jTextField39.getText()
                            +"%' AND orders.date_time BETWEEN "+StartTime+" AND "
                            +EndTime+filterUserCreator+filterEdrpou+filterOrderNumber
                            +filterGiftName+filterTypePay+" ORDER BY orders.date_time DESC";
                }
            } else {
                if (jTextField48.getText().isEmpty()) {
                    sql = "SELECT DISTINCT orders.id AS _id_,orders.date_time,orders.id_client,"
                            + "client.official_name,orders.state,(SELECT MIN(date_time) "
                            + "FROM delivery WHERE delivery.id_orders=_id_) AS min_delivery,"
                            + "orders.number FROM orders,client,gift,suborder "
                            + "WHERE orders.id_client=client.id "+filter
                            +" AND client.official_name LIKE '%"+jTextField39.getText()
                            +"%' AND orders.date_time BETWEEN "+StartTime+" AND "
                            +EndTime+filterUserCreator+filterEdrpou+filterOrderNumber
                            +filterGiftName+filterTypePay+" ORDER BY min_delivery";
                } else {
                    sql = "SELECT DISTINCT orders.id AS _id_,orders.date_time,orders.id_client,"
                            + "client.official_name,orders.state,(SELECT MIN(date_time) "
                            + "FROM delivery WHERE delivery.id_orders=_id_) AS min_delivery,"
                            + "orders.number FROM orders,client,gift,suborder "
                            + "WHERE orders.id_client=client.id "+filter
                            +" AND client.official_name LIKE '%"+jTextField39.getText()
                            +"%' AND orders.date_time BETWEEN "+StartTime+" AND "
                            +EndTime+filterUserCreator+filterEdrpou+filterOrderNumber
                            +filterGiftName+filterGiftName+filterTypePay+" ORDER BY min_delivery"; 
                }
            }
            Object[][] obj = db.SelectSQL(sql,null);
            Orders.set(obj);
            return obj != null;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public boolean areOrdersSortedByDateOfOrder() {
        return jRadioButton6.isSelected();
    }
    
    public void MakeTableOfOrders() {
        int SelectedRow = jTable3.getSelectedRow();
        String[] columnNames;
        if (jRadioButton6.isSelected()) {
            columnNames = new String[]{"Заказ","№", "Клиент",""};
        } else {
            columnNames = new String[]{"Доставка","№", "Клиент",""};
        }
        Object data[][] = new Object[0][columnNames.length+1];
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        
//        int amount = 0;
//        int currentAmount;
//        double cost = 0;
//        double currentCost;
     
        if (!Orders.IsNull()) {
            
            data = new Object[Orders.getLength()][columnNames.length+1];
            int j = 0;
            for (int i = 0; i < Orders.getLength(); i++) {
                    if (jRadioButton6.isSelected()) {
                        data[j][0] = sdf.format(new Date(Orders.getLong(i,"DATE_TIME")));
                    } else {
                        if (Orders.get(i,"MIN_DATE_DELIVERY")==null) {
                            data[j][0] = "-";
                        } else {
                            int minDate = Orders.get(i,"MIN_DATE_DELIVERY") instanceof Integer ? Orders.getInt(i,"MIN_DATE_DELIVERY") : Orders.getLong(i,"MIN_DATE_DELIVERY").intValue();
                            data[j][0] = sdf.format(new Date(minDate*1000L));
                        }
                    }
                    data[j][1] = Orders.getString(i,"NUMBER");
                    data[j][2] = Orders.getString(i,"CLIENT_NAME");
                    data[j][3] = Orders.getInt(i,"STATE");
//                    data[j][4] = System.currentTimeMillis()-Orders.getLong(i,"DATE_TIME")<36*3600*1000; //заказы младше 36 часов будут выделяться цветом
                    
                    j++;
                    
                    //Автоматический подсчет количеста подарков в выбраных заказах и их общей суммы
//                    GetOrdersCount(i);
//                    for (int k=0; k<OrdersCount.getLength(); k++) {
//                        currentAmount = OrdersCount.getInt(k,"AMOUNT");
//                        currentCost = (double) currentAmount * OrdersCount.getDouble(k,"COST");
//                        cost += currentCost;
//                        amount += currentAmount;
//                    }

            }
        }
        jTable3.makeTable(columnNames, data);
        if ((SelectedRow!=-1) && (SelectedRow<jTable3.getRowCount())) {
            jTable3.setRowSelectionInterval(SelectedRow, SelectedRow);
        }
        SelectNodeOfTableOrders();
        
        jPanel59.setVisible(true); 
    }
    
    public void ShowGiftsAndSumm() {
        int amount = 0;
        int currentAmount;
        double cost = 0;
        double currentCost;
        
        for (int i = 0; i < Orders.getLength(); i++) {
            //Подсчет количеста подарков в выбраных заказах и их общей суммы
            GetOrdersCount(i);
            for (int k=0; k<OrdersCount.getLength(); k++) {
                currentAmount = OrdersCount.getInt(k,"AMOUNT");
                currentCost = (double) currentAmount * OrdersCount.getDouble(k,"COST");
                cost += currentCost;
                amount += currentAmount;
            }
        }
        
        JOptionPane.showMessageDialog(null, amount + " подарков на сумму " + FormatUAH.format(cost));
        
        
//        jTextField5.setText(Integer.toString(amount));
//        jTextField36.setText(FormatUAH.format(cost));
        
    }
    
    public void SelectNodeOfTableOrders() {
        int SelectedRow = jTable3.getSelectedRow();
        if (SelectedRow==-1) {
            jPanel85.setVisible(false);
            jPanel96.setVisible(false);
            Orders.setPosition(-1);
            jTabbedPane4.setTitleAt(0, "Состав заказа");
        } else {
            jPanel85.setVisible(true);
            jPanel96.setVisible(true);
            Orders.setPosition(SelectedRow);
            jTabbedPane4.setTitleAt(0, "Состав заказа №"+Orders.getString("NUMBER"));            
            Object[][] obj = db.SelectSQL("SELECT type_pay,date_pay,date_pack,prepay,discount,comm_packing,comm,delivery_cost,(SELECT user.name FROM user WHERE id=ORD.user_creator_id LIMIT 1) FROM orders ORD WHERE id=?",new Object[]{Orders.getInt("ID")});
            ExtendedOrder.set(obj);
            
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy HH:mm");
            
            int type_pay = ExtendedOrder.getInt("TYPE_PAY");
            switch (type_pay) {
                case 0:
                    jLabel79.setText("наличный, предоплата");
                    jComboBoxPaymentTypesOrderDetail.setSelectedIndex(0);
                    break;
                case 1:
                    jLabel79.setText("наличный, по факту");
                    jComboBoxPaymentTypesOrderDetail.setSelectedIndex(1);
                    break;
                case 2:
                    jLabel79.setText("безналичный, ТОВ");
                    jComboBoxPaymentTypesOrderDetail.setSelectedIndex(2);
                    break;
                case 3:
                    jLabel79.setText("безналичный, ФОП");
                    jComboBoxPaymentTypesOrderDetail.setSelectedIndex(3);
                    break;
                case 4:
                    jLabel79.setText("наложенный платеж");
                    jComboBoxPaymentTypesOrderDetail.setSelectedIndex(4);
                    break;
                case 5:
                    jLabel79.setText("предоплата на карту");
                    jComboBoxPaymentTypesOrderDetail.setSelectedIndex(5);
                    break;
                case 6:
                    jLabel79.setText("ФОП Брукша");
                    jComboBoxPaymentTypesOrderDetail.setSelectedIndex(6);
                    break;
                case 7:
                    jLabel79.setText("ФОП Вацик");
                    jComboBoxPaymentTypesOrderDetail.setSelectedIndex(7);
                    break;
                case 8:
                    jLabel79.setText("ФОП Калиева");
                    jComboBoxPaymentTypesOrderDetail.setSelectedIndex(8);
                    break;
                default:
                    jLabel79.setText("");
                    jComboBoxPaymentTypesOrderDetail.setSelectedIndex(-1);
            }                    
            jLabel79.setVisible(true);
            jComboBoxPaymentTypesOrderDetail.setVisible(false);
            
            jTextArea8.setText(ExtendedOrder.getString("COMM"));            
            jTextArea8.setEditable(false);
            jTextArea8.setBackground(jTextArea8.getParent().getParent().getParent().getBackground());
            jTextArea8.setForeground(new Color(200,0,0));
            jTextArea8.setBorder(BorderFactory.createEmptyBorder());

            jTextArea9.setText(ExtendedOrder.getString("COMM_PACKING"));            
            jTextArea9.setEditable(false);
            jTextArea9.setBackground(jTextArea9.getParent().getParent().getParent().getBackground());
            jTextArea9.setForeground(new Color(200,0,0));
            jTextArea9.setBorder(BorderFactory.createEmptyBorder());
            
            if (userLevel == 1) {
                if (CanEditOrders) {
                    jButton27.setVisible(true);
                } else {
                    jButton27.setVisible(false);
                }
            } else {
                if ((userLevel == 0) && (Orders.getInt("STATE")<Order.ORDER_DONE)) {
                    jButton27.setVisible(true);
                } else {
                    jButton27.setVisible(false);
                }
            }
            
            jButtonSaveOrder.setVisible(false);
            jButton29.setVisible(false);
            
            int d = ExtendedOrder.getInt("DATE_PAY");
            if (d==0) {
                jLabel82.setText("не оплачено");
                jLabel82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/exclamation-red-icon.png")));
            } else {
                jLabel82.setText(sdf2.format(new Date(d*1000L)));
                jLabel82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png")));
            }
            
            d = ExtendedOrder.getInt("DATE_PACK");
            if (d==0){
                jLabel84.setText("не упаковано");
                jLabel84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/exclamation-red-icon.png")));
            } else {
                jLabel84.setText(sdf2.format(new Date(d*1000L)));
                jLabel84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png")));
            }
          
            jTextField33.setEditable(false);
            jTextField33.setText(FormatUAH.format(ExtendedOrder.getDouble("PREPAY")));
            jTextField33.setBorder(BorderFactory.createEmptyBorder());
            jTextField33.setBackground(jTextField33.getParent().getBackground());
            jTextField33.setForeground(new Color(200,0,0));
            
            jTextField37.setEditable(false);
            jTextField37.setText(FormatUAH.format(ExtendedOrder.getDouble("DISCOUNT")));
            jTextField37.setBorder(BorderFactory.createEmptyBorder());
            jTextField37.setBackground(jTextField33.getParent().getBackground());
            jTextField37.setForeground(new Color(200,0,0));
            
            jTextField41.setEditable(false);
            jTextField41.setText(FormatUAH.format(ExtendedOrder.getDouble("DELIVERY_COST")));
            jTextField41.setBorder(BorderFactory.createEmptyBorder());
            jTextField41.setBackground(jTextField41.getParent().getBackground());
            jTextField41.setForeground(new Color(200,0,0));
            
            int state = Orders.getInt("STATE");
            if (state==Order.ORDER_CANCEL) {
                jButton11.setVisible(false);
                jButton12.setVisible(false);
                jButton16.setVisible(false);
                jButton74.setVisible(false);
                jButton19.setVisible(false);
                jButton78.setVisible(false);
                jButton100.setVisible(false);
            } else if ((state==Order.ORDER_PREPARE) && (CanEditOrders)) {
                jButton11.setVisible(true);
                jButton12.setVisible(true);
                jButton16.setVisible(true);
                jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/coins-icon.png")));
                jButton74.setVisible(false);
                jButton19.setVisible(false);
                jButton78.setVisible(false);
                jButton100.setVisible(false);
            } else if ((state==Order.ORDER_PREPARE) && (!CanEditOrders)) {
                jButton11.setVisible(false);
                jButton12.setVisible(false);
                jButton16.setVisible(false);
                jButton74.setVisible(false);
                jButton19.setVisible(false);
                jButton78.setVisible(false);
                jButton100.setVisible(false);
            } else if ((state==Order.ORDER_PAY) && (CanPayOrders)) {
                if (CurrentUser.getInt("LEVEL")==DIRECTOR) {
                    jButton11.setVisible(true);
                    jButton12.setVisible(true);
                } else {
                    jButton11.setVisible(false);
                    jButton12.setVisible(false);
                }
                jButton16.setVisible(true);
                jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/upakovka_146_2.png")));
                jButton74.setVisible(false);
                jButton19.setVisible(false);
                jButton78.setVisible(false);
                jButton100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/exclamation-red-icon.png")));                
                jButton100.setVisible(true);
            } else if ((state==Order.ORDER_PAY) && (!CanPayOrders)) {
                jButton11.setVisible(false);
                jButton12.setVisible(false);
                jButton16.setVisible(false);
                jButton74.setVisible(false);
                jButton19.setVisible(false);
                jButton78.setVisible(false);
                jButton100.setVisible(CanEditOrders);
            } else if ((state==Order.ORDER_PACK) && (CanPackOrders)) {
                if (CurrentUser.getInt("LEVEL")==DIRECTOR) {
                    jButton11.setVisible(true);
                    jButton12.setVisible(true);
                } else {
                    jButton11.setVisible(false);
                    jButton12.setVisible(false);
                }
                jButton16.setVisible(true);
                jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/lock-icon2.png")));
                jButton74.setVisible(true);
                jButton19.setVisible(false);
                jButton78.setVisible(true); 
                jButton100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/coins-icon.png")));
                jButton100.setVisible(true);
            } else if ((state==Order.ORDER_PACK) && (!CanPackOrders)) {
                jButton11.setVisible(false);
                jButton12.setVisible(false);
                jButton16.setVisible(false);
                jButton74.setVisible(false);
                jButton19.setVisible(false);
                jButton78.setVisible(false);
                jButton100.setVisible(false);
            } else if ((state==Order.ORDER_STORAGE)  && (CurrentUser.getInt("LEVEL")==DIRECTOR)) {
                jButton11.setVisible(true);
                jButton12.setVisible(true);
                jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Lorry-icon.png")));
                jButton16.setVisible(true);
                jButton74.setVisible(false);
                jButton19.setVisible(true);
                jButton78.setVisible(false);
                jButton100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/upakovka_146_2.png")));
                jButton100.setVisible(true);
            } else if ((state==Order.ORDER_STORAGE)  && (CurrentUser.getInt("LEVEL")!=DIRECTOR)) {
                jButton11.setVisible(false);
                jButton12.setVisible(false);
                jButton16.setVisible(false);
                jButton74.setVisible(false);
                jButton19.setVisible(true);
                jButton78.setVisible(false);
                jButton100.setVisible(false);
            } else if ((state==Order.ORDER_SEND) && (CurrentUser.getInt("LEVEL")==DIRECTOR)) {
                jButton11.setVisible(true);
                jButton12.setVisible(true);
                jButton16.setVisible(true);
                jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png")));
                jButton74.setVisible(false);
                jButton19.setVisible(true);
                jButton78.setVisible(false);
                jButton100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/lock-icon2.png")));
                jButton100.setVisible(true);
            } else if ((state==Order.ORDER_SEND) && (CurrentUser.getInt("LEVEL")!=DIRECTOR)) {
                jButton11.setVisible(false);
                jButton12.setVisible(false);
                jButton16.setVisible(false);
                jButton74.setVisible(false);
                jButton19.setVisible(true);
                jButton78.setVisible(false); 
                jButton100.setVisible(false);
            }
//                else if ((state==Order.ORDER_DELIVERED) && (CurrentUser.getInt("LEVEL")==DIRECTOR)) {
//                jButton11.setVisible(false);
//                jButton12.setVisible(false);
//                jButton16.setVisible(true);
//                jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png")));
//                jButton74.setVisible(false);
//                jButton19.setVisible(true);
//                jButton78.setVisible(false); 
//                jButton100.setVisible(false);
//            } else if ((state==Order.ORDER_DELIVERED) && (CurrentUser.getInt("LEVEL")!=DIRECTOR)) {
//                jButton11.setVisible(false);
//                jButton12.setVisible(false);
//                jButton16.setVisible(false);
//                jButton74.setVisible(false);
//                jButton19.setVisible(true);
//                jButton78.setVisible(false); 
//                jButton100.setVisible(false);
//            }
            else if ((state==Order.ORDER_DONE) && (CurrentUser.getInt("LEVEL")==DIRECTOR)) {
                jButton11.setVisible(false);
                jButton12.setVisible(false);
                jButton16.setVisible(false);
                jButton74.setVisible(false);
                jButton19.setVisible(false);
                jButton78.setVisible(false);
                jButton100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Lorry-icon.png")));
                jButton100.setVisible(true);
            } else if ((state==Order.ORDER_DONE) && (CurrentUser.getInt("LEVEL")!=DIRECTOR)) {
                jButton11.setVisible(false);
                jButton12.setVisible(false);
                jButton16.setVisible(false);
                jButton74.setVisible(false);
                jButton19.setVisible(false);
                jButton78.setVisible(false); 
                jButton100.setVisible(false);
            }
           
            MakeTableOfDeliveryOrder();
            
            GetSubOrders();
            MakeTableOfSubOrders();
            
            double Summ = 0;
            for (int i=0;i<SubOrders.getLength();i++) {
                Summ+=SubOrders.getDouble(i,"COST")*SubOrders.getInt(i,"AMOUNT");
            }
            Summ -= ExtendedOrder.getDouble("DISCOUNT");
            jLabel104.setText(FormatUAH.format(ExtendedOrder.getDouble("DELIVERY_COST") 
                    + Summ  - ExtendedOrder.getDouble("PREPAY")));
            
            jLabel114.setText(FormatUAH.format(ExtendedOrder.getDouble("DELIVERY_COST")));
            
            Object user_creator = ExtendedOrder.get("USER_CREATOR_NAME");
            jTextArea12.setText(user_creator==null ? "неизвестен" : (String)user_creator);
        }
    }
    
    public void setDeliveryCostLabelText(String s) {
        jLabel104.setText(s);
    }
    
    public boolean GetSubOrders() {
        Object[][] obj = db.SelectSQL("SELECT suborder.id,suborder.id_gift,gift.name,"
                + "gift.cost_packing,suborder.id_packing,packing.name,packing.number,"
                + "suborder.amount,suborder.cost,suborder.self_cost,suborder.packed,"
                + "packing.weight FROM suborder,gift,packing WHERE suborder.id_orders=?"
                + " AND suborder.id_gift=gift.id AND suborder.id_packing=packing.id "
                + "ORDER BY suborder.id",new Object[]{Orders.getInt("ID")});
        SubOrders.set(obj);
        return obj!=null;
    }
    
    public boolean GetOrdersCount(int row) {
        Object[][] obj = db.SelectSQL("SELECT amount,cost FROM suborder WHERE id_orders=?",new Object[]{Orders.getInt(row, "ID")});
        OrdersCount.set(obj);
        return obj!=null;
    }
    
    public void MakeTableOfSubOrders() {
        Object data[][] = new Object[0][6];
        if (!SubOrders.IsNull()) {
            data = new Object[SubOrders.getLength()+1][6];
            double Summ_cost = 0;
            double Summ_self_cost = 0;
            int Summ_amount = 0;
            for (int i = 0; i < SubOrders.getLength(); i++) {
                data[i][0] = SubOrders.getString(i,"GIFT_NAME");
                data[i][1] = "№"+SubOrders.getInt(i,"PACKING_NUMBER")+" "+SubOrders.getString(i,"PACKING_NAME");
                double self_cost = SubOrders.getDouble(i,"SELF_COST");
                int amount = SubOrders.getInt(i,"AMOUNT");                
                Summ_self_cost+=self_cost*amount;
                data[i][2] = FormatUAH.format(self_cost);
                double cost = SubOrders.getDouble(i,"COST");
                data[i][3] = FormatUAH.format(cost);
                data[i][4] = amount;
                Summ_amount+=amount;
                Summ_cost+=cost*amount;
                data[i][5] = FormatUAH.format(cost*amount);
            }
            data[SubOrders.getLength()][1] = "<html><font color='#C80000'>Сумма:</font><html>";
            data[SubOrders.getLength()][2] = FormatUAH.format(Summ_self_cost);
            data[SubOrders.getLength()][4] = Summ_amount;
            data[SubOrders.getLength()][5] = FormatUAH.format(Summ_cost);
        }
        int state = Orders.getInt("STATE");
        boolean editable = (state==Order.ORDER_PREPARE) && (CanEditOrders);
        jTable5.makeTable(data, this, editable, userLevel);
    }
    
    private void MakeTableOfDeliveryOrder() {
        DeliveryOrder.set(db.SelectSQL("SELECT id,date_time,date_delivery_country,date_send,date_close,contact,who_pays,delivery_type,address,content,present,comm,debt,state,(SELECT type_pay FROM orders WHERE id=?) FROM delivery WHERE id_orders=?",new Object[]{Orders.getInt("ID"),Orders.getInt("ID")}));
        Object data[][] = new Object[0][7];
        if (!DeliveryOrder.IsNull()) {
            data = new Object[DeliveryOrder.getLength()][10];
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            for (int i = 0; i < DeliveryOrder.getLength(); i++) {
                int date_time = DeliveryOrder.getInt(i, "DATE_TIME");
                int date_send = DeliveryOrder.getInt(i, "DATE_SEND");
                int date_close = DeliveryOrder.getInt(i, "DATE_CLOSE");
                
                data[i][0] = DeliveryOrder.getInt(i,"STATE");
                data[i][1] = "доставка: "+(date_time==0 ? "" : sdf.format(new Date(date_time*1000L)))+"\nотослано: "+(date_send==0? "" : sdf.format(new Date(date_send*1000L)))+"\nзакрыто: "+(date_close==0 ? "" : sdf.format(new Date(date_close*1000L)));
                data[i][2] = DeliveryOrder.getString(i,"CONTACT");
                data[i][6] = DeliveryOrder.getString(i, "WHO_PAYS");
                data[i][3] = DeliveryOrder.getString(i,"DELIVERY_TYPE");
                data[i][4] = DeliveryOrder.getString(i,"ADDRESS");
                data[i][5] = DeliveryOrder.getString(i,"CONTENT");
                int payType = DeliveryOrder.getInt(i,"TYPE_PAY_ORDER"); //2 и 3 и 5 - безнал
                data[i][7] = FormatUAH.format(DeliveryOrder.getDouble(i,"DEBT"))+(payType==2 || payType==3 ? " БН" : "");
                data[i][9] = DeliveryOrder.getString(i,"COMM");
                data[i][8] = DeliveryOrder.getString(i,"PRESENT");
            }
        } 
        jTable14.makeTable(data);
    }
    
    private void MakeTreeOfCandiesInStorage() {
        int[] SaveCurrentRow = jTree10.getSelectionRows();

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(new Object[]{"",0});
        DefaultMutableTreeNode child1 = null;
        DefaultMutableTreeNode child2;
        int last_factory = 0;
        int id_factory;

        for (int i = 0; i < Candies.getLength(); i++) {
            id_factory = Candies.getInt(i, "ID_FACTORY");
            if (id_factory != last_factory) {
                Object obj[] = new Object[]{Candies.getString(i, "FACTORY_NAME"), i};
                child1 = new DefaultMutableTreeNode(obj);
                top.add(child1);
                last_factory = id_factory;
            }
            if (Candies.getInt(i, "ID") != 0) {
                String name = Candies.getString(i, "CANDY_NAME");
                int storage = Candies.getInt(i, "STORAGE");
                int reserved = Candies.getInt(i, "RESERVED");
                double reserved_boxes = reserved*1.0/Candies.getInt(i,"AMOUNT_IN_BOX");
                double storage_boxes = storage*1.0/Candies.getInt(i,"AMOUNT_IN_BOX");
                double free = storage_boxes-reserved_boxes;
                boolean IsUsed = Candies.getInt(i,"ISUSED")!=0;
                Object obj[] = new Object[]{name, i, FormatUAH.format(storage_boxes), FormatUAH.format(reserved_boxes), IsUsed, reserved_boxes>storage_boxes, FormatUAH.format(free)};
                child2 = new DefaultMutableTreeNode(obj);
                child1.add(child2);
            }
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(top);
        jTree10.setModel(treeModel);
        for (int i = 0; i <= jTree10.getRowCount() - 1; i++) {
            jTree10.expandRow(i);
        }

        jTree10.setSelectionRows(SaveCurrentRow);
        SelectNodeOfTreeInStorage(jTree10,Candies,jTable6,true);
        MakeTableOfStorageCandyReserv();
    }    
    
    public void updateTableOrdersAndSelectLastAddedOrder() {
        GetOrders();
        MakeTableOfOrders();
        int[] mas = new int[Orders.getLength()];
        for (int i = 0; i < Orders.getLength(); i++) {
            mas[i] = Orders.getInt(i, "ID");
        }
        Arrays.sort(mas);
        int max = mas[mas.length - 1];
        for (int i = 0; i < Orders.getLength(); i++) {
            if (Orders.getInt(i, "ID") == max) {
                jTable3.setRowSelectionInterval(i, i);
                Rectangle rect = jTable3.getCellRect(i, 0, true);
                ((JScrollPane) jTable3.getParent().getParent()).getVerticalScrollBar().setValue(rect.y);
                SelectNodeOfTableOrders();
                break;
            }
        }
    }
    
    private void MakeTableOfStoragePackingReserv() {
        Object data[][] = new Object[0][3];
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree11.getLastSelectedPathComponent();
        if ((SelectedNode!=null) && (SelectedNode.getLevel()==2)) {
            int index = (Integer)((Object[])SelectedNode.getUserObject())[1];
            ObjectResultSet StoragePackingReserv = new ObjectResultSet();
            StoragePackingReserv.setColumnNames(new String[]{"AMOUNT","CLIENT","MANAGER"});
            StoragePackingReserv.set(db.SelectSQL("SELECT SUM(suborder.amount-suborder.packed),client.official_name,user.name FROM suborder,client,orders,user WHERE suborder.id_packing=? AND suborder.id_orders=orders.id AND (orders.state=1 OR orders.state=2) AND orders.id_client=client.id AND user.id=orders.user_creator_id GROUP BY client.id",new Object[]{FilteredPackings.getInt(index,"ID")}));
            if (!StoragePackingReserv.IsNull()) {
                int sum = 0;
                data = new Object[StoragePackingReserv.getLength()+1][3];
                for (int i=0;i<StoragePackingReserv.getLength();i++) {
                    sum+=StoragePackingReserv.getBigDecimalAsLong(i,"AMOUNT");
                    data[i][0] = StoragePackingReserv.getBigDecimalAsLong(i,"AMOUNT");
                    data[i][1] = StoragePackingReserv.getString(i,"CLIENT");
                    data[i][2] = StoragePackingReserv.getString(i,"MANAGER");
                    
                }
                data[StoragePackingReserv.getLength()][0] = FormatUAH.format(sum);
                data[StoragePackingReserv.getLength()][1] = "<html><font color='red'>ИТОГО</font></html>";
            }
        }
        jTable16.makeTable(data);            
    }
    
    private void MakeTableOfStorageOrderPref() {
        int SelectedRow = jTable22.getSelectedRow();
        
        String packingRecord = (String) jTable22.getValueAt(SelectedRow, 1);
        String[] packingRecordArr = packingRecord.split(" ");
        String packingNumber = packingRecordArr[0].substring(1);
        String packingName = "";
        for (int i=1;i<packingRecordArr.length;i++) {
            if (i!=1) {
                packingName += " ";
            }
            packingName += packingRecordArr[i];
        }

        System.out.println("P.name :_" + packingName + "_");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Object data[][] = new Object[0][4];
        ObjectResultSet StorageOrderPref = new ObjectResultSet();
        StorageOrderPref.setColumnNames(new String[]{"CLIENT","AMOUNT","DATE","MANAGER"});
        StorageOrderPref.set(db.SelectSQL("SELECT DISTINCT client.official_name, S.amount, D.date_time, U.name \n" +
                "FROM user U, client, delivery D, orders O, suborder S, packing P \n" +
                "WHERE P.number = ? AND P.name = ? AND S.id_packing = P.id AND O.id = S.id_orders AND O.state < 3\n" +
                "AND O.state > 0 AND D.id_orders = O.id AND client.id = O.id_client AND U.id = O.user_creator_id",new Object[]{packingNumber, packingName}));
        if (!StorageOrderPref.IsNull()) {
            data = new Object[StorageOrderPref.getLength()][4];
            for (int i=0;i<StorageOrderPref.getLength();i++) {
                    
                data[i][0] = StorageOrderPref.getString(i,"CLIENT");
                data[i][1] = StorageOrderPref.getInt(i,"AMOUNT");
                data[i][2] = sdf.format(new Date(StorageOrderPref.getInt(i,"DATE")*1000L));
                data[i][3] = StorageOrderPref.getString(i,"MANAGER");  
            }
        }

        jTable11.makeTable(data);            
    }
    
    private void MakeTableOfStorageCandyReserv() {
        Object data[][] = new Object[0][2];
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree10.getLastSelectedPathComponent();
        if ((SelectedNode!=null) && (SelectedNode.getLevel()==2)) {
            int index = (Integer)((Object[])SelectedNode.getUserObject())[1];
            ObjectResultSet StorageCandyReserv = new ObjectResultSet();
            StorageCandyReserv.setColumnNames(new String[]{"AMOUNT","AMOUNT_IN_BOX","CLIENT"});
            StorageCandyReserv.set(db.SelectSQL("SELECT sum(suborder.amount - suborder.packed)*gift_candy.amount,candy.AMOUNT_IN_BOX, client.official_name FROM suborder, client, orders, gift_candy,candy WHERE suborder.id_gift = gift_candy.ID_GIFT AND gift_candy.ID_CANDY=candy.id AND candy.ID=? AND suborder.id_orders = orders.id AND orders.state = 2 AND orders.id_client = client.id GROUP BY client.id",new Object[]{Candies.getInt(index,"ID")}));
            if (!StorageCandyReserv.IsNull()) {
                data = new Object[StorageCandyReserv.getLength()+1][2];
                double sum = 0;
                for (int i=0;i<StorageCandyReserv.getLength();i++) {
                    double summ = StorageCandyReserv.getBigDecimalAsLong(i,"AMOUNT")*1.0/StorageCandyReserv.getInt(i,"AMOUNT_IN_BOX");
                    sum+=summ;
                    data[i][0] = FormatUAH.format(summ);
                    data[i][1] = StorageCandyReserv.getString(i,"CLIENT");
                }
                data[StorageCandyReserv.getLength()][0] = FormatUAH.format(sum);
                data[StorageCandyReserv.getLength()][1] = "<html><font color='red'>ИТОГО</font></html>";
            }
        }
        jTable17.makeTable(data);            
    }    
    
    private void MakeTreeOfPackingsInStorage() {
        int[] SaveCurrentRow = jTree11.getSelectionRows();

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
        
        for (int i = 0; i < FilteredPackings.getLength(); i++) {
            int type = FilteredPackings.getInt(i, "TYPE");
            String filename = FilteredPackings.getString(i, "FILENAME").trim();
            if (!PackingsImages.existsImageWithName(filename)) {
                if ((filename != null) && (!"".equals(filename))) {
                    try {
                        if (!thereWereProblemsWithFTP) {
                            Image image = FTP.readImageFromFileOrDownloadFromFTP(filename);
                            PackingsImages.addImage(image, filename);                    
                        }
                    } catch (Exception ex) {
                        thereWereProblemsWithFTP = true;
                        //JOptionPane.showMessageDialog(null, "Проблемы с загрузкой изображений с сервера:\n" + ex.getMessage());
                        //break;
                    }
                }
            }
            
            int storage = FilteredPackings.getInt(i, "STORAGE");
            int reserved = FilteredPackings.getInt(i, "RESERVED");
            DefaultMutableTreeNode dftn = new DefaultMutableTreeNode(new Object[]{"№"+Integer.toString(FilteredPackings.getInt(i, "NUMBER"))+"  "+FilteredPackings.getString(i, "NAME"), i,PackingsImages.getImage(filename),storage,reserved,FilteredPackings.getBoolean(i, "MARKED")});
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
        jTree11.setModel(treeModel);
        for (int i = 0; i <= jTree11.getRowCount() - 1; i++) {
            jTree11.expandRow(i);
        }
        jTree11.setSelectionRows(SaveCurrentRow);
        SelectNodeOfTreeInStorage(jTree11,FilteredPackings,jTable8,false);
        MakeTableOfStoragePackingReserv();
        
        if (thereWereProblemsWithFTP) {
            JOptionPane.showMessageDialog(null, "Проблемы с загрузкой изображений с сервера");
        }
   }

   private void GetStorage(int type, int id_object) {
       if (jRadioButton1.isSelected()) {
           Storage.set(db.SelectSQL("(SELECT storage_factura.date_time AS DT, storage_factura.amount, user.name, '', storage_factura.factura FROM storage_factura, user WHERE storage_factura.type = ? AND id_object = ? AND user.id = storage_factura.id_user ORDER BY DT DESC LIMIT 100) "+
                                " UNION (SELECT storage_orders.date_time AS DT, storage_orders.amount, '', client.official_name, '' FROM storage_orders, orders, client WHERE storage_orders.type = ? AND id_object = ? AND storage_orders.id_orders = orders.ID AND orders.ID_CLIENT=client.id ORDER BY DT DESC LIMIT 100) ORDER BY DT DESC",new Object[]{type, id_object, type, id_object}));
       } else {
           Calendar cal = (Calendar) jDateChooser3.getCalendar().clone();
           cal.set(Calendar.SECOND, 0);
           cal.set(Calendar.MINUTE, 0);
           cal.set(Calendar.HOUR_OF_DAY, 0);
           int Start = ((Long) (cal.getTimeInMillis() / 1000)).intValue();
           cal = (Calendar) jDateChooser4.getCalendar().clone();
           cal.set(Calendar.SECOND, 0);
           cal.set(Calendar.MINUTE, 0);
           cal.set(Calendar.HOUR_OF_DAY, 0);
           cal.add(Calendar.DAY_OF_MONTH, 1);
           int End = ((Long) (cal.getTimeInMillis() / 1000)).intValue();
           Storage.set(db.SelectSQL("(SELECT storage_factura.date_time AS DT, storage_factura.amount, user.name, '', storage_factura.factura FROM storage_factura, user WHERE storage_factura.type = ? AND id_object = ? AND user.id = storage_factura.id_user AND storage_factura.date_time BETWEEN ? AND ? ORDER BY DT DESC LIMIT 100) "+
                                " UNION (SELECT storage_orders.date_time AS DT, storage_orders.amount, '', client.official_name, '' FROM storage_orders, orders, client WHERE storage_orders.type = ? AND id_object = ? AND storage_orders.id_orders = orders.ID AND orders.ID_CLIENT=client.id AND storage_orders.date_time BETWEEN ? AND ? ORDER BY DT DESC LIMIT 100) ORDER BY DT DESC",new Object[]{type, id_object, Start,End, type,id_object,Start,End}));
       }
   }
   
   private void SelectNodeOfTreeInStorage(JTree tree, ObjectResultSet ors, TableStorage table, boolean IsCandy) {
        Object[] obj;
        int index;
        Object data[][] = new Object[0][5];        
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if ((SelectedNode == null) || (SelectedNode.getLevel()==1)) {
            jButton72.setVisible(false);
            jButton52.setVisible(false);
        } else {
            jButton72.setVisible(CanEditStorage);
            jButton52.setVisible(CanEditStorage);
            obj = (Object[]) SelectedNode.getUserObject();
            index = (Integer) obj[1];
            if (SelectedNode.getLevel()==1) {
                
            } else {
                GetStorage((IsCandy ? 1 : 2),ors.getInt(index, "ID"));
                if (!Storage.IsNull()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                    data = new Object[Storage.getLength()+1][5];
                    double sum = 0;
                    double summ;
                    for (int i = 0; i < Storage.getLength(); i++) {
                        data[i][0] = sdf.format(new Date(Storage.getInt(i,"DATE_TIME")*1000L));
                        int amount = Storage.getInt(i,"AMOUNT");
                        String color = amount<0 ? "<html><font color='blue'>" : "<html><font color='#C80000'>";
                        if (IsCandy) {
                            summ = amount*1.0/Candies.getInt(index,"AMOUNT_IN_BOX");
                            sum += summ;
                            data[i][1] = color + FormatUAH.format(summ)+"</font></html>";
                        } else {
                            data[i][1] = color + Integer.toString(amount)+"</font></html>";
                            sum += amount;
                        }
                        data[i][2] = Storage.getString(i,"USER");
                        data[i][3] = Storage.getString(i,"FACTURA");
                        data[i][4] = Storage.getString(i,"CLIENT");
                    }
                    data[Storage.getLength()][0] = "<html><font color='red'>ИТОГО:</font></html>";
                    data[Storage.getLength()][1] = FormatUAH.format(sum);
                }
            }
        }
        table.makeTable(data);            
   }
   
    private boolean DoTransactionInStorage(int id, int type, double value, String number_factura, String tablename) {
        int date_time = ((Long) (Calendar.getInstance().getTimeInMillis() / 1000)).intValue();
        int id_user = CurrentUser.getInt("ID");
        boolean result = db.DoSQL("START TRANSACTION");
        if (result) {
            result = db.UpdateSQL("INSERT INTO storage_factura(type,id_object,date_time,amount,id_user,factura) VALUES(?,?,?,?,?,?)", new Object[]{type, id, date_time, value, id_user, number_factura});
            if (result) {
                result = db.UpdateSQL("UPDATE " + tablename + " SET storage=storage+? WHERE id=?", new Object[]{value, id});
            }
        }
        if (result) {
            db.DoSQL("COMMIT");
        } else {
            db.DoSQL("ROLLBACK");
        }
        return result;
    }   
   
    private void GetConstants() {
        Constants.set(db.SelectSQL("SELECT * FROM constants", null));
    }
    
    private void MakePanelOfConstants() {
        JTextField[] mas = new JTextField[]{jTextField23,jTextField40,jTextField42,jTextField44};
        for (JTextField ma : mas) {
            ma.setEditable(false);
            ma.setBorder(null);
            ma.setBackground(ma.getParent().getBackground());
            ma.setForeground(new Color(200,0,0));
        }     
        jTextField23.setText(FormatUAH.format(Constants.getDouble("STICK_COST")));
        jTextField40.setText(FormatUAH.format(Constants.getDouble("COST_BOX_FOR_1_GIFT")));
        jTextField42.setText(Constants.getString("FTPaddress"));
        jTextField44.setText(Constants.getString("FTPpass"));
        
        jButton56.setVisible(true);
        jButton57.setVisible(false);
        jButton58.setVisible(false);
    }
 
    private void MakeTableOfOrderCandyForStorage() {
        Object data[][] = new Object[0][6];
        if (!Candies.IsNull()) {

            ArrayList vec = new ArrayList();
            int last_factory = Candies.getInt(0, "ID_FACTORY");
            int id_factory;
            double Summ_weight_factory = 0;
            double Summ_cost_factory = 0;
            double Summ_weight = 0;
            double Summ_cost = 0;
            double discount = Candies.getLength()>0 ? Candies.getDouble(0,"DISCOUNT") : 0;
            
            for (int i = 0; i < Candies.getLength(); i++) {
                id_factory = Candies.getInt(i, "ID_FACTORY");
                if (id_factory != last_factory) {
                    if (Summ_cost_factory>0) {
                        last_factory = id_factory;
                        Summ_weight+=Summ_weight_factory;
                        Summ_cost+=Summ_cost_factory*(100-discount)/100;
                        vec.add(new String[]{"<html><font color='#C80000'><b>ИТОГО:</b></font></html>","","","<html><font color='#C80000'><b>"+FormatKG.format(Summ_weight_factory)+"</b></font><html>","","<html><font color='#C80000'><b>"+FormatUAH.format(Summ_cost_factory)+"</b></font><html>"});
                        vec.add(new String[]{"","","","","<html><font color='#C80000'><b>СКИДКА,%</b></font></html>","<html><font color='#C80000'><b>"+FormatKG.format(discount)+"</b></font><html>"});
                        vec.add(new String[]{"","","","","","<html><font color='#C80000'><b>"+FormatUAH.format(Summ_cost_factory*(100-discount)/100)+"</b></font><html>"});
                        vec.add(new String[]{"","","","","",""});           
                        discount = Candies.getDouble(i,"DISCOUNT");                                        
                        Summ_weight_factory = 0;
                        Summ_cost_factory = 0;
                    } else {
                        last_factory = id_factory;                        
                    }
                }
                if (Candies.getInt(i, "ID") != 0) {
                    int storage = Candies.getInt(i, "STORAGE");
                    int reserved = Candies.getInt(i, "RESERVED");
                    double amount_boxes = (storage-reserved)*1.0/Candies.getInt(i,"AMOUNT_IN_BOX");
                    if (amount_boxes<0) {
                        String[] obj = new String[6];
                        int amount = ((Double)(-Math.floor(amount_boxes))).intValue();
                        obj[0] = Candies.getString(i, "FACTORY_NAME");
                        obj[1] = Candies.getString(i, "CANDY_NAME");
                        obj[2] = Integer.toString(amount);
                        double weight = Candies.getDouble(i,"BOX_WEIGHT")*amount;
                        obj[3] = FormatKG.format(weight);
                        obj[4] = FormatUAH.format(Candies.getDouble(i,"COST_KG"));
                        double cost = weight*Candies.getDouble(i,"COST_KG");
                        obj[5] = FormatUAH.format(cost);
                        Summ_weight_factory+=weight;
                        Summ_cost_factory+=cost;
                        vec.add(obj);
                    }
                }
            }
            Summ_weight+=Summ_weight_factory;
            Summ_cost+=Summ_cost_factory*(100-discount)/100;
            vec.add(new String[]{"<html><font color='#C80000'><b>ИТОГО:</b></font></html>","","","<html><font color='#C80000'><b>"+FormatKG.format(Summ_weight_factory)+"</b></font><html>","","<html><font color='#C80000'><b>"+FormatUAH.format(Summ_cost_factory)+"</b></font><html>"});
            vec.add(new String[]{"","","","","<html><font color='#C80000'><b>СКИДКА,%</b></font></html>","<html><font color='#C80000'><b>"+FormatKG.format(discount)+"</b></font><html>"});
            vec.add(new String[]{"","","","","","<html><font color='#C80000'><b>"+FormatUAH.format(Summ_cost_factory*(100-discount)/100)+"</b></font><html>"});
            vec.add(new String[]{"","","","","",""});            
            vec.add(new String[]{"<html><font color='#C80000'><b>ИТОГО:</b></font></html>","","","<html><font color='#C80000'><b>"+FormatKG.format(Summ_weight)+"</b></font><html>","","<html><font color='#C80000'><b>"+FormatUAH.format(Summ_cost)+"</b></font><html>"});            
            
            data = new Object[vec.size()][6];
            for (int i=0;i<vec.size();i++) {
                System.arraycopy((String[])vec.get(i), 0, data[i], 0, ((String[])vec.get(i)).length);
            }
        }
        jTable9.makeTable(data);                    
    }
    
    private void MakeTableOfOrderPackingForStorage() {
        Object data[][] = new Object[0][6];
        
        boolean thereWereProblemsWithFTP = false;
            
        if (!Packings.IsNull()) {

            data = new Object[Packings.getLength()+1][6];
            double allSum = 0;
            int count = 0;
            
            for (int i = 0;i<Packings.getLength();i++) {
                int storage = Packings.getInt(i, "STORAGE");
                int reserved = Packings.getInt(i, "RESERVED");
                if (storage-reserved<0) { 
                    String fileName = Packings.getString(i,"FILENAME").trim();
                    if (!PackingsImages.existsImageWithName(fileName)) {
                        if ((fileName != null) && (!"".equals(fileName))) {
                            try {
                                if (!thereWereProblemsWithFTP) {
                                    Image image = FTP.readImageFromFileOrDownloadFromFTP(fileName);
                                    if (image != null) {
                                        PackingsImages.addImage(image, fileName);
                                    }
                                }
                            } catch (Exception ex) {
                                thereWereProblemsWithFTP = true;
                            }
                        }
                    }                    
                    data[count][0] = PackingsImages.getImage(fileName);
                    data[count][1] = "№"+Packings.getInt(i,"NUMBER")+" "+Packings.getString(i,"NAME");
                    data[count][2] = -(storage-reserved);
                    data[count][3] = FormatUAH.format(Packings.getDouble(i,"COST"));
                    double sum = Packings.getDouble(i,"COST")*(-(storage-reserved));
                    data[count][4] = FormatUAH.format(sum);
                    data[count][5] = i;
                    allSum+=sum;
                    count++;
                }
            }
            data[count][0] = null;
            data[count][1] = "";
            data[count][2] = "";
            data[count][3] = "<html><font color='#C80000'><b>ИТОГО:</b></font></html>";
            data[count][4] = FormatUAH.format(allSum);
            data[count][5] = -1;
            data = Arrays.copyOf(data, count+1);
        }
        jTable22.makeTable(data);        
        
        if (thereWereProblemsWithFTP) {
            JOptionPane.showMessageDialog(null, "Проблемы с загрузкой изображений с сервера");
        }
        
    }    
    
    public void GetDelivery() {
        int startTime = 0;
        int endTime = 0;
        
        if (jRadioButton3.isSelected()) {
            int year = (int)jComboBox5.getSelectedItem();
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0, 1, 0, 0, 0);
            startTime = (int)(cal.getTimeInMillis()/1000);
            cal.set(year+1, 0, 1, 0, 0, 0);
            endTime = (int)(cal.getTimeInMillis()/1000);
        } else if (jRadioButton4.isSelected()) {
            Calendar cal = (Calendar)jDateChooser7.getCalendar().clone();
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            startTime = (int)(cal.getTimeInMillis()/1000); 
            endTime = (int)(cal.getTimeInMillis()/1000+24*3600-1);
        }
        
        String filterState = jComboBox3.getSelectedIndex()==0 ? "" : " AND D.state="+(jComboBox3.getSelectedIndex()-1);
        String filterManager = jComboBox7.getSelectedIndex()==0 ? "" : " AND orders.user_creator_id="+jComboBox7.getSelectedID();
        String filterOrderNumber = jTextField49.getText().isEmpty() ? "" : " AND orders.number LIKE '"+jTextField49.getText()+"%'";
        String filterExpeditors = jComboBox14.getSelectedIndex()==0 ? "" : " AND D.id_expeditors="+jComboBox14.getSelectedID();
        String filterEdrpou = jTextField59.getText().isEmpty() ? "" : " AND client.EDRPOU LIKE '%"+jTextField59.getText()+"%'";
        String filterClientName = jTextField51.getText().isEmpty() ? "" : " AND client.official_name LIKE '%"+jTextField51.getText()+"%'";
        String l = (String) jComboBox16.getItemAt(jComboBox16.getSelectedIndex());
        String filterDType = jComboBox16.getSelectedIndex()==0 ? "" : " AND D.delivery_type='"+l+"'";
        String filterTypePay = jComboBox17.getSelectedIndex()<=0 ? "" : " AND orders.type_pay = '" + (jComboBox17.getSelectedIndex()-1) + "'";
        String filterTTN = jTextField60.getText().isEmpty() ? "" : " AND D.TTN LIKE '%"+jTextField60.getText()+"%'";
        
        Delivery.set(db.SelectSQL("SELECT D.id,D.date_time,D.date_send,D.date_close,"
                + "D.ttn,D.contact,D.who_pays,D.delivery_type,D.address,D.content,"
                + "D.present,D.comm,D.state,client.official_name,D.id_orders,D.debt,"
                + "(SELECT name FROM user WHERE D.id_orders=orders.id "
                + "AND orders.user_creator_id=user.id),orders.type_pay,(SELECT name "
                + "FROM expeditors WHERE id=D.id_expeditors) FROM delivery D,orders,client "
                + "WHERE D.id_orders=orders.id AND orders.id_client=client.id"
                +filterState+filterManager+filterOrderNumber+filterExpeditors
                +filterDType+filterClientName+filterTypePay+filterEdrpou+filterTTN
                +" AND D.date_time BETWEEN ? AND ? ORDER BY client.official_name",new Object[]{startTime,endTime}));            
    }
    
    public int getDeliveryTableSelectedRow() {
        return jTable12.getSelectedRow();
    }
    
    public void MakeTableOfDelivery() {
        Object data[][] = new Object[0][15];
        if (!Delivery.IsNull()) {
            data = new Object[Delivery.getLength()][15];
            for (int i = 0; i < Delivery.getLength(); i++) {
                data[i][0] = i+1;
                data[i][1] = Delivery.getInt(i,"STATE");
                data[i][2] = " "+new SimpleDateFormat("dd-MMM-yyyy").format(new Date(Delivery.getInt(i,"DATE_TIME")*1000L));
                data[i][3] = " "+Delivery.get(i,"EXPEDITORS_NAME")==null ? "" : Delivery.getString(i,"EXPEDITORS_NAME");
                data[i][4] = " "+Delivery.getString(i,"TTN");
                data[i][5] = " "+Delivery.getString(i,"CLIENT");
                data[i][6] = " "+Delivery.getString(i,"CONTACT");
                data[i][7] = " "+Delivery.getString(i,"DELIVERY_TYPE");
                data[i][8] = " "+Delivery.getString(i,"ADDRESS");
                data[i][9] = " "+Delivery.getString(i,"CONTENT");
                data[i][10] = " "+Delivery.getString(i,"WHO_PAYS");
                int payType = Delivery.getInt(i,"TYPE_PAY_ORDER"); //2 и 3 и 5 - безнал
                data[i][11] = " "+FormatUAH.format(Delivery.getDouble(i,"DEBT"))+(payType==2 || payType==3 ? " БН" : "");
                data[i][12] = " "+Delivery.getString(i,"PRESENT");
                data[i][13] = " "+Delivery.getString(i,"COMM");
                data[i][14] = " "+Delivery.get(i,"USER_CREATOR_NAME")==null ? "" : Delivery.getString(i,"USER_CREATOR_NAME");
            }
        }
        jTable12.makeTable(data);
        SelectNodeOfTableDelivery();
    }
    
    CellEditorListener ChangeNotification = new CellEditorListener() {
        @Override
        public void editingCanceled(ChangeEvent e) {}

        @Override
        public void editingStopped(ChangeEvent e) {
            int row = jTable12.getSelectedRow();
            String ttn = (String) jTable12.getValueAt(row, 4);
            String commentary = (String) jTable12.getValueAt(row, 13);
            int id = Delivery.getInt(row, "ID");
            boolean result = db.DoSQL("START TRANSACTION");
            if (result) {
                result = db.UpdateSQL("UPDATE delivery SET TTN=?, COMM=? WHERE id=?", new Object[]{ttn, commentary, id});
            }
            if (result) {
                db.DoSQL("COMMIT");
                GetOrders();
                MakeTableOfOrders();
            } else {
                db.DoSQL("ROLLBACK");
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }

        }
    };
    
//    CellEditorListener EditConditers = new CellEditorListener() {
//        @Override
//        public void editingCanceled(ChangeEvent e) {}
//
//        @Override
//        public void editingStopped(ChangeEvent e) {
//            int row = jTable9.getSelectedRow();
//            String ttn = (String) jTable9.getValueAt(row, 2);
//            int id = Delivery.getInt(row, "ID");
//            boolean result = db.DoSQL("START TRANSACTION");
//            if (result) {
//                result = db.UpdateSQL("UPDATE delivery SET TTN=? WHERE id=?", new Object[]{ttn, id});
//            }
//            if (result) {
//                db.DoSQL("COMMIT");
//                GetOrders();
//                MakeTableOfOrders();
//            } else {
//                db.DoSQL("ROLLBACK");
//                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
//            }
//
//        }
//    };
    
    private void SelectNodeOfTableDelivery() {
        if (jTable12.getSelectedRow()==-1) {
            jButton82.setVisible(false);
            Delivery.setPosition(-1);
        } else {
            Delivery.setPosition(jTable12.getSelectedRow());
            switch (Delivery.getInt("STATE")) {
                case Order.ORDER_PREPARE:
                case Order.ORDER_PAY:
                case Order.ORDER_PACK:
                    jButton82.setVisible(false);
                    break;
                case Order.ORDER_STORAGE:
                    jButton82.setVisible(true);
                    jButton82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Lorry-icon.png")));
                    break;
                case Order.ORDER_SEND:
                    jButton82.setVisible(CurrentUser.getInt("LEVEL")==DIRECTOR);
                    jButton82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png")));
                    break;
//                case Order.ORDER_DELIVERED:
                case Order.ORDER_DONE:
                    jButton82.setVisible(false);
                    break;
            }
//            && Delivery.getInt("STATE")!=Order.ORDER_DELIVERED
            if (CanEditExpeditors && expeditorsDialog.isVisible() && Delivery.getInt("STATE")!=Order.ORDER_DONE) {
                expeditorsDialog.addRow(jTable12.getSelectedRow());
            }
        }
    }
    
    private void RefreshNodeCandies() {
        Object[][] obj = db.SelectSQL("SELECT candy.id as candy_id, candy.name as candy_name, candy.id_factory AS id_factory, candy.box_weight, candy.amount_in_box, candy.cost_kg, null,candy.cost_kg*candy.box_weight/candy.amount_in_box as candy_cost,candy.storage,candy.reserved,candy.comm,candy.last_change_cost,(SELECT id_candy FROM gift_candy WHERE id_candy=candy_id LIMIT 1),null FROM candy WHERE candy.id=? LIMIT 1",new Object[]{Candies.getInt("ID")});
        Candies.refreshValue(Candies.getPosition(),obj[0]);
        boolean IsUsed = Candies.getInt("ISUSED") !=0;
        ((DefaultMutableTreeNode)jTree1.getLastSelectedPathComponent()).setUserObject(new Object[]{Candies.getString("CANDY_NAME"), Candies.getPosition(), IsUsed});
        jTree1.repaint();
        SelectNodeOfTreeCandies();
    }
    
    private void RefreshNodeCandiesInStorage() {
        Object[] user_obj = (Object[])((DefaultMutableTreeNode)jTree10.getLastSelectedPathComponent()).getUserObject();
        int position = (Integer)user_obj[1];
        Object[][] obj = db.SelectSQL("SELECT candy.id as candy_id, candy.name as candy_name, candy.id_factory AS id_factory, candy.box_weight, candy.amount_in_box, candy.cost_kg, null,candy.cost_kg*candy.box_weight/candy.amount_in_box as candy_cost,candy.storage,candy.reserved,candy.comm,candy.last_change_cost,(SELECT id_candy FROM gift_candy WHERE id_candy=candy_id LIMIT 1),null FROM candy WHERE candy.id=? LIMIT 1",new Object[]{Candies.getInt(position,"ID")});
        Candies.refreshValue(position,obj[0]);
        String name = Candies.getString(position,"CANDY_NAME");
        int storage = Candies.getInt(position,"STORAGE");
        int reserved = Candies.getInt(position,"RESERVED");
        double reserved_boxes = reserved*1.0/Candies.getInt(position,"AMOUNT_IN_BOX");
        double storage_boxes = storage*1.0/Candies.getInt(position,"AMOUNT_IN_BOX");
        double free = storage_boxes-reserved_boxes;
        boolean IsUsed = Candies.getInt(position,"ISUSED")!=0;
        ((DefaultMutableTreeNode)jTree10.getLastSelectedPathComponent()).setUserObject(new Object[]{name, position, FormatUAH.format(storage_boxes), FormatUAH.format(reserved_boxes), IsUsed, reserved_boxes>storage_boxes, FormatUAH.format(free)});
        jTree10.repaint();
        SelectNodeOfTreeInStorage(jTree10,Candies,jTable6,true);
    }    
    
    private void RefreshNodePackings() {
        Object[][] obj = db.SelectSQL("SELECT ID,NAME,TYPE,NUMBER,CAPACITY,STORAGE,RESERVED,FILENAME,COST,MARKED FROM packing WHERE id=?",new Object[]{Packings.getInt("ID")});
        Packings.refreshValue(Packings.getPosition(),obj[0]);
        String filename = Packings.getString("FILENAME").trim();
        
        boolean thereWereProblemsWithFTP = false;
        
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
        ((DefaultMutableTreeNode)jTree6.getLastSelectedPathComponent()).setUserObject(new Object[]{"№"+Integer.toString(Packings.getInt("NUMBER"))+"  "+Packings.getString("NAME"), Packings.getPosition(),FormatKG.format(Packings.getDouble("CAPACITY"))+" кг",PackingsImages.getImage(filename),Packings.getBoolean("MARKED")});
        jTree6.repaint();
        SelectNodeOfTreePackings();
        
        if (thereWereProblemsWithFTP) {
         //   JOptionPane.showMessageDialog(null, "Проблемы с загрузкой изображений с сервера");            
        }
    }    
    
    private void RefreshNodePackingsInStorage() {
        Object[] user_obj = (Object[])((DefaultMutableTreeNode)jTree11.getLastSelectedPathComponent()).getUserObject();
        int position = (Integer)user_obj[1];
        Object[][] obj = db.SelectSQL("SELECT ID,NAME,TYPE,NUMBER,CAPACITY,STORAGE,RESERVED,FILENAME,COST,MARKED FROM packing WHERE id=?",new Object[]{FilteredPackings.getInt(position,"ID")});
        FilteredPackings.refreshValue(position,obj[0]);
        String filename = FilteredPackings.getString(position,"FILENAME").trim();
        
        boolean thereWereProblemsWithFTP = false;
        
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
        int storage = FilteredPackings.getInt(position,"STORAGE");
        int reserved = FilteredPackings.getInt(position,"RESERVED");
        ((DefaultMutableTreeNode)jTree11.getLastSelectedPathComponent()).setUserObject(new Object[]{"№"+Integer.toString(FilteredPackings.getInt(position,"NUMBER"))+"  "+FilteredPackings.getString(position,"NAME"), position,PackingsImages.getImage(filename),storage,reserved,FilteredPackings.getBoolean(position, "MARKED")});
        jTree11.repaint();
        SelectNodeOfTreeInStorage(jTree11,FilteredPackings,jTable8,false);
        MakeTableOfStoragePackingReserv();
        
        if (thereWereProblemsWithFTP) {
          //  JOptionPane.showMessageDialog(null, "Проблемы с загрузкой изображений с сервера");
        }
    }    
  
    private void GetFinances() {
        Calendar cal = (Calendar)jDateChooser8.getCalendar().clone();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        long StartTime = cal.getTimeInMillis();
        cal = (Calendar)jDateChooser9.getCalendar().clone();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        long EndTime = cal.getTimeInMillis();
        Finances.set(db.SelectSQL("SELECT t.DATE_TIME, t.delivery_cost, client.official_NAME, sum(suborder.AMOUNT * suborder.SELF_COST), sum(suborder.AMOUNT * suborder.COST), (SELECT sum(delivery.COST) FROM delivery WHERE delivery.ID_ORDERS = t.id), (SELECT sum(delivery.PAYMENT) "+
                "FROM delivery WHERE delivery.ID_ORDERS = t.id) , (SELECT sum(delivery.COURIER) FROM delivery WHERE delivery.ID_ORDERS = t.id) FROM client, orders t, suborder "+
                " WHERE t.ID=suborder.ID_ORDERS AND t.STATE = 5 AND t.ID_CLIENT = client.ID AND t.DATE_TIME BETWEEN ? AND ? GROUP BY t.ID ORDER BY t.DATE_TIME",new Object[]{StartTime,EndTime}));
    }

    private void MakeTableOfFinances() {
        Object data[][] = new Object[0][9];
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        if (!Finances.IsNull()) {
            data = new Object[Finances.getLength()+1][9];
            double cost;
            double self_cost;
            double delivery_cost;
            double profit;
            double delivery_transport_cost;
            double delivery_payment;
            double delivery_courier_cost;
            double summ_profit = 0;
            double summ_delivery_transport_cost = 0;
            double summ_delivery_payment = 0;
            double summ_delivery_cost = 0;
            double summ_delivery_courier_cost = 0;
            double summ_self_cost = 0;
            double summ_cost = 0;
            
            for (int i = 0; i < Finances.getLength(); i++) {
                self_cost = Finances.getDouble(i,"SELF_COST");                
                cost = Finances.getDouble(i,"COST");                
                delivery_payment = Finances.getDouble(i,"DELIVERY_PAYMENT");
                delivery_transport_cost = Finances.getDouble(i,"DELIVERY_TRANSPORT_COST");
                delivery_cost = Finances.getDouble(i,"DELIVERY_COST");
                delivery_courier_cost = Finances.getDouble(i,"DELIVERY_COURIER_COST");
                profit = cost-self_cost+(delivery_cost-delivery_payment-delivery_transport_cost-delivery_courier_cost);
                
                summ_delivery_payment+=delivery_payment;
                summ_delivery_transport_cost+=delivery_transport_cost;
                summ_delivery_cost+=delivery_cost;
                summ_delivery_courier_cost+=delivery_courier_cost;
                summ_profit+=profit;
                summ_self_cost +=-self_cost;
                summ_cost+=cost;
                
                data[i][0] = sdf.format(new Date(Finances.getLong(i,"DATE_TIME")));
                data[i][1] = Finances.getString(i,"CLIENT_NAME");
                data[i][2] = FormatUAH.format(-self_cost);
                data[i][3] = FormatUAH.format(cost);
                data[i][4] = FormatUAH.format(delivery_cost);
                data[i][5] = FormatUAH.format(-delivery_payment);
                data[i][6] = FormatUAH.format(-delivery_transport_cost);
                data[i][7] = FormatUAH.format(-delivery_courier_cost);
                data[i][8] = FormatUAH.format(profit);
            }
            data[Finances.getLength()][1] = "<html><b>ИТОГО:</b></html>";
            data[Finances.getLength()][2] = FormatUAH.format(summ_self_cost);
            data[Finances.getLength()][3] = FormatUAH.format(summ_cost);
            data[Finances.getLength()][4] = FormatUAH.format(summ_delivery_cost);
            data[Finances.getLength()][5] = FormatUAH.format(-summ_delivery_payment);
            data[Finances.getLength()][6] = FormatUAH.format(-summ_delivery_transport_cost);
            data[Finances.getLength()][7] = FormatUAH.format(-summ_delivery_courier_cost);
            data[Finances.getLength()][8] = FormatUAH.format(summ_profit);
        }
        jTable19.makeTable(data); 
    }
    
    public void GetExpenses() {
        Calendar cal = (Calendar)jDateChooser10.getCalendar().clone();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        long StartTime = cal.getTimeInMillis()/1000;
        cal = (Calendar)jDateChooser11.getCalendar().clone();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        long EndTime = cal.getTimeInMillis()/1000;        
        Expenses.set(db.SelectSQL("SELECT * FROM expense WHERE date_time BETWEEN ? AND ? AND name LIKE '%"+jTextField45.getText()+"%' ORDER BY date_time",new Object[]{StartTime,EndTime}));
    }
    
    public int getExpensesTableSelectedRow() {
        return jTable20.getSelectedRow();
    }
    
    public int getExpensesTableRowCount() {
        return jTable20.getRowCount();
    }
    
    public void MakeTableOfExpenses() {
        Object data[][] = new Object[0][3];
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        if (!Expenses.IsNull()) {
            data = new Object[Expenses.getLength()+1][3];
            double summ = 0;
            for (int i = 0; i < Expenses.getLength(); i++) {
                data[i][0] = sdf.format(new Date(Expenses.getInt(i,"DATE_TIME")*1000L));
                data[i][1] = Expenses.getString(i,"NAME");
                data[i][2] = FormatUAH.format(Expenses.getDouble(i,"COST"));
                summ+=Expenses.getDouble(i,"COST");
            }
            data[Expenses.getLength()][2] = FormatUAH.format(summ);
        }
        jTable20.makeTable(data); 
    }
    
    private void deleteOrCancelOrder() {
        if (Orders.getPosition() == -1) {
            return;
        }
        int state = Orders.getInt("STATE");
        if (state > Order.ORDER_PACK) {
            if (userLevel==DIRECTOR) {
                String str;
                while (true) {
                    str = JOptionPane.showInputDialog(null, "Заказ уже упакован. Его отмена может привести к потере/порче данных!\nВведите свой пароль для отмены заказа на свой страх и риск", "Отмена заказа", JOptionPane.WARNING_MESSAGE);
                    if (str==null) return;
                    if (str.equals(CurrentUser.getString("PASS"))) {
                        cancelOrder(state);
                        return;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Нельзя отменить уже упакованный заказ!");
                return;
            }
        }
        
        if (Orders.getInt("STATE") == Order.ORDER_CANCEL) {
            deleteOrder();
        } else {
            cancelOrder(state);
        }        
    }
    
    private void deleteOrder() {
            if (JOptionPane.showConfirmDialog(null, "Окончательно удалить заказ " + Orders.getString("CLIENT_NAME") + " ?", "Окончательное удаление заказа", JOptionPane.YES_NO_OPTION) == 0) {
                int id_orders = Orders.getInt("ID");
                boolean result = db.DoSQL("START TRANSACTION");
                if (result) {
                    if (SubOrders.getLength() > 0) {
                        result = db.UpdateSQL("DELETE FROM suborder WHERE id_orders=?", new Object[]{id_orders});
                    }
                    if (result) {
                        result = db.UpdateSQL("DELETE FROM orders WHERE id=? LIMIT 1", new Object[]{id_orders});
                    }
                }
                if (result) {
                    db.DoSQL("COMMIT");
                    GetOrders();
                    MakeTableOfOrders();
                } else {
                    db.DoSQL("ROLLBACK");
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
            }
    }
    
    private void cancelOrder(int orderState) {
            if (JOptionPane.showConfirmDialog(null, "Действительно отменить заказ для " + Orders.getString("CLIENT_NAME") + " ?", "Отмена заказа", JOptionPane.YES_NO_OPTION) == 0) {
                int id_orders = Orders.getInt("ID");
                boolean result = db.DoSQL("START TRANSACTION");
                if (result) {
                    result = db.UpdateSQL("UPDATE orders SET state=-1 WHERE id=?", new Object[]{id_orders});
                    if (result) {
                        db.UpdateSQL("UPDATE delivery SET state=-1 WHERE id_orders=?", new Object[]{id_orders});
                        if (SubOrders.getLength() > 0) {
                            if (orderState >= Order.ORDER_PACK) {
                                for (int i = 0; i < SubOrders.getLength(); i++) {
                                    result = result && db.UpdateSQL("UPDATE packing, suborder SET packing.reserved = packing.reserved - suborder.amount WHERE packing.ID=suborder.id_packing AND suborder.ID=?", new Object[]{SubOrders.getInt(i, "ID")});
                                }
                                for (int i=0;i<SubOrders.getLength();i++) {
                                   result = result && db.UpdateSQL("UPDATE candy, gift_candy, suborder SET candy.reserved = candy.reserved - suborder.amount*gift_candy.amount WHERE gift_candy.ID_CANDY = candy.ID AND gift_candy.ID_GIFT=suborder.ID_GIFT AND suborder.ID=?",new Object[]{SubOrders.getInt(i,"ID")}); 
                                }
                            } else if (orderState == Order.ORDER_PAY) {
                                for (int i = 0; i < SubOrders.getLength(); i++) {
                                    result = result && db.UpdateSQL("UPDATE packing, suborder SET packing.reserved = packing.reserved - suborder.amount WHERE packing.ID=suborder.id_packing AND suborder.ID=?", new Object[]{SubOrders.getInt(i, "ID")});
                                }                                
                            }
                        }
                    }
                }
                if (result) {
                    db.DoSQL("COMMIT");
                    GetOrders();
                    MakeTableOfOrders();
                } else {
                    db.DoSQL("ROLLBACK");
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
            }
    }
    
    private void login() {
        jLabel72.setVisible(false);
        jLabel73.setVisible(false);
        if (!new String(jPasswordField2.getPassword()).equals(new String(jPasswordField3.getPassword()))) {
            jLabel73.setVisible(true);
        } else {
            try {
                Object[][] obj = db.SelectSQL("SELECT * FROM user WHERE login=? AND pass=?",new Object[]{jTextField16.getText(),new String(jPasswordField1.getPassword())});
                CurrentUser.setColumnNames(new String[]{"ID", "NAME", "LOGIN", "PASS", "LEVEL", "CAN_ENTER","FINANCE_PASS"});
                CurrentUser.set(obj);
                if (!CurrentUser.IsNull()) {
                    CurrentUser.setPosition(0);
                    if (jPasswordField2.getPassword().length != 0) {
                        if (db.UpdateSQL("UPDATE user SET pass=? WHERE id=?",new Object[]{new String(jPasswordField2.getPassword()),CurrentUser.getInt("ID_USER")})) {
                            JOptionPane.showMessageDialog(null, "Пароль успешно изменен");
                        } else {
                            JOptionPane.showMessageDialog(null, "Не удалось изменить пароль");
                            return;
                        }
                    }
                    if (CurrentUser.get("CAN_ENTER").equals(false)) {
                        JOptionPane.showMessageDialog(null,"Вход в систему запрещен администратором");
                    } else {
                        Start();
                    }
                } else {
                    jLabel72.setVisible(true);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage() + " login?!");
                jLabel72.setVisible(true);
            }
        }        
    }
    
    private void unblockSession() {
        jLabel72.setVisible(false);
        jLabel73.setVisible(false);     
        try {
                Object[][] obj = db.SelectSQL("SELECT * FROM user WHERE login=? AND pass=?",new Object[]{jTextField16.getText(),new String(jPasswordField1.getPassword())});
                CurrentUser.setColumnNames(new String[]{"ID", "NAME", "LOGIN", "PASS", "LEVEL", "CAN_ENTER"});
                CurrentUser.set(obj);
                if (!CurrentUser.IsNull()) {
                    CurrentUser.setPosition(0);
                    if (CurrentUser.get("CAN_ENTER").equals(false)) {
                        JOptionPane.showMessageDialog(null,"Вход в систему запрещен администратором");
                    } else {
                        CardLayout cl = (CardLayout) jPanel1.getParent().getLayout();
                        cl.show(jPanel1.getParent(), "card3");
                    }
                } else {
                    jLabel72.setVisible(true);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                jLabel72.setVisible(true);
            }        
    }
    
    private void changeView_Table_Tree_CandiesForPackage() {
        CardLayout cl = (CardLayout)jPanel182.getLayout();
        if (jCheckBox3.isSelected()) {
            cl.show(jPanel182, "card1");
        } else {
            cl.show(jPanel182, "card2");
        }
    }
    
    private void addCandyToGift() {
        if (jCheckBox3.isSelected()) { //group candies by fabric
            DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree5.getLastSelectedPathComponent();
            if ((SelectedNode!=null) && (SelectedNode.getLevel()==2) && (Gifts.getPosition()!=-1)) {
                Object[] obj = (Object[]) SelectedNode.getUserObject();
                int id = (Integer) obj[1];
                if (db.UpdateSQL("INSERT INTO gift_candy(id_gift,id_candy,amount) VALUES(?,?,1)",new Object[]{Gifts.getInt("ID"), Candies.getInt(id, "ID")})) {
                    GetGiftsCandy();
                    MakeTableOfGiftsCandy();
                } else {
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
            }        
        } else { // group candies by cost or name
            if ((jTable18.getSelectedRow()!=-1) && (Gifts.getPosition()!=-1)) {
                int id = jTable18.getIDselectedCandy();
                if (db.UpdateSQL("INSERT INTO gift_candy(id_gift,id_candy,amount) VALUES(?,?,1)",new Object[]{Gifts.getInt("ID"), id})) {
                    GetGiftsCandy();
                    MakeTableOfGiftsCandy();
                } else {
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
            }
        }
    }
    
    private void changeTab() {
        if (!CurrentUser.IsNull()) {
            if (jTabbedPane1.getSelectedComponent()==jPanel4) {
                GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
                MakeTreeOfCandies();                                    
            } else if (jTabbedPane1.getSelectedComponent()==jPanel41) {
                GetPackings();
                MakeTreeOfPackings();                                                                        
            } else if (jTabbedPane1.getSelectedComponent()==jPanel37) {
                jToggleButton3.setSelected(jToggleButton1.isSelected());
                GetGifts();
                MakeTreeOfGifts();
                MakeTreeOfCandiesForGift();
                MakeTableOfCandiesForGift();
            } else if (jTabbedPane1.getSelectedComponent()==jPanel7) {
                GetFolders();
                GetClients(jToggleButton5.isSelected());
                MakeTreeOfClients();                
            } else if (jTabbedPane1.getSelectedComponent()==jPanel9) {
                GetOrders();
                MakeTableOfOrders();
            } else if (jTabbedPane1.getSelectedComponent()==jPanel8) {
                jTabbedPane2StateChanged(null);
            } else if (jTabbedPane1.getSelectedComponent()==jPanel5) {
                GetUsers();
                MakeTreeOfUsers();
            } else if (jTabbedPane1.getSelectedComponent()==jPanel45) {
                GetConstants();
                MakePanelOfConstants();                
            } else if (jTabbedPane1.getSelectedComponent()==jPanel19) {
                GetDelivery();
                MakeTableOfDelivery();
            } else if (jTabbedPane1.getSelectedComponent()==jPanel174) {
                jPanel183.setVisible(false);
                char[] str;
                JPasswordField passwordField = new JPasswordField();                
                passwordField.setEchoChar('*');
                while (true) {
                    if (JOptionPane.showConfirmDialog(null, passwordField,"Для доступа введите пароль", JOptionPane.WARNING_MESSAGE)==JOptionPane.CANCEL_OPTION) {
                        jTabbedPane1.setSelectedIndex(0);
                        return;
                    }
                    str = passwordField.getPassword();
                    if (str == null) {
                        jTabbedPane1.setSelectedIndex(0);
                        return;
                    }
                    try {
                        MessageDigest digest = MessageDigest.getInstance("MD5");
                        digest.update(new String(str).getBytes());
                        if (new String(digest.digest()).equals(CurrentUser.getString("FINANCE_PASS"))) {
                            break;
                        }
                    } catch (Exception ex) {}
                }  
                jPanel183.setVisible(true);                
                GetFinances();
                MakeTableOfFinances();
                GetExpenses();
                MakeTableOfExpenses();
            }
        }        
    }
    
    private void createWindowAfterDragClient(String draggedToFolderName) {
        final JDialog windowAfterDragClient = new JDialog();
        windowAfterDragClient.setTitle("Выбрать действие");
        windowAfterDragClient.setAlwaysOnTop(true);
        windowAfterDragClient.setMinimumSize(new java.awt.Dimension(400, 170));
        windowAfterDragClient.setResizable(false);

        JPanel jPanel188 = new JPanel();
        jPanel188.setBackground(new java.awt.Color(204, 255, 255));
        jPanel188.setMaximumSize(new java.awt.Dimension(440, 150));
        jPanel188.setMinimumSize(new java.awt.Dimension(440, 150));
        jPanel188.setPreferredSize(new java.awt.Dimension(440, 150));

        JScrollPane jScrollPane47 = new JScrollPane();
        jScrollPane47.setBackground(new java.awt.Color(204, 255, 255));
        jScrollPane47.setBorder(null);
        jScrollPane47.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane47.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane47.setMaximumSize(new java.awt.Dimension(350, 100));
        jScrollPane47.setMinimumSize(new java.awt.Dimension(350, 100));
        jScrollPane47.setPreferredSize(new java.awt.Dimension(350, 100));

        JTextArea jTextArea11 = new JTextArea();
        jTextArea11.setEditable(false);
        jTextArea11.setBackground(new java.awt.Color(204, 255, 255));
        jTextArea11.setColumns(20);
        jTextArea11.setForeground(new java.awt.Color(0, 0, 255));
        jTextArea11.setLineWrap(true);
        jTextArea11.setRows(5);
        jTextArea11.setWrapStyleWord(true);
        jTextArea11.setBorder(null);
        jTextArea11.setText("Клиент '"+draggedClientName+"'\nиз папки '"+draggedFromFolderName+"'\nв папку '"+draggedToFolderName+"'\nКопировать/переместить?");
        jScrollPane47.setViewportView(jTextArea11);

        jPanel188.add(jScrollPane47);

        windowAfterDragClient.getContentPane().add(jPanel188, java.awt.BorderLayout.CENTER);

        JPanel jPanel189 = new JPanel();
        jPanel189.setBackground(new java.awt.Color(158, 255, 255));
        jPanel189.setMaximumSize(new java.awt.Dimension(32767, 35));
        jPanel189.setMinimumSize(new java.awt.Dimension(0, 35));
        jPanel189.setPreferredSize(new java.awt.Dimension(400, 35));
        jPanel189.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 40, 5));

        JButton jButton122 = new JButton();
        jButton122.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Copy-icon.png"))); // NOI18N
        jButton122.setText("копировать");
        jButton122.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (db.UpdateSQL("INSERT INTO client_folder(ID_CLIENT,ID_FOLDER) VALUES(?,?)", new Object[]{draggedClientID,draggedToFolderID})) {
                   GetFolders();
                    MakeTreeOfClients();
                    windowAfterDragClient.setVisible(false);
                }
            }
        });
        jPanel189.add(jButton122);

        JButton jButton123 = new JButton();
        jButton123.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Sign-Forward-icon.png"))); // NOI18N
        jButton123.setText("переместить");
        jButton123.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (db.UpdateSQL("INSERT INTO client_folder(ID_CLIENT,ID_FOLDER) VALUES(?,?)", new Object[]{draggedClientID,draggedToFolderID})) {
                   db.UpdateSQL("DELETE FROM client_folder WHERE id_client=? AND id_folder=?",new Object[]{draggedClientID,draggedFromFolderID});
                    GetFolders();
                    MakeTreeOfClients();
                    windowAfterDragClient.setVisible(false);
                }
            }
        });
        jPanel189.add(jButton123);

        windowAfterDragClient.getContentPane().add(jPanel189, java.awt.BorderLayout.SOUTH);
        windowAfterDragClient.pack();
        windowAfterDragClient.setLocationRelativeTo(null);
        windowAfterDragClient.setVisible(true);
    }
    
    private void showWindowDeliveryCost() {
        if (Users.IsNull()) {
            GetUsers();
        }
        productDeliveryDialog.initDialog();
    }
    
    public void makeRevenueExpenses(double value, String number_factura) {
        switch (jTabbedPane2.getSelectedIndex()) {
            case 0:
                Object[] obj = (Object[]) ((DefaultMutableTreeNode) jTree10.getLastSelectedPathComponent()).getUserObject();
                int id = Candies.getInt((Integer) obj[1], "ID");
                value = value * Candies.getInt((Integer) obj[1], "AMOUNT_IN_BOX");
                if (DoTransactionInStorage(id, 1, (int) (Math.round(value)), number_factura, "candy")) {
                    RefreshNodeCandiesInStorage();
                    jTabbedPane2StateChanged(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
                break;
            case 1:
                Object[] obj2 = (Object[]) ((DefaultMutableTreeNode) jTree11.getLastSelectedPathComponent()).getUserObject();
                int id2 = FilteredPackings.getInt((Integer) obj2[1], "ID");
                if (DoTransactionInStorage(id2, 2, value, number_factura, "packing")) {
                    RefreshNodePackingsInStorage();
                    jTabbedPane2StateChanged(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
                break;
        }      
    }
    
    public int getOrderDeliverySelectedRow() {
        return jTable14.getSelectedRow();
    }
    
    public String getOrderDebtValueFromItsLabel() {
        return jLabel104.getText();
    }
    
    public int getTableOrderSelectedRow() {
        return jTable3.getSelectedRow();
    }
    
    public boolean getExpeditors() {
        Object[][] obj = db.SelectSQL("SELECT id, name, driver FROM expeditors ORDER BY name",null);
        Expeditors.set(obj);
        return obj!=null;
    }
    
    public void fillComboboxExpeditorsInDeliveryFilter() {
        jComboBox14.removeAllItems();
        jComboBox14.addItem(" ");
        for (int i = 0;i<Expeditors.getLength();i++) {
            jComboBox14.addItem(new Object[]{Expeditors.getInt(i, "ID"), Expeditors.getString(i, "NAME")});
        }
    }
    
    public void fillComboboxDeliveryTypesInDeliveryFilter() {
        jComboBox16.removeAllItems();
        jComboBox16.addItem(" ");
        jComboBox16.addItem("По Киеву и обл.");
        jComboBox16.addItem("По Украине");
        jComboBox16.addItem("Самовывоз");
        jComboBox16.addItem("Самовывоз Склад");
        jComboBox16.addItem("Самовывоз Офис");
    }
    
    public void showClientWhichIsCalling(int idClient) {
        jComboBox9.setSelectedIndex(0); //сбрасываем фильтры
        jComboBox10.setSelectedIndex(0);
        jTextField38.setText("");
        
        GetFolders();
        GetClients(jToggleButton5.isSelected()); //обновляем список
        MakeTreeOfClients();
        
        jTabbedPane1.setSelectedComponent(jPanel7); //show clients tab
        
        TreeModel model = jTree3.getModel();
        for (int i = 0;i<model.getChildCount(model.getRoot());i++) {
            DefaultMutableTreeNode dftm = (DefaultMutableTreeNode)model.getChild(model.getRoot(), i);
            for (int j = 0;j<model.getChildCount(dftm);j++) {
                DefaultMutableTreeNode leaf = (DefaultMutableTreeNode)model.getChild(dftm, j);
                Object[] obj = (Object[]) leaf.getUserObject();
                int index = (Integer)obj[1];
                if (Clients.getInt(index,"ID")==idClient) {
                    jTree3.setSelectionPath(new TreePath(leaf.getPath()));
                    Rectangle r = jTree3.getRowBounds(jTree3.getMinSelectionRow());
                    Point p = new Point(r.x, r.y);
                    ((JViewport)(jTree3.getParent())).setViewPosition(p);
                    SelectNodeOfTreeClients();
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        buttonGroup7 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel70 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new CandiesTree();
        jPanel71 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jPanel117 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton7 = new javax.swing.JToggleButton();
        jButton30 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton66 = new javax.swing.JButton();
        jPanel173 = new javax.swing.JPanel();
        jLabel122 = new javax.swing.JLabel();
        jTextField53 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(130, 0), new java.awt.Dimension(32767, 0));
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton59 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel123 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField31 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel103 = new javax.swing.JPanel();
        jLabel97 = new javax.swing.JLabel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jPanel104 = new javax.swing.JPanel();
        jLabel98 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable2 = new TableCandiesOfFactory();
        jPanel41 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jPanel52 = new javax.swing.JPanel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(120, 0), new java.awt.Dimension(32767, 0));
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jPanel43 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jPanel112 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jTextField30 = new javax.swing.JTextField();
        jPanel46 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jPanel47 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jLabel56 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jPanel49 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jPanel120 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jScrollPane25 = new javax.swing.JScrollPane();
        jTextArea6 = new javax.swing.JTextArea();
        jPanel185 = new javax.swing.JPanel();
        filler23 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(100, 0), new java.awt.Dimension(32767, 0));
        jCheckBox4 = new javax.swing.JCheckBox();
        jPanel72 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTree6 = new PackingsTree();
        jPanel75 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton67 = new javax.swing.JButton();
        jPanel180 = new javax.swing.JPanel();
        jLabel126 = new javax.swing.JLabel();
        jTextField56 = new javax.swing.JTextField();
        jPanel37 = new javax.swing.JPanel();
        jPanel63 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTree4 = new GiftsTree();
        jPanel80 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton38 = new javax.swing.JButton();
        jButton63 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton68 = new javax.swing.JButton();
        jPanel81 = new javax.swing.JPanel();
        jPanel84 = new javax.swing.JPanel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(140, 0), new java.awt.Dimension(32767, 0));
        jButton33 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton64 = new javax.swing.JButton();
        jButton84 = new javax.swing.JButton();
        jPanel82 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jPanel126 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        jTextField32 = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable1 = new TableGiftCandy();
        jPanel39 = new javax.swing.JPanel();
        jPanel182 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTree5 = new CandiesTreeForPackage();
        jScrollPane45 = new javax.swing.JScrollPane();
        jTable18 = new TableCandiesForGift();
        jPanel40 = new GradientPanel(GradientPanel.CENTER_VERTICAL, DarkInterfaceColor, LightInterfaceColor);
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 75), new java.awt.Dimension(0, 75), new java.awt.Dimension(0, 75));
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel118 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jPanel119 = new javax.swing.JPanel();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton8 = new javax.swing.JToggleButton();
        jCheckBox3 = new javax.swing.JCheckBox();
        jButton69 = new javax.swing.JButton();
        jPanel178 = new javax.swing.JPanel();
        jLabel123 = new javax.swing.JLabel();
        jTextField54 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel77 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree3 = new ClientsTree();
        jPanel79 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton36 = new javax.swing.JButton();
        jButton121 = new javax.swing.JButton();
        jButton129 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jPanel121 = new javax.swing.JPanel();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton6 = new javax.swing.JToggleButton();
        jPanel197 = new javax.swing.JPanel();
        jButton65 = new javax.swing.JButton();
        jButton115 = new javax.swing.JButton();
        jButton70 = new javax.swing.JButton();
        jComboBox9 = new ComboboxClientState(true);
        jComboBox10 = new ComboboxUsers();
        jPanel161 = new javax.swing.JPanel();
        jLabel106 = new javax.swing.JLabel();
        jTextField38 = new javax.swing.JTextField();
        jPanel122 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel23 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(130, 0), new java.awt.Dimension(32767, 0));
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel172 = new javax.swing.JPanel();
        jLabel125 = new javax.swing.JLabel();
        jTextField47 = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jLabel134 = new javax.swing.JLabel();
        jComboBox8 = new ComboboxClientState(false);
        jLabel135 = new javax.swing.JLabel();
        jComboBox11 = new ComboboxUsers();
        jPanel105 = new javax.swing.JPanel();
        jLabel100 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jPanel25 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jPanel55 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jTextField25 = new javax.swing.JTextField();
        jPanel60 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jTextField52 = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jPanel56 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jPanel108 = new javax.swing.JPanel();
        jLabel102 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jPanel109 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jTextField27 = new javax.swing.JTextField();
        jPanel110 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jTextField28 = new javax.swing.JTextField();
        jPanel58 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jPanel111 = new javax.swing.JPanel();
        jLabel103 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jPanel35 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jPanel36 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea7 = new javax.swing.JTextArea();
        jPanel115 = new javax.swing.JPanel();
        jPanel91 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable4 = new TableOrdersOfClient();
        jPanel195 = new javax.swing.JPanel();
        jScrollPane26 = new javax.swing.JScrollPane();
        jTable10 = new TableClients();
        jPanel196 = new javax.swing.JPanel();
        jDateChooser13 = new com.toedter.calendar.JDateChooser();
        jLabel133 = new javax.swing.JLabel();
        jDateChooser14 = new com.toedter.calendar.JDateChooser();
        jButton128 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel64 = new javax.swing.JPanel();
        jPanel65 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jPanel162 = new javax.swing.JPanel();
        jPanel170 = new javax.swing.JPanel();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jPanel61 = new javax.swing.JPanel();
        jLabel146 = new javax.swing.JLabel();
        jLabel144 = new javax.swing.JLabel();
        jTextField57 = new javax.swing.JTextField();
        jLabel145 = new javax.swing.JLabel();
        jTextField58 = new javax.swing.JTextField();
        jPanel171 = new javax.swing.JPanel();
        jLabel93 = new javax.swing.JLabel();
        jPanel107 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel108 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel107 = new javax.swing.JLabel();
        jTextField39 = new javax.swing.JTextField();
        jLabel137 = new javax.swing.JLabel();
        jTextField43 = new javax.swing.JTextField();
        jLabel130 = new javax.swing.JLabel();
        jComboBox6 = new ComboboxUsers();
        jLabel138 = new javax.swing.JLabel();
        jTextField46 = new javax.swing.JTextField();
        jLabel139 = new javax.swing.JLabel();
        jTextField48 = new javax.swing.JTextField();
        jLabel121 = new javax.swing.JLabel();
        jComboBoxPaymentTypesOrderList = new javax.swing.JComboBox();
        jPanel163 = new javax.swing.JPanel();
        filler18 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(32767, 0));
        jButton22 = new javax.swing.JButton();
        filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(32767, 0));
        jButton10 = new javax.swing.JButton();
        jButton108 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable3 = new TableOrders();
        jPanel59 = new javax.swing.JPanel();
        jButton20 = new javax.swing.JButton();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel151 = new javax.swing.JPanel();
        jPanel85 = new javax.swing.JPanel();
        jPanel150 = new javax.swing.JPanel();
        jPanel132 = new javax.swing.JPanel();
        jPanel130 = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        jPanel124 = new javax.swing.JPanel();
        jLabel69 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jComboBoxPaymentTypesOrderDetail = new javax.swing.JComboBox();
        jPanel129 = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        jTextField33 = new javax.swing.JTextField();
        jPanel133 = new javax.swing.JPanel();
        jLabel94 = new javax.swing.JLabel();
        jTextField37 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jPanel169 = new javax.swing.JPanel();
        jLabel113 = new javax.swing.JLabel();
        jTextField41 = new javax.swing.JTextField();
        jLabel114 = new javax.swing.JLabel();
        jPanel159 = new javax.swing.JPanel();
        jLabel101 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jPanel94 = new javax.swing.JPanel();
        jPanel131 = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        jScrollPane32 = new javax.swing.JScrollPane();
        jTextArea9 = new javax.swing.JTextArea();
        jPanel134 = new javax.swing.JPanel();
        jButton74 = new javax.swing.JButton();
        jButton78 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jPanel125 = new javax.swing.JPanel();
        jPanel148 = new javax.swing.JPanel();
        jScrollPane38 = new javax.swing.JScrollPane();
        jTextArea8 = new javax.swing.JTextArea();
        jPanel194 = new javax.swing.JPanel();
        jScrollPane49 = new javax.swing.JScrollPane();
        jTextArea12 = new javax.swing.JTextArea();
        jPanel147 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jButton27 = new javax.swing.JButton();
        jButtonSaveOrder = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(40, 0), new java.awt.Dimension(32767, 0));
        jButton100 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel51 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTable5 = new TableSubOrders();
        jPanel50 = new javax.swing.JPanel();
        jPanel96 = new javax.swing.JPanel();
        jPanel57 = new javax.swing.JPanel();
        filler13 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(40, 20), new java.awt.Dimension(0, 32767));
        jButton90 = new javax.swing.JButton();
        jButton91 = new javax.swing.JButton();
        jButton89 = new javax.swing.JButton();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable14 = new TableDeliveryOrder();
        jPanel8 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel113 = new javax.swing.JPanel();
        jPanel153 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTree10 = new CandiesTreeForStorage();
        jPanel154 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        filler11 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(25, 0), new java.awt.Dimension(32767, 0));
        jPanel179 = new javax.swing.JPanel();
        jLabel124 = new javax.swing.JLabel();
        jTextField55 = new javax.swing.JTextField();
        jToggleButton9 = new javax.swing.JToggleButton();
        jToggleButton10 = new javax.swing.JToggleButton();
        jPanel165 = new javax.swing.JPanel();
        jPanel168 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jLabel112 = new javax.swing.JLabel();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTable6 = new TableStorage();
        jPanel166 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jLabel110 = new javax.swing.JLabel();
        jScrollPane40 = new javax.swing.JScrollPane();
        jTable17 = new TableStorageReserv();
        jPanel114 = new javax.swing.JPanel();
        jPanel155 = new javax.swing.JPanel();
        jPanel156 = new javax.swing.JPanel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        filler12 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(15, 0), new java.awt.Dimension(32767, 0));
        jScrollPane19 = new javax.swing.JScrollPane();
        jTree11 = new PackingsTreeForStorage();
        jPanel188 = new javax.swing.JPanel();
        jLabel136 = new javax.swing.JLabel();
        jTextField50 = new javax.swing.JTextField();
        jPanel157 = new javax.swing.JPanel();
        jPanel167 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jLabel111 = new javax.swing.JLabel();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTable8 = new TableStorage();
        jPanel158 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jLabel96 = new javax.swing.JLabel();
        jScrollPane39 = new javax.swing.JScrollPane();
        jTable16 = new TableStorageReserv();
        jPanel88 = new javax.swing.JPanel();
        jScrollPane24 = new javax.swing.JScrollPane();
        jTable9 = new MainPackage.TableOrderCandyForStorage();
        jPanel116 = new javax.swing.JPanel();
        jScrollPane46 = new javax.swing.JScrollPane();
        jTable22 = new MainPackage.TableOrderPackingForStorage();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable11 = new MainPackage.TableStorageOrderPref();
        jPanel89 = new javax.swing.JPanel();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0));
        jButton62 = new javax.swing.JButton();
        jButton61 = new javax.swing.JButton();
        jButton85 = new javax.swing.JButton();
        filler14 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(300, 0), new java.awt.Dimension(32767, 0));
        jButton105 = new javax.swing.JButton();
        filler22 = new javax.swing.Box.Filler(new java.awt.Dimension(100, 0), new java.awt.Dimension(100, 0), new java.awt.Dimension(32767, 0));
        jButton117 = new javax.swing.JButton();
        jButton118 = new javax.swing.JButton();
        jButton119 = new javax.swing.JButton();
        jPanel90 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton71 = new javax.swing.JButton();
        jPanel192 = new javax.swing.JPanel();
        jButton72 = new javax.swing.JButton();
        jButton52 = new javax.swing.JButton();
        jPanel193 = new javax.swing.JPanel();
        jButton120 = new javax.swing.JButton();
        jButton116 = new javax.swing.JButton();
        jButton126 = new javax.swing.JButton();
        filler24 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(32767, 0));
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jButton44 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jPanel99 = new javax.swing.JPanel();
        jPanel138 = new javax.swing.JPanel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jComboBox5 = new javax.swing.JComboBox();
        jRadioButton4 = new javax.swing.JRadioButton();
        jDateChooser7 = new com.toedter.calendar.JDateChooser();
        jLabel131 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel132 = new javax.swing.JLabel();
        jComboBox7 = new ComboboxUsers();
        jLabel140 = new javax.swing.JLabel();
        jTextField49 = new javax.swing.JTextField();
        jLabel141 = new javax.swing.JLabel();
        jComboBox14 = new ComboboxUsers();
        jLabel142 = new javax.swing.JLabel();
        jTextField51 = new javax.swing.JTextField();
        jLabel143 = new javax.swing.JLabel();
        jComboBox16 = new javax.swing.JComboBox();
        jComboBox17 = new javax.swing.JComboBox();
        jLabel147 = new javax.swing.JLabel();
        jTextField59 = new javax.swing.JTextField();
        jLabel148 = new javax.swing.JLabel();
        jLabel149 = new javax.swing.JLabel();
        jTextField60 = new javax.swing.JTextField();
        jButton81 = new javax.swing.JButton();
        jButton82 = new javax.swing.JButton();
        jPanel160 = new javax.swing.JPanel();
        jButton97 = new javax.swing.JButton();
        jButton80 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jScrollPane29 = new javax.swing.JScrollPane();
        jTable12 = new MainPackage.TableDelivery();
        jPanel5 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel83 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton51 = new javax.swing.JButton();
        jPanel44 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jPanel62 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jPanel76 = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel128 = new javax.swing.JLabel();
        jLabel129 = new javax.swing.JLabel();
        jPanel53 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jPanel54 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane27 = new javax.swing.JScrollPane();
        jTextArea10 = new javax.swing.JTextArea();
        jPanel66 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox();
        jLabel89 = new javax.swing.JLabel();
        jComboBox13 = new ComboboxUsers();
        jPanel78 = new javax.swing.JPanel();
        jLabel81 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel22 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel66 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton55 = new javax.swing.JButton();
        jButton60 = new javax.swing.JButton();
        jPanel149 = new javax.swing.JPanel();
        jPanel69 = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTable7 = new TableUsersStorage();
        jPanel128 = new javax.swing.JPanel();
        jLabel92 = new javax.swing.JLabel();
        jScrollPane31 = new javax.swing.JScrollPane();
        jTable13 = new TableUsersStorage();
        jPanel86 = new javax.swing.JPanel();
        jPanel87 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jButton42 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jButton73 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree2 = new UsersTree();
        jPanel30 = new javax.swing.JPanel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel45 = new javax.swing.JPanel();
        jPanel101 = new javax.swing.JPanel();
        jPanel102 = new javax.swing.JPanel();
        jButton56 = new javax.swing.JButton();
        jButton57 = new javax.swing.JButton();
        jButton58 = new javax.swing.JButton();
        jPanel98 = new javax.swing.JPanel();
        jLabel90 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jPanel164 = new javax.swing.JPanel();
        jLabel109 = new javax.swing.JLabel();
        jTextField40 = new javax.swing.JTextField();
        jPanel176 = new javax.swing.JPanel();
        jLabel117 = new javax.swing.JLabel();
        jTextField42 = new javax.swing.JTextField();
        jLabel119 = new javax.swing.JLabel();
        jTextField44 = new javax.swing.JTextField();
        jPanel174 = new javax.swing.JPanel();
        jPanel183 = new javax.swing.JPanel();
        jPanel175 = new javax.swing.JPanel();
        filler19 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(100, 0), new java.awt.Dimension(32767, 0));
        jDateChooser8 = new com.toedter.calendar.JDateChooser();
        jLabel115 = new javax.swing.JLabel();
        jDateChooser9 = new com.toedter.calendar.JDateChooser();
        jLabel116 = new javax.swing.JLabel();
        jScrollPane41 = new javax.swing.JScrollPane();
        jTable19 = new TableFinance();
        jPanel177 = new javax.swing.JPanel();
        filler21 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(32767, 0));
        jButton75 = new javax.swing.JButton();
        jButton114 = new javax.swing.JButton();
        jButton111 = new javax.swing.JButton();
        jLabel120 = new javax.swing.JLabel();
        jTextField45 = new javax.swing.JTextField();
        filler20 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(40, 0), new java.awt.Dimension(32767, 0));
        jDateChooser10 = new com.toedter.calendar.JDateChooser();
        jLabel118 = new javax.swing.JLabel();
        jDateChooser11 = new com.toedter.calendar.JDateChooser();
        jScrollPane43 = new javax.swing.JScrollPane();
        jTable20 = new TableExpense();
        jPanel6 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel43 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jButton127 = new javax.swing.JButton();
        jPanel1 = new GradientPanel(GradientPanel.UP_DOWN,DarkInterfaceColor,Color.WHITE);
        jPanel38 = new GradientPanel(GradientPanel.CENTER_HORIZONTAL,DarkInterfaceColor,LightInterfaceColor);
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jPanel73 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel70 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel71 = new javax.swing.JLabel();
        jPasswordField3 = new javax.swing.JPasswordField();
        jPanel74 = new javax.swing.JPanel();
        jButton49 = new javax.swing.JButton();
        jButton50 = new javax.swing.JButton();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();

        jMenuItem1.setText("лист расхода");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("лист паковки");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem2);

        jMenuItem3.setText("этикетка на ящик");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem3);

        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel2.setBackground(new java.awt.Color(204, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(1640, 1542));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(153, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setMinimumSize(new java.awt.Dimension(333, 50));
        jPanel3.setPreferredSize(new java.awt.Dimension(333, 50));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/back8.png"))); // NOI18N
        jPanel3.add(jLabel1, java.awt.BorderLayout.WEST);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/back2.png"))); // NOI18N
        jPanel3.add(jLabel2, java.awt.BorderLayout.EAST);

        jPanel2.add(jPanel3, java.awt.BorderLayout.NORTH);

        jTabbedPane1.setBackground(new java.awt.Color(153, 255, 255));
        jTabbedPane1.setForeground(new java.awt.Color(0, 0, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(153, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel70.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        jPanel70.setMinimumSize(new java.awt.Dimension(300, 23));
        jPanel70.setPreferredSize(new java.awt.Dimension(300, 2));
        jPanel70.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTree1.setBackground(new java.awt.Color(204, 255, 255));
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree1MousePressed(evt);
            }
        });
        jTree1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTree1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jPanel70.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel71.setBackground(new java.awt.Color(153, 255, 255));
        jPanel71.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel71.setMinimumSize(new java.awt.Dimension(215, 30));
        jPanel71.setPreferredSize(new java.awt.Dimension(215, 30));
        jPanel71.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 0));

        jPanel117.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel117.setOpaque(false);
        jPanel117.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        buttonGroup2.add(jToggleButton1);
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Letter-A-icon.png"))); // NOI18N
        jToggleButton1.setToolTipText("сортировка по алфавиту");
        jToggleButton1.setMaximumSize(new java.awt.Dimension(33, 25));
        jToggleButton1.setMinimumSize(new java.awt.Dimension(33, 25));
        jToggleButton1.setPreferredSize(new java.awt.Dimension(33, 25));
        jToggleButton1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jToggleButton1StateChanged(evt);
            }
        });
        jPanel117.add(jToggleButton1);

        buttonGroup2.add(jToggleButton2);
        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/coins-icon.png"))); // NOI18N
        jToggleButton2.setSelected(true);
        jToggleButton2.setToolTipText("сортировка по цене");
        jToggleButton2.setMaximumSize(new java.awt.Dimension(33, 25));
        jToggleButton2.setMinimumSize(new java.awt.Dimension(33, 25));
        jToggleButton2.setPreferredSize(new java.awt.Dimension(33, 25));
        jToggleButton2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jToggleButton2StateChanged(evt);
            }
        });
        jPanel117.add(jToggleButton2);

        buttonGroup2.add(jToggleButton7);
        jToggleButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sort-price-icon.png"))); // NOI18N
        jToggleButton7.setToolTipText("сортировка по удельной цене");
        jToggleButton7.setMaximumSize(new java.awt.Dimension(33, 25));
        jToggleButton7.setMinimumSize(new java.awt.Dimension(33, 25));
        jToggleButton7.setPreferredSize(new java.awt.Dimension(33, 25));
        jToggleButton7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jToggleButton7StateChanged(evt);
            }
        });
        jPanel117.add(jToggleButton7);

        jPanel71.add(jPanel117);

        jButton30.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton30.setForeground(new java.awt.Color(255, 0, 0));
        jButton30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/industry-icon.png"))); // NOI18N
        jButton30.setText("+");
        jButton30.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton30.setMaximumSize(new java.awt.Dimension(60, 25));
        jButton30.setMinimumSize(new java.awt.Dimension(60, 25));
        jButton30.setPreferredSize(new java.awt.Dimension(60, 25));
        jButton30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton30MousePressed(evt);
            }
        });
        jPanel71.add(jButton30);

        jButton31.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton31.setForeground(new java.awt.Color(255, 0, 0));
        jButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/candy-icon.png"))); // NOI18N
        jButton31.setText("+");
        jButton31.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton31.setMaximumSize(new java.awt.Dimension(60, 25));
        jButton31.setMinimumSize(new java.awt.Dimension(60, 25));
        jButton31.setPreferredSize(new java.awt.Dimension(60, 25));
        jButton31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton31MousePressed(evt);
            }
        });
        jPanel71.add(jButton31);

        jButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png"))); // NOI18N
        jButton32.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton32.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton32.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton32MousePressed(evt);
            }
        });
        jPanel71.add(jButton32);

        jButton66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton66.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton66.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton66.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton66.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton66MousePressed(evt);
            }
        });
        jPanel71.add(jButton66);

        jPanel70.add(jPanel71, java.awt.BorderLayout.NORTH);

        jPanel173.setBackground(new java.awt.Color(153, 255, 255));
        jPanel173.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel173.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel173.setPreferredSize(new java.awt.Dimension(270, 30));
        jPanel173.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 2));

        jLabel122.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Zoom-icon.png"))); // NOI18N
        jPanel173.add(jLabel122);

        jTextField53.setMinimumSize(new java.awt.Dimension(6, 26));
        jTextField53.setPreferredSize(new java.awt.Dimension(200, 26));
        jTextField53.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField53KeyReleased(evt);
            }
        });
        jPanel173.add(jTextField53);

        jPanel70.add(jPanel173, java.awt.BorderLayout.SOUTH);

        jPanel4.add(jPanel70, java.awt.BorderLayout.WEST);

        jPanel10.setBackground(new java.awt.Color(153, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel10.setOpaque(false);
        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.Y_AXIS));

        jPanel17.setBackground(new java.awt.Color(204, 255, 255));
        jPanel17.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel17.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel17.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 3));
        jPanel17.add(filler1);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton1.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton1.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton1.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton1MousePressed(evt);
            }
        });
        jPanel17.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Save-icon.png"))); // NOI18N
        jButton2.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton2.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton2.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton2MousePressed(evt);
            }
        });
        jPanel17.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton3.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton3.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton3.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton3MousePressed(evt);
            }
        });
        jPanel17.add(jButton3);

        jButton59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton59.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton59.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton59.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton59.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton59MousePressed(evt);
            }
        });
        jPanel17.add(jButton59);

        jPanel10.add(jPanel17);

        jPanel12.setBackground(new java.awt.Color(153, 255, 255));
        jPanel12.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel12.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel12.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Название:");
        jLabel5.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel5.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel5.setPreferredSize(new java.awt.Dimension(110, 18));
        jPanel12.add(jLabel5);

        jTextField1.setMaximumSize(new java.awt.Dimension(215, 18));
        jTextField1.setMinimumSize(new java.awt.Dimension(215, 18));
        jTextField1.setPreferredSize(new java.awt.Dimension(215, 18));
        jPanel12.add(jTextField1);

        jPanel10.add(jPanel12);

        jPanel123.setBackground(new java.awt.Color(204, 255, 255));
        jPanel123.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel123.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel123.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel123.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Скидка на закупку:");
        jLabel8.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel8.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel8.setPreferredSize(new java.awt.Dimension(110, 18));
        jPanel123.add(jLabel8);

        jTextField31.setMaximumSize(new java.awt.Dimension(60, 18));
        jTextField31.setMinimumSize(new java.awt.Dimension(60, 18));
        jTextField31.setPreferredSize(new java.awt.Dimension(60, 18));
        jPanel123.add(jTextField31);

        jLabel28.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 255));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("%");
        jLabel28.setMaximumSize(new java.awt.Dimension(20, 18));
        jLabel28.setMinimumSize(new java.awt.Dimension(20, 18));
        jLabel28.setPreferredSize(new java.awt.Dimension(20, 18));
        jLabel28.setRequestFocusEnabled(false);
        jPanel123.add(jLabel28);

        jPanel10.add(jPanel123);

        jPanel14.setBackground(new java.awt.Color(204, 255, 255));
        jPanel14.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel14.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel14.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Вес ящика:");
        jLabel7.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel7.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel7.setPreferredSize(new java.awt.Dimension(110, 18));
        jPanel14.add(jLabel7);

        jTextField2.setMaximumSize(new java.awt.Dimension(100, 18));
        jTextField2.setMinimumSize(new java.awt.Dimension(100, 18));
        jTextField2.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel14.add(jTextField2);

        jLabel23.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23.setText("кг");
        jLabel23.setMaximumSize(new java.awt.Dimension(20, 18));
        jLabel23.setMinimumSize(new java.awt.Dimension(20, 18));
        jLabel23.setPreferredSize(new java.awt.Dimension(20, 18));
        jLabel23.setRequestFocusEnabled(false);
        jPanel14.add(jLabel23);

        jPanel10.add(jPanel14);

        jPanel15.setBackground(new java.awt.Color(153, 255, 255));
        jPanel15.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel15.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel15.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Штук в ящике:");
        jLabel9.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel9.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel9.setPreferredSize(new java.awt.Dimension(110, 18));
        jPanel15.add(jLabel9);

        jTextField3.setMaximumSize(new java.awt.Dimension(100, 18));
        jTextField3.setMinimumSize(new java.awt.Dimension(100, 18));
        jTextField3.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel15.add(jTextField3);

        jLabel24.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("шт");
        jLabel24.setMaximumSize(new java.awt.Dimension(20, 18));
        jLabel24.setMinimumSize(new java.awt.Dimension(20, 18));
        jLabel24.setPreferredSize(new java.awt.Dimension(20, 18));
        jLabel24.setRequestFocusEnabled(false);
        jPanel15.add(jLabel24);

        jPanel10.add(jPanel15);

        jPanel16.setBackground(new java.awt.Color(204, 255, 255));
        jPanel16.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel16.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel16.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Цена 1 кг:");
        jLabel11.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel11.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel11.setPreferredSize(new java.awt.Dimension(110, 18));
        jPanel16.add(jLabel11);

        jTextField4.setMaximumSize(new java.awt.Dimension(100, 18));
        jTextField4.setMinimumSize(new java.awt.Dimension(100, 18));
        jTextField4.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel16.add(jTextField4);

        jLabel19.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("грн");
        jLabel19.setMaximumSize(new java.awt.Dimension(20, 18));
        jLabel19.setMinimumSize(new java.awt.Dimension(20, 18));
        jLabel19.setPreferredSize(new java.awt.Dimension(20, 18));
        jLabel19.setRequestFocusEnabled(false);
        jPanel16.add(jLabel19);

        jPanel10.add(jPanel16);

        jPanel103.setBackground(new java.awt.Color(153, 255, 255));
        jPanel103.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel103.setMinimumSize(new java.awt.Dimension(0, 100));
        jPanel103.setPreferredSize(new java.awt.Dimension(345, 100));
        jPanel103.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel97.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(0, 0, 255));
        jLabel97.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel97.setText("Комментарий:");
        jLabel97.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel97.setMaximumSize(new java.awt.Dimension(110, 90));
        jLabel97.setMinimumSize(new java.awt.Dimension(110, 90));
        jLabel97.setPreferredSize(new java.awt.Dimension(110, 90));
        jPanel103.add(jLabel97);

        jScrollPane22.setBorder(null);
        jScrollPane22.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane22.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane22.setPreferredSize(new java.awt.Dimension(215, 90));

        jTextArea5.setColumns(20);
        jTextArea5.setLineWrap(true);
        jTextArea5.setRows(5);
        jTextArea5.setWrapStyleWord(true);
        jScrollPane22.setViewportView(jTextArea5);

        jPanel103.add(jScrollPane22);

        jPanel10.add(jPanel103);

        jPanel104.setBackground(new java.awt.Color(204, 255, 255));
        jPanel104.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel104.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel104.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel104.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel98.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(0, 0, 255));
        jLabel98.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel98.setText("Изменение цены:");
        jLabel98.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel98.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel98.setPreferredSize(new java.awt.Dimension(110, 18));
        jPanel104.add(jLabel98);

        jLabel99.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(200, 0, 0));
        jLabel99.setMaximumSize(new java.awt.Dimension(100, 14));
        jLabel99.setMinimumSize(new java.awt.Dimension(100, 14));
        jLabel99.setPreferredSize(new java.awt.Dimension(100, 14));
        jPanel104.add(jLabel99);

        jPanel10.add(jPanel104);

        jPanel18.setBackground(new java.awt.Color(153, 255, 255));
        jPanel18.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel18.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel18.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Вес 1 конфеты:");
        jLabel13.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel13.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel13.setPreferredSize(new java.awt.Dimension(110, 18));
        jPanel18.add(jLabel13);

        jLabel14.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(200, 0, 0));
        jLabel14.setMaximumSize(new java.awt.Dimension(50, 14));
        jLabel14.setMinimumSize(new java.awt.Dimension(50, 14));
        jLabel14.setPreferredSize(new java.awt.Dimension(50, 14));
        jPanel18.add(jLabel14);

        jLabel22.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel22.setText("кг");
        jLabel22.setMaximumSize(new java.awt.Dimension(20, 18));
        jLabel22.setMinimumSize(new java.awt.Dimension(20, 18));
        jLabel22.setPreferredSize(new java.awt.Dimension(20, 18));
        jLabel22.setRequestFocusEnabled(false);
        jPanel18.add(jLabel22);

        jPanel10.add(jPanel18);

        jPanel20.setBackground(new java.awt.Color(204, 255, 255));
        jPanel20.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel20.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel20.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Цена 1 конфеты:");
        jLabel15.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel15.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel15.setPreferredSize(new java.awt.Dimension(110, 18));
        jPanel20.add(jLabel15);

        jLabel16.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(200, 0, 0));
        jLabel16.setMaximumSize(new java.awt.Dimension(50, 14));
        jLabel16.setMinimumSize(new java.awt.Dimension(50, 14));
        jLabel16.setPreferredSize(new java.awt.Dimension(50, 14));
        jPanel20.add(jLabel16);

        jLabel20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20.setText("грн");
        jLabel20.setMaximumSize(new java.awt.Dimension(20, 18));
        jLabel20.setMinimumSize(new java.awt.Dimension(20, 18));
        jLabel20.setPreferredSize(new java.awt.Dimension(20, 18));
        jLabel20.setRequestFocusEnabled(false);
        jPanel20.add(jLabel20);

        jPanel10.add(jPanel20);

        jPanel21.setBackground(new java.awt.Color(153, 255, 255));
        jPanel21.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel21.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel21.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Цена ящика:");
        jLabel17.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel17.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel17.setPreferredSize(new java.awt.Dimension(110, 18));
        jPanel21.add(jLabel17);

        jLabel18.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(200, 0, 0));
        jLabel18.setMaximumSize(new java.awt.Dimension(50, 14));
        jLabel18.setMinimumSize(new java.awt.Dimension(50, 14));
        jLabel18.setPreferredSize(new java.awt.Dimension(50, 14));
        jPanel21.add(jLabel18);

        jLabel21.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setText("грн");
        jLabel21.setMaximumSize(new java.awt.Dimension(20, 18));
        jLabel21.setMinimumSize(new java.awt.Dimension(20, 18));
        jLabel21.setPreferredSize(new java.awt.Dimension(20, 18));
        jLabel21.setRequestFocusEnabled(false);
        jPanel21.add(jLabel21);

        jPanel10.add(jPanel21);

        jPanel13.setBackground(new java.awt.Color(153, 255, 255));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jScrollPane7.setBorder(null);

        jTable2.setBackground(new java.awt.Color(153, 255, 255));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable2.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane7.setViewportView(jTable2);

        jPanel13.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel13);

        jPanel4.add(jPanel10, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("КОНФЕТЫ", new javax.swing.ImageIcon(getClass().getResource("/Images/candy-cane-icon.png")), jPanel4); // NOI18N

        jPanel41.setBackground(new java.awt.Color(153, 255, 255));
        jPanel41.setLayout(new java.awt.BorderLayout());

        jPanel42.setBackground(new java.awt.Color(153, 255, 255));
        jPanel42.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel42.setLayout(new javax.swing.BoxLayout(jPanel42, javax.swing.BoxLayout.Y_AXIS));

        jPanel52.setBackground(new java.awt.Color(153, 255, 255));
        jPanel52.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel52.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel52.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel52.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));
        jPanel52.add(filler2);

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton13.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton13.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton13.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton13MousePressed(evt);
            }
        });
        jPanel52.add(jButton13);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Save-icon.png"))); // NOI18N
        jButton14.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton14.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton14.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton14MousePressed(evt);
            }
        });
        jPanel52.add(jButton14);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton15.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton15.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton15.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton15MousePressed(evt);
            }
        });
        jPanel52.add(jButton15);

        jPanel42.add(jPanel52);

        jPanel43.setBackground(new java.awt.Color(204, 255, 255));
        jPanel43.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel43.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel43.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel43.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel48.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 0, 255));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Название:");
        jLabel48.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel48.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel48.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel43.add(jLabel48);

        jTextField17.setMaximumSize(new java.awt.Dimension(150, 18));
        jTextField17.setMinimumSize(new java.awt.Dimension(150, 18));
        jTextField17.setPreferredSize(new java.awt.Dimension(150, 18));
        jPanel43.add(jTextField17);

        jPanel42.add(jPanel43);

        jPanel112.setBackground(new java.awt.Color(153, 255, 255));
        jPanel112.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel112.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel112.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel112.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel49.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 0, 255));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("№");
        jLabel49.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel49.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel49.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel112.add(jLabel49);

        jTextField30.setMaximumSize(new java.awt.Dimension(150, 18));
        jTextField30.setMinimumSize(new java.awt.Dimension(150, 18));
        jTextField30.setPreferredSize(new java.awt.Dimension(150, 18));
        jPanel112.add(jTextField30);

        jPanel42.add(jPanel112);

        jPanel46.setBackground(new java.awt.Color(204, 255, 255));
        jPanel46.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel46.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel46.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel46.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel50.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 0, 255));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel50.setText("Цена:");
        jLabel50.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel50.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel50.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel46.add(jLabel50);

        jTextField18.setMaximumSize(new java.awt.Dimension(150, 18));
        jTextField18.setMinimumSize(new java.awt.Dimension(150, 18));
        jTextField18.setPreferredSize(new java.awt.Dimension(150, 18));
        jPanel46.add(jTextField18);

        jLabel52.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 0, 255));
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel52.setText("грн");
        jLabel52.setMaximumSize(new java.awt.Dimension(20, 18));
        jLabel52.setMinimumSize(new java.awt.Dimension(20, 18));
        jLabel52.setPreferredSize(new java.awt.Dimension(20, 18));
        jPanel46.add(jLabel52);

        jPanel42.add(jPanel46);

        jPanel47.setBackground(new java.awt.Color(153, 255, 255));
        jPanel47.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel47.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel47.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel47.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel53.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 0, 255));
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel53.setText("Вес:");
        jLabel53.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel53.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel53.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel47.add(jLabel53);

        jTextField19.setMaximumSize(new java.awt.Dimension(150, 18));
        jTextField19.setMinimumSize(new java.awt.Dimension(150, 18));
        jTextField19.setPreferredSize(new java.awt.Dimension(150, 18));
        jPanel47.add(jTextField19);

        jLabel55.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 0, 255));
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel55.setText("кг");
        jLabel55.setMaximumSize(new java.awt.Dimension(20, 18));
        jLabel55.setMinimumSize(new java.awt.Dimension(20, 18));
        jLabel55.setPreferredSize(new java.awt.Dimension(20, 18));
        jPanel47.add(jLabel55);

        jPanel42.add(jPanel47);

        jPanel48.setBackground(new java.awt.Color(204, 255, 255));
        jPanel48.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel48.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel48.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel48.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel56.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(0, 0, 255));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("Вместимость:");
        jLabel56.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel56.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel56.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel48.add(jLabel56);

        jTextField20.setMaximumSize(new java.awt.Dimension(150, 18));
        jTextField20.setMinimumSize(new java.awt.Dimension(150, 18));
        jTextField20.setPreferredSize(new java.awt.Dimension(150, 18));
        jPanel48.add(jTextField20);

        jLabel58.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(0, 0, 255));
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel58.setText("кг");
        jLabel58.setMaximumSize(new java.awt.Dimension(20, 18));
        jLabel58.setMinimumSize(new java.awt.Dimension(20, 18));
        jLabel58.setPreferredSize(new java.awt.Dimension(20, 18));
        jPanel48.add(jLabel58);

        jPanel42.add(jPanel48);

        jPanel49.setBackground(new java.awt.Color(153, 255, 255));
        jPanel49.setMaximumSize(new java.awt.Dimension(32767, 160));
        jPanel49.setMinimumSize(new java.awt.Dimension(395, 160));
        jPanel49.setPreferredSize(new java.awt.Dimension(395, 160));
        jPanel49.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel59.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(0, 0, 255));
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel59.setText("Фото:");
        jLabel59.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel59.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel59.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel49.add(jLabel59);

        jLabel60.setBackground(new java.awt.Color(153, 255, 255));
        jLabel60.setForeground(new java.awt.Color(255, 255, 0));
        jLabel60.setMaximumSize(new java.awt.Dimension(150, 150));
        jLabel60.setMinimumSize(new java.awt.Dimension(150, 150));
        jLabel60.setOpaque(true);
        jLabel60.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanel49.add(jLabel60);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Folder-Downloads-icon.png"))); // NOI18N
        jButton9.setMaximumSize(new java.awt.Dimension(35, 35));
        jButton9.setMinimumSize(new java.awt.Dimension(35, 35));
        jButton9.setPreferredSize(new java.awt.Dimension(35, 35));
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton9MousePressed(evt);
            }
        });
        jPanel49.add(jButton9);

        jButton45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Sign-Error-icon.png"))); // NOI18N
        jButton45.setMaximumSize(new java.awt.Dimension(35, 35));
        jButton45.setMinimumSize(new java.awt.Dimension(35, 35));
        jButton45.setPreferredSize(new java.awt.Dimension(35, 35));
        jButton45.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton45MousePressed(evt);
            }
        });
        jPanel49.add(jButton45);

        jPanel42.add(jPanel49);

        jPanel120.setBackground(new java.awt.Color(204, 255, 255));
        jPanel120.setMaximumSize(new java.awt.Dimension(32767, 90));
        jPanel120.setMinimumSize(new java.awt.Dimension(0, 90));
        jPanel120.setPreferredSize(new java.awt.Dimension(345, 90));
        jPanel120.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel57.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(0, 0, 255));
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel57.setText("Комментарий:");
        jLabel57.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel57.setMaximumSize(new java.awt.Dimension(100, 75));
        jLabel57.setMinimumSize(new java.awt.Dimension(100, 75));
        jLabel57.setPreferredSize(new java.awt.Dimension(100, 75));
        jPanel120.add(jLabel57);

        jScrollPane25.setBorder(null);
        jScrollPane25.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane25.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane25.setPreferredSize(new java.awt.Dimension(180, 80));

        jTextArea6.setColumns(20);
        jTextArea6.setRows(5);
        jScrollPane25.setViewportView(jTextArea6);

        jPanel120.add(jScrollPane25);

        jPanel42.add(jPanel120);

        jPanel185.setBackground(new java.awt.Color(153, 255, 255));
        jPanel185.setMaximumSize(new java.awt.Dimension(32767, 38));
        jPanel185.setMinimumSize(new java.awt.Dimension(0, 38));
        jPanel185.setPreferredSize(new java.awt.Dimension(345, 38));
        jPanel185.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 15));
        jPanel185.add(filler23);

        jCheckBox4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jCheckBox4.setForeground(new java.awt.Color(0, 0, 255));
        jCheckBox4.setSelected(true);
        jCheckBox4.setText("специальная отметка");
        jCheckBox4.setOpaque(false);
        jPanel185.add(jCheckBox4);

        jPanel42.add(jPanel185);

        jPanel41.add(jPanel42, java.awt.BorderLayout.CENTER);

        jPanel72.setMaximumSize(new java.awt.Dimension(410, 2147483647));
        jPanel72.setMinimumSize(new java.awt.Dimension(410, 0));
        jPanel72.setPreferredSize(new java.awt.Dimension(410, 0));
        jPanel72.setLayout(new java.awt.BorderLayout());

        jScrollPane9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTree6.setBackground(new java.awt.Color(204, 255, 255));
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree6.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree6.setRootVisible(false);
        jTree6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree6MousePressed(evt);
            }
        });
        jTree6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTree6KeyReleased(evt);
            }
        });
        jScrollPane9.setViewportView(jTree6);

        jPanel72.add(jScrollPane9, java.awt.BorderLayout.CENTER);

        jPanel75.setBackground(new java.awt.Color(153, 255, 255));
        jPanel75.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel75.setMinimumSize(new java.awt.Dimension(145, 30));
        jPanel75.setPreferredSize(new java.awt.Dimension(145, 30));
        jPanel75.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        jButton34.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton34.setForeground(new java.awt.Color(255, 0, 0));
        jButton34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/upakovka_146_2.png"))); // NOI18N
        jButton34.setText("+");
        jButton34.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton34.setMaximumSize(new java.awt.Dimension(65, 25));
        jButton34.setMinimumSize(new java.awt.Dimension(65, 25));
        jButton34.setPreferredSize(new java.awt.Dimension(65, 25));
        jButton34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton34MousePressed(evt);
            }
        });
        jPanel75.add(jButton34);

        jButton35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png"))); // NOI18N
        jButton35.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton35.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton35.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton35MousePressed(evt);
            }
        });
        jPanel75.add(jButton35);

        jButton67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton67.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton67.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton67.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton67.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton67MousePressed(evt);
            }
        });
        jPanel75.add(jButton67);

        jPanel72.add(jPanel75, java.awt.BorderLayout.NORTH);

        jPanel180.setBackground(new java.awt.Color(153, 255, 255));
        jPanel180.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel180.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel180.setPreferredSize(new java.awt.Dimension(270, 30));
        jPanel180.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 2));

        jLabel126.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Zoom-icon.png"))); // NOI18N
        jPanel180.add(jLabel126);

        jTextField56.setMinimumSize(new java.awt.Dimension(6, 26));
        jTextField56.setPreferredSize(new java.awt.Dimension(200, 26));
        jTextField56.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField56KeyReleased(evt);
            }
        });
        jPanel180.add(jTextField56);

        jPanel72.add(jPanel180, java.awt.BorderLayout.SOUTH);

        jPanel41.add(jPanel72, java.awt.BorderLayout.WEST);

        jTabbedPane1.addTab("УПАКОВКА", new javax.swing.ImageIcon(getClass().getResource("/Images/upakovka_146.png")), jPanel41); // NOI18N

        jPanel37.setBackground(new java.awt.Color(153, 255, 255));
        jPanel37.setLayout(new java.awt.BorderLayout());

        jPanel63.setMaximumSize(new java.awt.Dimension(180, 2147483647));
        jPanel63.setMinimumSize(new java.awt.Dimension(180, 23));
        jPanel63.setPreferredSize(new java.awt.Dimension(180, 100));
        jPanel63.setLayout(new java.awt.BorderLayout());

        jTree4.setBackground(new java.awt.Color(204, 255, 255));
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
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

        jPanel63.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jPanel80.setBackground(new java.awt.Color(153, 255, 255));
        jPanel80.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel80.setMinimumSize(new java.awt.Dimension(145, 30));
        jPanel80.setPreferredSize(new java.awt.Dimension(145, 30));
        jPanel80.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 1));

        jButton38.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton38.setForeground(new java.awt.Color(255, 0, 0));
        jButton38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/gift-icon2.png"))); // NOI18N
        jButton38.setText("+");
        jButton38.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton38.setMaximumSize(new java.awt.Dimension(65, 25));
        jButton38.setMinimumSize(new java.awt.Dimension(65, 25));
        jButton38.setPreferredSize(new java.awt.Dimension(65, 25));
        jButton38.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton38MousePressed(evt);
            }
        });
        jPanel80.add(jButton38);

        jButton63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Copy-icon.png"))); // NOI18N
        jButton63.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton63.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton63.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton63.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton63MousePressed(evt);
            }
        });
        jPanel80.add(jButton63);

        jButton39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png"))); // NOI18N
        jButton39.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton39.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton39.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton39.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton39MousePressed(evt);
            }
        });
        jPanel80.add(jButton39);

        jButton68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton68.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton68.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton68.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton68.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton68MousePressed(evt);
            }
        });
        jPanel80.add(jButton68);

        jPanel63.add(jPanel80, java.awt.BorderLayout.NORTH);

        jPanel37.add(jPanel63, java.awt.BorderLayout.WEST);

        jPanel81.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel81.setLayout(new javax.swing.BoxLayout(jPanel81, javax.swing.BoxLayout.Y_AXIS));

        jPanel84.setBackground(new java.awt.Color(153, 255, 255));
        jPanel84.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel84.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel84.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel84.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));
        jPanel84.add(filler3);

        jButton33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton33.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton33.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton33.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton33MousePressed(evt);
            }
        });
        jPanel84.add(jButton33);

        jButton40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Save-icon.png"))); // NOI18N
        jButton40.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton40.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton40.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton40MousePressed(evt);
            }
        });
        jPanel84.add(jButton40);

        jButton41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton41.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton41.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton41.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton41MousePressed(evt);
            }
        });
        jPanel84.add(jButton41);

        jButton64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton64.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton64.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton64.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton64.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton64MousePressed(evt);
            }
        });
        jPanel84.add(jButton64);

        jButton84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Excel-icon.png"))); // NOI18N
        jButton84.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton84.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton84.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton84.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton84MousePressed(evt);
            }
        });
        jPanel84.add(jButton84);

        jPanel81.add(jPanel84);

        jPanel82.setBackground(new java.awt.Color(204, 255, 255));
        jPanel82.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel82.setMinimumSize(new java.awt.Dimension(0, 26));
        jPanel82.setPreferredSize(new java.awt.Dimension(345, 26));
        jPanel82.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel61.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(0, 0, 255));
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel61.setText("Название:");
        jLabel61.setMaximumSize(new java.awt.Dimension(120, 18));
        jLabel61.setMinimumSize(new java.awt.Dimension(120, 18));
        jLabel61.setPreferredSize(new java.awt.Dimension(120, 18));
        jPanel82.add(jLabel61);

        jTextField21.setMaximumSize(new java.awt.Dimension(140, 18));
        jTextField21.setMinimumSize(new java.awt.Dimension(140, 18));
        jTextField21.setPreferredSize(new java.awt.Dimension(140, 18));
        jPanel82.add(jTextField21);

        jPanel81.add(jPanel82);

        jPanel126.setBackground(new java.awt.Color(153, 255, 255));
        jPanel126.setMaximumSize(new java.awt.Dimension(32767, 26));
        jPanel126.setMinimumSize(new java.awt.Dimension(0, 26));
        jPanel126.setPreferredSize(new java.awt.Dimension(345, 26));
        jPanel126.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel62.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(0, 0, 255));
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel62.setText("Плата за паковку:");
        jLabel62.setMaximumSize(new java.awt.Dimension(120, 18));
        jLabel62.setMinimumSize(new java.awt.Dimension(120, 18));
        jLabel62.setPreferredSize(new java.awt.Dimension(120, 18));
        jPanel126.add(jLabel62);

        jTextField32.setMaximumSize(new java.awt.Dimension(140, 18));
        jTextField32.setMinimumSize(new java.awt.Dimension(140, 18));
        jTextField32.setPreferredSize(new java.awt.Dimension(140, 18));
        jPanel126.add(jTextField32);

        jPanel81.add(jPanel126);

        jTable1.setBackground(new java.awt.Color(153, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane6.setViewportView(jTable1);

        jPanel81.add(jScrollPane6);

        jPanel37.add(jPanel81, java.awt.BorderLayout.CENTER);

        jPanel39.setMaximumSize(new java.awt.Dimension(365, 32767));
        jPanel39.setMinimumSize(new java.awt.Dimension(365, 10));
        jPanel39.setPreferredSize(new java.awt.Dimension(365, 100));
        jPanel39.setLayout(new java.awt.BorderLayout());

        jPanel182.setLayout(new java.awt.CardLayout());

        jTree5.setBackground(new java.awt.Color(204, 255, 255));
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree5.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree5.setRootVisible(false);
        jScrollPane8.setViewportView(jTree5);

        jPanel182.add(jScrollPane8, "card1");

        jTable18.setBackground(new java.awt.Color(153, 255, 255));
        jTable18.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable18.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jTable18.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane45.setViewportView(jTable18);

        jPanel182.add(jScrollPane45, "card2");

        jPanel39.add(jPanel182, java.awt.BorderLayout.CENTER);

        jPanel40.setBackground(new java.awt.Color(153, 255, 255));
        jPanel40.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel40.setMaximumSize(new java.awt.Dimension(30, 32767));
        jPanel40.setMinimumSize(new java.awt.Dimension(30, 10));
        jPanel40.setPreferredSize(new java.awt.Dimension(30, 100));
        jPanel40.setLayout(new javax.swing.BoxLayout(jPanel40, javax.swing.BoxLayout.Y_AXIS));
        jPanel40.add(filler4);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Actions-arrow-left-double-icon.png"))); // NOI18N
        jButton7.setOpaque(false);
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton7MousePressed(evt);
            }
        });
        jPanel40.add(jButton7);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Sign-Error-icon.png"))); // NOI18N
        jButton8.setOpaque(false);
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton8MousePressed(evt);
            }
        });
        jPanel40.add(jButton8);

        jPanel39.add(jPanel40, java.awt.BorderLayout.WEST);

        jPanel118.setBackground(new java.awt.Color(153, 255, 255));
        jPanel118.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel118.setMinimumSize(new java.awt.Dimension(215, 30));
        jPanel118.setPreferredSize(new java.awt.Dimension(215, 30));
        jPanel118.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        jPanel119.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel119.setOpaque(false);
        jPanel119.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        buttonGroup3.add(jToggleButton3);
        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Letter-A-icon.png"))); // NOI18N
        jToggleButton3.setToolTipText("сортировать по алфавиту");
        jToggleButton3.setMaximumSize(new java.awt.Dimension(33, 25));
        jToggleButton3.setMinimumSize(new java.awt.Dimension(33, 25));
        jToggleButton3.setPreferredSize(new java.awt.Dimension(33, 25));
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });
        jPanel119.add(jToggleButton3);

        buttonGroup3.add(jToggleButton4);
        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/coins-icon.png"))); // NOI18N
        jToggleButton4.setSelected(true);
        jToggleButton4.setToolTipText("сортировать по цене");
        jToggleButton4.setMaximumSize(new java.awt.Dimension(33, 25));
        jToggleButton4.setMinimumSize(new java.awt.Dimension(33, 25));
        jToggleButton4.setPreferredSize(new java.awt.Dimension(33, 25));
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });
        jPanel119.add(jToggleButton4);

        buttonGroup3.add(jToggleButton8);
        jToggleButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/sort-price-icon.png"))); // NOI18N
        jToggleButton8.setToolTipText("сортировать по удельной цене");
        jToggleButton8.setMaximumSize(new java.awt.Dimension(33, 25));
        jToggleButton8.setMinimumSize(new java.awt.Dimension(33, 25));
        jToggleButton8.setPreferredSize(new java.awt.Dimension(33, 25));
        jToggleButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton8ActionPerformed(evt);
            }
        });
        jPanel119.add(jToggleButton8);

        jPanel118.add(jPanel119);

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("по фабрикам");
        jCheckBox3.setOpaque(false);
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        jPanel118.add(jCheckBox3);

        jButton69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton69.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton69.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton69.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton69.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton69MousePressed(evt);
            }
        });
        jPanel118.add(jButton69);

        jPanel39.add(jPanel118, java.awt.BorderLayout.NORTH);

        jPanel178.setBackground(new java.awt.Color(153, 255, 255));
        jPanel178.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel178.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel178.setPreferredSize(new java.awt.Dimension(270, 30));
        jPanel178.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 2));

        jLabel123.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Zoom-icon.png"))); // NOI18N
        jPanel178.add(jLabel123);

        jTextField54.setMinimumSize(new java.awt.Dimension(6, 26));
        jTextField54.setPreferredSize(new java.awt.Dimension(200, 26));
        jTextField54.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField54KeyReleased(evt);
            }
        });
        jPanel178.add(jTextField54);

        jPanel39.add(jPanel178, java.awt.BorderLayout.SOUTH);

        jPanel37.add(jPanel39, java.awt.BorderLayout.EAST);

        jTabbedPane1.addTab("НАБОРЫ  ", new javax.swing.ImageIcon(getClass().getResource("/Images/gift-icon.png")), jPanel37); // NOI18N

        jPanel7.setBackground(new java.awt.Color(153, 255, 255));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel77.setMaximumSize(new java.awt.Dimension(350, 32767));
        jPanel77.setMinimumSize(new java.awt.Dimension(350, 10));
        jPanel77.setPreferredSize(new java.awt.Dimension(350, 100));
        jPanel77.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTree3.setBackground(new java.awt.Color(204, 255, 255));
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree3.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree3.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jTree3MouseDragged(evt);
            }
        });
        jTree3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTree3MouseReleased(evt);
            }
        });
        jTree3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTree3KeyReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jTree3);

        jPanel77.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel79.setBackground(new java.awt.Color(153, 255, 255));
        jPanel79.setMaximumSize(new java.awt.Dimension(32767, 60));
        jPanel79.setMinimumSize(new java.awt.Dimension(145, 60));
        jPanel79.setPreferredSize(new java.awt.Dimension(145, 60));
        jPanel79.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 2));

        jButton36.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton36.setForeground(new java.awt.Color(255, 0, 0));
        jButton36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Office-Client-Male-Light-icon.png"))); // NOI18N
        jButton36.setText("+");
        jButton36.setToolTipText("новый клиент");
        jButton36.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton36.setMaximumSize(new java.awt.Dimension(60, 25));
        jButton36.setMinimumSize(new java.awt.Dimension(60, 25));
        jButton36.setPreferredSize(new java.awt.Dimension(60, 25));
        jButton36.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton36MousePressed(evt);
            }
        });
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });
        jPanel79.add(jButton36);

        jButton121.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton121.setForeground(new java.awt.Color(255, 0, 0));
        jButton121.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Folder-Open-icon.png"))); // NOI18N
        jButton121.setText("+");
        jButton121.setToolTipText("новая папка");
        jButton121.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton121.setMaximumSize(new java.awt.Dimension(60, 25));
        jButton121.setMinimumSize(new java.awt.Dimension(60, 25));
        jButton121.setPreferredSize(new java.awt.Dimension(60, 25));
        jButton121.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton121MousePressed(evt);
            }
        });
        jPanel79.add(jButton121);

        jButton129.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton129.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton129.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton129.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton129.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton129ActionPerformed(evt);
            }
        });
        jPanel79.add(jButton129);

        jButton37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png"))); // NOI18N
        jButton37.setToolTipText("удалить");
        jButton37.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton37.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton37.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton37.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton37MousePressed(evt);
            }
        });
        jPanel79.add(jButton37);

        jPanel121.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel121.setOpaque(false);
        jPanel121.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        buttonGroup4.add(jToggleButton5);
        jToggleButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Letter-A-icon.png"))); // NOI18N
        jToggleButton5.setSelected(true);
        jToggleButton5.setToolTipText("сортировать по алфавиту");
        jToggleButton5.setMaximumSize(new java.awt.Dimension(33, 25));
        jToggleButton5.setMinimumSize(new java.awt.Dimension(33, 25));
        jToggleButton5.setPreferredSize(new java.awt.Dimension(33, 25));
        jToggleButton5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jToggleButton5StateChanged(evt);
            }
        });
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });
        jPanel121.add(jToggleButton5);

        buttonGroup4.add(jToggleButton6);
        jToggleButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/calendar-icon.png"))); // NOI18N
        jToggleButton6.setToolTipText("сортировать по дате обращения");
        jToggleButton6.setMaximumSize(new java.awt.Dimension(33, 25));
        jToggleButton6.setMinimumSize(new java.awt.Dimension(33, 25));
        jToggleButton6.setPreferredSize(new java.awt.Dimension(33, 25));
        jToggleButton6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jToggleButton6StateChanged(evt);
            }
        });
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });
        jPanel121.add(jToggleButton6);

        jPanel79.add(jPanel121);

        jPanel197.setOpaque(false);
        jPanel197.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 0));

        jButton65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton65.setToolTipText("печать");
        jButton65.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton65.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton65.setPreferredSize(new java.awt.Dimension(32, 25));
        jButton65.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton65MousePressed(evt);
            }
        });
        jPanel197.add(jButton65);

        jButton115.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Excel-icon.png"))); // NOI18N
        jButton115.setToolTipText("импорт в Excel");
        jButton115.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton115.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton115.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton115.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton115ActionPerformed(evt);
            }
        });
        jPanel197.add(jButton115);

        jButton70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton70.setToolTipText("обновить");
        jButton70.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton70.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton70.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton70.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton70MousePressed(evt);
            }
        });
        jPanel197.add(jButton70);

        jPanel79.add(jPanel197);

        jComboBox9.setBackground(new java.awt.Color(153, 255, 255));
        jComboBox9.setMaximumSize(new java.awt.Dimension(49, 24));
        jComboBox9.setMinimumSize(new java.awt.Dimension(49, 24));
        jComboBox9.setPreferredSize(new java.awt.Dimension(49, 24));
        jComboBox9.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox9ItemStateChanged(evt);
            }
        });
        jPanel79.add(jComboBox9);

        jComboBox10.setMaximumSize(new java.awt.Dimension(135, 24));
        jComboBox10.setMinimumSize(new java.awt.Dimension(135, 24));
        jComboBox10.setPreferredSize(new java.awt.Dimension(135, 24));
        jComboBox10.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox10ItemStateChanged(evt);
            }
        });
        jPanel79.add(jComboBox10);

        jPanel77.add(jPanel79, java.awt.BorderLayout.NORTH);

        jPanel161.setBackground(new java.awt.Color(153, 255, 255));
        jPanel161.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel161.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel161.setPreferredSize(new java.awt.Dimension(270, 30));
        jPanel161.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 2));

        jLabel106.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Zoom-icon.png"))); // NOI18N
        jPanel161.add(jLabel106);

        jTextField38.setMinimumSize(new java.awt.Dimension(6, 26));
        jTextField38.setPreferredSize(new java.awt.Dimension(200, 26));
        jTextField38.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField38KeyReleased(evt);
            }
        });
        jPanel161.add(jTextField38);

        jPanel77.add(jPanel161, java.awt.BorderLayout.SOUTH);

        jPanel7.add(jPanel77, java.awt.BorderLayout.WEST);

        jPanel122.setBackground(new java.awt.Color(204, 255, 255));
        jPanel122.setLayout(new java.awt.CardLayout());

        jTabbedPane3.setBackground(new java.awt.Color(204, 255, 255));

        jPanel23.setBackground(new java.awt.Color(204, 255, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel23.setOpaque(false);
        jPanel23.setLayout(new javax.swing.BoxLayout(jPanel23, javax.swing.BoxLayout.Y_AXIS));

        jPanel29.setBackground(new java.awt.Color(153, 255, 255));
        jPanel29.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel29.setMinimumSize(new java.awt.Dimension(0, 28));
        jPanel29.setPreferredSize(new java.awt.Dimension(345, 28));
        jPanel29.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));
        jPanel29.add(filler5);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton4.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton4.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton4.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton4MousePressed(evt);
            }
        });
        jPanel29.add(jButton4);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Save-icon.png"))); // NOI18N
        jButton5.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton5.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton5.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton5MousePressed(evt);
            }
        });
        jPanel29.add(jButton5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton6.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton6.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton6.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton6MousePressed(evt);
            }
        });
        jPanel29.add(jButton6);

        jPanel23.add(jPanel29);

        jPanel172.setBackground(new java.awt.Color(204, 255, 255));
        jPanel172.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel172.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel172.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel172.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel125.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel125.setForeground(new java.awt.Color(0, 0, 255));
        jLabel125.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel125.setText("Офиц. название:");
        jLabel125.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel125.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel125.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel125.setRequestFocusEnabled(false);
        jPanel172.add(jLabel125);

        jTextField47.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField47.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField47.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel172.add(jTextField47);

        jPanel23.add(jPanel172);

        jPanel24.setBackground(new java.awt.Color(153, 255, 255));
        jPanel24.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel24.setMinimumSize(new java.awt.Dimension(0, 32));
        jPanel24.setPreferredSize(new java.awt.Dimension(345, 32));
        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel134.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(0, 0, 255));
        jLabel134.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel134.setText("Статус клиента:");
        jLabel134.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel134.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel134.setPreferredSize(new java.awt.Dimension(110, 18));
        jPanel24.add(jLabel134);

        jComboBox8.setBackground(new java.awt.Color(153, 255, 255));
        jComboBox8.setMaximumSize(new java.awt.Dimension(49, 24));
        jComboBox8.setMinimumSize(new java.awt.Dimension(49, 24));
        jComboBox8.setPreferredSize(new java.awt.Dimension(49, 24));
        jPanel24.add(jComboBox8);

        jLabel135.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(0, 0, 255));
        jLabel135.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel135.setText("Менеджер:");
        jLabel135.setMaximumSize(new java.awt.Dimension(70, 18));
        jLabel135.setMinimumSize(new java.awt.Dimension(70, 18));
        jLabel135.setPreferredSize(new java.awt.Dimension(70, 18));
        jPanel24.add(jLabel135);

        jComboBox11.setMaximumSize(new java.awt.Dimension(135, 24));
        jComboBox11.setMinimumSize(new java.awt.Dimension(135, 24));
        jComboBox11.setPreferredSize(new java.awt.Dimension(135, 24));
        jPanel24.add(jComboBox11);

        jPanel23.add(jPanel24);

        jPanel105.setBackground(new java.awt.Color(204, 255, 255));
        jPanel105.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel105.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel105.setPreferredSize(new java.awt.Dimension(345, 26));
        jPanel105.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 1));

        jLabel100.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(0, 0, 255));
        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel100.setText("Дата обращения:");
        jLabel100.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel100.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel100.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel100.setRequestFocusEnabled(false);
        jPanel105.add(jLabel100);

        jLabel74.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(200, 0, 0));
        jLabel74.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel74.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel74.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel105.add(jLabel74);

        jDateChooser5.setOpaque(false);
        jDateChooser5.setPreferredSize(new java.awt.Dimension(120, 24));
        jPanel105.add(jDateChooser5);

        jPanel23.add(jPanel105);

        jPanel25.setBackground(new java.awt.Color(153, 255, 255));
        jPanel25.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel25.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel25.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel27.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 0, 255));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Контактное лицо 1:");
        jLabel27.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel27.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel27.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel27.setRequestFocusEnabled(false);
        jPanel25.add(jLabel27);

        jTextField6.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField6.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField6.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel25.add(jTextField6);

        jPanel23.add(jPanel25);

        jPanel26.setBackground(new java.awt.Color(153, 255, 255));
        jPanel26.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel26.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel26.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel31.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 255));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Осн. телефон:");
        jLabel31.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel31.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel31.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel31.setRequestFocusEnabled(false);
        jPanel26.add(jLabel31);

        jTextField7.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField7.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField7.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel26.add(jTextField7);

        jPanel23.add(jPanel26);

        jPanel55.setBackground(new java.awt.Color(153, 255, 255));
        jPanel55.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel55.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel55.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel55.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel33.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 0, 255));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Доп. телефоны:");
        jLabel33.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel33.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel33.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel33.setRequestFocusEnabled(false);
        jPanel55.add(jLabel33);

        jTextField25.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField25.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField25.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel55.add(jTextField25);

        jPanel23.add(jPanel55);

        jPanel60.setBackground(new java.awt.Color(153, 255, 255));
        jPanel60.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel60.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel60.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel60.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel63.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(0, 0, 255));
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel63.setText("Телефон-факс:");
        jLabel63.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel63.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel63.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel63.setRequestFocusEnabled(false);
        jPanel60.add(jLabel63);

        jTextField52.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField52.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField52.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel60.add(jTextField52);

        jPanel23.add(jPanel60);

        jPanel32.setBackground(new java.awt.Color(153, 255, 255));
        jPanel32.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel32.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel32.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel36.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 0, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("E-mail:");
        jLabel36.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel36.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel36.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel36.setRequestFocusEnabled(false);
        jPanel32.add(jLabel36);

        jTextField13.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField13.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField13.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel32.add(jTextField13);

        jPanel23.add(jPanel32);

        jPanel28.setBackground(new java.awt.Color(204, 255, 255));
        jPanel28.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel28.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel28.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel34.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 0, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("Контактное лицо 2:");
        jLabel34.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel34.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel34.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel34.setRequestFocusEnabled(false);
        jPanel28.add(jLabel34);

        jTextField8.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField8.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField8.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel28.add(jTextField8);

        jPanel23.add(jPanel28);

        jPanel31.setBackground(new java.awt.Color(204, 255, 255));
        jPanel31.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel31.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel31.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel35.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 0, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel35.setText("Осн. телефон:");
        jLabel35.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel35.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel35.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel35.setRequestFocusEnabled(false);
        jPanel31.add(jLabel35);

        jTextField11.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField11.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField11.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel31.add(jTextField11);

        jPanel23.add(jPanel31);

        jPanel56.setBackground(new java.awt.Color(204, 255, 255));
        jPanel56.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel56.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel56.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel56.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel51.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 0, 255));
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel51.setText("Доп. телефоны:");
        jLabel51.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel51.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel51.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel51.setRequestFocusEnabled(false);
        jPanel56.add(jLabel51);

        jTextField34.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField34.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField34.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel56.add(jTextField34);

        jPanel23.add(jPanel56);

        jPanel108.setBackground(new java.awt.Color(204, 255, 255));
        jPanel108.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel108.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel108.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel108.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel102.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(0, 0, 255));
        jLabel102.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel102.setText("E-mail:");
        jLabel102.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel102.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel102.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel102.setRequestFocusEnabled(false);
        jPanel108.add(jLabel102);

        jTextField26.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField26.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField26.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel108.add(jTextField26);

        jPanel23.add(jPanel108);

        jPanel109.setBackground(new java.awt.Color(153, 255, 255));
        jPanel109.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel109.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel109.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel109.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel37.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 0, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("Контактное лицо 3:");
        jLabel37.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel37.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel37.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel37.setRequestFocusEnabled(false);
        jPanel109.add(jLabel37);

        jTextField27.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField27.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField27.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel109.add(jTextField27);

        jPanel23.add(jPanel109);

        jPanel110.setBackground(new java.awt.Color(153, 255, 255));
        jPanel110.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel110.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel110.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel110.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel39.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(0, 0, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Осн. телефон:");
        jLabel39.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel39.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel39.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel39.setRequestFocusEnabled(false);
        jPanel110.add(jLabel39);

        jTextField28.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField28.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField28.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel110.add(jTextField28);

        jPanel23.add(jPanel110);

        jPanel58.setBackground(new java.awt.Color(153, 255, 255));
        jPanel58.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel58.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel58.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel58.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel54.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 0, 255));
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel54.setText("Доп. телефоны:");
        jLabel54.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel54.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel54.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel54.setRequestFocusEnabled(false);
        jPanel58.add(jLabel54);

        jTextField35.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField35.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField35.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel58.add(jTextField35);

        jPanel23.add(jPanel58);

        jPanel111.setBackground(new java.awt.Color(153, 255, 255));
        jPanel111.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel111.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel111.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel111.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel103.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(0, 0, 255));
        jLabel103.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel103.setText("E-mail:");
        jLabel103.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel103.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel103.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel103.setRequestFocusEnabled(false);
        jPanel111.add(jLabel103);

        jTextField29.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField29.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField29.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel111.add(jTextField29);

        jPanel23.add(jPanel111);

        jPanel35.setBackground(new java.awt.Color(204, 255, 255));
        jPanel35.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel35.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel35.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel40.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 0, 255));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Адрес:");
        jLabel40.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel40.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel40.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel40.setRequestFocusEnabled(false);
        jPanel35.add(jLabel40);

        jTextField15.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField15.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField15.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField15.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel35.add(jTextField15);

        jPanel23.add(jPanel35);

        jPanel33.setBackground(new java.awt.Color(153, 255, 255));
        jPanel33.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel33.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel33.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel38.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 0, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Site:");
        jLabel38.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel38.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel38.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel38.setRequestFocusEnabled(false);
        jPanel33.add(jLabel38);

        jTextField14.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField14.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField14.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel33.add(jTextField14);

        jPanel23.add(jPanel33);

        jPanel34.setBackground(new java.awt.Color(204, 255, 255));
        jPanel34.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel34.setMinimumSize(new java.awt.Dimension(0, 25));
        jPanel34.setPreferredSize(new java.awt.Dimension(345, 25));
        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel41.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 0, 255));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("ЕГРПОУ:");
        jLabel41.setMaximumSize(new java.awt.Dimension(110, 18));
        jLabel41.setMinimumSize(new java.awt.Dimension(110, 18));
        jLabel41.setPreferredSize(new java.awt.Dimension(110, 18));
        jLabel41.setRequestFocusEnabled(false);
        jPanel34.add(jLabel41);

        jTextField22.setMaximumSize(new java.awt.Dimension(280, 18));
        jTextField22.setMinimumSize(new java.awt.Dimension(280, 18));
        jTextField22.setPreferredSize(new java.awt.Dimension(280, 18));
        jPanel34.add(jTextField22);

        jPanel23.add(jPanel34);

        jPanel36.setBackground(new java.awt.Color(153, 255, 255));
        jPanel36.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel36.setMinimumSize(new java.awt.Dimension(0, 50));
        jPanel36.setPreferredSize(new java.awt.Dimension(345, 100));
        jPanel36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel42.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 0, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Комментарий:");
        jLabel42.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel42.setMaximumSize(new java.awt.Dimension(110, 90));
        jLabel42.setMinimumSize(new java.awt.Dimension(110, 90));
        jLabel42.setPreferredSize(new java.awt.Dimension(110, 90));
        jLabel42.setRequestFocusEnabled(false);
        jPanel36.add(jLabel42);

        jScrollPane4.setBorder(null);
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setToolTipText("");
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane4.setPreferredSize(new java.awt.Dimension(280, 90));

        jTextArea7.setColumns(20);
        jTextArea7.setRows(5);
        jScrollPane4.setViewportView(jTextArea7);

        jPanel36.add(jScrollPane4);

        jPanel23.add(jPanel36);

        jPanel115.setBackground(new java.awt.Color(204, 255, 255));
        jPanel115.setLayout(new java.awt.BorderLayout());
        jPanel23.add(jPanel115);

        jTabbedPane3.addTab("Данные клиента", jPanel23);

        jPanel91.setBackground(new java.awt.Color(204, 255, 255));
        jPanel91.setLayout(new javax.swing.BoxLayout(jPanel91, javax.swing.BoxLayout.Y_AXIS));

        jScrollPane11.setPreferredSize(new java.awt.Dimension(370, 402));

        jTable4.setBackground(new java.awt.Color(153, 255, 255));
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable4.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane11.setViewportView(jTable4);

        jPanel91.add(jScrollPane11);

        jTabbedPane3.addTab("Заказы", jPanel91);

        jPanel122.add(jTabbedPane3, "card2");

        jPanel195.setLayout(new java.awt.BorderLayout());

        jScrollPane26.setPreferredSize(new java.awt.Dimension(370, 402));

        jTable10.setBackground(new java.awt.Color(153, 255, 255));
        jTable10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable10.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane26.setViewportView(jTable10);

        jPanel195.add(jScrollPane26, java.awt.BorderLayout.CENTER);

        jPanel196.setBackground(new java.awt.Color(153, 255, 255));
        jPanel196.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel196.setMinimumSize(new java.awt.Dimension(0, 30));
        jPanel196.setPreferredSize(new java.awt.Dimension(973, 30));
        jPanel196.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        jDateChooser13.setBackground(new java.awt.Color(204, 255, 255));
        jDateChooser13.setDateFormatString("dd MMM yyyy");
        jDateChooser13.setPreferredSize(new java.awt.Dimension(130, 24));
        jPanel196.add(jDateChooser13);

        jLabel133.setText("-");
        jPanel196.add(jLabel133);

        jDateChooser14.setBackground(new java.awt.Color(204, 255, 255));
        jDateChooser14.setDateFormatString("dd MMM yyyy");
        jDateChooser14.setPreferredSize(new java.awt.Dimension(130, 24));
        jPanel196.add(jDateChooser14);

        jButton128.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton128.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton128.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton128.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton128.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton128ActionPerformed(evt);
            }
        });
        jPanel196.add(jButton128);

        jPanel195.add(jPanel196, java.awt.BorderLayout.NORTH);

        jPanel122.add(jPanel195, "card3");

        jPanel7.add(jPanel122, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("КЛИЕНТЫ", new javax.swing.ImageIcon(getClass().getResource("/Images/Office-Client-Male-Dark-icon.png")), jPanel7); // NOI18N

        jPanel9.setBackground(new java.awt.Color(153, 255, 255));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel64.setMaximumSize(new java.awt.Dimension(320, 2147483647));
        jPanel64.setMinimumSize(new java.awt.Dimension(320, 23));
        jPanel64.setPreferredSize(new java.awt.Dimension(330, 2));
        jPanel64.setLayout(new java.awt.BorderLayout());

        jPanel65.setBackground(new java.awt.Color(153, 255, 255));
        jPanel65.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel65.setMinimumSize(new java.awt.Dimension(10, 100));
        jPanel65.setPreferredSize(new java.awt.Dimension(10, 240));
        jPanel65.setLayout(new java.awt.BorderLayout());

        jPanel162.setBackground(new java.awt.Color(204, 255, 255));
        jPanel162.setMaximumSize(new java.awt.Dimension(190, 32767));
        jPanel162.setPreferredSize(new java.awt.Dimension(327, 240));
        jPanel162.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jPanel170.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "сортировка", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10))); // NOI18N
        jPanel170.setOpaque(false);
        jPanel170.setPreferredSize(new java.awt.Dimension(115, 210));
        jPanel170.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 2));

        buttonGroup7.add(jRadioButton6);
        jRadioButton6.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jRadioButton6.setForeground(new java.awt.Color(0, 0, 255));
        jRadioButton6.setSelected(true);
        jRadioButton6.setText("дата заказа");
        jRadioButton6.setOpaque(false);
        jRadioButton6.setPreferredSize(new java.awt.Dimension(100, 20));
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton6ActionPerformed(evt);
            }
        });
        jPanel170.add(jRadioButton6);

        buttonGroup7.add(jRadioButton7);
        jRadioButton7.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jRadioButton7.setForeground(new java.awt.Color(0, 0, 255));
        jRadioButton7.setText("дата доставки");
        jRadioButton7.setOpaque(false);
        jRadioButton7.setPreferredSize(new java.awt.Dimension(100, 20));
        jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton7ActionPerformed(evt);
            }
        });
        jPanel170.add(jRadioButton7);

        jPanel61.setBackground(new java.awt.Color(204, 255, 255));
        jPanel61.setPreferredSize(new java.awt.Dimension(100, 15));
        jPanel170.add(jPanel61);

        jLabel146.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jLabel146.setForeground(new java.awt.Color(0, 0, 255));
        jLabel146.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel146.setText("на сумму");
        jLabel146.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel146.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel146.setPreferredSize(new java.awt.Dimension(100, 15));
        jPanel170.add(jLabel146);

        jLabel144.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel144.setForeground(new java.awt.Color(0, 0, 255));
        jLabel144.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel144.setText("от");
        jLabel144.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel144.setMinimumSize(new java.awt.Dimension(10, 15));
        jLabel144.setPreferredSize(new java.awt.Dimension(20, 15));
        jPanel170.add(jLabel144);

        jTextField57.setToolTipText("");
        jTextField57.setMaximumSize(new java.awt.Dimension(135, 24));
        jTextField57.setMinimumSize(new java.awt.Dimension(70, 24));
        jTextField57.setPreferredSize(new java.awt.Dimension(70, 24));
        jTextField57.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField57KeyReleased(evt);
            }
        });
        jPanel170.add(jTextField57);

        jLabel145.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel145.setForeground(new java.awt.Color(0, 0, 255));
        jLabel145.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel145.setText("до");
        jLabel145.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel145.setMinimumSize(new java.awt.Dimension(10, 15));
        jLabel145.setPreferredSize(new java.awt.Dimension(20, 15));
        jPanel170.add(jLabel145);

        jTextField58.setMaximumSize(new java.awt.Dimension(135, 24));
        jTextField58.setMinimumSize(new java.awt.Dimension(70, 24));
        jTextField58.setPreferredSize(new java.awt.Dimension(70, 24));
        jTextField58.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField58KeyReleased(evt);
            }
        });
        jPanel170.add(jTextField58);

        jPanel162.add(jPanel170);

        jPanel171.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "фильтр", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 10))); // NOI18N
        jPanel171.setOpaque(false);
        jPanel171.setPreferredSize(new java.awt.Dimension(212, 210));
        java.awt.GridBagLayout jPanel171Layout = new java.awt.GridBagLayout();
        jPanel171Layout.columnWidths = new int[] {0, 5, 0, 5, 0, 5, 0};
        jPanel171Layout.rowHeights = new int[] {0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0};
        jPanel171.setLayout(jPanel171Layout);

        jLabel93.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(0, 0, 255));
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel93.setText("статус");
        jLabel93.setToolTipText("");
        jLabel93.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel93.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel93.setPreferredSize(new java.awt.Dimension(57, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel171.add(jLabel93, gridBagConstraints);

        jPanel107.setBackground(new java.awt.Color(204, 255, 255));
        jPanel107.setMaximumSize(new java.awt.Dimension(2147483647, 25));
        jPanel107.setMinimumSize(new java.awt.Dimension(50, 25));
        jPanel107.setPreferredSize(new java.awt.Dimension(50, 25));
        jPanel107.setLayout(new java.awt.BorderLayout());

        jComboBox1.setBackground(new java.awt.Color(153, 255, 255));
        jComboBox1.setMaximumSize(new java.awt.Dimension(49, 20));
        jComboBox1.setMinimumSize(new java.awt.Dimension(49, 20));
        jComboBox1.setPreferredSize(new java.awt.Dimension(49, 20));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel107.add(jComboBox1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel171.add(jPanel107, gridBagConstraints);

        jLabel108.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel108.setForeground(new java.awt.Color(0, 0, 255));
        jLabel108.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel108.setText("год");
        jLabel108.setMaximumSize(new java.awt.Dimension(20, 15));
        jLabel108.setMinimumSize(new java.awt.Dimension(20, 15));
        jLabel108.setPreferredSize(new java.awt.Dimension(20, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanel171.add(jLabel108, gridBagConstraints);

        jComboBox4.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jComboBox4.setMaximumSize(new java.awt.Dimension(60, 24));
        jComboBox4.setMinimumSize(new java.awt.Dimension(60, 24));
        jComboBox4.setPreferredSize(new java.awt.Dimension(60, 24));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        jPanel171.add(jComboBox4, gridBagConstraints);

        jLabel107.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel107.setForeground(new java.awt.Color(0, 0, 255));
        jLabel107.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel107.setText("менеджер");
        jLabel107.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel107.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel107.setPreferredSize(new java.awt.Dimension(57, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        jPanel171.add(jLabel107, gridBagConstraints);

        jTextField39.setMaximumSize(new java.awt.Dimension(135, 24));
        jTextField39.setMinimumSize(new java.awt.Dimension(135, 24));
        jTextField39.setPreferredSize(new java.awt.Dimension(125, 24));
        jTextField39.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField39KeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        jPanel171.add(jTextField39, gridBagConstraints);

        jLabel137.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(0, 0, 255));
        jLabel137.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel137.setText("№ заказа");
        jLabel137.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel137.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel137.setPreferredSize(new java.awt.Dimension(57, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        jPanel171.add(jLabel137, gridBagConstraints);

        jTextField43.setMaximumSize(new java.awt.Dimension(135, 24));
        jTextField43.setMinimumSize(new java.awt.Dimension(135, 24));
        jTextField43.setPreferredSize(new java.awt.Dimension(125, 24));
        jTextField43.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField43KeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        jPanel171.add(jTextField43, gridBagConstraints);

        jLabel130.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel130.setForeground(new java.awt.Color(0, 0, 255));
        jLabel130.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel130.setText("клиент");
        jLabel130.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel130.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel130.setPreferredSize(new java.awt.Dimension(57, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel171.add(jLabel130, gridBagConstraints);

        jComboBox6.setMaximumSize(new java.awt.Dimension(135, 24));
        jComboBox6.setMinimumSize(new java.awt.Dimension(135, 24));
        jComboBox6.setPreferredSize(new java.awt.Dimension(125, 24));
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 5;
        jPanel171.add(jComboBox6, gridBagConstraints);

        jLabel138.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel138.setForeground(new java.awt.Color(0, 0, 255));
        jLabel138.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel138.setText("ЕДРПОУ");
        jLabel138.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel138.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel138.setPreferredSize(new java.awt.Dimension(57, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        jPanel171.add(jLabel138, gridBagConstraints);

        jTextField46.setMaximumSize(new java.awt.Dimension(135, 24));
        jTextField46.setMinimumSize(new java.awt.Dimension(135, 24));
        jTextField46.setPreferredSize(new java.awt.Dimension(125, 24));
        jTextField46.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField46KeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        jPanel171.add(jTextField46, gridBagConstraints);

        jLabel139.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel139.setForeground(new java.awt.Color(0, 0, 255));
        jLabel139.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel139.setText("набор");
        jLabel139.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel139.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel139.setPreferredSize(new java.awt.Dimension(57, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        jPanel171.add(jLabel139, gridBagConstraints);

        jTextField48.setMaximumSize(new java.awt.Dimension(135, 24));
        jTextField48.setMinimumSize(new java.awt.Dimension(135, 24));
        jTextField48.setPreferredSize(new java.awt.Dimension(125, 24));
        jTextField48.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField48KeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        jPanel171.add(jTextField48, gridBagConstraints);

        jLabel121.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel121.setForeground(new java.awt.Color(0, 0, 255));
        jLabel121.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel121.setText("оплата");
        jLabel121.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel121.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel121.setPreferredSize(new java.awt.Dimension(57, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        jPanel171.add(jLabel121, gridBagConstraints);

        jComboBoxPaymentTypesOrderList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "нал. предоплата", "нал. по факту", "безнал. ТОВ", "безнал. ФОП", "налож. платеж", "предопл. на карту", "ФОП Брукша", "ФОП Вацик", "ФОП Калиева" }));
        jComboBoxPaymentTypesOrderList.setSelectedIndex(0);
        jComboBoxPaymentTypesOrderList.setSelectedItem("");
        jComboBoxPaymentTypesOrderList.setMaximumSize(new java.awt.Dimension(135, 24));
        jComboBoxPaymentTypesOrderList.setMinimumSize(new java.awt.Dimension(135, 24));
        jComboBoxPaymentTypesOrderList.setPreferredSize(new java.awt.Dimension(125, 24));
        jComboBoxPaymentTypesOrderList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPaymentTypesOrderListActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 5;
        jPanel171.add(jComboBoxPaymentTypesOrderList, gridBagConstraints);

        jPanel162.add(jPanel171);

        jPanel65.add(jPanel162, java.awt.BorderLayout.CENTER);

        jPanel163.setBackground(new java.awt.Color(153, 255, 255));
        jPanel163.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel163.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel163.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 1));
        jPanel163.add(filler18);

        jButton22.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton22.setForeground(new java.awt.Color(255, 0, 0));
        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/order-history-icon2.png"))); // NOI18N
        jButton22.setText("+");
        jButton22.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton22.setMaximumSize(new java.awt.Dimension(65, 25));
        jButton22.setMinimumSize(new java.awt.Dimension(65, 25));
        jButton22.setOpaque(false);
        jButton22.setPreferredSize(new java.awt.Dimension(65, 25));
        jButton22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton22MousePressed(evt);
            }
        });
        jPanel163.add(jButton22);
        jPanel163.add(filler10);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton10.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton10.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton10.setOpaque(false);
        jButton10.setPreferredSize(new java.awt.Dimension(35, 25));
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton10MousePressed(evt);
            }
        });
        jPanel163.add(jButton10);

        jButton108.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton108.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton108.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton108.setOpaque(false);
        jButton108.setPreferredSize(new java.awt.Dimension(35, 25));
        jButton108.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton108MousePressed(evt);
            }
        });
        jPanel163.add(jButton108);

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png"))); // NOI18N
        jButton23.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton23.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton23.setOpaque(false);
        jButton23.setPreferredSize(new java.awt.Dimension(35, 25));
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jPanel163.add(jButton23);

        jPanel65.add(jPanel163, java.awt.BorderLayout.SOUTH);

        jPanel64.add(jPanel65, java.awt.BorderLayout.NORTH);

        jTable3.setBackground(new java.awt.Color(153, 255, 255));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable3.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jTable3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable3MousePressed(evt);
            }
        });
        jTable3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable3KeyReleased(evt);
            }
        });
        jScrollPane10.setViewportView(jTable3);

        jPanel64.add(jScrollPane10, java.awt.BorderLayout.CENTER);

        jPanel59.setBackground(new java.awt.Color(153, 255, 255));
        jPanel59.setMinimumSize(new java.awt.Dimension(100, 25));
        jPanel59.setPreferredSize(new java.awt.Dimension(300, 25));
        jPanel59.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        jButton20.setText("Подсчитать количество подарков и их стоимость");
        jButton20.setPreferredSize(new java.awt.Dimension(300, 21));
        jButton20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton20MouseClicked(evt);
            }
        });
        jPanel59.add(jButton20);

        jPanel64.add(jPanel59, java.awt.BorderLayout.PAGE_END);

        jPanel9.add(jPanel64, java.awt.BorderLayout.WEST);

        jTabbedPane4.setBackground(new java.awt.Color(204, 255, 255));

        jPanel151.setBackground(new java.awt.Color(204, 255, 255));
        jPanel151.setLayout(new java.awt.BorderLayout());

        jPanel85.setLayout(new javax.swing.BoxLayout(jPanel85, javax.swing.BoxLayout.Y_AXIS));

        jPanel150.setMaximumSize(new java.awt.Dimension(2147483647, 240));
        jPanel150.setMinimumSize(new java.awt.Dimension(235, 240));
        jPanel150.setPreferredSize(new java.awt.Dimension(580, 300));
        jPanel150.setLayout(new java.awt.BorderLayout());

        jPanel132.setBackground(new java.awt.Color(204, 255, 255));
        jPanel132.setBorder(javax.swing.BorderFactory.createTitledBorder("Оплата"));
        jPanel132.setForeground(new java.awt.Color(0, 0, 255));
        jPanel132.setPreferredSize(new java.awt.Dimension(250, 50));
        jPanel132.setLayout(new javax.swing.BoxLayout(jPanel132, javax.swing.BoxLayout.Y_AXIS));

        jPanel130.setMaximumSize(new java.awt.Dimension(32767, 18));
        jPanel130.setMinimumSize(new java.awt.Dimension(145, 18));
        jPanel130.setOpaque(false);
        jPanel130.setPreferredSize(new java.awt.Dimension(438, 18));
        jPanel130.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel82.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(200, 0, 0));
        jLabel82.setMaximumSize(new java.awt.Dimension(130, 14));
        jLabel82.setMinimumSize(new java.awt.Dimension(130, 14));
        jLabel82.setPreferredSize(new java.awt.Dimension(130, 14));
        jPanel130.add(jLabel82);

        jPanel132.add(jPanel130);

        jPanel124.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel124.setMinimumSize(new java.awt.Dimension(380, 25));
        jPanel124.setOpaque(false);
        jPanel124.setPreferredSize(new java.awt.Dimension(238, 25));
        jPanel124.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jLabel69.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(0, 0, 255));
        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel69.setText("Тип оплаты:");
        jLabel69.setMaximumSize(new java.awt.Dimension(95, 18));
        jLabel69.setMinimumSize(new java.awt.Dimension(95, 18));
        jLabel69.setPreferredSize(new java.awt.Dimension(75, 18));
        jPanel124.add(jLabel69);

        jLabel79.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(200, 0, 0));
        jLabel79.setMaximumSize(new java.awt.Dimension(135, 14));
        jLabel79.setMinimumSize(new java.awt.Dimension(135, 14));
        jLabel79.setPreferredSize(new java.awt.Dimension(135, 14));
        jPanel124.add(jLabel79);

        jComboBoxPaymentTypesOrderDetail.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "нал. предоплата", "нал. по факту", "безнал. ТОВ", "безнал. ФОП", "налож. платеж", "предопл. на карту", "ФОП Брукша", "ФОП Вацик", "ФОП Калиева" }));
        jComboBoxPaymentTypesOrderDetail.setMaximumSize(new java.awt.Dimension(130, 32767));
        jComboBoxPaymentTypesOrderDetail.setMinimumSize(new java.awt.Dimension(130, 18));
        jComboBoxPaymentTypesOrderDetail.setPreferredSize(new java.awt.Dimension(130, 20));
        jPanel124.add(jComboBoxPaymentTypesOrderDetail);

        jPanel132.add(jPanel124);

        jPanel129.setBackground(new java.awt.Color(204, 255, 255));
        jPanel129.setMaximumSize(new java.awt.Dimension(32767, 22));
        jPanel129.setMinimumSize(new java.awt.Dimension(155, 22));
        jPanel129.setOpaque(false);
        jPanel129.setPreferredSize(new java.awt.Dimension(438, 22));
        jPanel129.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jLabel76.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(0, 0, 255));
        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel76.setText("Предоплата:");
        jLabel76.setMaximumSize(new java.awt.Dimension(80, 18));
        jLabel76.setMinimumSize(new java.awt.Dimension(80, 18));
        jLabel76.setPreferredSize(new java.awt.Dimension(75, 18));
        jPanel129.add(jLabel76);

        jTextField33.setForeground(new java.awt.Color(200, 0, 0));
        jTextField33.setMaximumSize(new java.awt.Dimension(60, 18));
        jTextField33.setMinimumSize(new java.awt.Dimension(60, 18));
        jTextField33.setPreferredSize(new java.awt.Dimension(70, 18));
        jPanel129.add(jTextField33);

        jPanel132.add(jPanel129);

        jPanel133.setBackground(new java.awt.Color(204, 255, 255));
        jPanel133.setMaximumSize(new java.awt.Dimension(32767, 22));
        jPanel133.setOpaque(false);
        jPanel133.setPreferredSize(new java.awt.Dimension(438, 22));
        jPanel133.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jLabel94.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(0, 0, 255));
        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel94.setText("Скидка:");
        jLabel94.setMaximumSize(new java.awt.Dimension(80, 18));
        jLabel94.setMinimumSize(new java.awt.Dimension(80, 18));
        jLabel94.setPreferredSize(new java.awt.Dimension(75, 18));
        jPanel133.add(jLabel94);

        jTextField37.setForeground(new java.awt.Color(200, 0, 0));
        jTextField37.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextField37.setMaximumSize(new java.awt.Dimension(60, 18));
        jTextField37.setMinimumSize(new java.awt.Dimension(60, 18));
        jTextField37.setPreferredSize(new java.awt.Dimension(70, 18));
        jTextField37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField37ActionPerformed(evt);
            }
        });
        jPanel133.add(jTextField37);

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("грн.");
        jPanel133.add(jLabel25);

        jPanel132.add(jPanel133);

        jPanel169.setBackground(new java.awt.Color(204, 255, 255));
        jPanel169.setMaximumSize(new java.awt.Dimension(32767, 22));
        jPanel169.setMinimumSize(new java.awt.Dimension(155, 22));
        jPanel169.setOpaque(false);
        jPanel169.setPreferredSize(new java.awt.Dimension(438, 22));
        jPanel169.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jLabel113.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel113.setForeground(new java.awt.Color(0, 0, 255));
        jLabel113.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel113.setText("За доставку:");
        jLabel113.setMaximumSize(new java.awt.Dimension(80, 18));
        jLabel113.setMinimumSize(new java.awt.Dimension(80, 18));
        jLabel113.setPreferredSize(new java.awt.Dimension(75, 18));
        jPanel169.add(jLabel113);

        jTextField41.setForeground(new java.awt.Color(200, 0, 0));
        jTextField41.setMaximumSize(new java.awt.Dimension(60, 18));
        jTextField41.setMinimumSize(new java.awt.Dimension(60, 18));
        jTextField41.setPreferredSize(new java.awt.Dimension(70, 18));
        jPanel169.add(jTextField41);

        jLabel114.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel114.setForeground(new java.awt.Color(200, 0, 0));
        jLabel114.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel114.setMaximumSize(new java.awt.Dimension(80, 18));
        jLabel114.setMinimumSize(new java.awt.Dimension(80, 18));
        jLabel114.setPreferredSize(new java.awt.Dimension(75, 18));
        jPanel169.add(jLabel114);

        jPanel132.add(jPanel169);

        jPanel159.setBackground(new java.awt.Color(204, 255, 255));
        jPanel159.setMaximumSize(new java.awt.Dimension(32767, 22));
        jPanel159.setMinimumSize(new java.awt.Dimension(155, 22));
        jPanel159.setOpaque(false);
        jPanel159.setPreferredSize(new java.awt.Dimension(438, 22));
        jPanel159.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jLabel101.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(0, 0, 255));
        jLabel101.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel101.setText("Долг:");
        jLabel101.setMaximumSize(new java.awt.Dimension(80, 18));
        jLabel101.setMinimumSize(new java.awt.Dimension(80, 18));
        jLabel101.setPreferredSize(new java.awt.Dimension(75, 18));
        jPanel159.add(jLabel101);

        jLabel104.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel104.setForeground(new java.awt.Color(200, 0, 0));
        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel104.setMaximumSize(new java.awt.Dimension(80, 18));
        jLabel104.setMinimumSize(new java.awt.Dimension(80, 18));
        jLabel104.setPreferredSize(new java.awt.Dimension(75, 18));
        jPanel159.add(jLabel104);

        jPanel132.add(jPanel159);

        jPanel150.add(jPanel132, java.awt.BorderLayout.WEST);

        jPanel94.setBackground(new java.awt.Color(204, 255, 255));
        jPanel94.setBorder(javax.swing.BorderFactory.createTitledBorder("Паковка"));
        jPanel94.setForeground(new java.awt.Color(0, 0, 255));
        jPanel94.setLayout(new javax.swing.BoxLayout(jPanel94, javax.swing.BoxLayout.Y_AXIS));

        jPanel131.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel131.setMinimumSize(new java.awt.Dimension(145, 25));
        jPanel131.setOpaque(false);
        jPanel131.setPreferredSize(new java.awt.Dimension(438, 25));
        jPanel131.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel84.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(200, 0, 0));
        jLabel84.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel84.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel84.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel84.setPreferredSize(new java.awt.Dimension(130, 15));
        jPanel131.add(jLabel84);

        jPanel94.add(jPanel131);

        jScrollPane32.setBorder(null);
        jScrollPane32.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane32.setToolTipText("");
        jScrollPane32.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea9.setColumns(20);
        jTextArea9.setRows(5);
        jScrollPane32.setViewportView(jTextArea9);

        jPanel94.add(jScrollPane32);

        jPanel134.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel134.setMinimumSize(new java.awt.Dimension(145, 28));
        jPanel134.setOpaque(false);
        jPanel134.setPreferredSize(new java.awt.Dimension(438, 28));
        jPanel134.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 3));

        jButton74.setForeground(new java.awt.Color(0, 0, 255));
        jButton74.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton74.setText("печать...");
        jButton74.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton74.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton74.setMaximumSize(new java.awt.Dimension(85, 25));
        jButton74.setMinimumSize(new java.awt.Dimension(85, 25));
        jButton74.setOpaque(false);
        jButton74.setPreferredSize(new java.awt.Dimension(110, 25));
        jButton74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton74ActionPerformed(evt);
            }
        });
        jPanel134.add(jButton74);

        jButton78.setForeground(new java.awt.Color(0, 0, 255));
        jButton78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/upakovka_146_2.png"))); // NOI18N
        jButton78.setText("закрыть смену");
        jButton78.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton78.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton78.setMaximumSize(new java.awt.Dimension(85, 25));
        jButton78.setMinimumSize(new java.awt.Dimension(85, 25));
        jButton78.setOpaque(false);
        jButton78.setPreferredSize(new java.awt.Dimension(145, 25));
        jButton78.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton78MousePressed(evt);
            }
        });
        jPanel134.add(jButton78);

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton19.setText("печать этикетки на ящик");
        jButton19.setMaximumSize(new java.awt.Dimension(183, 24));
        jButton19.setMinimumSize(new java.awt.Dimension(183, 24));
        jButton19.setOpaque(false);
        jButton19.setPreferredSize(new java.awt.Dimension(200, 24));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jPanel134.add(jButton19);

        jPanel94.add(jPanel134);

        jPanel150.add(jPanel94, java.awt.BorderLayout.CENTER);

        jPanel125.setLayout(new java.awt.BorderLayout());

        jPanel148.setBackground(new java.awt.Color(204, 255, 255));
        jPanel148.setBorder(javax.swing.BorderFactory.createTitledBorder("Комментарий к заказу"));
        jPanel148.setMinimumSize(new java.awt.Dimension(26, 85));
        jPanel148.setPreferredSize(new java.awt.Dimension(26, 85));
        jPanel148.setLayout(new java.awt.BorderLayout());

        jScrollPane38.setBorder(null);
        jScrollPane38.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane38.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea8.setColumns(20);
        jTextArea8.setRows(5);
        jScrollPane38.setViewportView(jTextArea8);

        jPanel148.add(jScrollPane38, java.awt.BorderLayout.CENTER);

        jPanel125.add(jPanel148, java.awt.BorderLayout.CENTER);

        jPanel194.setBackground(new java.awt.Color(204, 255, 255));
        jPanel194.setBorder(javax.swing.BorderFactory.createTitledBorder("Заказ создал"));
        jPanel194.setPreferredSize(new java.awt.Dimension(150, 100));
        jPanel194.setLayout(new java.awt.BorderLayout());

        jScrollPane49.setBorder(null);
        jScrollPane49.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane49.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea12.setEditable(false);
        jTextArea12.setBackground(new java.awt.Color(204, 255, 255));
        jTextArea12.setColumns(20);
        jTextArea12.setLineWrap(true);
        jTextArea12.setRows(5);
        jTextArea12.setWrapStyleWord(true);
        jTextArea12.setBorder(null);
        jScrollPane49.setViewportView(jTextArea12);

        jPanel194.add(jScrollPane49, java.awt.BorderLayout.CENTER);

        jPanel125.add(jPanel194, java.awt.BorderLayout.WEST);

        jPanel150.add(jPanel125, java.awt.BorderLayout.NORTH);

        jPanel85.add(jPanel150);

        jPanel147.setBackground(new java.awt.Color(153, 255, 255));
        jPanel147.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel147.setPreferredSize(new java.awt.Dimension(10, 30));
        jPanel147.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));

        jPanel68.setBackground(new java.awt.Color(153, 255, 255));
        jPanel68.setPreferredSize(new java.awt.Dimension(150, 25));
        jPanel68.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        jButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton27.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton27.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton27.setOpaque(false);
        jButton27.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton27MousePressed(evt);
            }
        });
        jPanel68.add(jButton27);

        jButtonSaveOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Save-icon.png"))); // NOI18N
        jButtonSaveOrder.setMaximumSize(new java.awt.Dimension(33, 25));
        jButtonSaveOrder.setMinimumSize(new java.awt.Dimension(33, 25));
        jButtonSaveOrder.setOpaque(false);
        jButtonSaveOrder.setPreferredSize(new java.awt.Dimension(33, 25));
        jButtonSaveOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonSaveOrderMousePressed(evt);
            }
        });
        jButtonSaveOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveOrderActionPerformed(evt);
            }
        });
        jPanel68.add(jButtonSaveOrder);

        jButton29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton29.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton29.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton29.setOpaque(false);
        jButton29.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton29MousePressed(evt);
            }
        });
        jPanel68.add(jButton29);

        jPanel147.add(jPanel68);

        jButton11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 0, 0));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Add-icon.png"))); // NOI18N
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton11.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton11.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton11.setOpaque(false);
        jButton11.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton11MousePressed(evt);
            }
        });
        jPanel147.add(jButton11);

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png"))); // NOI18N
        jButton12.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton12.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton12.setOpaque(false);
        jButton12.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton12MousePressed(evt);
            }
        });
        jPanel147.add(jButton12);
        jPanel147.add(filler8);

        jButton100.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/exclamation-red-icon.png"))); // NOI18N
        jButton100.setText("<html>&larr;</html>");
        jButton100.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton100.setMargin(new java.awt.Insets(2, 3, 2, 3));
        jButton100.setMaximumSize(new java.awt.Dimension(60, 25));
        jButton100.setMinimumSize(new java.awt.Dimension(60, 25));
        jButton100.setOpaque(false);
        jButton100.setPreferredSize(new java.awt.Dimension(60, 25));
        jButton100.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton100MousePressed(evt);
            }
        });
        jPanel147.add(jButton100);

        jButton16.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton16.setText("<html>&rarr;&nbsp;</html>");
        jButton16.setActionCommand("<html>&rarr;</html>");
        jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton16.setIconTextGap(5);
        jButton16.setMargin(new java.awt.Insets(2, 3, 2, 3));
        jButton16.setMaximumSize(new java.awt.Dimension(60, 25));
        jButton16.setMinimumSize(new java.awt.Dimension(60, 25));
        jButton16.setOpaque(false);
        jButton16.setPreferredSize(new java.awt.Dimension(60, 25));
        jButton16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton16MousePressed(evt);
            }
        });
        jPanel147.add(jButton16);

        jPanel85.add(jPanel147);

        jPanel51.setBackground(new java.awt.Color(204, 255, 255));
        jPanel51.setPreferredSize(new java.awt.Dimension(492, 900));
        jPanel51.setLayout(new javax.swing.BoxLayout(jPanel51, javax.swing.BoxLayout.X_AXIS));

        jTable5.setBackground(new java.awt.Color(153, 255, 255));
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable5.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jTable5.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane12.setViewportView(jTable5);

        jPanel51.add(jScrollPane12);

        jPanel85.add(jPanel51);

        jPanel151.add(jPanel85, java.awt.BorderLayout.CENTER);

        jTabbedPane4.addTab("Состав заказа", jPanel151);

        jPanel50.setBackground(new java.awt.Color(153, 255, 255));
        jPanel50.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel50.setLayout(new java.awt.BorderLayout());

        jPanel96.setLayout(new java.awt.BorderLayout());

        jPanel57.setBackground(new java.awt.Color(153, 255, 255));
        jPanel57.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel57.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel57.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));
        jPanel57.add(filler13);

        jButton90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Add-icon.png"))); // NOI18N
        jButton90.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton90.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton90.setOpaque(false);
        jButton90.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton90.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton90MousePressed(evt);
            }
        });
        jPanel57.add(jButton90);

        jButton91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton91.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton91.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton91.setOpaque(false);
        jButton91.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton91.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton91MousePressed(evt);
            }
        });
        jPanel57.add(jButton91);

        jButton89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png"))); // NOI18N
        jButton89.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton89.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton89.setOpaque(false);
        jButton89.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton89.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton89MousePressed(evt);
            }
        });
        jPanel57.add(jButton89);

        jPanel96.add(jPanel57, java.awt.BorderLayout.NORTH);

        jScrollPane13.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        jTable14.setBackground(new java.awt.Color(204, 255, 255));
        jTable14.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable14.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable14.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jTable14.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane13.setViewportView(jTable14);

        jPanel96.add(jScrollPane13, java.awt.BorderLayout.CENTER);

        jPanel50.add(jPanel96, java.awt.BorderLayout.CENTER);

        jTabbedPane4.addTab("Доставка", jPanel50);

        jPanel9.add(jTabbedPane4, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("ЗАКАЗЫ  ", new javax.swing.ImageIcon(getClass().getResource("/Images/order-history-icon.png")), jPanel9); // NOI18N

        jPanel8.setBackground(new java.awt.Color(153, 255, 255));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jTabbedPane2.setBackground(new java.awt.Color(153, 255, 255));
        jTabbedPane2.setForeground(new java.awt.Color(0, 0, 255));
        jTabbedPane2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane2StateChanged(evt);
            }
        });

        jPanel113.setLayout(new java.awt.BorderLayout());

        jPanel153.setBackground(new java.awt.Color(204, 255, 255));
        jPanel153.setPreferredSize(new java.awt.Dimension(450, 100));
        jPanel153.setLayout(new java.awt.BorderLayout());

        jScrollPane18.setMaximumSize(new java.awt.Dimension(450, 100));
        jScrollPane18.setMinimumSize(new java.awt.Dimension(450, 100));
        jScrollPane18.setPreferredSize(new java.awt.Dimension(450, 100));

        jTree10.setBackground(new java.awt.Color(204, 255, 255));
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree10.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree10.setRootVisible(false);
        jTree10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree10MousePressed(evt);
            }
        });
        jTree10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTree10KeyReleased(evt);
            }
        });
        jScrollPane18.setViewportView(jTree10);

        jPanel153.add(jScrollPane18, java.awt.BorderLayout.CENTER);

        jPanel154.setBackground(new java.awt.Color(204, 255, 255));
        jPanel154.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel154.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel154.setPreferredSize(new java.awt.Dimension(450, 20));
        jPanel154.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 3));

        jLabel80.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(0, 0, 255));
        jLabel80.setText("Доступно");
        jLabel80.setPreferredSize(new java.awt.Dimension(80, 14));
        jPanel154.add(jLabel80);

        jLabel83.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(0, 0, 255));
        jLabel83.setText("Резерв");
        jLabel83.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel154.add(jLabel83);

        jLabel85.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(0, 0, 255));
        jLabel85.setText("На складе");
        jLabel85.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel154.add(jLabel85);
        jPanel154.add(filler11);

        jPanel153.add(jPanel154, java.awt.BorderLayout.NORTH);

        jPanel179.setBackground(new java.awt.Color(153, 255, 255));
        jPanel179.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel179.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel179.setPreferredSize(new java.awt.Dimension(270, 30));
        jPanel179.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 2));

        jLabel124.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Zoom-icon.png"))); // NOI18N
        jPanel179.add(jLabel124);

        jTextField55.setMinimumSize(new java.awt.Dimension(6, 26));
        jTextField55.setPreferredSize(new java.awt.Dimension(200, 26));
        jTextField55.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField55KeyReleased(evt);
            }
        });
        jPanel179.add(jTextField55);

        buttonGroup5.add(jToggleButton9);
        jToggleButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Letter-A-icon.png"))); // NOI18N
        jToggleButton9.setToolTipText("сортировка по алфавиту");
        jToggleButton9.setMaximumSize(new java.awt.Dimension(33, 25));
        jToggleButton9.setMinimumSize(new java.awt.Dimension(33, 25));
        jToggleButton9.setPreferredSize(new java.awt.Dimension(33, 25));
        jToggleButton9.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jToggleButton9StateChanged(evt);
            }
        });
        jPanel179.add(jToggleButton9);

        buttonGroup5.add(jToggleButton10);
        jToggleButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/coins-icon.png"))); // NOI18N
        jToggleButton10.setToolTipText("сортировка по алфавиту");
        jToggleButton10.setMaximumSize(new java.awt.Dimension(33, 25));
        jToggleButton10.setMinimumSize(new java.awt.Dimension(33, 25));
        jToggleButton10.setPreferredSize(new java.awt.Dimension(33, 25));
        jToggleButton10.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jToggleButton10StateChanged(evt);
            }
        });
        jPanel179.add(jToggleButton10);

        jPanel153.add(jPanel179, java.awt.BorderLayout.SOUTH);

        jPanel113.add(jPanel153, java.awt.BorderLayout.WEST);

        jPanel165.setBackground(new java.awt.Color(204, 255, 255));
        jPanel165.setLayout(new javax.swing.BoxLayout(jPanel165, javax.swing.BoxLayout.Y_AXIS));

        jPanel168.setBackground(new java.awt.Color(153, 255, 255));
        jPanel168.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel168.setMinimumSize(new java.awt.Dimension(10, 20));
        jPanel168.setPreferredSize(new java.awt.Dimension(73, 20));

        jLabel112.setForeground(new java.awt.Color(0, 0, 255));
        jLabel112.setText("СКЛАД");
        jPanel168.add(jLabel112);

        jPanel165.add(jPanel168);

        jTable6.setBackground(new java.awt.Color(153, 255, 255));
        jTable6.setForeground(new java.awt.Color(0, 0, 255));
        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable6.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane17.setViewportView(jTable6);

        jPanel165.add(jScrollPane17);

        jPanel166.setBackground(new java.awt.Color(153, 255, 255));
        jPanel166.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel166.setMinimumSize(new java.awt.Dimension(10, 20));
        jPanel166.setPreferredSize(new java.awt.Dimension(73, 20));

        jLabel110.setForeground(new java.awt.Color(0, 0, 255));
        jLabel110.setText("РЕЗЕРВ");
        jPanel166.add(jLabel110);

        jPanel165.add(jPanel166);

        jTable17.setBackground(new java.awt.Color(153, 255, 255));
        jTable17.setForeground(new java.awt.Color(0, 0, 255));
        jTable17.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable17.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane40.setViewportView(jTable17);

        jPanel165.add(jScrollPane40);

        jPanel113.add(jPanel165, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Конфеты", new javax.swing.ImageIcon(getClass().getResource("/Images/candy-icon.png")), jPanel113); // NOI18N

        jPanel114.setLayout(new java.awt.BorderLayout());

        jPanel155.setLayout(new java.awt.BorderLayout());

        jPanel156.setBackground(new java.awt.Color(204, 255, 255));
        jPanel156.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel156.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel156.setPreferredSize(new java.awt.Dimension(450, 20));
        jPanel156.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 3));

        jLabel86.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(0, 0, 255));
        jLabel86.setText("Доступно");
        jLabel86.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel156.add(jLabel86);

        jLabel87.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel87.setForeground(new java.awt.Color(0, 0, 255));
        jLabel87.setText("Резерв");
        jLabel87.setPreferredSize(new java.awt.Dimension(60, 14));
        jPanel156.add(jLabel87);

        jLabel88.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(0, 0, 255));
        jLabel88.setText("На складе");
        jLabel88.setPreferredSize(new java.awt.Dimension(70, 14));
        jPanel156.add(jLabel88);
        jPanel156.add(filler12);

        jPanel155.add(jPanel156, java.awt.BorderLayout.NORTH);

        jScrollPane19.setMaximumSize(new java.awt.Dimension(450, 100));
        jScrollPane19.setMinimumSize(new java.awt.Dimension(450, 100));
        jScrollPane19.setPreferredSize(new java.awt.Dimension(450, 100));

        jTree11.setBackground(new java.awt.Color(204, 255, 255));
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree11.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree11.setRootVisible(false);
        jTree11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree11MousePressed(evt);
            }
        });
        jTree11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTree11KeyReleased(evt);
            }
        });
        jScrollPane19.setViewportView(jTree11);

        jPanel155.add(jScrollPane19, java.awt.BorderLayout.CENTER);

        jPanel188.setBackground(new java.awt.Color(153, 255, 255));
        jPanel188.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel188.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel188.setPreferredSize(new java.awt.Dimension(270, 30));
        jPanel188.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        jLabel136.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Zoom-icon.png"))); // NOI18N
        jLabel136.setText("номер или имя упаковки");
        jPanel188.add(jLabel136);

        jTextField50.setMinimumSize(new java.awt.Dimension(6, 26));
        jTextField50.setPreferredSize(new java.awt.Dimension(250, 26));
        jTextField50.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField50KeyReleased(evt);
            }
        });
        jPanel188.add(jTextField50);

        jPanel155.add(jPanel188, java.awt.BorderLayout.SOUTH);

        jPanel114.add(jPanel155, java.awt.BorderLayout.WEST);

        jPanel157.setBackground(new java.awt.Color(204, 255, 255));
        jPanel157.setLayout(new javax.swing.BoxLayout(jPanel157, javax.swing.BoxLayout.Y_AXIS));

        jPanel167.setBackground(new java.awt.Color(153, 255, 255));
        jPanel167.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel167.setMinimumSize(new java.awt.Dimension(10, 20));
        jPanel167.setPreferredSize(new java.awt.Dimension(73, 20));

        jLabel111.setForeground(new java.awt.Color(0, 0, 255));
        jLabel111.setText("СКЛАД");
        jPanel167.add(jLabel111);

        jPanel157.add(jPanel167);

        jTable8.setBackground(new java.awt.Color(153, 255, 255));
        jTable8.setForeground(new java.awt.Color(0, 0, 255));
        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable8.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane23.setViewportView(jTable8);

        jPanel157.add(jScrollPane23);

        jPanel158.setBackground(new java.awt.Color(153, 255, 255));
        jPanel158.setMaximumSize(new java.awt.Dimension(32767, 20));
        jPanel158.setMinimumSize(new java.awt.Dimension(10, 20));
        jPanel158.setPreferredSize(new java.awt.Dimension(73, 20));

        jLabel96.setForeground(new java.awt.Color(0, 0, 255));
        jLabel96.setText("РЕЗЕРВ");
        jPanel158.add(jLabel96);

        jPanel157.add(jPanel158);

        jTable16.setBackground(new java.awt.Color(153, 255, 255));
        jTable16.setForeground(new java.awt.Color(0, 0, 255));
        jTable16.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable16.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane39.setViewportView(jTable16);

        jPanel157.add(jScrollPane39);

        jPanel114.add(jPanel157, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("Упаковка", new javax.swing.ImageIcon(getClass().getResource("/Images/upakovka_146_2.png")), jPanel114); // NOI18N

        jPanel88.setLayout(new java.awt.BorderLayout());

        jScrollPane24.setMaximumSize(new java.awt.Dimension(560, 32767));
        jScrollPane24.setMinimumSize(new java.awt.Dimension(560, 27));
        jScrollPane24.setPreferredSize(new java.awt.Dimension(560, 402));

        jTable9.setBackground(new java.awt.Color(153, 255, 255));
        jTable9.setForeground(new java.awt.Color(0, 0, 255));
        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable9.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane24.setViewportView(jTable9);

        jPanel88.add(jScrollPane24, java.awt.BorderLayout.WEST);

        jPanel116.setLayout(new javax.swing.BoxLayout(jPanel116, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane46.setMaximumSize(new java.awt.Dimension(425, 32767));
        jScrollPane46.setMinimumSize(new java.awt.Dimension(425, 27));
        jScrollPane46.setPreferredSize(new java.awt.Dimension(425, 402));

        jTable22.setBackground(new java.awt.Color(153, 255, 255));
        jTable22.setForeground(new java.awt.Color(0, 0, 255));
        jTable22.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable22.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jTable22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable22MouseClicked(evt);
            }
        });
        jScrollPane46.setViewportView(jTable22);

        jPanel116.add(jScrollPane46);

        jScrollPane14.setForeground(new java.awt.Color(0, 0, 255));

        jTable11.setBackground(new java.awt.Color(153, 255, 255));
        jTable11.setForeground(new java.awt.Color(0, 0, 255));
        jTable11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable11.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane14.setViewportView(jTable11);

        jPanel116.add(jScrollPane14);

        jPanel88.add(jPanel116, java.awt.BorderLayout.CENTER);

        jPanel89.setBackground(new java.awt.Color(204, 255, 255));
        jPanel89.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel89.setMinimumSize(new java.awt.Dimension(379, 30));
        jPanel89.setPreferredSize(new java.awt.Dimension(379, 30));
        jPanel89.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 3));
        jPanel89.add(filler9);

        jButton62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton62.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton62.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton62.setPreferredSize(new java.awt.Dimension(32, 25));
        jButton62.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton62MousePressed(evt);
            }
        });
        jPanel89.add(jButton62);

        jButton61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton61.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton61.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton61.setPreferredSize(new java.awt.Dimension(32, 25));
        jButton61.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton61MousePressed(evt);
            }
        });
        jPanel89.add(jButton61);

        jButton85.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Excel-icon.png"))); // NOI18N
        jButton85.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton85.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton85.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton85.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton85MousePressed(evt);
            }
        });
        jPanel89.add(jButton85);
        jPanel89.add(filler14);

        jButton105.setForeground(new java.awt.Color(0, 0, 255));
        jButton105.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton105.setText("лист инвентаризации");
        jButton105.setMargin(new java.awt.Insets(2, 6, 2, 6));
        jButton105.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton105.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton105.setPreferredSize(new java.awt.Dimension(190, 22));
        jButton105.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton105MousePressed(evt);
            }
        });
        jPanel89.add(jButton105);
        jPanel89.add(filler22);

        jButton117.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton117.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton117.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton117.setPreferredSize(new java.awt.Dimension(32, 25));
        jButton117.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton117MousePressed(evt);
            }
        });
        jPanel89.add(jButton117);

        jButton118.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton118.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton118.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton118.setPreferredSize(new java.awt.Dimension(32, 25));
        jButton118.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton118MousePressed(evt);
            }
        });
        jPanel89.add(jButton118);

        jButton119.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Excel-icon.png"))); // NOI18N
        jButton119.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton119.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton119.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton119.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton119MousePressed(evt);
            }
        });
        jPanel89.add(jButton119);

        jPanel88.add(jPanel89, java.awt.BorderLayout.PAGE_START);

        jTabbedPane2.addTab("Заказ для склада", new javax.swing.ImageIcon(getClass().getResource("/Images/shopping-cart-icon.png")), jPanel88); // NOI18N

        jPanel8.add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        jPanel90.setBackground(new java.awt.Color(153, 255, 255));
        jPanel90.setMaximumSize(new java.awt.Dimension(32767, 65));
        jPanel90.setMinimumSize(new java.awt.Dimension(819, 65));
        jPanel90.setPreferredSize(new java.awt.Dimension(789, 65));
        jPanel90.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 3));

        jButton71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton71.setMaximumSize(new java.awt.Dimension(63, 25));
        jButton71.setMinimumSize(new java.awt.Dimension(63, 25));
        jButton71.setPreferredSize(new java.awt.Dimension(63, 25));
        jButton71.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton71MousePressed(evt);
            }
        });
        jPanel90.add(jButton71);

        jPanel192.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Приход/расход"));
        jPanel192.setMaximumSize(new java.awt.Dimension(170, 55));
        jPanel192.setMinimumSize(new java.awt.Dimension(170, 55));
        jPanel192.setOpaque(false);
        jPanel192.setPreferredSize(new java.awt.Dimension(170, 55));
        jPanel192.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        jButton72.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton72.setForeground(new java.awt.Color(204, 0, 0));
        jButton72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/lock-icon2.png"))); // NOI18N
        jButton72.setText("+");
        jButton72.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton72.setMaximumSize(new java.awt.Dimension(63, 25));
        jButton72.setMinimumSize(new java.awt.Dimension(63, 25));
        jButton72.setOpaque(false);
        jButton72.setPreferredSize(new java.awt.Dimension(63, 25));
        jButton72.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton72MousePressed(evt);
            }
        });
        jPanel192.add(jButton72);

        jButton52.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton52.setForeground(new java.awt.Color(0, 0, 255));
        jButton52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/lock-icon2.png"))); // NOI18N
        jButton52.setText("-");
        jButton52.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton52.setMaximumSize(new java.awt.Dimension(63, 25));
        jButton52.setMinimumSize(new java.awt.Dimension(63, 25));
        jButton52.setOpaque(false);
        jButton52.setPreferredSize(new java.awt.Dimension(63, 25));
        jButton52.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton52MousePressed(evt);
            }
        });
        jPanel192.add(jButton52);

        jPanel90.add(jPanel192);

        jPanel193.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Обнулить склад"));
        jPanel193.setMaximumSize(new java.awt.Dimension(300, 55));
        jPanel193.setMinimumSize(new java.awt.Dimension(300, 55));
        jPanel193.setOpaque(false);
        jPanel193.setPreferredSize(new java.awt.Dimension(385, 55));
        jPanel193.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        jButton120.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button-warning-icon.png"))); // NOI18N
        jButton120.setText("1 позицию");
        jButton120.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton120.setMaximumSize(new java.awt.Dimension(130, 25));
        jButton120.setMinimumSize(new java.awt.Dimension(110, 25));
        jButton120.setOpaque(false);
        jButton120.setPreferredSize(new java.awt.Dimension(110, 25));
        jButton120.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton120MousePressed(evt);
            }
        });
        jPanel193.add(jButton120);

        jButton116.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button-warning-icon.png"))); // NOI18N
        jButton116.setText("по конфетам");
        jButton116.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton116.setMaximumSize(new java.awt.Dimension(130, 25));
        jButton116.setMinimumSize(new java.awt.Dimension(100, 25));
        jButton116.setOpaque(false);
        jButton116.setPreferredSize(new java.awt.Dimension(120, 25));
        jButton116.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton116MousePressed(evt);
            }
        });
        jPanel193.add(jButton116);

        jButton126.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Button-warning-icon.png"))); // NOI18N
        jButton126.setText("по упаковке");
        jButton126.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton126.setMaximumSize(new java.awt.Dimension(130, 25));
        jButton126.setMinimumSize(new java.awt.Dimension(120, 25));
        jButton126.setOpaque(false);
        jButton126.setPreferredSize(new java.awt.Dimension(120, 25));
        jButton126.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton126MousePressed(evt);
            }
        });
        jPanel193.add(jButton126);

        jPanel90.add(jPanel193);
        jPanel90.add(filler24);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jRadioButton1.setForeground(new java.awt.Color(0, 0, 255));
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("последние 30");
        jRadioButton1.setOpaque(false);
        jPanel90.add(jRadioButton1);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jRadioButton2.setForeground(new java.awt.Color(0, 0, 255));
        jRadioButton2.setText("период:");
        jRadioButton2.setOpaque(false);
        jPanel90.add(jRadioButton2);

        jDateChooser3.setBackground(new java.awt.Color(204, 255, 255));
        jDateChooser3.setDateFormatString("dd MMM yyyy");
        jDateChooser3.setPreferredSize(new java.awt.Dimension(130, 24));
        jPanel90.add(jDateChooser3);

        jLabel12.setText("-");
        jPanel90.add(jLabel12);

        jDateChooser4.setBackground(new java.awt.Color(204, 255, 255));
        jDateChooser4.setDateFormatString("dd MMM yyyy");
        jDateChooser4.setPreferredSize(new java.awt.Dimension(130, 24));
        jPanel90.add(jDateChooser4);

        jButton44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton44.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton44.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton44.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton44MousePressed(evt);
            }
        });
        jPanel90.add(jButton44);

        jPanel8.add(jPanel90, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addTab("СКЛАД    ", new javax.swing.ImageIcon(getClass().getResource("/Images/lock-icon.png")), jPanel8); // NOI18N

        jPanel19.setBackground(new java.awt.Color(153, 255, 255));
        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.Y_AXIS));

        jPanel99.setBackground(new java.awt.Color(153, 255, 255));
        jPanel99.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel99.setMinimumSize(new java.awt.Dimension(366, 100));
        jPanel99.setPreferredSize(new java.awt.Dimension(646, 200));
        jPanel99.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 0));

        jPanel138.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "фильтр", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 11))); // NOI18N
        jPanel138.setMinimumSize(new java.awt.Dimension(410, 71));
        jPanel138.setOpaque(false);
        jPanel138.setPreferredSize(new java.awt.Dimension(410, 200));
        java.awt.GridBagLayout jPanel138Layout = new java.awt.GridBagLayout();
        jPanel138Layout.columnWidths = new int[] {0, 5, 0, 5, 0, 5, 0};
        jPanel138Layout.rowHeights = new int[] {0, 3, 0, 3, 0, 3, 0, 3, 0};
        jPanel138.setLayout(jPanel138Layout);

        buttonGroup6.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jRadioButton3.setForeground(new java.awt.Color(0, 0, 255));
        jRadioButton3.setSelected(true);
        jRadioButton3.setText("год");
        jRadioButton3.setOpaque(false);
        jRadioButton3.setPreferredSize(new java.awt.Dimension(50, 23));
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel138.add(jRadioButton3, gridBagConstraints);

        jComboBox5.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jComboBox5.setPreferredSize(new java.awt.Dimension(65, 20));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel138.add(jComboBox5, gridBagConstraints);

        buttonGroup6.add(jRadioButton4);
        jRadioButton4.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jRadioButton4.setForeground(new java.awt.Color(0, 0, 255));
        jRadioButton4.setText("дата");
        jRadioButton4.setOpaque(false);
        jRadioButton4.setPreferredSize(new java.awt.Dimension(52, 23));
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel138.add(jRadioButton4, gridBagConstraints);

        jDateChooser7.setDateFormatString("dd MMM yyyy");
        jDateChooser7.setMaximumSize(new java.awt.Dimension(125, 24));
        jDateChooser7.setMinimumSize(new java.awt.Dimension(125, 24));
        jDateChooser7.setPreferredSize(new java.awt.Dimension(125, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel138.add(jDateChooser7, gridBagConstraints);

        jLabel131.setForeground(new java.awt.Color(0, 0, 255));
        jLabel131.setText("тип");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel138.add(jLabel131, gridBagConstraints);

        jComboBox3.setBackground(new java.awt.Color(153, 255, 255));
        jComboBox3.setMaximumSize(new java.awt.Dimension(50, 24));
        jComboBox3.setMinimumSize(new java.awt.Dimension(50, 24));
        jComboBox3.setPreferredSize(new java.awt.Dimension(50, 24));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel138.add(jComboBox3, gridBagConstraints);

        jLabel132.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel132.setForeground(new java.awt.Color(0, 0, 255));
        jLabel132.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel132.setText("менеджер");
        jLabel132.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel132.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel132.setPreferredSize(new java.awt.Dimension(57, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel138.add(jLabel132, gridBagConstraints);

        jComboBox7.setMaximumSize(new java.awt.Dimension(135, 24));
        jComboBox7.setMinimumSize(new java.awt.Dimension(135, 24));
        jComboBox7.setPreferredSize(new java.awt.Dimension(135, 24));
        jComboBox7.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox7ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel138.add(jComboBox7, gridBagConstraints);

        jLabel140.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel140.setForeground(new java.awt.Color(0, 0, 255));
        jLabel140.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel140.setText("№ заказа");
        jLabel140.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel140.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel140.setPreferredSize(new java.awt.Dimension(57, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        jPanel138.add(jLabel140, gridBagConstraints);

        jTextField49.setMaximumSize(new java.awt.Dimension(135, 24));
        jTextField49.setMinimumSize(new java.awt.Dimension(135, 24));
        jTextField49.setPreferredSize(new java.awt.Dimension(135, 26));
        jTextField49.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField49KeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        jPanel138.add(jTextField49, gridBagConstraints);

        jLabel141.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel141.setForeground(new java.awt.Color(0, 0, 255));
        jLabel141.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel141.setText("вид дост.");
        jLabel141.setMaximumSize(new java.awt.Dimension(100, 15));
        jLabel141.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel141.setPreferredSize(new java.awt.Dimension(70, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel138.add(jLabel141, gridBagConstraints);

        jComboBox14.setMaximumSize(new java.awt.Dimension(135, 24));
        jComboBox14.setMinimumSize(new java.awt.Dimension(135, 24));
        jComboBox14.setPreferredSize(new java.awt.Dimension(135, 24));
        jComboBox14.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox14ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel138.add(jComboBox14, gridBagConstraints);

        jLabel142.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel142.setForeground(new java.awt.Color(0, 0, 255));
        jLabel142.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel142.setText("клиент");
        jLabel142.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel142.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel142.setPreferredSize(new java.awt.Dimension(57, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        jPanel138.add(jLabel142, gridBagConstraints);

        jTextField51.setMaximumSize(new java.awt.Dimension(135, 24));
        jTextField51.setMinimumSize(new java.awt.Dimension(135, 24));
        jTextField51.setPreferredSize(new java.awt.Dimension(135, 26));
        jTextField51.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField51KeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        jPanel138.add(jTextField51, gridBagConstraints);

        jLabel143.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel143.setForeground(new java.awt.Color(0, 0, 255));
        jLabel143.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel143.setText("экипаж");
        jLabel143.setMaximumSize(new java.awt.Dimension(57, 15));
        jLabel143.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel143.setPreferredSize(new java.awt.Dimension(57, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel138.add(jLabel143, gridBagConstraints);

        jComboBox16.setMaximumSize(new java.awt.Dimension(135, 24));
        jComboBox16.setMinimumSize(new java.awt.Dimension(135, 24));
        jComboBox16.setPreferredSize(new java.awt.Dimension(135, 24));
        jComboBox16.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox16ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel138.add(jComboBox16, gridBagConstraints);

        jComboBox17.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "нал. предоплата", "нал. по факту", "безнал. ТОВ", "безнал. ФОП", "налож. платеж", "предопл. на карту" }));
        jComboBox17.setMaximumSize(new java.awt.Dimension(135, 24));
        jComboBox17.setMinimumSize(new java.awt.Dimension(135, 24));
        jComboBox17.setPreferredSize(new java.awt.Dimension(125, 24));
        jComboBox17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox17ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        jPanel138.add(jComboBox17, gridBagConstraints);

        jLabel147.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel147.setForeground(new java.awt.Color(0, 0, 255));
        jLabel147.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel147.setText("оплата");
        jLabel147.setMaximumSize(new java.awt.Dimension(100, 15));
        jLabel147.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel147.setPreferredSize(new java.awt.Dimension(70, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel138.add(jLabel147, gridBagConstraints);

        jTextField59.setMaximumSize(new java.awt.Dimension(135, 24));
        jTextField59.setMinimumSize(new java.awt.Dimension(135, 24));
        jTextField59.setPreferredSize(new java.awt.Dimension(125, 24));
        jTextField59.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField59KeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        jPanel138.add(jTextField59, gridBagConstraints);

        jLabel148.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel148.setForeground(new java.awt.Color(0, 0, 255));
        jLabel148.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel148.setText("ЕДРПОУ");
        jLabel148.setMaximumSize(new java.awt.Dimension(100, 15));
        jLabel148.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel148.setPreferredSize(new java.awt.Dimension(70, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel138.add(jLabel148, gridBagConstraints);

        jLabel149.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel149.setForeground(new java.awt.Color(0, 0, 255));
        jLabel149.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel149.setText("ТТН");
        jLabel149.setMaximumSize(new java.awt.Dimension(100, 15));
        jLabel149.setMinimumSize(new java.awt.Dimension(57, 15));
        jLabel149.setPreferredSize(new java.awt.Dimension(70, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel138.add(jLabel149, gridBagConstraints);

        jTextField60.setMaximumSize(new java.awt.Dimension(135, 24));
        jTextField60.setMinimumSize(new java.awt.Dimension(135, 24));
        jTextField60.setPreferredSize(new java.awt.Dimension(125, 24));
        jTextField60.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField60KeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        jPanel138.add(jTextField60, gridBagConstraints);

        jPanel99.add(jPanel138);

        jButton81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton81.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton81.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton81.setOpaque(false);
        jButton81.setPreferredSize(new java.awt.Dimension(45, 32));
        jButton81.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton81MousePressed(evt);
            }
        });
        jPanel99.add(jButton81);

        jButton82.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton82.setText("<html>&rarr;</html>");
        jButton82.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton82.setMaximumSize(new java.awt.Dimension(2147483647, 32));
        jButton82.setMinimumSize(new java.awt.Dimension(47, 32));
        jButton82.setOpaque(false);
        jButton82.setPreferredSize(new java.awt.Dimension(70, 32));
        jButton82.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton82MousePressed(evt);
            }
        });
        jPanel99.add(jButton82);

        jPanel160.setOpaque(false);
        jPanel160.setPreferredSize(new java.awt.Dimension(190, 80));
        jPanel160.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        jButton97.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jButton97.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton97.setText("выделенные строки");
        jButton97.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton97.setMaximumSize(new java.awt.Dimension(32, 22));
        jButton97.setMinimumSize(new java.awt.Dimension(32, 22));
        jButton97.setOpaque(false);
        jButton97.setPreferredSize(new java.awt.Dimension(170, 25));
        jButton97.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton97MousePressed(evt);
            }
        });
        jPanel160.add(jButton97);

        jButton80.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jButton80.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton80.setText("все строки");
        jButton80.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton80.setMaximumSize(new java.awt.Dimension(75, 25));
        jButton80.setMinimumSize(new java.awt.Dimension(75, 25));
        jButton80.setOpaque(false);
        jButton80.setPreferredSize(new java.awt.Dimension(170, 25));
        jButton80.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton80MousePressed(evt);
            }
        });
        jPanel160.add(jButton80);

        jPanel99.add(jPanel160);

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/delivery.png"))); // NOI18N
        jButton18.setText("Экипажи");
        jButton18.setOpaque(false);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jPanel99.add(jButton18);

        jPanel19.add(jPanel99);

        jScrollPane29.setPreferredSize(new java.awt.Dimension(452, 380));

        jTable12.setBackground(new java.awt.Color(153, 255, 255));
        jTable12.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable12.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jTable12.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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

        jPanel19.add(jScrollPane29);

        jTabbedPane1.addTab("ДОСТАВКА", new javax.swing.ImageIcon(getClass().getResource("/Images/Lorry-icon2.png")), jPanel19); // NOI18N

        jPanel5.setBackground(new java.awt.Color(153, 255, 255));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel11.setBackground(new java.awt.Color(153, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.Y_AXIS));

        jPanel83.setBackground(new java.awt.Color(153, 255, 255));
        jPanel83.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel83.setMinimumSize(new java.awt.Dimension(366, 28));
        jPanel83.setPreferredSize(new java.awt.Dimension(366, 28));
        jPanel83.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 125, 3));

        jPanel27.setOpaque(false);
        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        jButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton17.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton17.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton17.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton17MousePressed(evt);
            }
        });
        jPanel27.add(jButton17);

        jButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Save-icon.png"))); // NOI18N
        jButton24.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton24.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton24.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton24MousePressed(evt);
            }
        });
        jPanel27.add(jButton24);

        jButton51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton51.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton51.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton51.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton51.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton51MousePressed(evt);
            }
        });
        jPanel27.add(jButton51);

        jPanel83.add(jPanel27);

        jPanel11.add(jPanel83);

        jPanel44.setBackground(new java.awt.Color(204, 255, 255));
        jPanel44.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel44.setMinimumSize(new java.awt.Dimension(206, 28));
        jPanel44.setPreferredSize(new java.awt.Dimension(480, 28));
        jPanel44.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel29.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 0, 255));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Имя:");
        jLabel29.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel29.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel29.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel44.add(jLabel29);

        jTextField9.setMaximumSize(new java.awt.Dimension(200, 2147483647));
        jTextField9.setMinimumSize(new java.awt.Dimension(200, 20));
        jTextField9.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanel44.add(jTextField9);

        jPanel11.add(jPanel44);

        jPanel62.setBackground(new java.awt.Color(153, 255, 255));
        jPanel62.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel62.setMinimumSize(new java.awt.Dimension(206, 28));
        jPanel62.setPreferredSize(new java.awt.Dimension(480, 28));
        jPanel62.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel75.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(0, 0, 255));
        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel75.setText("Логин:");
        jLabel75.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel75.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel75.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel62.add(jLabel75);

        jTextField10.setMaximumSize(new java.awt.Dimension(200, 2147483647));
        jTextField10.setMinimumSize(new java.awt.Dimension(200, 20));
        jTextField10.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanel62.add(jTextField10);

        jPanel11.add(jPanel62);

        jPanel76.setBackground(new java.awt.Color(204, 255, 255));
        jPanel76.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel76.setMinimumSize(new java.awt.Dimension(206, 28));
        jPanel76.setPreferredSize(new java.awt.Dimension(480, 28));
        jPanel76.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel77.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(0, 0, 255));
        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel77.setText("Пароль:");
        jLabel77.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel77.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel77.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel76.add(jLabel77);

        jTextField12.setMaximumSize(new java.awt.Dimension(200, 2147483647));
        jTextField12.setMinimumSize(new java.awt.Dimension(200, 20));
        jTextField12.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanel76.add(jTextField12);

        jLabel128.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel128.setForeground(new java.awt.Color(0, 0, 255));
        jLabel128.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel128.setText("Пароль к разделу финансов:");
        jLabel128.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel128.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel128.setPreferredSize(new java.awt.Dimension(185, 18));
        jPanel76.add(jLabel128);

        jLabel129.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel129.setForeground(new java.awt.Color(0, 0, 102));
        jLabel129.setText("<html><u>зашифрован, нажмите для изменения</u></html>");
        jLabel129.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel129MousePressed(evt);
            }
        });
        jPanel76.add(jLabel129);

        jPanel11.add(jPanel76);

        jPanel53.setBackground(new java.awt.Color(153, 255, 255));
        jPanel53.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel53.setMinimumSize(new java.awt.Dimension(206, 28));
        jPanel53.setPreferredSize(new java.awt.Dimension(480, 28));
        jPanel53.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel30.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 0, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Телефон:");
        jLabel30.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel30.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel30.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel53.add(jLabel30);

        jTextField24.setMaximumSize(new java.awt.Dimension(200, 2147483647));
        jTextField24.setMinimumSize(new java.awt.Dimension(200, 20));
        jTextField24.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanel53.add(jTextField24);

        jPanel11.add(jPanel53);

        jPanel54.setBackground(new java.awt.Color(204, 255, 255));
        jPanel54.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel54.setMinimumSize(new java.awt.Dimension(206, 28));
        jPanel54.setPreferredSize(new java.awt.Dimension(480, 92));
        jPanel54.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel32.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 255));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("Комментарий:");
        jLabel32.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel32.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel32.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel54.add(jLabel32);

        jScrollPane27.setBorder(null);
        jScrollPane27.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane27.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane27.setPreferredSize(new java.awt.Dimension(200, 80));

        jTextArea10.setColumns(20);
        jTextArea10.setLineWrap(true);
        jTextArea10.setRows(5);
        jTextArea10.setWrapStyleWord(true);
        jScrollPane27.setViewportView(jTextArea10);

        jPanel54.add(jScrollPane27);

        jPanel11.add(jPanel54);

        jPanel66.setBackground(new java.awt.Color(153, 255, 255));
        jPanel66.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel66.setMinimumSize(new java.awt.Dimension(206, 28));
        jPanel66.setPreferredSize(new java.awt.Dimension(480, 32));
        jPanel66.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel78.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(0, 0, 255));
        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel78.setText("Должность:");
        jLabel78.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel78.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel78.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel66.add(jLabel78);

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "бригадир", "экспедитор", "фасовщик", "грузчик" }));
        jComboBox12.setPreferredSize(new java.awt.Dimension(200, 24));
        jComboBox12.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox12ItemStateChanged(evt);
            }
        });
        jPanel66.add(jComboBox12);

        jLabel89.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(0, 0, 255));
        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel89.setText("Бригадир:");
        jLabel89.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel89.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel89.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel66.add(jLabel89);

        jComboBox13.setPreferredSize(new java.awt.Dimension(200, 24));
        jPanel66.add(jComboBox13);

        jPanel11.add(jPanel66);

        jPanel78.setBackground(new java.awt.Color(204, 255, 255));
        jPanel78.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel78.setMinimumSize(new java.awt.Dimension(206, 28));
        jPanel78.setPreferredSize(new java.awt.Dimension(480, 28));
        jPanel78.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel81.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(0, 0, 255));
        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel81.setText("Вход в систему:");
        jLabel81.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel81.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel81.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel78.add(jLabel81);

        jCheckBox1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox1.setMaximumSize(new java.awt.Dimension(30, 21));
        jCheckBox1.setMinimumSize(new java.awt.Dimension(30, 21));
        jCheckBox1.setOpaque(false);
        jCheckBox1.setPreferredSize(new java.awt.Dimension(30, 21));
        jPanel78.add(jCheckBox1);

        jPanel11.add(jPanel78);

        jPanel22.setBackground(new java.awt.Color(153, 255, 255));
        jPanel22.setForeground(new java.awt.Color(255, 255, 0));
        jPanel22.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel22.setMinimumSize(new java.awt.Dimension(284, 32));
        jPanel22.setPreferredSize(new java.awt.Dimension(100, 52));
        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 20));

        jLabel65.setForeground(new java.awt.Color(0, 0, 255));
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel65.setText("с:");
        jLabel65.setMaximumSize(new java.awt.Dimension(25, 14));
        jLabel65.setMinimumSize(new java.awt.Dimension(25, 14));
        jLabel65.setPreferredSize(new java.awt.Dimension(25, 14));
        jPanel22.add(jLabel65);

        jDateChooser1.setBackground(new java.awt.Color(204, 255, 255));
        jDateChooser1.setDateFormatString("dd MMM yyyy");
        jDateChooser1.setPreferredSize(new java.awt.Dimension(125, 24));
        jPanel22.add(jDateChooser1);

        jLabel66.setForeground(new java.awt.Color(0, 0, 255));
        jLabel66.setText("по:");
        jPanel22.add(jLabel66);

        jDateChooser2.setBackground(new java.awt.Color(204, 255, 255));
        jDateChooser2.setDateFormatString("dd MMM yyyy");
        jDateChooser2.setPreferredSize(new java.awt.Dimension(125, 24));
        jPanel22.add(jDateChooser2);

        jButton55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton55.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton55.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton55.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton55.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton55MousePressed(evt);
            }
        });
        jPanel22.add(jButton55);

        jButton60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Print-icon.png"))); // NOI18N
        jButton60.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton60.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton60.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton60.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton60MousePressed(evt);
            }
        });
        jPanel22.add(jButton60);

        jPanel11.add(jPanel22);

        jPanel149.setBackground(new java.awt.Color(153, 255, 255));
        jPanel149.setMaximumSize(new java.awt.Dimension(65534, 10000));
        jPanel149.setLayout(new javax.swing.BoxLayout(jPanel149, javax.swing.BoxLayout.X_AXIS));

        jPanel69.setBackground(new java.awt.Color(153, 255, 255));
        jPanel69.setLayout(new javax.swing.BoxLayout(jPanel69, javax.swing.BoxLayout.Y_AXIS));

        jLabel91.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(0, 0, 255));
        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setText("паковка");
        jLabel91.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel91.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel91.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel69.add(jLabel91);

        jScrollPane21.setBackground(new java.awt.Color(153, 255, 255));

        jTable7.setBackground(new java.awt.Color(153, 255, 255));
        jTable7.setForeground(new java.awt.Color(0, 0, 255));
        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable7.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane21.setViewportView(jTable7);

        jPanel69.add(jScrollPane21);

        jPanel149.add(jPanel69);

        jPanel128.setBackground(new java.awt.Color(153, 255, 255));
        jPanel128.setLayout(new javax.swing.BoxLayout(jPanel128, javax.swing.BoxLayout.Y_AXIS));

        jLabel92.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(0, 0, 255));
        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel92.setText("доставка");
        jLabel92.setMaximumSize(new java.awt.Dimension(100, 18));
        jLabel92.setMinimumSize(new java.awt.Dimension(100, 18));
        jLabel92.setPreferredSize(new java.awt.Dimension(100, 18));
        jPanel128.add(jLabel92);

        jScrollPane31.setBackground(new java.awt.Color(153, 255, 255));

        jTable13.setBackground(new java.awt.Color(153, 255, 255));
        jTable13.setForeground(new java.awt.Color(0, 0, 255));
        jTable13.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable13.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jScrollPane31.setViewportView(jTable13);

        jPanel128.add(jScrollPane31);

        jPanel149.add(jPanel128);

        jPanel11.add(jPanel149);

        jPanel5.add(jPanel11, java.awt.BorderLayout.CENTER);

        jPanel86.setMaximumSize(new java.awt.Dimension(300, 2147483647));
        jPanel86.setMinimumSize(new java.awt.Dimension(300, 23));
        jPanel86.setPreferredSize(new java.awt.Dimension(300, 2));
        jPanel86.setLayout(new java.awt.BorderLayout());

        jPanel87.setBackground(new java.awt.Color(153, 255, 255));
        jPanel87.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel87.setMinimumSize(new java.awt.Dimension(145, 30));
        jPanel87.setPreferredSize(new java.awt.Dimension(145, 30));
        jPanel87.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        jButton42.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton42.setForeground(new java.awt.Color(255, 0, 0));
        jButton42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/User-icon.png"))); // NOI18N
        jButton42.setText("+");
        jButton42.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton42.setMaximumSize(new java.awt.Dimension(65, 25));
        jButton42.setMinimumSize(new java.awt.Dimension(65, 25));
        jButton42.setPreferredSize(new java.awt.Dimension(65, 25));
        jButton42.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton42MousePressed(evt);
            }
        });
        jPanel87.add(jButton42);

        jButton43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png"))); // NOI18N
        jButton43.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton43.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton43.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton43.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton43MousePressed(evt);
            }
        });
        jPanel87.add(jButton43);

        jButton73.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Refresh-icon.png"))); // NOI18N
        jButton73.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton73.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton73.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton73.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton73MousePressed(evt);
            }
        });
        jPanel87.add(jButton73);

        jPanel86.add(jPanel87, java.awt.BorderLayout.NORTH);

        jTree2.setBackground(new java.awt.Color(204, 255, 255));
        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTree2.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree2.setRootVisible(false);
        jTree2.setShowsRootHandles(true);
        jTree2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree2MousePressed(evt);
            }
        });
        jTree2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTree2KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTree2);

        jPanel86.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel30.setBackground(new java.awt.Color(204, 255, 255));
        jPanel30.setPreferredSize(new java.awt.Dimension(10, 26));
        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 2));

        jCheckBox2.setForeground(new java.awt.Color(0, 0, 255));
        jCheckBox2.setSelected(true);
        jCheckBox2.setText("группировать по подчинению");
        jCheckBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jCheckBox2.setOpaque(false);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jPanel30.add(jCheckBox2);

        jPanel86.add(jPanel30, java.awt.BorderLayout.PAGE_END);

        jPanel5.add(jPanel86, java.awt.BorderLayout.WEST);

        jTabbedPane1.addTab("КАДРЫ  ", new javax.swing.ImageIcon(getClass().getResource("/Images/user-group-icon.png")), jPanel5); // NOI18N

        jPanel45.setBackground(new java.awt.Color(153, 255, 255));
        jPanel45.setLayout(new javax.swing.BoxLayout(jPanel45, javax.swing.BoxLayout.Y_AXIS));

        jPanel101.setBackground(new java.awt.Color(204, 255, 255));
        jPanel101.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel101.setMinimumSize(new java.awt.Dimension(366, 28));
        jPanel101.setPreferredSize(new java.awt.Dimension(544, 28));
        jPanel101.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 214, 3));

        jPanel102.setOpaque(false);
        jPanel102.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        jButton56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton56.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton56.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton56.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton56.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton56MousePressed(evt);
            }
        });
        jPanel102.add(jButton56);

        jButton57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Save-icon.png"))); // NOI18N
        jButton57.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton57.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton57.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton57.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton57MousePressed(evt);
            }
        });
        jPanel102.add(jButton57);

        jButton58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Windows-Close-Program-icon.png"))); // NOI18N
        jButton58.setMaximumSize(new java.awt.Dimension(32, 25));
        jButton58.setMinimumSize(new java.awt.Dimension(32, 25));
        jButton58.setPreferredSize(new java.awt.Dimension(32, 22));
        jButton58.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton58MousePressed(evt);
            }
        });
        jPanel102.add(jButton58);

        jPanel101.add(jPanel102);

        jPanel45.add(jPanel101);

        jPanel98.setBackground(new java.awt.Color(153, 255, 255));
        jPanel98.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel98.setMinimumSize(new java.awt.Dimension(276, 60));
        jPanel98.setPreferredSize(new java.awt.Dimension(550, 60));
        jPanel98.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel90.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel90.setForeground(new java.awt.Color(0, 0, 255));
        jLabel90.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel90.setText("Цена стика, грн:");
        jLabel90.setMaximumSize(new java.awt.Dimension(190, 18));
        jLabel90.setMinimumSize(new java.awt.Dimension(190, 18));
        jLabel90.setPreferredSize(new java.awt.Dimension(190, 18));
        jPanel98.add(jLabel90);

        jTextField23.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel98.add(jTextField23);

        jPanel45.add(jPanel98);

        jPanel164.setBackground(new java.awt.Color(204, 255, 255));
        jPanel164.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel164.setMinimumSize(new java.awt.Dimension(276, 28));
        jPanel164.setPreferredSize(new java.awt.Dimension(550, 28));
        jPanel164.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 4));

        jLabel109.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel109.setForeground(new java.awt.Color(0, 0, 255));
        jLabel109.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel109.setText("Цена коробки на 1 подарок, грн:");
        jLabel109.setMaximumSize(new java.awt.Dimension(190, 18));
        jLabel109.setMinimumSize(new java.awt.Dimension(190, 18));
        jLabel109.setPreferredSize(new java.awt.Dimension(190, 18));
        jPanel164.add(jLabel109);

        jTextField40.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel164.add(jTextField40);

        jPanel45.add(jPanel164);

        jPanel176.setBackground(new java.awt.Color(153, 255, 255));
        jPanel176.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "FTP-сервер", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 11))); // NOI18N
        jPanel176.setMaximumSize(new java.awt.Dimension(32767, 60));
        jPanel176.setMinimumSize(new java.awt.Dimension(276, 60));
        jPanel176.setPreferredSize(new java.awt.Dimension(550, 60));
        jPanel176.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 0));

        jLabel117.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel117.setForeground(new java.awt.Color(0, 0, 255));
        jLabel117.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel117.setText("Адрес:");
        jLabel117.setMaximumSize(new java.awt.Dimension(190, 18));
        jLabel117.setMinimumSize(new java.awt.Dimension(190, 18));
        jLabel117.setPreferredSize(new java.awt.Dimension(50, 15));
        jPanel176.add(jLabel117);

        jTextField42.setPreferredSize(new java.awt.Dimension(125, 20));
        jPanel176.add(jTextField42);

        jLabel119.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel119.setForeground(new java.awt.Color(0, 0, 255));
        jLabel119.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel119.setText("Пароль:");
        jLabel119.setMaximumSize(new java.awt.Dimension(190, 18));
        jLabel119.setMinimumSize(new java.awt.Dimension(190, 18));
        jLabel119.setPreferredSize(new java.awt.Dimension(50, 15));
        jPanel176.add(jLabel119);

        jTextField44.setPreferredSize(new java.awt.Dimension(125, 20));
        jPanel176.add(jTextField44);

        jPanel45.add(jPanel176);

        jTabbedPane1.addTab("CONST    ", new javax.swing.ImageIcon(getClass().getResource("/Images/gear-icon.png")), jPanel45); // NOI18N

        jPanel174.setBackground(new java.awt.Color(153, 255, 255));
        jPanel174.setLayout(new javax.swing.BoxLayout(jPanel174, javax.swing.BoxLayout.Y_AXIS));

        jPanel183.setLayout(new javax.swing.BoxLayout(jPanel183, javax.swing.BoxLayout.Y_AXIS));

        jPanel175.setBackground(new java.awt.Color(204, 255, 255));
        jPanel175.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel175.setMinimumSize(new java.awt.Dimension(100, 30));
        jPanel175.setPreferredSize(new java.awt.Dimension(903, 30));
        jPanel175.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));
        jPanel175.add(filler19);

        jDateChooser8.setBackground(new java.awt.Color(204, 255, 255));
        jDateChooser8.setDateFormatString("dd MMM yyyy");
        jDateChooser8.setPreferredSize(new java.awt.Dimension(140, 24));
        jPanel175.add(jDateChooser8);

        jLabel115.setText("-");
        jPanel175.add(jLabel115);

        jDateChooser9.setBackground(new java.awt.Color(204, 255, 255));
        jDateChooser9.setDateFormatString("dd MMM yyyy");
        jDateChooser9.setPreferredSize(new java.awt.Dimension(140, 24));
        jPanel175.add(jDateChooser9);

        jLabel116.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel116.setForeground(new java.awt.Color(200, 0, 0));
        jLabel116.setText("учитываются только завершенные заказы!");
        jPanel175.add(jLabel116);

        jPanel183.add(jPanel175);

        jTable19.setBackground(new java.awt.Color(153, 255, 255));
        jTable19.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable19.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jTable19.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane41.setViewportView(jTable19);

        jPanel183.add(jScrollPane41);

        jPanel177.setBackground(new java.awt.Color(204, 255, 255));
        jPanel177.setMaximumSize(new java.awt.Dimension(32767, 30));
        jPanel177.setMinimumSize(new java.awt.Dimension(100, 30));
        jPanel177.setPreferredSize(new java.awt.Dimension(933, 30));
        jPanel177.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 2));
        jPanel177.add(filler21);

        jButton75.setForeground(new java.awt.Color(255, 0, 0));
        jButton75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Add-icon.png"))); // NOI18N
        jButton75.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jButton75.setMaximumSize(new java.awt.Dimension(65, 25));
        jButton75.setMinimumSize(new java.awt.Dimension(65, 25));
        jButton75.setPreferredSize(new java.awt.Dimension(65, 25));
        jButton75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton75ActionPerformed(evt);
            }
        });
        jPanel177.add(jButton75);

        jButton114.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Edit-Document-icon.png"))); // NOI18N
        jButton114.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton114.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton114.setOpaque(false);
        jButton114.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton114.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton114ActionPerformed(evt);
            }
        });
        jPanel177.add(jButton114);

        jButton111.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delete.png"))); // NOI18N
        jButton111.setMaximumSize(new java.awt.Dimension(33, 25));
        jButton111.setMinimumSize(new java.awt.Dimension(33, 25));
        jButton111.setPreferredSize(new java.awt.Dimension(33, 25));
        jButton111.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton111ActionPerformed(evt);
            }
        });
        jPanel177.add(jButton111);

        jLabel120.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel120.setForeground(new java.awt.Color(0, 0, 255));
        jLabel120.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel120.setText("фильтр");
        jLabel120.setPreferredSize(new java.awt.Dimension(70, 13));
        jPanel177.add(jLabel120);

        jTextField45.setMinimumSize(new java.awt.Dimension(6, 26));
        jTextField45.setPreferredSize(new java.awt.Dimension(150, 26));
        jTextField45.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField45KeyReleased(evt);
            }
        });
        jPanel177.add(jTextField45);
        jPanel177.add(filler20);

        jDateChooser10.setBackground(new java.awt.Color(204, 255, 255));
        jDateChooser10.setDateFormatString("dd MMM yyyy");
        jDateChooser10.setPreferredSize(new java.awt.Dimension(140, 24));
        jPanel177.add(jDateChooser10);

        jLabel118.setText("-");
        jPanel177.add(jLabel118);

        jDateChooser11.setBackground(new java.awt.Color(204, 255, 255));
        jDateChooser11.setDateFormatString("dd MMM yyyy");
        jDateChooser11.setPreferredSize(new java.awt.Dimension(140, 24));
        jPanel177.add(jDateChooser11);

        jPanel183.add(jPanel177);

        jTable20.setBackground(new java.awt.Color(153, 255, 255));
        jTable20.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable20.setSelectionBackground(new java.awt.Color(102, 255, 255));
        jTable20.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane43.setViewportView(jTable20);

        jPanel183.add(jScrollPane43);

        jPanel174.add(jPanel183);

        jTabbedPane1.addTab("ФИНАНСЫ", new javax.swing.ImageIcon(getClass().getResource("/Images/coins-icon_big.png")), jPanel174); // NOI18N

        jPanel2.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(153, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 25));
        jPanel6.setMinimumSize(new java.awt.Dimension(10, 25));
        jPanel6.setPreferredSize(new java.awt.Dimension(100, 25));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 0));

        jLabel3.setForeground(new java.awt.Color(0, 0, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel3.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel3.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel6.add(jLabel3);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setMaximumSize(new java.awt.Dimension(2, 22));
        jSeparator1.setMinimumSize(new java.awt.Dimension(2, 22));
        jSeparator1.setPreferredSize(new java.awt.Dimension(2, 22));
        jPanel6.add(jSeparator1);

        jLabel4.setForeground(new java.awt.Color(0, 0, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Ронис Алексей - админ");
        jLabel4.setMaximumSize(new java.awt.Dimension(220, 14));
        jLabel4.setMinimumSize(new java.awt.Dimension(220, 14));
        jLabel4.setPreferredSize(new java.awt.Dimension(220, 14));
        jPanel6.add(jLabel4);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setMaximumSize(new java.awt.Dimension(2, 22));
        jSeparator2.setMinimumSize(new java.awt.Dimension(2, 22));
        jSeparator2.setPreferredSize(new java.awt.Dimension(2, 22));
        jPanel6.add(jSeparator2);

        jLabel43.setForeground(new java.awt.Color(0, 0, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Администратор");
        jLabel43.setMaximumSize(new java.awt.Dimension(150, 14));
        jLabel43.setMinimumSize(new java.awt.Dimension(150, 14));
        jLabel43.setPreferredSize(new java.awt.Dimension(150, 14));
        jPanel6.add(jLabel43);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator3.setMaximumSize(new java.awt.Dimension(2, 22));
        jSeparator3.setMinimumSize(new java.awt.Dimension(2, 22));
        jSeparator3.setPreferredSize(new java.awt.Dimension(2, 22));
        jPanel6.add(jSeparator3);

        jButton127.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/lock-icon2.png"))); // NOI18N
        jButton127.setText("блокировать сеанс");
        jButton127.setOpaque(false);
        jButton127.setPreferredSize(new java.awt.Dimension(170, 22));
        jButton127.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton127ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton127);

        jPanel2.add(jPanel6, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel2, "card3");

        jPanel1.setBackground(new java.awt.Color(153, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel38.setBackground(new java.awt.Color(153, 255, 255));
        jPanel38.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel38.setMaximumSize(new java.awt.Dimension(2147483647, 54));
        jPanel38.setMinimumSize(new java.awt.Dimension(245, 54));
        jPanel38.setPreferredSize(new java.awt.Dimension(100, 54));
        jPanel38.setLayout(new java.awt.BorderLayout());

        jLabel44.setBackground(new java.awt.Color(0, 100, 255));
        jLabel44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/back8.png"))); // NOI18N
        jPanel38.add(jLabel44, java.awt.BorderLayout.CENTER);

        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/back2.png"))); // NOI18N
        jPanel38.add(jLabel45, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel38);

        jPanel73.setMaximumSize(new java.awt.Dimension(270, 350));
        jPanel73.setMinimumSize(new java.awt.Dimension(270, 300));
        jPanel73.setOpaque(false);
        jPanel73.setPreferredSize(new java.awt.Dimension(270, 320));
        jPanel73.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        jLabel46.setForeground(new java.awt.Color(0, 0, 255));
        jLabel46.setText("Логин:");
        jLabel46.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel46.setPreferredSize(new java.awt.Dimension(240, 50));
        jPanel73.add(jLabel46);

        jTextField16.setPreferredSize(new java.awt.Dimension(240, 26));
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField16KeyPressed(evt);
            }
        });
        jPanel73.add(jTextField16);

        jLabel47.setForeground(new java.awt.Color(0, 0, 255));
        jLabel47.setText("Пароль:");
        jLabel47.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel47.setPreferredSize(new java.awt.Dimension(240, 22));
        jPanel73.add(jLabel47);

        jPasswordField1.setPreferredSize(new java.awt.Dimension(240, 26));
        jPasswordField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField1KeyPressed(evt);
            }
        });
        jPanel73.add(jPasswordField1);

        jLabel70.setForeground(new java.awt.Color(0, 0, 255));
        jLabel70.setText("Новый пароль:");
        jLabel70.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel70.setPreferredSize(new java.awt.Dimension(240, 22));
        jPanel73.add(jLabel70);

        jPasswordField2.setPreferredSize(new java.awt.Dimension(240, 26));
        jPasswordField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField2KeyPressed(evt);
            }
        });
        jPanel73.add(jPasswordField2);

        jLabel71.setForeground(new java.awt.Color(0, 0, 255));
        jLabel71.setText("Подтверждение:");
        jLabel71.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel71.setPreferredSize(new java.awt.Dimension(240, 22));
        jPanel73.add(jLabel71);

        jPasswordField3.setPreferredSize(new java.awt.Dimension(240, 26));
        jPasswordField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField3KeyPressed(evt);
            }
        });
        jPanel73.add(jPasswordField3);

        jPanel74.setOpaque(false);
        jPanel74.setPreferredSize(new java.awt.Dimension(240, 45));
        jPanel74.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        jButton49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/change-password-icon.png"))); // NOI18N
        jButton49.setText("Смена пароля");
        jButton49.setOpaque(false);
        jButton49.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton49MousePressed(evt);
            }
        });
        jPanel74.add(jButton49);

        jButton50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Ok-icon.png"))); // NOI18N
        jButton50.setText("Вход");
        jButton50.setOpaque(false);
        jButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50ActionPerformed(evt);
            }
        });
        jPanel74.add(jButton50);

        jPanel73.add(jPanel74);

        jLabel72.setForeground(new java.awt.Color(0, 0, 255));
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel72.setText("неверный логин/пароль");
        jLabel72.setPreferredSize(new java.awt.Dimension(240, 22));
        jPanel73.add(jLabel72);

        jLabel73.setForeground(new java.awt.Color(0, 0, 255));
        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel73.setText("пароль и подтверждение не совпадают");
        jLabel73.setPreferredSize(new java.awt.Dimension(240, 22));
        jPanel73.add(jLabel73);

        jPanel1.add(jPanel73);

        getContentPane().add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void jTree1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTree1KeyReleased
        SelectNodeOfTreeCandies();
}//GEN-LAST:event_jTree1KeyReleased

    private void jButton1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MousePressed
        jButton1.setVisible(false);
        jButton2.setVisible(true);
        jButton3.setVisible(true);
        
        JTextField[] mas = new JTextField[]{jTextField1,jTextField2,jTextField3,jTextField4,jTextField31};
        for (JTextField ma : mas) {
            ma.setEditable(true);
            ma.setBorder(BorderFactory.createEtchedBorder());
            ma.setBackground(Color.WHITE);
            ma.setForeground(Color.BLACK);
        }        
        jTextArea5.setBackground(Color.WHITE);
        jTextArea5.setForeground(Color.black);
        jTextArea5.setEditable(true);
        jTextArea5.setBorder(BorderFactory.createEtchedBorder());
    }//GEN-LAST:event_jButton1MousePressed

    private void jButton3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MousePressed
        SelectNodeOfTreeCandies();
    }//GEN-LAST:event_jButton3MousePressed

    private void jButton2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MousePressed
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        if (SelectedNode.getLevel() == 2) {
            try {
                String s = jTextField2.getText();
                double d1 = Double.parseDouble(s.replaceAll(" ", "").replace(',', '.'));
                s = jTextField4.getText();
                double d2 = Double.parseDouble(s.replaceAll(" ", "").replace(',', '.'));
                int last_change_cost;
                if (d2!=Candies.getDouble("COST_KG")) {
                    last_change_cost = ((Long)(Calendar.getInstance().getTimeInMillis()/1000)).intValue();
                } else {
                    last_change_cost = Candies.getInt("LAST_CHANGE_COST");
                }
                if (db.UpdateSQL("UPDATE candy SET name=?, box_weight=?, cost_kg=?, amount_in_box=?, comm=?, last_change_cost=? WHERE id=?", new Object[]{jTextField1.getText(), d1, d2, Integer.parseInt(jTextField3.getText()), jTextArea5.getText(),last_change_cost,Candies.getInt("ID")})) {
                    RefreshNodeCandies();
                } else {
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
            } catch (NumberFormatException | HeadlessException ex) {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        } else {
            try {
                String s = jTextField31.getText();
                double discount = Double.parseDouble(s.replaceAll(" ", "").replace(',', '.'));
                if (db.UpdateSQL("UPDATE factory SET name=?,discount=? WHERE id=?",new Object[]{jTextField1.getText(),discount,Candies.getInt("ID_FACTORY")})) {
                    GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
                    MakeTreeOfCandies();
                } else {
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
            } catch (NumberFormatException | HeadlessException ex) {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        }
    }//GEN-LAST:event_jButton2MousePressed

    private void jTree2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree2MousePressed
        SelectNodeOfTreeUsers();
    }//GEN-LAST:event_jTree2MousePressed

    private void jTree2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTree2KeyReleased
        SelectNodeOfTreeUsers();
    }//GEN-LAST:event_jTree2KeyReleased

    private void jButton17MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton17MousePressed
        jButton17.setVisible(false);
        jButton24.setVisible(true);
        jButton51.setVisible(true);
        JTextField[] mas = new JTextField[]{jTextField9,jTextField10,jTextField12,jTextField24};
        for (JTextField ma : mas) {
            ma.setEditable(true);
            ma.setBorder(BorderFactory.createEtchedBorder());
            ma.setBackground(Color.WHITE);
            ma.setForeground(Color.black);
        }        
        jCheckBox1.setEnabled(true);
        jLabel129.setEnabled(true);
        jLabel129.setText("<html><u>зашифрован, нажмите для изменения</u></html>");
        jComboBox12.setEnabled(Users.getInt("LEVEL")==STORAGE_WORKER);
        jComboBox13.setEnabled(true);
        jTextArea10.setBackground(Color.WHITE);
        jTextArea10.setForeground(Color.black);
        jTextArea10.setEditable(true);
        jTextArea10.setBorder(BorderFactory.createEtchedBorder());
}//GEN-LAST:event_jButton17MousePressed

    private void jButton24MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton24MousePressed
        String name = jTextField9.getText();
        String login = jTextField10.getText();
        String pass = jTextField12.getText();
        int can_enter = jCheckBox1.isSelected() ? 1 : 0;
        String phone = jTextField24.getText();
        String comment = jTextArea10.getText();
        int idPosition = jComboBox12.getSelectedIndex();
        int idBoss = (Users.getInt("LEVEL")==STORAGE_WORKER) && (idPosition==STORAGE_WORKER_PACKER) ? jComboBox13.getSelectedID() : -1;
       
        int id_user = Users.getInt("ID");

        if (db.UpdateSQL("UPDATE user SET name=?,login=?,pass=?,can_enter=?,phone=?,id_position=?,comment=?,id_user_boss=? WHERE id=?",new Object[]{name,login,pass,can_enter,phone,idPosition,comment,idBoss,id_user})) {
            GetUsers();
            MakeTreeOfUsers();
        } else {
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }
}//GEN-LAST:event_jButton24MousePressed

    private void jButton51MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton51MousePressed
        SelectNodeOfTreeUsers();
}//GEN-LAST:event_jButton51MousePressed

    private void jButton4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MousePressed
        jButton4.setVisible(false);
        jButton5.setVisible(true);
        jButton6.setVisible(true);
        jTextArea7.setEditable(true);
        jTextArea7.setBorder(BorderFactory.createEtchedBorder());
        jTextArea7.setBackground(Color.WHITE);
        jTextArea7.setForeground(Color.black);
        
        jComboBox8.setEnabled(true);
        jComboBox11.setEnabled(true);
        
        JTextField[] mas = new JTextField[]{jTextField47,jTextField6,jTextField7,
            jTextField25,jTextField52,jTextField13,jTextField8,jTextField11,jTextField34,jTextField35,jTextField26,jTextField27,
            jTextField28,jTextField29,jTextField15,jTextField14,jTextField22};
        for (JTextField ma : mas) {
            ma.setBorder(BorderFactory.createEtchedBorder());
            ma.setBackground(Color.WHITE);
            ma.setForeground(Color.BLACK);
            ma.setEditable(true);
        }
        jLabel74.setVisible(false);
        jDateChooser5.setVisible(true);
      
    }//GEN-LAST:event_jButton4MousePressed

    public boolean checkTheClient(String str) {
        char[] symbols = str.toCharArray();  
        if(symbols.length < 3 || symbols.length > 15 ) return false;  
          
        String validationString = "#№\"\'";  
          
        for(char c : symbols){  
            if(validationString.indexOf(c)!=-1) return true;  
        }  
          
        return false;
    }
    
    private void jButton5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MousePressed
        int clientID = Clients.getInt("ID");
        DefaultMutableTreeNode folderNode = (DefaultMutableTreeNode)jTree3.getSelectionPath().getParentPath().getLastPathComponent();
        int folderID = (int)((Object[])folderNode.getUserObject())[0];
        String toCheck = jTextField47.getText();
        
        if(checkTheClient(toCheck)) {
            JOptionPane.showMessageDialog(null, "Название клиента не может содержать номер или кавычки");
        } else {
            boolean clientExists = false;
            String existingValue = "";
            Object[][] objecto = db.SelectSQL("SELECT phone1, phone2, phone3, "
                    + "email1, email2, email3, edrpou FROM client",new Object[]{});
            ClientsToCheck.set(objecto);
            for (int i=0; i<ClientsToCheck.getLength(); i++) {
                //////////////// - checking phones - /////////////////
                if (jTextField7.getText() == null ? ClientsToCheck.getString(i, "PHONE1") == null : jTextField7.getText().equals(ClientsToCheck.getString(i, "PHONE1"))) {
                    existingValue = jTextField7.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField7.getText() == null ? ClientsToCheck.getString(i, "PHONE2") == null : jTextField7.getText().equals(ClientsToCheck.getString(i, "PHONE2"))){
                    existingValue = jTextField7.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField7.getText() == null ? ClientsToCheck.getString(i, "PHONE3") == null : jTextField7.getText().equals(ClientsToCheck.getString(i, "PHONE3"))){
                    existingValue = jTextField7.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                ///////////////////////////////////////////////////
                if (jTextField11.getText() == null ? ClientsToCheck.getString(i, "PHONE1") == null : jTextField11.getText().equals(ClientsToCheck.getString(i, "PHONE1"))){
                    existingValue = jTextField11.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField11.getText() == null ? ClientsToCheck.getString(i, "PHONE2") == null : jTextField11.getText().equals(ClientsToCheck.getString(i, "PHONE2"))){
                    existingValue = jTextField11.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField11.getText() == null ? ClientsToCheck.getString(i, "PHONE3") == null : jTextField11.getText().equals(ClientsToCheck.getString(i, "PHONE3"))){
                    existingValue = jTextField11.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                ////////////////////////////////////////////////////
                if (jTextField28.getText() == null ? ClientsToCheck.getString(i, "PHONE1") == null : jTextField28.getText().equals(ClientsToCheck.getString(i, "PHONE1"))){
                    existingValue = jTextField28.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField28.getText() == null ? ClientsToCheck.getString(i, "PHONE2") == null : jTextField28.getText().equals(ClientsToCheck.getString(i, "PHONE2"))){
                    existingValue = jTextField28.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField28.getText() == null ? ClientsToCheck.getString(i, "PHONE3") == null : jTextField28.getText().equals(ClientsToCheck.getString(i, "PHONE3"))){
                    existingValue = jTextField28.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                ////////////////// - phones are checked - //////////////////////
                
                ///////////////// - checking emails - /////////////////////////
                if (jTextField13.getText() == null ? ClientsToCheck.getString(i, "EMAIL1") == null : jTextField13.getText().equals(ClientsToCheck.getString(i, "EMAIL1"))) {
                    existingValue = jTextField13.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField13.getText() == null ? ClientsToCheck.getString(i, "EMAIL2") == null : jTextField13.getText().equals(ClientsToCheck.getString(i, "EMAIL2"))) {
                    existingValue = jTextField13.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField13.getText() == null ? ClientsToCheck.getString(i, "EMAIL3") == null : jTextField13.getText().equals(ClientsToCheck.getString(i, "EMAIL3"))) {
                    existingValue = jTextField13.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                ////////////////
                if (jTextField26.getText() == null ? ClientsToCheck.getString(i, "EMAIL1") == null : jTextField26.getText().equals(ClientsToCheck.getString(i, "EMAIL1"))) {
                    existingValue = jTextField26.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField26.getText() == null ? ClientsToCheck.getString(i, "EMAIL2") == null : jTextField26.getText().equals(ClientsToCheck.getString(i, "EMAIL2"))) {
                    existingValue = jTextField26.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField26.getText() == null ? ClientsToCheck.getString(i, "EMAIL3") == null : jTextField26.getText().equals(ClientsToCheck.getString(i, "EMAIL3"))) {
                    existingValue = jTextField26.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                /////////////////
                if (jTextField29.getText() == null ? ClientsToCheck.getString(i, "EMAIL1") == null : jTextField29.getText().equals(ClientsToCheck.getString(i, "EMAIL1"))) {
                    existingValue = jTextField29.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField29.getText() == null ? ClientsToCheck.getString(i, "EMAIL2") == null : jTextField29.getText().equals(ClientsToCheck.getString(i, "EMAIL2"))) {
                    existingValue = jTextField29.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                if (jTextField29.getText() == null ? ClientsToCheck.getString(i, "EMAIL3") == null : jTextField29.getText().equals(ClientsToCheck.getString(i, "EMAIL3"))) {
                    existingValue = jTextField29.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }
                ///////////// - finished checking emails - ///////////////////
                
                if (jTextField22.getText() == null ? ClientsToCheck.getString(i, "EDRPOU") == null : jTextField22.getText().equals(ClientsToCheck.getString(i, "EDRPOU"))) {
                    existingValue = jTextField22.getText();
                    if (existingValue.length() > 3) {
                        clientExists = true;
                        i = 20000;
                        break;
                    }
                }    
            }

            if (clientExists && newClientMode) {
                JOptionPane.showMessageDialog(null, "Клиент с такими данными уже существует!\n\n" + existingValue);
            } else {
                if (db.UpdateSQL("UPDATE client SET name=?,official_name=?, date_time=?,"
                        + "contact1=?,contact2=?,contact3=?,phone1=?,additional_phone1=?,fax=?,"
                        + "phone2=?,additional_phone2=?,phone3=?,additional_phone3=?,"
                        + "email1=?,email2=?,email3=?,address=?,site=?,comm=?,state=?,"
                        + "user_creator_id=?,edrpou=? WHERE id=?",
                        new Object[]{"", jTextField47.getText(), 
                            jDateChooser5.getCalendar().getTimeInMillis()/1000,
                            jTextField6.getText(), jTextField8.getText(),jTextField27.getText(),
                            jTextField7.getText(),jTextField25.getText(),jTextField52.getText(),jTextField11.getText(),
                            jTextField34.getText(),jTextField28.getText(),jTextField35.getText(),
                            jTextField13.getText(),jTextField26.getText(),jTextField29.getText(),
                            jTextField15.getText(),jTextField14.getText(),jTextArea7.getText(), 
                            ClientState.getValueForDB(jComboBox8.getSelectedState()), 
                            jComboBox11.getSelectedID(), jTextField22.getText(),Clients.getInt("ID")})) {
                    GetClients(jToggleButton5.isSelected());
                    MakeTreeOfClients();
                    newClientMode = false;

                    DefaultMutableTreeNode root = (DefaultMutableTreeNode)jTree3.getModel().getRoot();
                    for (int i = 0;i<root.getChildCount();i++) {
                        DefaultMutableTreeNode folder = (DefaultMutableTreeNode)root.getChildAt(i);
                        Object[] obj = (Object[])folder.getUserObject();
                        int foldID = (int)obj[0];
                        if (foldID==folderID) {
                            for (int j = 0;j<folder.getChildCount();j++) {
                                DefaultMutableTreeNode client = (DefaultMutableTreeNode)folder.getChildAt(j);
                                Object[] ob = (Object[])client.getUserObject();
                                int clienID = Clients.getInt((int)ob[1],"ID");
                                if (clienID==clientID) {
                                    jTree3.setSelectionPath(new TreePath(client.getPath()));
                                    SelectNodeOfTreeClients();       
                                    break;
                                }
                            }
                            break;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
            } 
        }   
    }//GEN-LAST:event_jButton5MousePressed

    private void jButton6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MousePressed
        SelectNodeOfTreeClients();
    }//GEN-LAST:event_jButton6MousePressed

    private void jTree3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree3MousePressed
        clientDraggedInClientsTree = false;
        SelectNodeOfTreeClients();
    }//GEN-LAST:event_jTree3MousePressed

    private void jTree3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTree3KeyReleased
        SelectNodeOfTreeClients();
    }//GEN-LAST:event_jTree3KeyReleased

    private void jTextField16KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER) {
            login();
        }
}//GEN-LAST:event_jTextField16KeyPressed

    private void jPasswordField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField1KeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER) {
            login();
        }
}//GEN-LAST:event_jPasswordField1KeyPressed

    private void jPasswordField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField2KeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER) {
            login();
        }
}//GEN-LAST:event_jPasswordField2KeyPressed

    private void jPasswordField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField3KeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ENTER) {
            login();
        }
}//GEN-LAST:event_jPasswordField3KeyPressed

    private void jButton49MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton49MousePressed
        jLabel70.setVisible(true);
        jLabel71.setVisible(true);
        jPasswordField2.setVisible(true);
        jPasswordField3.setVisible(true);
        jButton49.setVisible(false);
}//GEN-LAST:event_jButton49MousePressed

    private void jTree4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree4MousePressed
        SelectNodeOfTreeGifts();
    }//GEN-LAST:event_jTree4MousePressed

    private void jTree4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTree4KeyReleased
        SelectNodeOfTreeGifts();
    }//GEN-LAST:event_jTree4KeyReleased

    private void jButton8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton8MousePressed
        int SelectedRow = jTable1.getSelectedRow();
        if ((SelectedRow!=-1) && (SelectedRow!=jTable1.getRowCount()-1)) {
            if (db.UpdateSQL("DELETE FROM gift_candy WHERE id=?", new Object[]{Gift_Candy.getInt(SelectedRow, "ID")})) {
                GetGiftsCandy();
                MakeTableOfGiftsCandy();
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        }
    }//GEN-LAST:event_jButton8MousePressed

    private void jButton7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton7MousePressed
        addCandyToGift();
    }//GEN-LAST:event_jButton7MousePressed

    private void jButton13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton13MousePressed
        JTextField[] mas = new JTextField[]{jTextField17, jTextField30, jTextField18, jTextField19, jTextField20};
        for (JTextField ma : mas) {
            ma.setEditable(true);
            ma.setBorder(BorderFactory.createEtchedBorder());
            ma.setBackground(Color.WHITE);
            ma.setForeground(Color.BLACK);
        }
        jTextArea6.setBackground(Color.WHITE);
        jTextArea6.setForeground(Color.BLACK);
        jTextArea6.setEditable(true);
        jTextArea6.setBorder(BorderFactory.createEtchedBorder());
        
        jCheckBox4.setEnabled(true);
        
        jButton9.setVisible(true);
        jButton45.setVisible(true);
        jButton13.setVisible(false);
        jButton14.setVisible(true);
        jButton15.setVisible(true);
    }//GEN-LAST:event_jButton13MousePressed

    private void jButton14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton14MousePressed
        try {
            String s = jTextField18.getText();
            double d1 = Double.parseDouble(s.replaceAll(" ", "").replace(',', '.'));
            s = jTextField19.getText();
            double d2 = Double.parseDouble(s.replaceAll(" ", "").replace(',', '.'));
            int number = Integer.parseInt(jTextField30.getText());
            s = jTextField20.getText();
            double d3 = Double.parseDouble(s.replaceAll(" ", "").replace(',', '.'));
            if (db.UpdateSQL("UPDATE packing SET name=?,number=?, cost=?, weight=?, capacity=?,comm=?,marked=? WHERE id=?", new Object[]{jTextField17.getText(), number, d1, d2, d3, jTextArea6.getText(),jCheckBox4.isSelected() ? 1 : 0,Packings.getInt("ID")})) {
                RefreshNodePackings();
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        } catch (NumberFormatException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");            
        }
    }//GEN-LAST:event_jButton14MousePressed

    private void jButton15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton15MousePressed
        SelectNodeOfTreePackings();
    }//GEN-LAST:event_jButton15MousePressed

    private void jButton9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MousePressed
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(ChoosedDirectory);
        ImagePreviewPanel preview = new ImagePreviewPanel();
        chooser.setAccessory(preview);
        chooser.addPropertyChangeListener(preview);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            ChoosedDirectory = chooser.getCurrentDirectory();
            String name = chooser.getSelectedFile().getName();
            try {
                FTP.uploadFile(chooser.getSelectedFile(), name);
                if (db.UpdateSQL("UPDATE packing SET filename=? WHERE id=?",new Object[]{name,Packings.getInt("ID")})) {
                    RefreshNodePackings();
                } else {
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                JOptionPane.showMessageDialog(null, "Не удалось подключиться к FTP-серверу \n"+ex.getMessage());
            }
        }
    }//GEN-LAST:event_jButton9MousePressed

    private void jTree6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTree6KeyReleased
        SelectNodeOfTreePackings();
    }//GEN-LAST:event_jTree6KeyReleased

    private void jTree6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree6MousePressed
        SelectNodeOfTreePackings();
    }//GEN-LAST:event_jTree6MousePressed

    private void jTable3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MousePressed
        SelectNodeOfTableOrders();
    }//GEN-LAST:event_jTable3MousePressed

    private void jTable3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable3KeyReleased
        SelectNodeOfTableOrders();
    }//GEN-LAST:event_jTable3KeyReleased

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        changeTab();
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jButton10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MousePressed
        GetOrders();
        MakeTableOfOrders();
    }//GEN-LAST:event_jButton10MousePressed

    private void jButton11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MousePressed
        GetGifts();        
        orderComplectationDialog.initDialog(Gifts);
    }//GEN-LAST:event_jButton11MousePressed

    private void jButton12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MousePressed
        int SelectedRow = jTable5.getSelectedRow();
        if ((SelectedRow!=-1) && (SelectedRow<jTable5.getRowCount()-1)) {
            if (db.UpdateSQL("DELETE FROM suborder WHERE id=?",new Object[]{SubOrders.getInt(SelectedRow, "ID")})) {
                GetSubOrders();
                MakeTableOfSubOrders();
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        }
    }//GEN-LAST:event_jButton12MousePressed

    private void jButton16MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton16MousePressed
        int state = Orders.getInt("STATE");
        int date = ((Long)(Calendar.getInstance().getTimeInMillis()/1000)).intValue();
        int id_orders = Orders.getInt("ID"); 
        boolean result;
        switch (state) {
            case Order.ORDER_PREPARE:
                JOptionPane.showMessageDialog(null, "Проверьте ВМЕСТИМОСТЬ ДОСТАВКИ и ДОЛГ!");
                if (ExtendedOrder.getInt("TYPE_PAY")==-1) { //не выбран тип оплаты
                    JOptionPane.showMessageDialog(null, "Выберите тип оплаты заказа!");
                    return;
                }
                result = db.DoSQL("START TRANSACTION");
                if (result) {
                    result = db.UpdateSQL("UPDATE orders SET state=? WHERE id=?",new Object[]{Order.ORDER_PAY,id_orders});
                    if (result) {
                        db.UpdateSQL("UPDATE delivery SET state=? WHERE id_orders=?",new Object[]{Order.ORDER_PAY,id_orders});
                        for (int i=0;i<SubOrders.getLength();i++) {
                            result = result && db.UpdateSQL("UPDATE packing, suborder SET packing.reserved = packing.reserved + suborder.amount WHERE packing.ID=suborder.id_packing AND suborder.ID=?",new Object[]{SubOrders.getInt(i,"ID")});
                        }
                    }
                }
                if (result) {
                    db.DoSQL("COMMIT");   
                    GetOrders();
                    MakeTableOfOrders();
                } else {
                    db.DoSQL("ROLLBACK");
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
                break;
            case Order.ORDER_PAY:
                if (DeliveryOrder.getLength()==0) {
                    JOptionPane.showMessageDialog(null, "Заполните хотя бы одну доставку для заказа!");
                    return;
                }
                result = db.DoSQL("START TRANSACTION");
                if (result) {
                    result = db.UpdateSQL("UPDATE orders SET state=?,date_pay=? WHERE id=?",new Object[]{Order.ORDER_PACK,date,id_orders});                    
                    if (result) {
                        db.UpdateSQL("UPDATE delivery SET state=? WHERE id_orders=?",new Object[]{Order.ORDER_PACK,id_orders});                        
                        for (int i=0;i<SubOrders.getLength();i++) {
                            result = result && db.UpdateSQL("UPDATE candy, gift_candy, suborder SET candy.reserved = candy.reserved + suborder.amount*gift_candy.amount WHERE gift_candy.ID_CANDY = candy.ID AND gift_candy.ID_GIFT=suborder.ID_GIFT AND suborder.ID=?",new Object[]{SubOrders.getInt(i,"ID")}); 
                        }
                    } 
                }
                if (result) {
                    db.DoSQL("COMMIT");   
                    GetOrders();
                    MakeTableOfOrders();
                } else {
                    db.DoSQL("ROLLBACK");
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
                break;
            case Order.ORDER_PACK:
                for (int i=0;i<SubOrders.getLength();i++) {
                    if (SubOrders.getInt(i,"AMOUNT")!=SubOrders.getInt(i,"PACKED")) {
                        JOptionPane.showMessageDialog(null, "Заказ еще не упакован полностью!");
                        return;
                    }
                }
                result = db.DoSQL("START TRANSACTION");
                if (result) {
                    result = db.UpdateSQL("UPDATE orders SET state=?,date_pack=? WHERE id=?", new Object[]{Order.ORDER_STORAGE, date, id_orders});
                    if (result) {
                        db.UpdateSQL("UPDATE delivery SET state=? WHERE id_orders=?",new Object[]{Order.ORDER_STORAGE,id_orders});
                    }
                }
                if (result) {
                    db.DoSQL("COMMIT"); 
                    GetOrders();
                    MakeTableOfOrders();            
                } else {
                    db.DoSQL("ROLLBACK");
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
                break;
            case Order.ORDER_STORAGE:
                for (int i=0;i<SubOrders.getLength();i++) {
                    if (SubOrders.getInt(i,"AMOUNT")!=SubOrders.getInt(i,"PACKED")) {
                        JOptionPane.showMessageDialog(null, "Заказ еще не упакован полностью!");
                        return;
                    }
                }
                result = db.DoSQL("START TRANSACTION");
                if (result) {
                    result = db.UpdateSQL("UPDATE orders SET state=?,date_pack=? WHERE id=?", new Object[]{Order.ORDER_SEND, date, id_orders});
                    if (result) {
                        db.UpdateSQL("UPDATE delivery SET state=? WHERE id_orders=?",new Object[]{Order.ORDER_SEND,id_orders});
                    }
                }
                if (result) {
                    db.DoSQL("COMMIT"); 
                    GetOrders();
                    MakeTableOfOrders();            
                } else {
                    db.DoSQL("ROLLBACK");
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
                break;
            case Order.ORDER_SEND:
                result = db.DoSQL("START TRANSACTION");
                if (result) {
                    result = db.UpdateSQL("UPDATE orders SET state=? WHERE id=?", new Object[]{Order.ORDER_DONE, id_orders});
                    if (result) {
                        db.UpdateSQL("UPDATE delivery SET state=? WHERE id_orders=?",new Object[]{Order.ORDER_DONE,id_orders});
                    }
                }
                if (result) {
                    db.DoSQL("COMMIT"); 
                    GetOrders();
                    MakeTableOfOrders();            
                } else {
                    db.DoSQL("ROLLBACK");
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
                break;
//            case Order.ORDER_DELIVERED:
//                result = db.DoSQL("START TRANSACTION");
//                if (result) {
//                    result = db.UpdateSQL("UPDATE orders SET state=? WHERE id=?",new Object[]{Order.ORDER_DONE,id_orders});
//                    if (result) {
//                        result = db.UpdateSQL("UPDATE delivery SET state=? WHERE id_orders=?",new Object[]{Order.ORDER_DONE,id_orders});
//                    }
//                }
//                if (result) {
//                    db.DoSQL("COMMIT");   
//                    GetOrders();
//                    MakeTableOfOrders();
//                } else {
//                    db.DoSQL("ROLLBACK");
//                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
//                }
//                break;
        }
    }//GEN-LAST:event_jButton16MousePressed

    private void jButton22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MousePressed
        clientChooseDialog.initDialog();
    }//GEN-LAST:event_jButton22MousePressed

    private void jButton27MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton27MousePressed
        jButton27.setVisible(false);
        jButtonSaveOrder.setVisible(true);
        jButton29.setVisible(true);
        
        if(CurrentUser.getInt("LEVEL")==DIRECTOR){
            jTextArea12.setEditable(true);
        }

        if (Orders.getInt("STATE")<Order.ORDER_PACK || CurrentUser.getInt("LEVEL")==DIRECTOR) {
            jTextField33.setEditable(true);
            jTextField33.setBorder(BorderFactory.createEtchedBorder());
            jTextField33.setBackground(Color.WHITE);
            jTextField33.setForeground(Color.black);
            
            jTextField37.setEditable(true);
            jTextField37.setBorder(BorderFactory.createEtchedBorder());
            jTextField37.setBackground(Color.WHITE);
            jTextField37.setForeground(Color.black);
        
            jTextField41.setEditable(true);
            jTextField41.setBorder(BorderFactory.createEtchedBorder());
            jTextField41.setBackground(Color.WHITE);
            jTextField41.setForeground(Color.black);
        
            jLabel79.setVisible(false);
            jComboBoxPaymentTypesOrderDetail.setVisible(true);
        }
        
        jTextArea8.setEditable(true);
        jTextArea8.setBackground(Color.WHITE);
        jTextArea8.setForeground(Color.BLACK);   
        jTextArea8.setBorder(BorderFactory.createEtchedBorder());
        jTextArea9.setEditable(true);
        jTextArea9.setBackground(Color.WHITE);
        jTextArea9.setForeground(Color.BLACK);       
        jTextArea9.setBorder(BorderFactory.createEtchedBorder());
        
    }//GEN-LAST:event_jButton27MousePressed

    private void jButtonSaveOrderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonSaveOrderMousePressed
        System.out.println("Запрос на сохранение заказа: jComboBox2.getSelectedIndex()="+jComboBoxPaymentTypesOrderDetail.getSelectedIndex());
        try {
            int type_pay = jComboBoxPaymentTypesOrderDetail.getSelectedIndex();
            String name = jTextArea12.getText();
            Object[][] obj = db.SelectSQL("SELECT id FROM user WHERE name=?",new Object[]{name});
            Users.set(obj);
            Object creator = Users.get("ID");
            String creator_id = Integer.toString((int)creator);
            String comm = jTextArea8.getText();
            String comm_packing = jTextArea9.getText();
            double prepay = Double.parseDouble((jTextField33.getText()).replaceAll(" ", "").replace(',', '.'));
            double delivery_cost = Double.parseDouble((jTextField41.getText()).replaceAll(" ", "").replace(',', '.'));
            double discount = Double.parseDouble((jTextField37.getText()).replaceAll(" ", "").replace(',', '.'));
            if (db.UpdateSQL("UPDATE orders SET type_pay=?,prepay=?,discount=?,comm_packing=?,comm=?,delivery_cost=?,USER_CREATOR_ID=? WHERE id=?",new Object[]{type_pay,prepay,discount,comm_packing,comm,delivery_cost,creator_id,Orders.getInt("ID")})) {
                
                if (DeliveryOrder.getLength()==1) {
                    double Summ = 0;
                    for (int i=0;i<SubOrders.getLength();i++) {
                        Summ+=SubOrders.getDouble(i,"COST")*SubOrders.getInt(i,"AMOUNT");
                    }
                    double newDebt = delivery_cost + Summ - prepay;
                    db.UpdateSQL("UPDATE delivery SET debt=? WHERE id_orders=? LIMIT 1",new Object[]{newDebt,Orders.getInt("ID")});
                }
                
                GetOrders();
                MakeTableOfOrders();
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
            jTextArea12.setEditable(false);
        } catch (NumberFormatException | HeadlessException | NullPointerException | ArrayIndexOutOfBoundsException ex) {
            System.out.println("An exception occured!!!\n" + ex);
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }
    }//GEN-LAST:event_jButtonSaveOrderMousePressed

    private void jButton29MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton29MousePressed
        SelectNodeOfTableOrders();
    }//GEN-LAST:event_jButton29MousePressed

    private void jButton32MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton32MousePressed
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        if (SelectedNode.getLevel() == 1) {
            if (JOptionPane.showConfirmDialog(null, "Действительно удалить фабрику " + Candies.getString("FACTORY_NAME") + " ?", "Удаление фабрики", JOptionPane.YES_NO_OPTION) == 0) {
                if (SelectedNode.getChildCount() != 0) {
                    JOptionPane.showMessageDialog(null, "Нельзя удалить непустой узел");
                } else {
                    if (db.UpdateSQL("DELETE FROM factory WHERE id=?", new Object[]{Candies.getInt("ID_FACTORY")})) {
                        GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
                        MakeTreeOfCandies();
                    } else {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                    }
                }
            }
        } else {
            if (JOptionPane.showConfirmDialog(null, "Действительно удалить конфету " + Candies.getString("CANDY_NAME") + " ?", "Удаление конфеты", JOptionPane.YES_NO_OPTION) == 0) {
                if (db.UpdateSQL("DELETE FROM candy WHERE id=?", new Object[]{Candies.getInt("ID")})) {
                    GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
                    MakeTreeOfCandies();
                } else {
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
            }
        }
    }//GEN-LAST:event_jButton32MousePressed

    private void jButton30MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton30MousePressed
        if (db.UpdateSQL("INSERT INTO factory (name,discount) VALUES('_НОВАЯ_',0)",null)) {
            GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
            MakeTreeOfCandies();
        } else {
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }
    }//GEN-LAST:event_jButton30MousePressed

    private void jButton31MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton31MousePressed
        if (db.UpdateSQL("INSERT INTO candy (name,id_factory,box_weight,amount_in_box,cost_kg,storage,reserved,comm,last_change_cost) VALUES('_НОВАЯ_',?,1,1,1,0,0,'',0)", new Object[]{Candies.getInt("ID_FACTORY")})) {
            GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
            MakeTreeOfCandies();
            int[] mas = new int[Candies.getLength()];
            for (int i=0;i<Candies.getLength();i++) {
                mas[i] = Candies.getInt(i, "ID");
            }
            Arrays.sort(mas);
            int max = mas[mas.length-1];
            for (int i=0;i<jTree1.getRowCount();i++) {
                Object[] obj = (Object[])((DefaultMutableTreeNode)jTree1.getPathForRow(i).getLastPathComponent()).getUserObject();
                if (Candies.getInt((Integer)obj[1],"ID")==max) {
                    jTree1.setSelectionRow(i);
                    Rectangle r = jTree1.getRowBounds(jTree1.getMinSelectionRow());
                    Point p = new Point(r.x, r.y);
                    ((JViewport)(jTree1.getParent())).setViewPosition(p);
                    SelectNodeOfTreeCandies();
                    break;
                }
            }            
        } else {
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }
    }//GEN-LAST:event_jButton31MousePressed

    private void jTree1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MousePressed
        SelectNodeOfTreeCandies();
    }//GEN-LAST:event_jTree1MousePressed

    private void jButton34MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton34MousePressed
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode)jTree6.getLastSelectedPathComponent();
        if (SelectedNode!=null) {
            int type;
            if (SelectedNode.getLevel()==1) {
                type = (Integer)(((Object[])SelectedNode.getUserObject())[1]);
            } else {
                type = Packings.getInt((Integer)(((Object[])SelectedNode.getUserObject())[1]),"TYPE");
            }
            if (db.UpdateSQL("INSERT INTO packing(name,type,number,cost,weight,capacity,storage,reserved,comm,filename,marked) VALUES ('_НОВЫЙ_',?,0,0,0,0,0,0,'','',0)",new Object[]{type})) {
                GetPackings();  
                MakeTreeOfPackings();
                int[] mas = new int[Packings.getLength()];
                for (int i=0;i<Packings.getLength();i++) {
                    mas[i] = Packings.getInt(i, "ID");
                }
                Arrays.sort(mas);
                int max = mas[mas.length-1];
                for (int i=0;i<jTree6.getRowCount();i++) {
                    Object[] obj = (Object[])((DefaultMutableTreeNode)jTree6.getPathForRow(i).getLastPathComponent()).getUserObject();
                    if ((obj.length>2) && (Packings.getInt((Integer)obj[1],"ID")==max)) {
                        jTree6.setSelectionRow(i);
                        Rectangle r = jTree6.getRowBounds(jTree6.getMinSelectionRow());
                        Point p = new Point(r.x, r.y);
                        ((JViewport)(jTree6.getParent())).setViewPosition(p);
                        SelectNodeOfTreePackings();
                        break;
                    }
                }                  
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        }
    }//GEN-LAST:event_jButton34MousePressed

    private void jButton35MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton35MousePressed
        if (JOptionPane.showConfirmDialog(null, "Действительно удалить упаковку " + Packings.getString("NAME") + " ?", "Удаление упаковки", JOptionPane.YES_NO_OPTION) == 0) {
            if (db.UpdateSQL("DELETE FROM packing WHERE id=?", new Object[]{Packings.getInt("ID")})) {
                GetPackings();
                MakeTreeOfPackings();
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        }
    }//GEN-LAST:event_jButton35MousePressed

    private void jButton36MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton36MousePressed
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)jTree3.getLastSelectedPathComponent();
        if (selectedNode==null) {
            return;
        }
        int folderID;
        if (selectedNode.getLevel()==1) { //folder
            Object[] obj = (Object[])selectedNode.getUserObject();
            folderID = (int)obj[0];    
        } else if (selectedNode.getLevel()==2) { //client
            Object[] obj = (Object[])((DefaultMutableTreeNode)selectedNode.getParent()).getUserObject();
            folderID = (int)obj[0];
        } else { //root
            return;
        }      
                
        int date_time = ((Long)(Calendar.getInstance().getTimeInMillis()/1000)).intValue();
        if (db.UpdateSQL("INSERT INTO client(date_time,official_name,name,contact1,"
                + "contact2,contact3,phone1,phone2,phone3,email1,email2,email3,"
                + "site,address,comm,user_creator_id) VALUES(?,'_НОВЫЙ_',"
                + "'_НОВЫЙ_','','','','','','','','','','','','',?)",
                new Object[]{date_time,CurrentUser.getInt("ID")})) {
            Object[][] ob = db.SelectSQL("SELECT max(id) FROM client",new Object[]{});
            int newClientID = (int)ob[0][0]; 
            db.UpdateSQL("INSERT INTO client_folder(id_client,id_folder) VALUES(?,?)",new Object[]{newClientID,folderID});
            GetFolders();
            GetClients(jToggleButton5.isSelected());
            MakeTreeOfClients();
            newClientMode = true;

            for (int i=0;i<jTree3.getRowCount();i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)jTree3.getPathForRow(i).getLastPathComponent();
                if (node.getLevel()==1) {
                    continue;
                }
                Object[] obj = (Object[])node.getUserObject();
                if (Clients.getInt((Integer)obj[1],"ID")==newClientID) {
                    jTree3.setSelectionRow(i);
                    Rectangle r = jTree3.getRowBounds(jTree3.getMinSelectionRow());
                    Point p = new Point(r.x, r.y);
                    ((JViewport)(jTree3.getParent())).setViewPosition(p);
                    SelectNodeOfTreeClients();
                    break;
                }
            }              
        } else {
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }
    }//GEN-LAST:event_jButton36MousePressed

    private void jButton37MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton37MousePressed
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree3.getLastSelectedPathComponent();
        if (SelectedNode == null) {
            return;
        }
        if (SelectedNode.getLevel()==2) { //если выбран клиент
            int clientID = Clients.getInt((int)((Object[])SelectedNode.getUserObject())[1],"ID");
            int folderID = (int)((Object[])((DefaultMutableTreeNode)SelectedNode.getParent()).getUserObject())[0];
            
            Object[][] count_obj = db.SelectSQL("SELECT count(*) FROM client_folder WHERE id_client=?", new Object[]{clientID});
            int count = ((Long)count_obj[0][0]).intValue();
            
            if (count>1) {
                if (JOptionPane.showConfirmDialog(null, "Клиент будет удален из ТЕКУЩЕЙ папки,\nно останется в других. Продолжить?", "Удаление клиента", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (db.UpdateSQL("DELETE FROM client_folder WHERE id_client=? AND id_folder=?",new Object[]{clientID, folderID})) {
                        GetFolders();
                        GetClients(jToggleButton5.isSelected());
                        MakeTreeOfClients();
                    } else {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                    }
                }
            } else { //if count==1
                if (JOptionPane.showConfirmDialog(null, "Клиент будет удален, данное действие\nневозможно будет отменить. Продолжить?", "Удаление клиента", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (db.UpdateSQL("DELETE FROM client WHERE id=?", new Object[]{clientID})) {
                        db.UpdateSQL("DELETE FROM client_folder WHERE id_client=? AND id_folder=?",new Object[]{clientID, folderID});
                        GetFolders();
                        GetClients(jToggleButton5.isSelected());
                        MakeTreeOfClients();
                    } else {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                    }
                }
            }
        } else if (SelectedNode.getLevel()==1) { //если выбрана папка
            int folderID = (int)((Object[])SelectedNode.getUserObject())[0];
            if (JOptionPane.showConfirmDialog(null, "Папка будет удалена. Продолжить?", "Удаление папки", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (db.UpdateSQL("DELETE FROM folder WHERE id=?", new Object[]{folderID})) {
                    db.UpdateSQL("DELETE FROM client_folder WHERE id_folder=?", new Object[]{folderID});
                    GetFolders();
                    GetClients(jToggleButton5.isSelected());
                    MakeTreeOfClients();
                } else {
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }
            }
        }
    }//GEN-LAST:event_jButton37MousePressed

    private void jButton38MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton38MousePressed
        if (db.UpdateSQL("INSERT INTO gift(name,cost_packing) VALUES('_НОВЫЙ_',0)",null)) {
            GetGifts();
            MakeTreeOfGifts();
            int[] mas = new int[Gifts.getLength()];
            for (int i=0;i<Gifts.getLength();i++) {
                mas[i] = Gifts.getInt(i, "ID");
            }
            Arrays.sort(mas);
            int max = mas[mas.length-1];
            for (int i=0;i<jTree4.getRowCount();i++) {
                Object[] obj = (Object[])((DefaultMutableTreeNode)jTree4.getPathForRow(i).getLastPathComponent()).getUserObject();
                if (Gifts.getInt((Integer)obj[1],"ID")==max) {
                    jTree4.setSelectionRow(i);
                    Rectangle r = jTree4.getRowBounds(jTree4.getMinSelectionRow());
                    Point p = new Point(r.x, r.y);
                    ((JViewport)(jTree4.getParent())).setViewPosition(p);
                    SelectNodeOfTreeGifts();
                    break;
                }
            }  
        } else {
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }
    }//GEN-LAST:event_jButton38MousePressed

    private void jButton39MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton39MousePressed
        if (JOptionPane.showConfirmDialog(null, "Действительно удалить набор " + Gifts.getString("NAME") + " ?", "Удаление набора", JOptionPane.YES_NO_OPTION) == 0) {
            if (db.UpdateSQL("DELETE FROM gift WHERE id=?", new Object[]{Gifts.getInt("ID")})) {
                GetGifts();
                MakeTreeOfGifts();
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        }
    }//GEN-LAST:event_jButton39MousePressed

    private void jButton33MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton33MousePressed
        jTextField21.setBorder(BorderFactory.createEtchedBorder());
        jTextField21.setEditable(true);
        jTextField21.setBackground(Color.WHITE);
        jTextField21.setForeground(Color.BLACK);

        jTextField32.setBorder(BorderFactory.createEtchedBorder());
        jTextField32.setEditable(true);
        jTextField32.setBackground(Color.WHITE);
        jTextField32.setForeground(Color.BLACK);
        
        jButton33.setVisible(false);
        jButton40.setVisible(true);
        jButton41.setVisible(true);
    }//GEN-LAST:event_jButton33MousePressed

    private void jButton40MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton40MousePressed
        String s = jTextField32.getText();
        double cost_packing = Double.parseDouble(s.replaceAll(" ", "").replace(',', '.'));
        int id = Gifts.getInt("ID");
        if (db.UpdateSQL("UPDATE gift SET name=?,cost_packing=? WHERE id=?", new Object[]{jTextField21.getText(), cost_packing, Gifts.getInt("ID")})) {
            GetGifts();
            MakeTreeOfGifts();
            
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)jTree4.getModel().getRoot();
            for (int i = 0;i<root.getChildCount();i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(i);
                Object[] obj = (Object[])node.getUserObject();
                int packingID = Gifts.getInt((int)obj[1],"ID");
                if (packingID==id) {
                    jTree4.setSelectionPath(new TreePath(node.getPath()));
                    SelectNodeOfTreeGifts();
                    break;
                }
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }
    }//GEN-LAST:event_jButton40MousePressed

    private void jButton41MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton41MousePressed
        SelectNodeOfTreeGifts();
    }//GEN-LAST:event_jButton41MousePressed

    private void jButton42MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton42MousePressed
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree2.getLastSelectedPathComponent();
        if (SelectedNode!=null) {
            Object[] obj = (Object[]) SelectedNode.getUserObject();
            int level;
            if (SelectedNode.getLevel()==1) {
                level = (Integer)obj[1];
            } else {
                level = Users.getInt((Integer)obj[1],"LEVEL");
            }
            if (db.UpdateSQL("INSERT INTO user(name,login,pass,level,can_enter,finance_pass) VALUES('_НОВЫЙ_',?,'',?,0,'')", new Object[]{"новый"+Math.round(Math.random()*100000),level})) {
                GetUsers();
                MakeTreeOfUsers();
                int[] mas = new int[Users.getLength()];
                for (int i=0;i<Users.getLength();i++) {
                    mas[i] = Users.getInt(i, "ID");
                }
                Arrays.sort(mas);
                int max = mas[mas.length-1];
                for (int i=0;i<jTree2.getRowCount();i++) {
                    Object[] objobj = (Object[])((DefaultMutableTreeNode)jTree2.getPathForRow(i).getLastPathComponent()).getUserObject();
                    if (Users.getInt((Integer)objobj[1],"ID")==max) {
                        jTree2.setSelectionRow(i);
                        Rectangle r = jTree2.getRowBounds(jTree2.getMinSelectionRow());
                        Point p = new Point(r.x, r.y);
                        ((JViewport)(jTree2.getParent())).setViewPosition(p);
                        SelectNodeOfTreeUsers();
                        break;
                    }
                }                  
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        }
    }//GEN-LAST:event_jButton42MousePressed

    private void jButton43MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton43MousePressed
        if (JOptionPane.showConfirmDialog(null, "Действительно удалить пользователя " + Users.getString("NAME") + " ?", "Удаление пользователя", JOptionPane.YES_NO_OPTION) == 0) {
            if (db.UpdateSQL("DELETE FROM user WHERE id=?", new Object[]{Users.getInt("ID")})) {
                GetUsers();
                MakeTreeOfUsers();
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        }
    }//GEN-LAST:event_jButton43MousePressed

    private void jTree10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree10MousePressed
        SelectNodeOfTreeInStorage(jTree10,Candies,jTable6,true);
        MakeTableOfStorageCandyReserv();        
    }//GEN-LAST:event_jTree10MousePressed

    private void jTree10KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTree10KeyReleased
        SelectNodeOfTreeInStorage(jTree10,Candies,jTable6,true);
        MakeTableOfStorageCandyReserv();        
    }//GEN-LAST:event_jTree10KeyReleased

    private void jTree11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree11MousePressed
        SelectNodeOfTreeInStorage(jTree11,FilteredPackings,jTable8,false);
        MakeTableOfStoragePackingReserv();
    }//GEN-LAST:event_jTree11MousePressed

    private void jTree11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTree11KeyReleased
        SelectNodeOfTreeInStorage(jTree11,FilteredPackings,jTable8,false);        
        MakeTableOfStoragePackingReserv();
    }//GEN-LAST:event_jTree11KeyReleased

    private void jButton44MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton44MousePressed
        jTabbedPane2StateChanged(null);
    }//GEN-LAST:event_jButton44MousePressed

    private void jTabbedPane2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane2StateChanged
        if (!CurrentUser.IsNull()) {
            switch (jTabbedPane2.getSelectedIndex()) {
                case 0:
                    GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
                    MakeTreeOfCandiesInStorage();
                    jPanel90.setVisible(true);
                    break;
                case 1:
                    GetFilteredPackings();
                    MakeTreeOfPackingsInStorage();
                    jPanel90.setVisible(true);
                    break;
                case 2:
                    GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
                    MakeTableOfOrderCandyForStorage();
                    GetPackings();
                    MakeTableOfOrderPackingForStorage();
                    jPanel90.setVisible(false);
                    break;
            }
        }
    }//GEN-LAST:event_jTabbedPane2StateChanged

    private void jButton52MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton52MousePressed
        JTree tree;
        if (jTabbedPane2.getSelectedIndex()==0) {
            tree = jTree10;
        } else {
            tree = jTree11;                
        }        
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (SelectedNode!=null) {
            revenueExpensesDialog.initDialog(true);
        }
    }//GEN-LAST:event_jButton52MousePressed

    private void jButton55MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton55MousePressed
        MakeTableOfUsersStorage();
        MakeTableOfUsersDelivery();
    }//GEN-LAST:event_jButton55MousePressed

    private void jButton56MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton56MousePressed
        JTextField[] mas = new JTextField[]{jTextField23,jTextField40,jTextField42,jTextField44};
        for (JTextField ma : mas) {
            ma.setEditable(true);
            ma.setBorder(BorderFactory.createEtchedBorder());
            ma.setBackground(Color.WHITE);
            ma.setForeground(Color.BLACK);
        } 
        jButton56.setVisible(false);
        jButton57.setVisible(true);
        jButton58.setVisible(true);
    }//GEN-LAST:event_jButton56MousePressed

    private void jButton57MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton57MousePressed
        try {
            double stick_cost = Double.parseDouble(jTextField23.getText().replaceAll(" ", "").replace(',', '.'));
            double cost_box_for_1_gift = Double.parseDouble(jTextField40.getText().replaceAll(" ", "").replace(',', '.'));
            if (db.UpdateSQL("UPDATE constants SET stick_cost=?, cost_box_for_1_gift=?, ftpaddress=?, ftppass=?", new Object[]{stick_cost,cost_box_for_1_gift,jTextField42.getText(),jTextField44.getText()})) {
                GetConstants();
                MakePanelOfConstants();
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        } catch (NumberFormatException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, "Неверно введены значения");
        }
    }//GEN-LAST:event_jButton57MousePressed

    private void jButton58MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton58MousePressed
        MakePanelOfConstants();
    }//GEN-LAST:event_jButton58MousePressed

    private void jButton59MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton59MousePressed
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();
        String s;
        if (SelectedNode.getLevel()==0) {
            s = "<html><center>Отчет по фабрикам и конфетам<br><br><table border='1'>";
            s = s + "<tr>";
            for (int j=0;j<jTable2.getColumnCount();j++) {
                s = s + "<td width='60' align='center'>" + jTable2.getModel().getColumnName(j)+"</td>";    
            }
            s = s + "</tr>";
            for (int i=0;i<jTable2.getRowCount();i++) {
                s = s+"<tr>";
                for (int j=0;j<jTable2.getColumnCount();j++) {
                    if (jTable2.getValueAt(i, j)==null) {
                        s = s+"<td></td>";    
                    } else {
                        s = s+"<td>"+jTable2.getValueAt(i, j)+"</td>";    
                    }
                }
                s = s+"</tr>";
            }
            s = s + "</table><br></center></html>";
        } else {
            s = "<html><center>Отчет по конфетам фабрики "+Candies.getString("FACTORY_NAME") +"<br><br><table border='1'>";
            s = s + "<tr>";
            for (int j=0;j<jTable2.getColumnCount();j++) {
                s = s + "<td width='60' align='center'>" + jTable2.getModel().getColumnName(j)+"</td>";    
            }
            s = s + "</tr>";
            for (int i=0;i<jTable2.getRowCount();i++) {
                s = s+"<tr>";
                for (int j=0;j<jTable2.getColumnCount();j++) {
                    if (jTable2.getValueAt(i, j)==null) {
                        s = s+"<td></td>";    
                    } else {
                        s = s+"<td>"+jTable2.getValueAt(i, j)+"</td>";    
                    }
                }
                s = s+"</tr>";
            }
            s = s + "</table><br></center></html>";
        }
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
     
    }//GEN-LAST:event_jButton59MousePressed

    private void jButton60MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton60MousePressed
        Calendar cal = (Calendar) jDateChooser1.getCalendar().clone();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        int Start = ((Long) (cal.getTimeInMillis() / 1000)).intValue();
        cal = (Calendar) jDateChooser2.getCalendar().clone();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        int End = ((Long) (cal.getTimeInMillis() / 1000)).intValue();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String s = "<html><center>Отчет по работе сотрудника<br>" + Users.getString("NAME") + "<br>" +sdf.format(new Date(Start*1000L))+" - "+sdf.format(new Date(End*1000L))+"<br><br>паковка<br><br><table border='1'>";
        s = s + "<tr>";
        for (int j = 0; j < jTable7.getColumnCount(); j++) {
            s = s + "<td width='80' align='center'>" + jTable7.getModel().getColumnName(j) + "</td>";
        }
        s = s + "</tr>";
        for (int i = 0; i < jTable7.getRowCount(); i++) {
            s = s + "<tr>";
            for (int j = 0; j < jTable7.getColumnCount(); j++) {
                if (jTable7.getValueAt(i, j) == null) {
                    s = s + "<td></td>";
                } else {
                    s = s + "<td>" + jTable7.getValueAt(i, j) + "</td>";
                }
            }
            s = s + "</tr>";
        }
        s = s + "</table><br><br>доставка<br><br><table border='1'>";
        s = s + "<tr>";
        for (int j = 0; j < jTable13.getColumnCount(); j++) {
            s = s + "<td width='80' align='center'>" + jTable13.getModel().getColumnName(j) + "</td>";
        }
        s = s + "</tr>";
        for (int i = 0; i < jTable13.getRowCount(); i++) {
            s = s + "<tr>";
            for (int j = 0; j < jTable13.getColumnCount(); j++) {
                if (jTable13.getValueAt(i, j) == null) {
                    s = s + "<td></td>";
                } else {
                    s = s + "<td>" + jTable13.getValueAt(i, j) + "</td>";
                }
            }
            s = s + "</tr>";
        }
        s = s + "</table><br></center></html>";
        
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
    }//GEN-LAST:event_jButton60MousePressed

    private void jButton62MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton62MousePressed
        GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
        MakeTableOfOrderCandyForStorage();
    }//GEN-LAST:event_jButton62MousePressed

    private void jButton61MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton61MousePressed
        String s = "<html><center>Заказ для склада<br><br><table border='1'>";
        s = s + "<tr>";
        for (int j = 0; j < jTable9.getColumnCount(); j++) {
            s = s + "<td width='80' align='center'>" + jTable9.getModel().getColumnName(j) + "</td>";
        }
        s = s + "</tr>";
        for (int i = 0; i < jTable9.getRowCount(); i++) {
            s = s + "<tr>";
            for (int j = 0; j < jTable9.getColumnCount(); j++) {
                if (jTable9.getValueAt(i, j) == null) {
                    s = s + "<td></td>";
                } else {
                    s = s + "<td>" + jTable9.getValueAt(i, j) + "</td>";
                }
            }
            s = s + "</tr>";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        s = s + "</table><br><br></center>Дата: "+sdf.format(Calendar.getInstance().getTime())+"<br>Пользователь: "+CurrentUser.getString("NAME")+"<br></html>";
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
    }//GEN-LAST:event_jButton61MousePressed

    private void jToggleButton1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jToggleButton1StateChanged
        GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
        MakeTreeOfCandies();
    }//GEN-LAST:event_jToggleButton1StateChanged

    private void jToggleButton2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jToggleButton2StateChanged
        GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
        MakeTreeOfCandies();
    }//GEN-LAST:event_jToggleButton2StateChanged

    private void jButton63MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton63MousePressed
        if (db.UpdateSQL("INSERT INTO gift(name,cost_packing) VALUES(?,?)", new Object[]{Gifts.getString("NAME")+"__2",Gifts.getDouble("COST_PACKING")})) {
            if (db.UpdateSQL("INSERT INTO gift_candy(id_gift,id_candy,amount) SELECT (SELECT max(id) FROM gift),id_candy,amount FROM gift_candy WHERE id_gift=?", new Object[]{Gifts.getInt("ID")})) {
                GetGifts();
                MakeTreeOfGifts();
            } else {
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        }
    }//GEN-LAST:event_jButton63MousePressed

    private void jButton64MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton64MousePressed
        String s = "<html><center>Состав набора '"+Gifts.getString("NAME") +"'<br><br><table border='1'>";
        s = s + "<tr>";
        for (int j=0;j<jTable1.getColumnCount();j++) {
            s = s + "<td width='60' align='center'>" + jTable1.getModel().getColumnName(j)+"</td>";    
        }
        s = s + "</tr>";
        for (int i=0;i<jTable1.getRowCount();i++) {
            s = s+"<tr>";
            for (int j=0;j<jTable1.getColumnCount();j++) {
                if (jTable1.getValueAt(i, j)==null) {
                    s = s+"<td></td>";    
                } else {
                    s = s+"<td>"+jTable1.getValueAt(i, j)+"</td>";    
                }
            }
            s = s+"</tr>";
        }
        s = s + "</table><br></center></html>";

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
    }//GEN-LAST:event_jButton64MousePressed

    private void jToggleButton5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jToggleButton5StateChanged

    }//GEN-LAST:event_jToggleButton5StateChanged

    private void jToggleButton6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jToggleButton6StateChanged

    }//GEN-LAST:event_jToggleButton6StateChanged

    private void jButton65MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton65MousePressed
        String s = "<html><center>Список клиентов<br><br><table border='1'>";
        s = s + "<tr>";
        for (int j = 0; j < jTable10.getColumnCount(); j++) {
            s = s + "<td width='60' align='center'>" + jTable10.getModel().getColumnName(j) + "</td>";
        }
        s = s + "</tr>";
        for (int i = 0; i < jTable10.getRowCount(); i++) {
            s = s + "<tr>";
            for (int j = 0; j < jTable10.getColumnCount(); j++) {
                if (jTable10.getValueAt(i, j) == null) {
                    s = s + "<td></td>";
                } else {
                    s = s + "<td>" + jTable10.getValueAt(i, j) + "</td>";
                }
            }
            s = s + "</tr>";
        }
        s = s + "</table><br></center></html>";

        JEditorPane ep = new JEditorPane();
        ep.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        ep.setFont(new Font("Arial", Font.PLAIN, 8));
        ep.setEditorKit(new HTMLEditorKit());
        ep.setText(s);
        try {
            ep.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Не удалось напечатать документ");
        }
    }//GEN-LAST:event_jButton65MousePressed

    private void jButton66MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton66MousePressed
        GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
        MakeTreeOfCandies();
    }//GEN-LAST:event_jButton66MousePressed

    private void jButton67MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton67MousePressed
        GetPackings();
        MakeTreeOfPackings();
    }//GEN-LAST:event_jButton67MousePressed

    private void jButton68MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton68MousePressed
        GetGifts();
        MakeTreeOfGifts();
    }//GEN-LAST:event_jButton68MousePressed

    private void jButton69MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton69MousePressed
        GetCandies(jToggleButton3.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton4.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
        MakeTreeOfCandiesForGift();
        MakeTableOfCandiesForGift();
    }//GEN-LAST:event_jButton69MousePressed

    private void jButton70MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton70MousePressed
        GetClients(jToggleButton5.isSelected());
        MakeTreeOfClients();
    }//GEN-LAST:event_jButton70MousePressed

    private void jButton71MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton71MousePressed
        if (jTabbedPane2.getSelectedIndex()==0) {
            GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
            MakeTreeOfCandiesInStorage();
        } else if (jTabbedPane2.getSelectedIndex()==1) {
            GetFilteredPackings();
            MakeTreeOfPackingsInStorage();
        }
    }//GEN-LAST:event_jButton71MousePressed

    private void jButton72MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton72MousePressed
        JTree tree;
        if (jTabbedPane2.getSelectedIndex()==0) {
            tree = jTree10;
        } else {
            tree = jTree11;                
        }        
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        if (SelectedNode!=null) {
            revenueExpensesDialog.initDialog(false);
        }
    }//GEN-LAST:event_jButton72MousePressed

    private void jButton73MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton73MousePressed
        GetUsers();
        MakeTreeOfUsers();
    }//GEN-LAST:event_jButton73MousePressed

    private void jButton45MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton45MousePressed
        if (!db.UpdateSQL("UPDATE packing SET filename='' WHERE id=?", new Object[]{Packings.getInt("ID")})) {
            JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
        } else {
            RefreshNodePackings();
        }
    }//GEN-LAST:event_jButton45MousePressed

private void jButton78MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton78MousePressed
    if (Users.IsNull()) {
        GetUsers();
    }
    chooseWorkersForPackingDialog.initDialog();
}//GEN-LAST:event_jButton78MousePressed

private void jButton80MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton80MousePressed
        String s = "<html><center>Отчет по доставке<br><br><table border='1'>";
        s = s + "<tr>";
        
            for (int i=0;i<jTable12.getColumnCount();i++) {
                s = s+"<td width='60px'>"+jTable12.getColumnName(i)+"</td>";
            }
            s = s+"</tr>";
            for (int j=0;j<jTable12.getRowCount();j++) {        
                s = s+"<tr>";
                for (int i=0;i<jTable12.getColumnCount();i++) {
                    if (i==0) {
                        int val = (Integer)jTable12.getValueAt(j, i);
                        s = s+"<td>"+(val==Order.ORDER_PREPARE ? "подготовка" : 
                                (val==Order.ORDER_PAY ? "на оплате" : 
                                (val==Order.ORDER_PACK ? "на паковке" : 
                                (val==Order.ORDER_STORAGE ? "на складе" : 
                                (val==Order.ORDER_SEND ? "отправлено" : "закрыто")))))+"</td>";
                    } else {
                        Object ss = jTable12.getValueAt(j, i);
                        s = s+"<td>"+(ss==null || ss.equals(" ") ? "-" : (String)ss)+"</td>";
                    }
                }
                s = s+"</tr>";
            }
            s = s+"</table><br><br></center></html>";        

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
}//GEN-LAST:event_jButton80MousePressed

private void jButton81MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton81MousePressed
        GetDelivery();
        MakeTableOfDelivery();
}//GEN-LAST:event_jButton81MousePressed

    private void jButton82MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton82MousePressed
        if (jTable12.getSelectedRowCount()>1) {
            JOptionPane.showMessageDialog(null, "Необходимо выбрать только одну строку");
            return;
        }
        int SelectedRow = jTable12.getSelectedRow();
        if (SelectedRow!=-1) {
            switch (Delivery.getInt(SelectedRow,"STATE")) {
                case Order.ORDER_STORAGE:
                    showWindowDeliveryCost();
                    break;
                case Order.ORDER_SEND:
                    int id = Delivery.getInt(SelectedRow,"ID");
                    int id_orders = Delivery.getInt(SelectedRow,"ID_ORDERS");
                    int date = (int)(System.currentTimeMillis()/1000);
                    boolean result = db.DoSQL("START TRANSACTION");
                    if (result) {
                        result = db.UpdateSQL("UPDATE delivery SET state=?,date_send=? WHERE id=?",new Object[]{Order.ORDER_DONE,date,id});
                        if (result) {
                            Object[][] obj = db.SelectSQL("SELECT count(id) FROM delivery WHERE state<? AND id_orders=?",new Object[]{Order.ORDER_DONE,id_orders});
                            if ((Long)obj[0][0]==0) {
                                result = db.UpdateSQL("UPDATE orders SET state=? WHERE id=? LIMIT 1",new Object[]{Order.ORDER_DONE,id_orders});
                            }
                        }
                    }
                    if (result) {
                        db.DoSQL("COMMIT");
                        GetDelivery();
                        MakeTableOfDelivery();
                    } else {
                        db.DoSQL("ROLLBACK");
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                    }      
                    break;
            }
        }
    }//GEN-LAST:event_jButton82MousePressed

    private void jTable12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable12MousePressed
        SelectNodeOfTableDelivery();
    }//GEN-LAST:event_jTable12MousePressed

    private void jTable12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable12KeyReleased
        SelectNodeOfTableDelivery();
    }//GEN-LAST:event_jTable12KeyReleased

    private void jButton84MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton84MousePressed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(ChoosedDirectory);
        chooser.setDialogTitle("Выберите папку для сохранения файла");
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            ChoosedDirectory = chooser.getCurrentDirectory();
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile.isDirectory()) {
                if (!Gift_Candy.IsNull()) {
                    try {
                        WritableWorkbook workbook;
                        WorkbookSettings ws = new WorkbookSettings();
                        ws.setLocale(new Locale("ru", "RU"));
                        workbook = Workbook.createWorkbook(new File(selectedFile.getPath() +"\\"+ Gifts.getString("NAME") + ".xls"), ws);
                        WritableSheet s = workbook.createSheet("Состав", 0);
                        s.getSettings().setDefaultColumnWidth(10);
                        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                        WritableCellFormat cf = new WritableCellFormat(wf);
                        jxl.write.Label l;
                        jxl.write.Number n;
                        l = new jxl.write.Label(0, 0, "Фабрика", cf);
                        s.addCell(l);
                        l = new jxl.write.Label(1, 0, "Конфета", cf);
                        s.addCell(l);
                        l = new jxl.write.Label(2, 0, "Количество", cf);
                        s.addCell(l);
                        int SummAmount = 0;
                        for (int i = 0; i < Gift_Candy.getLength(); i++) {
                            l = new jxl.write.Label(0, i + 1, Gift_Candy.getString(i, "FACTORY_NAME"), cf);
                            s.addCell(l);
                            l = new jxl.write.Label(1, i + 1, Gift_Candy.getString(i, "CANDY_NAME"), cf);
                            s.addCell(l);
                            int amount = Gift_Candy.getInt(i, "AMOUNT");
                            n = new jxl.write.Number(2, i + 1, amount, cf);
                            s.addCell(n);
                            SummAmount += amount;
                        }
                        l = new jxl.write.Label(1, Gift_Candy.getLength()+1, "ИТОГО:", cf);
                        s.addCell(l);
                        n = new jxl.write.Number(2, Gift_Candy.getLength()+1, SummAmount, cf);
                        s.addCell(n);
                        workbook.write();
                        workbook.close();
                        JOptionPane.showMessageDialog(null, "Документ сохранен в " + selectedFile.getPath() + Gifts.getString("NAME") + ".xls");
                    } catch (IOException | WriteException | HeadlessException ex) {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить документ");
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton84MousePressed

    private void jButton85MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton85MousePressed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(ChoosedDirectory);
        chooser.setDialogTitle("Выберите папку для сохранения файла");
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            ChoosedDirectory = chooser.getCurrentDirectory();
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile.isDirectory()) {
                
                if (!Candies.IsNull()) {
                    ArrayList vec = new ArrayList();
                    int last_factory = Candies.getInt(0, "ID_FACTORY");
                    int id_factory;
                    double Summ_weight_factory = 0;
                    double Summ_cost_factory = 0;
                    double Summ_weight = 0;
                    double Summ_cost = 0;
                    double discount = Candies.getLength()>0 ? Candies.getDouble(0,"DISCOUNT") : 0;
//                    String excelWeight;
//                    String excelPrice;
//                    int correctCount = 3;
                    for (int i = 0; i < Candies.getLength(); i++) {
                        id_factory = Candies.getInt(i, "ID_FACTORY");
                        if (id_factory != last_factory) {
                            if (Summ_cost_factory > 0) {
                                last_factory = id_factory;
                                Summ_weight += Summ_weight_factory;
                                Summ_cost += Summ_cost_factory * (100 - discount) / 100;
                                vec.add(new String[]{"ИТОГО:", "", "", FormatKG.format(Summ_weight_factory), "", FormatUAH.format(Summ_cost_factory)});
                                vec.add(new String[]{"", "", "", "", "СКИДКА,%", FormatKG.format(discount)});
                                vec.add(new String[]{"", "", "", "", "", FormatUAH.format(Summ_cost_factory * (100 - discount) / 100)});
                                vec.add(new String[]{"", "", "", "", "", ""});
                                discount = Candies.getDouble(i, "DISCOUNT");
                                Summ_weight_factory = 0;
                                Summ_cost_factory = 0;
//                                correctCount += 4;
                            } else {
                                last_factory = id_factory;
                            }
                        }
                        if (Candies.getInt(i, "ID") != 0) {
                            int storage = Candies.getInt(i, "STORAGE");
                            int reserved = Candies.getInt(i, "RESERVED");
                            double amount_boxes = (storage - reserved) * 1.0 / Candies.getInt(i, "AMOUNT_IN_BOX");
                            if (amount_boxes < 0) {
                                String[] obj = new String[6];
                                int amount = ((Double) (-Math.floor(amount_boxes))).intValue();
                                obj[0] = Candies.getString(i, "FACTORY_NAME");
                                obj[1] = Candies.getString(i, "CANDY_NAME");
                                obj[2] = Integer.toString(amount);
//                                excelWeight = "=C"+correctCount+"*" + String.valueOf(FormatKG.format(Candies.getDouble(i, "BOX_WEIGHT")));
//                                obj[3] = excelWeight;
                                double weight = Candies.getDouble(i, "BOX_WEIGHT") * amount;
                                obj[3] = FormatKG.format(weight);
                                obj[4] = FormatUAH.format(Candies.getDouble(i, "COST_KG"));
//                                excelPrice = "=D" +correctCount+ "*E" +correctCount;
//                                obj[5] = excelPrice;
                                double cost = weight * Candies.getDouble(i, "COST_KG");
                                obj[5] = FormatUAH.format(cost);
                                Summ_weight_factory += weight;
                                Summ_cost_factory += cost;
                                vec.add(obj);
//                                correctCount++;
                            }
                        }
                        
                    }
                    Summ_weight+=Summ_weight_factory;
                    Summ_cost+=Summ_cost_factory*(100-discount)/100;
                    vec.add(new String[]{"ИТОГО:","","",FormatKG.format(Summ_weight_factory),"",FormatUAH.format(Summ_cost_factory)});
                    vec.add(new String[]{"","","","","СКИДКА,%",FormatKG.format(discount)});
                    vec.add(new String[]{"","","","","",FormatUAH.format(Summ_cost_factory*(100-discount)/100)});
                    vec.add(new String[]{"","","","","",""});            
                    vec.add(new String[]{"ИТОГО:","","",FormatKG.format(Summ_weight),"",FormatUAH.format(Summ_cost)});

                    try {
                        WritableWorkbook workbook;
                        WorkbookSettings ws = new WorkbookSettings();
                        ws.setLocale(new Locale("ru", "RU"));
                        workbook = Workbook.createWorkbook(new File(selectedFile.getPath() +"\\заказ_конфет_для_склада.xls"), ws);
                        WritableSheet s = workbook.createSheet("заказ конфет для склада", 0);
                        s.getSettings().setDefaultColumnWidth(10);
                        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                        WritableCellFormat cf = new WritableCellFormat(wf);
                        jxl.write.Label l;
                        jxl.write.Number n;
                        String[] ColumnNames = new String[]{"Фабрика", "Конфета", "Заказ, ящ.","Вес, кг","Цена 1 кг","Цена, грн"};
                        for (int i=0;i<ColumnNames.length;i++) {
                            l = new jxl.write.Label(i, 0, ColumnNames[i], cf);
                            s.addCell(l);
                        }
                        String[] str_mas;
                        for (int i=0;i<vec.size();i++) {
                            str_mas = (String[])vec.get(i);
                            for (int j=0;j<str_mas.length;j++) {
                                l = new jxl.write.Label(j, i+2, str_mas[j], cf);
                                s.addCell(l);
                            }
                        }
                        workbook.write();
                        workbook.close();
                        JOptionPane.showMessageDialog(null, "Документ сохранен в " + selectedFile.getPath() + "\\заказ_конфет_для_склада.xls");
                    } catch (IOException | WriteException | HeadlessException ex) {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить документ");
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton85MousePressed

    private void jButton89MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton89MousePressed
        int index = jTable14.getSelectedRow();
        if (index!=-1) {
            if (JOptionPane.showConfirmDialog(null, "Удалить доставку?","Удаление доставки",JOptionPane.YES_NO_OPTION) == 0) {
                if (db.UpdateSQL("DELETE FROM delivery WHERE id=?",new Object[]{DeliveryOrder.getInt(index,"ID")})) {
                    SelectNodeOfTableOrders();
                } else {
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");                
                }
            }
        }
    }//GEN-LAST:event_jButton89MousePressed

    private void jButton90MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton90MousePressed
        deliveryDialog.initDialog(false);
    }//GEN-LAST:event_jButton90MousePressed

    private void jButton91MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton91MousePressed
        deliveryDialog.initDialog(true);
    }//GEN-LAST:event_jButton91MousePressed

    private void jButton97MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton97MousePressed
        int[] rows = jTable12.getSelectedRows();
        if (rows.length==0) {
            //do nothing
        } else if (rows.length==1) {
            String s = "<html><center>Отчет по доставке</center><br><br><b>Статус доставки:</b> ";
            int state = Delivery.getInt(rows[0],"STATE");
            if (state==Order.ORDER_STORAGE) {
                s = s+ "на складе";
            } else if (state==Order.ORDER_SEND) {
                s = s+ "в дороге";
            } else {
                s = s+ "доставлено";
            }
            s = s + "<br><br><b>Дата доставки: </b>"+new SimpleDateFormat("dd-MMM-yyyy").format(new Date(Delivery.getInt(rows[0],"DATE_TIME")*1000L))+"<br><br>";
            s = s + "<b>ТТН: </b>"+Delivery.getString(rows[0],"TTN")+"<br><br>";
            s = s + "<b>Клиент: </b>"+Delivery.getString(rows[0],"CLIENT")+"<br><br>";
            s = s + "<b>Контактное лицо:</b><br>"+Delivery.getString(rows[0],"CONTACT")+"<br><br>";
            s = s + "<b>Тип доставки: </b>"+Delivery.getString(rows[0],"DELIVERY_TYPE")+"<br><br>";
            s = s + "<b>Адрес:</b><br>"+Delivery.getString(rows[0],"ADDRESS")+"<br><br>";
            s = s + "<b>Содержание доставки:</b><br>"+Delivery.getString(rows[0],"CONTENT")+"<br><br>";
            s = s + "<b>Кто платит: </b>"+Delivery.getString(rows[0],"WHO_PAYS")+"<br><br>";
            int typePay = Delivery.getInt(rows[0],"TYPE_PAY_ORDER"); //2 и 3 и 5 - безнал
            s = s + "<b>Долг: </b>"+FormatUAH.format(Delivery.getDouble(rows[0],"DEBT"))+(typePay==2 || typePay==3 ? " БН" : "")+"<br><br>";
            s = s + "<b>Презент: </b>"+Delivery.getString(rows[0],"PRESENT")+"<br><br>";
            s = s + "<b>Комментарий:</b><br>"+Delivery.getString(rows[0],"COMM")+"<br><br>";
            s = s + "</html>";
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
        } else {
            String s = "<html><center>Отчет по доставке<br><br><table border=1><tr>";
            for (int i=0;i<jTable12.getColumnCount();i++) {
                s = s+"<td width='60px'>"+jTable12.getColumnName(i)+"</td>";
            }
            s = s+"</tr>";
            for (int j=0;j<rows.length;j++) {        
                s = s+"<tr>";
                for (int i=0;i<jTable12.getColumnCount();i++) {
                    if (i==0) {
                        int val = (Integer)jTable12.getValueAt(rows[j], i);
                        s = s+"<td>"+(val==Order.ORDER_PREPARE ? "подготовка" : (val==Order.ORDER_PAY ? "на оплате" : (val==Order.ORDER_PACK ? "на паковке" : (val==Order.ORDER_STORAGE ? "на складе" : (val==Order.ORDER_SEND ? "отправлено" : "закрыто")))))+"</td>";
                    } else {
                        Object ss = (String)jTable12.getValueAt(rows[j], i);
                        s = s+"<td>"+(ss==null || ss.equals(" ") ? "-" : (Object)ss)+"</td>";
                    }
                }
                s = s+"</tr>";
            }
            s = s+"</table><br><br></center></html>";
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
    }//GEN-LAST:event_jButton97MousePressed

    private void jButton100MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton100MousePressed
        if (Orders.getInt("STATE")==Order.ORDER_PAY) {
            int id_orders = Orders.getInt("ID"); 
            boolean result = db.DoSQL("START TRANSACTION");
            if (result) {
                result = db.UpdateSQL("UPDATE orders SET state=? WHERE id=?", new Object[]{Order.ORDER_PREPARE, id_orders});
                if (result) {
                    db.UpdateSQL("UPDATE delivery SET state=? WHERE id_orders=?",new Object[]{Order.ORDER_PREPARE,id_orders});
                    for (int i=0;i<SubOrders.getLength();i++) {
                        result = result && db.UpdateSQL("UPDATE packing, suborder SET packing.reserved = packing.reserved - suborder.amount WHERE packing.ID=suborder.id_packing AND suborder.ID=?",new Object[]{SubOrders.getInt(i,"ID")});
                    }
                }
            }
            if (result) {
                db.DoSQL("COMMIT");
                GetOrders();
                MakeTableOfOrders();
            } else {
                db.DoSQL("ROLLBACK");
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        } else if (Orders.getInt("STATE")==Order.ORDER_PACK) {
            boolean already_packed = false;
            for (int i=0;i<SubOrders.getLength();i++) {
                if (SubOrders.getInt(i,"PACKED")>0) {
                    already_packed = true;
                    break;
                }
            }
            if (already_packed) {
                JOptionPane.showMessageDialog(null, "Нельзя вернуть заказ на оплату. Уже начата паковка!");
            } else {
                int id_orders = Orders.getInt("ID"); 
                boolean result = db.DoSQL("START TRANSACTION");
                if (result) {
                    result = db.UpdateSQL("UPDATE orders SET state=? WHERE id=?", new Object[]{Order.ORDER_PAY, id_orders});
                    if (result) {
                        db.UpdateSQL("UPDATE delivery SET state=? WHERE id_orders=?",new Object[]{Order.ORDER_PAY,id_orders});
                        for (int i=0;i<SubOrders.getLength();i++) {
                            result = result && db.UpdateSQL("UPDATE candy, gift_candy, suborder SET candy.reserved = candy.reserved - suborder.amount*gift_candy.amount WHERE gift_candy.ID_CANDY = candy.ID AND gift_candy.ID_GIFT=suborder.ID_GIFT AND suborder.ID=?",new Object[]{SubOrders.getInt(i,"ID")}); 
                        }                    
                    }
                }
                if (result) {
                    db.DoSQL("COMMIT");
                    GetOrders();
                    MakeTableOfOrders();
                } else {
                    db.DoSQL("ROLLBACK");
                    JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
                }                
            }
        } else if (Orders.getInt("STATE")==Order.ORDER_STORAGE) {
            int id_orders = Orders.getInt("ID"); 
            boolean result = db.DoSQL("START TRANSACTION");
            if (result) {
                result = db.UpdateSQL("UPDATE orders SET state=? WHERE id=?", new Object[]{Order.ORDER_PACK, id_orders});
                if (result) {
                    db.UpdateSQL("UPDATE delivery SET state=? WHERE id_orders=?",new Object[]{Order.ORDER_PACK,id_orders});
//                    for (int i=0;i<SubOrders.getLength();i++) {
//                        result = result && db.UpdateSQL("UPDATE packing, suborder SET packing.reserved = packing.reserved - suborder.amount WHERE packing.ID=suborder.id_packing AND suborder.ID=?",new Object[]{SubOrders.getInt(i,"ID")});
//                    }
                }
            }
            if (result) {
                db.DoSQL("COMMIT");
                GetOrders();
                MakeTableOfOrders();
            } else {
                db.DoSQL("ROLLBACK");
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        } else if (Orders.getInt("STATE")==Order.ORDER_SEND) {
            int id_orders = Orders.getInt("ID"); 
            boolean result = db.DoSQL("START TRANSACTION");
            if (result) {
                result = db.UpdateSQL("UPDATE orders SET state=? WHERE id=?", new Object[]{Order.ORDER_STORAGE, id_orders});
                if (result) {
                    db.UpdateSQL("UPDATE delivery SET state=? WHERE id_orders=?",new Object[]{Order.ORDER_STORAGE,id_orders});
//                    for (int i=0;i<SubOrders.getLength();i++) {
//                        result = result && db.UpdateSQL("UPDATE packing, suborder SET packing.reserved = packing.reserved - suborder.amount WHERE packing.ID=suborder.id_packing AND suborder.ID=?",new Object[]{SubOrders.getInt(i,"ID")});
//                    }
                }
            }
            if (result) {
                db.DoSQL("COMMIT");
                GetOrders();
                MakeTableOfOrders();
            } else {
                db.DoSQL("ROLLBACK");
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        } else if (Orders.getInt("STATE")==Order.ORDER_DONE) {
            int id_orders = Orders.getInt("ID"); 
            boolean result = db.DoSQL("START TRANSACTION");
            if (result) {
                result = db.UpdateSQL("UPDATE orders SET state=? WHERE id=?", new Object[]{Order.ORDER_SEND, id_orders});
                if (result) {
                    db.UpdateSQL("UPDATE delivery SET state=? WHERE id_orders=?",new Object[]{Order.ORDER_SEND,id_orders});
//                    for (int i=0;i<SubOrders.getLength();i++) {
//                        result = result && db.UpdateSQL("UPDATE packing, suborder SET packing.reserved = packing.reserved - suborder.amount WHERE packing.ID=suborder.id_packing AND suborder.ID=?",new Object[]{SubOrders.getInt(i,"ID")});
//                    }
                }
            }
            if (result) {
                db.DoSQL("COMMIT");
                GetOrders();
                MakeTableOfOrders();
            } else {
                db.DoSQL("ROLLBACK");
                JOptionPane.showMessageDialog(null, "Не удалось сохранить изменения");
            }
        }
    }//GEN-LAST:event_jButton100MousePressed

    private void jButton105MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton105MousePressed
        if (Candies.IsNull()) {
            GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
        }
        if (Packings.IsNull()) {
            GetPackings();
        }

        String s = "<html><center><b>ЛИСТ ИНВЕНТАРИЗАЦИИ</b><br><br><table border='1px'>";
        s = s+"<tr><td align='center'>Фабрика</td><td align='center'>Конфета</td><td align='center'>На складе</td><td>Пометки</td></tr>";
        for (int i = 0; i < Candies.getLength(); i++) {
            s = s+"<tr><td>"+Candies.getString(i,"FACTORY_NAME")+"</td><td>"+Candies.getString(i,"CANDY_NAME")+"</td><td>"+FormatUAH.format(Candies.getInt(i,"STORAGE")*1.0/Candies.getInt(i,"AMOUNT_IN_BOX"))+"</td><td></td></tr>";
        }
        s = s + "</table><br><br></center></html>";
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
        
        
        s = "<html><center><b>ЛИСТ ИНВЕНТАРИЗАЦИИ</b><br><br><table border='1px'>";
        s = s+"<tr><td align='center'>Тип</td><td align='center'>Название</td><td align='center'>На складе</td><td>Пометки</td></tr>";
        for (int i = 0; i < Packings.getLength(); i++) {
            int t = Packings.getInt(i,"TYPE");
            String type = (t==0 ? "картон" : (t==1 ? "пакет" : (t==2 ? "туба" : (t==3 ? "жесть" : "игрушка"))));
            s = s+"<tr><td>"+type+"</td><td>"+Packings.getString(i,"NAME")+" №"+Packings.getInt(i,"NUMBER")+"</td><td>"+Packings.getInt(i,"STORAGE")+"</td><td></td></tr>";
        }
        s = s + "</table><br><br></center></html>";
        ep.setText(s);
        try {
            ep.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Не удалось напечатать документ");
        }        
    }//GEN-LAST:event_jButton105MousePressed

    private void jTextField38KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField38KeyReleased
        GetClients(jToggleButton5.isSelected());
        MakeTreeOfClients();
    }//GEN-LAST:event_jTextField38KeyReleased

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (!Orders.IsNull()) {
            GetOrders();
            MakeTableOfOrders();
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField39KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField39KeyReleased
        GetOrders();
        MakeTableOfOrders();
    }//GEN-LAST:event_jTextField39KeyReleased

    private String GetNameOfMonth(int index) {
        switch (index) {
            case Calendar.JANUARY:
                return "січня";
            case Calendar.FEBRUARY:
                return "лютого";
            case Calendar.MARCH:
                return "березня";
            case Calendar.APRIL:
                return "квітня";
            case Calendar.MAY:
                return "травня";
            case Calendar.JUNE:
                return "червня";
            case Calendar.JULY:
                return "липня";
            case Calendar.AUGUST:
                return "серпня";
            case Calendar.SEPTEMBER:
                return "вересня";
            case Calendar.OCTOBER:
                return "жовтня";
            case Calendar.NOVEMBER:
                return "листопада";
            case Calendar.DECEMBER:
                return "грудня";
            default:
                return "";
        }
    }
    
    private void jButton108MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton108MousePressed
        if (jTable3.getSelectedRow()==-1) {
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(ChoosedDirectory);
        chooser.setDialogTitle("Выберите папку для сохранения файла");
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            ChoosedDirectory = chooser.getCurrentDirectory();
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile.isDirectory()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                        WritableWorkbook workbook;
                        WorkbookSettings ws = new WorkbookSettings();
                        ws.setLocale(new Locale("ru", "RU"));
                        workbook = Workbook.createWorkbook(new File(selectedFile.getPath() +"\\"+ Orders.getString("CLIENT_NAME")+"_"+sdf.format(new Date(Orders.getLong("DATE_TIME")))+ ".xls"), ws);
                        WritableSheet s = workbook.createSheet("Замовлення", 0);
                        jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#0.00");
                        WritableCellFormat cf1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,false,UnderlineStyle.SINGLE));
                        WritableCellFormat cf2 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
                        WritableCellFormat cf3 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD));
                        WritableCellFormat cf4 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
                        
                        WritableCellFormat cf4_1 = new WritableCellFormat(nf);
                        cf4_1.setAlignment(Alignment.CENTRE);
                        cf4_1.setBorder(Border.ALL, BorderLineStyle.THIN);

                        WritableCellFormat cf5 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));                        
                        WritableCellFormat cf6 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
                        
                        WritableCellFormat cf7 = new WritableCellFormat(nf);
                        cf7.setFont(new WritableFont(WritableFont.ARIAL, 10));
                        cf7.setAlignment(Alignment.RIGHT);
                        cf7.setBorder(Border.ALL, BorderLineStyle.THIN);
                        
                        WritableCellFormat cf8 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD));
                        cf8.setAlignment(Alignment.RIGHT);
                        
                        WritableCellFormat cf9 = new WritableCellFormat(nf);
                        cf9.setFont(new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD));
                        cf9.setAlignment(Alignment.RIGHT);
                        cf9.setBorder(Border.ALL, BorderLineStyle.THIN);
                        
                        WritableCellFormat cf10 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 11));
                        WritableCellFormat cf11 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD));
                        WritableCellFormat cf12 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
                        cf3.setAlignment(Alignment.CENTRE);
                        cf4.setAlignment(Alignment.CENTRE);
                        cf4.setBorder(Border.ALL, BorderLineStyle.THIN);
                        cf5.setAlignment(Alignment.CENTRE);
                        cf5.setBackground(Colour.GREY_25_PERCENT);
                        cf5.setBorder(Border.ALL, BorderLineStyle.THIN);
                        cf6.setAlignment(Alignment.LEFT);
                        cf6.setBorder(Border.ALL, BorderLineStyle.THIN);
                        cf12.setAlignment(Alignment.RIGHT);
                        
                        s.addCell(new jxl.write.Label(0, 0, "Постачальник", cf1));
                        s.addCell(new jxl.write.Label(2, 0, "ФОП Бурдін Р.А.", cf2));
                        s.addCell(new jxl.write.Label(2, 1, "ЄДРПОУ 3211013395, тел. (044) 599-13-00, (099) 651-44-00", cf2));
                        s.addCell(new jxl.write.Label(2, 2, "Р/р 26007000027890 МФО 30023 ПАТ «Укрсоцбанк»", cf2));
                        s.addCell(new jxl.write.Label(2, 3, "ІПН 3211013395, номер свідоцтва 271143", cf2));
                        s.addCell(new jxl.write.Label(2, 4, "Не є платником податку на прибуток на загальних підставах", cf2));
                        s.addCell(new jxl.write.Label(2, 5, "Адреса 02132 м. Київ, ул. Ареф'єва, 16", cf2));
                        s.addCell(new jxl.write.Label(0, 6, "Одержувач", cf1));
                        s.addCell(new jxl.write.Label(2, 6, Orders.getString("CLIENT_NAME"), cf2));
                        
                      
                        s.mergeCells(0, 0, 1, 0);
                        s.mergeCells(0, 1, 1, 1);
                        s.setColumnView(0, 5);
                        s.setColumnView(1, 10);
                        s.setColumnView(2, 10);
                        s.setColumnView(3, 10);
                        s.setColumnView(4, 5);
                        s.setColumnView(5, 10);
                        s.setColumnView(6, 15);
                        s.setColumnView(7, 15);
                        
                        s.mergeCells(0,11,7, 11);
                        db.UpdateSQL("UPDATE constants SET current_number=current_number+1",null);
                        GetConstants();
                        s.addCell(new jxl.write.Label(0, 11, "Видаткова накладна № "+Constants.getInt("CURRENT_NUMBER"), cf3));
                        s.mergeCells(0, 12, 7,12);
                        Calendar cal = Calendar.getInstance();
                        String today = "від "+Integer.toString(cal.get(Calendar.DAY_OF_MONTH))+" "+GetNameOfMonth(cal.get(Calendar.MONTH))+" "+cal.get(Calendar.YEAR)+" р.";
                        s.addCell(new jxl.write.Label(0, 12, today, cf3));                        
                        
                        s.addCell(new jxl.write.Label(0, 14, "№", cf5));                        
                        s.mergeCells(1, 14, 3, 14);
                        s.addCell(new jxl.write.Label(1, 14, "Товар", cf5));                        
                        s.addCell(new jxl.write.Label(4, 14, "Од.", cf5));                        
                        s.addCell(new jxl.write.Label(5, 14, "Кількість", cf5));                        
                        s.addCell(new jxl.write.Label(6, 14, "Ціна", cf5));                        
                        s.addCell(new jxl.write.Label(7, 14, "Сума", cf5));                        
                        
                        double all_sum = 0;
                        double sum;
                        for (int i=0;i<SubOrders.getLength();i++) {
                            s.addCell(new jxl.write.Number(0, 15+i, i+1, cf4));
                            s.mergeCells(1, 15+i, 3, 15+i);
                            s.addCell(new jxl.write.Label(1, 15+i, "Подарунковий набір "+SubOrders.getString(i,"GIFT_NAME"), cf6));
                            s.addCell(new jxl.write.Label(4, 15+i, "шт.", cf4));
                            s.addCell(new jxl.write.Number(5, 15+i, SubOrders.getInt(i,"AMOUNT"), cf4));
                            s.addCell(new jxl.write.Number(6, 15+i, SubOrders.getDouble(i,"COST"), cf4_1));
                            sum = SubOrders.getDouble(i,"COST")*SubOrders.getInt(i,"AMOUNT");
                            all_sum+=sum;
                            s.addCell(new jxl.write.Number(7, 15+i, sum, cf7));
                        }
                        double delivery_cost = ExtendedOrder.getDouble("DELIVERY_COST");
                        double discount = ExtendedOrder.getDouble("DISCOUNT");
                        double debt = delivery_cost + all_sum*(1-(discount*0.01)) - ExtendedOrder.getDouble("PREPAY");
                        s.addCell(new jxl.write.Label(6, 15+SubOrders.getLength(), "Разом:", cf8));
                        s.addCell(new jxl.write.Number(7, 15+SubOrders.getLength(), all_sum, cf9));
                        s.addCell(new jxl.write.Label(6, 15+SubOrders.getLength()+1, "Доставка:", cf8));
                        s.addCell(new jxl.write.Number(7, 15+SubOrders.getLength()+1, delivery_cost, cf9));
                        s.addCell(new jxl.write.Label(6, 15+SubOrders.getLength()+2, "Передплата:", cf8));
                        s.addCell(new jxl.write.Number(7, 15+SubOrders.getLength()+2, ExtendedOrder.getDouble("PREPAY"), cf9));
                        s.addCell(new jxl.write.Label(6, 15+SubOrders.getLength()+2, "Знижка:", cf8));
                        s.addCell(new jxl.write.Number(7, 15+SubOrders.getLength()+2, discount, cf9));
                        s.addCell(new jxl.write.Label(6, 15+SubOrders.getLength()+3, "Борг:", cf8));
                        s.addCell(new jxl.write.Number(7, 15+SubOrders.getLength()+3, debt, cf9));
                        
                        s.addCell(new jxl.write.Label(0, 15+SubOrders.getLength()+5, "Всього на суму:", cf10));
                        all_sum = Math.round((all_sum+delivery_cost)*100)*1.0d/100;
                        String money = new MoneyToString(all_sum).num2str();
                        money = money.substring(0, 1).toUpperCase()+money.substring(1);
                        s.addCell(new jxl.write.Label(0, 15+SubOrders.getLength()+6, money, cf11));
                        
                        s.addCell(new jxl.write.Label(1, 15+SubOrders.getLength()+10, "Відвантажив ", cf12));
                        s.addCell(new jxl.write.Label(2, 15+SubOrders.getLength()+10, "_________________", cf2));
                        s.addCell(new jxl.write.Label(5, 15+SubOrders.getLength()+10, "Отримав(ла) ", cf12));
                        s.addCell(new jxl.write.Label(6, 15+SubOrders.getLength()+10, "_________________", cf2));
                        
                        workbook.write();
                        workbook.close();
                        JOptionPane.showMessageDialog(null, "Документ сохранен в " + selectedFile.getPath() +"\\"+ Orders.getString("CLIENT_NAME")+"_"+sdf.format(new Date(Orders.getLong("DATE_TIME")))+ ".xls");
                    } catch (IOException | WriteException | HeadlessException ex) {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить документ");
                    }
            }
        }
    }//GEN-LAST:event_jButton108MousePressed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        GetDelivery();
        MakeTableOfDelivery();
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        GetDelivery();
        MakeTableOfDelivery();
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        GetDelivery();
        MakeTableOfDelivery();
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        GetOrders();
        MakeTableOfOrders();
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton7ActionPerformed
        GetOrders();
        MakeTableOfOrders();
    }//GEN-LAST:event_jRadioButton7ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        if (jComboBox4.getItemCount()>0) {
            try {
                GetOrders();
                MakeTableOfOrders();
            } catch (Exception ex) {}
        }
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        chooseConsolidatedOrdersDialog.initDialog();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        if (!SubOrders.IsNull()) {
            if (Users.IsNull()) {
                GetUsers();
            }
            choosePackingWorkersDialog.initDialog(Users);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton74ActionPerformed
        jPopupMenu1.show(jButton74, 0, 0);
    }//GEN-LAST:event_jButton74ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        labelPrintDialog.initDialog();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        if (jComboBox5.getItemCount()>0) {
            GetDelivery();
            MakeTableOfDelivery();
        }
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jButton75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton75ActionPerformed
        expensesDialog.initDialog(false);
    }//GEN-LAST:event_jButton75ActionPerformed

    private void jButton111ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton111ActionPerformed
        int row = jTable20.getSelectedRow();
        if ((row!=-1) && (row!=jTable20.getRowCount()-1)) {
            if (JOptionPane.showConfirmDialog(null, "Удалить запись?", "Удаление записи расхода", JOptionPane.YES_NO_OPTION) == 0) {
                db.UpdateSQL("DELETE FROM expense WHERE id=?",new Object[]{Expenses.getInt(row,"ID")});
                GetExpenses();
                MakeTableOfExpenses();
            }
        }
    }//GEN-LAST:event_jButton111ActionPerformed

    private void jTextField45KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField45KeyReleased
        GetExpenses();
        MakeTableOfExpenses();
    }//GEN-LAST:event_jTextField45KeyReleased

    private void jButton114ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton114ActionPerformed
        expensesDialog.initDialog(true);
    }//GEN-LAST:event_jButton114ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        jToggleButton1.setSelected(true);
        jButton69.doClick();
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        jToggleButton2.setSelected(true);
        jButton69.doClick();
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        deleteOrCancelOrder();
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        if (sessionIsBlocked) {
            unblockSession();
        } else {
            login();
        }
    }//GEN-LAST:event_jButton50ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        changeView_Table_Tree_CandiesForPackage();
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton115ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton115ActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(ChoosedDirectory);
        chooser.setDialogTitle("Выберите папку для сохранения файла");
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            ChoosedDirectory = chooser.getCurrentDirectory();
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile.isDirectory()) {
                    try {
                        WritableWorkbook workbook;
                        WorkbookSettings ws = new WorkbookSettings();
                        ws.setLocale(new Locale("ru", "RU"));
                        workbook = Workbook.createWorkbook(new File(selectedFile.getPath() +"\\Клиенты.xls"), ws);
                        WritableSheet s = workbook.createSheet("клиенты", 0);
                        
                        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                        WritableCellFormat cf = new WritableCellFormat(wf);
                        cf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); 
                        cf.setAlignment(Alignment.CENTRE);
                        
                        jxl.write.WritableCellFormat cf2 = new jxl.write.WritableCellFormat();  
                        cf2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);  
                        cf.setAlignment(Alignment.CENTRE);
              
                        s.setColumnView(0, 30);
                        s.setColumnView(1, 20);
                        s.setColumnView(2, 15);
                        s.setColumnView(3, 30);
                        s.setColumnView(4, 30);
                        s.setColumnView(5, 30);
                        s.setColumnView(6, 30);
                        s.setColumnView(7, 30);
                        s.setColumnView(8, 30);
                        s.setColumnView(9, 30);
                        s.setColumnView(10, 30);
                        s.setColumnView(11, 30);
                        
                        for (int i=0;i<jTable10.getColumnCount();i++) {
                            jxl.write.Label l = new jxl.write.Label(i, 0, jTable10.getColumnName(i), cf);
                            s.addCell(l);
                        }
                        
                        jxl.write.Label l;
                        
                        for (int j=0;j<jTable10.getRowCount();j++) {
                            String value = jTable10.getValueAt(j, 0).toString();
                            value = value.replace("<html><font color='gray'>", "");
                            value = value.replace("<html><font color='blue'>", "");
                            value = value.replace("</font></html>", "");
                            System.out.println(value);
//                            l  = new jxl.write.Label(0, j+1, value, cf2);
                        }
                        
                        for (int j=0;j<jTable10.getRowCount();j++) {
                            for (int i=0;i<jTable10.getColumnCount();i++) {
                                if (i==0) {
                                    String value = jTable10.getValueAt(j, i).toString();
                                    value = value.replace("<html><font color='gray'>", "");
                                    value = value.replace("<html><font color='blue'>", "");
                                    value = value.replace("</font></html>", "");
                                    l  = new jxl.write.Label(i, j+1, value, cf2);                                    
                                } else {
                                    l = new jxl.write.Label(i, j+1, jTable10.getValueAt(j, i).toString(), cf2);
                                }
                                s.addCell(l);
                            }
                        }
                        workbook.write();
                        workbook.close();
                        JOptionPane.showMessageDialog(null, "Документ сохранен в " + selectedFile.getPath() + "\\Клиенты.xls");
                    } catch (IOException | WriteException | HeadlessException ex) {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить документ");
                    }
                }
            }
        
    }//GEN-LAST:event_jButton115ActionPerformed

    private void jButton116MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton116MousePressed
        if (JOptionPane.showConfirmDialog(null, "Остатки и резерв по складу (КОНФЕТЫ) будут очищены.\nДанное действие нельзя будет отменить.\nПродолжить?", "Очистка склада", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            db.UpdateSQL("UPDATE candy SET storage=0, reserved=0", new Object[]{});
            jTabbedPane2StateChanged(null);
            JOptionPane.showMessageDialog(null, "Выполнена очистка склада");
        }
    }//GEN-LAST:event_jButton116MousePressed

    private void jButton117MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton117MousePressed
        GetPackings();
        MakeTableOfOrderPackingForStorage();
    }//GEN-LAST:event_jButton117MousePressed

    private void jButton118MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton118MousePressed
        String s = "<html><center>Заказ для склада<br><br><table border='1'>";
        s = s + "<tr>";
        for (int j = 0; j < jTable22.getColumnCount()-1; j++) {
            s = s + "<td width='80' align='center'>" + jTable22.getModel().getColumnName(j) + "</td>";
        }
        s = s + "</tr>";
        for (int i = 0; i < jTable22.getRowCount(); i++) {
            s = s + "<tr>";
            for (int j = 0; j < jTable22.getColumnCount()-1; j++) {
                if (jTable22.getValueAt(i, j) == null) {
                    s = s + "<td></td>";
                } else if (j==0) { //image
                    s = s + "<td>" + "<img src='file://localhost/c:/temp/" + Packings.getString((Integer)jTable22.getValueAt(i,5),"FILENAME") + "' width='150px' height='150px'>"+"</td>";
                } else {
                    s = s + "<td>" + jTable22.getValueAt(i, j) + "</td>";
                }
            }
            s = s + "</tr>";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        s = s + "</table><br><br></center>Дата: "+sdf.format(Calendar.getInstance().getTime())+"<br>Пользователь: "+CurrentUser.getString("NAME")+"<br></html>";
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
    }//GEN-LAST:event_jButton118MousePressed

    private void jButton119MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton119MousePressed
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(ChoosedDirectory);
        chooser.setDialogTitle("Выберите папку для сохранения файла");
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            ChoosedDirectory = chooser.getCurrentDirectory();
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile.isDirectory()) {
                    try {
                        WritableWorkbook workbook;
                        WorkbookSettings ws = new WorkbookSettings();
                        ws.setLocale(new Locale("ru", "RU"));
                        workbook = Workbook.createWorkbook(new File(selectedFile.getPath() +"\\заказ_упаковки_для_склада.xls"), ws);
                        WritableSheet s = workbook.createSheet("заказ упаковки для склада", 0);
                        
                        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
                        WritableCellFormat cf = new WritableCellFormat(wf);
                        cf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); 
                        cf.setAlignment(Alignment.CENTRE);
                        
                        jxl.write.WritableCellFormat cf2 = new jxl.write.WritableCellFormat();  
                        cf2.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);  
                        cf.setAlignment(Alignment.CENTRE);
              
                        s.setColumnView(0, 10);
                        s.setColumnView(1, 30);
                        s.setColumnView(2, 20);
                        s.setColumnView(3, 20);
                        s.setColumnView(4, 20);
                        
                        for (int i=0;i<jTable22.getColumnCount()-1;i++) {
                            String value = jTable22.getColumnName(i);
                            value = value.replace("<html>&Sigma; ", "");
                            value = value.replace("</html>", "");
                            jxl.write.Label l = new jxl.write.Label(i, 0, value, cf);
                            s.addCell(l);
                        }
                        
                        jxl.write.Label l;
                        
                        for (int j=0;j<jTable22.getRowCount();j++) {
                            for (int i=0;i<jTable22.getColumnCount()-1;i++) {
                                if (i==0) { //image
                                    if (jTable22.getValueAt(j,i)==null) {
                                        s.setRowView(j, 1000);
                                        continue;
                                    }
                                    String fileName = Packings.getString((Integer)jTable22.getValueAt(j,5),"FILENAME");
                                    File imageFile = new File("c:\\temp\\" + fileName);
                                    BufferedImage input = ImageIO.read(imageFile);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ImageIO.write(input,"PNG",baos);
                                    jxl.write.WritableImage im = new jxl.write.WritableImage(i,j+1,1,1,baos.toByteArray());
                                    l  = new jxl.write.Label(i, j+1, "", cf2);                                    
                                    s.addCell(l);
                                    s.addImage(im);
                                    s.setRowView(j, 1000);
                                } else {
                                    String value = jTable22.getValueAt(j, i).toString();
                                    value = value.replace("<html><font color='#C80000'><b>", "");
                                    value = value.replace("</b></font></html>", "");
                                    l  = new jxl.write.Label(i, j+1, value, cf2);                                    
                                    s.addCell(l);
                                }
                            }
                        }
                        workbook.write();
                        workbook.close();
                        JOptionPane.showMessageDialog(null, "Документ сохранен в " + selectedFile.getPath() + "\\заказ_упаковки_для_склада.xls");
                    } catch (IOException | WriteException | HeadlessException ex) {
                        JOptionPane.showMessageDialog(null, "Не удалось сохранить документ");
                    }
                }
            }
    }//GEN-LAST:event_jButton119MousePressed

    private void jButton121MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton121MousePressed
        String s = JOptionPane.showInputDialog("Введите имя новой папки");
        if (s!=null) {
            if (db.UpdateSQL("INSERT INTO folder(name) VALUES(?)", new Object[]{s})) {
                JOptionPane.showMessageDialog(null, "Папка успешно создана");
                GetFolders();
                MakeTreeOfClients();
            }
        }
    }//GEN-LAST:event_jButton121MousePressed

    private void jTree3MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree3MouseDragged
        if (!clientDraggedInClientsTree) {
            DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) jTree3.getLastSelectedPathComponent();
            if (SelectedNode != null && SelectedNode.getLevel()==2) {
                jTree3.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                draggedClientID = (int)Clients.get((int)((Object[])SelectedNode.getUserObject())[1],"ID");
                draggedClientName = (String)((Object[])SelectedNode.getUserObject())[0];
                draggedFromFolderID = (int)((Object[])(((DefaultMutableTreeNode)SelectedNode.getParent()).getUserObject()))[0];
                draggedFromFolderName = (String)((Object[])(((DefaultMutableTreeNode)SelectedNode.getParent()).getUserObject()))[1];
                clientDraggedInClientsTree = true;
            }
        }
    }//GEN-LAST:event_jTree3MouseDragged

    private void jTree3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree3MouseReleased
        if (clientDraggedInClientsTree) {
            jTree3.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));            
            DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode)jTree3.getClosestPathForLocation(evt.getX(), evt.getY()).getLastPathComponent();
            if (SelectedNode!=null && SelectedNode.getLevel()!=0) {
                String draggedToFolderName = "";
                if (SelectedNode.getLevel()==1) { //папка
                    draggedToFolderID = (int)((Object[])SelectedNode.getUserObject())[0];
                    draggedToFolderName = (String)((Object[])SelectedNode.getUserObject())[1];
                } else if (SelectedNode.getLevel()==2) { //клиент в папке
                    draggedToFolderID = (int)((Object[])((DefaultMutableTreeNode)SelectedNode.getParent()).getUserObject())[0];
                    draggedToFolderName = (String)((Object[])((DefaultMutableTreeNode)SelectedNode.getParent()).getUserObject())[1];
                }
                if (draggedToFolderID!=draggedFromFolderID) {
                    clientDraggedInClientsTree = false;
                    createWindowAfterDragClient(draggedToFolderName);
                }
            }
        }
    }//GEN-LAST:event_jTree3MouseReleased

    private void jButton126MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton126MousePressed
        if (JOptionPane.showConfirmDialog(null, "Остатки и резерв по складу (УПАКОВКА) будут очищены.\nДанное действие нельзя будет отменить.\nПродолжить?", "Очистка склада", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            db.UpdateSQL("UPDATE packing SET storage=0, reserved=0", new Object[]{});
            jTabbedPane2StateChanged(null);
            JOptionPane.showMessageDialog(null, "Выполнена очистка склада");
        }
    }//GEN-LAST:event_jButton126MousePressed

    private void jButton127ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton127ActionPerformed
        jButton49.setVisible(false);
        jTextField16.setEnabled(false);
        jPasswordField1.setText("");
        jLabel72.setVisible(false);
        jLabel73.setVisible(false);
        sessionIsBlocked = true;
        CardLayout cl = (CardLayout) jPanel1.getParent().getLayout();
        cl.show(jPanel1.getParent(), "card2");
    }//GEN-LAST:event_jButton127ActionPerformed

    private void jLabel129MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel129MousePressed
        if (jLabel129.isEnabled()) {
            while (true) {
                String s = JOptionPane.showInputDialog(null, "Введите новый пароль");
                if (s==null) {
                    break;
                }
                try {
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    digest.update(s.getBytes());
                    if (db.UpdateSQL("UPDATE user SET finance_pass=? WHERE id=?", new Object[]{new String(digest.digest()),Users.getInt("ID")})) {
                        if (Users.getInt("ID")==CurrentUser.getInt("ID")) {
                            CurrentUser.set(db.SelectSQL("SELECT * FROM user WHERE login=? AND pass=?",new Object[]{CurrentUser.getString("LOGIN"),CurrentUser.getString("PASS")}));
                        }
                        JOptionPane.showMessageDialog(null, "Пароль изменен");
                        break;
                    }
                } catch (NoSuchAlgorithmException | HeadlessException ex) {
                }        
            }
        }
    }//GEN-LAST:event_jLabel129MousePressed

    private void jComboBox7ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox7ItemStateChanged
        GetDelivery();
        MakeTableOfDelivery();
    }//GEN-LAST:event_jComboBox7ItemStateChanged

    private void jButton128ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton128ActionPerformed
        SelectNodeOfTreeClients();
    }//GEN-LAST:event_jButton128ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        GetClients(jToggleButton5.isSelected());
        MakeTreeOfClients();
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        GetClients(jToggleButton5.isSelected());
        MakeTreeOfClients();
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jToggleButton7StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jToggleButton7StateChanged
        GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
        MakeTreeOfCandies();
    }//GEN-LAST:event_jToggleButton7StateChanged

    private void jToggleButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton8ActionPerformed
        jToggleButton7.setSelected(true);
        jButton69.doClick();
    }//GEN-LAST:event_jToggleButton8ActionPerformed

    private void jComboBox9ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox9ItemStateChanged
        GetClients(jToggleButton5.isSelected());
        MakeTreeOfClients();              
    }//GEN-LAST:event_jComboBox9ItemStateChanged

    private void jButton129ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton129ActionPerformed
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)jTree3.getLastSelectedPathComponent();
        if (selectedNode!=null && selectedNode.getLevel()==1) {
            Object[] obj = (Object[])selectedNode.getUserObject();
            int folderID = (int)obj[0];
            String currentName = (String)obj[1];
            
            while (true) {
                String s = JOptionPane.showInputDialog("Введите новое название папки",currentName);
                if (s==null) {
                    break;
                }
                if (!s.isEmpty()) {
                    db.UpdateSQL("UPDATE folder SET name=? WHERE id=?", new Object[]{s,folderID});
                    GetFolders();
                    MakeTreeOfClients();
                    break;
                }
            }         
        }
    }//GEN-LAST:event_jButton129ActionPerformed

    private void jComboBox10ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox10ItemStateChanged
        try {
            GetClients(jToggleButton5.isSelected());
            MakeTreeOfClients();
        } catch (Exception ex) {}        
    }//GEN-LAST:event_jComboBox10ItemStateChanged

    private void jTextField50KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField50KeyReleased
        GetFilteredPackings();
        MakeTreeOfPackingsInStorage();
    }//GEN-LAST:event_jTextField50KeyReleased

    private void jTextField43KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField43KeyReleased
        GetOrders();
        MakeTableOfOrders();
    }//GEN-LAST:event_jTextField43KeyReleased

    private void jTextField46KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField46KeyReleased
        GetOrders();
        MakeTableOfOrders();
    }//GEN-LAST:event_jTextField46KeyReleased

    private void jTextField48KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField48KeyReleased
        GetOrders();
        MakeTableOfOrders();
    }//GEN-LAST:event_jTextField48KeyReleased

    private void jTextField49KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField49KeyReleased
        GetDelivery();
        MakeTableOfDelivery();
    }//GEN-LAST:event_jTextField49KeyReleased

    private void jComboBox12ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox12ItemStateChanged
        if (jComboBox12.getSelectedIndex()==STORAGE_WORKER_PACKER) {
            int selectedIndex = jComboBox13.getSelectedIndex();
            fillListBrigadiers();
            jComboBox13.setSelectedIndex(selectedIndex);
            jComboBox13.setVisible(true);
            jLabel89.setVisible(true);
        } else {
            jComboBox13.setVisible(false);
            jLabel89.setVisible(false);
        }
    }//GEN-LAST:event_jComboBox12ItemStateChanged

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        GetUsers();
        MakeTreeOfUsers();
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        expeditorsDialog.initDialog(CanEditExpeditors);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jComboBox14ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox14ItemStateChanged
        GetDelivery();
        MakeTableOfDelivery();
    }//GEN-LAST:event_jComboBox14ItemStateChanged

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        labelPrintDialog.initDialog();
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        if (jComboBox6.getItemCount()>0) {
            try {
                GetOrders();
                MakeTableOfOrders();
            } catch (Exception ex) {}
        }
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void jComboBoxPaymentTypesOrderListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPaymentTypesOrderListActionPerformed
        if (jComboBoxPaymentTypesOrderList.getItemCount()>0) {
            try {
                GetOrders();
                MakeTableOfOrders();
            } catch (Exception ex) {}
        }
    }//GEN-LAST:event_jComboBoxPaymentTypesOrderListActionPerformed

    private void jTextField51KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField51KeyReleased
        GetDelivery();
        MakeTableOfDelivery();
    }//GEN-LAST:event_jTextField51KeyReleased

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jTextField37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField37ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField37ActionPerformed

    private void jTextField53KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField53KeyReleased
        filterName = jTextField53.getText().isEmpty() ? "" : " AND candy.name LIKE '"+jTextField53.getText()+"%'";
        GetCandies(jToggleButton1.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton2.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
        MakeTreeOfCandies();
    }//GEN-LAST:event_jTextField53KeyReleased

    private void jTextField54KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField54KeyReleased
        filterName = jTextField54.getText().isEmpty() ? "" : " AND candy.name LIKE '"+jTextField54.getText()+"%'";
        GetCandies(jToggleButton3.isSelected() ? CandiesOrder.ALPHABET : (jToggleButton4.isSelected() ? CandiesOrder.COST : CandiesOrder.RELATIVE_COST));
        MakeTreeOfCandiesForGift();
    }//GEN-LAST:event_jTextField54KeyReleased

    private void jTextField55KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField55KeyReleased
        filterName = jTextField55.getText().isEmpty() ? "" : " AND candy.name LIKE '"+jTextField55.getText()+"%'";
        GetCandies(jToggleButton9.isSelected() ? CandiesOrder.ALPHABET : CandiesOrder.COST);
        MakeTreeOfCandiesInStorage();
    }//GEN-LAST:event_jTextField55KeyReleased

    private void jToggleButton9StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jToggleButton9StateChanged
        filterName = jTextField55.getText().isEmpty() ? "" : " AND candy.name LIKE '"+jTextField55.getText()+"%'";
        GetCandies(jToggleButton9.isSelected() ? CandiesOrder.ALPHABET : CandiesOrder.COST);
        MakeTreeOfCandies();
    }//GEN-LAST:event_jToggleButton9StateChanged

    private void jToggleButton10StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jToggleButton10StateChanged
        filterName = jTextField55.getText().isEmpty() ? "" : " AND candy.name LIKE '"+jTextField55.getText()+"%'";
        GetCandies(jToggleButton9.isSelected() ? CandiesOrder.ALPHABET : CandiesOrder.COST);
        MakeTreeOfCandies();
    }//GEN-LAST:event_jToggleButton10StateChanged

    private void jTextField56KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField56KeyReleased
        GetPackings();
        MakeTreeOfPackings();
    }//GEN-LAST:event_jTextField56KeyReleased

    private void jTextField57KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField57KeyReleased
        MakeTableOfOrders();
    }//GEN-LAST:event_jTextField57KeyReleased

    private void jTextField58KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField58KeyReleased
        MakeTableOfOrders();
    }//GEN-LAST:event_jTextField58KeyReleased

    private void jComboBox16ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox16ItemStateChanged
        GetDelivery();
        MakeTableOfDelivery();
    }//GEN-LAST:event_jComboBox16ItemStateChanged

    private void jComboBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox17ActionPerformed
        if (jComboBox17.getItemCount()>0) {
            try {
                GetDelivery();
                MakeTableOfDelivery();
            } catch (Exception ex) {}
        }
    }//GEN-LAST:event_jComboBox17ActionPerformed

    private void jTextField59KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField59KeyReleased
        GetDelivery();
        MakeTableOfDelivery();
    }//GEN-LAST:event_jTextField59KeyReleased

    private void jTable22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable22MouseClicked
        MakeTableOfStorageOrderPref();
    }//GEN-LAST:event_jTable22MouseClicked

    private void jButton20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseClicked
        ShowGiftsAndSumm();
    }//GEN-LAST:event_jButton20MouseClicked

    private void jTextField60KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField60KeyReleased
        GetDelivery();
        MakeTableOfDelivery();
    }//GEN-LAST:event_jTextField60KeyReleased

    private void jButton120MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton120MousePressed
        if (jTabbedPane2.getSelectedIndex()==0) {
            if (JOptionPane.showConfirmDialog(null, "Остатки и резерв по выбранной позиции (КОНФЕТЫ) будут очищены.\nДанное действие нельзя будет отменить.\nПродолжить?", "Очистка склада", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                Object[] obj = (Object[]) ((DefaultMutableTreeNode) jTree10.getLastSelectedPathComponent()).getUserObject();
                int id = Candies.getInt((Integer) obj[1], "ID");
                db.UpdateSQL("UPDATE candy SET storage=0, reserved=0 WHERE id=?", new Object[]{id});
                JOptionPane.showMessageDialog(null, "Выполнена очистка одной позиции (КОНФЕТЫ)");
                jTabbedPane2StateChanged(null);
            }
        } else {
            if (JOptionPane.showConfirmDialog(null, "Остатки и резерв по выбранной позиции (УПАКОВКА) будут очищены.\nДанное действие нельзя будет отменить.\nПродолжить?", "Очистка склада", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
                Object[] obj2 = (Object[]) ((DefaultMutableTreeNode) jTree11.getLastSelectedPathComponent()).getUserObject();
                int id2 = FilteredPackings.getInt((Integer) obj2[1], "ID");
                db.UpdateSQL("UPDATE packing SET storage=0, reserved=0 WHERE id=?", new Object[]{id2});
                JOptionPane.showMessageDialog(null, "Выполнена очистка одной позиции (УПАКОВКА)");
                jTabbedPane2StateChanged(null);
            }             
        }        
        
    }//GEN-LAST:event_jButton120MousePressed

    private void jButtonSaveOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSaveOrderActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler11;
    private javax.swing.Box.Filler filler12;
    private javax.swing.Box.Filler filler13;
    private javax.swing.Box.Filler filler14;
    private javax.swing.Box.Filler filler18;
    private javax.swing.Box.Filler filler19;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler20;
    private javax.swing.Box.Filler filler21;
    private javax.swing.Box.Filler filler22;
    private javax.swing.Box.Filler filler23;
    private javax.swing.Box.Filler filler24;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton100;
    private javax.swing.JButton jButton105;
    private javax.swing.JButton jButton108;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton111;
    private javax.swing.JButton jButton114;
    private javax.swing.JButton jButton115;
    private javax.swing.JButton jButton116;
    private javax.swing.JButton jButton117;
    private javax.swing.JButton jButton118;
    private javax.swing.JButton jButton119;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton120;
    private javax.swing.JButton jButton121;
    private javax.swing.JButton jButton126;
    private javax.swing.JButton jButton127;
    private javax.swing.JButton jButton128;
    private javax.swing.JButton jButton129;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton52;
    private javax.swing.JButton jButton55;
    private javax.swing.JButton jButton56;
    private javax.swing.JButton jButton57;
    private javax.swing.JButton jButton58;
    private javax.swing.JButton jButton59;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton60;
    private javax.swing.JButton jButton61;
    private javax.swing.JButton jButton62;
    private javax.swing.JButton jButton63;
    private javax.swing.JButton jButton64;
    private javax.swing.JButton jButton65;
    private javax.swing.JButton jButton66;
    private javax.swing.JButton jButton67;
    private javax.swing.JButton jButton68;
    private javax.swing.JButton jButton69;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton70;
    private javax.swing.JButton jButton71;
    private javax.swing.JButton jButton72;
    private javax.swing.JButton jButton73;
    private javax.swing.JButton jButton74;
    private javax.swing.JButton jButton75;
    private javax.swing.JButton jButton78;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton80;
    private javax.swing.JButton jButton81;
    private javax.swing.JButton jButton82;
    private javax.swing.JButton jButton84;
    private javax.swing.JButton jButton85;
    private javax.swing.JButton jButton89;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButton90;
    private javax.swing.JButton jButton91;
    private javax.swing.JButton jButton97;
    private javax.swing.JButton jButtonSaveOrder;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox jComboBox1;
    /*
    private javax.swing.JComboBox jComboBox10;
    */
    private ComboboxUsers jComboBox10;
    /*
    private javax.swing.JComboBox jComboBox11;
    */
    private ComboboxUsers jComboBox11;
    private javax.swing.JComboBox jComboBox12;
    /*
    private javax.swing.JComboBox jComboBox13;
    */
    private ComboboxUsers jComboBox13;
    /*
    private javax.swing.JComboBox jComboBox14;
    */
    private ComboboxUsers jComboBox14;
    private javax.swing.JComboBox jComboBox16;
    private javax.swing.JComboBox jComboBox17;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    /*
    private javax.swing.JComboBox jComboBox6;
    */
    private ComboboxUsers jComboBox6;
    /*
    private javax.swing.JComboBox jComboBox7;
    */
    private ComboboxUsers jComboBox7;
    /*
    private javax.swing.JComboBox jComboBox8;
    */
    private ComboboxClientState jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JComboBox jComboBoxPaymentTypesOrderDetail;
    private javax.swing.JComboBox jComboBoxPaymentTypesOrderList;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser10;
    private com.toedter.calendar.JDateChooser jDateChooser11;
    private com.toedter.calendar.JDateChooser jDateChooser13;
    private com.toedter.calendar.JDateChooser jDateChooser14;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private com.toedter.calendar.JDateChooser jDateChooser7;
    private com.toedter.calendar.JDateChooser jDateChooser8;
    private com.toedter.calendar.JDateChooser jDateChooser9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    /*
    private javax.swing.JPanel jPanel1;
    */
    private GradientPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel101;
    private javax.swing.JPanel jPanel102;
    private javax.swing.JPanel jPanel103;
    private javax.swing.JPanel jPanel104;
    private javax.swing.JPanel jPanel105;
    private javax.swing.JPanel jPanel107;
    private javax.swing.JPanel jPanel108;
    private javax.swing.JPanel jPanel109;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel110;
    private javax.swing.JPanel jPanel111;
    private javax.swing.JPanel jPanel112;
    private javax.swing.JPanel jPanel113;
    private javax.swing.JPanel jPanel114;
    private javax.swing.JPanel jPanel115;
    private javax.swing.JPanel jPanel116;
    private javax.swing.JPanel jPanel117;
    /*
    private javax.swing.JPanel jPanel118;
    */
    private GradientPanel jPanel118;
    private javax.swing.JPanel jPanel119;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel120;
    private javax.swing.JPanel jPanel121;
    private javax.swing.JPanel jPanel122;
    private javax.swing.JPanel jPanel123;
    private javax.swing.JPanel jPanel124;
    private javax.swing.JPanel jPanel125;
    private javax.swing.JPanel jPanel126;
    private javax.swing.JPanel jPanel128;
    private javax.swing.JPanel jPanel129;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel130;
    private javax.swing.JPanel jPanel131;
    private javax.swing.JPanel jPanel132;
    private javax.swing.JPanel jPanel133;
    private javax.swing.JPanel jPanel134;
    private javax.swing.JPanel jPanel138;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel147;
    private javax.swing.JPanel jPanel148;
    private javax.swing.JPanel jPanel149;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel150;
    private javax.swing.JPanel jPanel151;
    private javax.swing.JPanel jPanel153;
    private javax.swing.JPanel jPanel154;
    private javax.swing.JPanel jPanel155;
    private javax.swing.JPanel jPanel156;
    private javax.swing.JPanel jPanel157;
    private javax.swing.JPanel jPanel158;
    private javax.swing.JPanel jPanel159;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel160;
    private javax.swing.JPanel jPanel161;
    private javax.swing.JPanel jPanel162;
    private javax.swing.JPanel jPanel163;
    private javax.swing.JPanel jPanel164;
    private javax.swing.JPanel jPanel165;
    private javax.swing.JPanel jPanel166;
    private javax.swing.JPanel jPanel167;
    private javax.swing.JPanel jPanel168;
    private javax.swing.JPanel jPanel169;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel170;
    private javax.swing.JPanel jPanel171;
    private javax.swing.JPanel jPanel172;
    private javax.swing.JPanel jPanel173;
    private javax.swing.JPanel jPanel174;
    private javax.swing.JPanel jPanel175;
    private javax.swing.JPanel jPanel176;
    private javax.swing.JPanel jPanel177;
    private javax.swing.JPanel jPanel178;
    private javax.swing.JPanel jPanel179;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel180;
    private javax.swing.JPanel jPanel182;
    private javax.swing.JPanel jPanel183;
    private javax.swing.JPanel jPanel185;
    private javax.swing.JPanel jPanel188;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel192;
    private javax.swing.JPanel jPanel193;
    private javax.swing.JPanel jPanel194;
    private javax.swing.JPanel jPanel195;
    private javax.swing.JPanel jPanel196;
    private javax.swing.JPanel jPanel197;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    /*
    private javax.swing.JPanel jPanel3;
    */
    private GradientPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    /*
    private javax.swing.JPanel jPanel38;
    */
    private GradientPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    /*
    private javax.swing.JPanel jPanel6;
    */
    private GradientPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    /*
    private javax.swing.JPanel jPanel71;
    */
    private GradientPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    /*
    private javax.swing.JPanel jPanel75;
    */
    private GradientPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel78;
    /*
    private javax.swing.JPanel jPanel79;
    */
    private GradientPanel jPanel79;
    private javax.swing.JPanel jPanel8;
    /*
    private javax.swing.JPanel jPanel80;
    */
    private GradientPanel jPanel80;
    private javax.swing.JPanel jPanel81;
    private javax.swing.JPanel jPanel82;
    private javax.swing.JPanel jPanel83;
    private javax.swing.JPanel jPanel84;
    private javax.swing.JPanel jPanel85;
    private javax.swing.JPanel jPanel86;
    /*
    private javax.swing.JPanel jPanel87;
    */
    private GradientPanel jPanel87;
    private javax.swing.JPanel jPanel88;
    private javax.swing.JPanel jPanel89;
    private javax.swing.JPanel jPanel9;
    /*
    private javax.swing.JPanel jPanel90;
    */
    private GradientPanel jPanel90;
    private javax.swing.JPanel jPanel91;
    private javax.swing.JPanel jPanel94;
    private javax.swing.JPanel jPanel96;
    private javax.swing.JPanel jPanel98;
    private javax.swing.JPanel jPanel99;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane31;
    private javax.swing.JScrollPane jScrollPane32;
    private javax.swing.JScrollPane jScrollPane38;
    private javax.swing.JScrollPane jScrollPane39;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane40;
    private javax.swing.JScrollPane jScrollPane41;
    private javax.swing.JScrollPane jScrollPane43;
    private javax.swing.JScrollPane jScrollPane45;
    private javax.swing.JScrollPane jScrollPane46;
    private javax.swing.JScrollPane jScrollPane49;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    /*
    private javax.swing.JTable jTable1;
    */
    private TableGiftCandy jTable1;
    /*
    private javax.swing.JTable jTable10;
    */
    private TableClients jTable10;
    /*
    private javax.swing.JTable jTable11;
    */ private MainPackage.TableStorageOrderPref jTable11;
    /*
    private javax.swing.JTable jTable12;
    */
    private MainPackage.TableDelivery jTable12;
    /*
    private javax.swing.JTable jTable13;
    */
    private TableUsersStorage jTable13;
    /*
    private javax.swing.JTable jTable14;
    */
    private TableDeliveryOrder jTable14;
    /*
    private javax.swing.JTable jTable16;
    */
    private TableStorageReserv jTable16;
    /*
    private javax.swing.JTable jTable17;
    */
    private TableStorageReserv jTable17;
    /*
    private javax.swing.JTable jTable18;
    */
    private TableCandiesForGift jTable18;
    /*
    private javax.swing.JTable jTable19;
    */
    private TableFinance jTable19;
    /*
    private javax.swing.JTable jTable2;
    */
    private TableCandiesOfFactory jTable2;
    /*
    private javax.swing.JTable jTable20;
    */
    private TableExpense jTable20;
    /*
    private javax.swing.JTable jTable22;
    */
    private MainPackage.TableOrderPackingForStorage jTable22;
    /*
    private javax.swing.JTable jTable3;
    */
    private TableOrders jTable3;
    /*
    private javax.swing.JTable jTable4;
    */
    private TableOrdersOfClient jTable4;
    /*
    private javax.swing.JTable jTable5;
    */
    private TableSubOrders jTable5;
    /*
    private javax.swing.JTable jTable6;
    */
    private TableStorage jTable6;
    /*
    private javax.swing.JTable jTable7;
    */
    private TableUsersStorage jTable7;
    /*
    private javax.swing.JTable jTable8;
    */
    private TableStorage jTable8;
    /*
    private javax.swing.JTable jTable9;
    */
    private MainPackage.TableOrderCandyForStorage jTable9;
    private javax.swing.JTextArea jTextArea10;
    private javax.swing.JTextArea jTextArea12;
    private javax.swing.JTextArea jTextArea5;
    private javax.swing.JTextArea jTextArea6;
    private javax.swing.JTextArea jTextArea7;
    private javax.swing.JTextArea jTextArea8;
    private javax.swing.JTextArea jTextArea9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField44;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField47;
    private javax.swing.JTextField jTextField48;
    private javax.swing.JTextField jTextField49;
    private javax.swing.JTextField jTextField50;
    private javax.swing.JTextField jTextField51;
    private javax.swing.JTextField jTextField52;
    private javax.swing.JTextField jTextField53;
    private javax.swing.JTextField jTextField54;
    private javax.swing.JTextField jTextField55;
    private javax.swing.JTextField jTextField56;
    private javax.swing.JTextField jTextField57;
    private javax.swing.JTextField jTextField58;
    private javax.swing.JTextField jTextField59;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField60;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton10;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JToggleButton jToggleButton9;
    /*
    private javax.swing.JTree jTree1;
    */
    private CandiesTree jTree1;
    /*
    private javax.swing.JTree jTree10;
    */
    private CandiesTreeForStorage jTree10;
    /*
    private javax.swing.JTree jTree11;
    */
    private PackingsTreeForStorage jTree11;
    /*
    private javax.swing.JTree jTree2;
    */
    private UsersTree jTree2;
    /*
    private javax.swing.JTree jTree3;
    */
    private ClientsTree jTree3;
    /*
    private javax.swing.JTree jTree4;
    */
    private GiftsTree jTree4;
    /*
    private javax.swing.JTree jTree5;
    */
    private CandiesTreeForPackage jTree5;
    /*
    private javax.swing.JTree jTree6;
    */
    private PackingsTree jTree6;
    // End of variables declaration//GEN-END:variables


}
