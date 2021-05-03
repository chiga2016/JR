package com.game.repository;

import com.game.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerDao extends JpaRepository<Player,Long> {
    Player findPlayerById(long id);


}
