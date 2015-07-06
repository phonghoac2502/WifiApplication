package khang.android.wifi.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import khang.android.wifi.R;

/**
 * Created by khang on 1/27/2015.
 */
public class ACAdapter extends ArrayAdapter<AccessPoint> {
    Context context = null;
    int layoutId;
    ArrayList<AccessPoint> data;
    private DBHelper dbHelper;
    Button btn;

    public ACAdapter(Context context, int resource, ArrayList<AccessPoint> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutId = resource;
        this.data = objects;
    }

    private class ViewHolder {
        CheckBox name;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, null);
        }
        TextView txtMAC = (TextView) convertView.findViewById(R.id.txtMAC);
        TextView txtSSID = (TextView) convertView.findViewById(R.id.txtSSID);
        TextView txtLevel = (TextView) convertView.findViewById(R.id.txtLevel);
        TextView txtFrequency = (TextView) convertView.findViewById(R.id.txtFrequency);
        ProgressBar prBar = (ProgressBar) convertView.findViewById(R.id.prBarLevel);
        ViewHolder holder = new ViewHolder();
        holder.name = (CheckBox) convertView.findViewById(R.id.chkBox);
        convertView.setTag(holder);
        //CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.chkBox);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                AccessPoint accessPoint = data.get(position);
                accessPoint.setSelected(cb.isChecked());
            }
        });

        AccessPoint ac = data.get(position);

        if (ac != null) {
                txtMAC.setText(" " + ac.getBSSID());
                txtSSID.setText(" " + ac.getSSID());
                txtLevel.setText("Level: " + ac.getLevel());
                txtFrequency.setText("Frequency: " + ac.getFrequency());
                prBar.setProgress(ac.getLeveldBm() + 100);
        }
        return convertView;
    }
}
