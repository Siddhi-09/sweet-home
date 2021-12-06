package com.sweethome.sweethome.Booking.Services;

import com.sweethome.sweethome.Booking.Entities.BookingInfoEntity;
import com.sweethome.sweethome.Booking.DTO.TransactionDetailsEntityDTO;
import com.sweethome.sweethome.Booking.exceptions.BookingIDNotFound;
import com.sweethome.sweethome.Booking.exceptions.InvalidPayment;

import java.util.List;

public interface BookingService {

    public BookingInfoEntity book(BookingInfoEntity bookingInfoEntity);
    public BookingInfoEntity getBookingbasedonId(int id);
    public List<BookingInfoEntity> getAllBookings();
    public void deleteBookingID(BookingInfoEntity bookingInfoEntity);
    public BookingInfoEntity updateBooking(BookingInfoEntity bookingInfoEntity);
    public BookingInfoEntity makeTransaction(TransactionDetailsEntityDTO transactionDetailsEntityDTO , int ID) throws InvalidPayment, BookingIDNotFound;


}
