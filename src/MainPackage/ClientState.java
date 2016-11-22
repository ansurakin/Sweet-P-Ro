package MainPackage;

import javax.swing.ImageIcon;

public class ClientState {

    public static enum STATE {CALL, QUESTION, MONEY, BAD, WAIT, OTHER};
    
    private static final ImageIcon iconCall = new javax.swing.ImageIcon(ClientState.class.getResource("/Images/telephone-handset-icon.png"));
    private static final ImageIcon iconQuestion = new javax.swing.ImageIcon(ClientState.class.getResource("/Images/question-white-icon.png"));
    private static final ImageIcon iconMoney = new javax.swing.ImageIcon(ClientState.class.getResource("/Images/coins-icon.png"));
    private static final ImageIcon iconBad = new javax.swing.ImageIcon(ClientState.class.getResource("/Images/Delete.png"));
    private static final ImageIcon iconWait = new javax.swing.ImageIcon(ClientState.class.getResource("/Images/hourglass-wait-cursor.png"));
    private static final ImageIcon iconOther = new javax.swing.ImageIcon(ClientState.class.getResource("/Images/Office-Client-Male-Light-icon.png"));
    
    public static ImageIcon getIcon(STATE state) {
        switch (state) {
            case CALL:
                return iconCall;
            case QUESTION:
                return iconQuestion;
            case MONEY:
                return iconMoney;
            case OTHER:
                return iconOther;
            case BAD:
                return iconBad;
            case WAIT:
                return iconWait;
            default:
                return null;
        }
    }
    
    public static int getValueForDB(STATE state) {
        switch (state) {
            case CALL:
                return 1;
            case QUESTION:
                return 2;
            case MONEY:
                return 3;
            case BAD:
                return 4;
            case WAIT:
                return 5;
            case OTHER:
                return 0;
            default:
                return 0;
        }
    }
    
    public static STATE getStateByValueDB(int value) {
        switch (value) {
            case 0:
                return STATE.OTHER;
            case 1:
                return STATE.CALL;
            case 2:
                return STATE.QUESTION;
            case 3:
                return STATE.MONEY;
            case 4:
                return STATE.BAD;
            case 5:
                return STATE.WAIT;
            default:
                return STATE.OTHER;
        }
    }
    
}
