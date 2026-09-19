package org.laicose.nexadelivery.configuration;

import lombok.RequiredArgsConstructor;
import org.laicose.nexadelivery.Enum.DeliveryStatus;
import org.laicose.nexadelivery.model.Delivery;
import org.laicose.nexadelivery.repository.DeliveryRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final DeliveryRepository deliveryRepository;


    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );

        if (accessor == null) {
            return message;
        }

        

        if (StompCommand.CONNECT.equals(
                accessor.getCommand()
        )) {

            String authHeader =
                    accessor.getFirstNativeHeader(
                            "Authorization"
                    );

            if (
                    authHeader == null ||
                            !authHeader.startsWith("Bearer ")
            ) {

                throw new RuntimeException(
                        "Token JWT manquant dans WebSocket"
                );
            }


            String token =
                    authHeader.substring(7);


            String email =
                    jwtUtil.extractUsername(token);


            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(email);


            if (!jwtUtil.validateToken(
                    token,
                    userDetails
            )) {

                throw new RuntimeException(
                        "Token JWT WebSocket invalide"
                );
            }


            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );


            accessor.setUser(authentication);
        }




        if (StompCommand.SUBSCRIBE.equals(
                accessor.getCommand()
        )) {

            if (accessor.getUser() == null) {

                throw new RuntimeException(
                        "Utilisateur WebSocket non authentifié"
                );
            }


            Authentication authentication =
                    (Authentication) accessor.getUser();


            String destination =
                    accessor.getDestination();


            if (destination == null) {
                return message;
            }




            if (
                    destination.startsWith(
                            "/topic/deliveries/"
                    )
                            &&
                            destination.endsWith(
                                    "/location"
                            )
            ) {



                boolean isAdmin =
                        authentication
                                .getAuthorities()
                                .stream()
                                .anyMatch(authority ->
                                        authority
                                                .getAuthority()
                                                .equals(
                                                        "ROLE_ADMIN"
                                                )
                                );


                boolean isMerchant =
                        authentication
                                .getAuthorities()
                                .stream()
                                .anyMatch(authority ->
                                        authority
                                                .getAuthority()
                                                .equals(
                                                        "ROLE_MERCHANT"
                                                )
                                );


                boolean isDriver =
                        authentication
                                .getAuthorities()
                                .stream()
                                .anyMatch(authority ->
                                        authority
                                                .getAuthority()
                                                .equals(
                                                        "ROLE_DRIVER"
                                                )
                                );




                if (isAdmin) {

                    return message;
                }




                if (isDriver) {

                    throw new RuntimeException(
                            "Un driver ne peut pas suivre une livraison"
                    );
                }



                if (isMerchant) {




                    String idPart =
                            destination
                                    .replace(
                                            "/topic/deliveries/",
                                            ""
                                    )
                                    .replace(
                                            "/location",
                                            ""
                                    );


                    Long deliveryId;

                    try {

                        deliveryId =
                                Long.valueOf(idPart);

                    } catch (NumberFormatException e) {

                        throw new RuntimeException(
                                "Destination WebSocket invalide"
                        );
                    }




                    Delivery delivery =
                            deliveryRepository
                                    .findById(deliveryId)
                                    .orElseThrow(() ->
                                            new RuntimeException(
                                                    "Delivery introuvable"
                                            )
                                    );




                    String merchantEmail =
                            delivery
                                    .getMerchant()
                                    .getEmail();


                    if (!merchantEmail.equals(
                            authentication.getName()
                    )) {

                        throw new RuntimeException(
                                "Vous n'êtes pas autorisé à suivre cette livraison"
                        );
                    }




                    List<DeliveryStatus>
                            trackingAllowedStatuses =
                            List.of(
                                    DeliveryStatus.ACCEPTEE,
                                    DeliveryStatus.RECUPEREE,
                                    DeliveryStatus.EN_ROUTE
                            );


                    if (!trackingAllowedStatuses.contains(
                            delivery.getDeliveryStatus()
                    )) {

                        throw new RuntimeException(
                                "Le suivi n'est pas disponible pour cette livraison"
                        );
                    }


                    return message;
                }




                throw new RuntimeException(
                        "Accès WebSocket non autorisé"
                );
            }
        }


        return message;
    }
}