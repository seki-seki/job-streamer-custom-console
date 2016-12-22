package job_streamer.job_streamer_custom_console.resource;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.server.mvc.Viewable;

import com.sun.research.ws.wadl.Response;

import job_streamer.job_streamer_custom_console.client.ControlBusEndpoint;
import job_streamer.job_streamer_custom_console.client.ControlBusEndpoint.JobSearchQuery;
import job_streamer.job_streamer_custom_console.model.Job;

@Path("job")
public class JobResource {

    @Inject
    private ControlBusEndpoint endpoint;

    @GET
    @Path("index")
    public Viewable index(@Context final HttpServletRequest servletRequest) throws Exception {
        final HttpSession session = servletRequest.getSession(true);
        String token = (String)session.getAttribute("Token");
        final List<Job> jobs = endpoint.fetchJobs(token);
        return new Viewable("/index", jobs);
    }

    @GET
    @Path("search")
    public Viewable search(
            // TODO: 日付等のバリデーション
            @QueryParam("name") final String name,
            @QueryParam("since") final String since,
            @QueryParam("until") final String until,
            @QueryParam("exitStatus") final String exitStatus,
            @QueryParam("batchStatus") final String batchStatus,
            @Context final HttpServletRequest servletRequest) throws Exception {
        final HttpSession session = servletRequest.getSession(true);
        String token = (String)session.getAttribute("Token");
        final JobSearchQuery query = JobSearchQuery.builder()
                .name(name).since(since).until(until).exitStatus(exitStatus).batchStatus(batchStatus)
                .build();
        final List<Job> jobs = endpoint.fetchJobs(query, token);

        return new Viewable("/index", jobs);
    }

    @POST
    @Path("{jobname}/execute")
    public Viewable execute(@PathParam("jobname") final String jobName,
            @Context final HttpServletRequest servletRequest) {
        final HttpSession session = servletRequest.getSession(true);
        String token = (String)session.getAttribute("Token");
        endpoint.postExecutions(jobName, null, token);

        return new Viewable("/afterExecute");
    }

    @POST
    @Path("{jobname}/{executionid}/stop")
    public Viewable stop(@PathParam("jobname") final String jobName,@PathParam("executionid") final String executionid,
            @Context final HttpServletRequest servletRequest) {
        final HttpSession session = servletRequest.getSession(true);
        String token = (String)session.getAttribute("Token");
        endpoint.stopExecutions(jobName,executionid, null, token);
        return new Viewable("/afterStop");
    }

}