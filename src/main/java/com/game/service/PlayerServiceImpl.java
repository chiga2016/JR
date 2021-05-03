package com.game.service;

import com.game.controller.*;
import com.game.entity.*;
import com.game.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    // Хранилище клиентов
    //private static final Map<Long, Player> CLIENT_REPOSITORY_MAP = new HashMap<>();

    // Переменная для генерации ID клиента
    //private static final AtomicInteger CLIENT_ID_HOLDER = new AtomicInteger();

    @Autowired
    PlayerDao playerDao;

    @Override
    public void create(Player player) {

        int level = (int) ((Math.sqrt((2500 + player.getExperience()*200))-50)/100);
        int untilNextLevel=50*(level+1)*(level+2)-player.getExperience();
        player.setLevel(level);
        player.setUntilNextLevel(untilNextLevel);

        if(player.getBanned()==null){
            player.setBanned(false);
        }
        playerDao.save(player);
//        final long playerId = CLIENT_ID_HOLDER.incrementAndGet();
//        player.setId(playerId);
//        CLIENT_REPOSITORY_MAP.put(playerId, player);
    }

    @Override
    public List<Player> readAll() {
        return playerDao.findAll();
    }

    @Override
    public List<Player> readAllOrdered(PlayerOrder order, String pageNumber, String limit) {
        int pageNumber1 = Integer.parseInt(pageNumber);
        int limit1 = Integer.parseInt(limit);
        List<Player> list = playerDao.findAll();
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
}
