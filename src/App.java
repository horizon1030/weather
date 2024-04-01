
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
public class App {
	public static void main(String[] args)
	{
        //data yymmdd
        //time 3시간 간격으로 예보가 올라오는 듯하며 18시의 예보를 보고싶다면 그보다 전의 시간(ex 1759)를 넣어야함
        getWeather("20240331", "1100", "55", "127");
	}	

    public static String getWeather(String date, String time, String nx, String ny)
    {`
        URL url;
        URI uri;
		HttpURLConnection con = null;
        String s = null;
        String serviceKey = new String("QD7zanvK2jd%2FgLcdz2nBxdtEq6Fysy0gY9Mz4YvPT7XizIYXPkOPvMwSeTHG%2BCDhQcuI5g%2BfE%2FU3u3NIY7lsFQ%3D%3D");
        String api = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=10&pageNo=1&base_date=" + date + "&base_time=" + time + "&nx=" + nx + "&ny=" + ny;
        System.out.println(api);
        try
        {
            uri = new URI(api);
            url = uri.toURL();
			con = (HttpURLConnection)url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(sb.toString());
            JSONObject responseJson = (JSONObject) obj;
            System.out.println(responseJson);
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return s;
    }
}
