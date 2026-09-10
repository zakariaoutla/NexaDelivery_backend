package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.Enum.DeliveryStatus;
import org.laicose.nexadelivery.dto.request.RatingDtoReq;
import org.laicose.nexadelivery.dto.response.RatingDtoResp;
import org.laicose.nexadelivery.mapper.RatingMapper;
import org.laicose.nexadelivery.model.Delivery;
import org.laicose.nexadelivery.model.Rating;
import org.laicose.nexadelivery.repository.DeliveryRepository;
import org.laicose.nexadelivery.repository.RatingRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final DeliveryRepository deliveryRepository;

    public RatingDtoResp createRating(Long deliveryId, RatingDtoReq ratingDtoReq){
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(()->new RuntimeException("Delivery avec l'ID " + deliveryId + " est introuvable"));
        if (ratingRepository.existsByDeliveryId(deliveryId)) {
            throw new RuntimeException(
                    "Cette livraison possède déjà une évaluation"
            );
        }
        if (delivery.getDeliveryStatus() != DeliveryStatus.LIVREE) {
            throw new RuntimeException(
                    "Vous ne pouvez évaluer qu'une livraison livrée"
            );
        }

        Rating rating = ratingMapper.toEntityDto(ratingDtoReq);
        rating.setDelivery(delivery);
        Rating savedRating = ratingRepository.save(rating);

        return ratingMapper.toResponseDto(savedRating);
    }



}
