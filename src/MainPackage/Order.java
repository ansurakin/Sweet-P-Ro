package MainPackage;

import javax.swing.ImageIcon;

public class Order {
    
    private static final ImageIcon iconAlarm = new javax.swing.ImageIcon(Order.class.getResource("/Images/exclamation-red-icon.png"));
    private static final ImageIcon iconCoins = new javax.swing.ImageIcon(Order.class.getResource("/Images/coins-icon.png"));
    private static final ImageIcon packingIcon = new javax.swing.ImageIcon(Order.class.getResource("/Images/upakovka_146_2.png"));
    private static final ImageIcon lockIcon =  new javax.swing.ImageIcon(Order.class.getResource("/Images/lock-icon2.png"));
    private static final ImageIcon lorryIcon = new javax.swing.ImageIcon(Order.class.getResource("/Images/Lorry-icon.png"));
    private static final ImageIcon okIcon = new javax.swing.ImageIcon(Order.class.getResource("/Images/Ok-icon.png"));
    private static final ImageIcon deleteIcon = new javax.swing.ImageIcon(Order.class.getResource("/Images/Delete.png"));
//    private static final ImageIcon deliveredIcon = new javax.swing.ImageIcon(Order.class.getResource("/Images/gift-icon2.png"));
    
    
    public static final int ORDER_CANCEL = -1;
    public static final int ORDER_PREPARE = 0;
    public static final int ORDER_PAY = 1;
    public static final int ORDER_PACK = 2;
    public static final int ORDER_STORAGE = 3;
    public static final int ORDER_SEND = 4;
//    public static final int ORDER_DELIVERED = 5;
    public static final int ORDER_DONE = 5;
    
    
    public static ImageIcon getIconForState(int state) {
        switch (state) {
            case ORDER_PREPARE:
                return iconAlarm;
            case ORDER_PAY:
                return iconCoins;
            case ORDER_PACK:
                return packingIcon;
            case ORDER_STORAGE:
                return lockIcon;
            case ORDER_SEND:
                return lorryIcon;
//            case ORDER_DELIVERED:
//                return deliveredIcon;
            case ORDER_DONE:
                return okIcon;
            case ORDER_CANCEL:
                return deleteIcon;
            default: return null;
        }
    }
    
}
