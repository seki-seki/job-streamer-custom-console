package job_streamer.job_streamer_custom_console.util;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

public class HttpRequestUtil {
    public static String executeGet(String url) {
        Client client = null;
        try {
            client = ClientBuilder.newBuilder().build();
            Response response = client.target(url).request().get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.readEntity(String.class);
            } else {
                // TODO:OK以外のハンドリング
                System.out.println(response.getStatus());
                System.out.println(response.getStatusInfo());
            }

            response.close();
        } finally {
            Optional.ofNullable(client).ifPresent(Client::close);
        }
        // exampleなので200以外のステータスにはnullを返す。
        return null;
    }

    public static String executePostJSON(String url, Entity<?> entity) {
        Client client = null;
        try {
            client = ClientBuilder.newBuilder().build();
            final Entity<?> json = entity == null ? null : Entity.json(entity);
            final Response response = client.target(url).request(MediaType.APPLICATION_JSON)
                    .post(json);

            if (response.getStatus() == Response.Status.OK.getStatusCode() || response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                return response.readEntity(String.class);
            } else {
                // TODO:OK,CREATED以外のハンドリング
                System.out.println(response.getStatus());
                System.out.println(response.getStatusInfo());
            }

            response.close();
        } finally {
            Optional.ofNullable(client).ifPresent(Client::close);
        }
        // exampleなので200以外のステータスにはnullを返す。
        return null;
    }
}
