package com.town.small.brewtopia.Timer;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.town.small.brewtopia.R;

public class BrewTimer extends Fragment {

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

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_brew_timer, container, false);

        brewTimer = (TextView) view.findViewById(R.id.BrewTimeTextView);
        addition = (TextView) view.findViewById(R.id.AdditionTextView);
        brewName = (TextView) view.findViewById(R.id.TimerBrewNametextView);

        StartStopButton = (Button) view.findViewById(R.id.StartStopButton);
        StartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartStopTimerClick(view);
            }
        });

        StartStopButton.setText("Start");

        AckButton = (Button) view.findViewById(R.id.AcknowledgeButton);
        AckButton.setVisibility(View.INVISIBLE);
        AckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAckClick(view);
            }
        });

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

        context = getActivity();
        return view;
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
        //TODO:  DOESN'T WORK TO RE-OPEN TIMER IF CLOSE
        if(isBackground)
        {
            try{
                getActivity().finish();
                Intent intent = new Intent(context, TimerPager.class);
                startActivity(intent);
            }
            catch (Exception e){}
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
            getActivity().finish();
        }
        else
        {
            td.startTimer(getActivity());
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
