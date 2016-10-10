package job_streamer.job_streamer_custom_console.client;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import job_streamer.job_streamer_custom_console.edn.converter.EdnJobConverter;
import job_streamer.job_streamer_custom_console.model.Job;
import job_streamer.job_streamer_custom_console.util.HttpRequestUtil;
import lombok.Builder;
import org.glassfish.jersey.uri.UriComponent;
import org.glassfish.jersey.uri.UriComponent.Type;

import javax.annotation.Nonnull;
import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.UriBuilder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yhonda
 */
@Singleton
public final class ControlBusEndpoint {

    private static final String DEFAULT_URL = "http://localhost:45102/";

    private static final String CONTROL_BUS_URL =
            Optional.ofNullable(System.getenv().get("CONTROL_BUS_URL")).orElse(DEFAULT_URL);

    private EdnJobConverter ednJobConverter = new EdnJobConverter();

    public List<Job> fetchJobs() {
        return fetchJobs(null);
    }

    public List<Job> fetchJobs(final JobSearchQuery query) {
        final UriBuilder uriBuilder = UriBuilder.fromUri(CONTROL_BUS_URL).path("default/jobs");

        if (query != null && !Strings.isNullOrEmpty(query.toString()) ) {
            final String encodedQuery = UriComponent.contextualEncode(query.toString(), Type.QUERY_PARAM_SPACE_ENCODED);

            uriBuilder.queryParam("q", encodedQuery);
        }
        final String url = uriBuilder.build().toString();
        final String edn = HttpRequestUtil.executeGet(url);

        if (Strings.isNullOrEmpty(edn)) {
            return Lists.newArrayList();
        }
        return ednJobConverter.convert(edn);
    }

    public void postExecutions(@Nonnull final String jobName, final Entity<?> exections) {
        final UriBuilder uriBuilder = UriBuilder.fromUri(CONTROL_BUS_URL).path("default/jobs/{name}/executions");

        final String url = uriBuilder.build(jobName).toString();
        HttpRequestUtil.executePostJSON(url, exections);
    }

    /**
     * @author yhonda
     */
    @Builder
    public static class JobSearchQuery {

        private String name;
        private String since;
        private String until;
        private String status;

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
            if (!Strings.isNullOrEmpty(status)) {
                queryItems.add("exit-status:" + status);
            }
            return queryItems.stream().collect(Collectors.joining(" "));
        }
    }

}
