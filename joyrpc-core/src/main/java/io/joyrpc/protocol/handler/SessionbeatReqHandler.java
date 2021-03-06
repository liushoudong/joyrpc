package io.joyrpc.protocol.handler;

/*-
 * #%L
 * joyrpc
 * %%
 * Copyright (C) 2019 joyrpc.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.joyrpc.exception.HandlerException;
import io.joyrpc.protocol.MessageHandler;
import io.joyrpc.protocol.MsgType;
import io.joyrpc.protocol.message.Message;
import io.joyrpc.transport.channel.ChannelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @date: 2019/3/29
 */
public class SessionbeatReqHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(SessionbeatReqHandler.class);

    @Override
    public void handle(ChannelContext context, Message message) throws HandlerException {
        //sessionbeat是oneway发送，不需要应答
        boolean res = context.getChannel().beatSession(message.getSessionId());
        //session心跳不成功，说明session已经被清理，断开连接重新协商
        if (!res) {
            logger.warn(String.format("The session %s has expired when receiving sessionbeat message.", message.getSessionId()));
        }
        context.end();
    }

    @Override
    public Integer type() {
        return (int) MsgType.SessionbeatReq.getType();
    }
}
