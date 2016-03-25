package com.town.small.brewtopia.Calculations;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;

import com.town.small.brewtopia.R;

import java.math.BigDecimal;

public class SpecificGravity extends ActionBarActivity {

    // Log cat tag
    private static final String LOG = "SpecificGravity";


    private Button CalculateButton;
    private EditText SG;
    private TextView brix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_gravity);
        Log.e(LOG, "Entering: onCreate");

        getActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Specific Gravity");

        SG = (EditText)findViewById(R.id.SGeditText);
        brix = (TextView)findViewById(R.id.BrixtextView);
        CalculateButton = (Button)findViewById(R.id.SGCalculatebutton);

    }

    public void onCalculateClick(View aView)
    {
        double sg = Double.parseDouble(SG.getText().toString());

        double bx = (((182.4601*sg-775.6821)*sg+1262.7794)*sg-669.5622);

        //Calculation
        Double truncatedDouble = new BigDecimal(Double.toString(bx))
                .setScale(4, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        brix.setText(truncatedDouble.toString());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

}
