import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class Weather {
    //�����Ǵ� �ð��� ��¥�� �߰����� ���� ���� �ʿ�
    //�߱� ������ �ܱ� ������ �� 4���� Ȯ���ϰ� ���� ������ ���� ������ �ʿ䰡 ����
    int tmp; // �µ�
    int pop; // ����Ȯ��
    int pcp; // ������
    String serviceKey = new String("QD7zanvK2jd%2FgLcdz2nBxdtEq6Fysy0gY9Mz4YvPT7XizIYXPkOPvMwSeTHG%2BCDhQcuI5g%2BfE%2FU3u3NIY7lsFQ%3D%3D");
    
    public int getTmp(){return tmp;}
    public int getPop(){return pop;}
    public int getpcp(){return pcp;}
    public void getWeather(String date, String time, String nx, String ny)
    //data yymmdd time hhmm
    //time 3�ð� �������� ������ �ö���� ���ϸ� 18���� ������ ����ʹٸ� �׺��� ���� �ð�(ex 1759)�� �־����
    {
        getWeather_shortTerm(date, time, nx, ny);
        // getWeather_2(date, time, "108");
    }

    private void getWeather_shortTerm(String date, String time, String nx, String ny)
    {
        int num = 12;
        //�ܱ⿹�� ��ȸ
        String api = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=" + serviceKey + "&dataType=JSON&numOfRows=" +  Integer.toString(num) + "&pageNo=1&base_date=" + date + "&base_time=" + time + "&nx=" + nx + "&ny=" + ny;
        JSONObject responseJson = getJson(api);
        JSONObject temp;
        JSONArray item = (JSONArray)((JSONObject)((JSONObject)((JSONObject)responseJson.get("response")).get("body")).get("items")).get("item");
        for(int i = 0; i < num; ++i)
        {
            temp = (JSONObject)item.get(i);
            // System.err.println(temp.get("category") + " : " + temp.get("fcstValue"));
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
    private void getWeather_2(String date, String time, String regld)
    {
        //�߱����� ��ȸ
        //text ������ ������ �ʿ����� ����
        int num = 12;
        //�߱⿹�� ��ȸ
        String api = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidFcst?serviceKey=" + serviceKey + "&numOfRows=10&pageNo=1&stnId=133&tmFc=202404020600";
        // System.out.println(api);
        JSONObject responseJson = getJson(api);
        // System.out.println(responseJson);
    }
    private static JSONObject getJson(String api)
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
            // String a;
            // while ((a = br.readLine()) != null) {
            //     sb.append(a);
            // }
            br.close();
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(sb.toString());
            responseJson = (JSONObject)obj;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        System.out.println(responseJson);
        return responseJson;
    }
}
