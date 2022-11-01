# apisix-authorize-plugin

> apisix version: `v2.15`
> 
> apisix-java-plugin version: `v0.3.0`

自定义认证插件，实现`Token`校验与续签等功能

## 构建插件`jar`包

```shell
mvn clean package -P prod,source -Djps.track.ap.dependencies=false -Dmaven.javadoc.skip=true -Drat.skip=true
```

其它说明待完善...

![external-plugin.png](https://raw.githubusercontent.com/apache/apisix/release/2.99/docs/assets/images/external-plugin.png)

## 相关资料

-  [Apache APISIX](https://apisix.apache.org/zh/docs/apisix/2.15/getting-started/)
- [APISIX External Plugin](https://apisix.apache.org/zh/docs/apisix/2.15/external-plugin/) 
- [APISIX Plugin ext-plugin-post-resp](https://apisix.apache.org/zh/docs/apisix/2.15/plugins/ext-plugin-post-resp/)
- [APISIX java-plugin-runner](https://apisix.apache.org/zh/docs/java-plugin-runner/development/)
- [Github java-plugin-runner](https://github.com/apache/apisix-java-plugin-runner)

