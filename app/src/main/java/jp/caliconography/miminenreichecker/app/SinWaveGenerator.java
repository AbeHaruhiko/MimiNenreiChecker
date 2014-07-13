package jp.caliconography.miminenreichecker.app;

class SinWaveGenerator {
    private double mFrequency = 0;
    private double mAmplification = 0;
    private double mPhase = 0;
    private int mSamplerate = 0;

    public SinWaveGenerator(double frequency, double amp, int samplerate) {
        mFrequency = frequency;
        mAmplification = amp;
        mSamplerate = samplerate;
    }

    public double generateSinWave() {
        mPhase += mFrequency / mSamplerate;
        mPhase = (mPhase > 1) ? 0 : mPhase;
        return Math.sin(2 * Math.PI * mPhase) * mAmplification;
    }

//    public double generateSinWave(int samplingIndex){
//        double t = (double)(samplingIndex) / mSamplerate;
//        // y = a * sin (2PI * f * t), t = i/fs, 0 <= i < TotalSamples
//        return mAmplification * Math.sin(2.0 * Math.PI * mFrequency * t);
//    }

    public double getFrequency() {
        return mFrequency;
    }
}
