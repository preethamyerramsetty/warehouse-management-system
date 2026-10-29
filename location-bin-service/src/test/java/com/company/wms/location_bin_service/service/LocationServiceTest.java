package com.company.wms.location_bin_service.service;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.wms.location_bin_service.dto.CreateLocationRequest;
import com.company.wms.location_bin_service.exception.DuplicateResourceException;
import com.company.wms.location_bin_service.mapper.LocationMapper;
import com.company.wms.location_bin_service.repository.LocationRepository;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationService locationService;

    @Test
    void createLocation_whenCodeAlreadyExists_throwsException() {
        UUID warehouseId = UUID.randomUUID();

        CreateLocationRequest request = new CreateLocationRequest();
        request.setWarehouseId(warehouseId);
        request.setLocationCode("LOC-001");

        when(locationRepository.existsByWarehouseIdAndLocationCode(
                warehouseId, "LOC-001"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> locationService.createLocation(request)
        );

        verify(locationRepository).existsByWarehouseIdAndLocationCode(
                warehouseId, "LOC-001");
    }
}
