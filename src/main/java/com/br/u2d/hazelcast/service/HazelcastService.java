package com.br.u2d.hazelcast.service;

import com.br.u2d.hazelcast.controller.response.ClientResponse;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HazelcastService {

    private static final String CLIENT = "client";

    @Autowired
    HazelcastInstance hazelcastInstance;

    public ClientResponse put(Long id, ClientResponse user) {
        IMap<Long, ClientResponse> map = hazelcastInstance.getMap(CLIENT);
        return map.putIfAbsent(id, user);
    }

    public ClientResponse get(Long id) {
        IMap<Long, ClientResponse> map = hazelcastInstance.getMap(CLIENT);
        return map.get(id);
    }

}
