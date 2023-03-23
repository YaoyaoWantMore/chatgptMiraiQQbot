package com.hao.chatgptbot.Module.ChatGpt;

import com.unfbx.chatgpt.OpenAiClient;

import com.unfbx.chatgpt.entity.billing.CreditGrantsResponse;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.extern.slf4j.Slf4j;


import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class chatgptClient {

    private chatgptClient(){

    }

    private volatile static chatgptClient chat;



    public static chatgptClient getInstance(){

        if(chat==null){
            synchronized (chatgptClient.class){
                if(chat==null){
                    chat = new chatgptClient();
                }
            }
        }



        return chat;
    }


//    private final OpenAiClient client = OpenAiClient.builder()
//            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getIp(), config.getPort())))
//            .apiKey(new config().getToken())
//            .connectTimeout(50)
//            .readTimeout(50)
//            .writeTimeout(50)
//            .apiHost("https://api.openai.com/")
//            .build();


    List<Message> messages = new ArrayList<>();
    //进行持续对话
    public String GetContinueChat(String question,OpenAiClient client){
        StringBuffer answer = new StringBuffer();
        //

        System.out.println("开始封装请求============");
        Message message = Message.builder().role(Message.Role.USER).content(question).build();
        messages.add(message);
        ChatCompletion completion = ChatCompletion.builder().messages(messages).build();
        ChatCompletionResponse chatCompletionResponse = client.chatCompletion(completion);
        chatCompletionResponse.getChoices().forEach(e ->{
            answer.append(e.getMessage().getContent());
            messages.add(e.getMessage());
//            System.out.println(e.getMessage().getContent());
        });

        return answer.toString();
    }

    //清空当前对话
    public void ClearMessages(){
        messages.clear();
    }

    public double GetRestToken(OpenAiClient client){
        CreditGrantsResponse creditGrantsResponse = client.creditGrants();
        log.info("账户总余额（美元）：{}", creditGrantsResponse.getTotalGranted());
        log.info("账户总使用金额（美元）：{}", creditGrantsResponse.getTotalUsed());
        log.info("账户总剩余金额（美元）：{}", creditGrantsResponse.getTotalAvailable());
        return creditGrantsResponse.getTotalAvailable().floatValue();
    }
//一问一答模式
    public String GetSingleChat(String question,OpenAiClient client){
        StringBuffer answer = new StringBuffer();
        //

        System.out.println("=========开始封装请求============");
        Message message = Message.builder().role(Message.Role.USER).content(question).build();
        ChatCompletion completion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
        ChatCompletionResponse chatCompletionResponse = client.chatCompletion(completion);

        chatCompletionResponse.getChoices().forEach(e ->{
            answer.append(e.getMessage().getContent());
        });
        System.out.println("========得到结果============");
        return answer.toString();
    }
}

//class Test{
//    public static void main(String[] args) {
//
//
//    }
//}
