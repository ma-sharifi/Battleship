package com.example.battleship.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Mahdi Sharifi
 */

@Target({ElementType.METHOD})
@Retention(RUNTIME)
public @interface PlayerTurnControllerNeeded {

}
