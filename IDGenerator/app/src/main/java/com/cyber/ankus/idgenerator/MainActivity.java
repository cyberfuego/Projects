package com.cyber.ankus.idgenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.lang.String ;


public class MainActivity extends AppCompatActivity {
    TextView etID ;
    Button btnSub ;
    TextView tv ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        etID = findViewById(R.id.etID) ;
        btnSub = findViewById(R.id.btnSub) ;
        tv = findViewById(R.id.tv) ;

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idNumber = etID.getText().toString().trim() ;
                String year = idNumber.substring(0,2) ;
                String month = idNumber.substring(2,4) ;
                String day = idNumber.substring(4,6) ;
                Character ge = idNumber.charAt(6) ;
                int gender = Integer.parseInt(Character.toString(ge)) ;
                StringBuilder dob = new StringBuilder(year + "/" + month + "/") ;
                dob.append(day) ;
                String disp_gender = new String() ;
                disp_gender = gender > 5 ? getString(R.string.male) : getString(R.string.female) ;
                String citizenship = new String() ;
                int citizen = Integer.parseInt(Character.toString(idNumber.charAt(10))) ;
                citizenship = citizen == 1 ? getString(R.string.sacitizen) : getString(R.string.sapr) ;
                String display = "Date Of Birth: " + dob + "\n" + "Resident: " + citizenship + "\n" + "Gender: " + disp_gender ;
                tv.setText(display) ;
            }
        }) ;
    }
}
