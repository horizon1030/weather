public class MidTermWeathr {
    public String wfAm;
    public String wfPm;
    public String rnStAm;
    public String rnStPm;

    public MidTermWeathr(){}
    public MidTermWeathr(String _wfAm, String _wfPm, String _rnStAm, String _rnStPm)
    {
        wfAm = _wfAm;//날씨
        wfPm = _wfPm;
        rnStAm = _rnStAm;//강수확률
        rnStPm = _rnStPm;
    }
}
