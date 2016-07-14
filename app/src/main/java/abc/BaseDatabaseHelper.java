package abc;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import info.mixun.baseframework.database.FrameDatabaseHelper;

/**
 * Created by Administrator on 2016/7/2.
 */
public class BaseDatabaseHelper extends SQLiteOpenHelper {

    private String password = "mx3cy61wl*1";
    protected static FrameDatabaseHelper frameDatabaseHelper;
    protected static SQLiteDatabase writerInstance;
    protected static SQLiteDatabase readerInstance;
    private int oldVersion = 0;

    public BaseDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        writerInstance = this.getWritableDatabase(this.getPassword());
        readerInstance = this.getReadableDatabase(this.getPassword());
    }

    public static SQLiteDatabase getWriterInstance() {
        return writerInstance;
    }

    public static SQLiteDatabase getReaderInstance() {
        return readerInstance;
    }

    public static SQLiteDatabase resetDatabase() {
        if (writerInstance.isOpen()) {
            writerInstance.close();
        }

        if (readerInstance.isOpen()) {
            readerInstance.close();
        }

        writerInstance = frameDatabaseHelper.getWritableDatabase(frameDatabaseHelper.getPassword());
        readerInstance = frameDatabaseHelper.getReadableDatabase(frameDatabaseHelper.getPassword());
        return writerInstance;
    }

    public static void closeDB() {
        if (frameDatabaseHelper != null) {
            writerInstance.close();
            readerInstance.close();
            frameDatabaseHelper.close();
            writerInstance = null;
            readerInstance = null;
            frameDatabaseHelper = null;
        }

    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        this.oldVersion = newVersion;
    }
}
