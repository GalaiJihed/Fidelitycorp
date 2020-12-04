package com.example.fidelitycorporation.helper;

import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

public class SingleUploadBroadcastReceiver extends UploadServiceBroadcastReceiver {
    public interface Delegate {
        void onProgress(int progress);
        void onProgress(long uploadedBytes, long totalBytes);
        void onError(Exception exception);
        void onCompleted(int serverResponseCode, byte[] serverResponseBody);
        void onCancelled();
    }
    private String mUploadID;
    private Delegate mDelegate;

    public void setUploadID(String uploadID) {
        mUploadID = uploadID;
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onProgress(String uploadId, int progress) {
        if (uploadId.equals(mUploadID) && mDelegate != null) {
            mDelegate.onProgress(progress);
        }
    }

    @Override
    public void onProgress(String uploadId, long uploadedBytes, long totalBytes) {
        if (uploadId.equals(mUploadID) && mDelegate != null) {
            mDelegate.onProgress(uploadedBytes, totalBytes);
        }
    }

    @Override
    public void onError(String uploadId, Exception exception) {
        if (uploadId.equals(mUploadID) && mDelegate != null) {
            mDelegate.onError(exception);
        }
    }

    @Override
    public void onCompleted(String uploadId, int serverResponseCode, byte[] serverResponseBody) {
        if (uploadId.equals(mUploadID) && mDelegate != null) {
            mDelegate.onCompleted(serverResponseCode, serverResponseBody);
        }
    }

    @Override
    public void onCancelled(String uploadId) {
        if (uploadId.equals(mUploadID) && mDelegate != null) {
            mDelegate.onCancelled();
        }
    }
}
