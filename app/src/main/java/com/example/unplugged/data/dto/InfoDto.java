package com.example.unplugged.data.dto;

public class InfoDto {

    private String name;
    private String region;

    public InfoDto() {
    }

    public InfoDto(String name, String region) {
        this.name = name;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
