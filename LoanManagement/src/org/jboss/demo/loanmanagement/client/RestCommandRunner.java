/*
 * Copyright 2013-2014 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.jboss.demo.loanmanagement.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 * Executes a REST command.
 */
public class RestCommandRunner {

    /**
     * @param restCommand the command to execute (cannot be <code>null</code>)
     * @return the response as a JSON object (never <code>null</code>)
     * @throws Exception if there are problems executing the command
     */
    public static JSONObject run( final Command restCommand ) throws Exception {
        final HttpGet httpGet = new HttpGet(restCommand.getUrl());

        { // headers
            if (restCommand.useAuthorization()) {
                httpGet.addHeader("Authorization", restCommand.geAuthorizationHeader()); //$NON-NLS-1$
            }

            httpGet.addHeader("Accept", "*/*;charset=utf-8"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        final HttpClient httpClient = new DefaultHttpClient();
        InputStream is = null;

        try {
            final HttpResponse httpResponse = httpClient.execute(httpGet);

            final HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8); //$NON-NLS-1$
            final StringBuilder sb = new StringBuilder();

            String line = null;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            final String content = sb.toString();
            final JSONObject result = new JSONObject(content);

            return result;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (final IOException e) {
                    // nothing to do
                }
            }
        }
    }

    /**
     * Don't allow public construction.
     */
    private RestCommandRunner() {
        // nothing to do
    }

}
