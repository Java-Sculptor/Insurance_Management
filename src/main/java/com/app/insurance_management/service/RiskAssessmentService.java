
package com.app.insurance_management.service;

import com.app.insurance_management.dto.RiskAssessmentRequestDto;
import com.app.insurance_management.dto.RiskAssessmentResponseDto;

public interface RiskAssessmentService {

    RiskAssessmentResponseDto assessRisk(RiskAssessmentRequestDto requestDto);

    RiskAssessmentResponseDto getRiskAssessment(Long quoteId);
}
