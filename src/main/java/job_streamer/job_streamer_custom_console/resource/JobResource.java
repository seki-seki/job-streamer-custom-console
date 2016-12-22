package job_streamer.job_streamer_custom_console.resource;

import java.net.URI;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.server.mvc.Viewable;

import job_streamer.job_streamer_custom_console.client.ControlBusEndpoint;
import job_streamer.job_streamer_custom_console.client.ControlBusEndpoint.JobSearchQuery;
import job_streamer.job_streamer_custom_console.model.Job;

@Path("job")
public class JobResource {

    @Inject
    private ControlBusEndpoint endpoint;

    @GET
    @Path("index")
    public Object index(@Context UriInfo uriInfo, @Context final HttpServletRequest servletRequest) throws Exception {
        // TODO: sessionチェックをmiddlewareで行う。
        final HttpSession session = servletRequest.getSession(true);
        String token = (String)session.getAttribute("Token");
        if (token == null) {
            URI uri = uriInfo.getBaseUriBuilder().path("/login").build();
            return Response.seeOther(uri).build();
        }
        final List<Job> jobs = endpoint.fetchJobs(token);
        // TODO: 適切なエラーレスポンスの実装を行う。
        if (jobs == null) {
            URI uri = uriInfo.getBaseUriBuilder().path("/login").build();
            return Response.seeOther(uri).build();
        }

        return new Viewable("/index", jobs);
    }

    @GET
    @Path("search")
    public Object search(@Context UriInfo uriInfo, 
            // TODO: 日付等のバリデーション
            @QueryParam("name") final String name,
            @QueryParam("since") final String since,
            @QueryParam("until") final String until,
            @QueryParam("exitStatus") final String exitStatus,
            @QueryParam("batchStatus") final String batchStatus,
            @Context final HttpServletRequest servletRequest) throws Exception {
        // TODO: sessionチェックをmiddlewareで行う。
        final HttpSession session = servletRequest.getSession(true);
        String token = (String)session.getAttribute("Token");
        if (token == null) {
            URI uri = uriInfo.getBaseUriBuilder().path("/login").build();
            return Response.seeOther(uri).build();
        }
        final JobSearchQuery query = JobSearchQuery.builder()
                .name(name).since(since).until(until).exitStatus(exitStatus).batchStatus(batchStatus)
                .build();
        final List<Job> jobs = endpoint.fetchJobs(query, token);
        // TODO: 適切なエラーレスポンスの実装を行う。
        if (jobs == null) {
            URI uri = uriInfo.getBaseUriBuilder().path("/login").build();
            return Response.seeOther(uri).build();
        }

        return new Viewable("/index", jobs);
    }

    @POST
    @Path("{jobname}/execute")
    public Object execute(@Context UriInfo uriInfo, 
            @PathParam("jobname") final String jobName,
            @Context final HttpServletRequest servletRequest) {
        // TODO: sessionチェックをmiddlewareで行う。
        final HttpSession session = servletRequest.getSession(true);
        String token = (String)session.getAttribute("Token");
        if (token == null) {
            URI uri = uriInfo.getBaseUriBuilder().path("/login").build();
            return Response.seeOther(uri).build();
        }

        // TODO: 適切なエラーレスポンスの実装を行う。
        if (endpoint.postExecutions(jobName, null, token) == null) {
            URI uri = uriInfo.getBaseUriBuilder().path("/login").build();
            return Response.seeOther(uri).build();
        }

        return new Viewable("/afterExecute");
    }

    @POST
    @Path("{jobname}/{executionid}/stop")
    public Object stop(@Context UriInfo uriInfo, 
            @PathParam("jobname") final String jobName,@PathParam("executionid") final String executionid,
            @Context final HttpServletRequest servletRequest) {
        // TODO: sessionチェックをmiddlewareで行う。
        final HttpSession session = servletRequest.getSession(true);
        String token = (String)session.getAttribute("Token");
        if (token == null) {
            URI uri = uriInfo.getBaseUriBuilder().path("/login").build();
            return Response.seeOther(uri).build();
        }

        // TODO: 適切なエラーレスポンスの実装を行う。
        if (endpoint.stopExecutions(jobName,executionid, null, token) == null) {
            URI uri = uriInfo.getBaseUriBuilder().path("/login").build();
            return Response.seeOther(uri).build();
        }
        return new Viewable("/afterStop");
    }

}