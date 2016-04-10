package com.town.small.brewtopia.Calculations;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.town.small.brewtopia.R;

import java.math.BigDecimal;

public class BrixCalculations extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "BrixCalculations";


    private Button CalculateButton;
    private EditText brix;
    private EditText divisor;
    private TextView AB;
    private TextView SG;

    private double div = 1.04;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brix_calculations);
        Log.e(LOG, "Entering: onCreate");

        toolbar=(Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Brix Calculations");

        brix = (EditText)findViewById(R.id.BrixeditText);
        divisor = (EditText)findViewById(R.id.DivisoreditText);
        AB = (TextView)findViewById(R.id.ActualBrixtextView);
        SG = (TextView)findViewById(R.id.SpecificGravitytextView);
        CalculateButton = (Button)findViewById(R.id.BrixCalculatebutton);

        divisor.setText(Double.toString(div));

    }

    public void onCalculateClick(View aView)
    {

        if(brix.getText().toString().matches("")  || divisor.getText().toString().matches(""))
        {
            Toast.makeText(getApplicationContext(), "Invalid Values", Toast.LENGTH_LONG).show();
            return;
        }

        double brx = Double.parseDouble(brix.getText().toString());
        double d = Double.parseDouble(divisor.getText().toString());

        if(d <= 0.0)
        {
            Toast.makeText(getApplicationContext(), "Invalid divisor", Toast.LENGTH_LONG).show();
            return;
        }

        double actualBrix = (brx / d);
        double sg = (actualBrix/(258.6-((actualBrix/258.2)*227.1)))+1;

        //Calculation
        Double truncatedDouble = new BigDecimal(Double.toString(actualBrix))
                .setScale(4, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        AB.setText(truncatedDouble.toString());

        truncatedDouble = new BigDecimal(Double.toString(sg))
                .setScale(4, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        SG.setText(truncatedDouble.toString());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
