import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class midTermForecast {
    
    String type;
    Date fcst; //예보
    Date base; // 발표
    Location location;
    String serviceKey = new String("QD7zanvK2jd%2FgLcdz2nBxdtEq6Fysy0gY9Mz4YvPT7XizIYXPkOPvMwSeTHG%2BCDhQcuI5g%2BfE%2FU3u3NIY7lsFQ%3D%3D");
    public MidTermWeathr weathr[];
    
    public midTermForecast(Date _base, Location _location)
    {
        base = _base;
        location = _location;
        type = null;
        weathr = new MidTermWeathr[8];
    }

    public void getWeather_midTerm()
    {
        //날씨와 기온을 2,3이 제공하니 오늘에 대해서 이후의 예보를 한꺼번에 조회하는 형식으로 사용하는 것이 좋으며
        //2,3 각각 다른 regId를 사용하기 때문에 받을 때 두 종류를 받는 것이 좋을 것같다
        //단기 중기의 형식이 꽤나 달라서 아예 다른 클래스로 바꾸는 것이 졸다
        // getWeather_midTerm_1();
        getWeather_midTerm_2();
        // getWeather_midTerm_3();
    }

    private void getWeather_midTerm_1()
    {
        //text 형식의 예보라 필요하지 않음
        //만약 사용한다면 stnId로 다른 코드가 필요
        type = "중기전망조회";
        int num = 12;
        String api = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidFcst?dataType=JSON&serviceKey=" + serviceKey + "&numOfRows=" + num + "pageNo=1&stnId=" + location.regId + "&tmFc=" + base.date + base.time;
        System.out.println(api);
        JSONObject responseJson = getJson(api);
        System.out.println(responseJson);
    }

    private void getWeather_midTerm_2()
    {
        type = "중기육상예보조회";
        //강수 확률과 날씨(ex 구름많음 최대 10일)
        int num = 12;
        int pageNo = 1; // 1400의 예보라면 1페이지에 1500 2페이지에 1500식
        String api = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=" + pageNo + "&regId=" + location.regId + "&base_date=" + "&tmFc=" + base.date + base.time;
        JSONObject responseJson = getJson(api);
        JSONObject temp;
        JSONObject item = (JSONObject)((JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item")).get(0);
        System.err.println(item.get("wf8"));
        for(int i = 0; i < 5; ++i)
        {
            weathr[i].rnStAm = item.get("rnSt" + (i + 3) + "Am").toString();
            weathr[i].rnStPm = item.get("rnSt" + (i + 3) + "Pm").toString();
            weathr[i].wfAm =  item.get("wf" + (i + 3) + "Am").toString();
            weathr[i].wfPm = item.get("wf" + (i + 3) + "Pm").toString();
        }
        for(int i = 5; i < 7; ++i)
        {
            weathr[i].rnStAm = item.get("rnSt" + (i + 3)).toString();
            weathr[i].rnStPm = item.get("rnSt" + (i + 3)).toString();
            weathr[i].wfAm =  item.get("wf" + (i + 3)).toString();
            weathr[i].wfPm = item.get("wf" + (i + 3)).toString();
        }
        // for(int i = 0; i < num; ++i)
        // {
        //     temp = (JSONObject)item.get(i);
        //     System.err.println(temp);
        // }
    }

    private void getWeather_midTerm_3()
    {
        type = "중기기온조회";
        //예상기온
        //중기육상예보조회와 다른 rdgId를 사용함
        int num = 12;
        int pageNo = 1; // 1400의 예보라면 1페이지에 1500 2페이지에 1500식
        String api = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=" + pageNo + "&regId=" + location.regId + "&base_date=" + "&tmFc=" + base.date + base.time;
        JSONObject responseJson = getJson(api);
        JSONObject temp;
        System.err.println(responseJson);
        // JSONArray item = (JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item");
        // System.err.println(item);
        // for(int i = 0; i < num; ++i)
        // {
        //     temp = (JSONObject)item.get(i);
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

    private void getWeather_midTerm_4()
    {
        type = "중기해상예보조회";
        //regid 형식이 다름
        //바다의 날씨를 보는 것이니 크게 필요하지 않을 것
        int num = 12;
        int pageNo = 1; // 1400의 예보라면 1페이지에 1500 2페이지에 1500식
        String api = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidSeaFcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=" + pageNo + "&regId=" + location.regId + "&base_date=" + "&tmFc=" + base.date + base.time;
        JSONObject responseJson = getJson(api);
        JSONObject temp;
        System.err.println(responseJson);
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
