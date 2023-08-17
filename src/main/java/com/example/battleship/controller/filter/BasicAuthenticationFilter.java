package com.example.battleship.controller.filter;

import com.example.battleship.controller.dto.ErrorMessageDto;
import com.example.battleship.exception.errorcode.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;

/**
 * @author Mahdi Sharifi
 *
 * For the sake of simplicity, I did not use Spring security for security, I just used a simple filter for it.
 * AuthenticationFilter filter, extract the player id and add it to the request;
 * If a user does not provide an authorization header he will get the following error.
 *
 * It handles authentication on application. Used must provvide user and password in Basic Authentication.
 *
 */

@Component
@Order(1)
@Slf4j
public class BasicAuthenticationFilter implements Filter {

    @Value("#{'${userlist}'.split(',')}")
    private List<String> userList; //for the sake of simplicity, I assumed the password is the same

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        //Exclude swagger from protection
        if(request.getRequestURI()!=null && (request.getRequestURI().contains("/swagger-ui/") || (request.getRequestURI().contains("/v3/api-docs")))){
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        // Get the HTTP Authorization header from the request
        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
            buildErrorResponse(response,"Missing or invalid Authorization header!");
            return;
        }

        // Extract the token from the HTTP Authorization header
        var userPasswordEncoded = authorizationHeader.substring("Basic ".length()).trim();
        var userPassDecoded = new String(Base64.getDecoder().decode(userPasswordEncoded));
        var player = userPassDecoded.substring(0, userPassDecoded.indexOf(":"));
        if (!userList.contains(player) || !userPassDecoded.substring(userPassDecoded.indexOf(":") + 1).equals("password")) {
            buildErrorResponse(response,"Username or password not match!");
        } else {
            request.setAttribute("player-id", player.substring("player".length()));
            chain.doFilter(servletRequest, servletResponse);//go ahead and call API
        }

    }
    //build the error response
    private static void buildErrorResponse(HttpServletResponse response,String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream responseStream = response.getOutputStream();
        var mapper = new ObjectMapper();
        var responseDto = new ErrorMessageDto(ErrorCode.USERNAME_OR_PASSWORD_NOT_MATCH.code(), message);
        mapper.writeValue(responseStream, responseDto);
        responseStream.flush();
    }
}