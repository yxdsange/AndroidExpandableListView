package com.it.androidexpandablelistview.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.it.androidexpandablelistview.bean.Chapter;
import com.it.androidexpandablelistview.bean.ChapterItem;

import java.util.ArrayList;
import java.util.List;

public class ChapterDao {
    public List<Chapter> loadFromDb(Context context){
        ChapterDbHelper dbHelper=ChapterDbHelper.getInstance(context);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        List<Chapter> chapterList=new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + Chapter.TABLE_NAME, null);
        Chapter chapter=null;
        while (cursor.moveToNext()){
            chapter=new Chapter();
            int id=cursor.getInt(cursor.getColumnIndex(chapter.COL_ID));
            String name =cursor.getString(cursor.getColumnIndex(Chapter.COL_NAME));
            chapter.setName(name);
            chapterList.add(chapter);

        }
        cursor.close();
        ChapterItem chapterItem=null;
        for(Chapter parent:chapterList){
            int pid=parent.getId();
            cursor=db.rawQuery("select * from "+ChapterItem.TABLE_NAME+" where "+ChapterItem.COL_PID+"=?",
                    new String[]{pid+""});

            while (cursor.moveToNext()){
                chapterItem=new ChapterItem();
                int id=cursor.getInt(cursor.getColumnIndex(chapterItem.COL_ID));
                String name =cursor.getString(cursor.getColumnIndex(ChapterItem.COL_NAME));
                chapterItem.setName(name);
                parent.addChild(chapterItem);
            }
            cursor.close();
        }
        return chapterList;
    }
    public void insert2Db(Context context,List<Chapter> chapterList){

        if (context== null){
            throw  new IllegalArgumentException("context can non be null");


        }

        if (chapterList ==null ||chapterList.isEmpty()){
            return;
        }
        ChapterDbHelper dbHelper=ChapterDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues=null;
        for (Chapter chapter:chapterList){
            contentValues =new ContentValues();
            contentValues.put(Chapter.COL_ID,chapter.getId());
            contentValues.put(Chapter.COL_NAME,chapter.getName());

            db.insertWithOnConflict(chapter.TABLE_NAME,null,
                    contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE); // 更新树操作

            List<ChapterItem> children=chapter.getChildren();
            for(ChapterItem chapterItem:children){
                contentValues =new ContentValues();
                contentValues.put(ChapterItem.COL_ID,chapterItem.getId());
                contentValues.put(ChapterItem.COL_NAME,chapterItem.getName());
                contentValues.put(ChapterItem.COL_PID,chapterItem.getPid());
                db.insertWithOnConflict(ChapterItem.TABLE_NAME,
                        null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
            }

        }

        db.setTransactionSuccessful();
        db.endTransaction();


    }
}
