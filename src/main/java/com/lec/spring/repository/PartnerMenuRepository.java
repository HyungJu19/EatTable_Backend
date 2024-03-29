package com.lec.spring.repository;

import com.lec.spring.domain.Partner;
import com.lec.spring.domain.PartnerMenu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerMenuRepository extends JpaRepository<PartnerMenu,Long> {



        List<PartnerMenu> findByPartnerId(Long partenrId);

        // 정렬 메소드
        List<PartnerMenu> findByPartnerId(Long partenrId, Sort sort);
}
