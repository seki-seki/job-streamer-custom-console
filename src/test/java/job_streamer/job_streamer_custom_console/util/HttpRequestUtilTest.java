package job_streamer.job_streamer_custom_console.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import javax.ws.rs.core.UriBuilder;

import org.junit.Test;

public class HttpRequestUtilTest {

    @Test
    public void test() {
        final UriBuilder uriBuilder = UriBuilder.fromUri(Optional.ofNullable(System.getenv().get("CONTROL_BUS_URL")).orElse("http://localhost:45102/")).path("login")
                .queryParam("next", Optional.ofNullable(System.getenv().get("ACCESS_CONTROL_ALLOW_ORIGIN")).orElse("http://localhost:3000"))
                .queryParam("back", Optional.ofNullable(System.getenv().get("ACCESS_CONTROL_ALLOW_ORIGIN")).orElse("http://localhost:3000/login"))
                .queryParam("username", "admin")
                .queryParam("password", "password123")
                .queryParam("appname", "default");
        final String url = uriBuilder.build().toString();
         HttpRequestUtil.executeLoginPost(url);
         System.out.println(HttpRequestUtil.executeLoginPost(url));
        assertThat(HttpRequestUtil.executeLoginPost(url),is(not("UNAUTHORIZED")));
    }

}
