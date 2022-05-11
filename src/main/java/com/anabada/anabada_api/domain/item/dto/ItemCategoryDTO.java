package com.anabada.anabada_api.domain.item.dto;


import com.anabada.anabada_api.domain.item.entity.ItemCategoryVO;
import com.anabada.anabada_api.domain.etc.dto.ValidationGroups;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemCategoryDTO {

    private Long idx;
//    private ItemCategoryDTO upperCategory;
    private String name;

    @Builder
    public ItemCategoryDTO(ItemCategoryDTO upperCategory, String name) {
        this.name = name;
    }

    public static ItemCategoryDTO fromEntity(ItemCategoryVO entity){
        return ItemCategoryDTO.builder()
                .name(entity.getName())
                .build();
    }
}
