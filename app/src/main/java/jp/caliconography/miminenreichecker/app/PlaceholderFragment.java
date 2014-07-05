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
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import jp.caliconography.android.miminenreichecker.app.R;
import jp.caliconography.android.widget.CustomFontButton;
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
    private static final String ARG_SECTION_NUMBER = "section_number";

    private AudioTrack mAudioTrack;
    private CustomFontButtonWithRightIcon mBtnLv1;
    private CustomFontButtonWithRightIcon mBtnLv2;
    private CustomFontButtonWithRightIcon mBtnLv3;
    private CustomFontButtonWithRightIcon mBtnLv4;
    private CustomFontButtonWithRightIcon mBtnLv5;
    private CustomFontButtonWithRightIcon mBtnLv6;
    private ArrayList<CustomFontButtonWithRightIcon> mButtonList = new ArrayList<CustomFontButtonWithRightIcon>();
    private Timer mForceStopTimer = new Timer();
    private ForceStopTimerTask mForceStopTimerTask;
    private Handler mHandler = new Handler();
    private SinWaveGenerator mCurrentWaveGenerator;
    private SinWaveGenerator mSinWaveGenerator1;
    private SinWaveGenerator mSinWaveGenerator2;
    private SinWaveGenerator mSinWaveGenerator3;
    private SinWaveGenerator mSinWaveGenerator4;
    private SinWaveGenerator mSinWaveGenerator5;
    private SinWaveGenerator mSinWaveGenerator6;
    private HashMap<View, SinWaveGenerator> mBtnGenMap = new HashMap<View, SinWaveGenerator>();
    private short[] mSoundBuffer;
    private Map<String, String> mFaMap = FontAwesome.getFaMap();
    private RunnnableForUpdateRightIcon mRunnableForUpdateRightIcon;
    private RunnnableForRandomPlay mRunnnableForRandomPlay;

    private TextView mLblDiagDesc;
    private CustomFontButton mBtnStartDiag;
    private CustomFontButton mBtnStopDiag;
    private CustomFontButton mBtnGotIt;
    private ScheduledFuture<?> mScheduledFuture;
    private ScheduledExecutorService mScheduledExecutor;

    public PlaceholderFragment() {
        /**
         * ここには何も書かない（書いてはいけない）
         */
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
        int sampleRate = 44100;
        View rootView = null;

        mSinWaveGenerator1 = new SinWaveGenerator(440, 1, sampleRate);
        mSinWaveGenerator2 = new SinWaveGenerator(880, 1, sampleRate);
        mSinWaveGenerator3 = new SinWaveGenerator(1600, 1, sampleRate);
        mSinWaveGenerator4 = new SinWaveGenerator(1800, 1, sampleRate);
        mSinWaveGenerator5 = new SinWaveGenerator(2000, 1, sampleRate);
        mSinWaveGenerator6 = new SinWaveGenerator(2100, 1, sampleRate);

        switch (this.getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
                rootView = inflater.inflate(R.layout.fragment_main, container, false);

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

                mBtnGenMap.put(mBtnLv1, mSinWaveGenerator1);
                mBtnGenMap.put(mBtnLv2, mSinWaveGenerator2);
                mBtnGenMap.put(mBtnLv3, mSinWaveGenerator3);
                mBtnGenMap.put(mBtnLv4, mSinWaveGenerator4);
                mBtnGenMap.put(mBtnLv5, mSinWaveGenerator5);
                mBtnGenMap.put(mBtnLv6, mSinWaveGenerator6);

                View.OnClickListener onLvBtnClickListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        doOnLvBtnClick(view);
                    }
                };
                mBtnLv1.setOnClickListener(onLvBtnClickListener);
                mBtnLv2.setOnClickListener(onLvBtnClickListener);
                mBtnLv3.setOnClickListener(onLvBtnClickListener);
                mBtnLv4.setOnClickListener(onLvBtnClickListener);
                mBtnLv5.setOnClickListener(onLvBtnClickListener);
                mBtnLv6.setOnClickListener(onLvBtnClickListener);

                break;
            case 2:
                rootView = inflater.inflate(R.layout.fragment_diagnosis, container, false);

                mLblDiagDesc = (TextView) rootView.findViewById(R.id.lbl_diag_desc);
                mBtnStartDiag = (CustomFontButton) rootView.findViewById(R.id.btn_start_diag);
                mBtnStopDiag = (CustomFontButton) rootView.findViewById(R.id.btn_stop);
                mBtnGotIt = (CustomFontButton) rootView.findViewById(R.id.btn_got_it);

                View.OnClickListener onStartDiagBtnClickListener = new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        doOnDiagBtnClick(view);
                    }
                };
                mBtnStartDiag.setOnClickListener(onStartDiagBtnClickListener);
                mBtnStopDiag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mRunnnableForRandomPlay != null)
                            mRunnnableForRandomPlay.running = false;
                        if (mScheduledFuture != null) mScheduledFuture.cancel(true);
                        if (mScheduledExecutor != null) mScheduledExecutor.shutdown();
                    }
                });
                mBtnGotIt.setOnClickListener(onStartDiagBtnClickListener);

                break;
        }

        //バッファーサイズの取得
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

        return rootView;
    }

    private void doOnLvBtnClick(final View view) {

        if (mAudioTrack != null) {
            final CustomFontButtonWithRightIcon clickedButton = ((CustomFontButtonWithRightIcon) view);
            if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_STOPPED) {

                mCurrentWaveGenerator = view == mBtnLv1 ? mSinWaveGenerator1
                        : view == mBtnLv2 ? mSinWaveGenerator2
                        : view == mBtnLv3 ? mSinWaveGenerator3
                        : view == mBtnLv4 ? mSinWaveGenerator4
                        : view == mBtnLv5 ? mSinWaveGenerator5
                        : mSinWaveGenerator6;

                //ボタンが押されたら再生する。
                mAudioTrack.setStereoVolume(0, 0);
                //AudioTrack.playの後はgetMinBufferSizeで取得した
                //サイズより小さいとonPeriodicNotificationが実行されない。
                mAudioTrack.play();
                if (mForceStopTimerTask != null) mForceStopTimerTask.cancel();
                mForceStopTimerTask = new ForceStopTimerTask();
//                mForceStopTimer.schedule(mForceStopTimerTask, 10000);
                mHandler.postDelayed(mForceStopTimerTask, 10000);

                mRunnableForUpdateRightIcon = new RunnnableForUpdateRightIcon(clickedButton);
                new Thread(mRunnableForUpdateRightIcon).start();

                writeSound();
                mAudioTrack.setStereoVolume(1, 1);

                for (CustomFontButtonWithRightIcon item : mButtonList) {

                    if (item == clickedButton) {
//                            item.setText(item.getText().toString().replace(FontAwesome.getFaMap().get("fa-play"), FontAwesome.getFaMap().get("fa-pause")));
                    } else {
                        item.setRightIcon(mFaMap.get("fa-play"));
                    }
                }
            } else if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                //ボタンが押されたら停止
                mRunnableForUpdateRightIcon.running = false;
                mAudioTrack.setStereoVolume(0, 0);
                mAudioTrack.stop();

                for (CustomFontButtonWithRightIcon item : mButtonList) {
                    item.setRightIcon(mFaMap.get("fa-play"));
                }

                if (mBtnGenMap.get(clickedButton) == mCurrentWaveGenerator) {
                    // 再生中のLvのボタンが押された
//                                mCurrentWaveGenerator = null;
                } else {
                    doOnLvBtnClick(view);
                }
            }
        }
    }

    private void doOnDiagBtnClick(final View view) {

        if (mAudioTrack != null) {

            mLblDiagDesc.setVisibility(View.INVISIBLE);
            mBtnStartDiag.setVisibility(View.INVISIBLE);
            mBtnStopDiag.setVisibility(View.VISIBLE);
            mBtnGotIt.setVisibility(View.VISIBLE);

            mRunnnableForRandomPlay = new RunnnableForRandomPlay();
//            new Thread(mRunnnableForRandomPlay).start();
            mScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
            mScheduledFuture = mScheduledExecutor.schedule(mRunnnableForRandomPlay, 0, TimeUnit.MILLISECONDS);

//            final CustomFontButton clickedButton = ((CustomFontButton) view);
//            if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_STOPPED) {
//
//                // ランダムに音を再生。
//                // 周波数：ランダム
//                // 再生秒数：ランダム（0.5s 〜 1.5s程度）
//                // 再生休止秒数：ランダム（2s 〜 5s程度）
//                // 診断時間：15秒
//                mCurrentWaveGenerator = view == mBtnLv1 ? mSinWaveGenerator1
//                        : view == mBtnLv2 ? mSinWaveGenerator2
//                        : view == mBtnLv3 ? mSinWaveGenerator3
//                        : view == mBtnLv4 ? mSinWaveGenerator4
//                        : view == mBtnLv5 ? mSinWaveGenerator5
//                        : mSinWaveGenerator6;
//
//                //ボタンが押されたら再生する。
//                mAudioTrack.setStereoVolume(0, 0);
//                //AudioTrack.playの後はgetMinBufferSizeで取得した
//                //サイズより小さいとonPeriodicNotificationが実行されない。
//                mAudioTrack.play();
//                if (mForceStopTimerTask != null) mForceStopTimerTask.cancel();
//                mForceStopTimerTask = new ForceStopTimerTask();
//                mForceStopTimer.schedule(mForceStopTimerTask, 10000);
//
//                mRunnableForUpdateRightIcon = new RunnnableForUpdateRightIcon(clickedButton);
//                new Thread(mRunnableForUpdateRightIcon).start();
//
//                writeSound();
//                mAudioTrack.setStereoVolume(1, 1);
//
//                for (CustomFontButtonWithRightIcon item : mButtonList) {
//
//                    if (item == clickedButton) {
////                            item.setText(item.getText().toString().replace(FontAwesome.getFaMap().get("fa-play"), FontAwesome.getFaMap().get("fa-pause")));
//                    } else {
//                        item.setRightIcon(mFaMap.get("fa-play"));
//                    }
//                }
//            } else if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
//                //ボタンが押されたら停止
//                mRunnableForUpdateRightIcon.running = false;
//                mAudioTrack.setStereoVolume(0, 0);
//                mAudioTrack.stop();
//
//                for (CustomFontButtonWithRightIcon item : mButtonList) {
//                    item.setRightIcon(mFaMap.get("fa-play"));
//                }
//
//                if (mBtnGenMap.get(clickedButton) == mCurrentWaveGenerator) {
//                    // 再生中のLvのボタンが押された
////                                mCurrentWaveGenerator = null;
//                } else {
//                    doOnLvBtnClick(view);
//                }
//            }
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

    class RunnnableForUpdateRightIcon implements Runnable {

        protected boolean running = true;
        private CustomFontButtonWithRightIcon mClickedButton;

        public RunnnableForUpdateRightIcon(CustomFontButtonWithRightIcon button) {
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
                mHandler.post(new Runnable() {
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

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mClickedButton.setRightIcon(mFaMap.get("fa-play"));
                }
            });
        }
    }

    class ForceStopTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                        mAudioTrack.setStereoVolume(0, 0);
                        mAudioTrack.stop();
                    }
                    mRunnableForUpdateRightIcon.running = false;
                    for (CustomFontButtonWithRightIcon item : mButtonList) {
                        item.setRightIcon(mFaMap.get("fa-play"));
                    }

                }
            });
        }
    }

    class RunnnableForRandomPlay implements Runnable {

        protected boolean running = true;

        @Override
        public void run() {
            Random random = new Random();
            List<SinWaveGenerator> waveGenList = Arrays.asList(mSinWaveGenerator1, mSinWaveGenerator2, mSinWaveGenerator3, mSinWaveGenerator4, mSinWaveGenerator5, mSinWaveGenerator6);
            while (running) {
                // 停止
                mAudioTrack.setStereoVolume(0, 0);
                mAudioTrack.stop();

                // 休止秒数（ミリ秒）
                try {
                    int pauseTime = ((2 + random.nextInt(5)) * 1000);
                    Thread.sleep(pauseTime);
                } catch (InterruptedException e) {
                }

                // 周波数を決定
                int i = random.nextInt(5);
                mCurrentWaveGenerator = waveGenList.get(i);
                mAudioTrack.play();
                writeSound();
                mAudioTrack.setStereoVolume(1, 1);

                // 再生秒数（ミリ秒）
                try {
                    int playTime = ((5 + random.nextInt(10)) * 100);
                    Thread.sleep(playTime);
                } catch (InterruptedException e) {
                }
            }

            mAudioTrack.setStereoVolume(0, 0);
            mAudioTrack.stop();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mLblDiagDesc.setVisibility(View.VISIBLE);
                    mBtnStartDiag.setVisibility(View.VISIBLE);
                    mBtnStopDiag.setVisibility(View.INVISIBLE);
                    mBtnGotIt.setVisibility(View.INVISIBLE);
                }
            });

        }
    }
}
