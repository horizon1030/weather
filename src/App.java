
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import netscape.javascript.JSObject;
public class App {
	public static void main(String[] args)
	{
        //data yymmdd
        //time 3시간 간격으로 예보가 올라오는 듯하며 18시의 예보를 보고싶다면 그보다 전의 시간(ex 1759)를 넣어야함
        getWeather("20240402", "1100", "55", "127");
	}	

    public static String getWeather(String date, String time, String nx, String ny)
    {
        String s = null;
        String serviceKey = new String("QD7zanvK2jd%2FgLcdz2nBxdtEq6Fysy0gY9Mz4YvPT7XizIYXPkOPvMwSeTHG%2BCDhQcuI5g%2BfE%2FU3u3NIY7lsFQ%3D%3D");
        String api = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=10&pageNo=1&base_date=" + date + "&base_time=" + time + "&nx=" + nx + "&ny=" + ny;
        //단기예보 조회
        // System.out.println(api);
        JSONObject responseJson = getJson(api);
        // System.out.println(responseJson);
        JSONArray asd = (JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item");
        System.out.println(asd.get(0));
        return s;
    }

    public static JSONObject getJson(String api)
    {
        JSONObject responseJson = null;
        try
        {
            URI uri = new URI(api);
            URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestProperty("Content-Type", "application/json;UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            int a;
            while ((a = br.read()) != -1) {
                sb.append((char)a);
            }
            br.close();
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(sb.toString());
            responseJson = (JSONObject) obj;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return responseJson;
    }
}
