package com.example.sharepreferences1;

public  interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
