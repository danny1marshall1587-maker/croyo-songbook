package com.cryoprompter.data;

import android.database.Cursor;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SongDao_Impl implements SongDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SongEntity> __insertionAdapterOfSongEntity;

  private final EntityInsertionAdapter<SetlistEntity> __insertionAdapterOfSetlistEntity;

  private final EntityInsertionAdapter<SetlistSongCrossRef> __insertionAdapterOfSetlistSongCrossRef;

  private final EntityDeletionOrUpdateAdapter<SongEntity> __deletionAdapterOfSongEntity;

  private final EntityDeletionOrUpdateAdapter<SongEntity> __updateAdapterOfSongEntity;

  public SongDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSongEntity = new EntityInsertionAdapter<SongEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `songs` (`title`,`artist`,`bpm`,`chordProContent`,`originalKey`,`displayKey`,`capo`,`durationSeconds`,`syncMap`,`isTrained`,`isFavorite`,`lastAccessed`,`id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SongEntity value) {
        if (value.getTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTitle());
        }
        if (value.getArtist() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getArtist());
        }
        stmt.bindLong(3, value.getBpm());
        if (value.getChordProContent() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getChordProContent());
        }
        if (value.getOriginalKey() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getOriginalKey());
        }
        if (value.getDisplayKey() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getDisplayKey());
        }
        stmt.bindLong(7, value.getCapo());
        stmt.bindLong(8, value.getDurationSeconds());
        if (value.getSyncMap() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getSyncMap());
        }
        final int _tmp = value.isTrained() ? 1 : 0;
        stmt.bindLong(10, _tmp);
        final int _tmp_1 = value.isFavorite() ? 1 : 0;
        stmt.bindLong(11, _tmp_1);
        stmt.bindLong(12, value.getLastAccessed());
        stmt.bindLong(13, value.getId());
      }
    };
    this.__insertionAdapterOfSetlistEntity = new EntityInsertionAdapter<SetlistEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `setlists` (`name`,`color`,`lastModified`,`id`) VALUES (?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SetlistEntity value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        if (value.getColor() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getColor());
        }
        stmt.bindLong(3, value.getLastModified());
        stmt.bindLong(4, value.getId());
      }
    };
    this.__insertionAdapterOfSetlistSongCrossRef = new EntityInsertionAdapter<SetlistSongCrossRef>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `setlist_song_cross_ref` (`setlistId`,`songId`,`orderIndex`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SetlistSongCrossRef value) {
        stmt.bindLong(1, value.getSetlistId());
        stmt.bindLong(2, value.getSongId());
        stmt.bindLong(3, value.getOrderIndex());
      }
    };
    this.__deletionAdapterOfSongEntity = new EntityDeletionOrUpdateAdapter<SongEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `songs` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SongEntity value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfSongEntity = new EntityDeletionOrUpdateAdapter<SongEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `songs` SET `title` = ?,`artist` = ?,`bpm` = ?,`chordProContent` = ?,`originalKey` = ?,`displayKey` = ?,`capo` = ?,`durationSeconds` = ?,`syncMap` = ?,`isTrained` = ?,`isFavorite` = ?,`lastAccessed` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SongEntity value) {
        if (value.getTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTitle());
        }
        if (value.getArtist() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getArtist());
        }
        stmt.bindLong(3, value.getBpm());
        if (value.getChordProContent() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getChordProContent());
        }
        if (value.getOriginalKey() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getOriginalKey());
        }
        if (value.getDisplayKey() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getDisplayKey());
        }
        stmt.bindLong(7, value.getCapo());
        stmt.bindLong(8, value.getDurationSeconds());
        if (value.getSyncMap() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getSyncMap());
        }
        final int _tmp = value.isTrained() ? 1 : 0;
        stmt.bindLong(10, _tmp);
        final int _tmp_1 = value.isFavorite() ? 1 : 0;
        stmt.bindLong(11, _tmp_1);
        stmt.bindLong(12, value.getLastAccessed());
        stmt.bindLong(13, value.getId());
        stmt.bindLong(14, value.getId());
      }
    };
  }

  @Override
  public void insertSong(final SongEntity song) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSongEntity.insert(song);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertSetlist(final SetlistEntity setlist) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSetlistEntity.insert(setlist);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void addSongToSetlist(final SetlistSongCrossRef crossRef) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSetlistSongCrossRef.insert(crossRef);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int deleteSong(final SongEntity song) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__deletionAdapterOfSongEntity.handle(song);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int updateSong(final SongEntity song) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__updateAdapterOfSongEntity.handle(song);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public Flow<List<SongEntity>> getAllSongs() {
    final String _sql = "SELECT * FROM songs ORDER BY title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"songs"}, new Callable<List<SongEntity>>() {
      @Override
      public List<SongEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfBpm = CursorUtil.getColumnIndexOrThrow(_cursor, "bpm");
          final int _cursorIndexOfChordProContent = CursorUtil.getColumnIndexOrThrow(_cursor, "chordProContent");
          final int _cursorIndexOfOriginalKey = CursorUtil.getColumnIndexOrThrow(_cursor, "originalKey");
          final int _cursorIndexOfDisplayKey = CursorUtil.getColumnIndexOrThrow(_cursor, "displayKey");
          final int _cursorIndexOfCapo = CursorUtil.getColumnIndexOrThrow(_cursor, "capo");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfSyncMap = CursorUtil.getColumnIndexOrThrow(_cursor, "syncMap");
          final int _cursorIndexOfIsTrained = CursorUtil.getColumnIndexOrThrow(_cursor, "isTrained");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastAccessed = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessed");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<SongEntity> _result = new ArrayList<SongEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final SongEntity _item;
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final int _tmpBpm;
            _tmpBpm = _cursor.getInt(_cursorIndexOfBpm);
            final String _tmpChordProContent;
            if (_cursor.isNull(_cursorIndexOfChordProContent)) {
              _tmpChordProContent = null;
            } else {
              _tmpChordProContent = _cursor.getString(_cursorIndexOfChordProContent);
            }
            final String _tmpOriginalKey;
            if (_cursor.isNull(_cursorIndexOfOriginalKey)) {
              _tmpOriginalKey = null;
            } else {
              _tmpOriginalKey = _cursor.getString(_cursorIndexOfOriginalKey);
            }
            final String _tmpDisplayKey;
            if (_cursor.isNull(_cursorIndexOfDisplayKey)) {
              _tmpDisplayKey = null;
            } else {
              _tmpDisplayKey = _cursor.getString(_cursorIndexOfDisplayKey);
            }
            final int _tmpCapo;
            _tmpCapo = _cursor.getInt(_cursorIndexOfCapo);
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final String _tmpSyncMap;
            if (_cursor.isNull(_cursorIndexOfSyncMap)) {
              _tmpSyncMap = null;
            } else {
              _tmpSyncMap = _cursor.getString(_cursorIndexOfSyncMap);
            }
            final boolean _tmpIsTrained;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsTrained);
            _tmpIsTrained = _tmp != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final long _tmpLastAccessed;
            _tmpLastAccessed = _cursor.getLong(_cursorIndexOfLastAccessed);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item = new SongEntity(_tmpTitle,_tmpArtist,_tmpBpm,_tmpChordProContent,_tmpOriginalKey,_tmpDisplayKey,_tmpCapo,_tmpDurationSeconds,_tmpSyncMap,_tmpIsTrained,_tmpIsFavorite,_tmpLastAccessed,_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<SongEntity> getSongById(final int id) {
    final String _sql = "SELECT * FROM songs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"songs"}, new Callable<SongEntity>() {
      @Override
      public SongEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfBpm = CursorUtil.getColumnIndexOrThrow(_cursor, "bpm");
          final int _cursorIndexOfChordProContent = CursorUtil.getColumnIndexOrThrow(_cursor, "chordProContent");
          final int _cursorIndexOfOriginalKey = CursorUtil.getColumnIndexOrThrow(_cursor, "originalKey");
          final int _cursorIndexOfDisplayKey = CursorUtil.getColumnIndexOrThrow(_cursor, "displayKey");
          final int _cursorIndexOfCapo = CursorUtil.getColumnIndexOrThrow(_cursor, "capo");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfSyncMap = CursorUtil.getColumnIndexOrThrow(_cursor, "syncMap");
          final int _cursorIndexOfIsTrained = CursorUtil.getColumnIndexOrThrow(_cursor, "isTrained");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastAccessed = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessed");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final SongEntity _result;
          if(_cursor.moveToFirst()) {
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final int _tmpBpm;
            _tmpBpm = _cursor.getInt(_cursorIndexOfBpm);
            final String _tmpChordProContent;
            if (_cursor.isNull(_cursorIndexOfChordProContent)) {
              _tmpChordProContent = null;
            } else {
              _tmpChordProContent = _cursor.getString(_cursorIndexOfChordProContent);
            }
            final String _tmpOriginalKey;
            if (_cursor.isNull(_cursorIndexOfOriginalKey)) {
              _tmpOriginalKey = null;
            } else {
              _tmpOriginalKey = _cursor.getString(_cursorIndexOfOriginalKey);
            }
            final String _tmpDisplayKey;
            if (_cursor.isNull(_cursorIndexOfDisplayKey)) {
              _tmpDisplayKey = null;
            } else {
              _tmpDisplayKey = _cursor.getString(_cursorIndexOfDisplayKey);
            }
            final int _tmpCapo;
            _tmpCapo = _cursor.getInt(_cursorIndexOfCapo);
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final String _tmpSyncMap;
            if (_cursor.isNull(_cursorIndexOfSyncMap)) {
              _tmpSyncMap = null;
            } else {
              _tmpSyncMap = _cursor.getString(_cursorIndexOfSyncMap);
            }
            final boolean _tmpIsTrained;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsTrained);
            _tmpIsTrained = _tmp != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final long _tmpLastAccessed;
            _tmpLastAccessed = _cursor.getLong(_cursorIndexOfLastAccessed);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _result = new SongEntity(_tmpTitle,_tmpArtist,_tmpBpm,_tmpChordProContent,_tmpOriginalKey,_tmpDisplayKey,_tmpCapo,_tmpDurationSeconds,_tmpSyncMap,_tmpIsTrained,_tmpIsFavorite,_tmpLastAccessed,_tmpId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<SetlistWithSongs>> getSetlistsWithSongs() {
    final String _sql = "SELECT * FROM setlists ORDER BY lastModified DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[]{"setlist_song_cross_ref","songs","setlists"}, new Callable<List<SetlistWithSongs>>() {
      @Override
      public List<SetlistWithSongs> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
            final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
            final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final LongSparseArray<ArrayList<SongEntity>> _collectionSongs = new LongSparseArray<ArrayList<SongEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey = _cursor.getLong(_cursorIndexOfId);
              ArrayList<SongEntity> _tmpSongsCollection = _collectionSongs.get(_tmpKey);
              if (_tmpSongsCollection == null) {
                _tmpSongsCollection = new ArrayList<SongEntity>();
                _collectionSongs.put(_tmpKey, _tmpSongsCollection);
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipsongsAscomCryoprompterDataSongEntity(_collectionSongs);
            final List<SetlistWithSongs> _result = new ArrayList<SetlistWithSongs>(_cursor.getCount());
            while(_cursor.moveToNext()) {
              final SetlistWithSongs _item;
              final SetlistEntity _tmpSetlist;
              final String _tmpName;
              if (_cursor.isNull(_cursorIndexOfName)) {
                _tmpName = null;
              } else {
                _tmpName = _cursor.getString(_cursorIndexOfName);
              }
              final String _tmpColor;
              if (_cursor.isNull(_cursorIndexOfColor)) {
                _tmpColor = null;
              } else {
                _tmpColor = _cursor.getString(_cursorIndexOfColor);
              }
              final long _tmpLastModified;
              _tmpLastModified = _cursor.getLong(_cursorIndexOfLastModified);
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              _tmpSetlist = new SetlistEntity(_tmpName,_tmpColor,_tmpLastModified,_tmpId);
              ArrayList<SongEntity> _tmpSongsCollection_1 = null;
              final long _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpSongsCollection_1 = _collectionSongs.get(_tmpKey_1);
              if (_tmpSongsCollection_1 == null) {
                _tmpSongsCollection_1 = new ArrayList<SongEntity>();
              }
              _item = new SetlistWithSongs(_tmpSetlist,_tmpSongsCollection_1);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<SongEntity>> getSongsForSetlist(final int setlistId) {
    final String _sql = "SELECT * FROM songs WHERE id IN (SELECT songId FROM setlist_song_cross_ref WHERE setlistId = ?) ORDER BY (SELECT orderIndex FROM setlist_song_cross_ref WHERE setlistId = ? AND songId = songs.id) ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, setlistId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, setlistId);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"songs","setlist_song_cross_ref"}, new Callable<List<SongEntity>>() {
      @Override
      public List<SongEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfBpm = CursorUtil.getColumnIndexOrThrow(_cursor, "bpm");
          final int _cursorIndexOfChordProContent = CursorUtil.getColumnIndexOrThrow(_cursor, "chordProContent");
          final int _cursorIndexOfOriginalKey = CursorUtil.getColumnIndexOrThrow(_cursor, "originalKey");
          final int _cursorIndexOfDisplayKey = CursorUtil.getColumnIndexOrThrow(_cursor, "displayKey");
          final int _cursorIndexOfCapo = CursorUtil.getColumnIndexOrThrow(_cursor, "capo");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final int _cursorIndexOfSyncMap = CursorUtil.getColumnIndexOrThrow(_cursor, "syncMap");
          final int _cursorIndexOfIsTrained = CursorUtil.getColumnIndexOrThrow(_cursor, "isTrained");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfLastAccessed = CursorUtil.getColumnIndexOrThrow(_cursor, "lastAccessed");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<SongEntity> _result = new ArrayList<SongEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final SongEntity _item;
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final int _tmpBpm;
            _tmpBpm = _cursor.getInt(_cursorIndexOfBpm);
            final String _tmpChordProContent;
            if (_cursor.isNull(_cursorIndexOfChordProContent)) {
              _tmpChordProContent = null;
            } else {
              _tmpChordProContent = _cursor.getString(_cursorIndexOfChordProContent);
            }
            final String _tmpOriginalKey;
            if (_cursor.isNull(_cursorIndexOfOriginalKey)) {
              _tmpOriginalKey = null;
            } else {
              _tmpOriginalKey = _cursor.getString(_cursorIndexOfOriginalKey);
            }
            final String _tmpDisplayKey;
            if (_cursor.isNull(_cursorIndexOfDisplayKey)) {
              _tmpDisplayKey = null;
            } else {
              _tmpDisplayKey = _cursor.getString(_cursorIndexOfDisplayKey);
            }
            final int _tmpCapo;
            _tmpCapo = _cursor.getInt(_cursorIndexOfCapo);
            final int _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
            final String _tmpSyncMap;
            if (_cursor.isNull(_cursorIndexOfSyncMap)) {
              _tmpSyncMap = null;
            } else {
              _tmpSyncMap = _cursor.getString(_cursorIndexOfSyncMap);
            }
            final boolean _tmpIsTrained;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsTrained);
            _tmpIsTrained = _tmp != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final long _tmpLastAccessed;
            _tmpLastAccessed = _cursor.getLong(_cursorIndexOfLastAccessed);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item = new SongEntity(_tmpTitle,_tmpArtist,_tmpBpm,_tmpChordProContent,_tmpOriginalKey,_tmpDisplayKey,_tmpCapo,_tmpDurationSeconds,_tmpSyncMap,_tmpIsTrained,_tmpIsFavorite,_tmpLastAccessed,_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipsongsAscomCryoprompterDataSongEntity(
      final LongSparseArray<ArrayList<SongEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    // check if the size is too big, if so divide;
    if(_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      LongSparseArray<ArrayList<SongEntity>> _tmpInnerMap = new LongSparseArray<ArrayList<SongEntity>>(androidx.room.RoomDatabase.MAX_BIND_PARAMETER_CNT);
      int _tmpIndex = 0;
      int _mapIndex = 0;
      final int _limit = _map.size();
      while(_mapIndex < _limit) {
        _tmpInnerMap.put(_map.keyAt(_mapIndex), _map.valueAt(_mapIndex));
        _mapIndex++;
        _tmpIndex++;
        if(_tmpIndex == RoomDatabase.MAX_BIND_PARAMETER_CNT) {
          __fetchRelationshipsongsAscomCryoprompterDataSongEntity(_tmpInnerMap);
          _tmpInnerMap = new LongSparseArray<ArrayList<SongEntity>>(RoomDatabase.MAX_BIND_PARAMETER_CNT);
          _tmpIndex = 0;
        }
      }
      if(_tmpIndex > 0) {
        __fetchRelationshipsongsAscomCryoprompterDataSongEntity(_tmpInnerMap);
      }
      return;
    }
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `songs`.`title` AS `title`,`songs`.`artist` AS `artist`,`songs`.`bpm` AS `bpm`,`songs`.`chordProContent` AS `chordProContent`,`songs`.`originalKey` AS `originalKey`,`songs`.`displayKey` AS `displayKey`,`songs`.`capo` AS `capo`,`songs`.`durationSeconds` AS `durationSeconds`,`songs`.`syncMap` AS `syncMap`,`songs`.`isTrained` AS `isTrained`,`songs`.`isFavorite` AS `isFavorite`,`songs`.`lastAccessed` AS `lastAccessed`,`songs`.`id` AS `id`,_junction.`setlistId` FROM `setlist_song_cross_ref` AS _junction INNER JOIN `songs` ON (_junction.`songId` = `songs`.`id`) WHERE _junction.`setlistId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex ++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = 13; // _junction.setlistId;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfTitle = 0;
      final int _cursorIndexOfArtist = 1;
      final int _cursorIndexOfBpm = 2;
      final int _cursorIndexOfChordProContent = 3;
      final int _cursorIndexOfOriginalKey = 4;
      final int _cursorIndexOfDisplayKey = 5;
      final int _cursorIndexOfCapo = 6;
      final int _cursorIndexOfDurationSeconds = 7;
      final int _cursorIndexOfSyncMap = 8;
      final int _cursorIndexOfIsTrained = 9;
      final int _cursorIndexOfIsFavorite = 10;
      final int _cursorIndexOfLastAccessed = 11;
      final int _cursorIndexOfId = 12;
      while(_cursor.moveToNext()) {
        final long _tmpKey = _cursor.getLong(_itemKeyIndex);
        ArrayList<SongEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final SongEntity _item_1;
          final String _tmpTitle;
          if (_cursor.isNull(_cursorIndexOfTitle)) {
            _tmpTitle = null;
          } else {
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
          }
          final String _tmpArtist;
          if (_cursor.isNull(_cursorIndexOfArtist)) {
            _tmpArtist = null;
          } else {
            _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
          }
          final int _tmpBpm;
          _tmpBpm = _cursor.getInt(_cursorIndexOfBpm);
          final String _tmpChordProContent;
          if (_cursor.isNull(_cursorIndexOfChordProContent)) {
            _tmpChordProContent = null;
          } else {
            _tmpChordProContent = _cursor.getString(_cursorIndexOfChordProContent);
          }
          final String _tmpOriginalKey;
          if (_cursor.isNull(_cursorIndexOfOriginalKey)) {
            _tmpOriginalKey = null;
          } else {
            _tmpOriginalKey = _cursor.getString(_cursorIndexOfOriginalKey);
          }
          final String _tmpDisplayKey;
          if (_cursor.isNull(_cursorIndexOfDisplayKey)) {
            _tmpDisplayKey = null;
          } else {
            _tmpDisplayKey = _cursor.getString(_cursorIndexOfDisplayKey);
          }
          final int _tmpCapo;
          _tmpCapo = _cursor.getInt(_cursorIndexOfCapo);
          final int _tmpDurationSeconds;
          _tmpDurationSeconds = _cursor.getInt(_cursorIndexOfDurationSeconds);
          final String _tmpSyncMap;
          if (_cursor.isNull(_cursorIndexOfSyncMap)) {
            _tmpSyncMap = null;
          } else {
            _tmpSyncMap = _cursor.getString(_cursorIndexOfSyncMap);
          }
          final boolean _tmpIsTrained;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsTrained);
          _tmpIsTrained = _tmp != 0;
          final boolean _tmpIsFavorite;
          final int _tmp_1;
          _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
          _tmpIsFavorite = _tmp_1 != 0;
          final long _tmpLastAccessed;
          _tmpLastAccessed = _cursor.getLong(_cursorIndexOfLastAccessed);
          final int _tmpId;
          _tmpId = _cursor.getInt(_cursorIndexOfId);
          _item_1 = new SongEntity(_tmpTitle,_tmpArtist,_tmpBpm,_tmpChordProContent,_tmpOriginalKey,_tmpDisplayKey,_tmpCapo,_tmpDurationSeconds,_tmpSyncMap,_tmpIsTrained,_tmpIsFavorite,_tmpLastAccessed,_tmpId);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
