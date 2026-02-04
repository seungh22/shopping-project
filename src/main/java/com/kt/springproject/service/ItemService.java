package com.kt.springproject.service;

import com.kt.springproject.domain.item.Item;
import com.kt.springproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    //todo: 단발성 업데이트 안 좋음 -> 엔티티 레벨에서 보고 어디서 바뀌는지 쉽게 추적할 수 있게 메서드를 만들자 -> 그래서 setter 쓰지 말자
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId); //Id를 기반으로 실제 디비에 있는 영속 상태 엔티티를 찾아옴
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
//        itemRepository.save(findItem);
        // 이걸 할 필요가 없다 -> 엔티티 매니저의 persist를 호출할 필요 없음 왜? findItem으로 찾아온 엔티티는 영속 상태
        // 값을 세팅한 다음엔 @Transactional을 통해 트랜잭션이 커밋됨 커밋되면 JPA가 flush를 날린다
        // flush를 날린다? -> 영속성 컨택스트에 있는 인티티중에 변경된 얘가 뭔지 찾아서 바뀌었으면 업데이트 쿼리를 디비에 날려서 업데이트를 함
        // => 이게 더티체킹
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
