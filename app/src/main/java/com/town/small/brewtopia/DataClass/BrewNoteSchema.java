package com.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/8/2016.
 */
public class BrewNoteSchema {

    // Log cat tag
    private static final String LOG = "BrewNoteSchema";

    private int noteId =-1;
    private String BrewName;
    private String UserName;
    private String CreatedOn;
    private String BrewNote;

    // constructors
    public BrewNoteSchema() {
    }

    //Getters
    public String getBrewName() {
        return BrewName;
    }
    public String getUserName() {
        return UserName;
    }
    public String getCreatedOn() {
        return CreatedOn;
    }
    public String getBrewNote() {
        return BrewNote;
    }
    public int getNoteId() {
        return noteId;
    }



    //Setters
    public void setBrewName(String brewName) {
        BrewName = brewName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }
    public void setBrewNote(String brewNote) {
        BrewNote = brewNote;
    }
    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
}
