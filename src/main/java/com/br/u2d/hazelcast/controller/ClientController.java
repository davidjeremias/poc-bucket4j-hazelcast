package com.br.u2d.hazelcast.controller;

import com.br.u2d.hazelcast.controller.response.ClientResponse;
import com.br.u2d.hazelcast.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/client")
@CrossOrigin("*")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable Long id) {
        Optional<ClientResponse> response = clientService.findById(id);
        return response.isPresent() ? ResponseEntity.ok(response.get())
                : ResponseEntity.noContent().build();
    }
}
