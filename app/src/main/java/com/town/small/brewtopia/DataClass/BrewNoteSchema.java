package com.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/8/2016.
 */
public class BrewNoteSchema {

    // Log cat tag
    private static final String LOG = "BrewNoteSchema";

    private long noteId =-1;
    private String BrewName;
    private long UserId;
    private String CreatedOn;
    private String BrewNote;

    // constructors
    public BrewNoteSchema() {
    }

    //Getters
    public String getBrewName() {
        return BrewName;
    }
    public long getUserId() {
        return UserId;
    }
    public String getCreatedOn() {
        return CreatedOn;
    }
    public String getBrewNote() {
        return BrewNote;
    }
    public long getNoteId() {
        return noteId;
    }



    //Setters
    public void setBrewName(String brewName) {
        BrewName = brewName;
    }
    public void setUserId(long userId) {
        UserId = userId;
    }
    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }
    public void setBrewNote(String brewNote) {
        BrewNote = brewNote;
    }
    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }
}
