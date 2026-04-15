package com.garethevans.church.opensongtablet.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.garethevans.church.opensongtablet.R;
import com.garethevans.church.opensongtablet.interfaces.MainActivityInterface;
import com.garethevans.church.opensongtablet.nearby.ShareableObject;
import com.garethevans.church.opensongtablet.openchords.OpenChordsTag;
import com.garethevans.church.opensongtablet.songprocessing.Song;
import com.garethevans.church.opensongtablet.songprocessing.SongId;

import java.io.File;
import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 11;
    private final Context c;
    private final MainActivityInterface mainActivityInterface;
    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "SQLiteHelper";

    public SQLiteHelper(Context c) {
        // Don't create the database here as we don't want to recreate on each call.
        super(c,  SQLite.DATABASE_NAME, null, DATABASE_VERSION);
        this.c = c;
        mainActivityInterface = (MainActivityInterface) c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // If the table doesn't exist, create it.
        if (db!=null) {
            try {
                db.execSQL(SQLite.CREATE_TABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + SQLite.TABLE_NAME + ";");

        // Create tables again
        onCreate(db);
    }



    // Create and reset the database
    public SQLiteDatabase getDB() {
        try {
            File f = mainActivityInterface.getStorageAccess().getAppSpecificFile("Database", "", SQLite.DATABASE_NAME);
            if (f == null) {
                Log.e(TAG, "getAppSpecificFile returned null for Database");
                return null;
            }
            return SQLiteDatabase.openOrCreateDatabase(f, null);
        } catch (Exception e) {
            Log.e(TAG, "Failed to open or create database: " + SQLite.DATABASE_NAME, e);
            return null;
        }
    }
    void emptyTable(SQLiteDatabase db) {
        // This drops the table if it exists (wipes it ready to start again)
        if (db!=null) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + SQLite.TABLE_NAME + ";");
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void resetDatabase() {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                emptyTable(db);
                onCreate(db);
            } finally {
                db.close();
            }
        }
    }

    // Create, delete and update entries
    public void removeOldSongs(ArrayList<String> songIds) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                mainActivityInterface.getCommonSQL().removeOldSongs(db, songIds);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
    }
    public void insertFast() {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                mainActivityInterface.getCommonSQL().insertFast(db);
                db.setTransactionSuccessful();
            } catch (OutOfMemoryError | Exception e) {
                db.setTransactionSuccessful();
                e.printStackTrace();
            } finally {
                db.endTransaction();
                db.close();
            }
        }
    }
    public void createSong(String folder, String filename) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                mainActivityInterface.getCommonSQL().createSong(db, folder, filename);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
    }
    public void updateSong(Song thisSong) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                mainActivityInterface.getCommonSQL().updateSong(db, thisSong);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
    }
    public boolean deleteSong(String folder, String file) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().deleteSong(db, folder, file) > -1;
            } catch (OutOfMemoryError | Exception e) {
                return false;
            } finally {
                db.close();
            }
        }
        return false;
    }
    public void renameSong(String oldFolder, String newFolder, String oldName, String newName) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                mainActivityInterface.getCommonSQL().renameSong(db, oldFolder, newFolder, oldName, newName);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
    }



    // Search for entries in the database
    public ArrayList<String> getFolders() {
        // Get the database
        try (SQLiteDatabase db = getDB()) {
            return mainActivityInterface.getCommonSQL().getFolders(db);
        } catch (OutOfMemoryError | Exception e) {
            Log.d(TAG,"SQLite error - likely DB doesn't exist yet");
            //e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public boolean songExists(String folder, String filename) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().songExists(db, folder, filename);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return false;
    }
    public Song getSpecificSong(String folder, String filename) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getSpecificSong(db, folder, filename);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        
        Song thisSong = new Song();
        thisSong.setFolder(folder);
        thisSong.setFilename(filename);
        String songId = mainActivityInterface.getCommonSQL().getAnySongId(folder, filename);
        thisSong.setSongid(songId);
        return thisSong;
    }
    public Song getSongByUuid(String uuid) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getSongFromUuid(db, uuid);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return null;
    }
    public ArrayList<Song> getSongsByFilters(boolean searchByFolder, boolean searchByArtist,
                                             boolean searchByKey, boolean searchByTag,
                                             boolean searchByFilter, boolean searchByTitle,
                                             String folderVal, String artistVal, String keyVal,
                                             String tagVal, String filterVal, String titleVal,
                                             boolean songMenuSortTitles) {

        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getSongsByFilters(db, searchByFolder,
                        searchByArtist, searchByKey, searchByTag, searchByFilter, searchByTitle,
                        folderVal, artistVal, keyVal, tagVal, filterVal, titleVal, songMenuSortTitles);
            } catch (OutOfMemoryError | Exception e) {
                Log.d(TAG, "Table doesn't exist or other error");
                resetDatabase();
            } finally {
                db.close();
            }
        }
        return new ArrayList<>();
    }
    public ArrayList<Song> openChordsSyncGetSongsFromFolder(String folder) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().openChordsSyncGetSongsFromFolder(db, folder);
            } catch (Exception | OutOfMemoryError e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return new ArrayList<>();
    }
    public Song getOpenChordsSong(String folder, String uuid) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getOpenChordsSong(db, folder, uuid);
            } catch (Exception | OutOfMemoryError e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return null;
    }

    public String[] getUuidFromFolderAndFile(String folderAndFile) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getUuidFromFolderAndFile(db, folderAndFile);
            } catch (Exception | OutOfMemoryError e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return null;
    }

    public ArrayList<OpenChordsTag> getThemesFromFilesInFolder(String folder) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getThemesFromFilesInFolder(db, folder);
            } catch (Exception | OutOfMemoryError e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return new ArrayList<>();
    }

    public String getKey(String folder, String filename) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getKey(db, folder, filename);
            } catch (Exception | OutOfMemoryError e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return "";
    }
    public ArrayList<String> getThemeTags() {
        // Get unique theme tags
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getUniqueThemeTags(db);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return new ArrayList<>();
    }
    public ArrayList<String> renameThemeTags(String oldTag, String newTag) {
        // Rename matching tags if found and don't already exist
        try (SQLiteDatabase db = getDB(); SQLiteDatabase db2 = mainActivityInterface.getNonDyslexaSQLiteHelper().getDB()) {
            return mainActivityInterface.getCommonSQL().renameThemeTags(db, db2, oldTag, newTag);
        } catch (OutOfMemoryError | Exception e) {
            return new ArrayList<>();
        }
    }
    public String songsWithThemeTags(String tag) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getSongsWithThemeTag(db, tag);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return "";
    }

    public String getFolderForSong(String filename) {
        // Set the default folder
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getFolderForSong(db, filename);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return c.getString(R.string.mainfoldername);
    }

    public Song getSongFromMidiIndex(int midiIndex) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getSongFromMidiIndex(db, midiIndex);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return null;
    }
    public void exportDatabase() {
        // Export a csv version of the temporary database
        mainActivityInterface.getCommonSQL().exportDatabase(getDB(),"SongDatabase.csv");
        getDB().close();
    }

    public ArrayList<ShareableObject> getShareableSongs() {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getShareableSongs(db);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return new ArrayList<>();
    }
    public String[] getSongCreationInfo(String folder, String filename) {
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getSongCreationInfo(db, folder, filename);
            } catch (OutOfMemoryError | Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return new String[]{"", "", "false"};
    }

    public ArrayList<SongId> getSongIds() {
        // Create an array of simple song details - used for the web server
        SQLiteDatabase db = getDB();
        if (db != null) {
            try {
                return mainActivityInterface.getCommonSQL().getSongIds(db);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return new ArrayList<>();
    }
}


