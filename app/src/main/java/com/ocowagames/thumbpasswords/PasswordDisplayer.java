package com.ocowagames.thumbpasswords;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Created by mexib on 1/14/2017.
 */

public class PasswordDisplayer extends AppCompatActivity implements View.OnClickListener{


    private TextView pw;
    private Button saveButton,encryptButton;
    private EditText name,password;
    private boolean encrypted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);
        pw = (TextView) findViewById(R.id.passwords);
        pw.setMovementMethod(new ScrollingMovementMethod());
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);
        name = (EditText) findViewById(R.id.pwName);
        password = (EditText) findViewById(R.id.pWord);
        updateTextView();

        SharedPreferences prefs = this.getSharedPreferences("ocowagames.passwords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();


        System.out.println(prefs.getString("ocowagames.passwords.stored",""));
        System.out.println(prefs.getString("ocowagames.firsttime",""));
    }

    private void encryptString(){
        SharedPreferences prefs = this.getSharedPreferences("ocowagames.passwords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String stored = prefs.getString("ocowagames.passwords.stored","");
        String s = prefs.getString("ocowagames.firsttime","");
        int x = 0;
        if(s.length() > 0){
            x = Integer.parseInt(s);
        }

        String encryptedString = "";
        for(int i = 0; i < stored.length(); i++){
            char c = stored.charAt(i);
            String p = "" + c;
            if(!p.equals(",")){
                c+=x;
                p = "" + c;
            }
            encryptedString+=p;
        }
        encrypted=true;
        editor.putString("ocowagames.passwords.stored",encryptedString);
        editor.commit();
        updateTextView();

    }
    private void decryptString(){
        SharedPreferences prefs = this.getSharedPreferences("ocowagames.passwords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String stored = prefs.getString("ocowagames.passwords.stored","");
        int x = Integer.parseInt(prefs.getString("ocowagames.firsttime",""));

        String decrypted = "";
        for(int i = 0; i < stored.length(); i++){
            char c = stored.charAt(i);
            String p = "" + c;

            if(!p.equals(",")){
                c-=x;
                p = "" + c;
            }

            decrypted+=p;
        }
        encrypted=false;
        editor.putString("ocowagames.passwords.stored",decrypted);
        editor.commit();
        updateTextView();
    }
    private void savePasswords(String s1,String s2){
        SharedPreferences prefs = this.getSharedPreferences("ocowagames.passwords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if(encrypted) {
            decryptString();

        }

        String stored = prefs.getString("ocowagames.passwords.stored","");
        String newPasswords = stored + ",,,," + s1 + ",," + s2;
        System.out.println(stored);
        System.out.println("-------------");
        System.out.println(newPasswords);


        editor.putString("ocowagames.passwords.stored",newPasswords);
        editor.commit();
    }
    private void updateTextView(){
        SharedPreferences prefs = this.getSharedPreferences("ocowagames.passwords", Context.MODE_PRIVATE);
        String stored = prefs.getString("ocowagames.passwords.stored","");

        String toSet = "";
        String[] sArray = stored.split(",,,,");
        System.out.println(Arrays.toString(sArray));

        for(String s : sArray){
            if(s.contains(",,")) {
                String[] arr = s.split(",,");
                System.out.println(Arrays.toString(arr));
                toSet += arr[0] + ": " + arr[1] + "\n";
            }
        }
        pw.setText(toSet);

    }

    @Override
    public void onUserLeaveHint(){
        super.onUserLeaveHint();
        if(encrypted){
            decryptString();
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == saveButton.getId()){
            String s1 = name.getText().toString();
            String s2 = password.getText().toString();
            if(s1.length()>0&&s2.length()>0) {
                savePasswords(s1, s2);
                name.setText("");
                password.setText("");
                updateTextView();
            }
        }
    }
}
