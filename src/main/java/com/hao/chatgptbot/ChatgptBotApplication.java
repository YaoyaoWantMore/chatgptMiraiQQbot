package com.hao.chatgptbot;

import com.hao.chatgptbot.QQbot.qBot;
import com.hao.chatgptbot.Listener.MyListener;
import com.hao.chatgptbot.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ChatgptBotApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChatgptBotApplication.class, args);
        ApplicationContext context =  SpringUtil.getApplicationContext();

        qBot qqbot = context.getBean(qBot.class);
        qqbot.StartBot();
        qqbot.getBot().getEventChannel().registerListenerHost(new MyListener());
    }

}
