package com.bookshopweb.dao;

import com.bookshopweb.beans.Cart;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(Cart.class)
// @RegisterBeanMapper(Cart.class): Sử dụng để ánh xạ các kết quả truy vấn SQL trực tiếp với lớp Cart (bean).
// Khi thực thi các câu truy vấn trả về dữ liệu từ bảng cart, dữ liệu sẽ tự động ánh xạ vào các thuộc tính của lớp Cart.
public interface CartDAO extends DAO<Cart> {
    @Override
    @SqlUpdate("INSERT INTO cart VALUES (default, :userId, :createdAt, :updatedAt)")
    @GetGeneratedKeys("id")
    long insert(@BindBean Cart cart);

    @Override
    @SqlUpdate("UPDATE cart SET userId = :userId, createdAt = :createdAt, updatedAt = :updatedAt WHERE id = :id")
    void update(@BindBean Cart cart);

    @Override
    @SqlUpdate("DELETE FROM cart WHERE id = :id")
    void delete(@Bind("id") long id);

    @Override
    @SqlQuery("SELECT * FROM cart WHERE id = :id")
    Optional<Cart> getById(@Bind("id") long id);

    @Override
    @SqlQuery("SELECT * FROM cart")
    List<Cart> getAll();

    @Override
    @SqlQuery("SELECT * FROM cart LIMIT :limit OFFSET :offset")
    List<Cart> getPart(@Bind("limit") int limit, @Bind("offset") int offset);

    @Override
    @SqlQuery("SELECT * FROM cart ORDER BY <orderBy> <orderDir> LIMIT :limit OFFSET :offset")
    List<Cart> getOrderedPart(@Bind("limit") int limit, @Bind("offset") int offset,
                              @Define("orderBy") String orderBy, @Define("orderDir") String orderDir);

    @SqlQuery("SELECT * FROM cart WHERE userId = :userId")
    Optional<Cart> getByUserId(@Bind("userId") long userId);

    @SqlQuery("SELECT SUM(ci.quantity) FROM cart c JOIN cart_item ci ON c.id = ci.cartId WHERE c.userId = :userId")
    int countCartItemQuantityByUserId(@Bind("userId") long userId);

    @SqlQuery("SELECT COUNT(orders.id) FROM orders WHERE userId = :userId")
    int countOrderByUserId(@Bind("userId") long userId);

    @SqlQuery("SELECT COUNT(orders.id) FROM orders WHERE userId = :userId AND status = 1")
    int countOrderDeliverByUserId(@Bind("userId") long userId);

    @SqlQuery("SELECT COUNT(orders.id) FROM orders WHERE userId = :userId AND status = 2")
    int countOrderReceivedByUserId(@Bind("userId") long userId);
}
