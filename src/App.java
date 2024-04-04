
public class App {
        public static void main(String[] args)
        {
                // System.out.println("한글");
                Weather weather = new Weather(new Date("20240404", "0600"), new Location(55, 127, "11D20501"));
                weather.getWeather();
	}	
}
//-Dfile.encoding=UTF-8 컴파일시 추가