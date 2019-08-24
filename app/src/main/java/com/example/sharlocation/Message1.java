package com.example.sharlocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.BatchUpdateException;


public class Message1 extends AppCompatActivity {

    Button Enter;
    EditText Number;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message1);

        Enter = (Button)findViewById(R.id.button_enter);
        Number = (EditText)findViewById(R.id.editText_Number);

        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });

    }

    public void Back(){

        String message = Number.getText().toString();
        int c = 0;
        char msg[] = message.toCharArray();
        for(int i=0;i<msg.length;i++){
            int temp = (int) msg[i];
            if(temp>=48 && temp<=57){

            }
            else{
                c++;
            }
        }
        if(c==0) {
            Intent intent = new Intent();
            intent.putExtra("key", message);
            setResult(5, intent);
            finish();
        }
        else{
            Toast.makeText(Message1.this,"Invalid Mobile Number", Toast.LENGTH_SHORT).show();
        }




    }
}
