package job_streamer.job_streamer_custom_console;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Test;

public class MyResourceTest {

    private static final MyResource TARGET = new MyResource();

    @Test
    public void parseQueryTest() {
        assertThat("", is(TARGET.parseQuery(null, null, null, null)));
        assertThat("a", is(TARGET.parseQuery("a", null, null, null)));
        assertThat("a since:2000-01-01", is(TARGET.parseQuery("a", "2000-01-01", null, null)));
        assertThat("a since:2000-01-01 until:2100-01-01", is(TARGET.parseQuery("a", "2000-01-01", "2100-01-01", null)));
        assertThat("a since:2000-01-01 until:2100-01-01 exit-status:COMPLETED",
                is(TARGET.parseQuery("a", "2000-01-01", "2100-01-01", "COMPLETED")));
    }

    @Test
    public void ednToJobTest() {
        assertThat(TARGET.ednToJob("{:results [{:job/name \"a\"}]}").get(0).getName(), is("a"));
        assertThat(TARGET.ednToJob("{:results [{:job/latest-execution {:job-execution/exit-status \"COMPLETED\"}}]}")
                .get(0).getLastExitStatus(), is("COMPLETED"));
        assertThat(TARGET
                .ednToJob(
                        "{:results [{:job/latest-execution {:job-execution/end-time #inst \"2000-01-01T00:00:00.000-00:00\"}}]}")
                .get(0).getLastExecutionEndTime(), is(toDate(LocalDateTime.of(2000, 1, 1, 9, 0))));
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


}
