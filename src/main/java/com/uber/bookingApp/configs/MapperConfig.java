package com.uber.bookingApp.configs;

import com.uber.bookingApp.dto.PaymentDto;
import com.uber.bookingApp.dto.PointDto;
import com.uber.bookingApp.model.Payment;
import com.uber.bookingApp.utils.GeometryUtils;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(PointDto.class , Point.class).setConverter(
                context-> {
                    PointDto pointDto = context.getSource();
                    return GeometryUtils.buildPoint(pointDto);
                }
        );

        modelMapper.typeMap(Point.class , PointDto.class).setConverter(
                context-> {
                    Point point = context.getSource();
                    return PointDto.builder()
                            .longitude(point.getX())
                            .latitude(point.getY())
                            .type("Point")
                            .build();
                }
        );

        modelMapper.typeMap(Payment.class, PaymentDto.class).setConverter(
                context-> {
                    Payment payment = context.getSource();
                    if(payment == null) return null;
                    return PaymentDto.builder()
                            .paymentMethod(payment.getPaymentMethod())
                            .amount(payment.getAmount())
                            .paymentTime(payment.getPaymentTime())
                            .paymentStatus(payment.getPaymentStatus())
                            .paymentUrl(payment.getPaymentUrl())
                            .transactionId(payment.getTransactionId())
                            .build();
                }

        );
        return modelMapper;
    }


}
