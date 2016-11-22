package MainPackage;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

public class EZPLprint {
    
    public static enum STATUS {OK, NO_PRINTER, ERROR};
    
    public static STATUS print(String clientName, String giftName, int amountInBox, int copiesNumber) {
        try {
            //102,102 -> 95,95
            String commands = "^Q95,3\n\r^W95\n\r^H10\n\r^P1\n\r^S3\n\r^AD\n\r^C"+copiesNumber+"\n\r^R0\n\r~Q+0\n\r^O0\n\r^D0\n\r^E20\n\r~R200\n\r^XSET,ROTATION,0\n\r^L\n\rDy2-me-dd\n\rTh:m:s\n\r"+
                              "ATA,60,80,45,45,0,0E,A,0,Клиент:\n\rATB,60,140,45,45,0,0E,B,0,"+clientName+"\n\rATA,60,290,45,45,0,0E,A,0,Набор:\n\rATB,230,290,51,51,0,0E,B,0,"+giftName+"\n\rATA,60,370,45,45,0,0E,A,0,Кол-во в ящике:\n\r"+
                              "ATB,430,370,51,51,0,0E,B,0,"+amountInBox+"\n\rE\n\r";
            
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc  doc = new SimpleDoc(commands.getBytes("cp1251"), flavor, null);
            PrintService[] pservices = PrintServiceLookup.lookupPrintServices(flavor, null);
            PrintService godex = null;
            for (PrintService ps : pservices) {
                if (ps.getName().equals("Godex EZ-1100 Plus")) {
                    godex = ps;
                    break;
                }
            }
            if (godex==null) {
                return STATUS.NO_PRINTER;
            }
            DocPrintJob job = godex.createPrintJob();    
            job.print(doc,null);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return STATUS.ERROR;
        }
        return STATUS.OK;
    }
    
}


