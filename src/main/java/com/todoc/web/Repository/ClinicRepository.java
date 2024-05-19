package com.todoc.web.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.todoc.web.dto.ClinicContact;

@Repository
public class ClinicRepository {

	

    @Autowired
    private SqlSession sqlSession;

    public List<ClinicContact> getClinics(int startRow, int endRow, String category, String searchValue, String clinicNight, String clinicWeekend, String textSearch, String guName, List<String> runningNumList) {
        Map<String, Object> params = new HashMap<>();
        params.put("startRow", startRow);
        params.put("endRow", endRow);
        params.put("category", category);
        params.put("searchValue", searchValue);
        params.put("clinicNight", clinicNight);
        params.put("clinicWeekend", clinicWeekend);
        params.put("textSearch", textSearch);
        params.put("guName", guName);
        params.put("runningNumList", runningNumList);
        return sqlSession.selectList("getClinics", params);
    }
}
