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

import hu.icellmobilsoft.sampler.dto.SampleKafkaDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.config.ConfigProvider;
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

    private static final String AVRO_CHANNEL_NAME = "to-kafka-avro";

    private static final String STRING_CHANNEL_NAME = "to-kafka-string";

    @Inject
    private Logger log;

    @Inject
    private KafkaMessageLogger kafkaMessageLogger;

    @Inject
    private KafkaMessageHandler kafkaMessageHandler;

    @Inject
    @Channel(AVRO_CHANNEL_NAME)
    private Emitter<ProducerRecord<Integer, SampleKafkaDto>> messageEmitter;

    @Inject
    @Channel(STRING_CHANNEL_NAME)
    private Emitter<String> emitterString;

    /**
     * Kafka Stream producer
     * 
     * @return message payload to send
     */
    // Endless message loop
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
    public void toKafka(SampleKafkaDto message) throws BaseException {
        // Send message by system handled feature (not recommended)
        log.info("Sample Outgoing: [{0}] [{1}] [{2}]", message.getColumnA(), message.getColumnB(), message.getColumnC());
        waitForPublish(messageEmitter.send(new ProducerRecord<>(getTopic(), message)));

        // Send message by smallrye specific header handling (working, experimental feature)
        Headers headers = new RecordHeaders();
        headers.add("header-1-okr", "value-1-okr".getBytes(StandardCharsets.UTF_8));
        OutgoingKafkaRecordMetadata<Object> okrMetadata = OutgoingKafkaRecordMetadata.builder().withHeaders(headers).build();
        String payloadOkr = message.getColumnA() + "|okr";
        // Message okrMessage = Message.of(payloadOkr);
        // okrMessage = okrMessage.addMetadata(okrMetadata);
        Message<String> okrMessage = KafkaMetadataUtil.writeOutgoingKafkaMetadata(Message.of(payloadOkr), okrMetadata);
        okrMessage =
                kafkaMessageHandler.handleOutgoingMdc(okrMessage);
        kafkaMessageLogger.printOutgoingMessage(okrMessage);
        emitterString.send(okrMessage);

        // Send message by smallrye specific metadata handling (not working, experimental feature)
        Metadata metadata = Metadata.of(Map.of("header-2-meta", "value-2-meta"));
        String payloadMeta = message.getColumnA() + "|meta";
        Message<String> metaMessage = Message.of(payloadMeta, metadata);
        metaMessage =
                kafkaMessageHandler.handleOutgoingMdc(metaMessage);
        kafkaMessageLogger.printOutgoingMessage(metaMessage);
        emitterString.send(metaMessage);
    }

    private String getTopic() {
        return ConfigProvider.getConfig().getConfigValue("mp.messaging.outgoing." + AVRO_CHANNEL_NAME + ".topic").getValue();
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
