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
import android.widget.TextView;

import com.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.town.small.brewtopia.R;

public class BrewTimer extends ActionBarActivity {

    CountDownTimer timer;

    long hours;
    long min;
    long seconds;
    long millisRemaining;

    boolean isFinished = false;
    boolean allAdditionsComplete = false;

    BoilAdditionsSchema nextAddition;
    int nextAdditionIterator = 0;


    private TextView brewTimer;
    private TextView addition;
    private Button AckButton;
    private Ringtone r;
    Uri notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_timer);
        getActionBar().hide();

        //Set Alarm Manager
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);

        brewTimer = (TextView)findViewById(R.id.BrewTimeTextView);
        addition = (TextView)findViewById(R.id.AdditionTextView);
        AckButton = (Button)findViewById(R.id.AcknowledgeButton);
        AckButton.setVisibility(View.INVISIBLE);

        startTimer(TimerData.getInstance().getTotalTime());
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        r.stop();
        timer.cancel();
        this.finish();
    }

    public void startTimer(long totalTimeInMilli)
    {
        //Set First addition
        nextAddition = TimerData.getInstance().getAdditionsList().get(nextAdditionIterator);

        addition.setText("");

        timer = new CountDownTimer(totalTimeInMilli, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                millisRemaining = millisUntilFinished;//save if we want a pause feature
                setTimeDisplay(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                brewTimer.setText("Done");
                isFinished = true;
                FireAlarm();
            }
        }.start();
    }

    public void onAckClick(View aView)
    {
        r.stop();
        AckButton.setVisibility(View.INVISIBLE);

        if(isFinished)
            this.finish();
    }

    public void StopTimerClick(View aView)
    {
        r.stop();
        timer.cancel();
        this.finish();
    }

     //TODO: IF WANTED
    public void PauseTimerClick(View aView)
    {
        //if  pause save timer  value  then cancel
        r.stop();
        timer.cancel();
    }

    private void FireAlarm()
    {
        AckButton.setVisibility(View.VISIBLE);
        r.play();
    }

    private void setTimeDisplay(long millisUntilFinished)
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

        //Time for next addition fire alarm and move to next addition
        if(nextAddition.getAdditionTime()*60000 >= millisUntilFinished && !allAdditionsComplete)
        {
            FireAlarm();
            addition.setText("Add: " + nextAddition.getAdditionName());
            nextAdditionIterator++;
            if(nextAdditionIterator < TimerData.getInstance().getAdditionsList().size())
                nextAddition = TimerData.getInstance().getAdditionsList().get(nextAdditionIterator);
            else
                allAdditionsComplete = true;
        }
    }
}
