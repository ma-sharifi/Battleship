package com.example.battleship.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Mahdi Sharifi
 */
@Schema(description = "It presents a coordination with string that called label. Label can be A1, B10")
public record LabelDto(String label) {}
