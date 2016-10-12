package job_streamer.job_streamer_custom_console.edn.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import job_streamer.job_streamer_custom_console.model.Job;

public class EdnJobConverterTest {

    private static final EdnJobConverter CONVERTER = new EdnJobConverter();

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void convertJobs() {
        final String edn = "{" +
                ":results [{\n" +
                "  :db/id 17592186045451\n" +
                "  :job/stats {\n" +
                "    :total 49\n" +
                "    :success 39\n" +
                "    :failure 3\n" +
                "  }\n" +
                "  :job/schedule {\n" +
                "    :schedule/active? true\n" +
                "    :schedule/cron-notation \"0 0 * * * ?\"\n" +
                "  }\n" +
                "  :job/restartable? true\n" +
                "  :job/name \"MyShell\"\n" +
                "  :job/next-execution {\n" +
                "    :job-execution/start-time #inst \"2015-04-13T09:00:00.000-00:00\"\n" +
                "  }\n" +
                "  :job/latest-execution {\n" +
                "    :job-execution/exit-status \"queued\"\n" +
                "    :job-execution/end-time #inst \"2015-04-13T07:00:00.006-00:00\"\n" +
                "  }\n" +
                "}]" +
                "}";
        final List<Job> actual = CONVERTER.convertJobs(edn);

        assertThat(actual.size(), is(1));

        assertThat(actual.get(0).getName(), is("MyShell"));
        assertThat(actual.get(0).getLastExitStatus(), is("queued"));

        final Date lastExecutionEndTime = actual.get(0).getLastExecutionEndTime();
        assertThat(FORMAT.format(lastExecutionEndTime), is("2015-04-13 16:00:00"));
    }
    
    @Test
    public void convertJob() {
        final String edn = "{\n" +
                "  :db/id 17592186045451\n" +
                "  :job/stats {\n" +
                "    :total 49\n" +
                "    :success 39\n" +
                "    :failure 3\n" +
                "  }\n" +
                "  :job/schedule {\n" +
                "    :schedule/active? true\n" +
                "    :schedule/cron-notation \"0 0 * * * ?\"\n" +
                "  }\n" +
                "  :job/restartable? true\n" +
                "  :job/name \"MyShell\"\n" +
                "  :job/next-execution {\n" +
                "    :job-execution/start-time #inst \"2015-04-13T09:00:00.000-00:00\"\n" +
                "  }\n" +
                "  :job/latest-execution {\n" +
                "    :db/id 17592186045464"+
                "    :job-execution/exit-status \"queued\"\n" +
                "    :job-execution/end-time #inst \"2015-04-13T07:00:00.006-00:00\"\n" +
                "  }\n" +
                "}";
        final Job actual = CONVERTER.convertJob(edn);


        assertThat(actual.getName(), is("MyShell"));
        assertThat(actual.getLastExitStatus(), is("queued"));
        assertThat(actual.getLastExecutionId(), is(Long.parseLong("17592186045464")));
        final Date lastExecutionEndTime = actual.getLastExecutionEndTime();
        assertThat(FORMAT.format(lastExecutionEndTime), is("2015-04-13 16:00:00"));
    }

}