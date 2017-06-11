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

public class LMEConversion extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "LMEConversion";


    private Button CalculateButton;
    private EditText LME;
    private TextView DME;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lme_conversion);
        if(APPUTILS.isLogging)Log.e(LOG, "Entering: onCreate");

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("LME Conversion");

        LME = (EditText)findViewById(R.id.LMEeditText);
        DME = (TextView)findViewById(R.id.DMEtextView);

        CalculateButton = (Button)findViewById(R.id.LMECalculatebutton);
        CalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCalculateClick(view);
            }
        });

    }

    public void onCalculateClick(View aView)
    {
        if(LME.getText().toString().matches(""))
        {
            Toast.makeText(getApplicationContext(), "Invalid Values", Toast.LENGTH_LONG).show();
            return;
        }

        double lme = Double.parseDouble(LME.getText().toString());

        double dme = (lme * 0.837);

        //Calculation
        Double truncatedDouble = new BigDecimal(Double.toString(dme))
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        DME.setText(truncatedDouble.toString());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
