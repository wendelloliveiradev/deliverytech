package com.deliverytech.delivery_api.services.implementations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.deliverytech.delivery_api.exceptions.BusinessException;
import com.deliverytech.delivery_api.services.interfaces.CepLocationService;

@Service
public class BrasilApiCepLocationService implements CepLocationService {
    private final RestClient restClient;

    public BrasilApiCepLocationService(@Value("${app.cep-geocoder.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public CepLocation resolve(String cep) {
        try {
            BrasilApiCepResponse response = restClient.get().uri("/{cep}", cep.replaceAll("\\D", ""))
                    .retrieve().body(BrasilApiCepResponse.class);
            if (response == null || response.location() == null || response.location().coordinates() == null) {
                throw new BusinessException("The CEP does not have geographic coordinates.");
            }
            return new CepLocation(response.location().coordinates().latitude(),
                    response.location().coordinates().longitude());
        } catch (RestClientException ex) {
            throw new BusinessException("CEP geocoding is unavailable. Please try again later.");
        }
    }

    private record BrasilApiCepResponse(Location location) {
    }

    private record Location(Coordinates coordinates) {
    }

    private record Coordinates(double latitude, double longitude) {
    }
}