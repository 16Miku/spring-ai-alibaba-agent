package com.miku.springaialibabaagent.controller;


import com.miku.springaialibabaagent.service.Assistant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RequestMapping("/assistant")
@RestController
public class AssistantController {


    private final Assistant assistant;


    public AssistantController( Assistant assistant ) {

        this.assistant = assistant;
    }



    @RequestMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat( String chatId, String message) {


        return assistant.chat(chatId,message);

    }




}
