package com.anabada.anabada_api.domain.item.repository;

import com.anabada.anabada_api.domain.item.entity.ItemCategoryVO;
import com.anabada.anabada_api.domain.item.entity.ItemVO;
import com.anabada.anabada_api.domain.user.entity.UserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ItemRepository extends JpaRepository<ItemVO, Long> {

    @EntityGraph(attributePaths = {"itemCategory", "registrant", "owner", "images"})
    public List<ItemVO> findByRegistrantAndState(UserVO registrant, int state);
    @EntityGraph(attributePaths = {"itemCategory", "registrant", "owner", "images"})
    public List<ItemVO> findByRegistrant(UserVO registrant);
    @EntityGraph(attributePaths = {"itemCategory", "images"})
    public List<ItemVO> findByOwnerAndState(UserVO Owner, int state);

    @EntityGraph(attributePaths = {"itemCategory", "images"})
    public List<ItemVO> findByOwner(UserVO Owner);

    @EntityGraph(attributePaths = {"payment", "delivery", "itemCategory", "images", "registrant", "owner"})
    Page<ItemVO> findByOwner(UserVO Owner, Pageable pageable);

    @EntityGraph(attributePaths = {"payment", "delivery", "itemCategory", "images", "registrant", "owner"})
    Page<ItemVO> findByRegistrant(UserVO Owner, Pageable pageable);

    @Query(value = "select e.idx from ItemVO e where e.state = :state")
    public List<Long> findIdxByState(int state);

    @EntityGraph(attributePaths = {"itemCategory", "images", "owner"})
    @Query(value = "select e from ItemVO e where e.idx IN (:idxList)")
    public List<ItemVO> findByIdxList(List<Long> idxList);

    public Optional<ItemVO> findByIdxAndState(Long idx, int state);

    @EntityGraph(attributePaths = {"payment", "delivery", "itemCategory", "images", "registrant", "owner"})
    public Optional<ItemVO> findWithAllByIdx(Long idx);

    @EntityGraph(attributePaths = {"payment", "delivery", "itemCategory", "images", "registrant", "owner"})
    Page<ItemVO> findByNameContains(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"payment", "delivery", "itemCategory", "images", "registrant", "owner"})
    Page<ItemVO> findByItemCategory(ItemCategoryVO categoryVO, Pageable pageable);
}
