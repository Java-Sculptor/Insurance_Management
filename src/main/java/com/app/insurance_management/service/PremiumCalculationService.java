
package com.app.insurance_management.service;

import com.app.insurance_management.dto.PremiumCalculationRequestDto;
import com.app.insurance_management.dto.PremiumCalculationResponseDto;

public interface PremiumCalculationService {

    PremiumCalculationResponseDto calculatePremium(
            PremiumCalculationRequestDto requestDto);

    PremiumCalculationResponseDto getPremiumByQuoteId(Long quoteId);
}
