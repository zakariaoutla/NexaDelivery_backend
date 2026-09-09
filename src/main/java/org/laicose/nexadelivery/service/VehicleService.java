package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.VehicleDtoReq;
import org.laicose.nexadelivery.dto.response.VehicleDtoResp;
import org.laicose.nexadelivery.mapper.VehicleMapper;
import org.laicose.nexadelivery.model.Vehicle;
import org.laicose.nexadelivery.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public Page<VehicleDtoResp> getAllVehicle(Pageable pageable){
        Page<Vehicle> vehicles = vehicleRepository.findAll(pageable);
        return vehicles.map(vehicleMapper::toResponse);

    }

    public VehicleDtoResp getVehicleById(Long id){
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(()->new RuntimeException("Vehicle avec l'ID " + id + " est introuvable"));
        return vehicleMapper.toResponse(vehicle);
    }

    public VehicleDtoResp createVehicle(VehicleDtoReq vehicleDtoReq){
        Vehicle vehicle = vehicleMapper.toEntityDto(vehicleDtoReq);
        Vehicle saveVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.toResponse(saveVehicle);
    }

    public VehicleDtoResp updateVehicle(Long id, VehicleDtoReq vehicleDtoReq){
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(()->new RuntimeException("Vehicle avec l'ID " + id + " est introuvable"));

        vehicle.setType(vehicleDtoReq.getType());
        vehicle.setCapacityKg(vehicleDtoReq.getCapacityKg());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toResponse(updatedVehicle);
    }

    public void deleteVehicle(Long id){
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(()->new RuntimeException("Vehicle avec l'ID " + id + " est introuvable"));

        if (vehicle.getDriver() != null) {
            throw new RuntimeException(
                    "Impossible de supprimer ce véhicule car il est affecté à un livreur"
            );
        }

        vehicleRepository.delete(vehicle);
    }
}
