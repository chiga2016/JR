package com.game.util;

import com.game.controller.PlayerOrder;
import com.game.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ComparePlayer {


    public  Comparator<Player> myComparatorById = new Comparator<Player>() {
        public int compare(Player o1, Player o2) {
            int result = o2.getId().compareTo(o1.getId());
//            if (result == 0) {
//                return (int) (o1.getId() - o2.getId());
//            }
            return result;
        }
    };

    public  Comparator<Player> myComparatorByName = new Comparator<Player>() {
        public int compare(Player o1, Player o2) {
            int result = o2.getName().compareTo(o1.getName());
            if (result == 0) {
                return (int) (o1.getId() - o2.getId());
            }
            return result;
        }
    };

    public  Comparator<Player> myComparatorByExperience = new Comparator<Player>() {
        public int compare(Player o1, Player o2) {
            int result = o2.getExperience().compareTo(o1.getExperience());
            if (result == 0) {
                return (int) (o1.getId() - o2.getId());
            }
            return result;
        }
    };

    public  Comparator<Player> myComparatorByBirthday = new Comparator<Player>() {
        public int compare(Player o1, Player o2) {
            int result = o2.getBirthday().compareTo(o1.getBirthday());
            if (result == 0) {
                return (int) (o1.getId() - o2.getId());
            }
            return result;
        }
    };

    public List<Player> sortPlayers(List<Player> playerList, PlayerOrder order, String limit){
        List<Player> resultList = playerList;
        resultList = resultList.stream().limit(Long.parseLong(limit)).collect(Collectors.toList());
        System.out.println("Метод сортировки " + order + " " + limit);
        if(order==PlayerOrder.ID){
            System.out.println("Сортируем по ИД");
            Collections.sort(playerList, myComparatorById);
        } else if (order==PlayerOrder.NAME){
            System.out.println("Сортируем по NAME");
            Collections.sort(playerList, myComparatorByName);
        } else if (order==PlayerOrder.EXPERIENCE){
            System.out.println("Сортируем по EXPERIENCE");
            Collections.sort(playerList, myComparatorByExperience);
        } else if (order==PlayerOrder.BIRTHDAY){
            System.out.println("Сортируем по BIRTHDAY");
            Collections.sort(playerList, myComparatorByBirthday);
        }
        return resultList;
    }


}
