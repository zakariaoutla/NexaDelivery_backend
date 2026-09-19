package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.Enum.DeliveryStatus;
import org.laicose.nexadelivery.Enum.DriverStatus;
import org.laicose.nexadelivery.dto.request.DeliveryDtoReq;
import org.laicose.nexadelivery.dto.request.DeliveryStatusReq;
import org.laicose.nexadelivery.dto.response.DeliveryDtoResp;
import org.laicose.nexadelivery.mapper.DeliveryMapper;
import org.laicose.nexadelivery.model.*;
import org.laicose.nexadelivery.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final MerchantRepository merchantRepository;
    private final DriverRepository driverRepository;
    private final CollectionPointRepository collectionPointRepository;
    private final DriverLocationRepository driverLocationRepository;
    private final NotificationService notificationService;


    public Page<DeliveryDtoResp> findAllDelivery(Pageable pageable){
        Page<Delivery> merchants = deliveryRepository.findAll(pageable);
        return merchants.map(deliveryMapper::toResponseDto);
    }
    public DeliveryDtoResp findDeliveryById(Long id){
        Delivery delivery = deliveryRepository.findById(id).orElseThrow(()->new RuntimeException("Merchant avec l'ID " + id + " est introuvable"));
        return deliveryMapper.toResponseDto(delivery);
    }

    public DeliveryDtoResp getMyDeliveryById(String email, Long id) {

        Merchant merchant = merchantRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Merchant avec l'email " + email + " est introuvable"
                        )
                );

        Delivery delivery = deliveryRepository
                .findByIdAndMerchant(id, merchant)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Livraison introuvable"
                        )
                );

        return deliveryMapper.toResponseDto(delivery);
    }

    @Transactional
    public DeliveryDtoResp createDelivery(
            String email,
            DeliveryDtoReq deliveryDtoReq
    ) {

        Merchant merchant = merchantRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Merchant avec l'email " + email + " est introuvable"
                        )
                );

        CollectionPoint collectionPoint = collectionPointRepository
                .findByIdAndMerchant(
                        deliveryDtoReq.getCollectionPointId(),
                        merchant
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Point de collecte introuvable pour ce merchant"
                        )
                );

        Delivery delivery =
                deliveryMapper.toEntityDto(deliveryDtoReq);

        delivery.setCollectionPoint(collectionPoint);

        delivery.setPickupAddress(
                collectionPoint.getAddress()
        );

        delivery.setMerchant(merchant);

        delivery.setCreatedAt(
                LocalDateTime.now()
        );

        delivery.setTrackingCode(
                "NX-" +
                        UUID.randomUUID()
                                .toString()
                                .substring(0, 8)
                                .toUpperCase()
        );

        delivery.setDeliveryStatus(
                DeliveryStatus.EN_ATTENTE
        );

        Delivery savedDelivery =
                deliveryRepository.save(delivery);

        return autoAssignDriver(
                savedDelivery.getId()
        );
    }



    public DeliveryDtoResp updateDelivery(Long id, DeliveryDtoReq deliveryDtoReq){
        Delivery delivery = deliveryRepository.findById(id).orElseThrow(()->new RuntimeException("Delivery avec l'ID " + id + " est introuvable"));

        delivery.setClientName(deliveryDtoReq.getClientName());
        delivery.setClientPhone(deliveryDtoReq.getClientPhone());
        delivery.setDescription(deliveryDtoReq.getDescription());
        delivery.setDropAddress(deliveryDtoReq.getDropAddress());

        Delivery updatedDelivery = deliveryRepository.save(delivery);

        return deliveryMapper.toResponseDto(updatedDelivery);
    }





    public DeliveryDtoResp findByTrackingCode(String trackingCode){
        Delivery delivery = deliveryRepository.findByTrackingCode(trackingCode).orElseThrow(()-> new RuntimeException("Delivery avec Tracking code " + trackingCode + " est introuvable"));
         return deliveryMapper.toResponseDto(delivery);
    }

    @Transactional
    public DeliveryDtoResp assignDriverToDelivery(Long driverId, Long deliveryId){
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(()-> new RuntimeException("Delivery avec l'ID " + deliveryId + " est introuvable"));
        Driver driver = driverRepository.findById(driverId).orElseThrow(()->new RuntimeException("Driver avec l'ID " + driverId + " est introuvable"));

        if (delivery.getDeliveryStatus() != DeliveryStatus.EN_ATTENTE) {
            throw new RuntimeException(
                    "Cette livraison n'est pas en attente"
            );
        }
        if (driver.getDriverStatus() != DriverStatus.DISPONIBLE) {
            throw new RuntimeException(
                    "Ce driver n'est pas disponible"
            );
        }

        delivery.setDriver(driver);
        delivery.setDeliveryStatus(DeliveryStatus.ASSIGNEE);
        driver.setDriverStatus(DriverStatus.EN_LIVRAISON);
        driverRepository.save(driver);

        Delivery saveddelivery = deliveryRepository.save(delivery);

        notificationService.createNotification(
                driver,
                delivery,
                "Une nouvelle livraison vous a été assignée"
        );

        return deliveryMapper.toResponseDto(saveddelivery);

    }

    @Transactional
    public DeliveryDtoResp updateDeliveryStatus(
            Long deliveryId,
            DeliveryStatusReq request) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException(
                        "Delivery avec l'ID " + deliveryId + " est introuvable"
                ));

        DeliveryStatus currentStatus = delivery.getDeliveryStatus();
        DeliveryStatus newStatus = request.getDeliveryStatus();

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new RuntimeException(
                    "Transition de " + currentStatus +
                            " vers " + newStatus +
                            " non autorisée"
            );
        }

        delivery.setDeliveryStatus(newStatus);

        if (newStatus == DeliveryStatus.ANNULEE
                || newStatus == DeliveryStatus.LIVREE) {

            if (delivery.getDriver() != null) {
                Driver driver = delivery.getDriver();
                driver.setDriverStatus(DriverStatus.DISPONIBLE);
                driverRepository.save(driver);
            }
        }

        Delivery savedDelivery = deliveryRepository.save(delivery);
        String message = switch (newStatus) {
            case RECUPEREE ->
                    "Votre livraison a été récupérée par le chauffeur";

            case EN_ROUTE ->
                    "Votre livraison est maintenant en route";

            case LIVREE ->
                    "Votre livraison a été livrée avec succès";

            default ->
                    null;
        };

        if (message != null) {
            notificationService.createNotification(
                    delivery.getMerchant(),
                    delivery,
                    message
            );
        }

        return deliveryMapper.toResponseDto(savedDelivery);
    }


    private boolean isValidTransition(
            DeliveryStatus currentStatus,
            DeliveryStatus newStatus) {

        return switch (currentStatus) {

            case EN_ATTENTE ->
                    newStatus == DeliveryStatus.ANNULEE;

            case ASSIGNEE ->
                    newStatus == DeliveryStatus.RECUPEREE
                            || newStatus == DeliveryStatus.ANNULEE;

            case RECUPEREE ->
                    newStatus == DeliveryStatus.EN_ROUTE;

            case EN_ROUTE ->
                    newStatus == DeliveryStatus.LIVREE;

            case LIVREE, ANNULEE -> false;
        };
    }

    public Page<DeliveryDtoResp> getMyDeliveries(String email, Pageable pageable){
        Merchant merchant = merchantRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Merchant avec l'email " + email + " est introuvable"));
        Page<Delivery> deliveries  = deliveryRepository.findByMerchant(merchant,pageable);
        return deliveries.map(deliveryMapper::toResponseDto);
    }

    public Page<DeliveryDtoResp> getMyDriverDeliveries(String email, Pageable pageable){
        Driver driver = driverRepository.findByEmail(email).orElseThrow(()->new RuntimeException("Driver avec l'email " + email + " est introuvable"));
        Page<Delivery> deliveries = deliveryRepository.findByDriver(driver, pageable);
        return deliveries.map(deliveryMapper::toResponseDto);
    }

    @Transactional
    public DeliveryDtoResp updateMyDeliveryStatus(
            String email,
            Long deliveryId,
            DeliveryStatusReq request) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Delivery avec l'ID " + deliveryId + " est introuvable"
                        )
                );

        if (delivery.getDriver() == null) {
            throw new RuntimeException(
                    "Cette livraison n'est affectée à aucun driver"
            );
        }

        if (!delivery.getDriver().getEmail().equals(email)) {
            throw new RuntimeException(
                    "Vous n'êtes pas autorisé à modifier cette livraison"
            );
        }

        DeliveryStatus currentStatus = delivery.getDeliveryStatus();
        DeliveryStatus newStatus = request.getDeliveryStatus();

        boolean validTransition = switch (currentStatus) {

            case ASSIGNEE ->
                    newStatus == DeliveryStatus.RECUPEREE;

            case RECUPEREE ->
                    newStatus == DeliveryStatus.EN_ROUTE;

            case EN_ROUTE ->
                    newStatus == DeliveryStatus.LIVREE;

            case EN_ATTENTE, LIVREE, ANNULEE -> false;
        };

        if (!validTransition) {
            throw new RuntimeException(
                    "Transition de " + currentStatus +
                            " vers " + newStatus +
                            " non autorisée"
            );
        }

        delivery.setDeliveryStatus(newStatus);

        if (newStatus == DeliveryStatus.LIVREE) {
            Driver driver = delivery.getDriver();
            driver.setDriverStatus(DriverStatus.DISPONIBLE);
            driverRepository.save(driver);
        }

        Delivery savedDelivery = deliveryRepository.save(delivery);

        String message = switch (newStatus) {
            case RECUPEREE ->
                    "Votre livraison a été récupérée par le chauffeur";

            case EN_ROUTE ->
                    "Votre livraison est maintenant en route";

            case LIVREE ->
                    "Votre livraison a été livrée avec succès";

            default -> null;
        };

        if (message != null) {
            notificationService.createNotification(
                    savedDelivery.getMerchant(),
                    savedDelivery,
                    message
            );
        }


        return deliveryMapper.toResponseDto(savedDelivery);
    }

    public DeliveryDtoResp cancelMyDelivery(String email, Long deliveryId) {

        Merchant merchant = merchantRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Merchant avec l'email " + email + " est introuvable"
                        )
                );

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Delivery avec l'ID " + deliveryId + " est introuvable"
                        )
                );

        if (delivery.getMerchant().getId()!=(merchant.getId())) {
            throw new RuntimeException(
                    "Vous n'êtes pas autorisé à annuler cette livraison"
            );
        }

        if (delivery.getDeliveryStatus() != DeliveryStatus.EN_ATTENTE) {
            throw new RuntimeException(
                    "Cette livraison ne peut être annulée que si elle est en attente"
            );
        }

        delivery.setDeliveryStatus(DeliveryStatus.ANNULEE);

        Delivery savedDelivery = deliveryRepository.save(delivery);

        return deliveryMapper.toResponseDto(savedDelivery);
    }

    private double calculateDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        final double EARTH_RADIUS = 6371.0;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                        + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lonDistance / 2)
                        * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return EARTH_RADIUS * c;
    }


    @Transactional
    public DeliveryDtoResp autoAssignDriver(Long deliveryId) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Delivery avec l'ID " + deliveryId + " est introuvable"
                        )
                );

        if (delivery.getDeliveryStatus() != DeliveryStatus.EN_ATTENTE) {
            throw new RuntimeException(
                    "Seule une livraison EN_ATTENTE peut être assignée"
            );
        }

        CollectionPoint collectionPoint =
                delivery.getCollectionPoint();

        if (collectionPoint == null) {
            throw new RuntimeException(
                    "Aucun point de collecte associé à cette livraison"
            );
        }

        Zone zone = collectionPoint.getZone();

        List<Driver> availableDrivers =
                driverRepository.findByZoneAndDriverStatus(
                        zone,
                        DriverStatus.DISPONIBLE
                );

        
        if (availableDrivers.isEmpty()) {
            return deliveryMapper.toResponseDto(delivery);
        }


        Driver nearestDriver = null;
        double minimumDistance = Double.MAX_VALUE;


        for (Driver driver : availableDrivers) {

            Optional<DriverLocation> locationOptional =
                    driverLocationRepository
                            .findFirstByDriverOrderByTimestampDesc(driver);


            if (locationOptional.isEmpty()) {
                continue;
            }

            DriverLocation location =
                    locationOptional.get();

            double distance = calculateDistance(
                    collectionPoint.getLatitude(),
                    collectionPoint.getLongitude(),
                    location.getLatitude(),
                    location.getLongitude()
            );


            if (distance < minimumDistance) {

                minimumDistance = distance;

                nearestDriver = driver;
            }
        }


        if (nearestDriver == null) {
            return deliveryMapper.toResponseDto(delivery);
        }


        delivery.setDriver(nearestDriver);

        delivery.setDeliveryStatus(
                DeliveryStatus.ASSIGNEE
        );

        nearestDriver.setDriverStatus(
                DriverStatus.EN_LIVRAISON
        );

        driverRepository.save(nearestDriver);

        Delivery savedDelivery =
                deliveryRepository.save(delivery);


        notificationService.createNotification(
                nearestDriver,
                savedDelivery,
                "Une nouvelle livraison vous a été assignée"
        );


        return deliveryMapper.toResponseDto(
                savedDelivery
        );
    }


    public void deleteDelivery(Long id) {

        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Delivery avec l'ID " + id + " est introuvable"
                        )
                );

        if (delivery.getDeliveryStatus() == DeliveryStatus.ANNULEE
                || delivery.getDeliveryStatus() == DeliveryStatus.EN_ATTENTE) {

            deliveryRepository.delete(delivery);

        } else {
            throw new RuntimeException(
                    "Vous n'êtes pas autorisé à supprimer cette livraison"
            );
        }
    }



}
