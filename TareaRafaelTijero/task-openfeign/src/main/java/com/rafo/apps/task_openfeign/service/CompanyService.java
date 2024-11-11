package com.rafo.apps.task_openfeign.service;

import com.rafo.apps.task_openfeign.response.ResponseGeneric;

public interface CompanyService {

    ResponseGeneric getInfoSunat(String ruc);

}
