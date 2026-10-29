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

import com.company.wms.location_bin_service.dto.CreateBinRequest;
import com.company.wms.location_bin_service.exception.ResourceNotFoundException;
import com.company.wms.location_bin_service.mapper.BinMapper;
import com.company.wms.location_bin_service.repository.BinRepository;
import com.company.wms.location_bin_service.repository.LocationRepository;

@ExtendWith(MockitoExtension.class)
class BinServiceTest {

    @Mock
    private BinRepository binRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private BinMapper binMapper;

    @InjectMocks
    private BinService binService;

    @Test
    void createBin_whenLocationDoesNotExist_throwsException() {
        UUID locationId = UUID.randomUUID();

        CreateBinRequest request = new CreateBinRequest();
        request.setLocationId(locationId);
        request.setBinCode("BIN-001");

        when(locationRepository.existsById(locationId))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> binService.createBin(request)
        );

        verify(locationRepository).existsById(locationId);
    }
}
