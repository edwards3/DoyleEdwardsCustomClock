package cmsc434.doyleedwardsclock2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

    private boolean twelveHourEnabled;
    final Handler handler = new Handler();

    int dayTime = 6;
    int nightTime = 18;
    int index = 0;

    //Array that holds all the transition colors
    final int[] colors = {0x000000, 0x080808, 0x181818, 0x282828,
                          0x282828, 0x484848, 0x505050, 0x585858,
                          0x686868, 0x707070, 0x787878, 0x888888,
                          0x909090, 0x989898, 0xA8A8A8, 0xB0B0B0,
                          0xB8B8B8, 0xC8C8C8, 0xD0D0D0, 0xD8D8D8,
                          0xE0E0E0, 0xF0F0F0 ,0xF8F8F8, 0xFFFFFF, //AM Hours

                          0xFFFFFF, 0xF8F8F8, 0xF0F0F0, 0xE0E0E0,
                          0xD8D8D8, 0xD0D0D0, 0xC8C8C8, 0xB8B8B8,
                          0xB0B0B0, 0xA8A8A8, 0x989898, 0x909090,
                          0x888888, 0x787878, 0x707070, 0x686868,
                          0x585858, 0x505050, 0x484848, 0x383838,
                          0x282828, 0x181818, 0x080808, 0x000000}; //PM Hours

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("ST", "Starting activity");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onToggleClicked(View view) {
        twelveHourEnabled = ((ToggleButton) view).isChecked();
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(getString(R.string.twelve_hour_enabled), twelveHourEnabled);
        editor.commit();
    }

    public void updateColor(ClockView clock) {

        index++;

        if (index >= 48) {
            index = 0;
        }

        clock.setPaintColor(colors[index]);
        clock.invalidate();
    }

    @Override
    public void onResume() {
        super.onStart();

        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);

        final TextView hour = (TextView)findViewById(R.id.hour);
        final TextView minute = (TextView)findViewById(R.id.minute);
        final TextView second = (TextView)findViewById(R.id.second);
        final ClockView clock = (ClockView)findViewById(R.id.clockView);

        //Setting the initial time
        Time t = new Time();
        t.setToNow();
        index = t.hour * 2;
        if (t.minute > 30)
            index++;

        clock.setPaintColor(colors[index]);
        clock.invalidate();

        handler.post(new Runnable(){

            @Override
            public void run() {

                Time t = new Time();
                t.setToNow();

                if((t.minute == 30) || (t.minute == 0)){
                    updateColor(clock);
                }

                hour.setText(String.format("%02d", t.hour));
                minute.setText(String.format("%02d", t.minute));
                second.setText(String.format("%02d", t.second));

                handler.postDelayed(this,1000); // set time here to refresh textView
            }

        });
    }
}
