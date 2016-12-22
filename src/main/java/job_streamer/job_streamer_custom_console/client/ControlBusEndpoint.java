package job_streamer.job_streamer_custom_console.client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.uri.UriComponent;
import org.glassfish.jersey.uri.UriComponent.Type;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import job_streamer.job_streamer_custom_console.edn.converter.EdnJobConverter;
import job_streamer.job_streamer_custom_console.model.Job;
import job_streamer.job_streamer_custom_console.util.HttpRequestUtil;
import lombok.Builder;

/**
 * @author yhonda
 */
@Singleton
public final class ControlBusEndpoint {

    private static final String DEFAULT_URL = "http://localhost:45102/";

    private static final String CONTROL_BUS_URL =
            Optional.ofNullable(System.getenv().get("CONTROL_BUS_URL")).orElse(DEFAULT_URL);

    private EdnJobConverter ednJobConverter = new EdnJobConverter();

    public List<Job> fetchJobs(final String token) {
        return fetchJobs(null, token);
    }

    public List<Job> fetchJobs(final JobSearchQuery query, final String token) {
        final UriBuilder uriBuilder = UriBuilder.fromUri(CONTROL_BUS_URL).path("default/jobs");
        if (query != null && !Strings.isNullOrEmpty(query.toString()) ) {
            final String encodedQuery = UriComponent.contextualEncode(query.toString(), Type.QUERY_PARAM_SPACE_ENCODED);

            uriBuilder.queryParam("q", encodedQuery);
        }
        final String url = uriBuilder.build().toString();
        final String edn = HttpRequestUtil.executeGet(url, token);
        if (edn == HttpRequestUtil.UNAUTHORIZED) {
        	return null;
        } else if (Strings.isNullOrEmpty(edn)) {
            return Lists.newArrayList();
        }
        return ednJobConverter.convertJobs(edn);
    }

    public String postExecutions(@Nonnull final String jobName, final Entity<?> exections, @Nonnull final String token) {
        final UriBuilder uriBuilder = UriBuilder.fromUri(CONTROL_BUS_URL).path("default/job/{name}/executions");

        final String url = uriBuilder.build(jobName).toString();
        if (HttpRequestUtil.executePostJSON(url, exections, token) == HttpRequestUtil.UNAUTHORIZED) {
        	return null;
        }
        return "Success";
    }

    public String stopExecutions(@Nonnull final String jobName,@Nonnull final String executionId, final Entity<?> exections, @Nonnull final String token) {
        final UriBuilder uriBuilder = UriBuilder.fromUri(CONTROL_BUS_URL).path("default/job/{name}/execution/{id}/stop");

        final String url = uriBuilder.build(jobName,executionId).toString();
        if (HttpRequestUtil.executePutJSON(url, exections, token) == HttpRequestUtil.UNAUTHORIZED) {
        	return null;
        }
        return "Success";
    }

    /**
     * @author yhonda
     */
    @Builder
    public static class JobSearchQuery {

        private String name;
        private String since;
        private String until;
        private String exitStatus;
        private String batchStatus;

        @Override
        public String toString() {
            final List<String> queryItems = Lists.newArrayList();

            if (!Strings.isNullOrEmpty(name)) {
                queryItems.add(name);
            }
            if (!Strings.isNullOrEmpty(since)) {
                queryItems.add("since:" + since);
            }
            if (!Strings.isNullOrEmpty(until)) {
                queryItems.add("until:" + until);
            }
            if (!Strings.isNullOrEmpty(exitStatus)) {
                queryItems.add("exit-status:" + exitStatus);
            }
            if (!Strings.isNullOrEmpty(batchStatus)) {
                queryItems.add("batch-status:" + batchStatus);
            }
            return queryItems.stream().collect(Collectors.joining(" "));
        }
    }

    public String postLogin(@Nonnull final String username, @Nonnull final String password) {
        final UriBuilder uriBuilder = UriBuilder.fromUri(CONTROL_BUS_URL).path("login")
                // TODO: リダイレクトURLの環境変数化
                .queryParam("next", "http://localhost:3000")
                .queryParam("back", "http://localhost:3000/login")
                .queryParam("username", username)
                .queryParam("password", password)
                .queryParam("appname", "default");
        final String url = uriBuilder.build().toString();
        return HttpRequestUtil.executeLoginPost(url);
    }

}
