package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order_simpleQuery.OrderSimpleQueryRepository;
import jpabook.jpashop.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {

        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        //hibernate5Module FORCE_LAZY_LOADING 안쓰고 강제로 프록시 객체 초기화
        for(Order order : all) {
            order.getMember().getName(); //LAZY 강제 초기화
            order.getDelivery().getAddress(); //LAZY 강제 초기화
        }

        return all;
    }

    /**
     * * v1 *
     * 문제1) 무한루프ㅠㅠ 순환참조 -> 둘 중 하나 JsonIgnore 로 순환 참조 끊어줘야함
     * 문제2) 지연로딩-프록시 객체 생성 -> ByteBuddyInterceptor() 프록시 초기화 -> json으로 반환하지 못함
     *        Hibernate5Module 빈으로 등록하여 해걸 (이것도 지양!)
     * 문제3) 엔티티 직접 노출 -> api 스펙 관련 문제, 성능 저하
     */


    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {

        return orderRepository.findAllByString(new OrderSearch()).stream()
                .map(o -> new SimpleOrderDto(o))
//                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * * v2 *
     * 문제1) Eager Loading 으로 인한 성능 저하
     * 문제2) N+1 문제 - 1(처음 조회 쿼리) + N(해당 결과만큼의 쿼리)*연관된 객체 수
     *       -> EAGER로 해결 x
     *
     * 기본으로 지연로딩, 필요한 경우 fetch join
     */


    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {

        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * * v3 *
     * fetch join으로 쿼리 하나에 해결
     * 문제1) select절에 모든 컬럼 포함
     */


    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {

        return orderSimpleQueryRepository.findOrderDtos();
    }

    /**
     * * v4 *
     * 일반적인 sql처럼 select절에 원하는 컬럼 직접 작성
     * 성능 좀 더 최적화
     * 문제) 레퍼지토리 재사용성이 떨어짐 -api스펙에 맞춰진 쿼리
     * Dto로 조회하였기에 직접 변경 x
     *
     * v3과 v4 성능이 크게 차이나지 않음
     * join절이나 where절에서 더 성능차이가 큼
     */


    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getMember().getAddress(); //LAZY 초기화
        }
    }
}
