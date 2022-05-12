package com.anabada.anabada_api.domain.item.service;

import com.anabada.anabada_api.domain.item.dto.CreateItem;
import com.anabada.anabada_api.domain.item.entity.ItemImageVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.item.dto.ItemDTO;
import com.anabada.anabada_api.domain.item.repository.ItemRepository;
import com.anabada.anabada_api.domain.item.entity.ItemCategoryVO;
import com.anabada.anabada_api.domain.pay.entity.PaymentVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import com.anabada.anabada_api.domain.pay.service.PaymentUpdateService;
import com.anabada.anabada_api.domain.user.service.UserFindService;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.message.AuthException;
import javax.transaction.NotSupportedException;
import java.io.IOException;
import java.util.List;

@Service
public class ItemUpdateService {

    ItemRepository itemRepository;

    UserFindService userFindService;
    PaymentUpdateService paymentUpdateService;
    CategoryFindService categoryFindService;
    ItemImageService itemImageService;
    ItemFindService itemFindService;


    public ItemUpdateService(ItemRepository itemRepository, UserFindService userFindService, PaymentUpdateService paymentUpdateService, CategoryFindService categoryFindService, ItemImageService itemImageService, ItemFindService itemFindService) {
        this.itemRepository = itemRepository;
        this.userFindService = userFindService;
        this.paymentUpdateService = paymentUpdateService;
        this.categoryFindService = categoryFindService;
        this.itemImageService = itemImageService;
        this.itemFindService = itemFindService;
    }


    @Transactional
    public ItemVO save(CreateItem.Request request, List<MultipartFile> mfList) {

        UserVO user = userFindService.getMyUserWithAuthorities();
        PaymentVO payment = paymentUpdateService.save(request.getPayment());
        ItemCategoryVO category = categoryFindService.getByIdx(request.getCategoryIdx());


        ItemVO item = ItemVO.builder()
                .name(request.getName())
                .description(request.getDescription())
                .clauseAgree(request.isClause())
                .payment(payment)
                .itemCategory(category)
                .deposit(payment.getAmount())
                .owner(user)
                .registrant(user)
                .state(ItemVO.STATE.APPLIED.ordinal())
                .build();

        ItemVO savedItem = itemRepository.save(item);

        Long i = 1L;
        for (MultipartFile mf : mfList) {
            ItemImageVO image = itemImageService.save(mf, savedItem, i);
            savedItem.addImage(image);
            i++;
        }

        return savedItem;
    }

    @Transactional
    public void activateItem(Long itemIdx, boolean isActivate) {
        ItemVO item = itemFindService.findByIdx(itemIdx);
        item.activate(isActivate);
    }


}