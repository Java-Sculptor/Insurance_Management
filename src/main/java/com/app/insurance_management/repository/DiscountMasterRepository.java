
package com.app.insurance_management.repository;

import com.app.insurance_management.entity.DiscountMaster;
import com.app.insurance_management.enums.DiscountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiscountMasterRepository
        extends JpaRepository<DiscountMaster, Long> {

    List<DiscountMaster> findByStatusAndValidFromLessThanEqualAndValidToGreaterThanEqual(
            DiscountStatus status,
            LocalDate currentDate,
            LocalDate currentDate2
    );
}
