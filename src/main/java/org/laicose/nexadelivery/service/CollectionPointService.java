package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.dto.request.CollectionPointDtoReq;
import org.laicose.nexadelivery.dto.response.CollectionPointDtoResp;
import org.laicose.nexadelivery.mapper.CollectionPointMapper;
import org.laicose.nexadelivery.model.CollectionPoint;
import org.laicose.nexadelivery.model.Merchant;
import org.laicose.nexadelivery.repository.CollectionPointRepository;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectionPointService {

    private final CollectionPointMapper collectionPointMapper;
    private final CollectionPointRepository collectionPointRepository;
    private final MerchantRepository merchantRepository;


    public CollectionPointDtoResp createCollectionPoint(
            String email,
            CollectionPointDtoReq collectionPointDtoReq
    ) {

        Merchant merchant = merchantRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Merchant avec l'email "
                                        + email
                                        + " est introuvable"
                        )
                );

        CollectionPoint collectionPoint =
                collectionPointMapper.toEntity(
                        collectionPointDtoReq
                );

        collectionPoint.setMerchant(merchant);

        CollectionPoint savedCollectionPoint =
                collectionPointRepository.save(
                        collectionPoint
                );

        return collectionPointMapper.toResponse(
                savedCollectionPoint
        );
    }


    public Page<CollectionPointDtoResp> getAllCollectionPoints(
            Pageable pageable
    ) {

        Page<CollectionPoint> collectionPoints =
                collectionPointRepository.findAll(pageable);

        return collectionPoints.map(
                collectionPointMapper::toResponse
        );
    }


    public CollectionPointDtoResp getCollectionPointById(
            Long id
    ) {

        CollectionPoint collectionPoint =
                collectionPointRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Collection Point avec l'ID "
                                                + id
                                                + " est introuvable"
                                )
                        );

        return collectionPointMapper.toResponse(
                collectionPoint
        );
    }


    public Page<CollectionPointDtoResp> getMyCollectionPoints(
            String email,
            Pageable pageable
    ) {

        Merchant merchant =
                merchantRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Merchant avec l'email "
                                                + email
                                                + " est introuvable"
                                )
                        );

        Page<CollectionPoint> collectionPoints =
                collectionPointRepository.findByMerchant(
                        merchant,
                        pageable
                );

        return collectionPoints.map(
                collectionPointMapper::toResponse
        );
    }


    public CollectionPointDtoResp updateCollectionPoint(
            String email,
            Long id,
            CollectionPointDtoReq collectionPointDtoReq
    ) {

        Merchant merchant =
                merchantRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Merchant avec l'email "
                                                + email
                                                + " est introuvable"
                                )
                        );

        CollectionPoint collectionPoint =
                collectionPointRepository
                        .findByIdAndMerchant(
                                id,
                                merchant
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Collection Point avec l'ID "
                                                + id
                                                + " est introuvable"
                                )
                        );

        collectionPoint.setAddress(
                collectionPointDtoReq.getAddress()
        );

        collectionPoint.setLatitude(
                collectionPointDtoReq.getLatitude()
        );

        collectionPoint.setLongitude(
                collectionPointDtoReq.getLongitude()
        );

        CollectionPoint updatedCollectionPoint =
                collectionPointRepository.save(
                        collectionPoint
                );

        return collectionPointMapper.toResponse(
                updatedCollectionPoint
        );
    }


    public void deleteCollectionPoint(
            String email,
            Long id
    ) {

        Merchant merchant =
                merchantRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Merchant avec l'email "
                                                + email
                                                + " est introuvable"
                                )
                        );

        CollectionPoint collectionPoint =
                collectionPointRepository
                        .findByIdAndMerchant(
                                id,
                                merchant
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Collection Point avec l'ID "
                                                + id
                                                + " est introuvable pour ce merchant"
                                )
                        );

        collectionPointRepository.delete(
                collectionPoint
        );
    }
}