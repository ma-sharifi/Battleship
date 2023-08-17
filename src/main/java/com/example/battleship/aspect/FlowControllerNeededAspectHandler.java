package com.example.battleship.aspect;

import com.example.battleship.exception.PlayerDidNotJoinException;
import com.example.battleship.model.BattleshipGame;
import com.example.battleship.model.Player;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Mahdi Sharifi
 *
 * It controls the flow of the gameplay. The player can't call fire before joining the game.
 * The player just can fire his opponent after placing his fleet on board.
 */

@Aspect
@Component
@Order(2)
@Slf4j
public class FlowControllerNeededAspectHandler {
    private final Cache<String, BattleshipGame> battleshipGameCache;

    public FlowControllerNeededAspectHandler(Cache<String, BattleshipGame> battleshipGameCache) {
        this.battleshipGameCache = battleshipGameCache;
    }

    @Around("@annotation(com.example.battleship.aspect.FlowControllerNeeded)") //define the logic to execute
    public Object flowControllerNeededHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        var methodName = joinPoint.getSignature().getName();
        var gameId = (String) args[0];
        int playerId = (int) args[1];

        var battleshipGame = battleshipGameCache.getIfPresent(gameId); //It is not null since it is always executed after the GameIdNeedeAspectHandler aspect.

        Optional<Player> playerOptional=Objects.requireNonNull(battleshipGame).playerById(playerId);

        if (playerOptional.isEmpty()) {
            throw new PlayerDidNotJoinException(String.valueOf(playerId));
        }
        var expectedMethodCall = playerOptional.get().getNextAction();

        if (!methodName.equalsIgnoreCase(expectedMethodCall)) {
            throw new IllegalStateException("Expected method call is: "+expectedMethodCall+",and real method called is: "+methodName);
        }

        var result = joinPoint.proceed();
        playerOptional.get().setNextAction("fire");//Stay in action "fire" until the game is over
        return result;
    }

}
