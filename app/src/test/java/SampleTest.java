
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18, manifest = Config.NONE)
public class SampleTest {

    @Before
    public void setup() {
        // setup
    }

    @After
    public void teardown() {
        // teardown
    }

    @Test
    public void testSample() {
        Assert.assertEquals("a", "a");
    }
}