package com.miku.springaialibabaagent.controller;


import com.miku.springaialibabaagent.service.Assistant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RequestMapping("/assistant")
@RestController
@CrossOrigin(origins = "http://localhost:5173")   //在你需要允许跨域访问的 Controller 类或方法上添加 @CrossOrigin 注解
public class AssistantController {


    private final Assistant assistant;


    public AssistantController( Assistant assistant ) {

        this.assistant = assistant;
    }



    @RequestMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream( String chatId, String message) {


        return assistant.stream(chatId,message);

    }

    @RequestMapping(value = "/chat")
    public String chat( String chatId, String message) {

        String msg =
                "数据库配置信息如下,调用mcp-mysql-server服务，根据配置信息连接数据库\n" +
                "MYSQL_HOST: localhost\n" +
                "MYSQL_USER: root\n" +
                "MYSQL_PASSWORD: 123456\n" +
                "MYSQL_DATABASE: springai-alibaba-agent\n"
                + message +
                "执行命令后，说明你使用的工具名称和来源";

        return assistant.chat(chatId,msg);

    }




}
