package job_streamer.job_streamer_custom_console.edn.converter;

import static us.bpsm.edn.Keyword.newKeyword;
import static us.bpsm.edn.parser.Parsers.defaultConfiguration;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import job_streamer.job_streamer_custom_console.edn.annotation.EdnKey;
import job_streamer.job_streamer_custom_console.model.Job;
import lombok.Getter;
import us.bpsm.edn.Keyword;
import us.bpsm.edn.parser.Parseable;
import us.bpsm.edn.parser.Parser;
import us.bpsm.edn.parser.Parsers;

/**
 * EDN to Job converter.
 * -------------------------------------
 * https://github.com/bpsm/edn-java
 *
 * @author yhonda
 */
public class EdnJobConverter {

    private static final Parser EDN_PARSER = Parsers.newParser(defaultConfiguration());

    private static final Map<String, BeanPropertyMeta> CACHE;

    static {
        CACHE = Maps.newHashMap();

        for (final Field field : Job.class.getDeclaredFields()) {
            CACHE.put(field.getName(), new BeanPropertyMeta(Job.class, field));
        }
    }

    public Job convertJob(@Nonnull String edn) {
        // edn parser don't work when edn contains "[\d+]\/[\d+]"
        // TODO: remove it when edn-java fix this issue
        Pattern p = Pattern.compile("(\\d+)/(\\d+)");
        Matcher m = p.matcher(edn);
        while(m.find()){
            edn = edn.replace(m.group(), String.valueOf(Integer.parseInt(m.group(1))/Integer.parseInt(m.group(2))));
        }
        final Parseable parseable = Parsers.newParseable(edn);
        final Map jobSingleResults = (Map) EDN_PARSER.nextValue(parseable);

        return createJob(jobSingleResults, CACHE);
    }
    
    public List<Job> convertJobs(@Nonnull final String edn) {
        final Parseable parseable = Parsers.newParseable(edn);
        final Map parsedData = (Map) EDN_PARSER.nextValue(parseable);

        final List<Map<Keyword, ?>> jobResults = (List<Map<Keyword, ?>>) parsedData.get(newKeyword("results"));

        return jobResults.stream()
                .map(result -> createJob(result, CACHE))
                .collect(Collectors.toList());
    }

    private Job createJob(@Nonnull final Map<Keyword, ?> jobResult,
                          @Nonnull final Map<String, BeanPropertyMeta> beanPropertyMetaMap) {
        final Job entity = new Job();

        for (final Map.Entry<String, BeanPropertyMeta> entry : beanPropertyMetaMap.entrySet()) {
            final BeanPropertyMeta propertyMeta = entry.getValue();

            final EdnKey annotation = propertyMeta.getField().getAnnotation(EdnKey.class);
            if (annotation == null || Strings.isNullOrEmpty(annotation.value())) {
                continue;
            }
            final String ednKey = annotation.value();
            final List<String> nestedEdnKey = Stream.of(ednKey.split(" ")).collect(Collectors.toList());

            propertyMeta.set(entity, resolveValue(nestedEdnKey, jobResult));
        }
        return entity;
    }

    private static Object resolveValue(@Nonnull final List<String> ednKeyHierarchy,
                                       final Map<Keyword, ?> ednMap) {
        if (ednMap == null || ednKeyHierarchy.size() == 0) {
            return null;
        }
        final Keyword keyword = ednMap.keySet().stream()
                .filter(key -> key.toString().equals(ednKeyHierarchy.get(0)))
                .findFirst().orElse(null);
        if (keyword == null) {
            return null;
        }
        final Object ednValue = ednMap.get(keyword);

        if (ednKeyHierarchy.size() > 1) {
            final List<String> lowerEdnKeys = ednKeyHierarchy.subList(1, ednKeyHierarchy.size());

            return resolveValue(lowerEdnKeys, (Map<Keyword, ?>) ednValue);
        }
        return ednValue;
    }

    @Getter
    private static class BeanPropertyMeta<ENTITY> {
        private final Class<ENTITY> clazz;

        private final Method setter;

        private final Field field;

        public BeanPropertyMeta(Class<ENTITY> clazz, Field field) {
            this.clazz = clazz;
            this.field = field;

            try {
                PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz);
                this.setter = descriptor.getWriteMethod();

            } catch (IntrospectionException e) {
                throw new RuntimeException("Fail to init property descriptor.", e);
            }
        }

        public void set(ENTITY entity, Object value) {
            try {
                setter.invoke(entity, value);

            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("cannot set value to property.", e);
            }
        }

    }


}
