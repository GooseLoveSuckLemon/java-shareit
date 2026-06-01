package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerId(Long bookerId);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long bookerId,
                                                          LocalDateTime start,
                                                          LocalDateTime end);

    List<Booking> findByBookerIdAndEndBefore(Long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartAfter(Long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = :ownerId")
    List<Booking> findAllByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = :ownerId AND b.start < :now AND b.end > :now")
    List<Booking> findCurrentByOwnerId(@Param("ownerId") Long ownerId,
                                       @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = :ownerId AND b.end < :now")
    List<Booking> findPastByOwnerId(@Param("ownerId") Long ownerId,
                                    @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = :ownerId AND b.start > :now")
    List<Booking> findFutureByOwnerId(@Param("ownerId") Long ownerId,
                                      @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner = :ownerId AND b.status = :status")
    List<Booking> findByOwnerIdAndStatus(@Param("ownerId") Long ownerId,
                                         @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.item.id = :itemId AND b.end < :now AND b.status = :status ORDER BY b.end DESC")
    Optional<Booking> findFirstByBookerIdAndItemIdAndEndBeforeAndStatus(
            @Param("bookerId") Long bookerId,
            @Param("itemId") Long itemId,
            @Param("now") LocalDateTime now,
            @Param("status") BookingStatus status);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b WHERE b.booker.id = :bookerId AND b.item.id = :itemId AND b.end < :now AND b.status = :status")
    boolean existsByBookerIdAndItemIdAndEndBeforeAndStatus(
            @Param("bookerId") Long bookerId,
            @Param("itemId") Long itemId,
            @Param("now") LocalDateTime now,
            @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start < :now AND b.status = :status ORDER BY b.start DESC")
    Optional<Booking> findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(
            @Param("itemId") Long itemId,
            @Param("now") LocalDateTime now,
            @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.start > :now AND b.status = :status ORDER BY b.start ASC")
    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(
            @Param("itemId") Long itemId,
            @Param("now") LocalDateTime now,
            @Param("status") BookingStatus status);
}