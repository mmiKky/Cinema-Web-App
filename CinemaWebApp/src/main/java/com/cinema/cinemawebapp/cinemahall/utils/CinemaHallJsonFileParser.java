package com.cinema.cinemawebapp.cinemahall.utils;

import com.cinema.cinemawebapp.cinemahall.models.Seat;
import com.cinema.cinemawebapp.exceptions.CinemaHallStructureFileNotAvailableException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CinemaHallJsonFileParser implements CinemaHallFileParser {
    @Override
    public List<Seat> parseFile(String filePath) throws CinemaHallStructureFileNotAvailableException {
        List<Seat> seatsList = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(new File(filePath));
            JsonNode seatsNode = jsonNode.path("cinema_hall").path("seats");

            for (JsonNode seatNode : seatsNode) {
                int row = seatNode.get("row").asInt();
                int column = seatNode.get("seatNr").asInt();
                double price = seatNode.get("price").asDouble();
                boolean isDouble = seatNode.has("double_seat") && seatNode.get("double_seat").asBoolean();

                Seat seat = new Seat();
                seat.setRow(row);
                seat.setSeatNr(column);
                seat.setPrice(price);
                seat.setDouble(isDouble);

                seatsList.add(seat);
            }
        } catch (IOException e) {
            throw new CinemaHallStructureFileNotAvailableException();
        }

        return seatsList;
    }
}
