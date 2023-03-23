package com.hao.chatgptbot.Listener;

import com.hao.chatgptbot.util.chatTool;
import com.hao.chatgptbot.util.commandtool;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;


import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyListener extends SimpleListenerHost {
    private boolean isContinue = false;
    private boolean iscat = false;
    private int chatNum  = 0;



    private void ChatAdd(){
        chatNum++;
    }
    private void ChatReset(){
        chatNum=0;
        iscat = false;
        new chatTool().clear();
    }
    private boolean checkChatNum(int max){
        if(chatNum>=max){
            return true;
        }
        return false;
    }


    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception){
        // 处理事件处理时抛出的异常
    }
//好友私聊
    @EventHandler
    public void onMessage(@NotNull FriendMessageEvent event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理

        String message = event.getMessage().contentToString();
        //判断是否为指令
        String cmd = commandtool.getCommand(message);
        if(cmd!=null){
            //指令执行
            switch (cmd){
                case "扮演猫娘":
                    if(iscat){
                        chatTool.send(event.getSubject(), "人家已经是猫娘了哦");
                        break;
                    }
                    isContinue = true;
                    iscat = true;
                    chatTool.playCatGame(event.getSubject());
                    break;
                case "退出沉浸式":
                    if(!iscat&&!isContinue){
                        chatTool.send(event.getSubject(), "目前没有处在沉浸式模式");
                        break;
                    }
                    isContinue = false;

                    if(iscat){
                       chatTool.send(event.getSubject(), "艾特我发送退出角色扮演");
                        iscat = false;
                    }

                    break;
                case "连续对话":
                    if(isContinue){
                        chatTool.send(event.getSubject(), "已经处在沉浸式模式了");
                        break;
                    }
                    isContinue = true;
                    break;
                case "清除":
                    ChatReset();
                    if(chatNum==0){
                        chatTool.send(event.getSubject(), "已失忆，我记不得刚刚说过什么了");
                    }else{
                        chatTool.send(event.getSubject(), "出错了，居然没有删除掉");
                    }
                    break;
                case "剩余token":
                    chatTool.sendRestMoney(event.getSender());
                    break;

                case "ai帮助":
                    chatTool.send(event.getSubject(), commandtool.getHelpInfo());
                    break;

                default:
                    chatTool.send(event.getSubject(),commandtool.getBadParameter());

            }
        }else{
            //判断是否处于连续对话
            if(isContinue){
                if(checkChatNum(20)){
                    chatTool.send(event.getSubject(),"最大可持续聊天已达上线，自动清除聊天记录,已退出猫娘模式,但是仍可以持续对话");
                    ChatReset();
                }
                chatTool.sendAiContinue(event.getSubject(),message);
                ChatAdd();
            }else{
                chatTool.sendAiSingle(event.getSubject(), message);
            }

        }



    }


    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理

        Group group = event.getSubject();
        Member member = event.getSender();
        At at = new At(member.getId());
        //获取群里的指令
        String message = event.getMessage().contentToString();
        //判断是否为指令
        String cmd = commandtool.getCommand(message);
        if(cmd!=null){
            //指令执行
            switch (cmd){
                case "扮演猫娘":
                    if(iscat){
                        chatTool.send(group,at, "人家已经是猫娘了哦");
                        break;
                    }
                    isContinue = true;
                    iscat = true;
                    chatTool.playCatGame(group);
                    break;
                case "退出沉浸式":
                    if(!iscat&&!isContinue){
                        chatTool.send(group,at, "目前没有处在沉浸式模式");
                        break;
                    }
                    isContinue = false;
                    iscat = false;
                    if(iscat){
                        chatTool.send(group,at, "艾特我发送退出角色扮演");
                        iscat = false;
                    }
                    break;
                case "连续对话":
                    if(isContinue){
                        chatTool.send(group,at, "已经处在沉浸式模式了");
                        break;
                    }
                    isContinue = true;
                    break;
                case "清除":
                    ChatReset();
                    if(chatNum==0){
                        chatTool.send(group,at, "已失忆，我记不得刚刚说过什么了");
                    }else{
                        chatTool.send(group,at, "出错了，居然没有删除掉");
                    }
                    break;
                case "剩余token":
                    chatTool.sendRestMoney(group);
                    break;

                case "ai帮助":
                    chatTool.send(group,at, commandtool.getHelpInfo());
                    break;

                default:
                    chatTool.send(group,at,commandtool.getBadParameter());

            }
        }else {
            //过滤普通消息只回复艾特的
            String content = commandtool.AtChat(message,new At(event.getBot().getId()).contentToString());
            if(content!=null){
                //判断是否处于连续对话
                if(isContinue){
                    if(checkChatNum(20)){
                        chatTool.send(group,at,"最大可持续聊天已达上线，自动清除聊天记录,已退出猫娘模式,但是仍可以持续对话");
                        ChatReset();
                        ChatAdd();
                    }
                    chatTool.sendAiContinue(group,at,message);
                }else{
                    chatTool.sendAiSingle(group,at, message);
                }

            }
        }







    }


//    @NotNull
//    @EventHandler
//    public ListeningStatus onMessageStatus(@NotNull MessageEvent event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理
//        event.getSubject().sendMessage("received");
//        return ListeningStatus.LISTENING; // 表示继续监听事件
//        // return ListeningStatus.STOPPED; // 表示停止监听事件
//    }

}
