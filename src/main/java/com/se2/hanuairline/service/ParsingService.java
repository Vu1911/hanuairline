package com.se2.hanuairline.service;

public interface ParsingService {
    /**
     * Service to parse the json response and convert it to the desired model class
     */
    Object parse(String url);
}
