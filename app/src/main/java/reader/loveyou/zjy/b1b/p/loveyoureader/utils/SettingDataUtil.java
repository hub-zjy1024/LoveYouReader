package reader.loveyou.zjy.b1b.p.loveyoureader.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import reader.loveyou.zjy.b1b.p.loveyoureader.entity.Book;

/**
 Created by 张建宇 on 2019/1/22. */
public class SettingDataUtil {
    public static List<Book> readBooks(Context mContext) {
        List<Book> books = new ArrayList<>();
        SharedPreferences sp = mContext.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        String importedBooks = sp.getString("importedBooks", "[]");
        JSONArray jObj = null;
        try {
            jObj = new JSONArray(importedBooks);
            for (int i = jObj.length() - 1; i >= 0; i--) {
                JSONObject obj = jObj.getJSONObject(i);
                String savePath = obj.getString("filepath");
                Book mBook = new Book();
                mBook.filePath = savePath;
                books.add(mBook);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static void deletBook(Context mContext, File[] f) {
        SharedPreferences sp = mContext.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        String importedBooks = sp.getString("importedBooks", "[]");
        JSONArray jObj = null;
        try {
            jObj = new JSONArray(importedBooks);
            for (File tem : f) {
                for (int i = jObj.length() - 1; i >= 0; i--) {
                    JSONObject obj = jObj.getJSONObject(i);
                    String savePath = obj.getString("filepath");
                    if (savePath.equals(tem.getAbsolutePath())) {
                        jObj.remove(i);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jObj != null) {
            sp.edit().putString("importedBooks", jObj.toString()).apply();
        }
    }

    public static void clearAllBooks(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        sp.edit().putString("importedBooks", "[]").apply();
    }

    public static void saveBooks(Context mContext, File[] f) {
        SharedPreferences sp = mContext.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        String importedBooks = sp.getString("importedBooks", "[]");
        JSONArray jObj = null;
        try {
            jObj = new JSONArray(importedBooks);
            for (File tem : f) {
                JSONObject obj = new JSONObject();
                obj.put("filepath", tem.getAbsolutePath());
                jObj.put(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jObj != null) {
            sp.edit().putString("importedBooks", jObj.toString()).apply();
        }
    }

    public static void saveBook(Context mContext, File f) {
        saveBooks(mContext, new File[]{f});
    }

    public static boolean checkFileImported(Context mContext, File f) {
        SharedPreferences sp = mContext.getSharedPreferences("Setting", Context.MODE_PRIVATE);
        String importedBooks = sp.getString("importedBooks", "[]");
        try {
            JSONArray jObj = new JSONArray(importedBooks);
            for (int i = 0; i < jObj.length(); i++) {
                JSONObject obj = jObj.getJSONObject(i);
                String filepath = obj.getString("filepath");
                if (f.getAbsolutePath().equals(filepath)) {
                    return true;
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
