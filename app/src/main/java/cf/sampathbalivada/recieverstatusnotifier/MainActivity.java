package cf.sampathbalivada.recieverstatusnotifier;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                if(state == TelephonyManager.CALL_STATE_RINGING) {
                    Toast.makeText(getApplicationContext(),"Phone Is Ringing. Number: " + phoneNumber, Toast.LENGTH_LONG).show();
                    TextView incomingNumberView = findViewById(R.id.incomingNumberView);
                    incomingNumberView.setText(phoneNumber);
                    endIncomingCall(phoneNumber);
                }
                if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    Toast.makeText(getApplicationContext(),"Phone is Currently in A call", Toast.LENGTH_LONG).show();
                }
                if(state == TelephonyManager.CALL_STATE_IDLE) {
                    //Do Nothing
                }
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void endIncomingCall(String phoneNumber) {
        Toast.makeText(getApplicationContext(),"Call Ended", Toast.LENGTH_LONG).show();

        TelecomManager telecomManager = (TelecomManager)getSystemService(Context.TELECOM_SERVICE);

        if (telecomManager != null && ContextCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
            telecomManager.endCall();
            sendSms(phoneNumber);
        }
    }

    public void sendSms(String phoneNumber) {
        if(phoneNumber.length() >= 10) {
            System.out.println("We are in");
            Date currentTime = Calendar.getInstance().getTime();
            String currentTimeString = currentTime.toString();
            SmsManager smsManager = SmsManager.getDefault();
            System.out.println("Sending Message");
            smsManager.sendTextMessage(phoneNumber, null, "The user you are trying to call is currently busy. Please call later.\n\nThis is a System Generated Message. Time: " + currentTimeString,
                    null, null);
            System.out.println("Message Sent");
        }
    }

}

