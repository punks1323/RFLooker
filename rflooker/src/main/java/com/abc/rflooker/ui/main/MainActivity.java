package com.abc.rflooker.ui.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import timber.log.Timber;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.abc.rflooker.R;
import com.abc.rflooker.background.jobs.FileTreeJob;
import com.abc.rflooker.data.DataManager;
import com.abc.rflooker.service.FileUploadIntentService;
import com.abc.rflooker.utils.AppLogger;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scheduleCloudSyncJob(this);

        //temp
        String filePath = "/0/Doc/SSC/pankaj signature.jpg";
        //FileUploadIntentService.handleActionFileUpload(this, filePath);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void scheduleCloudSyncJob(Context context) {
        int pushToCloudJobId = 292;
        JobScheduler mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (mJobScheduler == null) {
            Timber.e("mJobScheduler is null");
            return;
        }
        mJobScheduler.cancel(pushToCloudJobId);
        JobInfo.Builder builder = new JobInfo.Builder(pushToCloudJobId,
                new ComponentName(context.getPackageName(), FileTreeJob.class.getName()));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        builder.setPeriodic(60 * 1000 * 20);
        builder.setBackoffCriteria(60 * 1000, JobInfo.BACKOFF_POLICY_LINEAR);

        if (mJobScheduler.schedule(builder.build()) == JobScheduler.RESULT_SUCCESS) {
            AppLogger.d("Job scheduled!");
        } else {
            AppLogger.e("Job not scheduled");
        }
    }
}
