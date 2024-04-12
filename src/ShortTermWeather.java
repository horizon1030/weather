import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class ShortTermWeather {
    public int tmp; // 온도
    public int pop; // 강수확률
    public int pcp; // 강수량
    public LocalDateTime fcst; //예보
    public LocalDateTime base; // 발표
}
