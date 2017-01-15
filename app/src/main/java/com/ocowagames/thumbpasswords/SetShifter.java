package com.ocowagames.thumbpasswords;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by mexib on 1/14/2017.
 */

public class SetShifter extends AppCompatActivity implements View.OnClickListener{


    Button setShiftNum;
    EditText number;

    @Override
    @RequiresApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setshifter);

        number = (EditText) findViewById(R.id.editNumber);
        setShiftNum = (Button) findViewById(R.id.setShift);
        setShiftNum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String s;
        if(v.getId() == setShiftNum.getId()){
            if((s = number.getText().toString()).length()  > 0){
                if(Integer.parseInt(s) <= 10){
                    SharedPreferences prefs = this.getSharedPreferences("ocowagames.passwords", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("ocowagames.firsttime",s);
                    editor.commit();
                    startActivity(new Intent(SetShifter.this,PasswordDisplayer.class));
                }
            }


        }
    }

    @Override
    public void onUserLeaveHint(){
        super.onUserLeaveHint();
        finish();
    }
}
