package com.town.small.brewtopia.Timer;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;

import com.town.small.brewtopia.DataClass.*;
import com.town.small.brewtopia.Schedule.SchedulerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;

/**
 * Created by Andrew on 3/28/2015.
 */
public class TimerData {


    private BrewSchema brew;
    private long totalTime;
    private List<BoilAdditionsSchema> cloneAdditionsList = new ArrayList<BoilAdditionsSchema>();
    private List<BoilAdditionsSchema> additionsList = new ArrayList<BoilAdditionsSchema>();

    CountDownTimer timer;
    private Ringtone r;
    Uri notification;

    //TODO: Make list so we can display all remaining  additions on screen
    BoilAdditionsSchema nextAddition = null;
    private int nextAdditionIterator = 0;
    private long millisRemaining;
    private boolean allAdditionsComplete = false;
    private boolean alarmActive = false;
    private boolean timerActive = false;
    private boolean paused = false;

    //event handling
    private EventHandler eventHandler = null;

    //Singleton
    private static TimerData mInstance = null;

    public static TimerData getInstance() {
        if (mInstance == null) {
            mInstance = new TimerData();
        }
        return mInstance;
    }
    // constructor
    private TimerData() {
    }

    public void setTimerData(BrewSchema aBrewSchema)
    {
        brew = aBrewSchema;

        totalTime = brew.getBoilTime()*60000;

        //Clear list
        if (cloneAdditionsList.size() > 0)
            cloneAdditionsList.clear();

        //set ordered list largest to smallest time
        cloneAdditionsList.addAll(brew.getBoilAdditionlist());
        setAdditionsList(cloneAdditionsList);
    }

    public void setStartTimeDisplay()
    {
        if( (!(eventHandler == null)) && (!isTimerActive()))
            eventHandler.onTimerTick(totalTime, null);
    }

    private void setAdditionsList(List<BoilAdditionsSchema> list)
    {
        int largest = 0;
        int largestLocation = 0;

        //clear list
        if(additionsList.size() >  0)
            additionsList.clear();

        while(!list.isEmpty())
        {
            for(ListIterator<BoilAdditionsSchema> i = list.listIterator();  i.hasNext();)
            {
                //find largest addition time
                BoilAdditionsSchema baSchema = i.next();
                if(largest <= baSchema.getAdditionTime())
                {
                    largest = baSchema.getAdditionTime();
                    largestLocation = i.previousIndex();
                }
            }
            //Remove largest found and continue until list is empty
            additionsList.add(list.get(largestLocation));
            list.remove(largestLocation);

            //reset var
            largest = 0;
            largestLocation = 0;
        }
    }

    public void startTimer(Context context)
    {
        //If the time is  active and we don't want to restart it
        if(timerActive == true)
            return;

        //Once timer starts create a Schedule
        SchedulerHelper schedulerHelper= new SchedulerHelper(context);
        schedulerHelper.createSchedule(brew);

        //Set Alarm Manager
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(context, notification);

        //Initialise var
        allAdditionsComplete = false;
        nextAddition = null;
        nextAdditionIterator = 0;

        //Set First addition
        if(getAdditionsList().size() > 0)
            nextAddition = getAdditionsList().get(nextAdditionIterator);

        CreateTimer(totalTime);
    }

    private void CreateTimer(long startTime)
    {
        //Initialise var
        alarmActive = false;
        timerActive = true;
        paused = false;

        timer = new CountDownTimer(startTime, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                millisRemaining = millisUntilFinished;//save if we want a pause feature

                if( (!(nextAddition== null)) && nextAddition.getAdditionTime()*60000 >= millisUntilFinished && !allAdditionsComplete)
                {
                    if(!(eventHandler == null))
                        eventHandler.onTimerTick(millisUntilFinished, nextAddition);

                    nextAdditionIterator++;
                    if(nextAdditionIterator < getAdditionsList().size())
                        nextAddition = getAdditionsList().get(nextAdditionIterator);
                    else
                        allAdditionsComplete = true;

                    return;
                }

                if(!(eventHandler == null))
                    eventHandler.onTimerTick(millisUntilFinished, nextAddition);
            }

            @Override
            public void onFinish() {
                if(!(eventHandler == null))
                    eventHandler.onTimerFinish();
            }
        }.start();
    }

    public void FireAlarm()
    {
        r.play();
        alarmActive = true;
    }

    public void StopTimer()
    {
        r.stop();
        alarmActive = false;
        timerActive = false;
        paused = false;
        timer.cancel();
        timer = null;
    }

    public void PauseTimer()
    {
        r.stop();
        timerActive = false;
        alarmActive = false;
        paused = true;
        timer.cancel();
        timer = null;
    }

    public void ResumeTimer()
    {
        if(!paused)
            return;

        CreateTimer(millisRemaining);
    }

    public void AckTimer()
    {
        r.stop();
        alarmActive = false;
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler
    {
        void onTimerTick(long millisUntilFinished, BoilAdditionsSchema nextAddition);
        void onTimerFinish();
    }

    //getter
    public long getTotalTime() {
        return totalTime;
    }
    public List<BoilAdditionsSchema> getAdditionsList() {
        return additionsList;
    }
    public boolean isAllAdditionsComplete() {
        return allAdditionsComplete;
    }
    public boolean isAlarmActive() {
        return alarmActive;
    }
    public boolean isTimerActive() {
        return timerActive;
    }
    public boolean isPaused() {
        return paused;
    }
    public String getBrewName()
    {
        return brew.getBrewName();
    }

}
