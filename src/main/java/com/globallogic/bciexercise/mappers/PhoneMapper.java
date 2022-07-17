package com.globallogic.bciexercise.mappers;

import com.globallogic.bciexercise.domain.Phone;
import com.globallogic.bciexercise.dtos.PhoneRequest;
import com.globallogic.bciexercise.dtos.PhoneResponse;

public class PhoneMapper {

    public static Phone toEntity(PhoneRequest request) {
        Phone phone = new Phone();
        phone.setNumber(request.getNumber());
        phone.setCityCode(request.getCityCode());
        phone.setCountryCode(request.getCountryCode());
        return phone;
    }

    public static PhoneResponse toResponse(Phone phone) {
        PhoneResponse response = new PhoneResponse();
        response.setNumber(phone.getNumber());
        response.setCityCode(phone.getCityCode());
        response.setCountryCode(phone.getCountryCode());
        return response;
    }
}
