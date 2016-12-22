package job_streamer.job_streamer_custom_console.resource;

import java.net.URI;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.server.mvc.Viewable;

import job_streamer.job_streamer_custom_console.client.ControlBusEndpoint;
import job_streamer.job_streamer_custom_console.util.HttpRequestUtil;

@Path("")
public class LoginResource {

    @Inject
    private ControlBusEndpoint endpoint;

    @GET
    @Path("login")
    public Viewable index(@Context final HttpServletRequest servletRequest) throws Exception {
        final HttpSession session = servletRequest.getSession(true);
        session.removeAttribute("Token");
        return new Viewable("/login", false);
    }

    @POST
    @Path("login")
    public Object login(@Context UriInfo uriInfo, 
            @Context final HttpServletRequest servletRequest,
            @FormParam("username") final String username,
            @FormParam("password") final String password) {
        final String token = endpoint.postLogin(username, password);
        if (token == HttpRequestUtil.UNAUTHORIZED) {
            return new Viewable("/login", true);
        } else {
            final HttpSession session = servletRequest.getSession(true);
            session.setAttribute("Token", token);
            URI uri = uriInfo.getBaseUriBuilder().path("/job/index").build();
            return Response.seeOther(uri).build();
        }
    }

    @GET
    @Path("logout")
    public Object logout(@Context UriInfo uriInfo,
            @Context final HttpServletRequest servletRequest) {
        final HttpSession session = servletRequest.getSession(true);
        session.removeAttribute("Token");
        URI uri = uriInfo.getBaseUriBuilder().path("/login").build();
        return Response.seeOther(uri).build();
    }
}