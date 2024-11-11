package com.rafo.apps.task_retrofit.service;

import com.rafo.apps.task_retrofit.response.ResponseGeneric;

import java.io.IOException;

public interface CompanyService {

    ResponseGeneric getInfoSunat(String ruc) throws IOException;

}
