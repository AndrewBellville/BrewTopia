package com.town.small.brewtopia.Timer;

import com.town.small.brewtopia.DataClass.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Andrew on 3/28/2015.
 */
public class TimerData {


    private BrewSchema brew;
    private long totalTime;
    private List<BoilAdditionsSchema> cloneAdditionsList = new ArrayList<BoilAdditionsSchema>();
    private List<BoilAdditionsSchema> additionsList = new ArrayList<BoilAdditionsSchema>();


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

    public void setTimeData(BrewSchema aBrewSchema)
    {
        brew = aBrewSchema;

        totalTime = brew.getBoilTime()*60000;
        //set ordered list largest to smallest time
        cloneAdditionsList.addAll(brew.getBoilAdditionlist());
        setAdditionsList(cloneAdditionsList);
    }

    private void setAdditionsList(List<BoilAdditionsSchema> list)
    {
        int largest = 0;
        int largestLocation = 0;

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

    //getter
    public long getTotalTime() {
        return totalTime;
    }
    public List<BoilAdditionsSchema> getAdditionsList() {
        return additionsList;
    }


}
