package job_streamer.job_streamer_custom_console.util;

import java.util.Map;
import java.util.Optional;

public class MapUtil {
    public static Object getIn(Map map, Object... keys) {
        if (keys == null) {
            return null;
        }
        // TODO:あとでstream API + 再帰にする。
        Optional optMap = Optional.ofNullable(map);
        for (Object key : keys) {
            optMap = optMap.filter(progressMap -> progressMap instanceof Map)
                    .map(progressMap -> ((Map) progressMap).get(key));
        }
        return optMap.orElse(null);
    }
}
