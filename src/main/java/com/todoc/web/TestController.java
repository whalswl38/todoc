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
    @GetMapping("/select-subject-page")
    public String test5( ) {
        return "untact/selectSubject";
    }

    @GetMapping("/select-item-page")
    public String test6( ) {
        return "untact/selectItem";
    }

    @GetMapping("/select-clinic-page")
    public String test7(Model model) {
        List<SelectClinic> list = new ArrayList<>();
        SelectClinic item1 = new SelectClinic("이현호 의사", "일등이비인후의원", 5.0, 8, true, "(금) 09:00 ~ 19:00", "이비인후과전문의", true,"https://d2m9duoqjhyhsq.cloudfront.net/marketingContents/article/article996-01.jpg");
        SelectClinic item2 = new SelectClinic("민현홍 의사", "둔산싱싱튼튼의원", 4.8, 120, true, "(금) 10:00 ~ 23:50", null, true,"https://d2m9duoqjhyhsq.cloudfront.net/marketingContents/article/article996-01.jpg");
        list.add(item1);
        list.add(item2);
        model.addAttribute("clinicList", list);
        return "untact/selectClinic";
    }

    @GetMapping("/select-clinic-detail-page")
    public String test8() {
        return "untact/selectClinicDetail";
    }

    @GetMapping("/clinic-reserve-page")
    public String test9() {
        return "untact/clinicReservation";
    }

    @GetMapping("/clinic-reserve-payment-page")
    public String test10() {
        return "untact/clinicReservationPayment";
    }

    @GetMapping("/clinic-reserve-user-page")
    public String test11() {
        return "untact/reservationUserView";
    }

    @GetMapping("/clinic-reserve-doctor-page")
    public String test12() {
        return "untact/reservationDoctorView";
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


     @GetMapping("/chat-home-page")
    public String test21() {
        return "chatbot/chatHome";
    }

      @GetMapping("/chat-message-page")
    public String test22() {
        return "chatbot/chatMessage";
    }

      @GetMapping("/chat-detail-page")
    public String test23() {
        return "chatbot/chatMessageDetail";
    }

    @GetMapping("/clinic-list-page")
    public String test24(Model model) {
        List<ClinicList> list = new ArrayList<>();
        ClinicList item1 = new ClinicList(true, "일등이비인후의원", "안면마비 비만 교통사고후유증", true, "21:00", 1.4, "서울 마포구 성산동", null, null, "https://www.urbanbrush.net/web/wp-content/uploads/edd/2017/09/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7-2017-09-19-%EC%98%A4%ED%9B%84-8.59.36.png", "한43422");
        ClinicList item2 = new ClinicList(true, "둔산싱싱튼튼의원", "쁘띠, 리프팅, 제모, 보톡스시술", true, "21:00", 1.6, "서울 마포구 성산동", null, null,"https://www.urbanbrush.net/web/wp-content/uploads/edd/2017/09/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7-2017-09-19-%EC%98%A4%ED%9B%84-8.59.36.png", "의13422");
        ClinicList item3 = new ClinicList(false, "일등이비인후의원", "피부과", true, "20:00", 1.6, "서울 마포구 영등포동", 966, 652, "https://www.urbanbrush.net/web/wp-content/uploads/edd/2017/09/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7-2017-09-19-%EC%98%A4%ED%9B%84-8.59.36.png", "");
        ClinicList item4 = new ClinicList(false,  "둔산싱싱튼튼의원", "피부과", false, "14:00", 1.6, "서울 마포구 성산동", 552,432,"https://www.urbanbrush.net/web/wp-content/uploads/edd/2017/09/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7-2017-09-19-%EC%98%A4%ED%9B%84-8.59.36.png", "");
        ClinicList item5 = new ClinicList(false, "일등이비인후의원", "피부과", false, "14:00", 1.7, "서울 마포구 성산동", 421,232,"https://www.urbanbrush.net/web/wp-content/uploads/edd/2017/09/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7-2017-09-19-%EC%98%A4%ED%9B%84-8.59.36.png", "한41412");
        ClinicList item6 = new ClinicList(false, "둔산싱싱튼튼의원", "피부과", true, "20:00", 1.9,"서울 마포구 성산동", 142, 111,"https://www.urbanbrush.net/web/wp-content/uploads/edd/2017/09/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7-2017-09-19-%EC%98%A4%ED%9B%84-8.59.36.png", "의43422");
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);
        list.add(item6);
        model.addAttribute("clinicList", list);	
        return "clinic/clinicList";
    }

    
    
    @GetMapping("/infoUpdate-page")
    public String test27() {
        return "mypage/infoUpdate";
    }
    
    @GetMapping("/mypage-page2")
    public String test28() {
        return "mypage/mypageMedical";
    }
}
