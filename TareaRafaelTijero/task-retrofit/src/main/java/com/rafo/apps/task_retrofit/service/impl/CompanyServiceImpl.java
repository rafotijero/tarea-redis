package com.rafo.apps.task_retrofit.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafo.apps.task_retrofit.redis.RedisService;
import com.rafo.apps.task_retrofit.repository.CompanyRepository;
import com.rafo.apps.task_retrofit.response.ResponseGeneric;
import com.rafo.apps.task_retrofit.response.ResponseSUNAT;
import com.rafo.apps.task_retrofit.retrofit.ClientSunatService;
import com.rafo.apps.task_retrofit.retrofit.impl.ClientSunatServiceImpl;
import com.rafo.apps.task_retrofit.service.CompanyService;
import com.rafo.apps.task_retrofit.utils.Constants;
import com.rafo.apps.task_retrofit.utils.Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

@Service
@Log4j2
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final RedisService redisService;

    public CompanyServiceImpl(CompanyRepository companyRepository, RedisService redisService) {
        this.companyRepository = companyRepository;
        this.redisService = redisService;
    }

    ClientSunatService sunatServiceRetrofit = ClientSunatServiceImpl
            .getRetrofit()
            .create(ClientSunatService.class);

    @Value("${token.api}")
    private String token;

    @Override
    public ResponseGeneric getInfoSunat(String ruc) throws IOException {
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

    private ResponseGeneric executionSUNAT(String ruc) throws IOException {
        ResponseGeneric response = new ResponseGeneric();
        Call<ResponseSUNAT> clientRetrofit = prepareSUNATRetrofit(ruc);
        log.info("getEntity -> Se preparó todo el cliente Retrofit, listo para ejecutar!");

        Response<ResponseSUNAT> execute = clientRetrofit.execute();

        if (execute.isSuccessful() && Objects.nonNull(execute.body())) {
            response.setData(execute.body());
        } else {
            String errorMessage;
            if (execute.code() == 422) {
                errorMessage = getErrorMessage(execute);
            } else {
                errorMessage = "Error en la solicitud: código " + execute.code();
            }
            response.setMessage(errorMessage);
            response.setCode(execute.code());
        }

        return response;
    }

    private Call<ResponseSUNAT> prepareSUNATRetrofit(String dni){
        String tokenComplete = Constants.BEARER + token;
        log.info("prepareReniecRetrofit -> Ejecutando Metodo de Apoyo que crea el objeto retrofit completo");
        return sunatServiceRetrofit.getInfoSunat(tokenComplete,dni);
    }

    private String getErrorMessage(Response<ResponseSUNAT> response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String errorBody = response.errorBody().string();
            JsonNode errorNode = objectMapper.readTree(errorBody);
            return errorNode.get("message").asText();
        } catch (Exception ex) {
            return Constants.ERROR_GENERAL + ": EmpresaServiceImpl -> getErrorMessage";
        }
    }


}
