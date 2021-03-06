# a socket file transfer program

这不是通用的程序，是应一个朋友的要求实现的特定的需求。

代码本身没什么花头，关键是理解运作的过程和机制。

c：客户端
s：服务端

上传（按发生的时间）：
start-》c开始发送头部（包含后续发送的文件的长度）-》s接收头部，解析头部-》返回指令给c-》c开始上传文件-》s接收保存-》返回结果给c-》end

从s的角度来说，只是不停的接受字节流而已，为了完成不同的动作（解析头部，保存文件），必须在s端维护一个状态（阈值，边界标志），根据当前的状态采取不同的动作。

可以设置这样一个值：{:state :start|:header-parsed}，这样问题就很明显了。每次接收到buffer，看看这个阈值，如果是:start，就累积buffer，直到头的规格得到解析，然后切换到:header-parsed， 告诉c可以发送文件了，s开始接收流，程序开始收到buffer，此时因为阈值是:header-parsed，对于这些buffer不再累积在内存，直接放到磁盘文件即可。


为了在一次链接过程中完成多个数据交换，可以将累积用的buffer放在atom中。这样每个阶段结束，可以用新的buffer开始。

此程序代码就是用这种思路写成，采用clojure，当然你用vertx支持的其它语言都是一样的。

# 性能测试

client config, bm_c.conf

````
{
  "client": true,
  "instances": 200, 启动200个verticle(线程)
  "per-instance-files": 10, 每个线程上传10次
  "total-files": 2000, 总共2000个上传
  "bytes-to-send": {"how-many": 1000}, 将预设的字符串上传1000次作为一个文件上传，见sampler.clj，单个字符串长度1140字节
  "host": "localhost",
  "port": 1234
}
````

server config, bm_s.conf
```
{
  "instances": 10, 启动10个verticle
  "host": "localhost",
  "port": 1234,
  "data-dir": "testdatafolder/upload", 上传文件的存储目录
  "bm-total-files": 2000, 上传总数，指示测试结束
  "verify-verticle": true
  为了真实反映实际情况，可以将客户端的per-instance-files设置成很高的值，这样运行可能会持续几十个小时或者数天，
  这样一来上传的文件数量会相当庞大，verify-verticle的作用就是验证上传文件的长度（没有验证内容，但测试里面验证了），
  然后将文件删除，将头部信息保存在report-to目录中，供稍后分析用。
}
```

1.启动服务
vertx run cn/intellijoy/clojure/starter.clj -cp src/main/resources -conf bm_s.json
或者
vertx runzip target\filetransfer-1.0-SNAPSHOT-mod.zip -conf bm_s.json

2.启动客户端
vertx run cn/intellijoy/clojure/starter.clj -cp src/main/resources -conf bm_c.json
vertx runzip target\filetransfer-1.0-SNAPSHOT-mod.zip -conf bm_c.json
