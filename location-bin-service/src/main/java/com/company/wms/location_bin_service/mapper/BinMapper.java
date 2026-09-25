package com.company.wms.location_bin_service.mapper;

import org.springframework.stereotype.Component;

import com.company.wms.location_bin_service.domain.Bin;
import com.company.wms.location_bin_service.dto.BinResponse;
import com.company.wms.location_bin_service.dto.CreateBinRequest;

@Component
public class BinMapper {

    public Bin toEntity(CreateBinRequest request) {

        Bin bin = new Bin();

        bin.setLocationId(request.getLocationId());
        bin.setBinCode(request.getBinCode());
        bin.setCapacity(request.getCapacity());
        bin.setStatus(request.getStatus());

        return bin;
    }

    public BinResponse toResponse(Bin bin) {

        BinResponse response = new BinResponse();

        response.setId(bin.getId());
        response.setLocationId(bin.getLocationId());
        response.setBinCode(bin.getBinCode());
        response.setCapacity(bin.getCapacity());
        response.setStatus(bin.getStatus());
        response.setCreatedAt(bin.getCreatedAt());
        response.setUpdatedAt(bin.getUpdatedAt());

        return response;
    }
}