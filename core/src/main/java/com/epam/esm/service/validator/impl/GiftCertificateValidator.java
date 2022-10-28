package com.epam.esm.service.validator.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.validator.Validator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GiftCertificateValidator implements Validator<GiftCertificate> {

    private static final int MAX_NAME_LENGTH = 45;
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final int MIN_DESCRIPTION_LENGTH = 3;
    private static final double MIN_PRICE_VALUE = 0;
    private static final int MIN_DURATION_VALUE = 1;
    @Override
    public boolean isValid(GiftCertificate giftCertificate) {
        if (!isValidName(giftCertificate.getName()) ||
            !isValidDescription(giftCertificate.getDescription()) ||
            !isValidPrice(giftCertificate.getPrice()) ||
            !isValidDuration(giftCertificate.getDuration())){
            return false;
        }
        return true;
    }

    public boolean isValidName(String name){
        if (name != null){
            return !(name.length() > MAX_NAME_LENGTH ||
                    name.length() < MIN_NAME_LENGTH);
        }
        return true;
    }

    public boolean isValidDescription(String description){
        if (description != null){
            return !(description.length() > MAX_DESCRIPTION_LENGTH ||
                    description.length() < MIN_DESCRIPTION_LENGTH);
        }
        return true;
    }

    public boolean isValidPrice(BigDecimal price){
        if (price !=null){
            return !(price.compareTo(BigDecimal.valueOf(MIN_PRICE_VALUE)) < 0);

        }
        return true;
    }

    public boolean isValidDuration(Integer duration){
        if (duration != null){
            return !(duration < MIN_DURATION_VALUE);
        }
        return true;
    }

}
