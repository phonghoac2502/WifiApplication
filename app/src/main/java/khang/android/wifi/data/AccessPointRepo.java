package khang.android.wifi.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
/**
 * Created by Phong on 04/06/2015.
 */
public class AccessPointRepo {
    private DBHelper dbHelper;
    public AccessPointRepo(Context context){
        dbHelper = new DBHelper(context);
    }
    public int insert(AccessPoint accessPoint)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccessPoint.KEY_MACADDRESS,accessPoint.macaddress);
        values.put(AccessPoint.KEY_RSSI,accessPoint.rssi);
        values.put(AccessPoint.KEY_LOCATION,accessPoint.locationID);

        long apId = db.insert(AccessPoint.TABLE,null,values);
        db.close();
        return (int) apId;
    }
}
