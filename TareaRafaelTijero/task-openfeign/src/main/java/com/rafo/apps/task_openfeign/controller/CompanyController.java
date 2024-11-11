package com.rafo.apps.task_openfeign.controller;

import com.rafo.apps.task_openfeign.response.ResponseGeneric;
import com.rafo.apps.task_openfeign.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
