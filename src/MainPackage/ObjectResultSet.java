package MainPackage;

public class ObjectResultSet {

    private Object[][] obj = null;
    private String[] columnNames = null;
    private int position = 0;

    public void set(Object[][] obj) {
        this.obj = obj;
    }

    public boolean IsNull() {
        return obj==null;
    }

    private int getColumnByName(String ColumnName) {
        int ColumnNo = -1;
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].equals(ColumnName)) {
                ColumnNo = i;
                break;
            }
        }
        return ColumnNo;
    }

    public int existsObjectInColumn(String ColumnName, Object ObjectName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1;
        } else {
            int RowNo = -1;
            for (int i = 0; i < obj.length; i++) {
                if ((obj[i][ColumnNo] != null) && (obj[i][ColumnNo].equals(ObjectName))) {
                    RowNo = i;
                    break;
                }
            }
            return RowNo;
        }
    }

    // поиск в строке с индексом RowIndex, и названием столбца ColumnName
    public Object get(int RowIndex, String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return null;
        } else {
            return obj[RowIndex][ColumnNo];
        }
    }

    // поиск в строке с индексом RowIndex, и названием столбца ColumnName. Возвращает int. Небезопасно в случае, если число не гарантированно int
    public int getInt(int RowIndex, String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1;
        } else {
            return (Integer)obj[RowIndex][ColumnNo];
        }
    }
    
    public boolean getBoolean(int RowIndex, String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return false;
        } else {
            return (boolean)obj[RowIndex][ColumnNo];
        }
    }    

    public String getString(int RowIndex, String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return "";
        } else {
            return (String)obj[RowIndex][ColumnNo];
        }
    }    

    public double getDouble(int RowIndex, String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1;
        } else {
            return (Double)obj[RowIndex][ColumnNo];
        }
    }

    // поиск в текущей строке с названием столбца ColumnName. Возвращает int. Небезопасно в случае, если число не гарантированно int
    public int getInt(String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1;
        } else {
            return (Integer)obj[this.position][ColumnNo];
        }
    }
    
    public boolean getBoolean(String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return false;
        } else {
            return (boolean)obj[this.position][ColumnNo];
        }
    }    
    
    public Long getLong(String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1L;
        } else {
            return (Long)obj[this.position][ColumnNo];
        }
    }    
    
    public Long getLong(int RowIndex, String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1L;
        } else {
            return (Long)obj[RowIndex][ColumnNo];
        }
    }    
    
    public String getString(String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return "";
        } else {
            return (String)obj[this.position][ColumnNo];
        }
    }   
    
    public double getDouble(String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1;
        } else {
            return (Double)obj[this.position][ColumnNo];
        }
    }    

    public long getBigDecimalAsLong(int RowIndex, String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1;
        } else {
            return ((java.math.BigDecimal)obj[RowIndex][ColumnNo]).longValue();
        }
    }
    
    public int getBigDecimalAsInt(int RowIndex, String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1;
        } else {
            return obj[RowIndex][ColumnNo]==null ? 0 : ((java.math.BigDecimal)obj[RowIndex][ColumnNo]).intValue();
        }
    }
    
    
    public long getBigIntegerAsLong(int RowIndex, String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1;
        } else {
            return ((java.math.BigInteger)obj[RowIndex][ColumnNo]).longValue();
        }
    }

    public long getBigIntegerAsLong(String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1;
        } else {
            return ((java.math.BigInteger)obj[this.position][ColumnNo]).longValue();
        }
    }    

    // поиск в текущей строке с названием столбца ColumnName
    public Object get(String ColumnName) {
        int ColumnNo = getColumnByName(ColumnName);
        if ((ColumnNo == -1) || (this.position==-1)) {
            return null;
        } else {
            return obj[this.position][ColumnNo];
        }
    }

    public void refreshValue(int position, Object[] value) {
        obj[position] = value;
    }

    public int setValue(int RowIndex, String ColumnName, Object value) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1;
        } else {
            obj[RowIndex][ColumnNo] = value;
            return 0;
        }
    }

    public int setValue(String ColumnName, Object value) {
        int ColumnNo = getColumnByName(ColumnName);
        if (ColumnNo == -1) {
            return -1;
        } else {
            obj[this.position][ColumnNo] = value;
            return 0;
        }
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public int getPosition() {
        return this.position;
    }

    public int getLength() {
        return obj.length;
    }

    public void setColumnNames(String[] names) {
        this.columnNames = names;
    }
}
