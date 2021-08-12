package com.br.u2d.hazelcast.controller.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ClientResponse implements Serializable {

    private static final long serialVersionUID = -2561329876695851149L;

    private Long id;
    private String name;
    private String login;
}
