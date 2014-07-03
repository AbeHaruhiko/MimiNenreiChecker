package jp.caliconography.miminenreichecker.app;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.FontAwesome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import jp.caliconography.android.miminenreichecker.app.R;
import jp.caliconography.android.widget.CustomFontButtonWithRightIcon;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    private final static String TAG = PlaceholderFragment.class.getSimpleName();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "1";

    private AudioTrack mAudioTrack;
    private CustomFontButtonWithRightIcon mBtnLv1;
    private CustomFontButtonWithRightIcon mBtnLv2;
    private CustomFontButtonWithRightIcon mBtnLv3;
    private CustomFontButtonWithRightIcon mBtnLv4;
    private CustomFontButtonWithRightIcon mBtnLv5;
    private CustomFontButtonWithRightIcon mBtnLv6;
    private ArrayList<CustomFontButtonWithRightIcon> mButtonList = new ArrayList<CustomFontButtonWithRightIcon>();
    private Timer timer = new Timer();
    private Handler handler = new Handler();
    private SinWaveGenerator mCurrentWaveGenerator;
    private SinWaveGenerator sinWaveGenerator1;
    private SinWaveGenerator sinWaveGenerator2;
    private SinWaveGenerator sinWaveGenerator3;
    private SinWaveGenerator sinWaveGenerator4;
    private SinWaveGenerator sinWaveGenerator5;
    private SinWaveGenerator sinWaveGenerator6;
    private HashMap<View, SinWaveGenerator> mBtnGenMap = new HashMap<View, SinWaveGenerator>();
    private short[] mSoundBuffer;
    private ForceStopTimer mForceStopTimer;
    private Map<String, String> mFaMap = FontAwesome.getFaMap();
    private BackgroundThread mBackgroundRunnable;

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //バッファーサイズの取得
        int sampleRate = 44100;
        int soundBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        //AudioTrackの初期化
        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                //サンプリング定数
                sampleRate,
                //モノラル
                AudioFormat.CHANNEL_OUT_MONO,
                //16bit
                AudioFormat.ENCODING_PCM_16BIT,
                //バッファーサイズ
                soundBufferSize,
                //ストリームモード
                AudioTrack.MODE_STREAM);

        mSoundBuffer = new short[soundBufferSize];
        Log.d("", String.valueOf(soundBufferSize));
        //所得したバッファーサイズごとに通知させる。
        mAudioTrack.setPositionNotificationPeriod(soundBufferSize);
        mAudioTrack.setPlaybackPositionUpdateListener(
                new AudioTrack.OnPlaybackPositionUpdateListener() {
                    public void onMarkerReached(AudioTrack track) {
                    }

                    //通知があるごとに実行される。
                    public void onPeriodicNotification(AudioTrack track) {
//                            Log.d(TAG, "notified");
                        writeSound();
                    }
                }
        );
        sinWaveGenerator1 = new SinWaveGenerator(440, 1, sampleRate);
        sinWaveGenerator2 = new SinWaveGenerator(880, 1, sampleRate);
        sinWaveGenerator3 = new SinWaveGenerator(16000, 1, sampleRate);
        sinWaveGenerator4 = new SinWaveGenerator(18000, 1, sampleRate);
        sinWaveGenerator5 = new SinWaveGenerator(20000, 1, sampleRate);
        sinWaveGenerator6 = new SinWaveGenerator(21000, 1, sampleRate);


        mBtnLv1 = (CustomFontButtonWithRightIcon) rootView.findViewById(R.id.btn_lv1);
        mBtnLv2 = (CustomFontButtonWithRightIcon) rootView.findViewById(R.id.btn_lv2);
        mBtnLv3 = (CustomFontButtonWithRightIcon) rootView.findViewById(R.id.btn_lv3);
        mBtnLv4 = (CustomFontButtonWithRightIcon) rootView.findViewById(R.id.btn_lv4);
        mBtnLv5 = (CustomFontButtonWithRightIcon) rootView.findViewById(R.id.btn_lv5);
        mBtnLv6 = (CustomFontButtonWithRightIcon) rootView.findViewById(R.id.btn_lv6);
        mButtonList.add(mBtnLv1);
        mButtonList.add(mBtnLv2);
        mButtonList.add(mBtnLv3);
        mButtonList.add(mBtnLv4);
        mButtonList.add(mBtnLv5);
        mButtonList.add(mBtnLv6);
        mBtnGenMap.put(mBtnLv1, sinWaveGenerator1);
        mBtnGenMap.put(mBtnLv2, sinWaveGenerator2);
        mBtnGenMap.put(mBtnLv3, sinWaveGenerator3);
        mBtnGenMap.put(mBtnLv4, sinWaveGenerator4);
        mBtnGenMap.put(mBtnLv5, sinWaveGenerator5);
        mBtnGenMap.put(mBtnLv6, sinWaveGenerator6);

        View.OnClickListener onLvBtnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                doOnClick(view);
            }
        };
        mBtnLv1.setOnClickListener(onLvBtnClickListener);
        mBtnLv2.setOnClickListener(onLvBtnClickListener);
        mBtnLv3.setOnClickListener(onLvBtnClickListener);
        mBtnLv4.setOnClickListener(onLvBtnClickListener);
        mBtnLv5.setOnClickListener(onLvBtnClickListener);
        mBtnLv6.setOnClickListener(onLvBtnClickListener);

        return rootView;
    }

    private void doOnClick(final View view) {
        Log.d(TAG, "1");
        if (mAudioTrack != null) {
            final CustomFontButtonWithRightIcon clickedButton = ((CustomFontButtonWithRightIcon) view);
            if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_STOPPED) {
                Log.d(TAG, "2");

                mCurrentWaveGenerator = view == mBtnLv1 ? sinWaveGenerator1
                        : view == mBtnLv2 ? sinWaveGenerator2
                        : view == mBtnLv3 ? sinWaveGenerator3
                        : view == mBtnLv4 ? sinWaveGenerator4
                        : view == mBtnLv5 ? sinWaveGenerator5
                        : sinWaveGenerator6;
                Log.d(TAG, "3");

                //ボタンが押されたら再生する。
                mAudioTrack.setStereoVolume(0, 0);
                //AudioTrack.playの後はgetMinBufferSizeで取得した
                //サイズより小さいとonPeriodicNotificationが実行されない。
                mAudioTrack.play();
                if (mForceStopTimer != null) mForceStopTimer.cancel();
                mForceStopTimer = new ForceStopTimer();
                timer.schedule(mForceStopTimer, 10000);

                mBackgroundRunnable = new BackgroundThread(clickedButton);
                new Thread(mBackgroundRunnable).start();
                Log.d(TAG, "4");

                writeSound();
                mAudioTrack.setStereoVolume(1, 1);
                Log.d(TAG, "5");

                for (CustomFontButtonWithRightIcon item : mButtonList) {
                    Log.d(TAG, "6");
                    if (item == clickedButton) {
//                            item.setText(item.getText().toString().replace(FontAwesome.getFaMap().get("fa-play"), FontAwesome.getFaMap().get("fa-pause")));
                    } else {
                        item.setRightIcon(mFaMap.get("fa-play"));
                    }
                }
                Log.d(TAG, "7");
            } else if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                Log.d(TAG, "8");

                //ボタンが押されたら停止
                mBackgroundRunnable.running = false;
                mAudioTrack.setStereoVolume(0, 0);
                mAudioTrack.stop();
                Log.d(TAG, "9");

                for (CustomFontButtonWithRightIcon item : mButtonList) {
                    Log.d(TAG, "10");
                    item.setRightIcon(mFaMap.get("fa-play"));
                }
                Log.d(TAG, "11");

                if (mBtnGenMap.get(clickedButton) == mCurrentWaveGenerator) {
                    Log.d(TAG, "12");
                    // 再生中のLvのボタンが押された
//                                mCurrentWaveGenerator = null;
                } else {
                    Log.d(TAG, "13");
                    doOnClick(view);
                }
                Log.d(TAG, "14");
            }
            Log.d(TAG, "15");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 再生中だったら停止してリリース
        if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            mAudioTrack.stop();
            mAudioTrack.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    void writeSound() {
        for (int i = 0; i < mSoundBuffer.length; i++) {
            mSoundBuffer[i] = (short) (Short.MAX_VALUE * mCurrentWaveGenerator.generateSinWave());
        }
        mAudioTrack.write(mSoundBuffer, 0, mSoundBuffer.length);
    }

    class BackgroundThread implements Runnable {

        protected boolean running = true;
        private CustomFontButtonWithRightIcon mClickedButton;

        public BackgroundThread(CustomFontButtonWithRightIcon button) {
            this.mClickedButton = button;
        }

        @Override
        public void run() {
            while (running) {
                // スリープ処理をmHandler.postの外でやってみる
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
                handler.post(new Runnable() {
                    public void run() {
                        String now = mClickedButton.getRightIcon();
                        String next = mFaMap.get("fa-volume-up");
                        if (now.equals(mFaMap.get("fa-volume-off"))) {
                            next = mFaMap.get("fa-volume-down");
                        } else if (now.contains(mFaMap.get("fa-volume-down"))) {
                            next = mFaMap.get("fa-volume-up");
                        } else if (now.contains(mFaMap.get("fa-volume-up"))) {
                            next = mFaMap.get("fa-volume-off");
                        } else {
                            next = mFaMap.get("fa-volume-off");
                        }
                        mClickedButton.setRightIcon(next);
                    }
                });
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mClickedButton.setRightIcon(mFaMap.get("fa-play"));
                }
            });
        }
    }

    class ForceStopTimer extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                        mAudioTrack.setStereoVolume(0, 0);
                        mAudioTrack.stop();
                    }
                    mBackgroundRunnable.running = false;
                    for (CustomFontButtonWithRightIcon item : mButtonList) {
                        item.setRightIcon(mFaMap.get("fa-play"));
                    }

                }
            });
        }
    }
}
