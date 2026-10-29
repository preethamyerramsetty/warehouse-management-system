package com.company.wms.inbound_service.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class GoodsReceiptValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void validGoodsReceiptRequest_hasNoValidationErrors() {
        CreateGoodsReceiptLineRequest line =
                new CreateGoodsReceiptLineRequest();

        line.setSkuId(UUID.randomUUID());
        line.setExpectedQuantity(new BigDecimal("10"));
        line.setReceivedQuantity(new BigDecimal("8"));
        line.setUom("EA");

        CreateGoodsReceiptRequest request =
                new CreateGoodsReceiptRequest();

        request.setWarehouseId(UUID.randomUUID());
        request.setReferenceType("PURCHASE_ORDER");
        request.setReferenceId("PO-1001");
        request.setLines(List.of(line));

        Set<ConstraintViolation<CreateGoodsReceiptRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void goodsReceiptRequest_withMissingWarehouse_hasValidationError() {
        CreateGoodsReceiptRequest request =
                new CreateGoodsReceiptRequest();

        request.setReferenceType("PURCHASE_ORDER");
        request.setReferenceId("PO-1001");

        CreateGoodsReceiptLineRequest line =
                new CreateGoodsReceiptLineRequest();

        line.setSkuId(UUID.randomUUID());
        line.setExpectedQuantity(new BigDecimal("10"));
        line.setReceivedQuantity(new BigDecimal("8"));
        line.setUom("EA");

        request.setLines(List.of(line));

        Set<ConstraintViolation<CreateGoodsReceiptRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(
                violation -> violation.getPropertyPath()
                        .toString().equals("warehouseId")));
    }

    @Test
    void goodsReceiptRequest_withZeroQuantity_hasValidationError() {
        CreateGoodsReceiptLineRequest line =
                new CreateGoodsReceiptLineRequest();

        line.setSkuId(UUID.randomUUID());
        line.setExpectedQuantity(new BigDecimal("10"));
        line.setReceivedQuantity(BigDecimal.ZERO);
        line.setUom("EA");

        CreateGoodsReceiptRequest request =
                new CreateGoodsReceiptRequest();

        request.setWarehouseId(UUID.randomUUID());
        request.setReferenceType("PURCHASE_ORDER");
        request.setReferenceId("PO-1001");
        request.setLines(List.of(line));

        Set<ConstraintViolation<CreateGoodsReceiptRequest>> violations =
                validator.validate(request);

        assertTrue(violations.stream().anyMatch(
                violation -> violation.getPropertyPath()
                        .toString().equals("lines[0].receivedQuantity")));
    }

    @Test
    void goodsReceiptRequest_withNoLines_hasValidationError() {
        CreateGoodsReceiptRequest request =
                new CreateGoodsReceiptRequest();

        request.setWarehouseId(UUID.randomUUID());
        request.setReferenceType("PURCHASE_ORDER");
        request.setReferenceId("PO-1001");
        request.setLines(List.of());

        Set<ConstraintViolation<CreateGoodsReceiptRequest>> violations =
                validator.validate(request);

        assertTrue(violations.stream().anyMatch(
                violation -> violation.getPropertyPath()
                        .toString().equals("lines")));
    }
}
