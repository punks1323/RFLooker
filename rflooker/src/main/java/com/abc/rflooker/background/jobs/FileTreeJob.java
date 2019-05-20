package com.abc.rflooker.background.jobs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.abc.rflooker.data.DataManager;
import com.abc.rflooker.ui.main.MainActivity;
import com.abc.rflooker.utils.AppLogger;
import com.abc.rflooker.utils.DeviceDetails;
import com.abc.rflooker.utils.FileTreeUtils;
import com.abc.rflooker.utils.rx.SchedulerProvider;
import com.androidnetworking.error.ANError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FileTreeJob extends JobService {

    @Inject
    DataManager dataManager;

    @Inject
    SchedulerProvider schedulerProvider;

    @Inject
    Gson gson;
    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        this.jobParameters = jobParameters;
        AndroidInjection.inject(this);
        AppLogger.i("Job started!");
        performJob();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }


    private void performJob() {
        Observable.just(0)
                .doOnNext(c -> {
                    saveToFile1();
                    AppLogger.w("File TIME update task completed");
                    sendFileTree();
                    AppLogger.w("FILE_TREE task completed");
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    tokenUpdate();
                });
    }

    private void sendFileTree() {
        String fileTree = FileTreeUtils.getFileTree();
        @SuppressLint("MissingPermission") String deviceDetails = DeviceDetails.getDeviceDetails(this, gson);
        dataManager.sendFileTreeAndDeviceDetails(fileTree, deviceDetails)
                /*.subscribeOn(schedulerProvider.newThread())
                .observeOn(schedulerProvider.ui())*/
                .subscribe(response -> {
                    AppLogger.d("sendFileTree response OK " + response);
                }, throwable -> {
                    AppLogger.d("sendFileTree response ERROR" + throwable);
                });
    }

    private void saveToFile1() {
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

    private void tokenUpdate() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            AppLogger.w("getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        sendTokenToServer(token);

                        AppLogger.w("TOKEN task completed");
                        AppLogger.i("Job task completed ");
                        jobFinished(jobParameters, false);
                    }

                    private void sendTokenToServer(String token) {
                        dataManager.updateToken(token)
                                /*.doOnSuccess(response -> AppLogger.i("Login response :: " + response))
                                .doOnError(onError -> AppLogger.e("Login error :: " + onError.toString()))*/
                                .subscribeOn(schedulerProvider.newThread())
                                .observeOn(schedulerProvider.ui())
                                .subscribe(response -> {
                                    AppLogger.d("Token sent response OK " + response);
                                }, throwable -> {
                                    AppLogger.d("Token sent response ERROR" + throwable);
                                });
                    }
                });
    }
}
