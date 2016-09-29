package job_streamer.job_streamer_custom_console;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MyResourceTest {

    private static final MyResource TARGET = new MyResource();
    @Test
    public void parseQueryTest() {
        assertThat("",is(TARGET.parseQuery(null, null, null, null)));
        assertThat("a",is(TARGET.parseQuery("a", null, null, null)));
        assertThat("a since:2000-01-01",is(TARGET.parseQuery("a", "2000-01-01", null, null)));
        assertThat("a since:2000-01-01 until:2100-01-01",is(TARGET.parseQuery("a", "2000-01-01", "2100-01-01", null)));
        assertThat("a since:2000-01-01 until:2100-01-01 exit-status:COMPLETED",is(TARGET.parseQuery("a", "2000-01-01", "2100-01-01", "COMPLETED")));
    }

}
