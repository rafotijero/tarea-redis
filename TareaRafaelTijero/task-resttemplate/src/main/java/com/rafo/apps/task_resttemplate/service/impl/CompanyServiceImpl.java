package com.rafo.apps.task_resttemplate.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafo.apps.task_resttemplate.redis.RedisService;
import com.rafo.apps.task_resttemplate.response.ResponseGeneric;
import com.rafo.apps.task_resttemplate.response.ResponseSUNAT;
import com.rafo.apps.task_resttemplate.service.CompanyService;
import com.rafo.apps.task_resttemplate.utils.Constants;
import com.rafo.apps.task_resttemplate.utils.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final RestTemplate restTemplate;
    private final RedisService redisService;

    @Value("${token.api}")
    private String token;

    public CompanyServiceImpl(RestTemplate restTemplate, RedisService redisService) {
        this.restTemplate = restTemplate;
        this.redisService = redisService;
    }

    @Override
    public ResponseGeneric getInfoSunat(String ruc) {
        ResponseGeneric response = new ResponseGeneric();
        ResponseSUNAT dataSUNAT;
        String redisInfo = redisService.getDataFromRedis(Constants.REDIS_KEY_API_SUNAT + ruc);
        if (Objects.nonNull(redisInfo)) {
            dataSUNAT = Util.stringToObject(redisInfo, ResponseSUNAT.class);
            response.setData(dataSUNAT);
            response.setMessage(Constants.DATA_SOURCE + Constants.REDIS_SERVER);
            response.setCode(Constants.CODE_OK);
        } else {
            response = executionSUNAT(ruc);
            if (response.getData() != null) {
                String dataRedis = Util.objectToString(response.getData());
                redisService.saveInRedis(Constants.REDIS_KEY_API_SUNAT + ruc, dataRedis, Constants.REDIS_TTL);
                response.setMessage(Constants.DATA_SOURCE + Constants.API_EXTERNAL_SUNAT + Constants.CLIENT);
                response.setCode(Constants.CODE_OK);
            }
        }
        return response;
    }

    private ResponseGeneric executionSUNAT(String ruc){

        ResponseGeneric response = new ResponseGeneric();

        String url = Constants.BASE_URL + Constants.URL_MICROSERVICE + "?numero="+ruc;
        try {
            ResponseEntity<ResponseSUNAT> executeRestTemplate = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders()),
                    ResponseSUNAT.class
            );

            if (executeRestTemplate.getStatusCode() == HttpStatus.OK) {
                response.setData(executeRestTemplate.getBody());
            } else if (executeRestTemplate.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                String errorMessage = getErrorMessage(executeRestTemplate);
                response.setMessage(errorMessage);
                response.setCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
            } else {
                response.setMessage(Constants.ERROR_REQUEST + executeRestTemplate.getStatusCode().value());
                response.setCode(executeRestTemplate.getStatusCode().value());
            }

        } catch (Exception ex) {
            response.setMessage(Constants.ERROR_EXEC_REQUEST + ex.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }


    private HttpHeaders createHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constants.AUTHORIZATION, Constants.BEARER + token);
        return headers;
    }

    private String getErrorMessage(ResponseEntity<ResponseSUNAT> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String errorBody = response.getBody().toString();
            JsonNode errorNode = objectMapper.readTree(errorBody);
            return errorNode.get("message").asText();
        } catch (Exception ex) {
            return Constants.ERROR_GENERAL + ": EmpresaServiceImpl -> getErrorMessage";
        }
    }


}
