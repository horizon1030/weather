import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetJson {
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
            System.out.println(responseJson);
            System.err.println(api);
        }
        return responseJson;
    }
}
