package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.*;

import java.util.List;

public interface PlayerService {
    /**
     * Создает нового клиента
     * @param player - клиент для создания
     */
    void create(Player player);

    /**
     * Возвращает список всех имеющихся клиентов
     * @return список клиентов
     */
    List<Player> readAll();

    List<Player> readAllOrdered(PlayerOrder order, String pageSize, String limit);

    /**
     * Возвращает клиента по его ID
     * @param id - ID клиента
     * @return - объект клиента с заданным ID
     */
    Player read(long id);

    /**
     * Обновляет клиента с заданным ID,
     * в соответствии с переданным клиентом
     * @param player - клиент в соответсвии с которым нужно обновить данные
     * @param id - id клиента которого нужно обновить
     * @return - true если данные были обновлены, иначе false
     */
    boolean update(Player player, long id);

    /**
     * Удаляет клиента с заданным ID
     * @param id - id клиента, которого нужно удалить
     * @return - true если клиент был удален, иначе false
     */
    boolean delete(long id);
}
