package khang.android.wifi.data;

import android.net.wifi.ScanResult;

/**
 * Created by khang on 1/27/2015.
 */
public class AccessPoint {
    private ScanResult infor;
    public static final String TABLE = "AccessPoint";
    public static final String KEY_ID = "apid";
    public static final String KEY_MACADDRESS = "macaddress";
    public static final String KEY_RSSI = "rssi";
    public static final String KEY_LOCATION = "locationID";
    public int locationID;
    public int rssi;
    public String macaddress;
    public int apid;
    public boolean selected = false;

    public String getBSSID() {
        return infor.BSSID;
    }

    public void setBSSID(String macaddress) {
        this.macaddress = macaddress;
    }

    public String getSSID() {
        return infor.SSID;
    }

    public String getLevel() {
        return infor.level + " dBm";
    }

    public int getLeveldBm() {
        return infor.level;
    }

    public void setLeveldBm(int rssi) {
        this.rssi = rssi;
    }

    public String getFrequency() {
        return infor.frequency + " MHz";
    }

    public AccessPoint(ScanResult scanResult) {
        this.infor = scanResult;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public AccessPoint() {
    }
}
