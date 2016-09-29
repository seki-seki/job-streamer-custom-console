package job_streamer.job_streamer_custom_console;

import static us.bpsm.edn.Keyword.newKeyword;
import static us.bpsm.edn.parser.Parsers.defaultConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.glassfish.jersey.server.mvc.Viewable;

import com.google.common.base.Strings;

import job_streamer.job_streamer_custom_console.model.Job;
import job_streamer.job_streamer_custom_console.util.HttpRequestUtil;
import job_streamer.job_streamer_custom_console.util.MapUtil;
import us.bpsm.edn.parser.Parseable;
import us.bpsm.edn.parser.Parser;
import us.bpsm.edn.parser.Parsers;

@Path("/")
public class MyResource {
    public static final String CONTROL_BUS_URL = System.getenv().get("CONTROL_BUS_URL");

    @GET
    public Viewable index() {
        // control-busはedn形式でResponseを返すのでhttps://github.com/bpsm/edn-javaを使いMapにparseする
        Parseable pbr = Parsers.newParseable(HttpRequestUtil.executeGet(CONTROL_BUS_URL + "/default/jobs"));
        Parser p = Parsers.newParser(defaultConfiguration());
        Map<?, ?> jobdata = (Map<?, ?>) p.nextValue(pbr);

        List<Map<?, ?>> jobResults = (List<Map<?, ?>>) jobdata.get((newKeyword("results")));
        // key型がｊｓｐでは扱いにくいので扱い安い形にparseする
        return new Viewable("/index",
                jobResults.stream()
                        .map(singleJobResult -> new Job((String) singleJobResult.get(newKeyword("job", "name")),
                                (String) MapUtil.getIn(singleJobResult, newKeyword("job", "latest-execution"),
                                        newKeyword("job-execution", "exit-status")),
                                (Date) MapUtil.getIn(singleJobResult, newKeyword("job", "latest-execution"),
                                        newKeyword("job-execution", "end-time"))))
                        .collect(ArrayList<Job>::new, ArrayList<Job>::add, ArrayList<Job>::addAll));

    }

    @Path("{jobname}/execute")
    @POST
    public Viewable execute(@PathParam("jobname") String jobname) {
        System.out.println();
        HttpRequestUtil.executePostJSON(CONTROL_BUS_URL + "/default/job/" + jobname + "/executions", null);
        return new Viewable("/afterExecute");
    }

    @Path("search")
    @GET
    public Viewable search(@QueryParam("name") String name, @QueryParam("since") String since,
            @QueryParam("until") String until, @QueryParam("status") String status) {
        // control-busはedn形式でResponseを返すのでhttps://github.com/bpsm/edn-javaを使いMapにparseする
        String query = parseQuery(name, since, until, status);
        System.out.println(query);
        Parseable pbr = Parsers.newParseable(HttpRequestUtil.executeGet(CONTROL_BUS_URL + "/default/jobs?q=" + query));
        Parser p = Parsers.newParser(defaultConfiguration());
        Map<?, ?> jobdata = (Map<?, ?>) p.nextValue(pbr);

        List<Map<?, ?>> jobResults = (List<Map<?, ?>>) jobdata.get((newKeyword("results")));
        // key型がｊｓｐでは扱いにくいので扱い安い形にparseする
        return new Viewable("/index",
                jobResults.stream()
                        .map(singleJobResult -> new Job((String) singleJobResult.get(newKeyword("job", "name")),
                                (String) MapUtil.getIn(singleJobResult, newKeyword("job", "latest-execution"),
                                        newKeyword("job-execution", "exit-status")),
                                (Date) MapUtil.getIn(singleJobResult, newKeyword("job", "latest-execution"),
                                        newKeyword("job-execution", "end-time"))))
                        .collect(ArrayList<Job>::new, ArrayList<Job>::add, ArrayList<Job>::addAll));
    }

    String parseQuery(String name, String since, String until, String status) {
        StringBuilder sb = new StringBuilder();
        if (Strings.isNullOrEmpty(name))
            sb.append(name).append(" ");
        if (Strings.isNullOrEmpty(since))
            sb.append("since:").append(since).append(" ");
        if (Strings.isNullOrEmpty(until))
            sb.append("until:").append(until).append(" ");
        if (Strings.isNullOrEmpty(status))
            sb.append("exit-status:").append(status).append(" ");
        if (sb.length() == 0)
            return "";
        return sb.toString().substring(0, sb.length() - 2);
    }
}