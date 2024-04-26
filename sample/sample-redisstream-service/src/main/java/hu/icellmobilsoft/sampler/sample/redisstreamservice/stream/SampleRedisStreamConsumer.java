/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 - 2024 i-Cell Mobilsoft Zrt.
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
package hu.icellmobilsoft.sampler.sample.redisstreamservice.stream;

import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import hu.icellmobilsoft.coffee.dto.exception.InvalidParameterException;
import hu.icellmobilsoft.coffee.module.redisstream.annotation.RedisStreamConsumer;
import hu.icellmobilsoft.coffee.module.redisstream.consumer.AbstractStreamConsumer;
import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.coffee.se.logging.Logger;
import hu.icellmobilsoft.sampler.sample.redisstreamservice.action.SampleRedisStreamTraceAction;
import hu.icellmobilsoft.sampler.sample.redisstreamservice.config.RedisStreamConfig;

/**
 * Sample consumer
 * 
 * @author czenczl
 * @since 2.0.0
 */
@Model
@RedisStreamConsumer(configKey = RedisStreamConfig.REDIS_KEY, //
        group = RedisStreamConfig.REDIS_KEY)
public class SampleRedisStreamConsumer extends AbstractStreamConsumer {

    @Inject
    private Logger log;

    @Inject
    private SampleRedisStreamTraceAction sampleRedisStreamTraceAction;

    @Override
    public void doWork(String event) throws BaseException {
        log.debug(">>> Sample consumer starts the processing with parameter: [{0}]", event);
        if (StringUtils.isBlank(event)) {
            throw new InvalidParameterException("event is empty!");
        }
        try {
            log.info("do something");
            sampleRedisStreamTraceAction.tracedMethod();

        } finally {
            log.debug("<<< Sample consumer finished the processing with parameter: [{0}]", event);
        }
    }
}
