package jp.caliconography.miminenreichecker.app;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import jp.caliconography.android.miminenreichecker.app.R;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    final static String TAG = MainActivity.class.getSimpleName();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private Thread backgroundThread;

        private AudioTrack mAudioTrack;

        private Button mBtnLv1;
        private Button mBtnLv2;
        private Button mBtnLv3;
        private Button mBtnLv4;
        private Button mBtnLv5;
        private Button mBtnLv6;
        private ArrayList<Button> mButtonList = new ArrayList<Button>();

        private Timer timer = new Timer();
        private Handler handle = new Handler();

        private SinWaveGenerator mCurrentWaveGenerator;
        private SinWaveGenerator sinWaveGenerator1;
        private SinWaveGenerator sinWaveGenerator2;
        private SinWaveGenerator sinWaveGenerator3;
        private SinWaveGenerator sinWaveGenerator4;
        private SinWaveGenerator sinWaveGenerator5;
        private SinWaveGenerator sinWaveGenerator6;
        private HashMap<View, SinWaveGenerator> mBtnGenMap = new HashMap<View, SinWaveGenerator>();
//        private double mCurrentWave;
//        private double wave1;
//        private double wave2;
//        private double wave3;
//        private double wave4;
//        private double wave5;
//        private double wave6;
//        private HashMap<View, Double> mBtnGenMap = new HashMap<View, Double>();
//        private short[] mCurrentWave;
//        private short[] wave1;
//        private short[] wave2;
//        private short[] wave3;
//        private short[] wave4;
//        private short[] wave5;
//        private short[] wave6;
//        private HashMap<View, short[]> mBtnGenMap = new HashMap<View, short[]>();

        private final int mFrequency = 17000;
        private int mSamplerate = 44100;
        private short[] mSoundBuffer;
        private int mSoundBufferSize;
        private ForceStopTimer mForceStopTimer;

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

        public PlaceholderFragment() {
//            backgroundThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    mAudioTrack.write(mCurrentWaveGenerator, 0, mCurrentWaveGenerator.length);
//                }
//            });
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            Debug.startMethodTracing("tao");

            //バッファーサイズの取得
            mSoundBufferSize = AudioTrack.getMinBufferSize(
                    mSamplerate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            //AudioTrackの初期化
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    //サンプリング定数
                    mSamplerate,
                    //モノラル
                    AudioFormat.CHANNEL_OUT_MONO,
                    //16bit
                    AudioFormat.ENCODING_PCM_16BIT,
                    //バッファーサイズ
                    mSoundBufferSize,
                    //ストリームモード
                    AudioTrack.MODE_STREAM);

            mSoundBuffer = new short[mSoundBufferSize];
            Log.d("", String.valueOf(mSoundBufferSize));
            //所得したバッファーサイズごとに通知させる。
            mAudioTrack.setPositionNotificationPeriod(mSoundBufferSize);
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
            sinWaveGenerator1 = new SinWaveGenerator(440, 1);
            sinWaveGenerator2 = new SinWaveGenerator(880, 1);
            sinWaveGenerator3 = new SinWaveGenerator(16000, 1);
            sinWaveGenerator4 = new SinWaveGenerator(18000, 1);
            sinWaveGenerator5 = new SinWaveGenerator(20000, 1);
            sinWaveGenerator6 = new SinWaveGenerator(21000, 1);
//            wave1 = new SinWaveGenerator(440, 1).generateSinWave();
//            wave2 = new SinWaveGenerator(15000, 1).generateSinWave();
//            wave3 = new SinWaveGenerator(16000, 1).generateSinWave();
//            wave4 = new SinWaveGenerator(18000, 1).generateSinWave();
//            wave5 = new SinWaveGenerator(20000, 1).generateSinWave();
//            wave6 = new SinWaveGenerator(21000, 1).generateSinWave();


            mBtnLv1 = (Button) rootView.findViewById(R.id.btn_lv1);
            mBtnLv2 = (Button) rootView.findViewById(R.id.btn_lv2);
            mBtnLv3 = (Button) rootView.findViewById(R.id.btn_lv3);
            mBtnLv4 = (Button) rootView.findViewById(R.id.btn_lv4);
            mBtnLv5 = (Button) rootView.findViewById(R.id.btn_lv5);
            mBtnLv6 = (Button) rootView.findViewById(R.id.btn_lv6);
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
//            mBtnGenMap.put(mBtnLv1, wave1);
//            mBtnGenMap.put(mBtnLv2, wave2);
//            mBtnGenMap.put(mBtnLv3, wave3);
//            mBtnGenMap.put(mBtnLv4, wave5);
//            mBtnGenMap.put(mBtnLv5, wave5);
//            mBtnGenMap.put(mBtnLv6, wave6);

            View.OnClickListener onClickListener = new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    doOnClick(view);
                }
            };
            mBtnLv1.setOnClickListener(onClickListener);
            mBtnLv2.setOnClickListener(onClickListener);
            mBtnLv3.setOnClickListener(onClickListener);
            mBtnLv4.setOnClickListener(onClickListener);
            mBtnLv5.setOnClickListener(onClickListener);
            mBtnLv6.setOnClickListener(onClickListener);

            return rootView;
        }

        private void doOnClick(final View view) {
            Log.d(TAG, "1");
            if (mAudioTrack != null) {
                Button clickedButton = ((Button) view);
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
                    Log.d(TAG, "4");

                    writeSound();
                    mAudioTrack.setStereoVolume(1, 1);
                    Log.d(TAG, "5");

                    for (Button item : mButtonList) {
                        Log.d(TAG, "6");
                        if (item == clickedButton) {
                            item.setText(item.getText().toString().replace("", ""));
                        } else {
                            item.setText(item.getText().toString().replace("", ""));
                        }
                    }
                    Log.d(TAG, "7");
                } else if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                    Log.d(TAG, "8");

                    //ボタンが押されたら停止
                    mAudioTrack.setStereoVolume(0, 0);
                    mAudioTrack.stop();
                    Log.d(TAG, "9");

                    for (Button item : mButtonList) {
                        Log.d(TAG, "10");
                        item.setText(item.getText().toString().replace("", ""));
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
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
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
            Debug.stopMethodTracing();
        }

        //後々モジュレーション方式に移行するためスタイル
        class SinWaveGenerator {
            public double freq = 0;
            public double amp = 0;
            public double phase = 0;

            public SinWaveGenerator(double f, double a) {
                freq = f;
                amp = a;
            }

            public double generateSinWave() {
                phase += freq / mSamplerate;
                phase = (phase > 1) ? 0 : phase;
                return Math.sin(2 * Math.PI * phase) * amp;
            }
//            public short[] generateSinWave() {
//                phase += freq / mSamplerate;
//                phase = (phase > 1) ? 0 : phase;
//                short[] buff = new short[mSoundBufferSize];
//                return getSound(buff, Math.sin(2 * Math.PI * phase) * amp);
//            }
        }

        void writeSound() {
//            backgroundThread.run();
            for (int i = 0; i < mSoundBuffer.length; i++) {
                mSoundBuffer[i] = (short) (Short.MAX_VALUE * mCurrentWaveGenerator.generateSinWave());
            }
            mAudioTrack.write(mSoundBuffer, 0, mSoundBuffer.length);
//            mAudioTrack.write(mCurrentWave, 0, mCurrentWave.length);
        }

        short[] getSound(short[] buff, double wave) {
            for (int i = 0; i < buff.length; i++) {
                mSoundBuffer[i] = (short) (Short.MAX_VALUE * wave);
            }
            return buff;
        }

        class ForceStopTimer extends TimerTask {
            @Override
            public void run() {
                handle.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                            mAudioTrack.setStereoVolume(0, 0);
                            mAudioTrack.stop();
                        }
                        for (Button item : mButtonList) {
                            item.setText(item.getText().toString().replace("", ""));
                        }

                    }
                });
            }
        }
    }
}