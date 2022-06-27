/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.activemq.artemis.jms.example;

import org.apache.activemq.artemis.api.core.ActiveMQException;
import org.apache.activemq.artemis.api.core.Message;

import org.apache.activemq.artemis.core.server.ServerSession;
import org.apache.activemq.artemis.core.server.plugin.ActiveMQServerPlugin;
import org.apache.activemq.artemis.core.transaction.Transaction;

import java.util.HashMap;
import java.util.Map;

public class CounterBrokerPlugin implements ActiveMQServerPlugin {
    static final int COUNT_THRESHOLD = 10000;
    private final Map<String, Integer> count = new HashMap<>();

    @Override
    public void beforeSend(ServerSession session,
                           Transaction tx,
                           Message message,
                           boolean direct,
                           boolean noAutoCreateQueue) throws ActiveMQException {
        String queue = message.getAddress();
        if (count.containsKey(queue)) {
            count.put(queue, count.get(queue) + 1);
        } else {
            count.put(queue, 1);
        }
        if (count.get(queue) == COUNT_THRESHOLD) {
            System.out.println("[CounterBrokerPlugin] Queue " + queue + " has " + count.get(queue) + " messages");
            count.put(queue, 0);
        }
    }

}