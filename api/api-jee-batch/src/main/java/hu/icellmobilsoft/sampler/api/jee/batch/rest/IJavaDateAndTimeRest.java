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
package hu.icellmobilsoft.sampler.api.jee.batch.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import hu.icellmobilsoft.coffee.cdi.annotation.xml.ValidateXML;
import hu.icellmobilsoft.coffee.dto.common.commonservice.BaseResponse;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.sampler.dto.constant.XsdConstants;
import hu.icellmobilsoft.sampler.dto.path.JpaBatchServicePath;
import hu.icellmobilsoft.sampler.dto.sample.batch.javadateandtime.JavaDateAndTimeInsertRequest;
import hu.icellmobilsoft.sampler.dto.sample.batch.javadateandtime.JavaDateAndTimeResponse;
import hu.icellmobilsoft.sampler.dto.sample.batch.javadateandtime.JavaDateAndTimeUpdateRequest;

/**
 * REST endpoints for JavaDateAndTime entities.
 *
 * @author csaba.balogh
 * @since 2.0.0
 */
@Tag(name = "REST endpoints for JavaDateAndTime entities", description = "REST endpoints for JavaDateAndTime entities")
@Path(JpaBatchServicePath.REST_JPA_BATCH_SERVICE_JAVA_DATE_AND_TIME)
public interface IJavaDateAndTimeRest {

    /**
     * Creates and inserts a JavaDateAndTime entity with BatchService.
     *
     * @param javaDateAndTimeInsertRequest
     *            {@link JavaDateAndTimeInsertRequest}.
     * @return {@link JavaDateAndTimeResponse}.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    @Operation(summary = "Creates and inserts a JavaDateAndTime entity with BatchService.",
            description = "Creates and inserts a JavaDateAndTime entity with BatchService.")
    @POST
    @Path(JpaBatchServicePath.INSERT)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    JavaDateAndTimeResponse postInsertJavaDateAndTimeEntityWithBatchService(
            @ValidateXML(xsdPath = XsdConstants.SUPER_XSD_PATH) JavaDateAndTimeInsertRequest javaDateAndTimeInsertRequest) throws BaseException;

    /**
     * Updates a JavaDateAndTime entity with BatchService.
     *
     * @param javaDateAndTimeId
     *            ID of JavaDateAndTime.
     * @param javaDateAndTimeUpdateRequest
     *            {@link JavaDateAndTimeUpdateRequest}.
     * @return {@link JavaDateAndTimeResponse}.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    @Operation(summary = "Updates a JavaDateAndTime entity with BatchService.", description = "Updates a JavaDateAndTime entity with BatchService.")
    @PUT
    @Path(JpaBatchServicePath.UPDATE_JAVA_DATE_AND_TIME_ID)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    JavaDateAndTimeResponse putUpdateJavaDateAndTimeEntityWithBatchService(
            @PathParam(JpaBatchServicePath.PARAM_JAVA_DATE_AND_TIME_ID) @Parameter(name = JpaBatchServicePath.PARAM_JAVA_DATE_AND_TIME_ID,
                    description = "JAVA_DATE_AND_TIME.X__ID", required = true) String javaDateAndTimeId,
            @ValidateXML(xsdPath = XsdConstants.SUPER_XSD_PATH) JavaDateAndTimeUpdateRequest javaDateAndTimeUpdateRequest) throws BaseException;

    /**
     * Deletes all JavaDateAndTime entities with BatchService.
     *
     * @return {@link BaseResponse}.
     * @throws BaseException
     *             if any exception occurs during the process.
     */
    @Operation(summary = "Deletes all JavaDateAndTime entities with BatchService.",
            description = "Deletes all JavaDateAndTime entities with BatchService.")
    @DELETE
    @Path(JpaBatchServicePath.DELETE)
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    BaseResponse deleteAllJavaDateAndTimeEntitiesWithBatchService() throws BaseException;
}
