package com.example.battleship.aspect;

import com.example.battleship.exception.NotYourTurnException;
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
 * It handles the player's turn, and after successful action, it will change the turn.
 */
@Aspect
@Component
@Order(3)
@Slf4j
public class TurnControllerNeededAspectHandler {
    private final Cache<String, BattleshipGame> battleshipGameCache;

    public TurnControllerNeededAspectHandler(Cache<String, BattleshipGame> battleshipGameCache) {
        this.battleshipGameCache = battleshipGameCache;
    }

    @Around("@annotation(com.example.battleship.aspect.PlayerTurnControllerNeeded)") //define the logic to execute
    public Object turnControllerNeededHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        var gameId = (String) args[0];
        int playerId = (int) args[1];

        var battleshipGame = battleshipGameCache.getIfPresent(gameId);

        if (battleshipGame.getTurn() != playerId) {
            throw new NotYourTurnException(String.valueOf(battleshipGame.getTurn()));
        }

        var result = joinPoint.proceed();

        battleshipGame.changeTurn();//If everything went well, change the turn to other player
        return result;
    }

}
