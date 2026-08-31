package com.deliverytech.server.services.interfaces;

public interface CepLocationService {
    CepLocation resolve(String cep);

    record CepLocation(double latitude, double longitude) {
    }
}