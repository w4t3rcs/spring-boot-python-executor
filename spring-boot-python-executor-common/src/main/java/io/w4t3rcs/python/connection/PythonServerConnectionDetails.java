package io.w4t3rcs.python.connection;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

public interface PythonServerConnectionDetails extends ConnectionDetails {
    String getUsername();

    String getPassword();

    String getUri();

    static PythonServerConnectionDetails of(String username, String password, String uri) {
        return new PythonServerConnectionDetails() {
            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public String getPassword() {
                return password;
            }

            @Override
            public String getUri() {
                return uri;
            }
        };
    }
}
