package com.game.service;

import com.game.controller.*;
import com.game.entity.*;
import com.game.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PlayerServiceImpl implements PlayerService {
    private int countPlayers=0;

    @Override
    public int getCountPlayers() {
        return countPlayers;
    }

    public void setCountPlayers(int countPlayers) {
        this.countPlayers = countPlayers;
    }

    @Autowired
    PlayerDao playerDao;

    @Override
    public boolean create(Player player) {

        int level = (int) ((Math.sqrt((2500 + player.getExperience()*200))-50)/100);
        int untilNextLevel=50*(level+1)*(level+2)-player.getExperience();
        player.setLevel(level);
        player.setUntilNextLevel(untilNextLevel);

        if(player.getBanned()==null){
            player.setBanned(false);
        }
        if (!validation(player)){
            System.out.println("Валидация не пройдена");
            return false;
        }
        playerDao.save(player);
        return true;

//        final long playerId = CLIENT_ID_HOLDER.incrementAndGet();
//        player.setId(playerId);
//        CLIENT_REPOSITORY_MAP.put(playerId, player);
    }

    @Override
    public List<Player> readAll() {
        return playerDao.findAll();
    }

    @Override
    public List<Player> readAllOrdered(PlayerOrder order, PlayerFilter playerFilter, String pageNumber, String limit) {
        int pageNumber1 = Integer.parseInt(pageNumber);
        int limit1 = Integer.parseInt(limit);
        List<Player> list = playerDao.findAll();
        Stream<Player> stream = list.stream();
        if(playerFilter!=null){

            if(playerFilter.getName()!=null){
                stream = stream.filter((s) -> s.getName().contains(playerFilter.getName()));
            }
            if(playerFilter.getTitle()!=null){
                stream = stream.filter((s) -> s.getTitle().contains(playerFilter.getTitle()));
            }
            if(playerFilter.getProfession()!=null){
                stream = stream.filter((s) -> s.getProfession().equals(playerFilter.getProfession()));
            }
            if(playerFilter.getRace()!=null){
                stream = stream.filter((s) -> s.getRace().equals(playerFilter.getRace()));
            }
            if(playerFilter.getBefore()!=null){
                stream = stream.filter((s) -> s.getBirthday().before(playerFilter.getBefore()));
            }
            if(playerFilter.getAfter()!=null){
                stream = stream.filter((s) -> s.getBirthday().after(playerFilter.getAfter()));
            }
            if(playerFilter.getMinExperience()!=null){
                stream = stream.filter((s) -> s.getExperience() >= (playerFilter.getMinExperience()));
            }
            if(playerFilter.getMaxExperience()!=null){
                stream = stream.filter((s) -> s.getExperience() <= (playerFilter.getMaxExperience()));
            }
            if(playerFilter.getMinLevel()!=null){
                stream = stream.filter((s) -> s.getLevel() >= (playerFilter.getMinLevel()));
            }
            if(playerFilter.getMaxLevel()!=null){
                stream = stream.filter((s) -> s.getLevel() <= (playerFilter.getMaxLevel()));
            }
            if(playerFilter.isBanned()){
                stream = stream.filter((s) -> s.getBanned() ==true);
            }
        }
        list = stream.collect(Collectors.toList());
        setCountPlayers(list.size());
        switch (order){
            case ID: list =list.stream().sorted(new Comparator<Player>() {
                @Override
                public int compare(Player o1, Player o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            }).skip(limit1*pageNumber1).limit(limit1)
                    .collect(Collectors.toList());
            break;
            case NAME: list =list.stream().sorted(new Comparator<Player>() {
                @Override
                public int compare(Player o1, Player o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            }).skip(limit1*pageNumber1).limit(limit1)
                    .collect(Collectors.toList());
            break;
            case EXPERIENCE: list =list.stream().sorted(new Comparator<Player>() {
                @Override
                public int compare(Player o1, Player o2) {
                    return o1.getExperience().compareTo(o2.getExperience());
                }
            }).skip(limit1*pageNumber1).limit(limit1)
                    .collect(Collectors.toList());
            break;
            case BIRTHDAY: list =list.stream().sorted(new Comparator<Player>() {
                @Override
                public int compare(Player o1, Player o2) {
                    return o1.getBirthday().compareTo(o2.getBirthday());
                }
            }).skip(limit1*pageNumber1).limit(limit1)
                    .collect(Collectors.toList());
            break;
        }
        return list;
    }

    @Override
    public Player read(long id) {
        return playerDao.findPlayerById(id);
    }

    @Override
    public boolean update(Player player, long id) {


        if (playerDao.existsById(id)) {
            Player playerFound = playerDao.findPlayerById(id);
            if(playerFound!=null){
                playerFound.setName(player.getName()!=null? player.getName():playerFound.getName());
                playerFound.setTitle(player.getTitle()!=null? player.getTitle():playerFound.getTitle());
                playerFound.setRace(player.getRace()!=null? player.getRace():playerFound.getRace());
                playerFound.setProfession(player.getProfession()!=null? player.getProfession():playerFound.getProfession());
                playerFound.setBirthday(player.getBirthday()!=null? player.getBirthday():playerFound.getBirthday());
                playerFound.setBanned(player.getBanned()!=null? player.getBanned():playerFound.getBanned());
                playerFound.setExperience(player.getExperience()!=null? player.getExperience():playerFound.getExperience());

                int level = (int) ((Math.sqrt((2500 + playerFound.getExperience()*200))-50)/100);
                int untilNextLevel=50*(level+1)*(level+2)-playerFound.getExperience();
                playerFound.setLevel(level);
                playerFound.setUntilNextLevel(untilNextLevel);

                if (!validation(playerFound)){
                    System.out.println("Валидация не пройдена");
                    return false;
                }

                playerDao.saveAndFlush(playerFound);
                return true;
            }
//            player.setId(id);
//            CLIENT_REPOSITORY_MAP.put(id, player);
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        //return CLIENT_REPOSITORY_MAP.remove(id) != null;
        if(playerDao.existsById(id)){
            playerDao.delete(playerDao.findPlayerById(id));
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean validation(Player player) {
        try {
            if(player.getName()==null||player.getTitle()==null||player.getExperience()==null||player.getProfession()==null||player.getRace()==null||player.getBirthday()==null){
                return false;
            }
            if(player.getName().isEmpty()){
                return false;
            }

            if (player.getName().length() > 12) {
                System.out.println("Имя длиннее 12 символов");
                return false;
            }
            if (player.getTitle().length() > 30) {
                System.out.println("Титул длиннее 30 символов");
                return false;
            }
            if (player.getExperience() < 0 || player.getExperience() > 10000000) {
                System.out.println("Опыт не укладывается в диапазон 0 .. 10 000 000");
                return false;
            }
            if (player.getBirthday().after(new SimpleDateFormat("dd.MM.yyyy").parse("01.01.3001")))
            {
                System.out.println("ДР больше 01.01.3001");
                return false;
            }
            if(player.getBirthday().before(new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2000"))){
                System.out.println("ДР меньше 01.01.2000");
                return false;
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
        return true;
    }


}
