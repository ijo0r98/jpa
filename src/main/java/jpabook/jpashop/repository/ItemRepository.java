package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item); //새로 생성된 아이템
        } else {
            em.merge(item); //원래 있던 아이템 -> update
            /** merge(병합) -->지양
             *
             * 파라미터로 넘어온 값을 받아서 영속성 컨텍스트 생성하여 수정
             *
             * 파라미터의 모든 값을 받아 넘김으로 원하는 값만 수정x
             */
        }
    }


    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
