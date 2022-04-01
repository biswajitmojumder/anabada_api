package com.anabada.anabada_api.dto;


import com.anabada.anabada_api.domain.DeliveryVO;
import com.anabada.anabada_api.domain.item.ItemVO;
import com.anabada.anabada_api.domain.pay.PaymentOptionVO;
import com.anabada.anabada_api.dto.item.ItemDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryDTO {

    private  String address;
    private Long idx;

    private Long state;

    @NotBlank(groups ={ValidationGroups.deliveryRequestGroup.class},message = "연락처가 입력되지않았습니다.")
    private String phone;

    @NotBlank(groups ={ValidationGroups.deliveryRequestGroup.class},message = "수령자 이름이 입력되지않았습니다.")
    private String receiverName;

    private boolean clauseAgree;

    private String trackingNumber;

    private LocalDateTime createdAt;

    private LocalDateTime dueAt;


    ItemDTO item;

    @Builder
    public DeliveryDTO(Long idx,
                       String address,Long state, LocalDateTime createdAt, LocalDateTime dueAt, String phone, String receiverName, boolean clauseAgree, String trackingNumber, ItemDTO item){
        this.idx=idx;
        this.state=state;
        this.createdAt=createdAt;
        this.dueAt=dueAt;
        this.phone=phone;
        this.address=address;
        this.receiverName=receiverName;
        this.clauseAgree=clauseAgree;
        this.trackingNumber=trackingNumber;
        this.item=item;
    }

    public DeliveryVO toEntity(){
        return DeliveryVO.builder()
                .state(this.state)
                .address(this.address)
                .dueAt(this.dueAt)
                .phone(this.phone)
                .receiverName(this.receiverName)
                .clauseAgree(this.clauseAgree)
                .trackingNumber(this.trackingNumber)
                .build();
    }





}
