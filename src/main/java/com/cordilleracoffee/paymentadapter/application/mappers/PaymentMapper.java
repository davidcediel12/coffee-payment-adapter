package com.cordilleracoffee.paymentadapter.application.mappers;

import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayRequest;
import com.cordilleracoffee.paymentadapter.application.dto.PaymentGatewayResponse;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentRequest;
import com.cordilleracoffee.paymentadapter.infrastructure.dto.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "cardHolderName", source = "cardHolder")
    @Mapping(target = "orderId", source = "cartId")
    PaymentGatewayRequest toGatewayRequest(PaymentRequest paymentRequest);


    @Mapping(target = "cartId", source = "orderId")
    PaymentResponse toResponse(PaymentGatewayResponse paymentGatewayResponse);
}
