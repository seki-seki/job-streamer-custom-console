package job_streamer.job_streamer_custom_console;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

public class MyApplication extends ResourceConfig {

    public MyApplication() {
        this.packages(MyApplication.class.getPackage().getName())
            .register(JspMvcFeature.class);
    }
}