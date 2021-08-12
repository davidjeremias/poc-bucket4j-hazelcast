package com.br.u2d.hazelcast.service;

import com.br.u2d.hazelcast.controller.response.ClientResponse;
import com.br.u2d.hazelcast.entity.Client;
import com.br.u2d.hazelcast.repository.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private HazelcastService hazelcastService;

    public Optional<ClientResponse> findById(Long id) {
        Optional<ClientResponse> clientResponseHazelcast = Optional.ofNullable(hazelcastService.get(id));
        if (clientResponseHazelcast.isPresent())
            return clientResponseHazelcast;
        Optional<Client> client = clientRepository.findById(id);
        Optional<ClientResponse> clientResponse = Optional.of(modelMapper.map(client.get(), ClientResponse.class));
        hazelcastService.put(clientResponse.get().getId(), clientResponse.get());
        return clientResponse;
    }
}
