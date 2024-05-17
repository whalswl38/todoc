package com.todoc.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.todoc.web.security.jwt.JwtAuthorizationFilter;
import com.todoc.web.service.UserService;

@Controller
@RequestMapping
public class TestController {

	@Autowired
	private UserService userService;
	private final JwtAuthorizationFilter jwtFilter;
	
	public TestController(JwtAuthorizationFilter jwtFilter)
	{
		this.jwtFilter = jwtFilter;
	}
	
    @GetMapping("/main-page")
    public String test2() {    	
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
    
    
    @GetMapping("/reservationList-page")
    public String test29() {
        return "mypage/reservationList";
    }
    
    @GetMapping("/medical-mypage")
    public String test30() {
        return "mypage/mypageMedical";
    }
}
