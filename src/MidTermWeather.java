import java.time.LocalDateTime;

public class MidTermWeather {
    public WeatherCode wfAm;
    public WeatherCode wfPm;
    public String rnStAm;
    public String rnStPm;
    public LocalDateTime base;
    public LocalDateTime fcst;

    public MidTermWeather(){}
    public MidTermWeather(WeatherCode _wfAm, WeatherCode _wfPm, String _rnStAm, String _rnStPm)
    {
        wfAm = _wfAm;//날씨
        wfPm = _wfPm;
        rnStAm = _rnStAm;//강수확률
        rnStPm = _rnStPm;
    }
}
