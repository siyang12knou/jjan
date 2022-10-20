package com.kailoslab.jjan.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface Constants {
    String PATH_FLUTTER = "/#";
    String PATH_API_PREFIX = "/api";
    String PATH_API_V1_0 = PATH_API_PREFIX + "/v1.0";
    String SYSTEM_ADMIN = "system";
    String DEFAULT_GROUP_ID = "0000000000";
    int ORDINAL_START = 0;
    ObjectMapper OBJECT_MAPPER = new ObjectMapper();
}