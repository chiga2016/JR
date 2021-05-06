package com.game.controller;

import com.game.entity.*;
import com.game.service.PlayerService;
import com.game.util.ComparePlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@RestController
public class PlayerController {
        private final PlayerService playerService;


    public PlayerService getPlayerService() {
        return playerService;
    }
    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

//Not found GET /rpg_war_exploded/rest/players?
// name=player1&title=p1&after=535852800000&pageNumber=0&pageSize=3&order=ID
    @PostMapping(value = "/rest/players")
    public ResponseEntity<?> create(@RequestBody Player player) {
        boolean result = playerService.create(player);
        //return new ResponseEntity<>(HttpStatus.OK);
        return result
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/rest/players")
    public ResponseEntity<List<Player>> read(
            @RequestParam(name = "pageNumber", defaultValue = "0") String pageNumber,
            @RequestParam(name = "order", defaultValue = "ID") String order,
            @RequestParam(name = "pageSize", defaultValue = "3") String pageSize,

            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "race", defaultValue = "") String race,
            @RequestParam(name = "profession", defaultValue = "") String profession,
            @RequestParam(name = "after", defaultValue = "") String after,
            @RequestParam(name = "before", defaultValue = "") String before,
            @RequestParam(name = "banned", defaultValue = "") String banned,
            @RequestParam(name = "minExperience", defaultValue = "") String minExperience,
            @RequestParam(name = "maxExperience", defaultValue = "") String maxExperience,
            @RequestParam(name = "minLevel", defaultValue = "") String minLevel,
            @RequestParam(name = "maxLevel", defaultValue = "") String maxLevel
    ) {
        PlayerFilter playerFilter=null;
    if(!name.isEmpty()||!title.isEmpty()||!race.isEmpty()||!profession.isEmpty()||!after.isEmpty()||!before.isEmpty()||!banned.isEmpty()||!minExperience.isEmpty()||!maxExperience.isEmpty()||!minLevel.isEmpty()||!maxLevel.isEmpty()){
        playerFilter = new PlayerFilter();
        if(!name.isEmpty()){
            playerFilter.setName(name);
        }
        if(!title.isEmpty()){
            playerFilter.setTitle(title);
        }
        if(!race.isEmpty()){
            playerFilter.setRace(Race.valueOf(race));
        }
        if(!profession.isEmpty()){
            playerFilter.setProfession(Profession.valueOf(profession));
        }
        if(!after.isEmpty()){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Long.parseLong(after));
            playerFilter.setAfter(c.getTime());
        }
        if(!before.isEmpty()){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Long.parseLong(before));
            playerFilter.setBefore(c.getTime());
        }
        if(!banned.isEmpty()){
            playerFilter.setBanned(banned);
        }
        if(!minExperience.isEmpty()){
            playerFilter.setMinExperience(Integer.parseInt(minExperience));
        }
        if(!maxExperience.isEmpty()){
            playerFilter.setMaxExperience(Integer.parseInt(maxExperience));
        }
        if(!minLevel.isEmpty()){
            playerFilter.setMinLevel(Integer.parseInt(minLevel));
        }
        if(!maxLevel.isEmpty()){
            playerFilter.setMaxLevel(Integer.parseInt(maxLevel));
        }
    }
        //final List<Player> clients = playerService.readAll();
        System.out.println(playerFilter);
        List<Player> resultList = playerService.readAllOrdered(PlayerOrder.valueOf(order), playerFilter, pageNumber, pageSize );


//        System.out.println("pageNumber " + pageNumber);
//        System.out.println("order " + order);
//        System.out.println("pageSize " + pageSize);
//        resultList = (new ComparePlayer()).sortPlayers(clients, PlayerOrder.valueOf(order),pageSize );

//        return resultList != null &&  !resultList.isEmpty()
//                ? new ResponseEntity<>(resultList, HttpStatus.OK)
//                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return resultList != null &&  !resultList.isEmpty()
                ? new ResponseEntity<>(resultList, HttpStatus.OK)
                : new ResponseEntity<>(new ArrayList<Player>(),HttpStatus.OK);
    }

    @GetMapping(value = "/rest/players/{id}")
    public ResponseEntity<Player> read(@PathVariable(name = "id") int id) {
        final Player player = playerService.read(id);

        return player != null
                ? new ResponseEntity<>(player, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/rest/players/count")
    public Integer getCount() {
        //List<Player> listPlayers = playerService.readAll();
        //return listPlayers.size();
        return playerService.getCountPlayers();
    }

    @PostMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody Player player) {
        final boolean updated = playerService.update(player, id);

//        return updated
//                ? new ResponseEntity<>(HttpStatus.OK)
//                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        final boolean deleted = playerService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
