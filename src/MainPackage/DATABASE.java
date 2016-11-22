package MainPackage;

import java.awt.Image;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.Properties;
import javax.swing.ImageIcon;

public class DATABASE {
    
    private Connection con;
    private String host = "localhost";   
    private final MainForm mf;   
   
    public DATABASE(MainForm mf, String host) {
        this.mf = mf;
        this.host = host;
    }   
   
    public boolean setConnectionToServer() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Properties properties = new Properties();
//            properties.setProperty("user", "rilrai");
//            properties.setProperty("password", "injuhby");
//            properties.setProperty("useUnicode", "true");
//            properties.setProperty("characterEncoding", "utf-8");
//            con = DriverManager.getConnection("jdbc:mysql://"+host+":3306/sweet_1", properties);
            properties.setProperty("user", "sweetr_ronis");
            properties.setProperty("password", "xPLsaUlUo1");
            properties.setProperty("useUnicode", "true");
            properties.setProperty("characterEncoding", "utf-8");
            con = DriverManager.getConnection("jdbc:mysql://"+host+":3306/sweetr_sweet", properties);
            con.setAutoCommit(true);
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }        
   
    private Object[] selectSQLLow(String sql, Object[] parameters) {
        try {
            Object[][] obj;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                if (parameters != null) {
                    for (int i = 0; i < parameters.length; i++) {
                        if (parameters[i] instanceof Integer) {
                            ps.setInt(i + 1, (Integer) parameters[i]);
                        } else if (parameters[i] instanceof Double) {
                            ps.setDouble(i + 1, (Double) parameters[i]);
                        } else if (parameters[i] instanceof String) {
                            ps.setString(i + 1, (String) parameters[i]);
                        } else if (parameters[i] instanceof Long) {
                            ps.setLong(i + 1, (Long) parameters[i]);
                        }
                    }
                }
                ResultSet res = ps.executeQuery();
                ResultSetMetaData metadata = res.getMetaData();
                int RowCount = 0;
                res.beforeFirst();
                while (res.next()) {
                    RowCount++;
                }
                obj = new Object[RowCount][metadata.getColumnCount()];
                res.beforeFirst();
                for (int CurrentRow=0;CurrentRow<RowCount;CurrentRow++) {
                    res.next();
                    for (int i = 1; i <= metadata.getColumnCount(); i++) {
                        int type = metadata.getColumnType(i);
                        switch (type) {
                            case Types.INTEGER:
                                obj[CurrentRow][i - 1] = res.getInt(i);
                                break;
                            case Types.FLOAT:
                                obj[CurrentRow][i - 1] = res.getFloat(i);
                                break;
                            case Types.BLOB:
                                obj[CurrentRow][i - 1] = res.getBlob(i);
                                break;
                            case Types.TINYINT:
                                obj[CurrentRow][i - 1] = res.getInt(i);
                                break;
                            case Types.VARCHAR:
                                obj[CurrentRow][i - 1] = res.getString(i);
                                break;
                            case Types.BOOLEAN:
                                obj[CurrentRow][i - 1] = res.getBoolean(i);
                                break;
                            case Types.DOUBLE:
                                obj[CurrentRow][i - 1] = res.getDouble(i);
                                break;
                            case -4:
                                obj[CurrentRow][i - 1] = res.getBlob(i);
                                break;
                            default:
                                obj[CurrentRow][i - 1] = res.getObject(i);
                                break;
                        }
                    }
                }
            }
            return new Object[]{obj,1};
        } catch (SQLException ex) {
            return new Object[]{null,0};
        } catch (Exception ex) {
            return new Object[]{null,-1};
        }
    }    
    
    private Object[] updateSQLLow(String sql, Object[] obj) {
        try {
            int answer;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                if (obj != null) {
                    for (int i = 0; i < obj.length; i++) {
                        if (obj[i] instanceof Integer) {
                            ps.setInt(i + 1, (Integer) obj[i]);
                        } else if (obj[i] instanceof Double) {
                            ps.setDouble(i + 1, (Double) obj[i]);
                        } else if (obj[i] instanceof String) {
                            ps.setString(i + 1, (String) obj[i]);
                        } else if (obj[i] instanceof Long) {
                            ps.setLong(i + 1, (Long) obj[i]);
                        } else if (obj[i] instanceof Image) {
                            Blob blob = con.createBlob();
                            try (ObjectOutputStream oos = new ObjectOutputStream(blob.setBinaryStream(1))) {
                                oos.writeObject(new ImageIcon((Image) obj[i]));
                            }                        
                            ps.setBlob(i + 1, blob);                        
                        }
                    }
                }
                answer = ps.executeUpdate();
            }
            return new Object[]{answer>0,1};
        } catch (SQLException ex) {
            return new Object[]{null,0};
        } catch (Exception ex) {
            return new Object[]{null,-1};
        }
    }
    
     private Object[] doSQLLow(String sql) {
        try {
            try (Statement stmt = con.createStatement()) {
                stmt.execute(sql);
            }
            return new Object[]{true,1};
        } catch (SQLException ex) {
            return new Object[]{null,0};
        } catch (Exception ex) {
            return new Object[]{null,-1};
        }
    }    
     
    public Object[][] SelectSQL(String sql,Object[] parameters) {
        Object[] obj = selectSQLLow(sql, parameters);
        switch ((Integer)obj[1]) {
            case -1:
                mf.ShowStartScreen();                
                return null;
            case 0:
                try {
                    Thread.sleep(2000);
                    setConnectionToServer();
                    Thread.sleep(500);
                } catch (Exception ex) { }
                obj = selectSQLLow(sql,parameters);
                switch ((Integer)obj[1]) {
                    case -1:
                    case 0:
                        mf.ShowStartScreen();
                        return null;
                    case 1:
                        return (Object[][])obj[0];
                }
                break;
            case 1:
                return (Object[][])obj[0];
        }
        return null; 
    }
    
    public boolean UpdateSQL(String sql,Object[] parameters) {
        Object[] obj = updateSQLLow(sql, parameters);
        switch ((Integer)obj[1]) {
            case -1:
                mf.ShowStartScreen();
                return false;                
            case 0:
                try {
                    Thread.sleep(2000);
                    setConnectionToServer();
                    Thread.sleep(500);
                } catch (Exception ex) { }
                obj = updateSQLLow(sql, parameters);
                switch ((Integer)obj[1]) {
                    case -1:
                    case 0:
                        mf.ShowStartScreen();
                        return false;
                    case 1:
                        return (Boolean)obj[0];
                }
                break;
            case 1:
                return (Boolean)obj[0];
        }
        return false;         
    }
    
    public boolean UpdateSQL_justTry(String sql,Object[] parameters) {
        Object[] obj = updateSQLLow(sql, parameters);
        switch ((Integer)obj[1]) {
            case -1:
                return false;                
            case 0:
                return false;
            case 1:
                return (Boolean)obj[0];
        }
        return false;         
    }    
    
    public boolean DoSQL(String sql) {
        Object[] obj = doSQLLow(sql);
        switch ((Integer)obj[1]) {
            case -1:
                mf.ShowStartScreen();
                return false;                
            case 0:
                try {
                    Thread.sleep(2000);
                    setConnectionToServer();
                    Thread.sleep(500);
                } catch (Exception ex) { }
                obj = doSQLLow(sql);
                switch ((Integer)obj[1]) {
                    case -1:
                    case 0:
                        mf.ShowStartScreen();
                        return false;                
                    case 1:
                        return (Boolean)obj[0];
                }
                break;
            case 1:
                return (Boolean)obj[0];    
        }
        return false;         
    }             
    
}