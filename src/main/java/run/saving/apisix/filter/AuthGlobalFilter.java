package run.saving.apisix.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.apisix.plugin.runner.HttpRequest;
import org.apache.apisix.plugin.runner.HttpResponse;
import org.apache.apisix.plugin.runner.PostRequest;
import org.apache.apisix.plugin.runner.PostResponse;
import org.apache.apisix.plugin.runner.filter.PluginFilter;
import org.apache.apisix.plugin.runner.filter.PluginFilterChain;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * <p> AuthGlobalFilter
 * <p> 2022/5/31 9:34
 *
 * @author brandon, <a href="https://github.com/savingrun">Saving</a>
 * @version 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements PluginFilter, Ordered {

    private final Gson gson = new Gson();
    private final Cache<String, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    @Override
    public String name() {
        return "AuthGlobalFilter";
    }

    @Override
    public void filter(HttpRequest request, HttpResponse response, PluginFilterChain chain) {
        log.warn("filter test: request {}", request);
        chain.filter(request, response);
    }

    @Override
    public void postFilter(PostRequest request, PostResponse response, PluginFilterChain chain) {
        Map<String, String> upstreamHeaders = request.getUpstreamHeaders();
        String config = request.getConfig(this);
        log.debug("postFilter log: config-{}", config);
        String jwtMemberSecretKey = getFilterConfig(request.getConfig(this), "jwt_member_secret_key");
        String tokenRenewalTime = getFilterConfig(request.getConfig(this), "token_renewal_time");
        String tokenExpirationMinute = getFilterConfig(request.getConfig(this), "token_expiration_minute");
        log.debug("postFilter log: jwtMemberSecretKey-{}, tokenRenewalTime-{}, tokenExpirationMinute-{}",
                jwtMemberSecretKey, tokenRenewalTime, tokenExpirationMinute);

        response.setHeader(JwtUtils.NEW_TOKEN_HEADER, "newToken");
        log.debug("[[postFilter Finish]]");
        chain.postFilter(request, response);
    }

    private String getFilterConfig(String config, String key) {
        String cacheValue = cache.getIfPresent(key);
        if (StringUtils.isNotBlank(cacheValue)) {
            log.debug("cache success: {} - {}", key, cacheValue);
            return cacheValue;
        }
        log.debug("no cache: {} - {}", key, cacheValue);
        Map<String, Object> conf = new HashMap<>(8);
        conf = gson.fromJson(config, conf.getClass());
        if (ObjectUtils.isEmpty(conf)) {
            return "";
        }
        conf.entrySet().forEach(entry -> {
            cache.put(entry.getKey(), (String) entry.getValue());
            log.debug("saving cache it: {} - {}", entry.getKey(), entry.getValue());
        });
        String configValue = (String) conf.getOrDefault(key, "");
        if (StringUtils.isBlank(configValue)) {
            log.error("Failed to get configuration " + key + " parameter, value is empty");
        }
        return configValue;
    }

    @Override
    public List<String> requiredVars() {
        return Collections.emptyList();
    }

    @Override
    public Boolean requiredBody() {
        return false;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
