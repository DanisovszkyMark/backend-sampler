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
package hu.icellmobilsoft.sampler.sample.jpa.batch.service.service;

import java.util.List;

import jakarta.enterprise.inject.Model;

import hu.icellmobilsoft.coffee.se.api.exception.BaseException;
import hu.icellmobilsoft.sampler.common.system.jpa.service.BaseService;
import hu.icellmobilsoft.sampler.model.sample.batch.JavaBaseTypes;

/**
 * Service for {@link JavaBaseTypes}.
 * 
 * @author csaba.balogh
 * @since 2.0.0
 */
@Model
public class JavaBaseTypesService extends BaseService<JavaBaseTypes> {

    /**
     * Find {@link JavaBaseTypes} by ID.
     *
     * @param id
     *            ID of {@link JavaBaseTypes}.
     * @return {@link JavaBaseTypes}.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    public JavaBaseTypes findById(String id) throws BaseException {
        return super.findById(id, JavaBaseTypes.class);
    }

    /**
     * Find all {@link JavaBaseTypes}.
     *
     * @return {@link JavaBaseTypes} list.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    public List<JavaBaseTypes> findAll() throws BaseException {
        return super.findAll(JavaBaseTypes.class);
    }
}
