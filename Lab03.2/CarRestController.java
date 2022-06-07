package com.rental;

import java.util.Collection;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.RentalProperties;
import com.rental.RentalProperties.Airport;
import com.rental.dao.CarService;
import com.rental.dao.RentalCar;

//TODO RestController tied to a mapping of "auto"
public class CarRestController {
	@Autowired private CarService dao;
	 
	 
	//HTTP Get where the path is "/{pickUp}/{price}" and only produces JSON
	//@PathVariables are required
	public Collection<RentalCar> getLocationPrice(String location,double price) throws JSONException { 
		 return dao.getAll().stream()
		.filter(p-> p.getDailyRate() < price && p.getLocation().equals(location)).collect(Collectors.toList());
	}
	
	@Autowired
	private RentalProperties rentalProperties;
	@GetMapping(path="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	//HTTP Get where the path is "/{id}" and produces JSON or XML
    //A @PathVariable is required
	public RentalCar getIndividualCar(long id) {
	 	RentalCar car =  dao.getByID(id);
	 	Airport airport = rentalProperties.getAirports().stream().filter(p-> p.getIataCode().equals(car.getLocation())).findFirst().get();
	 	double tax = car.getDailyRate() * airport.getTax()/100;
	 	car.setDailyRate(car.getDailyRate() + tax);
	 	return car;
	}
	
	 

}
