package com.lec.spring.controller;


import com.lec.spring.domain.DTO.PartnerReqDto;
import com.lec.spring.domain.*;
import com.lec.spring.domain.DTO.PartnerWriteDto;
import com.lec.spring.repository.UserHistoryRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.service.PartnerReqService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/req")
@CrossOrigin
public class PartnerReqController {

    private final PartnerReqService partnerReqService;
    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;


    //전체리스트 ( 필요시)
    @Transactional
    @GetMapping("/totalList")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> totalList(){
        return new ResponseEntity<>(partnerReqService.list(), HttpStatus.OK);
    }

    @Transactional
    @GetMapping("/totalListPage")
// @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> totalList(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = Pageable.unpaged(); // 페이지 크기를 지정하지 않음
        return new ResponseEntity<>(partnerReqService.pageList(pageable), HttpStatus.OK);
    }





    //신청작성
    @Transactional
    @PostMapping("/write")
    public ResponseEntity<?> write(@RequestBody PartnerReqDto partnerReqDto){

        Long userId = partnerReqDto.getUserId();
        PartnerReq partnerReq = partnerReqDto (partnerReqDto);
        // 서비스 메서드 호출
        PartnerReq savedPartnerReq = partnerReqService.write(partnerReq, userId);

        User user = userRepository.findById(partnerReqDto.getUserId()).orElse(null);
        UserHistory userHistory = new UserHistory();
        userHistory.setName(String.format("%s님이 업체 등록을 신청하였습니다.", user.getUsername()));

        userHistoryRepository.save(userHistory);

        return new ResponseEntity<>(savedPartnerReq, HttpStatus.CREATED);
    }

    private PartnerReq partnerReqDto(PartnerReqDto partnerReqDto) {
        return PartnerReq.builder()
                .managerName(partnerReqDto.getManagerName())
                .storeName(partnerReqDto.getStoreName())
                .phone(partnerReqDto.getPhone())
                .memo(partnerReqDto.getMemo())
                .partnerReqState(PartnerReqState.OPEN_READY)
                // 나머지 필드 설정은 각 필드에 대한 빌더 메서드를 호출하여 추가
                .build();
    }
    //작성 수정?? 상의
    @Transactional
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody PartnerReq partnerReq){
        return new ResponseEntity<>(partnerReqService.update(partnerReq),HttpStatus.OK);
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PartnerReq partnerReq) {
        partnerReq.setId(id);
        return new ResponseEntity<>(partnerReqService.update(partnerReq), HttpStatus.OK);
    }

    //신청 삭제 ( 마음바뀜)
    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return new ResponseEntity<>(partnerReqService.delete(id),HttpStatus.OK);
    }



    @PostMapping("/cancelPartner")
    public ResponseEntity<?> cancelPatner(@RequestBody Map<String, Long> requestBody){
        Long id = requestBody.get("userId");

        return new ResponseEntity<>(partnerReqService.cancelPartner(id), HttpStatus.CREATED);

    }
}
