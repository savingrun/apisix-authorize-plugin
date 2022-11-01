package run.saving.apisix;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

//@SpringBootTest
class ApisixAuthorizePluginApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testGson() {
        Gson gson = new Gson();
        String config = "{\\\"jwt_member_secret_key\\\":\\\"8JPWHA6D5YWMZ5ZUFXJG1O2VSFZGYOTMFGFIM0QKN6DV4LQW1SYILUR4VF743HO7\\\"}";
        Map<String, Object> conf = new HashMap<>(4);
        conf = gson.fromJson(config, conf.getClass());
        System.out.println(conf.toString());
    }

}
