package com.deliverytech.delivery_api.services.interfaces;

public interface CepLocationService {
    CepLocation resolve(String cep);

    record CepLocation(double latitude, double longitude) {
    }
}