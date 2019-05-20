/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.abc.rflooker.data.remote;

import android.os.Environment;

import com.abc.rflooker.service.FileUploadIntentService;
import com.abc.rflooker.utils.AppLogger;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.rx2androidnetworking.Rx2ANRequest;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 07/07/17.
 */

@Singleton
public class ApiHelperImpl implements ApiHelper {

    @Inject
    public ApiHelperImpl() {
    }

    @Override
    public Single<String> doServerLogin(String email, String password) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_LOGIN)
                .addBodyParameter("username", email)
                .addBodyParameter("password", password)
                .setTag("login")
                .setPriority(Priority.HIGH)
                .build()
                .getStringSingle();
        //.getObjectSingle(String.class);
    }

    @Override
    public Single<String> updateToken(String token) {
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_UPDATE_TOKEN)
                .addBodyParameter("token", token)
                .setTag("sending_token")
                .setPriority(Priority.HIGH)
                .build()
                .getStringSingle();
    }

    @Override
    public Single<String> doFileUpload(String filePath, FileUploadIntentService.FileUploadListener fileUploadListener, boolean asynchronous) {
        //String imageFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + filePath;
        String path = Environment.getExternalStorageDirectory().getPath();
        String imageFilePath = path.substring(0, path.lastIndexOf('/')) + filePath;

        if (asynchronous) {
            AppLogger.d("Uploading asynchronously....");
            Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_UPLOAD)
                    .addMultipartFile("file", new File(imageFilePath))
                    .build()
                    .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                        // do anything with progress
                        AppLogger.i("onProgress :: " + bytesUploaded + "\t" + totalBytes);
                    })
                    .getStringObservable()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            AppLogger.w("onSubscribe");
                            fileUploadListener.onComplete();
                        }

                        @Override
                        public void onNext(String jsonObject) {
                            AppLogger.w("onNext");
                            //fileUploadListener.onComplete();
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppLogger.w("onError");
                            fileUploadListener.onComplete();
                        }

                        @Override
                        public void onComplete() {
                            AppLogger.w("onComplete file upload");
                            fileUploadListener.onComplete();
                        }
                    });
        } else {
            AppLogger.d("Uploading synchronously....");
            Rx2ANRequest request = Rx2AndroidNetworking.upload(ApiEndPoint.ENDPOINT_UPLOAD)
                    .addMultipartFile("file", new File(imageFilePath))
                    .build()
                    .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                        // do anything with progress
                        AppLogger.i("onProgress :: " + bytesUploaded + "\t" + totalBytes);
                    });

            ANResponse anResponse = request.executeForString();
            if (anResponse.isSuccess()) {
                AppLogger.w("Upload synchronous success..");
            } else {
                AppLogger.e("Upload synchronous failed..");
            }
            fileUploadListener.onComplete();

        }
        return null;
    }

    @Override
    public Single<String> sendFileTreeAndDeviceDetails(String fileTree, String deviceDetails) {
        //DeviceDetails.getDeviceDetails(this, new Gson())
        return Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_FILE_TREE)
                .addBodyParameter("fileTree", fileTree)
                .addBodyParameter("deviceDetails", deviceDetails)
                .setTag("fileTree_deviceDetails")
                .setPriority(Priority.HIGH)
                .build()
                .getStringSingle();
    }
}
