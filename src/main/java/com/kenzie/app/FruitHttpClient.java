package com.kenzie.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class FruitHttpClient {
    public static String getDesiredURL(FruitCriteria fc){
        switch(fc){
            case ALL:
                // look through the documentation to determine the correct URL
                return "https://www.fruityvice.com/api/fruit/all";
            case LOW_CARB:
                // look through the documentation to determine the correct URL
                return "https://www.fruityvice.com/api/fruit/carbohydrates?max=5";
            case HIGH_CALORIE:
                // look through the documentation to determine the correct URL
                return "https://www.fruityvice.com/api/fruit/calories?min=100";
            default:
                return "";
        }
    }

    public String getFruits(String url) {
        // TODO: create a URL object from the desired URL
        // TODO: make the GET request and return the list of FruitDTOs
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = httpResponse.statusCode();
            if (statusCode == 200) {
                return httpResponse.body();
            } else {
               return String.format("GET request failed: %d status code received", statusCode);
            }
        } catch (IOException | InterruptedException e) {
            return e.getMessage();
        }

    }

    public static List<FruitDTO> getFruitsList(String httpResponseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<FruitDTO>> typeReferenceFruitDTO =  new TypeReference<>(){};
        List<FruitDTO> getFruitsList = objectMapper.readValue(httpResponseBody, typeReferenceFruitDTO);

        return getFruitsList;
    }
    
        public static FruitDTO createFruitDTO(){
        // TODO: create a sample FruitDTO object based on what you want to send in your PUT request
        // instantiate a new FruitDTO object and a new Nutritions DTO object
        // then fill in the properties for each item with the setter methods
            FruitDTO createFruit = new FruitDTO();
            createFruit.setGenus("Fragaria");
            createFruit.setName("Blackberry");
            createFruit.setFamily("Rosaceae");
            createFruit.setOrder("Rosales");
            FruitDTO.Nutritions nutritions = new FruitDTO.Nutritions();
            nutritions.setCarbohydrates(5.5);
            nutritions.setProtein(0);
            nutritions.setFat(0.4);
            nutritions.setCalories(29);
            nutritions.setSugar(5.4);
            createFruit.setNutritions(nutritions);

        return createFruit;
    }

    public static String fruitToJSON(FruitDTO fruit) throws JsonProcessingException {
        // TODO: convert FruitDTO to JSON using the Jackson library
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        String fruitJSON = objectMapper.writeValueAsString(fruit);
        return fruitJSON;
    }

    public String putFruit(String fruitStr, String url) {
        // TODO: make a PUT request with the data, and return the response
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(fruitStr))
                .build();

        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = httpResponse.statusCode();
            if (statusCode == 201 || statusCode == 202) {
                return httpResponse.body();
            } else {
                return String.format("GET request failed: %d status code received", statusCode);
            }
        } catch (IOException | InterruptedException e) {
            return e.getMessage();
        }
    }
}
