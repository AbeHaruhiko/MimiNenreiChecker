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

import jp.caliconography.android.miminenreichecker.app.R;
import jp.caliconography.android.widget.CustomFontButton;
import jp.caliconography.android.widget.CustomFontButtonWithRightIcon;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiagnosisFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiagnosisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiagnosisFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "2";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
    private int mSamplerate = 44100;
    private short[] mSoundBuffer;
    private int mSoundBufferSize;
    private ForceStopTimer mForceStopTimer;
    private Map<String, String> mFaMap = FontAwesome.getFaMap();
    private BackgroundThread mBackgroundRunnable;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiagnosisFragment.
     */
//    // TODO: Rename and change types and number of parameters
//    public static DiagnosisFragment newInstance(String param1, String param2) {
//        DiagnosisFragment fragment = new DiagnosisFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    public DiagnosisFragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DiagnosisFragment newInstance(int sectionNumber) {
        DiagnosisFragment fragment = new DiagnosisFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_diagnosis, container, false);

        CustomFontButton btnStart = (CustomFontButton) rootView.findViewById(R.id.btn_start_diag);

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


        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction();
    }

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
    }
}
