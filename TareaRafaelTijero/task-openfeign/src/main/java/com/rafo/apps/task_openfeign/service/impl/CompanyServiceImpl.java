package com.rafo.apps.task_openfeign.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafo.apps.task_openfeign.client.ClientSUNAT;
import com.rafo.apps.task_openfeign.entity.CompanyEntity;
import com.rafo.apps.task_openfeign.redis.RedisService;
import com.rafo.apps.task_openfeign.repository.CompanyRepository;
import com.rafo.apps.task_openfeign.response.ResponseGeneric;
import com.rafo.apps.task_openfeign.response.ResponseSUNAT;
import com.rafo.apps.task_openfeign.service.CompanyService;
import com.rafo.apps.task_openfeign.utils.Constants;
import com.rafo.apps.task_openfeign.utils.Util;
import feign.FeignException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ClientSUNAT clientSUNAT;
    private final RedisService redisService;

    @Value("${token.api}")
    private String token;

    public CompanyServiceImpl(CompanyRepository companyRepository, ClientSUNAT clientSUNAT, RedisService redisService) {
        this.companyRepository = companyRepository;
        this.clientSUNAT = clientSUNAT;
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
        }
        else {
            try {
                dataSUNAT = executionSUNAT(ruc);
                response.setData(dataSUNAT);
                String dataRedis = Util.objectToString(dataSUNAT);
                redisService.saveInRedis(Constants.REDIS_KEY_API_SUNAT + ruc, dataRedis, Constants.REDIS_TTL);
                response.setMessage(Constants.DATA_SOURCE + Constants.API_EXTERNAL_SUNAT + Constants.CLIENT);
                response.setCode(Constants.CODE_OK);
            } catch (FeignException ex) {
                if (ex.status() == 422) {
                    String errorMessage = getErrorMessage(ex);
                    response.setMessage(errorMessage);
                }
                response.setCode(ex.status());
            }
        }
        return response;
    }

    private CompanyEntity getCompany(String ruc) {
        CompanyEntity company = new CompanyEntity();
        ResponseSUNAT response = executionSUNAT(ruc);
        if (Objects.nonNull(response)) {
            BeanUtils.copyProperties(response, company);
            company.setFlagStatus(Constants.STATUS_ACTIVE);
            company.setUserCreated(Constants.USER_CREATED);
            company.setDateCreated(new Timestamp(System.currentTimeMillis()));
        }
        return company;
    }

    private ResponseSUNAT executionSUNAT(String ruc) {
        String tokenOK = Constants.BEARER + token;
        return clientSUNAT.getDataSUNAT(ruc, tokenOK);
    }

    private String getErrorMessage(FeignException e) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode errorBody = objectMapper.readTree(e.contentUTF8());
            return errorBody.get("message").asText();
        } catch (Exception ex) {
            return Constants.ERROR_GENERAL + ": EmpresaServiceImpl -> getErrorMessage";
        }
    }



}
