package com.demo.uaa.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * client
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Client implements Serializable {
    private Long id;

    private String clientId;

    private Date clientIdIssuedAt;

    private String clientSecret;

    private Date clientSecretExpiresAt;

    private String clientName;

    private String clientAuthenticationMethods;

    private String authorizationGrantTypes;

    private String redirectUris;

    private String scopes;

    private String clientSettings;

    private String tokenSettings;

    private static final long serialVersionUID = 1L;
}