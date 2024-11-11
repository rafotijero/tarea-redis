package com.rafo.apps.task_resttemplate.controller;

import com.rafo.apps.task_resttemplate.response.ResponseGeneric;
import com.rafo.apps.task_resttemplate.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/empresa")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/sunat/{ruc}")
    public ResponseEntity<ResponseGeneric> getInfoSUNAT(@PathVariable String ruc) {
        return new ResponseEntity<>(companyService.getInfoSunat(ruc), HttpStatus.OK);
    }

}
