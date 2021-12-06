package com.sweethome.sweethome.Booking.Controller;


import com.sweethome.sweethome.Booking.Entities.BookingInfoEntity;
import com.sweethome.sweethome.Booking.DTO.BookingInfoEntityDTO;
import com.sweethome.sweethome.Booking.Services.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotel")
public class BookingServiceController {
     BookingService bookingService;
     ModelMapper modelMapper;

     @Autowired
     public BookingServiceController(BookingService bookingService,ModelMapper modelMapper){
          this.bookingService=bookingService;
          this.modelMapper=modelMapper;
     }
     @PostMapping("/booking")
     public ResponseEntity<BookingInfoEntityDTO> book(@RequestBody BookingInfoEntityDTO bookingInfoEntityDTO){

          BookingInfoEntity bookingInfoEntity= modelMapper.map(bookingInfoEntityDTO,BookingInfoEntity.class);
          BookingInfoEntity savedBookingEntity= bookingService.book(bookingInfoEntity);
          BookingInfoEntityDTO savedBookingEntityDTO =modelMapper.map(savedBookingEntity,BookingInfoEntityDTO.class);
          return new ResponseEntity(savedBookingEntityDTO,HttpStatus.CREATED);
     }

}
