package com.sweethome.sweethome.Booking.exceptions.handler;

import com.sweethome.sweethome.Booking.DTO.ErrorResponseDTO;
import com.sweethome.sweethome.Booking.exceptions.BookingIDNotFound;
import com.sweethome.sweethome.Booking.exceptions.InvalidPayment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidPayment.class)
    public ResponseEntity<ErrorResponseDTO> invalidPaymentTypeException(){
        ErrorResponseDTO response= new ErrorResponseDTO("Invalid mode of payment", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BookingIDNotFound.class)
    public ResponseEntity<ErrorResponseDTO> bookingIdNotfound(){
        ErrorResponseDTO response = new ErrorResponseDTO("Invalid Booking Id",HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }
}
