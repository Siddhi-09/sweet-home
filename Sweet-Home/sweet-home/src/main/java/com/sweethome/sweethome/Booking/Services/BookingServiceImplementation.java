package com.sweethome.sweethome.Booking.Services;

import com.sweethome.sweethome.Booking.DAO.BookingInfoEntityDAO;
import com.sweethome.sweethome.Booking.DTO.TransactionDetailsEntityDTO;
import com.sweethome.sweethome.Booking.Entities.BookingInfoEntity;
import com.sweethome.sweethome.Booking.exceptions.BookingIDNotFound;
import com.sweethome.sweethome.Booking.exceptions.InvalidPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class BookingServiceImplementation implements  BookingService {

    private BookingInfoEntityDAO bookingInfoEntityDAO;
    private RestTemplate restTemplate;

    @Autowired
    public BookingServiceImplementation(BookingInfoEntityDAO bookingInfoEntityDAO, RestTemplate restTemplate) {
        this.bookingInfoEntityDAO = bookingInfoEntityDAO;
        this.restTemplate = restTemplate;
    }

    @Value("${transaction.url}")
    private String transactionUrl;

    @Override
    public BookingInfoEntity book(BookingInfoEntity bookingInfoEntity) {
        bookingInfoEntity.setRoomPrice(roomPrice(bookingInfoEntity.getNumOfRooms(), bookingDays(bookingInfoEntity.getFromDate(), bookingInfoEntity.getToDate())));
        bookingInfoEntity.setRoomNumbers(roomNumbers(bookingInfoEntity.getNumOfRooms()));

        bookingInfoEntity.setBookedOn(LocalDateTime.now());
        return bookingInfoEntityDAO.save(bookingInfoEntity);
    }

    @Override
    public BookingInfoEntity getBookingbasedonId(int id) {
        return bookingInfoEntityDAO.getById(id);
    }

    @Override
    public List<BookingInfoEntity> getAllBookings() {
        return bookingInfoEntityDAO.findAll();
    }

    @Override
    public void deleteBookingID(BookingInfoEntity bookingInfoEntity) {
        bookingInfoEntityDAO.delete(bookingInfoEntity);
    }

    @Override
    public BookingInfoEntity updateBooking(BookingInfoEntity bookingInfoEntity) {
        return bookingInfoEntityDAO.save(bookingInfoEntity);

    }

    public int roomPrice(int numofRooms, int numberOfDays) {
        int roomPrice = 1000 * numofRooms * numberOfDays;
        return roomPrice;
    }

    public static String roomNumbers(int count) {
        Random random = new Random();
        int upperBound = 100;
        ArrayList<String> numberList = new ArrayList<String>();

        for (int i = 0; i < count; i++) {
            numberList.add(String.valueOf(random.nextInt(upperBound)));
        }
        String roomNumbers = numberList.toString();
        return roomNumbers.substring(1, roomNumbers.length() - 1);
    }

    private int bookingDays(LocalDate fromDate, LocalDate toDate) {
        int days = (int) ChronoUnit.DAYS.between(fromDate, toDate);
        return days;
    }


    public BookingInfoEntity makeTransaction(TransactionDetailsEntityDTO transactionDetailsEntityDTO, int ID) throws InvalidPayment, BookingIDNotFound
    {

        if (!(transactionDetailsEntityDTO.getPaymentMode().equalsIgnoreCase("CARD") ||
                transactionDetailsEntityDTO.getPaymentMode().equalsIgnoreCase("UPI"))) {
            throw new InvalidPayment("Invalid mode of Payment " + transactionDetailsEntityDTO.getPaymentMode());
        }

        if (!(bookingInfoEntityDAO.exsistByBookingId(ID))) {
            throw new BookingIDNotFound("Invalid Booking Id " + ID);
        } else if (!(bookingInfoEntityDAO.exsistByBookingId(transactionDetailsEntityDTO.getBookingID()))) {
            throw new BookingIDNotFound("Invalid Booking Id " + transactionDetailsEntityDTO.getBookingID());
        }
        Map<String, String> bookUriMap = new HashMap<>();

        bookUriMap.put("bookingId", String.valueOf(ID));
        bookUriMap.put("paymentMode", transactionDetailsEntityDTO.getPaymentMode());
        bookUriMap.put("upiId", transactionDetailsEntityDTO.getUpiId());
        bookUriMap.put("cardNumber",transactionDetailsEntityDTO.getCardNumber());


        int transactionId = restTemplate.postForObject(transactionUrl, bookUriMap, Integer.class);

        BookingInfoEntity bookingInfoEntity = bookingInfoEntityDAO.getById(ID);
        bookingInfoEntity.setTransactionId(transactionId);

        String message = "Booking confirmed for user with aadhar number:"
                + bookingInfoEntity.getAadharNumber()
                + "    |     "
                + "Here are the booking details:    " + bookingInfoEntity.toString();
        System.out.println(message);

        return bookingInfoEntityDAO.save(bookingInfoEntity);
    }
}
