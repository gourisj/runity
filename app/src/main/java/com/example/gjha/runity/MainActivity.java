package com.example.gjha.runity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    Integer selection = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText uname = (EditText) findViewById(R.id.username_edittext);
        final EditText passwd = (EditText) findViewById(R.id.password_edittext);

        final Button loginButton = (Button) findViewById(R.id.login_main);
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (checkPassword(uname.getText(), passwd.getText())) {

                    // Create an explicit Intent for starting the HelloAndroid
                    // Activity
                    final CharSequence[] Models = {"Selection by Company", "Selection By NGO"};
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(MainActivity.this);
                    alt_bld.setIcon(R.drawable.running);
                    alt_bld.setTitle("Please select");
                    alt_bld.setSingleChoiceItems(Models, -1, new DialogInterface
                            .OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            Toast.makeText(getApplicationContext(),
                                    "Your selection is  " + Models[item], Toast.LENGTH_SHORT).show();
                            selection = item;
                            if ( selection == 0 )  {

                                Intent helloAndroidIntent = new Intent(MainActivity.this,
                                        CompanyListActivity.class);
                                startActivity(helloAndroidIntent);
                            }
                            else {
                                Intent helloAndroidIntent = new Intent(MainActivity.this,
                                        ItemNGOListActivity.class);
                                startActivity(helloAndroidIntent);
                            }

                        }
                    });
                    AlertDialog alert = alt_bld.create();
                    alert.show();


                    // Use the Intent to start the HelloAndroid Activity

                } else {
                    uname.setText("");
                    passwd.setText("");
                }
            }
        });
    }

    private boolean checkPassword(Editable uname, Editable passwd) {
        // Just pretending to extract text and check password
        return new Random().nextBoolean();
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

    public void proceedAnyway (View view) {
        Intent intent = new Intent(MainActivity.this , MainActivity2Activity.class);
        startActivity(intent);
    }
}
