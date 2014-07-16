package jp.caliconography.miminenreichecker.app;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by abeharuhiko on 2014/07/16.
 */
public class SinWaveGeneratorTest {

    @Test
    public void getFrequencyがインスタンス生成時に渡したfrequencyを返す() {

        // Setup
        double expected = 44100;
        SinWaveGenerator sinWaveGenerator = new SinWaveGenerator(expected, 0, 0);

        // Exercise
        double actual = sinWaveGenerator.getFrequency();

        // Verify
        assertThat(actual, is(expected));
    }

}
