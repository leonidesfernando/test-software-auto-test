package br.com.home.lab.softwaretesting.automation.util;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

@UtilityClass
public class DataGen {

    private final Faker faker = new Faker();

    public String strDate(){
        int month = getMonth();
        return String.format("%s/%s/%s",
                toDateFormat(getDayByMonth(month)), toDateFormat(month), getYear());
    }

    public String strDateCurrentMonthEnglisFormat(){
        var now = LocalDate.now();
        int month = now.getMonth().getValue();
        int year = now.getYear();
        return String.format("%s-%s-%s",
                year, toDateFormat(month), toDateFormat(getDayByMonth(month)));
    }

    public String strDateCurrentMonth(){
        var now = LocalDate.now();
        int month = now.getMonth().getValue();
        int year = now.getYear();
        return String.format("%s/%s/%s",
                toDateFormat(getDayByMonth(month)), toDateFormat(month), year);
    }

    private String toDateFormat(int value){
        if(value < 10)
            return "0"+value;
        return ""+value;
    }

    private int getMonth(){
        return number(12);
    }

    private int getDayByMonth(int month){
        var months31Days = List.of(1,3,5,7,8,10,12);

        int day = 0;
        while(day == 0){
            day = month == 2 ? number(28) :
                    months31Days.contains(month) ? number(31) : number(30);
        }
        return day;
    }

    private int getYear(){
        return number(1995, LocalDate.now().getYear());
    }

    private int getCurrentYear(){
        return LocalDate.now().getYear();
    }

    public Date date(){
        int month = getMonth();
        return dateByMonth(month);
    }

    public Date dateCurrentMonth(){
        int month = LocalDate.now().getMonth().getValue();
        return dateByMonth(month);
    }

    private Date dateByMonth(int month){
        int day = getDayByMonth(month);
        int year = getCurrentYear();
        return new GregorianCalendar(year, month-1, day).getTime();
    }

    public String productName(){
        return faker.commerce().productName();
    }

    public int number(int min, int max){
        return getRandom().nextInt(min, max+1);
    }

    public double moneyValue(){
        return moneyValue(700);
    }

    public double moneyValue(double max){
        return getRandom().nextDouble(max);
    }

    public int number(int max){
        return getRandom().nextInt(0, max+1);
    }

    private Random getRandom(){
        return new Random();
    }
}
