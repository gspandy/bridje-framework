/*
 * Copyright 2016 Bridje Framework.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bridje.http.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bridje.ioc.Component;
import org.bridje.http.HttpServerContext;
import org.bridje.http.HttpServerHandler;
import org.bridje.http.HttpServerRequest;
import org.bridje.http.HttpServerResponse;
import org.bridje.ioc.Priority;

/**
 *
 */
@Component
@Priority(0)
class RootServerHandler implements HttpServerHandler
{
    private static final Logger LOG = Logger.getLogger(RootServerHandler.class.getName());

    @Override
    public boolean handle(HttpServerContext context) throws IOException
    {
        HttpServerRequest req = context.get(HttpServerRequest.class);
        HttpServerResponse resp = context.get(HttpServerResponse.class);
        LOG.log(Level.INFO, "{0} {1} {2}", new Object[]{req.getMethod(), req.getPath(), req.getProtocol()});
        resp.setStatusCode(404);
        try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream()))
        {
            writer.append("Method: ");
            writer.append(req.getMethod());
            writer.append("<br/>");
            writer.append("Path: ");
            writer.append(req.getPath());
            writer.append("<br/>");
            writer.append("Protocol: ");
            writer.append(req.getProtocol());
            writer.flush();
        }
        //IOUtils.copy(new FileInputStream(new File("./test.html")), context.get(HttpServerResponse.class).getOutputStream());
        return true;
    }
    
}
