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
        switch (str.substring(0, str.indexOf("//"))){
            case "LoginRequest":
                request = mapper.readValue(str.substring(str.indexOf("//")+2), LoginRequest.class);
                break;
            case "MessageRequest":
                request = mapper.readValue(str.substring(str.indexOf("//")+2), MessageRequest.class);
                break;
            case "RegisterRequest":
                request = mapper.readValue(str.substring(str.indexOf("//")+2), RegisterRequest.class);
                break;
        }
        return request;
    }
}
