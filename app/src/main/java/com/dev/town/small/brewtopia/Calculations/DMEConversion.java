package com.dev.town.small.brewtopia.Calculations;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.town.small.brewtopia.DataClass.APPUTILS;
import com.dev.town.small.brewtopia.R;

import java.math.BigDecimal;

public class DMEConversion extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "DMEConversion";


    private Button CalculateButton;
    private EditText DME;
    private TextView LME;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dme_conversion);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("DME Conversion");

        DME = (EditText)findViewById(R.id.DMEeditText);
        LME = (TextView)findViewById(R.id.LMEtextView);

        CalculateButton = (Button)findViewById(R.id.DMECalculatebutton);
        CalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculateClick(view);
            }
        });

    }

    public void onCalculateClick(View aView)
    {
        if(DME.getText().toString().matches(""))
        {
            Toast.makeText(getApplicationContext(), "Invalid Values", Toast.LENGTH_LONG).show();
            return;
        }

        double dme = Double.parseDouble(DME.getText().toString());

        double lme = (dme * 1.194);

        //Calculation
        Double truncatedDouble = new BigDecimal(Double.toString(lme))
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        LME.setText(truncatedDouble.toString());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
