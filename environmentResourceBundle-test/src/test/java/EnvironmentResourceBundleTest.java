import com.environment.resource.EnvironmentResourceBundle;
import org.junit.Test;

import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;

@EnvironmentResourceBundle(names = {"test"})
public class EnvironmentResourceBundleTest {

    @Test
    public void resourceBundleTest() {
        System.setProperty("what_kind", "replaced");
        ResourceBundle testBundle = ResourceBundle.getBundle("test");
        assertEquals("here is a replaced property", testBundle.getString("testProp"));
    }
}
