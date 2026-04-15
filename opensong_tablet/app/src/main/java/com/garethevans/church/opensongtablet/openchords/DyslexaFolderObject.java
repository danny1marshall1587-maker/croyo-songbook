package com.garethevans.church.opensongtablet.openchords;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DyslexaFolderObject {
    // This is a json object that keeps our ownerID and a note of our folders/UUIDs
    @Nullable private String ownerID;
    @Nullable private ArrayList<DyslexaFolderRecordObject> openSongFolderRecordObjects;

    @Nullable public String getOwnerID() {
        return ownerID;
    }
    @Nullable public ArrayList<DyslexaFolderRecordObject> getDyslexaFolderRecordObjects() {
        return openSongFolderRecordObjects;
    }

    public void setOwnerID(@Nullable String ownerID) {
        this.ownerID = ownerID;
    }
    public void setDyslexaFolderRecordObjects(@Nullable ArrayList<DyslexaFolderRecordObject> openSongFolderRecordObjects) {
        this.openSongFolderRecordObjects = openSongFolderRecordObjects;
    }
}
