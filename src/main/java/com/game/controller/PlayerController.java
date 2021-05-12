package com.game.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.game.entity.*;
import com.game.service.PlayerService;
import com.game.util.ComparePlayer;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        //System.out.println("В метод create в контроллере пришел параметр " + player);
       Player createdPlayer= null;
        try {
            if(player!=null) {
                //System.out.println(player);
                if(playerService.validation(player)) {
                    createdPlayer = playerService.create(player);
                }
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //return new ResponseEntity<>(HttpStatus.OK);
        return createdPlayer!=null
                ? new ResponseEntity<>(player,HttpStatus.OK)
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
        playerFilter = getPlayerFilter( name, title, race, profession, after, before, banned, minExperience,maxExperience, minLevel, maxLevel);
    }
        //final List<Player> clients = playerService.readAll();
        //System.out.println(playerFilter);
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

        if(id==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Player player = playerService.read(id);

        return player != null
                ? new ResponseEntity<>(player, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/rest/players/count")
    public Integer getCount(
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
        //List<Player> listPlayers = playerService.readAll();
        //return listPlayers.size();
        PlayerFilter playerFilter=null;
        if(!name.isEmpty()||!title.isEmpty()||!race.isEmpty()||!profession.isEmpty()||!after.isEmpty()||!before.isEmpty()||!banned.isEmpty()||!minExperience.isEmpty()||!maxExperience.isEmpty()||!minLevel.isEmpty()||!maxLevel.isEmpty()){
            playerFilter = getPlayerFilter( name, title, race, profession, after, before, banned, minExperience,maxExperience, minLevel, maxLevel);
        }
        //final List<Player> clients = playerService.readAll();
        //System.out.println(playerFilter);
        List<Player> resultList = playerService.readAllOrdered(null, playerFilter, null, null );
        return playerService.getCountPlayers();
    }

    //@PostMapping(value = "/rest/players/{id}")
    @RequestMapping(value = "/rest/players/{id}", method = RequestMethod.POST/*, produces = "application/json; charset=utf-8"*/)
    //@ResponseBody
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody Player player) {
    //public HttpStatus update(@PathVariable(name = "id") int id, @RequestBody Player player) {
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.add("Content-Type", "application/json; charset=utf-8");
        //responseHeaders.setLocation(builder.path("/admin/{id}").buildAndExpand(adminCreatedEvent.getAdminId()).toUri());
        //return new ResponseEntity<Player>( responseHeaders, HttpStatus.CREATED);
//        System.out.println("Апдейтим игрока №" + id);
//        if(player==null||player.isNull()){
//            System.out.println("Игрок пустой");
//
//        } else {
//            System.out.println(player);
//        }
        try {
            boolean updated = false;
            if (id == 0) {
                //return HttpStatus.BAD_REQUEST;
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (player.isNull()) {
                //return HttpStatus.OK;
                //System.out.println("Игрок пустой");
                Player player1 = playerService.read(id);
                //player1.setId(50L);player1.setName("test"); player1.setBirthday(new Date());
                return new ResponseEntity<>(player1,HttpStatus.OK);
            }
            if (player.getExperience() != null) {
                if (player.getExperience() < 0 || player.getExperience() > 10000000) {
                    //return HttpStatus.BAD_REQUEST;
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            if (player.getBirthday() != null) {
                if (player.getBirthday().after(new SimpleDateFormat("dd.MM.yyyy").parse("01.01.3001"))) {
                    //System.out.println("ДР больше 01.01.3001");
                    //return HttpStatus.BAD_REQUEST;
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                if (player.getBirthday().before(new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2000"))) {
                    //System.out.println("ДР меньше 01.01.2000");
                    //return HttpStatus.BAD_REQUEST;
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
            if (!playerService.existsById(id)) {
                //return HttpStatus.NOT_FOUND;
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            if (!player.isNull()) {
                Player updatedPlayer = playerService.update(player, id);
                return new ResponseEntity<>(updatedPlayer,HttpStatus.OK);

            } else {
                //return HttpStatus.OK;
                new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return new ResponseEntity<>(HttpStatus.OK);
        } //ex.printStackTrace();


        return new ResponseEntity<>(HttpStatus.OK);
        //return HttpStatus.OK;
    }

    @DeleteMapping(value = "/rest/players/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {

        if(id==0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final boolean deleted = playerService.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public PlayerFilter getPlayerFilter (
            String name,String title,String race,String profession,String after,String before,String banned,String minExperience,
            String maxExperience,String minLevel,String maxLevel
    ){
       PlayerFilter playerFilter = new PlayerFilter();
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
        return playerFilter;
    }

}
