/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2023 i-Cell Mobilsoft Zrt.
 * %%
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
 * #L%
 */
package hu.icellmobilsoft.sampler.sample.kafka.service.action;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.dto.exception.TechnicalException;
import hu.icellmobilsoft.coffee.dto.exception.enums.CoffeeFaultType;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import io.smallrye.reactive.messaging.kafka.api.KafkaMetadataUtil;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;

/**
 * Sample Kafka Publisher
 * 
 * @author Imre Scheffer
 */
@ApplicationScoped
public class KafkaPublisher extends BaseAction {

    @Inject
    private Logger log;

    @Inject
    @Channel("to-kafka")
    private Emitter<String> emitterString;

    // @Inject
    // @Channel("to-kafka")
    // private Emitter<String> emitterMessage;

    /**
     * Kafka Stream producer
     * 
     * @return message payload to send
     */
    // Ebben a formaban vegtelen uzenet keletkezik
    // @Outgoing("to-kafka")
    public String toKafkaOutgoing() {
        String message = "sample";
        log.info("Sample Outgoing: [{0}]", message);
        return message;
    }

    /**
     * Send message to kafka
     * 
     * @param message
     *            message payload to publis
     * @throws BaseException
     *             error
     */
    public void toKafka(String message) throws BaseException {
        String payloadString = message + "|okr";
        log.info("Sample Outgoing: [{0}]", payloadString);
        waitForPublish(emitterString.send(payloadString));

        Headers headers = new RecordHeaders();
        headers.add("header-1-okr", "value-1-okr".getBytes(StandardCharsets.UTF_8));
        OutgoingKafkaRecordMetadata okrMetadata = OutgoingKafkaRecordMetadata.builder().withHeaders(headers).build();
        String payloadOkr = message + "|okr";
        // Message okrMessage = Message.of(payloadOkr);
        // okrMessage = okrMessage.addMetadata(okrMetadata);
        Message okrMessage = KafkaMetadataUtil.writeOutgoingKafkaMetadata(Message.of(payloadOkr), okrMetadata);
        emitterString.send(okrMessage);

        Metadata metadata = Metadata.of(Map.of("header-2-meta", "value-2-meta"));
        String payloadMeta = message + "|meta";
        Message<String> metaMessage = Message.of(payloadMeta, metadata);
        emitterString.send(metaMessage);
    }

    private void waitForPublish(CompletionStage<Void> publishStage) throws TechnicalException {
        try {
            publishStage.toCompletableFuture().get();
        } catch (InterruptedException ex) {
            handleInterrupt(ex);
        } catch (ExecutionException e) {
            log.error("Exception occured", e);
            throw new TechnicalException(CoffeeFaultType.OPERATION_FAILED, "Unkown Kafka publish error", e);
        }
    }

    private void handleInterrupt(InterruptedException ex) {
        log.warn("Interrupted sleep.", ex);
        try {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.warn("Exception during interrupt.", ex);
        }
    }
}
