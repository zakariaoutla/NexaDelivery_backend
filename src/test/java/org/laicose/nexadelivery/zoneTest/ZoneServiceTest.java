package org.laicose.nexadelivery.zoneTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laicose.nexadelivery.dto.request.ZoneDtoReq;
import org.laicose.nexadelivery.dto.response.ZoneDtoResp;
import org.laicose.nexadelivery.mapper.ZoneMapper;
import org.laicose.nexadelivery.model.Zone;
import org.laicose.nexadelivery.repository.ZoneRepository;
import org.laicose.nexadelivery.service.ZoneService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private ZoneMapper zoneMapper;

    @InjectMocks
    private ZoneService zoneService;

    @Test
    void createZoneTest() {

        ZoneDtoReq request = new ZoneDtoReq();
        request.setName("Beni Mellal");
        request.setDescription("Zone Beni Mellal");

        Zone zone = new Zone();
        zone.setName("Beni Mellal");
        zone.setDescription("Zone Beni Mellal");

        Zone savedZone = new Zone();
        savedZone.setId(1L);
        savedZone.setName("Beni Mellal");
        savedZone.setDescription("Zone Beni Mellal");

        ZoneDtoResp response = new ZoneDtoResp();
        response.setId(1L);
        response.setName("Beni Mellal");
        response.setDescription("Zone Beni Mellal");

        when(zoneMapper.toEntityDto(request)).thenReturn(zone);
        when(zoneRepository.save(zone)).thenReturn(savedZone);
        when(zoneMapper.toResponse(savedZone)).thenReturn(response);

        ZoneDtoResp result = zoneService.createZone(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Beni Mellal", result.getName());
        assertEquals("Zone Beni Mellal", result.getDescription());

        verify(zoneMapper).toEntityDto(request);
        verify(zoneRepository).save(zone);
        verify(zoneMapper).toResponse(savedZone);
    }

    @Test
    void getZoneByIdZoneDoesNotExistTest() {

        Long zoneId = 99L;

        when(zoneRepository.findById(zoneId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> zoneService.getZoneById(zoneId)
        );

        assertEquals(
                "Zone avec l'ID 99 est introuvable",
                exception.getMessage()
        );

        verify(zoneRepository).findById(zoneId);
        verifyNoInteractions(zoneMapper);
    }
}
