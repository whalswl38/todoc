package com.todoc.web;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping
public class TestController {

    @GetMapping("/main-page")
    public String test2( ) {
        return "main/main";
    }

    @GetMapping("/nutrients-list-page")
    public String test13() {
        return "nutrients/nutrientsList";
    }
    @GetMapping("/nutrients-detail-page")
    public String test14() {
        return "nutrients/nutrientsDetail";
    }
   @GetMapping("/megazine-list-page")
   public String test15() {
       return "megazines/megazineList";
   }
   @GetMapping("/megazine-detail-page")
   public String test16() {
       return "megazines/megazineDetail";
   }

    @GetMapping("/mypage-page")
   public String test17() {
       return "mypage/mypage";
   }

    @GetMapping("/medical-history-page")
    public String test18() {
        return "mypage/medicalHistory";
    }

    @GetMapping("/medical-history-detail-page")
    public String test19() {
        return "mypage/medicalHistoryDetail";
    }

    @GetMapping("/review-detail-page")
    public String test20() {
        return "mypage/reviewDetail";
    }

     @GetMapping("/chat-home-page")
    public String test21() {
        return "chat/chatHome";
    }

      @GetMapping("/chat-message-page")
    public String test22() {
        return "chat/chatMessage";
    }

      @GetMapping("/chat-detail-page")
    public String test23() {
        return "chat/chatMessageDetail";
    }

    @GetMapping("/review-page")
    public String test25() {
        return "mypage/review";
    }
}
