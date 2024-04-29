import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import org.json.simple.*;

public class ShortTermForeacast {
    Location location;
    String serviceKey = new String("QD7zanvK2jd%2FgLcdz2nBxdtEq6Fysy0gY9Mz4YvPT7XizIYXPkOPvMwSeTHG%2BCDhQcuI5g%2BfE%2FU3u3NIY7lsFQ%3D%3D");

    public ShortTermForeacast(Location _location)
    {
        location = _location;
    }

    public void setLocation(Location _location){location = _location;}
    
    // public void getWeather()
    // {
    //     // getWeather_shortTerm_1();
    //     // getWeather_shortTerm_2();
    //     getWeather_shortTerm_3();
    //     // getWeather_shortTerm_4();
    // }

    // private void getWeather_shortTerm_1()
    // {
    //     type = "초단기실황조회";
    //     int num = 8;
    //     String api = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=1&base_date=" + base.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "&base_time=" + base.format(DateTimeFormatter.ofPattern("HHmm")) + "&nx=" + location.nx + "&ny=" + location.ny;
    //     JSONObject responseJson = GetJson.getJson(api);
    //     JSONObject temp;
    //     JSONArray item = (JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item");
    //     System.out.println(item);
    //     for(int i = 0; i < num; ++i)
    //     {
    //         temp = (JSONObject)item.get(i);
    //         // System.err.println(temp.get("category") + " : " + temp.get("fcstValue"));
    //         if (temp.get("category") == "T1H")
    //             tmp = Integer.valueOf((String)(temp.get("fcstValue")));
    //         if (temp.get("category") == "RN1")
    //         {
    //             if (((String)(temp.get("fcstValue"))).chars().allMatch(Character::isDigit))
    //                 pcp = Integer.valueOf((String)(temp.get("fcstValue")));
    //             else
    //                 pcp = 0;
    //         }
    //     }
    // }

    // private void getWeather_shortTerm_2()
    // {
    //     type = "초단기예보조회";
    //     int num = 10;
    //     int pageNo = 1;
    //     String api = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" + num + "&pageNo=" + pageNo + "&base_date=" + base.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "&base_time=" + base.format(DateTimeFormatter.ofPattern("HHmm")) + "&nx=" + location.nx + "&ny=" + location.ny;
    //     JSONObject responseJson = GetJson.getJson(api);
    //     JSONObject temp;
    //     JSONArray item = (JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item");
    //     System.out.println(item);
    //     for(int i = 0; i < num; ++i)
    //     {
    //         temp = (JSONObject)item.get(i);
    //         System.err.println(temp.get("category") + " : " + temp.get("fcstValue"));
    //         if (temp.get("category") == "T1H")
    //             tmp = Integer.valueOf((String)(temp.get("fcstValue")));
    //         if (temp.get("category") == "RN1")
    //         {
    //             if (((String)(temp.get("fcstValue"))).chars().allMatch(Character::isDigit))
    //                 pcp = Integer.valueOf((String)(temp.get("fcstValue")));
    //             else
    //                 pcp = 0;
    //         }
    //     }
    // }

    public ShortTermWeather[] getWeather()
    {
        //단기예보조회
        //base_time 구하기
        LocalDateTime base = getTime();
        Vector<ShortTermWeather> weatherVector = new Vector<ShortTermWeather>();
        String api = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=9999&pageNo=1&base_date=" + base.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "&base_time=" + base.format(DateTimeFormatter.ofPattern("HHmm")) + "&nx=" + location.nx + "&ny=" + location.ny;
        JSONObject responseJson = GetJson.getJson(api);
        JSONArray item = (JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item");
        JSONObject tempJson = (JSONObject)item.get(0);
        ShortTermWeather tempWeather = getNewShortTermForeacast(tempJson, base);
        int sky = 0;
        int pty = 0;
        for(int i = 0; i < 999; ++i)
        {
            try{tempJson = (JSONObject)item.get(i);}
            catch(Exception e){break;}
            if (!tempWeather.fcst.format(DateTimeFormatter.ofPattern("HHmm")).equals(tempJson.get("fcstTime")))
            {
                weatherVecotrADD(weatherVector, tempWeather, sky, pty);
                sky = 0;
                pty = 0;
                weatherVector.add(tempWeather);
                tempWeather = getNewShortTermForeacast(tempJson, base);
            }
            if (tempJson.get("category").equals("TMP"))
                tempWeather.tmp = Integer.valueOf((String)(tempJson.get("fcstValue")));
            else if (tempJson.get("category").equals("POP"))
                tempWeather.pop = Integer.valueOf((String)(tempJson.get("fcstValue")));
            else if (tempJson.get("category").equals("PCP"))
                tempWeather.pcp = (String)(tempJson.get("fcstValue"));
            else if (tempJson.get("category").equals("SKY"))
                sky = Integer.parseInt((String)(tempJson.get("fcstValue")));
            else if (tempJson.get("category").equals("PTY"))
                pty = Integer.parseInt((String)(tempJson.get("fcstValue")));
        }
        weatherVecotrADD(weatherVector, tempWeather, sky, pty);
        ShortTermWeather weather[] = new ShortTermWeather[weatherVector.size()];
        for(int i = 0; i < weatherVector.size(); ++i){weather[i] = weatherVector.get(i);}
        return weather;
    }

    private LocalDateTime getTime()
    {
        LocalDateTime base = LocalDateTime.now();
        if (base.getHour() % 3 != 2) {base = base.minusHours(base.getHour() % 3 + 1);}
        else if (base.getMinute() < 10){base = base.minusHours(3);}
        base = base.withMinute(0);
        return base;
    }

    private ShortTermWeather getNewShortTermForeacast(JSONObject tempJson, LocalDateTime base)
    {
        ShortTermWeather tempWeather = new ShortTermWeather();
        tempWeather.fcst = LocalDateTime.of( Integer.parseInt((String)tempJson.get("fcstDate")) / 10000
                                            , ( Integer.parseInt((String)tempJson.get("fcstDate")) % 10000) / 100
                                            , ( Integer.parseInt((String)tempJson.get("fcstDate")) % 10000) % 100
                                            ,  Integer.parseInt((String)tempJson.get("fcstTime")) / 100, 0);
        tempWeather.base = base;
        return tempWeather;
    }

    private void weatherVecotrADD(Vector<ShortTermWeather> weatherVector, ShortTermWeather tempWeather, int sky, int pty)
    {
        if (sky == 0)
            tempWeather.wCode = WeatherCode.sunny;
        else if (sky == 3)
        {
            switch (pty) {
                case 0: tempWeather.wCode = WeatherCode.cloud; break;
                case 1: tempWeather.wCode = WeatherCode.cloudRain; break;
                case 2: tempWeather.wCode = WeatherCode.cloudRS; break;
                case 3: tempWeather.wCode = WeatherCode.cloudSnow; break;
                case 4: tempWeather.wCode = WeatherCode.cloudShower; break;
                default: break;
            }
        }
        else
        {
            switch (pty) {
                case 0: tempWeather.wCode = WeatherCode.cloudy; break;
                case 1: tempWeather.wCode = WeatherCode.cloudyRain; break;
                case 2: tempWeather.wCode = WeatherCode.cloudyRS; break;
                case 3: tempWeather.wCode = WeatherCode.cloudySnow; break;
                case 4: tempWeather.wCode = WeatherCode.cloudyShower; break;
                default: break;
            }
        }
        weatherVector.add(tempWeather);
    }
}
