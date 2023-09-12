package br.com.xyzservices.cloudSA;


import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;

public class CloudSqlAutoIamAuthnDataSource extends HikariDataSource {

    public CloudSqlAutoIamAuthnDataSource(HikariConfig configuration) {
        super(configuration);
    }

    @Override
    public String getPassword() {
        GoogleCredentials credentials;
        try {
            credentials = GoogleCredentials.getApplicationDefault();

        } catch (IOException err) {
            throw new RuntimeException(
                    "Unable to obtain credentials to communicate with the Cloud SQL API", err);
        }

        // Scope the token to ensure it's scoped to logins only.
        GoogleCredentials scoped = credentials.createScoped(
                "https://www.googleapis.com/auth/sqlservice.login");

        try {
            scoped.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AccessToken accessToken = scoped.getAccessToken();
        return accessToken.getTokenValue();
    }
}