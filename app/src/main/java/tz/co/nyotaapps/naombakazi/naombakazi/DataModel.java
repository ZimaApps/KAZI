package tz.co.nyotaapps.naombakazi.naombakazi;


public class DataModel {

    String simcard;
    String name;
    String umri;
    String area;
    String maelezo;
    String pichasource;
    String aspectratio;
    String love;
    String thenumber;
    String country;


    public DataModel(String simcard, String name, String umri, String area, String maelezo, String pichasource, String aspectratio, String love,String thenumber,String country)
    {
        this.simcard=simcard;
        this.name=name;
        this.umri=umri;
        this.area=area;
        this.maelezo=maelezo;
        this.pichasource=pichasource;
        this.aspectratio=aspectratio;
        this.love=love;
        this.thenumber=thenumber;
        this.country=country;

    }

    public String getsimcard() {
        return simcard;
    }

    public String getname() {
        return name;
    }

    public String getumri() {
        return umri;
    }

    public String getarea() {
        return area;
    }

    public String getmaelezo() {
        return maelezo;
    }

    public String getpichasource() {
        return pichasource;
    }

    public String getaspectratio() {
        return aspectratio;
    }

    public String getlove() {
        return love;
    }

    public String thenumber() {
        return thenumber;
    }

    public String country() {
        return country;
    }

}
