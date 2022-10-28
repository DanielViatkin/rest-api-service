package com.epam.esm.service.logic.order;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import com.epam.esm.repository.impl.OrderRepositoryImpl;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.exception.InvalidPagebaleParametersException;
import com.epam.esm.service.exception.NoAuthoritiesException;
import com.epam.esm.service.exception.NotFoundEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final GiftCertificateRepository certificateRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepositoryImpl orderRepository,
                            GiftCertificateRepositoryImpl certificateRepository,
                            UserRepositoryImpl userRepository){
        this.orderRepository = orderRepository;
        this.certificateRepository = certificateRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Order create(Long certificateId, Long userId) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User authUser = userRepository.findByLogin(login).get();
        String roleName =authUser.getRoles().iterator().next().getName();

        Order order = new Order();
        GiftCertificate certificate =
                certificateRepository.findById(certificateId).
                        orElseThrow(() -> new NotFoundEntityException("certificate.not.found",certificateId));
        User user = userRepository.findById(userId).
                        orElseThrow(() -> new NotFoundEntityException("user.not.found",userId));
        if (!isAdmin(roleName)){
            isUserOrderCheck(authUser.getId(), userId, "no.self.order.create", "40303");
        }
        order.setUser(user);
        order.setCertificate(certificate);
        order.setCost(certificate.getPrice());
        order.setPurchaseTime(Instant.now());
        return orderRepository.create(order);
    }

    @Override
    public Order getOrderById(long id){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User authUser = userRepository.findByLogin(login).get();
        String roleName =authUser.getRoles().iterator().next().getName();

        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("order.not.found", id));
        if (!isAdmin(roleName)){
            isUserOrderCheck(authUser.getId(), order.getUser().getId(),"no.self.order.check", "40302");
        }
        return order;
    }

    private void isUserOrderCheck(long userDbId, long orderUserId, String message, String code){
        if(userDbId != orderUserId){
            throw new NoAuthoritiesException(message, code);
        }
    }

    private boolean isAdmin(String roleName){
        return roleName.equals(com.epam.esm.model.security.Role.ADMIN.name());
    }


    @Override
    public List<Order> getOrdersByUserId(long userId, int page, int size) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User authUser = userRepository.findByLogin(login).get();
        String roleName =authUser.getRoles().iterator().next().getName();

        if (!isAdmin(roleName)){
            isUserOrderCheck(authUser.getId(), userId, "no.self.order.check", "40302");
        }

        Pageable pageable = getPageble(page, size);
        return orderRepository.getAllByUserId(userId, pageable);
    }

    private Pageable getPageble(int page, int size){
        Pageable pageble;
        try {
            pageble = PageRequest.of(page, size);
        } catch (IllegalArgumentException e) {
            throw new InvalidPagebaleParametersException("pageable.invalid.data",e);
        }
        return pageble;
    }
}
