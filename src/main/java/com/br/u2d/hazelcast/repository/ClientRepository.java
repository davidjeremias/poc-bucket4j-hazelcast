package com.br.u2d.hazelcast.repository;

import com.br.u2d.hazelcast.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.cdi.Eager;

@Eager
public interface ClientRepository extends JpaRepository<Client, Long> {
}
