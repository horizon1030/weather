
public class App {
        public static void main(String[] args)
        {
                // System.out.println("한글");
                ShortTermForeacast weather = new ShortTermForeacast(new Date("20240404", "0600"), new Location(55, 127, "11D20501"));
                weather.getWeather();
                midTermForecast midTerm = new midTermForecast(new Date("20240404", "0600"), new Location(55, 127, "11B00000"));
                midTerm.getWeather_midTerm();
	}	
}
//-Dfile.encoding=UTF-8 컴파일시 추가