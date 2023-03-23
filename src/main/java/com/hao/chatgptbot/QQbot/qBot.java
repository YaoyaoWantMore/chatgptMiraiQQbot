package com.hao.chatgptbot.QQbot;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;


@Component
@Slf4j
@Configuration
public class qBot {
    @Value("${bot.username}")
    private Long username;
    @Value("${bot.password}")
    private String password;

    public void setUsername(Long username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private static Bot bot;

    public  Bot getBot(){
        return bot;
    }

    private final static String deviceInfo = "device.json";

    public void StartBot(){
        if(null==username||null==password){
            System.err.println("未配置账号和密码");
            log.warn("未配置账号或密码");
        }else{
            bot = BotFactory.INSTANCE.newBot(username,password,new BotConfiguration(){
                {
                    //保存设备信息到device.json
                    fileBasedDeviceInfo(deviceInfo);
                    //设置为安卓平板在线
                    setProtocol(MiraiProtocol.ANDROID_PAD);

                    //设置心跳策略：STAT_HB、REGISTER 和 NONE
                    setHeartbeatStrategy(HeartbeatStrategy.STAT_HB);

                }
            });
            FixProtocolVersion.update();
            log.warn(FixProtocolVersion.info().toString());

            bot.login();


        }
    }







}
