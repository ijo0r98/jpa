package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    //상품 저장
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    //상품 전체 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    //상품 아이디로 조회
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    //상품 수정 -영속성 컨텍스트 이용
//    @Transactional
//    public void updateItem(Long itemId, Book bookparam) {
//        //변경 지점을 엔티티(의미있는 메서드 생성!)로 할것
//        //setter 남발 지양,,
//        Item findItem = itemRepository.findOne(itemId);
//        findItem.setPrice(bookparam.getPrice());
//        findItem.setName(bookparam.getName());
//        findItem.setStockQuantity(bookparam.getStockQuantity());
//
//        //save 호출 x
//        //findItemd이 현재 영속상태 임으로 바로 변경됨
//        //영속성 컨텍스트 -> flush 바로, update 쿼리 실행
//    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        //위의 방법보다 이 방법 더 좋음
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
    }
}
