package com.company.wms.warehouse_service.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.wms.warehouse_service.domain.WarehouseStatus;
import com.company.wms.warehouse_service.dto.CreateWarehouseRequest;
import com.company.wms.warehouse_service.exception.DuplicateResourceException;
import com.company.wms.warehouse_service.mapper.WarehouseMapper;
import com.company.wms.warehouse_service.repository.WarehouseRepository;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private WarehouseMapper warehouseMapper;

    @InjectMocks
    private WarehouseService warehouseService;

    @Test
    void createWarehouse_whenCodeAlreadyExists_throwsException() {
        CreateWarehouseRequest request = new CreateWarehouseRequest();
        request.setWarehouseCode("WH-001");
        request.setName("Main Warehouse");
        request.setAddress("Hyderabad");
        request.setStatus(WarehouseStatus.ACTIVE);

        when(warehouseRepository.existsByWarehouseCode("WH-001"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> warehouseService.createWarehouse(request)
        );

        verify(warehouseRepository).existsByWarehouseCode("WH-001");
        verifyNoInteractions(warehouseMapper);
    }
}
