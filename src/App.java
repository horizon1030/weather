
public class App {
        public static void main(String[] args)
        {
                // System.out.println("한글");
                ShortTermForeacast weather = new ShortTermForeacast(new Date("20240405", "0600"), new Location(55, 127, "11D20501"));
                weather.getWeather();
                System.out.println("tmp 온도 : " + weather.tmp);
                System.out.println("pop 강수확률 : " + weather.pop);
                System.out.println("pcp 강수량 : " + weather.pcp);
                midTermForecast midTerm = new midTermForecast(new Date("20240405", "0600"), new Location(55, 127, "11B00000"));
                midTerm.getWeather_midTerm();
                for(int i = 0; i < 8; ++i)
                {
                        System.out.println((i + 3) + "일 후 오전 강수확률 :" + midTerm.weathr[i].rnStAm);
                        System.out.println((i + 3) + "일 후 오전 날씨 :" + midTerm.weathr[i].wfAm);
                        System.out.println((i + 3) + "일 후 오후 강수확률 :" + midTerm.weathr[i].rnStPm);
                        System.out.println((i + 3) + "일 후 오후 날씨 :" + midTerm.weathr[i].wfPm);
                }
	}	
} 
//-Dfile.encoding=UTF-8 한글 꺠지면 컴파일시 추가