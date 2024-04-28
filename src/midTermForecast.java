import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class midTermForecast {
    String type;
    LocalDateTime base; // 발표
    Location location;
    String serviceKey = new String("QD7zanvK2jd%2FgLcdz2nBxdtEq6Fysy0gY9Mz4YvPT7XizIYXPkOPvMwSeTHG%2BCDhQcuI5g%2BfE%2FU3u3NIY7lsFQ%3D%3D");
    
    public midTermForecast(Location _location)
    {
        location = _location;
        type = null;
    }

    public String getWeather_midTerm_text(String stnId)
    {
        //text 형식의 예보라 필요하지 않음
        //만약 사용한다면 stnId로 다른 코드가 필요
        base = LocalDateTime.now();
        if (base.getHour() < 6){base = base.withHour(18).minusDays(1).withMinute(0);}
        else if (base.getHour() >= 6 && base.getHour() < 18){base = base.withHour(6).withMinute(0);}
        else{base = base.withHour(18).withMinute(0);}
        type = "중기전망조회";
        int num = 1;
        String api = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidFcst?dataType=JSON&serviceKey=" + serviceKey + "&numOfRows=" + num + "pageNo=1&stnId=" + stnId + "&tmFc=" + base.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        System.out.println(api);
        JSONObject responseJson = GetJson.getJson(api);
        System.out.println();
        return (String)(((JSONObject)((JSONObject)((JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item")).get(0))).get("wfSv"));
    }

    public MidTermWeather[] getWeather_midTerm_get_all()
    {
        // type = "중기육상예보조회";
        //강수 확률과 날씨(ex 구름많음 최대 10일)
        base = LocalDateTime.now();
        if (base.getHour() >= 18){base = base.withHour(18).withMinute(0);}
        else if (base.getHour() < 6){base = base.minusDays(1).withHour(18).withMinute(0);}
        else{base = base.withHour(6).withMinute(0);}
        base = base.withMinute(0);
        MidTermWeather weathr[] = new MidTermWeather[8];
        JSONObject responseJson = null;
        try
        {
            int num = 12;
            int pageNo = 1; // 1400의 예보라면 1페이지에 1500 2페이지에 1500식
            String api = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=" + pageNo + "&regId=" + location.regId + "&base_date=" + "&tmFc=" + base.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            responseJson = GetJson.getJson(api);
            JSONObject item = (JSONObject)((JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item")).get(0);
            for(int i = 0; i < 5; ++i)
            {
                weathr[i] = new MidTermWeather();
                weathr[i].base = base;
                weathr[i].rnStAm = item.get("rnSt" + (i + 3) + "Am").toString();
                weathr[i].rnStPm = item.get("rnSt" + (i + 3) + "Pm").toString();
                weathr[i].wfAm =  item.get("wf" + (i + 3) + "Am").toString();
                weathr[i].wfPm = item.get("wf" + (i + 3) + "Pm").toString();
            }
            try
            {
                for(int i = 5; i < 8; ++i)
                {
                    weathr[i] = new MidTermWeather();
                    weathr[i].rnStAm = item.get("rnSt" + (i + 3)).toString();
                    weathr[i].rnStPm = item.get("rnSt" + (i + 3)).toString();
                    weathr[i].wfAm =  item.get("wf" + (i + 3)).toString();
                    weathr[i].wfPm = item.get("wf" + (i + 3)).toString();
                }
            }
            catch(Exception e){}
        }
        catch(Exception e){System.err.println(responseJson);}
        return weathr;
    }

    public midTermTemp[] getWeather_midTerm_highest_lowest_temp(String regId)//중기육상예보조회와 다른 rdgId를 사용함
    {
        // type = "중기기온조회";
        //예상기온
        base = LocalDateTime.now();
        if (base.getHour() < 6){base = base.withHour(18).minusDays(1).withMinute(0);}
        else if (base.getHour() >= 6 && base.getHour() < 18){base = base.withHour(6).withMinute(0);}
        else{base = base.withHour(18).withMinute(0);}
        JSONObject responseJson = null;
        midTermTemp temp[] = new midTermTemp[8];
        try
        {
            int num = 1000;
            int pageNo = 1;
            String api = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=" + pageNo + "&regId=" + regId + "&base_date=" + "&tmFc=" + base.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            responseJson = GetJson.getJson(api);
            JSONObject item = (JSONObject)((JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item")).get(0);
            for(int i = 0; i < 5; ++i)
            {
                temp[i] = new midTermTemp();
                temp[i].max = Integer.parseInt(item.get("taMax" + (i + 3)).toString());
                temp[i].min = Integer.parseInt(item.get("taMax" + (i + 3)).toString());
            }
            try
            {
                for(int i = 5; i < 8; ++i)
                {
                    temp[i] = new midTermTemp();
                    temp[i].max = Integer.parseInt(item.get("taMax" + (i + 3)).toString());
                    temp[i].min = Integer.parseInt(item.get("taMax" + (i + 3)).toString());
                }
            }
            catch(Exception e){}
        }
        catch(Exception e){System.err.println(responseJson);}
        return temp;
    }

    // private void getWeather_midTerm_4()
    // {
    //     type = "중기해상예보조회";
    //     //regid 형식이 다름
    //     //바다의 날씨를 보는 것이니 크게 필요하지 않을 것
    //     int num = 12;
    //     int pageNo = 1; // 1400의 예보라면 1페이지에 1500 2페이지에 1500식
    //     String api = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidSeaFcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=" + pageNo + "&regId=" + location.regId + "&base_date=" + "&tmFc=" + base.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    //     JSONObject responseJson = GetJson.getJson(api);
    //     JSONObject temp;
    //     System.err.println(responseJson);
    // }
}
