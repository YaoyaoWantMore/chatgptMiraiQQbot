# 这是一个基于mirai+chatgpt-java+springboot的QQ机器人项目

### 快速安装运行

1.这是一个springboot项目，将项目压缩包下载到本地，或者使用git克隆。

2.然后打开idea，**修改配置文件**，直接**打包**，然后把**jar包上传服务器**。

3.在服务器的jar目录下执行，**注意：要保证服务器防火墙（安全组策略）将你设置的端口开放**

```cmd
java -jar -Xmx1024M -Xms256M  ChatgptBot-0.0.1-SNAPSHOT.jar --server.port=8089
```



4.会提示**qq登录滑动验证**，这时将link后面的链接（https开头）放入滑动验证助手，通过验证后把得到的一串代码粘贴到下面继续回车，这时候有的人会有设备锁就需要发送短信，输入yes，将手机收到的验证码输入，回车，得到Bot login successful就说明成功了，可以在qq对机器人私聊看看能不能得到回复。然会就会发现当前目录下生成了device.json![image-20230323154227923](C:/Users/86156/AppData/Roaming/Typora/typora-user-images/image-20230323154227923.png)这个东西就可以保证在这个设备登录不需要滑动验证之类的操作了。

5.此时为了保证bot能够在后台持续运行，我们先CTRL+C关闭程序运行，然后执行

```cmd
nohup java -jar -Xmx1024M -Xms256M  ChatgptBot-0.0.1-SNAPSHOT.jar --server.port=8089 >bot.log 1>&2 &
```

这段代码可以让程序后台运行而不会停止，执行后返回一个进程id说明成功了，也可以执行netstat -lnp|grep 8089 ，8089就是你设置的端口号，查看正在运行的java程序。



6.好了，机器人就运行起来了，如果服务器关机的话，需要再次使用5中的命令重新启动。



### 项目详解

这是项目非常简单，适合入门springboot，想上手mirai，想自己定制一个qq机器人，都可以在这个项目基础上进行拓展，还附带chatgpt功能哦。



- application.properties是项目的配置文件，机器人的密码账号，以及chatgpt的api的key都在里面修改

  ![image-20230323155011538](C:/Users/86156/AppData/Roaming/Typora/typora-user-images/image-20230323155011538.png)

- qBot是机器人的设置，里面包括账号密码（可以到配置文件修改），设备协议（平板登录，手机登录等等默认安卓平板），心跳策略等等设置，**注意：已加入登录修复，2023.3.23号测试没问题，如果显示登录失败版本过低就删除device.json，重新验证登录。**

![image-20230323155735798](C:/Users/86156/AppData/Roaming/Typora/typora-user-images/image-20230323155735798.png)

- MyListener是继承Mirai的事件监听器，主要实现了私聊监听和群聊监听，也就是好友私聊和qq群可以使用。

  ![image-20230323155928793](C:/Users/86156/AppData/Roaming/Typora/typora-user-images/image-20230323155928793.png)

- Module下面的chatgptClient就是实现chatgpt询问的客户端，是基于[chatgpt-java](https://github.com/Grt1228/chatgpt-java)一个别人的SDK实现的，实现的功能有连续对话，单次询问，清除对话历史（相当于开启新话题）,查询你的免费额度还剩多少钱（总所周知调用api是要花钱的，每个免费用户现在注册只有5美元的额度了），这个对话调用的模型是最新的GPT-3.5那个t什么的模型，chatgpt4据说也支持，你们自己研究这里是他的项目地址[chatgpt-java](https://github.com/Grt1228/chatgpt-java)。

- 然后是工具类：

  - chatTool是一个发送消息的工具类，里面设置了调教猫娘的常量[知乎上面抄的](https://zhuanlan.zhihu.com/p/610745644)，还有chatgptClient很重要的参数也在这里的静态代码里面封装，然后就是实现了，群消息，好友消息的发送等等功能。

  - commandtool其实是一个对命令进行解析和对群里艾特消息进行解析的工具类，里面有帮助菜单的常量，你们可以修改然后实现自己的功能。

  - SpringUtil是一个获得机器人bean的工具类，不了解springboot的bean创建过程知识就别去动他。

    ![image-20230323160942883](C:/Users/86156/AppData/Roaming/Typora/typora-user-images/image-20230323160942883.png)
