package com.game.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.game.entity.Profession;
import com.game.entity.Race;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
//@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //ID игрока
    private String name;
    //Имя персонажа (до 12 знаков включительно)
    private String title;
    //Титул персонажа (до 30 знаков включительно)
    @Enumerated(EnumType.STRING)
    private Race race;
    //Расса персонажа
    @Enumerated(EnumType.STRING)
    private Profession profession;
    //Профессия персонажа
    private Integer experience;
    //Опыт персонажа. Диапазон значений 0..10,000,000
    private Integer level;
    //Уровень персонажа
    private Integer untilNextLevel;
    //Остаток опыта до следующего уровня
    private Date birthday;
    //Дата регистрации
    //Диапазон значений года 2000..3000 включительно
    private Boolean banned;
    //Забанен / не забанен


    public Player() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public boolean isNull (){
        if(id==null&&name==null&&title==null&&race==null&&profession==null&&experience==null&&level==null&&untilNextLevel==null&&birthday==null&&banned==null){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String bd = birthday!=null? new SimpleDateFormat("dd.MM.yyyy").format(birthday):"";
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                ", birthday=" + bd  +
                ", banned=" + banned +
                '}';
    }
}
