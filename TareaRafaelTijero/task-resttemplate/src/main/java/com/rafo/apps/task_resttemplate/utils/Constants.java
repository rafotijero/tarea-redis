package com.rafo.apps.task_resttemplate.utils;

public class Constants {

    public static final String USER_CREATED = "RTIJERO";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final int STATUS_ACTIVE = 1;
    public static final String ERROR_GENERAL = "Error General en método: ";
    public static final String ERROR_REQUEST = "Error en la solicitud: código ";
    public static final String ERROR_EXEC_REQUEST = "Error al ejecutar la solicitud: ";

    public static final String BASE_URL = "https://api.apis.net.pe";
    public static final String URL_MICROSERVICE = "/v2/sunat/ruc/full";
    public static final int REDIS_TTL = 5;
    public static final String REDIS_KEY_API_SUNAT = "MS:REGISTRO:CONSULTA:SUNAT:";
    public static final String ERROR_CONVERT_TO_STRING = "Error al convertir el objeto de tipo %s a String: %s";
    public static final String ERROR_CONVERT_FROM_STRING = "Error al convertir desde String a la clase %s: %s";
    public static final String DATA_SOURCE = "El orígen de la información fue extraido de: ";
    public static final String REDIS_SERVER = "Servidor de REDIS.";
    public static final String API_EXTERNAL_SUNAT = "Consulta a la Api Externa de SUNAT. ";
    public static final int CODE_OK = 200;
    public static final String CLIENT = "[Cliente: RestTemplate]";

}

