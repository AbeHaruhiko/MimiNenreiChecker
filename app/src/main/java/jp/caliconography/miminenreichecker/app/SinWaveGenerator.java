package jp.caliconography.miminenreichecker.app;

class SinWaveGenerator {
    private double mFrequency = 0;
    private double mAmp = 0;
    private double mPhase = 0;
    private int mSamplerate = 0;

    public SinWaveGenerator(double frequency, double amp, int samplerate) {
        mFrequency = frequency;
        mAmp = amp;
        mSamplerate = samplerate;
    }

    public double generateSinWave() {
        mPhase += mFrequency / mSamplerate;
        mPhase = (mPhase > 1) ? 0 : mPhase;
        return Math.sin(2 * Math.PI * mPhase) * mAmp;
    }

    public double getFrequency() {
        return mFrequency;
    }
}
