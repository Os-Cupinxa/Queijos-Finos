package com.queijos_finos.main.dto;

import com.queijos_finos.main.model.Contrato;

import java.util.List;

public class PropriedadeDTO {
    private final String name;
    private final String owner;
    private final String city;
    private final String state;
    private final Integer status;
    private final String latitude;
    private final String longitude;
    private final List<Contrato> contractsActive;

    public PropriedadeDTO(String name, String owner, String city, String state, Integer status, String latitude, String longitude, List<Contrato> contractsActive) {
        this.name = name;
        this.owner = owner;
        this.city = city;
        this.state = state;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contractsActive = contractsActive;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Integer getStatus() {
        return status;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public List<Contrato> getContractsActive() {
        return contractsActive;
    }
}
