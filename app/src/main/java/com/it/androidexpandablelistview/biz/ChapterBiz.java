package com.it.androidexpandablelistview.biz;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.it.androidexpandablelistview.bean.Chapter;
import com.it.androidexpandablelistview.bean.ChapterItem;
import com.it.androidexpandablelistview.dao.ChapterDao;
import com.it.androidexpandablelistview.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChapterBiz {
    private ChapterDao mChapterDao=new ChapterDao();
    public void loadDatas(final Context context, final CallBack callBack, final boolean useCache) {

        AsyncTask<Boolean, Void, List<Chapter>> asyncTask =
//                new AsyncTask<Boolean, Void, List<Chapter>>() {
                  new AsyncTask<Boolean, Void, List<Chapter>>() {

            private Exception ex;
            @Override
            protected void onPostExecute(List<Chapter> chapters) {
                if (ex != null) {
                    callBack.onFailed(ex);
                } else {
                    callBack.onSuccess(chapters);
                }
            }
            @Override
            protected List<Chapter> doInBackground(Boolean... booleans) {
                boolean isUseCache = booleans[0];
               final List<Chapter> chapters = new ArrayList<>();
                try {
//                    从缓存中读取
                    if (booleans[0]){
                        chapters.addAll(mChapterDao.loadFromDb(context));
                    }
                    if (isUseCache) {
                        //                    characterList.addAll()
                        List<Chapter> chapterListFromDb= mChapterDao.loadFromDb(context);
                        Log.e("sange","ChapterListFromDb="+chapterListFromDb);
                        chapters.addAll(chapterListFromDb);
                    }
//              load from net
                    if (chapters.isEmpty()) {
                        //              load from net
                        //                    todo cache do  db
                        final List<Chapter> chaptersListFromNet=loadFromNet(context);
                        mChapterDao.insertToDb(context,chaptersListFromNet);
                        chapters.addAll(chaptersListFromNet);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    ex=e;  // 发生异常给ex赋值
                }
                return chapters;


            }


        };
        asyncTask.execute(useCache);

    }

    private List<Chapter> loadFromNet(Context context) {
        final String url="http://www.imooc.com/api/expandablelistview";
//        1.发请求，获取String数据
        String content= HttpUtils.doGet(url);
        final List<Chapter> chapterList=new ArrayList<>();
        Log.e("sange","=====null;===="+content);
        if (content != null){
//        2.content -> List<Chapter>
//            chapterList=parseContent(content);
            mChapterDao.insertToDb(context,chapterList);
            Log.e("sange","==parse finish chapterlist=="+chapterList);
        }
        return chapterList;
    }

    private List<Chapter> parseContent(String content) {
        List<Chapter> chapterList=new ArrayList<>();
        try {
            JSONObject root=new JSONObject(content);
            int errorCode=root.optInt("errorCode");
            if (errorCode ==0){
                JSONArray dataJsonArray=root.optJSONArray("data");
                for(int i=0;i<dataJsonArray.length();i++){
//                          chapter
                    JSONObject chapterJsonObj=dataJsonArray.getJSONObject(i);
                    int id=chapterJsonObj.optInt("id");
                    String name=chapterJsonObj.optString("name");
                    Chapter chapter=new Chapter(id,name);
                    chapterList.add(chapter);
//                    parse chapter items
                    JSONArray childrenJsonArr=chapterJsonObj.optJSONArray("children");
                    if (childrenJsonArr !=null){
                        for(int j=0;j<childrenJsonArr.length();j++){
                            JSONObject chapterItemJsonObj= childrenJsonArr.getJSONObject(j);
                            int  cid=chapterItemJsonObj.optInt("id");
                            String cname=chapterItemJsonObj.optString(name);

//                      生成      ChapterItem 对象
                        ChapterItem chapterItem=new ChapterItem(cid,cname);
                        chapter.addChild(chapterItem);

                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static interface CallBack {
        void onSuccess(List<Chapter> chapterList);

        void onFailed(Exception ex);
    }


}


