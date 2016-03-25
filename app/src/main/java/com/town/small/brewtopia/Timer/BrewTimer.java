package com.town.small.brewtopia.Timer;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.town.small.brewtopia.R;

public class BrewTimer extends ActionBarActivity {

    long hours;
    long min;
    long seconds;

    private TextView brewTimer;
    private TextView addition;
    private TextView brewName;
    private Button AckButton;
    private Button StartStopButton;

    private TimerData td;
    private boolean isBackground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_timer);
        getActionBar().hide();

        brewTimer = (TextView)findViewById(R.id.BrewTimeTextView);
        addition = (TextView)findViewById(R.id.AdditionTextView);
        brewName = (TextView)findViewById(R.id.TimerBrewNametextView);

        StartStopButton = (Button)findViewById(R.id.StartStopButton);
        StartStopButton.setText("Start");

        AckButton = (Button)findViewById(R.id.AcknowledgeButton);
        AckButton.setVisibility(View.INVISIBLE);

        addition.setText("");

        td = TimerData.getInstance();
        brewName.setText(td.getBrewName());
        // assign event handler
        td.setEventHandler(new TimerData.EventHandler() {
            @Override
            public void onTimerTick(long millisUntilFinished, BoilAdditionsSchema nextAddition ) {
                setTimeDisplay(millisUntilFinished,nextAddition);
            }

            @Override
            public void onTimerFinish(){
                TimerFinished();
            }
        });

        td.setStartTimeDisplay();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        isBackground= true;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        isBackground= false;
        SetStartStopButtonText();
    }

    private void CheckForBackground()
    {
        if(isBackground)
        {
            this.finish();
            startActivity(getIntent());
        }
    }

    public void onAckClick(View aView)
    {
        td.AckTimer();
        AckButton.setVisibility(View.INVISIBLE);
    }

    public void StartStopTimerClick(View aView)
    {
        if(td.isTimerActive())
        {
            td.StopTimer();
            this.finish();
        }
        else
        {
            td.startTimer(getApplicationContext());
            SetStartStopButtonText();
        }
    }

    private void SetStartStopButtonText()
    {
        if(td.isTimerActive())
        {
            StartStopButton.setText("Stop");
        }
        else{
            StartStopButton.setText("Start");
        }
    }

     //TODO: IF WANTED
    public void PauseTimerClick(View aView)
    {
        td.PauseTimer();
    }

    private void FireAlarm()
    {
        CheckForBackground();
        td.FireAlarm();
    }

    public void TimerFinished() {
        brewTimer.setText("Done");
        FireAlarm();
        if (td.isAlarmActive())
            AckButton.setVisibility(View.VISIBLE);
    }

    private void setTimeDisplay(long millisUntilFinished, BoilAdditionsSchema nextAddition)
    {
        long x = millisUntilFinished/1000;
        seconds = x % 60;
        x /= 60;
        min = x % 60;
        x /= 60;
        hours = x % 24;

        String str_hours = "";
        String str_mins = "";
        String str_secs = "";

        //set 0 padding
        if(hours < 10)
            str_hours = "0";
        if(min < 10)
            str_mins = "0";
        if(seconds < 10)
            str_secs = "0";

        brewTimer.setText(str_hours + hours + ":" + str_mins + min +  ":"  + str_secs +  seconds);

        if (td.isAlarmActive())
            AckButton.setVisibility(View.VISIBLE);

        //Time for next addition fire alarm and move to next addition
        if(nextAddition == null)
            return;

        if(nextAddition.getAdditionTime()*60000 >= millisUntilFinished )
        {
            if(!td.isAllAdditionsComplete())FireAlarm();

            addition.setText("Add: " + nextAddition.getAdditionName());
        }
    }
}
