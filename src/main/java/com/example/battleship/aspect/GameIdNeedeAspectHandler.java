package com.example.battleship.aspect;

import com.example.battleship.exception.GameNotCreatedYetException;
import com.example.battleship.model.BattleshipGame;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Mahdi Sharifi
 * If a player does not present the game id they can't play the game. A game will be represented by a game id.
 * This aspect handles this part.
 * Because most of the methods need this validation, for the sake of simplicity I put it in aspect.
 */

@Aspect
@Component
@Order(1) //It has the most priority, because it indicated the game and we are playing the game.
@Slf4j
public class GameIdNeedeAspectHandler {
    private final Cache<String, BattleshipGame> battleshipGameCache;

    public GameIdNeedeAspectHandler(Cache<String, BattleshipGame> battleshipGameCache) {
        this.battleshipGameCache = battleshipGameCache;
    }

    @Around("@annotation(com.example.battleship.aspect.GameIdNeeded)") //define the logic to execute
    public Object gameIdNeededHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        var gameId = (String) args[0];

        var battleshipGame = battleshipGameCache.getIfPresent(gameId);

        if (battleshipGame == null)
            throw new GameNotCreatedYetException(gameId);

        return joinPoint.proceed();
    }

}
