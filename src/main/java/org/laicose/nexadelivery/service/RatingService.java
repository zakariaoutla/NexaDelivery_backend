package org.laicose.nexadelivery.service;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.Enum.DeliveryStatus;
import org.laicose.nexadelivery.dto.request.RatingDtoReq;
import org.laicose.nexadelivery.dto.response.RatingDtoResp;
import org.laicose.nexadelivery.mapper.RatingMapper;
import org.laicose.nexadelivery.model.Delivery;
import org.laicose.nexadelivery.model.Driver;
import org.laicose.nexadelivery.model.Merchant;
import org.laicose.nexadelivery.model.Rating;
import org.laicose.nexadelivery.repository.DeliveryRepository;
import org.laicose.nexadelivery.repository.DriverRepository;
import org.laicose.nexadelivery.repository.MerchantRepository;
import org.laicose.nexadelivery.repository.RatingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;
    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final MerchantRepository merchantRepository;

    @Transactional
    public RatingDtoResp createRating(Long deliveryId, RatingDtoReq ratingDtoReq, String email){
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
        Merchant merchant = merchantRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Merchant avec l'email " + email + " est introuvable"
                        )
                );

        if (delivery.getMerchant().getId() != merchant.getId()) {
            throw new RuntimeException(
                    "Vous n'êtes pas autorisé à évaluer cette livraison"
            );
        }

        Rating rating = ratingMapper.toEntityDto(ratingDtoReq);
        rating.setDelivery(delivery);
        Rating savedRating = ratingRepository.save(rating);
        updateDriverAverageRating(deliveryId);

        return ratingMapper.toResponseDto(savedRating);
    }

    public RatingDtoResp findRatingById(Long id){
        Rating rating = ratingRepository.findById(id).orElseThrow(()-> new RuntimeException("Rating avec l'ID " + id + " est introuvable"));
        return ratingMapper.toResponseDto(rating);
    }

    public RatingDtoResp findRatingByDeliveryId(
            Long deliveryId,
            String email
    ) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Delivery avec l'ID " + deliveryId + " est introuvable"
                        )
                );

        Merchant merchant = merchantRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Merchant avec l'email " + email + " est introuvable"
                        )
                );

        if (delivery.getMerchant().getId() != merchant.getId()) {
            throw new RuntimeException(
                    "Vous n'êtes pas autorisé à consulter cette évaluation"
            );
        }

        Rating rating = ratingRepository.findByDeliveryId(deliveryId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cette livraison n'a pas encore d'évaluation"
                        )
                );

        return ratingMapper.toResponseDto(rating);
    }

    public Page<RatingDtoResp> findAllRating(Pageable pageable){
        Page<Rating> ratings = ratingRepository.findAll(pageable);
        return ratings.map(ratingMapper::toResponseDto);
    }

    @Transactional
    public RatingDtoResp updateRating(Long ratingId, RatingDtoReq ratingDtoReq, String email){

        Rating rating = ratingRepository.findById(ratingId).orElseThrow(()-> new RuntimeException("Rating avec l'ID " + ratingId + " est introuvable"));
        Merchant merchant = merchantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Merchant avec l'email " + email + " est introuvable"
                ));

        if (rating.getDelivery().getMerchant().getId() != merchant.getId()) {
            throw new RuntimeException(
                    "Vous n'êtes pas autorisé à modifier cette évaluation"
            );
        }

        rating.setComment(ratingDtoReq.getComment());
        rating.setScore(ratingDtoReq.getScore());

        Rating savedRating = ratingRepository.save(rating);
        updateDriverAverageRating(rating.getDelivery().getId());

        return ratingMapper.toResponseDto(savedRating);
    }

    @Transactional
    public void deleteRating(Long id){
        Rating rating = ratingRepository.findById(id).orElseThrow(()-> new RuntimeException("Rating avec l'ID " + id + " est introuvable"));
        ratingRepository.delete(rating);
        ratingRepository.flush();

        updateDriverAverageRating(rating.getDelivery().getId());
    }

    private void updateDriverAverageRating(Long deliveryId){
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(()->new RuntimeException("Delivery avec l'ID " + deliveryId + " est introuvable"));

        if (delivery.getDriver() != null){
            Driver driver = delivery.getDriver();

            Double average = ratingRepository.findAverageByDriverId(driver.getId());

            driver.setAverageRating(
                    average !=null ? average : 0.0
            );
            driverRepository.save(driver);

        }
    }






}
