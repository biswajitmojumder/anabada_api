package com.anabada.anabada_api.domain.delivery.service;

import com.anabada.anabada_api.domain.delivery.dto.CreateDelivery;
import com.anabada.anabada_api.domain.delivery.dto.RegisterTracking;
import com.anabada.anabada_api.domain.delivery.entity.DeliveryCompanyVO;
import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.delivery.repository.DeliveryRepository;
import com.anabada.anabada_api.domain.item.service.ItemFindService;
import com.anabada.anabada_api.domain.item.service.ItemUpdateService;
import com.anabada.anabada_api.domain.message.service.RoomUpdateService;
import com.anabada.anabada_api.exception.ApiException;
import com.anabada.anabada_api.exception.ExceptionEnum;
import com.sun.jdi.request.DuplicateRequestException;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.AuthException;
import java.time.LocalDateTime;

@Service
public class DeliveryUpdateService {

    DeliveryRepository deliveryRepository;

    ItemFindService itemFindService;
    ItemUpdateService itemUpdateService;
    RoomUpdateService roomUpdateService;
    DeliveryFindService deliveryFindService;
    DeliveryCompanyFindService deliveryCompanyFindService;

    public DeliveryUpdateService(DeliveryRepository deliveryRepository, ItemFindService itemFindService, ItemUpdateService itemUpdateService, RoomUpdateService roomUpdateService, DeliveryFindService deliveryFindService, DeliveryCompanyFindService deliveryCompanyFindService) {
        this.deliveryRepository = deliveryRepository;
        this.itemFindService = itemFindService;
        this.itemUpdateService = itemUpdateService;
        this.roomUpdateService = roomUpdateService;
        this.deliveryFindService = deliveryFindService;
        this.deliveryCompanyFindService = deliveryCompanyFindService;
    }


    @Transactional
    public DeliveryVO save(DeliveryVO vo) {
        return deliveryRepository.save(vo);
    }


    @Transactional
    public Long save(Long idx, CreateDelivery.Request request){

        ItemVO item = itemFindService.findByIdx(idx);

        if (item.getDelivery() != null)
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        DeliveryVO deliveryVO = DeliveryVO.builder()
                .receiverName(request.getReceiverName())
                .state(DeliveryVO.STATE.APPLIED.ordinal())
                .clauseAgree(request.isClauseAgree())
                .phone(request.getPhone())
                .address(request.getAddress())
                .dueAt(LocalDateTime.now().plusMonths(1))
                .item(item)
                .build();


        item.setDelivery(deliveryVO);
        roomUpdateService.save(deliveryVO);

        return deliveryVO.getIdx();
    }

    @Transactional
    public DeliveryVO saveTrackingNumber(Long itemIdx, RegisterTracking.Request request){

        ItemVO item = itemFindService.findByIdx(itemIdx);

        DeliveryVO delivery = item.getDelivery();

        if (delivery == null)
            throw new ApiException(ExceptionEnum.NOT_FOUND_EXCEPTION);

        if (delivery.getTrackingNumber() != null)
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);

        DeliveryCompanyVO company = deliveryCompanyFindService.findByIdx(request.getDeliveryCompanyIdx());

        delivery.setTrackingInfo(request.getTrackingNumber(), company);

        return delivery;
    }


}