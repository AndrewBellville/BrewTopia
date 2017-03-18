package com.dev.town.small.brewtopia.DataClass;

/**
 * Created by Andrew on 4/8/2016.
 */
public class BrewNoteSchema {

    // Log cat tag
    private static final String LOG = "BrewNoteSchema";

    private long noteId =0;
    private long BrewId=0;
    private String CreatedOn;
    private String BrewNote;

    // constructors
    public BrewNoteSchema() {
    }

    //Getters
    public long getBrewId() {
        return BrewId;
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
    public void setBrewId(long brewId) {
        this.BrewId = brewId;
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
