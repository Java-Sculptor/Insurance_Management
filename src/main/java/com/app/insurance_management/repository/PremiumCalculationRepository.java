
package com.app.insurance_management.repository;

import com.app.insurance_management.entity.PremiumCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PremiumCalculationRepository
        extends JpaRepository<PremiumCalculation, Long> {

    Optional<PremiumCalculation> findByQuote_QuoteId(Long quoteId);


}
