package es.uam.oscar_garcia.othello.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.util.Log;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.UUID;

import es.uam.oscar_garcia.othello.R;
import es.uam.oscar_garcia.othello.model.Round;
import es.uam.oscar_garcia.othello.model.RoundRepository;
import es.uam.oscar_garcia.othello.database.RoundDataBaseSchema.*;
import es.uam.oscar_garcia.othello.model.RoundRepositoryFactory;

/**
 * Created by oscar on 22/03/17.
 */

public class OthelloDataBase implements RoundRepository {
    private final String DEBUG_TAG = "DEBUG";
    private static final String DATABASE_NAME = "othello.db";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public OthelloDataBase(Context context) {
        helper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }


        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            String str1 = "DROP TABLE IF EXISTS"+UserTable.NAME;
            String str2 ="DROP TABLE IF EXISTS"+RoundTable.NAME;
            try {
                db.execSQL(str1);
                db.execSQL(str2);
                createTable(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        private void createTable(SQLiteDatabase db) {
            String str1 = "CREATE TABLE " + UserTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + UserTable.Cols.PLAYERUUID + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERNAME + " TEXT UNIQUE, "
                    + UserTable.Cols.PLAYERPASSWORD + " TEXT);";
            String str2 = "CREATE TABLE " + RoundTable.NAME + " ("
                    + "_id integer primary key autoincrement, "
                    + RoundTable.Cols.ROUNDUUID + " TEXT UNIQUE, "
                    + RoundTable.Cols.PLAYERUUID + " TEXT REFERENCES "+UserTable.Cols.PLAYERUUID + ", "
                    + RoundTable.Cols.DATE + " TEXT, "
                    + RoundTable.Cols.TITLE + " TEXT, "
                    + RoundTable.Cols.BOARD + " TEXT);";
            try {
                db.execSQL(str1);
                db.execSQL(str2);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
        @Override
        public void open() throws SQLException {db = helper.getWritableDatabase();}

        public void close() { db.close();}

        private static String byteToHex(final byte[] hash)
        {
            Formatter formatter = new Formatter();
            for (byte b : hash)
            {
                formatter.format("%02x", b);
            }
            String result = formatter.toString();
            formatter.close();
            return result;
        }

        private static String aplySHA1(String pss)
        {
            MessageDigest crypt = null;
            try {
            crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(pss.getBytes("UTF-8"));
            return byteToHex(crypt.digest());
            } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
                return "";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
        @Override
        public void login(String playername, String playerpassword,LoginRegisterCallback callback) {
            Log.d(DEBUG_TAG, "Login " + playername);

            String sha1=aplySHA1(playerpassword);

            Cursor cursor = db.query(UserTable.NAME, new String[]{UserTable.Cols.PLAYERUUID},
                    UserTable.Cols.PLAYERNAME + " = ? AND " + UserTable.Cols.PLAYERPASSWORD + " = ?",
                    new String[]{playername, sha1}, null, null, null);
            int count = cursor.getCount();
            String uuid = count == 1 && cursor.moveToFirst() ? cursor.getString(0) : "";
            cursor.close();

            if (count == 1)
             callback.onLogin(uuid);
            else
             callback.onError("Username or password incorrect.");
        }

        @Override
        public void register(String playername, String password,LoginRegisterCallback callback) {
            ContentValues values = new ContentValues();
            String uuid = UUID.randomUUID().toString();
            values.put(UserTable.Cols.PLAYERUUID, uuid);
            values.put(UserTable.Cols.PLAYERNAME, playername);
            values.put(UserTable.Cols.PLAYERPASSWORD, aplySHA1(password));
            long id = db.insert(UserTable.NAME, null, values);
            if (id < 0)
                callback.onError("Error inserting new player named " + playername);
            else
                callback.onLogin(uuid);

        }

        private ContentValues getContentValues(Round round) {
            ContentValues values = new ContentValues();
            values.put(RoundTable.Cols.PLAYERUUID, round.getPlayerUUID().toString());
            values.put(RoundTable.Cols.BOARD,round.getBoard().tableroToString());
            values.put(RoundTable.Cols.DATE,round.getDate());
            values.put(RoundTable.Cols.ROUNDUUID,round.getId());
            return values;

        }

        @Override
        public void addRound(Round round, BooleanCallback callback) {
            ContentValues values = getContentValues(round);
            long id = db.insert(RoundTable.NAME, null, values);
            if (callback != null)
                callback.onResponse(id >= 0);
        }

        @Override
        public void updateRound(Round round, BooleanCallback callback) {
            ContentValues values = new ContentValues();
            values.put(RoundTable.Cols.BOARD,round.getBoard().tableroToString());
            long id = db.update(RoundTable.NAME,values,RoundTable.Cols.ROUNDUUID+"="+round.getId(),null);
            if (callback != null)
                callback.onResponse(id >= 0);
        }

    private RoundCursorWrapper queryRounds() {
        String sql = "SELECT " + UserTable.Cols.PLAYERNAME + ", " +
                UserTable.Cols.PLAYERUUID + ", " +
                RoundTable.Cols.ROUNDUUID + ", " +
                RoundTable.Cols.DATE + ", " +
                RoundTable.Cols.TITLE + ", " +
                RoundTable.Cols.BOARD + " " +
                "FROM " + UserTable.NAME + " AS p, " +
                RoundTable.NAME + " AS r " +
                "WHERE " + "p." + UserTable.Cols.PLAYERUUID + "=" +
                "r." + RoundTable.Cols.PLAYERUUID + ";";
        Cursor cursor = db.rawQuery(sql, null);
        return new RoundCursorWrapper(cursor);
    }

    @Override
    public void getRounds(String playeruuid, String orderByField, String group,
                          RoundRepository.RoundsCallback callback) {
        List<Round> rounds = new ArrayList<>();
        RoundCursorWrapper cursor = queryRounds();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Round round = cursor.getRound();
            if (round.getPlayerUUID().equals(playeruuid))
                rounds.add(round);
            cursor.moveToNext();
        }
        cursor.close();
        if (cursor.getCount() > 0)
            callback.onResponse(rounds);
        else
            callback.onError("No rounds found in database");
    }
    /*private void updateRound() {
        Round round = createRound();
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        RoundRepository.BooleanCallback callback = new RoundRepository.BooleanCallback() {
            @Override
            public void onResponse(boolean response) {
                if (response == false)
                    Snackbar.make(getView(), R.string.error_updating_round,
                            Snackbar.LENGTH_LONG).show();
            }
        };
        repository.updateRound(round, callback);
    }*/
}
