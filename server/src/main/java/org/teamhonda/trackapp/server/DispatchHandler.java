/*
 * Copyright 2016 Quest Software, Inc.
 * ALL RIGHTS RESERVED.
 * 
 * This software is the confidential and proprietary information of
 * Quest Software Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered
 * into with Quest Software Inc.
 * 
 * QUEST SOFTWARE INC. MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. QUEST SOFTWARE SHALL NOT BE LIABLE FOR ANY
 * DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package org.teamhonda.trackapp.server;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gordon Tyler
 */
public class DispatchHandler implements HttpHandler {

private static Logger logger = LoggerFactory.getLogger(Main.class);

private final HttpHandler mHandler;

public DispatchHandler(HttpHandler handler) {
    mHandler = handler;
}

@Override
public void handleRequest(HttpServerExchange exchange) throws Exception {
    if (exchange.isInIoThread()) {
        exchange.dispatch(this);
        return;
    }

    try {
        mHandler.handleRequest(exchange);
    }
    catch (Exception e) {
        logger.error("An error ocurred while handling " + exchange.getRequestMethod() + " " + exchange.getRequestPath(), e);
        exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        exchange.getResponseSender().send(e.toString());
    }
}

}
