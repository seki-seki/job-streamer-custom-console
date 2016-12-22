package job_streamer.job_streamer_custom_console.resource;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.server.mvc.Viewable;

import job_streamer.job_streamer_custom_console.client.ControlBusEndpoint;

@Path("")
public class LoginResource {

    @Inject
    private ControlBusEndpoint endpoint;

    @GET
    @Path("login")
    public Viewable index() throws Exception {
        return new Viewable("/login");
    }

    @POST
    @Path("login")
    public Viewable login(@Context final HttpServletRequest servletRequest,
            @FormParam("username") final String username,
            @FormParam("password") final String password) {
        final String token = endpoint.postLogin(username, password);
        if (token != null) {
            final HttpSession session = servletRequest.getSession(true);
            session.setAttribute("Token", token);			
            return new Viewable("/index");
        } else {
            // TODO: ログイン失敗メッセージを出す
            return new Viewable("/index");
        }
    }

    @POST
    @Path("logout")
    public Viewable logout(@Context final HttpServletRequest servletRequest) {
        final HttpSession session = servletRequest.getSession(true);
        session.removeAttribute("Token");
        return new Viewable("/index");
    }
}