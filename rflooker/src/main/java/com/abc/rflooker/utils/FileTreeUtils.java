package com.abc.rflooker.utils;

import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class FileTreeUtils {
    public static String getFileTree() {
        String json = null;

        File dir = new File(Environment.getExternalStorageDirectory(), "/RFLooker");

        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (mkdirs)
                AppLogger.i("RFLooker root dir is created");
        }

        JSONArray dirJSONArr = new JSONArray();
        createJSONTree(dir, dirJSONArr);
        json = dirJSONArr.toString();
        return json;
    }

    private static JSONArray createJSONTree(File rootFile, JSONArray fileJSONArray) {
        try {

            boolean isFile = rootFile.isFile();
            String name = rootFile.getName();

            if (isFile) {
                JSONObject fileJSONObj = new JSONObject();
                fileJSONObj.put("type", "F");
                fileJSONObj.put("size", rootFile.length());
                fileJSONObj.put("name", name);
                fileJSONArray.put(fileJSONObj);
            } else {
                JSONObject dirJSONObject = new JSONObject();
                dirJSONObject.put("type", "D");
                JSONArray dirJSONArr = new JSONArray();
                dirJSONObject.put(name, dirJSONArr);
                fileJSONArray.put(dirJSONObject);

                File[] files = rootFile.listFiles();

                if (files == null) {
                    AppLogger.e("Access Denied: " + rootFile.getName());
                } else
                    for (int i = 0; i < files.length; i++) {
                        createJSONTree(files[i], dirJSONArr);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileJSONArray;
    }
}
