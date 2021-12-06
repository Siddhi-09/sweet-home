package com.sweethome.sweethome.Booking.DAO;


import com.sweethome.sweethome.Booking.Entities.BookingInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingInfoEntityDAO extends JpaRepository<BookingInfoEntity,Integer> {

public  Boolean exsistByBookingId(int id);
}
