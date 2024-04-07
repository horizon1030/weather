import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class ShortTermForeacast {
    //예측되는 시간의 날짜나 추가적인 정보 저장 필요
    //중기 예보와 단기 예보에 각 4종을 확인하고 가장 적합한 것을 선택할 필요가 있음
    String type;
    int tmp; // 온도
    int pop; // 강수확률
    int pcp; // 강수량
    LocalDateTime fcst; //예보
    LocalDateTime base; // 발표
    Location location;
    String serviceKey = new String("QD7zanvK2jd%2FgLcdz2nBxdtEq6Fysy0gY9Mz4YvPT7XizIYXPkOPvMwSeTHG%2BCDhQcuI5g%2BfE%2FU3u3NIY7lsFQ%3D%3D");
    
    public ShortTermForeacast(){type = null;}

    public ShortTermForeacast(LocalDateTime _base, Location _location)
    {
        base = _base;
        location = _location;
        type = null;
    }

    public int getTmp(){return tmp;}
    public int getPop(){return pop;}
    public int getpcp(){return pcp;}
    public void setType(String _type){type = _type;}
    public void setFcst(LocalDateTime _fcst){fcst = _fcst;}
    public void setBase(LocalDateTime _base){base = _base;}
    public void setLocation(Location _location){location = _location;}
    public void getWeather()
    {
        // getWeather_shortTerm();
    }

    private void getWeather_shortTerm()
    {
        // getWeather_shortTerm_1();
        // getWeather_shortTerm_2();
        // getWeather_shortTerm_3();
        getWeather_shortTerm_4();
    }

    private void getWeather_shortTerm_1()
    {
        type = "초단기실황조회";
        int num = 8;
        String api = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=1&base_date=" + base.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "&base_time=" + base.format(DateTimeFormatter.ofPattern("HHmm")) + "&nx=" + location.nx + "&ny=" + location.ny;
        JSONObject responseJson = getJson(api);
        JSONObject temp;
        JSONArray item = (JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item");
        System.out.println(item);
        for(int i = 0; i < num; ++i)
        {
            temp = (JSONObject)item.get(i);
            // System.err.println(temp.get("category") + " : " + temp.get("fcstValue"));
            if (temp.get("category") == "T1H")
                tmp = Integer.valueOf((String)(temp.get("fcstValue")));
            if (temp.get("category") == "RN1")
            {
                if (((String)(temp.get("fcstValue"))).chars().allMatch(Character::isDigit))
                    pcp = Integer.valueOf((String)(temp.get("fcstValue")));
                else
                    pcp = 0;
            }
        }
    }

    private void getWeather_shortTerm_2()
    {
        type = "초단기예보조회";
        int num = 10;
        int pageNo = 1;
        String api = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=" + pageNo + "&base_date=" + base.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "&base_time=" + base.format(DateTimeFormatter.ofPattern("HHmm")) + "&nx=" + location.nx + "&ny=" + location.ny;
        JSONObject responseJson = getJson(api);
        JSONObject temp;
        JSONArray item = (JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item");
        System.out.println(item);
        for(int i = 0; i < num; ++i)
        {
            temp = (JSONObject)item.get(i);
            System.err.println(temp.get("category") + " : " + temp.get("fcstValue"));
            if (temp.get("category") == "T1H")
                tmp = Integer.valueOf((String)(temp.get("fcstValue")));
            if (temp.get("category") == "RN1")
            {
                if (((String)(temp.get("fcstValue"))).chars().allMatch(Character::isDigit))
                    pcp = Integer.valueOf((String)(temp.get("fcstValue")));
                else
                    pcp = 0;
            }
        }
    }

    private void getWeather_shortTerm_3()
    {
        type = "단기예보조회";
        int num = 12;
        int pageNo = 1; // 1400의 예보라면 1페이지에 1500 2페이지에 1500식
        String api = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=" + pageNo + "&base_date=" + base.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "&base_time=" + base.format(DateTimeFormatter.ofPattern("HHmm")) + "&nx=" + location.nx + "&ny=" + location.ny;
        JSONObject responseJson = getJson(api);
        JSONObject temp;
        JSONArray item = (JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item");
        for(int i = 0; i < num; ++i)
        {
            temp = (JSONObject)item.get(i);
            if (temp.get("category") == "TMP")
                tmp = Integer.valueOf((String)(temp.get("fcstValue")));
            if (temp.get("category") == "POP")
                pop = Integer.valueOf((String)(temp.get("fcstValue")));
            if (temp.get("category") == "PCP")
            {
                if (((String)(temp.get("fcstValue"))).chars().allMatch(Character::isDigit))
                    pcp = Integer.valueOf((String)(temp.get("fcstValue")));
                else
                    pcp = 0;
            }
        }
    }

    private void getWeather_shortTerm_4()
    {
        type = "예보버전조회";
        //최근에 업데이트된 단기예보의 상태를 확인하는 용도로 보임 사용하지는 않을 듯
        int num = 12;
        int pageNo = 1; // 1400의 예보라면 1페이지에 1500 2페이지에 1500식
        String api = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getFcstVersion?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=" + pageNo + "&basedatetime=" + base.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")) + "&ftype=ODAM";
        JSONObject responseJson = getJson(api);
        JSONObject temp;
        System.out.println(responseJson);
        // JSONArray item = (JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item");
        // System.err.println(item);
        // for(int i = 0; i < num; ++i)
        // {
        //     temp = (JSONObject)item.get(i);
        //     System.err.println(temp.get("category") + " : " + temp.get("fcstValue"));
        //     if (temp.get("category") == "TMP")
        //         tmp = Integer.valueOf((String)(temp.get("fcstValue")));
        //     if (temp.get("category") == "POP")
        //         pop = Integer.valueOf((String)(temp.get("fcstValue")));
        //     if (temp.get("category") == "PCP")
        //     {
        //         if (((String)(temp.get("fcstValue"))).chars().allMatch(Character::isDigit))
        //             pcp = Integer.valueOf((String)(temp.get("fcstValue")));
        //         else
        //             pcp = 0;
        //     }
        // }
    }

    public static JSONObject getJson(String api)
    {
        JSONObject responseJson = null;
        try
        {
            URI uri = new URI(api);
            URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            int a;
            while ((a = br.read()) != -1) {
                sb.append((char)a);
            }
            br.close();
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(sb.toString());
            responseJson = (JSONObject)obj;
        }
        catch(Exception e)
        {
            System.out.println("error");
            System.out.println(e.getMessage());
        }
        return responseJson;
    }
}
