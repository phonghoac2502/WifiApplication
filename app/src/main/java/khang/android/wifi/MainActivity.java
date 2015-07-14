package khang.android.wifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;

import khang.android.wifi.data.ACAdapter;
import khang.android.wifi.data.AccessPoint;
import khang.android.wifi.data.AccessPointRepo;

public class MainActivity extends Activity {
    private WifiManager mainWifi;
    private WifiReceiver receiverWifi;
    private ACAdapter acAdapter;
    private ArrayList<AccessPoint> wifiData = new ArrayList<AccessPoint>();
    private final android.os.Handler handler = new android.os.Handler();
    private static final int RESET_TIME = 1000;

    TextView txtTimeRemaining;
    Button btnCollect;
    Button btnShow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "9p3PQkED2cBdE7VGHJZaLqo3xDYmPcZanP5bJe0B", "UGaow83vXG8oyIUgufniBu3mHI2fOsam49l5EdRt");



        btnCollect = (Button) findViewById(R.id.btnCollect);
        txtTimeRemaining = (TextView) findViewById(R.id.txtTimeRemaining);
        btnShow = (Button) findViewById(R.id.btnShow);


        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if (!mainWifi.isWifiEnabled()) {
            mainWifi.setWifiEnabled(true);
        }
        doInback();
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Detail.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiverWifi);
        super.onStop();
    }

    @Override
    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mainWifi != null && mainWifi.isWifiEnabled()) {
            mainWifi.setWifiEnabled(false);
        }
        super.onDestroy();
    }

    public void doInback() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

                receiverWifi = new WifiReceiver();
                registerReceiver(receiverWifi,
                        new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                mainWifi.startScan();
                doInback();
            }
        }, RESET_TIME);
    }

    public void InsertDB(int userInput) {
        final AccessPointRepo repo = new AccessPointRepo(this);
        ArrayList<AccessPoint> accessPointArrayList = wifiData;
        for (int i = 0; i < accessPointArrayList.size(); i++) {
            AccessPoint accessPoint = new AccessPoint();
            if (wifiData.get(i).getBSSID().equals("c0:4a:00:71:0b:78")
                    || wifiData.get(i).getBSSID().equals("30:b5:c2:a7:7c:3c")
                    || wifiData.get(i).getBSSID().equals("30:b5:c2:f1:bb:ac")
                    || wifiData.get(i).getBSSID().equals("30:b5:c2:2d:01:6a")) {
                accessPoint.setBSSID(wifiData.get(i).getBSSID());
                accessPoint.setLeveldBm(wifiData.get(i).getLeveldBm());
                accessPoint.locationID = userInput;
                repo.insert(accessPoint);
            }
        }
    }

    public void InsertParse(int userInput) {

        ArrayList<AccessPoint> accessPointArrayList = wifiData;
        for (int i = 0; i < accessPointArrayList.size(); i++) {
            AccessPoint accessPoint = new AccessPoint();
            if (wifiData.get(i).getBSSID().equals("c0:4a:00:71:0b:78")
                    || wifiData.get(i).getBSSID().equals("30:b5:c2:a7:7c:3c")
                    || wifiData.get(i).getBSSID().equals("30:b5:c2:f1:bb:ac")
                    || wifiData.get(i).getBSSID().equals("30:b5:c2:2d:01:6a")) {
                accessPoint.setBSSID(wifiData.get(i).getBSSID());
                accessPoint.setLeveldBm(wifiData.get(i).getLeveldBm());
                accessPoint.locationID = userInput;

                ParseObject gameScore = new ParseObject("Detail");
                gameScore.put("MacAddress", accessPoint.macaddress);
                gameScore.put("RSSI", accessPoint.rssi);
                gameScore.put("LocationID", accessPoint.locationID);
                gameScore.saveInBackground();
            }
        }
    }

    public void onCollectBtn_Click(View view) {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogInput = li.inflate(R.layout.dialog_input, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogInput);
        final EditText userInput = (EditText) dialogInput
                .findViewById(R.id.editTextDialogUserInput);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //ArrayList<AccessPoint> accessPointArrayList = wifiData;
                                final int uI = Integer.parseInt(userInput.getText().toString());
                                CountDownTimer waitTimer = new CountDownTimer(600000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        btnCollect.setVisibility(View.INVISIBLE);
                                        txtTimeRemaining.setText("Left: " + millisUntilFinished / 1000 + "s");
                                        //InsertDB(uI);
                                        InsertParse(uI);
                                    }

                                    @Override
                                    public void onFinish() {
                                        btnCollect.setVisibility(View.VISIBLE);
                                        txtTimeRemaining.setText("Done");
                                    }
                                }.start();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            ArrayList<ScanResult> wifiList = new ArrayList<ScanResult>(mainWifi.getScanResults());
            wifiData.clear();
            for (int i = 0; i < wifiList.size(); i++) {
                wifiData.add(new AccessPoint(wifiList.get(i)));
            }
            if (acAdapter == null) {
                acAdapter = new ACAdapter(context, R.layout.ac_list_item, wifiData);
                ListView listView = (ListView) findViewById(R.id.lstview_AC);
                listView.setAdapter(acAdapter);
            } else {
                acAdapter.notifyDataSetChanged();
            }
        }
    }
}
