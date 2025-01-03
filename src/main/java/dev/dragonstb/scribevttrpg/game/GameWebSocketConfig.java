/*
 * Copyright (c) 2024, Dragonstb
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package dev.dragonstb.scribevttrpg.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 *
 * @author Dragonstb
 * @since 0.1.1;
 */
@Configuration
@EnableWebSocketMessageBroker
public class GameWebSocketConfig implements WebSocketMessageBrokerConfigurer{

    @Autowired
    @Lazy
    private TaskScheduler scheduler;

    @Override
    public void configureMessageBroker( MessageBrokerRegistry config ) {
        config.setApplicationDestinationPrefixes( "/wschannel" );
        config.enableSimpleBroker( "/topic", "/queue" )
                .setHeartbeatValue( new long[]{10_000, 20_000} )
                .setTaskScheduler( scheduler );
    }

    @Override
    public void registerStompEndpoints( StompEndpointRegistry registry ) {

        registry.addEndpoint( "/wshandshake" )
                .addInterceptors( new HttpSessionHandshakeInterceptor() );
    }



    // TODO: prevent processing of messages sent from the client to /topic and /queue (these destinations are used for
    //       outbound only. Clients shall sent their messages to /wschannel, where it is processed, authorized, ...
    // TODO: prevent subscription of channels the clients are not authorized for (for example,
    //       /wschannel/admingame/{roomName} is only for administrators of the game, which is just the GM currently)
}
