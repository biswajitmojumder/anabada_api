package com.anabada.anabada_api.dto.room;


import com.anabada.anabada_api.dto.user.UserDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomUserDTO {
    private Long idx;
    private UserDTO user;
    private RoomDTO room;

    @Builder
    public RoomUserDTO(Long idx,UserDTO user,RoomDTO room){
        this.idx=idx;
        this.room=room;
        this.user=user;
    }
}
