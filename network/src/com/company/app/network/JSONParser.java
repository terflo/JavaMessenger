package com.company.app.network;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JSONParser {

    private final ObjectMapper mapper = new ObjectMapper();

    public synchronized String convertToString(Request request) throws IOException {
        return request.getClass().getSimpleName() + "//" + mapper.writeValueAsString(request);
    }

    public synchronized Request convertToRequest(String str) throws IOException {
        Request request = null;
        final String substring = str.substring(str.indexOf("//") + 2);
        switch (str.substring(0, str.indexOf("//"))){
            case "MessageRequest":
                request = mapper.readValue(substring, MessageRequest.class);
                break;
            case "LoginRequest":
                request = mapper.readValue(substring, LoginRequest.class);
                break;
            case "RegisterRequest":
                request = mapper.readValue(substring, RegisterRequest.class);
                break;
            case "ChangesRequest":
                request = mapper.readValue(substring, ChangesRequest.class);
                break;
            default:
                break;
        }
        return request;
    }
}
