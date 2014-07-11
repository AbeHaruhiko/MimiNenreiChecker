package jp.caliconography.miminenreichecker.app;

import android.util.Log;

import com.socdm.d.adgeneration.ADGListener;

class AdListener extends ADGListener {
    private static final String _TAG = "ADGListener";

    @Override
    public void onReceiveAd() {
        Log.d(_TAG, "onReceiveAd");
    }

    @Override
    public void onFailedToReceiveAd() {
        Log.d(_TAG, "onFailedToReceiveAd");
    }

    @Override
    public void onInternalBrowserOpen() {
        Log.d(_TAG, "onInternalBrowserOpen");
    }

    @Override
    public void onInternalBrowserClose() {
        Log.d(_TAG, "onInternalBrowserClose");
    }

    @Override
    public void onVideoPlayerStart() {
        Log.d(_TAG, "onVideoPlayerStart");
    }

    @Override
    public void onVideoPlayerEnd() {
        Log.d(_TAG, "onVideoPlayerEnd");
    }
}