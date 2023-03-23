package com.hao.chatgptbot.util;

import org.springframework.stereotype.Component;

@Component
public class commandtool {
    public static String help = "\n主要功能\n(<>内为必填,[]内为选填,所有选项只支持纯文本:)\n" +
            "@bot <文本>:与机器人对话\n" +
            "@bot 退出角色扮演模式: 退出预设猫娘模式\n" +
            "#连续对话: ai能记住你上下文，但是限制次数（20）\n"+
            "#扮演猫娘: 仅限私聊模式\n"+
            "#剩余token: 获取剩下买token的钱\n"+
            "以下命令会清除历史对话,小心使用:\n" +
            "#退出沉浸式: 退出猫娘模式或者连续对话模式\n"+
            "#清除: 清除历史对话(失忆~)\n" ;
    public static String badParameter = "错误的参数,使用#ai帮助 查看使用方法";
    //获取指令内容
    public static String getCommand(String input){
        if(input.charAt(0)=='#'){
            return input.substring(1);
        }

        return null;


    }
    //获取被艾特的消息内容
    public static String AtChat(String input,String botInfo){
        if(input.contains(botInfo)&&input.charAt(0)=='@'){
            return input.substring(botInfo.length());
        }
        return null;

    }

    public static String getHelpInfo(){
        return help;
    }

    public static String getBadParameter(){
        return badParameter;
    }




}
