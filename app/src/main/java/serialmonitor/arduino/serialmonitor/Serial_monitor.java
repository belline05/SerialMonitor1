package serialmonitor.arduino.serialmonitor;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Serial_monitor extends Activity {
    Button btOpen, btClose,  btWrite, btWrite2, btWrite3;
    EditText etWrite;
    TextView tvRead;
    Spinner spBaud;
    CheckBox cbAutoscroll;

    Physicaloid mPhysicaloid; // initialising library


    private String resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_monitor);

        btOpen  = (Button) findViewById(R.id.btOpen);
        btClose = (Button) findViewById(R.id.btClose);

        btWrite = (Button) findViewById(R.id.btWrite);
        btWrite2 = (Button) findViewById(R.id.btWrite2);
        btWrite3 = (Button) findViewById(R.id.btWrite3);

        etWrite = (EditText) findViewById(R.id.etWrite);
        tvRead  = (TextView) findViewById(R.id.tvRead);
        spBaud = (Spinner) findViewById(R.id.spBaud);
        cbAutoscroll = (CheckBox)findViewById(R.id.autoscroll);
        setEnabledUi(false);
        mPhysicaloid = new Physicaloid(this);


    }

    public void onClickOpen(View v) {
        //String baudtext = spBaud.getSelectedItem().toString();
        String baudtext  = "9600";

        if(mPhysicaloid.open()) {
            setEnabledUi(true);

            if(cbAutoscroll.isChecked())
            {
                tvRead.setMovementMethod(new ScrollingMovementMethod());
            }
            mPhysicaloid.addReadListener(new ReadLisener() {
                @Override
                public void onRead(int size) {
                    byte[] buf = new byte[size];
                    mPhysicaloid.read(buf, size);
                    tvAppend(tvRead, Html.fromHtml("<font color=blue>" + new String(buf) + "</font>"));
                }
            });
        } else {
            Toast.makeText(this, "Cannot open", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickClose(View v) {
        if(mPhysicaloid.close()) {
            mPhysicaloid.clearReadListener();
            setEnabledUi(false);
        }
    }

    public void onClickWrite(View v) {
        String A  = "MobileType_A";
        if(A.length()>0) {
            byte[] buf = A.getBytes();
            mPhysicaloid.write(buf, buf.length);
        }
    }
    public void onClickWrite1(View v) {
        String B  = "MobileType_B";
        if(B.length()>0) {
            byte[] buf = B.getBytes();
            mPhysicaloid.write(buf, buf.length);
        }
    }
    public void onClickWrite2(View v) {
        String C  = "MobileType_C";
        if(C.length()>0) {
            byte[] buf = C.getBytes();
            mPhysicaloid.write(buf, buf.length);
        }
    }

    private void setEnabledUi(boolean on) {
        if(on) {
            btOpen.setEnabled(false);
            spBaud.setEnabled(false);
            cbAutoscroll.setEnabled(false);
            btClose.setEnabled(true);

            btWrite.setEnabled(true);
//            btWrite2.setEnabled(true);
//            btWrite3.setEnabled(true);

            etWrite.setEnabled(true);
        } else {
            btOpen.setEnabled(true);
            spBaud.setEnabled(true);
            cbAutoscroll.setEnabled(true);
            btClose.setEnabled(false);

            btWrite.setEnabled(false);
//            btWrite2.setEnabled(false);
//            btWrite3.setEnabled(false);

            etWrite.setEnabled(false);
        }
    }

    Handler mHandler = new Handler();
    private void tvAppend(TextView tv, CharSequence text) {
        final TextView ftv = tv;
        final CharSequence ftext = text;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ftv.append(ftext);
            }
        });
    }
}
