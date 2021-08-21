package com.tts.weatherapp.service;

import com.tts.weatherapp.model.Response;
import com.tts.weatherapp.model.ZipCode;
import com.tts.weatherapp.repository.ZipCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final ZipCodeRepository zipCodeRepository;
    @Value("${api_key}")
    private String apiKey;


    public WeatherService(ZipCodeRepository zipCodeRepository){
        this.zipCodeRepository = zipCodeRepository;
    }

    public Response getForecast(String zipCode) {
        String url = "https://api.openweathermap.org/data/2.5/weather?zip=" + zipCode +
                "&units=imperial&appid=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        try {
            zipCodeRepository.save(new ZipCode(zipCode));
            return restTemplate.getForObject(url, Response.class);
        } catch (HttpClientErrorException ex) {
            Response response = new Response();
            response.setName("error");
            return response;
        }

    }

    /* Method that returns  the last 10 zipcode queries */

    public List<ZipCode> getLast10ZipCodes(){
        List<ZipCode> zipCodeList = (List<ZipCode>) zipCodeRepository.findAll();
        Collections.reverse(zipCodeList);
        return zipCodeList.stream()
                .limit(10)
                .collect(Collectors.toList());
    }

}
