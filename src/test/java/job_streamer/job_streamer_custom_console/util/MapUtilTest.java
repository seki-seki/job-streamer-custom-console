package job_streamer.job_streamer_custom_console.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class MapUtilTest {

    @Test
    public void test() {
        Map nest2Map = new HashMap();
        nest2Map.put("key", "value");
        Map nest1Map = new HashMap();
        nest1Map.put("nest2", nest2Map);
        Map parentMap = new HashMap();
        parentMap.put("nest1", nest1Map);
        
        assertThat("value",is(MapUtil.getIn(parentMap, "nest1","nest2","key")));
        assertThat("value",is(MapUtil.getIn(nest1Map, "nest2","key")));
        assertThat("value",is(MapUtil.getIn(nest2Map,"key")));
    }

}
