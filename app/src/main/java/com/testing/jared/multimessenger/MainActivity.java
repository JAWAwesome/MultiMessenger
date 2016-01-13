package com.testing.jared.multimessenger;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SmsManager sms;
    public final String TAG = this.toString();
    int messages = 0;
    TextView count;
    String phoneNo = "4439870209";
    boolean contacts = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        count = (TextView) findViewById(R.id.Size);
        setSupportActionBar(toolbar);
        sms = SmsManager.getDefault();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Choosing Contact", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                popup();
            }
        });
    }

    @Override
    protected void onActivityResult(int request,int result, Intent data){
        // find contact
        if (contacts) {
            try {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                phoneNo = cursor.getString(phoneIndex);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // find number
        try {
            messages = Integer.parseInt(count.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "onActivityResult: " + phoneNo);
        // send
        for (int i = 0; i < messages; i++) {
            try {
                Log.i(TAG, "Message: " + (i + 1) + " of, " + messages);
                sms.sendTextMessage(phoneNo, null, "Hi I am testing my app again, message: " + (i + 1) + " of, " + messages, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public void popup () {
        // Verify
        final Dialog dialog = new Dialog(this);
        // use the layout file created
        dialog.setContentView(R.layout.selcetpopup);
        dialog.setTitle("Prompt");
        // Find the buttons
        Button delete=(Button)dialog.findViewById(R.id.button_PopUp_Delete);
        Button cancel=(Button)dialog.findViewById(R.id.button_PopUp_Cancel);
        // Show
        dialog.show();
        // Look to make sure the buttons are intentional
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // No
                dialog.dismiss();
                contacts = false;
                onActivityResult(1,1,new Intent());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Yes
                dialog.dismiss();
                contacts = true;
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void up(View view) {
        count.setText(String.valueOf(Integer.parseInt(count.getText().toString())+1));
    }

    public void down(View view) {
        String temp = String.valueOf(Integer.parseInt(count.getText().toString()) - 1);
        if (temp.equals("-1")) {
            temp = "0";
        }
        count.setText(String.valueOf(temp));
    }
}
