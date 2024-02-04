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
package hu.icellmobilsoft.ts.sample.jpa.batch.service.jpa.association.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import hu.icellmobilsoft.coffee.dto.common.commonservice.FunctionCodeType;
import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.roaster.api.TestSuiteGroup;
import hu.icellmobilsoft.sampler.api.jee.rest.batch.IJpaAssociationRest;
import hu.icellmobilsoft.sampler.dto.sample.batch.jpaassociation.JpaAssociationResponse;
import hu.icellmobilsoft.sampler.dto.sample.batch.jpaassociation.JpaAssociationType;
import hu.icellmobilsoft.sampler.dto.sample.batch.jpaassociation.JpaAssociationUpdateRequest;
import hu.icellmobilsoft.sampler.dto.sample.batch.jpaassociation.JpaAssociationUpdateType;
import hu.icellmobilsoft.sampler.ts.common.rest.DtoHelper;
import hu.icellmobilsoft.ts.sample.jpa.batch.service.jpa.association.rest.base.BaseJpaAssociationRestIT;

/**
 * IT tests for {@link IJpaAssociationRest#putUpdateJpaAssociationEntityWithBatchService}.
 *
 * @author csaba.balogh
 * @since 2.0.0
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testing JpaAssociationRest.putUpdateJpaAssociationEntityWithBatchService()")
@Tag(TestSuiteGroup.RESTASSURED)
class PutUpdateJpaAssociationEntityWithBatchServiceIT extends BaseJpaAssociationRestIT {

    @Test
    @Order(1)
    void putUpdateJpaAssociationEntityWithBatchServiceChangeEmptyManyToOneToExistingTest() throws BaseException {
        // given
        String jpaAssociationId = createJpaAssociation(null).getJpaAssociation().getJpaAssociationId();

        String manyToOneId = createEmptyEntity().getEmptyEntityId();
        JpaAssociationUpdateRequest request = createRequest(manyToOneId);

        // when
        JpaAssociationResponse response = getRestClient().putUpdateJpaAssociationEntityWithBatchService(jpaAssociationId, request);
        JpaAssociationType actualJpaAssociation = response.getJpaAssociation();

        // then
        Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode(), "functionCode");
        Assertions.assertAll(
                () -> Assertions.assertEquals(manyToOneId, actualJpaAssociation.getManyToOneId(), "manyToOneId"),
                () -> Assertions.assertEquals(jpaAssociationId, actualJpaAssociation.getJpaAssociationId(), "jpaAssociationId"));
    }

    @Test
    @Order(2)
    void putUpdateJpaAssociationEntityWithBatchServiceChangeExistingManyToOneToEmptyTest() throws BaseException {
        // given
        String manyToOneId = createEmptyEntity().getEmptyEntityId();
        String jpaAssociationId = createJpaAssociation(manyToOneId).getJpaAssociation().getJpaAssociationId();

        JpaAssociationUpdateRequest request = createRequest(null);

        // when
        JpaAssociationResponse response = getRestClient().putUpdateJpaAssociationEntityWithBatchService(jpaAssociationId, request);
        JpaAssociationType actualJpaAssociation = response.getJpaAssociation();

        // then
        Assertions.assertEquals(FunctionCodeType.OK, response.getFuncCode(), "functionCode");
        Assertions.assertAll(
                () -> Assertions.assertNull(actualJpaAssociation.getManyToOneId(), "manyToOneId"),
                () -> Assertions.assertEquals(jpaAssociationId, actualJpaAssociation.getJpaAssociationId(), "jpaAssociationId"));
    }

    private JpaAssociationUpdateRequest createRequest(String manyToOneId) {
        JpaAssociationUpdateType jpaAssociation = new JpaAssociationUpdateType().withManyToOneId(manyToOneId);
        JpaAssociationUpdateRequest request = new JpaAssociationUpdateRequest().withJpaAssociation(jpaAssociation);
        request.setContext(DtoHelper.createContext());
        return request;
    }
}
