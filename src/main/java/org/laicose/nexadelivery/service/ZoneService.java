package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.ZoneDtoReq;
import org.laicose.nexadelivery.dto.response.ZoneDtoResp;
import org.laicose.nexadelivery.mapper.ZoneMapper;
import org.laicose.nexadelivery.model.Zone;
import org.laicose.nexadelivery.repository.ZoneRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final ZoneMapper zoneMapper;


    public Page<ZoneDtoResp> getAllZone(Pageable pageable){
        Page<Zone> zones = zoneRepository.findAll(pageable);
        return zones.map(zoneMapper::toResponse);
    }

    public ZoneDtoResp getZoneById(Long id){
        Zone zone = zoneRepository.findById(id).orElseThrow(()->new RuntimeException("Zone avec l'ID " + id + " est introuvable"));
        return zoneMapper.toResponse(zone);
    }

    public ZoneDtoResp createZone(ZoneDtoReq zoneDtoReq){
        Zone zone = zoneMapper.toEntityDto(zoneDtoReq);
        Zone savedZone = zoneRepository.save(zone);
        return zoneMapper.toResponse(savedZone);
    }

    public ZoneDtoResp updateZone(Long id, ZoneDtoReq zoneDtoReq){
        Zone zone = zoneRepository.findById(id).orElseThrow(()->new RuntimeException("Zone avec l'ID " + id + " est introuvable"));

        zone.setName(zoneDtoReq.getName());
        zone.setDescription(zoneDtoReq.getDescription());

        Zone updatedZone = zoneRepository.save(zone);

        return zoneMapper.toResponse(updatedZone);

    }

    public void deleteZone(Long id){
        Zone zone = zoneRepository.findById(id).orElseThrow(()->new RuntimeException("Zone avec l'ID " + id + " est introuvable"));

        if (zone.getDrivers() != null && !zone.getDrivers().isEmpty()) {
            throw new RuntimeException(
                    "Impossible de supprimer cette zone car elle contient des livreurs"
            );
        }
        zoneRepository.delete(zone);
    }

}
