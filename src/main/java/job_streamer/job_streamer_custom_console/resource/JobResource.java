package job_streamer.job_streamer_custom_console.resource;

import job_streamer.job_streamer_custom_console.client.ControlBusEndpoint;
import job_streamer.job_streamer_custom_console.client.ControlBusEndpoint.JobSearchQuery;
import job_streamer.job_streamer_custom_console.model.Job;
import org.glassfish.jersey.server.mvc.Viewable;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("job")
public class JobResource {

    @Inject
    private ControlBusEndpoint endpoint;

    @GET
    @Path("index")
    public Viewable index() throws Exception {
        final List<Job> jobs = endpoint.fetchJobs();

        return new Viewable("/index", jobs);
    }

    @GET
    public Viewable search(
            // TODO: 日付等のバリデーション
            @QueryParam("name") final String name,
            @QueryParam("since") final String since,
            @QueryParam("until") final String until,
            @QueryParam("status") final String status
    ) throws Exception {
        final JobSearchQuery query = JobSearchQuery.builder()
                .name(name).since(since).until(until).status(status)
                .build();
        final List<Job> jobs = endpoint.fetchJobs(query);

        return new Viewable("/index", jobs);
    }

    @POST
    @Path("{jobname}/execute")
    public Viewable execute(@PathParam("jobname") final String jobName) {
        endpoint.postExecutions(jobName, null);

        return new Viewable("/afterExecute");
    }

}