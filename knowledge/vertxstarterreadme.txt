Creating a project using the archetype and specifying it exactly and specifying parameters on the command line

This will search for any archetypes in the io.vertx group and prompt you to choose one.

mvn archetype:generate -DarchetypeGroupId=io.vertx -DarchetypeArtifactId=vertx-maven-archetype -DarchetypeVersion=2.0.11-final -DgroupId=cc.openscanner.vertx -DartifactId=notifier -Dversion=0.0.1

因为vertx官方将archetype plugin项目发布到中央maven库，所以采用这个方法可以忽略archetype catalog的限制。


安装：
1、从http://vertx.io/downloads.html下载合适的版本，
2、放在任意合适的位置，解压，将解压后的bin目录加到环境变量中，比如在windows系统中，新开一个cmd，输入vertx version，会有版本显示
3、java需要JDK 1.7.0 or later，确保JDK的bin目录在PATH变量中。


在项目源代码根目录运行必须带上路径：
vertx run src\main\resources\ping_verticle.js
vertx run src\main\resources\echo-server.js


测试相关：
mvn test
mvn integration-test

单个文件测试：
mvn -Dtest=TestCircle#mytest test

mvn test -Dtest=JavaScriptIntegrationTests
mvn test -Dtest=ClojureScriptIntegrationTests

mvn -Dit.test=ThreadTest verify
#ITCircle#testOne+testTwo

-Dtest.timeout=60 单位s
-Dorg.vertx.logger-delegate-factory-class-name=org.vertx.java.core.logging.impl.Log4jLogDelegateFactory

在集成测试环境下，classpath包含了target目录，但是直接vertx run raw verticle情况下，这些classpath都没有设置好，会启动失败。

vertx runmod cn.intellijoy.vertx~filetransfer~1.0-SNAPSHOT -conf bm_c.json

所以强制自己写测试代码，而不是人工观察。

vertx run cn/intellijoy/clojure/starter.clj -cp src/main/resources -conf bm_s.json
vertx run cn/intellijoy/clojure/starter.clj -cp src/main/resources -conf bm_c.json

vertx runzip target\filetransfer-1.0-SNAPSHOT-mod.zip -conf bm_s.json

vertx runmod cn.intellijoy.vertx~filetransfer~1.0-SNAPSHOT -conf bm_s.json

  "host": "192.168.88.130",
  "host": "localhost",
