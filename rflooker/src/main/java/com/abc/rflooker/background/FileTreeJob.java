package com.abc.rflooker.background;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.os.Environment;

import com.abc.rflooker.utils.AppLogger;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FileTreeJob extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        AppLogger.i("Job started!");
        performJob(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }


    private void performJob(JobParameters jobParameters) {
        Observable.just(0)
                .doOnNext(c -> saveToFile1())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    jobFinished(jobParameters, false);
                });
    }

    private static void saveToFile1() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "RFFileTreeJob.txt");
        File errFile = new File(Environment.getExternalStorageDirectory() + File.separator + "RFFileTreeJob.txt");
        try {
            String fileStr;
            if (file.exists())
                fileStr = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            else
                fileStr = null;
            boolean isEmpty = false;
            if (fileStr == null || fileStr.isEmpty()) {
                isEmpty = true;
            }
            String time = "time";
            String diff = "diff";
            JSONArray jsonArray;
            if (isEmpty) {
                jsonArray = new JSONArray();
                JSONObject firstObject = new JSONObject();
                firstObject.put(diff, "");
                firstObject.put(time, sdf.format(new Date()));
                jsonArray.put(firstObject);
            } else {
                jsonArray = new JSONArray(fileStr);
                JSONObject lastObject = jsonArray.optJSONObject(jsonArray.length() - 1);
                Date lastDate = sdf.parse(lastObject.optString(time));
                Date presentDate = new Date();
                long diffLong = presentDate.getTime() - lastDate.getTime();
                long diffMinutes = diffLong / (60 * 1000) % 60;

                JSONObject newJsonObject = new JSONObject();
                newJsonObject.put(diff, diffMinutes + " min");
                newJsonObject.put(time, sdf.format(presentDate));
                jsonArray.put(newJsonObject);
            }

            FileUtils.writeStringToFile(file, jsonArray.toString(), StandardCharsets.UTF_8, false);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                FileUtils.writeStringToFile(errFile, new Date().toString() + "\n", StandardCharsets.UTF_8, true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
