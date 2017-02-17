package com.dev.town.small.brewtopia.Timer;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.dev.town.small.brewtopia.DataClass.BoilAdditionsSchema;
import com.dev.town.small.brewtopia.R;

public class BrewTimer extends Fragment {

    long hours;
    long min;
    long seconds;

    private TextView brewTimer;
    private ListView addition;
    private TextView brewName;
    private Button AckButton;
    private Button StartStopButton;
    private Button pauseButton;

    private TimerData td;
    private boolean isBackground = false;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_brew_timer, container, false);

        brewTimer = (TextView) view.findViewById(R.id.BrewTimeTextView);
        brewName = (TextView) view.findViewById(R.id.TimerBrewNametextView);

        StartStopButton = (Button) view.findViewById(R.id.StartStopButton);
        StartStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartStopTimerClick(view);
            }
        });

        pauseButton = (Button) view.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PauseTimerClick(view);
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

        addition = (ListView) view.findViewById(R.id.AdditionListView);
        CustomAddListAdapter adapter = new CustomAddListAdapter(td.getAdditionsList(), getActivity());
        addition.setAdapter(adapter);

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
        else if(td.isPaused())
        {
            td.ResumeTimer();
            SetStartStopButtonText();
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
            pauseButton.setVisibility(View.VISIBLE);
        }
        else{
            StartStopButton.setText("Start");
            pauseButton.setVisibility(View.INVISIBLE);
        }
    }

    public void PauseTimerClick(View aView)
    {
        td.PauseTimer();
        SetStartStopButtonText();
    }

    private void FireAlarm()
    {
        CheckForBackground();
        td.FireAlarm();
    }

    public void TimerFinished() {
        brewTimer.setText("Done");
        FireAlarm();
        if (td.isAlarmActive()) {
            AckButton.setVisibility(View.VISIBLE);
            pauseButton.setVisibility(View.INVISIBLE);
        }
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

            HighlightAddition(nextAddition.getAdditionTime());
        }
    }

    private void HighlightAddition(int nextAdditionTime)
    {
        for (int i = 0;i < td.getAdditionsList().size();i++)
        {
            if(td.getAdditionsList().get(i).getAdditionTime()== nextAdditionTime)
                addition.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.AccentColor));
            else if(td.getAdditionsList().get(i).getAdditionTime()> nextAdditionTime)
                addition.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.ColorToneD2));
        }


    }
}
