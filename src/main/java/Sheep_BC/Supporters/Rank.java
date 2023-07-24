package Sheep_BC.Supporters;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Rank implements Comparable<Rank>, Serializable {

    static final long serialVersionUID = 1L;
    private String name;
    private BigDecimal number;

    public  Rank(String name,double number){
        this.name = name;
        this.number = new BigDecimal(String.valueOf(number));
    }

    public Rank(String name, BigDecimal number){
        this.name = name;
        this.number = number;
    }

    public Rank(String name, BigInteger number){
        this.name = name;
        this.number = new BigDecimal(number.toString());
    }

    public String getname(){
        return this.name;
    }
    public BigDecimal getNumber(){
        return this.number;
    }
    public void addNumber(double number){
        this.number = this.number.add(new BigDecimal(String.valueOf(number)));
    }

    @Override
    public int compareTo(Rank user) {
        return name.compareTo(user.getname());
    }

    public static ArrayList<Rank> getRankList(String[] list, double number){
        ArrayList<Rank> ranks = new ArrayList<Rank>();
        for(String name:list){
            ranks.add(new Rank(name,0));
        }
        for(int i = 0;i<number;i++){
            ranks.get((int)(Math.random()*ranks.size())).addNumber(1);
        }

        Collections.sort(ranks, new Comparator<Rank>() {
            @Override
            public int compare(Rank o1, Rank o2) {
                if(o1.getNumber().max(o2.getNumber()).equals(o1.getNumber())){
                    return -1;
                }else if(o1.getNumber().min(o2.getNumber()).equals(o1.getNumber())){
                    return 1;
                }else {
                    return 0;
                }
            }
        });
        return ranks;
    }

    public static long getRankSize(ArrayList<Rank> ranks){
        long size = 0L;
        for(Rank rank: ranks){
            if(!rank.getNumber().equals(BigDecimal.ZERO)){
                size++;
            }
        }
        return size;
    }

}
