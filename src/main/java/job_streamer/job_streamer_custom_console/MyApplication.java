package job_streamer.job_streamer_custom_console;

import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

import job_streamer.job_streamer_custom_console.client.ControlBusEndpoint;

@ApplicationPath("/")
public class MyApplication extends ResourceConfig {

    public MyApplication() {
        packages(MyApplication.class.getPackage().getName());
        register(JspMvcFeature.class);
        property(JspMvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/view");
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindAsContract(ControlBusEndpoint.class);
            }
        });
    }


}