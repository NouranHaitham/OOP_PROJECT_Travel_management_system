package cis.travel.eg.Main;
import cis.travel.eg.Service.helpingMethods.helpingMethods;
import cis.travel.eg.Service.CarRental.*;
import cis.travel.eg.Service.FlightSystem.*;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import cis.travel.eg.Service.Hotels.Reservation.*;
import cis.travel.eg.Trip.General_Tour;
import cis.travel.eg.Trip.Trip;

import static cis.travel.eg.Main.Main.in;

public class Ticket implements Serializable {
    int Id ;
    int NumberOfTicket=0;
    Voucher voucher;
    String type;
    public double price;
    String confirmationNumber;
    Car car;
    boolean rentCar;
    public boolean hotelReservation;
    public hotelReservation Hotel;
    public boolean RoundFlight;

    public boolean OneWayFlightGoing;
    public boolean OneWayFlightReturn;

    public ArrayList <Flight> Bookedflights=new ArrayList<>(2);
    public int numberOfSeats;
    public String CustomerLocation;

    public String CustomerDestination;
    public static int numberOfTotalTickets;
    public Trip trip1=new General_Tour();
    public General_Tour trip=(General_Tour) trip1;

    public String customerContactInfo;

    public Ticket() {
        confirmationNumber= "TCK"+ numberOfTotalTickets;
        NumberOfTicket=numberOfTotalTickets;
        numberOfTotalTickets++;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public boolean isRentCar() {
        return rentCar;
    }

    public void setRentCar(boolean rentCar) {
        this.rentCar = rentCar;
    }

    public void ticketAim(Ticket ticket) {
        System.out.println("Now you will be directed to book the trip you want./n");
        //book a trip function is called here
        System.out.println("What do you want to do now?");
        System.out.println("1. Book a flight.");
        System.out.println("2. Rent a car.");
        System.out.println("3. Search for hotel.");
        int assignToTicket = helpingMethods.choice(1, 3);
        switch (assignToTicket) {
            case 1:
                bookAFlight();
                break;
            case 2:
                this.WantToRentCar(trip.getStartDate(),trip.getEndDate(),CustomerDestination);//rent car function
                break;
            case 3:

                break;
        }


    }
    // carrentall
    public void WantToRentCar(String StartDateForTrip,String EndDateForTrip, String destinationForTrip) {

        Date pickupDate=new Date();
        Date retuenDate=new Date();

        System.out.println("-----  Pickup Date -----");
        pickupDate.TakeDateFromUser();

        System.out.println("-----  Return Date -----");
        retuenDate.TakeDateFromUser();
        boolean CheckIfRentingInDurationForTrip= helpingMethods.isDateInTheTripeDuration(pickupDate.getDate(),retuenDate.getDate(),StartDateForTrip,EndDateForTrip);
        if (!CheckIfRentingInDurationForTrip){
            System.out.println("Warning: The duration you entered does not match the trip date.\n Do you want to edit dates you entered ( y / n )");
            char ans=in.next().charAt(0);
            ans= helpingMethods.InputYesOrNo(ans);
            switch (ans)
            {
                case 'y':
                case 'Y':
                    this.WantToRentCar(StartDateForTrip, EndDateForTrip,destinationForTrip);
                    break;
                case 'n':
                case 'N':
                    break;
            }

        }

        ArrayList <Car> Availablecarsforrenting=new ArrayList<>();
        int NumberOfAvailableCarsForRenting = 0;

        for (Car value : Car.cars) {
            if (value.IsCarAvailableForRenting(pickupDate, retuenDate) && destinationForTrip.equals(value.getLocation())) {
                System.out.print(NumberOfAvailableCarsForRenting + 1 + " ");
                Availablecarsforrenting.add(NumberOfAvailableCarsForRenting, value);
                NumberOfAvailableCarsForRenting++;
            }
            value.DisplayAvailableCarsForRenting(pickupDate, retuenDate, destinationForTrip);
        }
        if (NumberOfAvailableCarsForRenting == 0) {
            System.out.println("sorry there are not available cars in this duration");
        }
        else {
            System.out.println("pleas enter the number of car you want to rent ");
            int choice;
            choice = in.nextInt();
            choice=helpingMethods.InputValidOrNot(1, NumberOfAvailableCarsForRenting, choice);

            this.car = new Car(Availablecarsforrenting.get(choice-1));
            rentCar = true;
            AddCarInTicket(pickupDate,retuenDate);
            int index=Car.cars.indexOf(this.car);
            Car.cars.get(index).AddRenting(pickupDate,retuenDate);


        }


    }
    public void AddCarInTicket(Date pickupDate,Date retuenDate) {
        this.car.AddRenting(pickupDate,retuenDate);
        System.out.println("car rented successfully");
        this.price+=this.car.rentingCars.get(0).getTotalCostForRenting();
    }
    public void cancelRentedCar() {
        rentCar=false;
        car.rentingCars.remove(0);
    }
    public void DisplayRentedCar() {
        if (rentCar) {
            System.out.println(this.car);
            System.out.println(this.car.rentingCars.get(0));
        }
        else {
            System.out.println("you haven't rented a car yet");
        }
    }


    //Flightsss
    public void bookAFlight() {
        System.out.println("Choose the type of Flight:");
        System.out.println("1. Round Trip");
        System.out.println("2. one-way Trip");
        int choice = helpingMethods.choice(1, 2);

        if (choice == 1) {
            // bookARoundFlight(trip.getDestination(),CustomerLocation,trip.getStartDate())
            bookARoundFlight(CustomerDestination,CustomerLocation,trip.getStartDate(),trip.getEndDate());

        } else {
            System.out.println("Choose the destination");
            System.out.println("1. going");
            System.out.println("2. coming back");
            int ans = helpingMethods.choice(1, 2);

            if (ans == 1) {
                OneWayFlightGoing=bookAOneWayFlight(CustomerDestination,CustomerLocation,"2023-12-01");


//                bookAOneWayFlight(trip.getDestination(),CustomerLocation,trip.getStartDate());
            } else {
                // bookAOneWayFlight(CustomerLocation,trip.getDestination(),trip.getEndDate());
                OneWayFlightReturn= bookAOneWayFlight(CustomerLocation,CustomerDestination,"2023-12-01");

            }
        }


    }
    public void bookARoundFlight(String to, String from, String tripDateString,String tripEndDateString) {
        this.RoundFlight=true;
        System.out.println("Available flights for the outbound journey:");
        bookAOneWayFlight(to, from, tripDateString);

        System.out.println("Available flights for the return journey:");
        bookAOneWayFlight(from, to, tripEndDateString);
    }
    public boolean bookAOneWayFlight ( String To , String From, String tripDateString ) {
        // to display available flights  trip location (From )match with flight arrival && customer location = ( to ) match with departure && date the same
        // trip day == flight day    &&   number of seats available
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate tripDate = LocalDate.parse(tripDateString, formatter);
        DayOfWeek TripdayOfWeek = tripDate.getDayOfWeek();


        ArrayList<Flight> availableFlights=new ArrayList<>();
        //Airport.Airports
        for (int i = 0; i< Airport.Airports.size(); i++)
        {
            for (int j = 0; j< Airport.Airports.get(i).flights.size(); j++)
            {
                if(To.equals(Airport.Airports.get(i).flights.get(j).getArrival())
                        && From.equals(Airport.Airports.get(i).flights.get(j).getDeparture())
                        && TripdayOfWeek.equals(Airport.Airports.get(i).flights.get(j).getFlightDayOfWeek())
                        && (Airport.Airports.get(i).flights.get(j).getAvailableSeats() >= this.numberOfSeats)){
                    availableFlights.add(Airport.Airports.get(i).flights.get(j));
                }

            }
        }
        if (availableFlights.isEmpty())
        {
            System.out.println("sorry there is not available flights ");
            return false;
        }
        else
        {
            int numbers=availableFlights.size();
            for (int i = 0; i < numbers; i++) {
                availableFlights.get(i).setFlightPrice(this.numberOfSeats*availableFlights.get(i).getFlightPrice());
                System.out.print((i+1));
                System.out.println(availableFlights);
            }
            System.out.println("please enter the flight Number you want to book");
            int ans=helpingMethods.choice(1,numbers);
            ans --;
            DisplayAvailableSeatsInFlight(availableFlights.get(ans),Airport.Airports);
            if (To.equals(CustomerDestination)&&From.equals(CustomerLocation))
            {
                OneWayFlightGoing=true;
            }
            else {
                OneWayFlightReturn=true;
            }
            AddFlightsToTicket(availableFlights.get(ans), Airport.Airports);
        }
        return true;

    }
    public void AddFlightsToTicket(Flight f,ArrayList<Airport> airports) {
        this.Bookedflights.add(f);
        if (this.RoundFlight)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(trip.getStartDate(), formatter);
            this.Bookedflights.get(0).getFlightDate().set(0,start);
            LocalDate end = LocalDate.parse(trip.getStartDate(), formatter);
            this.Bookedflights.get(0).getFlightDate().set(1,end);
        }
        else if(OneWayFlightGoing)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start = LocalDate.parse(trip.getStartDate(), formatter);
            this.Bookedflights.get(0).getFlightDate().set(0,start);
        }
        else if(OneWayFlightReturn)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate end = LocalDate.parse(this.trip.getEndDate(), formatter);
            this.Bookedflights.get(0).getFlightDate().set(0,end);
        }
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate end = LocalDate.parse(trip.getStartDate(), formatter);
            this.Bookedflights.get(0).getFlightDate().set(0,end);
        }


        this.price+=f.getFlightPrice();
    }
    public void DisplayAvailableSeatsInFlight(Flight f,ArrayList<Airport> airports) {
        int IndexInArrayOfFlights=-1,IndexInArrayOfAirport=-1;
        for (int i = 0; i < airports.size(); i++) {
            IndexInArrayOfFlights = airports.get(i).flights.indexOf(f);
            if (IndexInArrayOfFlights>-1)
            {
                IndexInArrayOfAirport=i;
                break;
            }
        }

        boolean falseInput=false;
        for (int k = 0; k < this.numberOfSeats; k++) {

            do {
                System.out.println("Available seats  number ");
                for (int i = 0; i < airports.get(IndexInArrayOfAirport).flights.get(IndexInArrayOfFlights).bookedSeats.size(); i++) {
                    if (airports.get(IndexInArrayOfAirport).flights.get(IndexInArrayOfFlights).bookedSeats.get(i)) {
                        System.out.print((i + 1) + "   ");
                    }
                }
                System.out.print("\n Please Choose Seat Number ");
                int choice = helpingMethods.choice(1, airports.get(IndexInArrayOfAirport).flights.get(IndexInArrayOfFlights).bookedSeats.size());
                choice--;
                if (airports.get(IndexInArrayOfAirport).flights.get(IndexInArrayOfFlights).bookedSeats.get(choice)) {
                    f.bookedSeats.add(true);
                    airports.get(IndexInArrayOfAirport).flights.get(IndexInArrayOfFlights).bookedSeats.set(choice, false);
                    falseInput = false;
                    airports.get(IndexInArrayOfAirport).flights.get(IndexInArrayOfFlights).setNumberOfBookedSeat(airports.get(IndexInArrayOfAirport).flights.get(IndexInArrayOfFlights).getNumberOfBookedSeat()+1);
                } else {
                    System.out.println("\n sorry this seat is booked please enter another Number");
                    falseInput = true;
                }
            } while (falseInput);
        }
    }
    public void CancelBookingForFlight(){
        if (this.RoundFlight)
        {
            this.Bookedflights.remove(0);
            this.Bookedflights.remove(1);
            RoundFlight=false;

        }
        else
        {
            this.Bookedflights.remove(0);
            OneWayFlightGoing=false;


        }
    }
    public void EditBookingForFlight(){

        if (this.RoundFlight)
        {
            System.out.println("Your Flights Details ");
            System.out.println(this.Bookedflights);
            System.out.println("press 1 to cancel first flight \npress 2 to cancel second flight \npress 3 to cancel both of them\npress 4 to return");
            int choice=helpingMethods.choice(1,4);
            switch (choice)
            {
                case 1:
                    this.Bookedflights.remove(0);
                    RoundFlight=false;
                    OneWayFlightGoing=true;
                    break;
                case 2:
                    this.Bookedflights.remove(1);
                    RoundFlight=false;
                    OneWayFlightReturn=true;
                    break;
                case 3:
                    this.Bookedflights.remove(0);
                    this.Bookedflights.remove(1);
                    RoundFlight=false;
                    OneWayFlightGoing=false;
                    OneWayFlightReturn=false;
                    break;
            }

        }
        else if(OneWayFlightReturn||OneWayFlightGoing) {
            System.out.println("Your Flight Details ");
            System.out.println(this.Bookedflights);
            System.out.println("press 1 to cancel first flight \npress 2 to return back");
            int choice=helpingMethods.choice(1,2);
            if (choice==1)
            {
                this.Bookedflights.remove(0);
            }
        }
        else {
            System.out.println("sorry you havent ");
        }
    }
    public void ticketDetails(boolean oneticket){
        System.out.println(" Ticket ID: "+Id );
        System.out.println(" Trip Assigned: "+ trip.getTripName());
        System.out.println(" From: "+ CustomerLocation + " to "+ CustomerDestination);
        System.out.println(" Ticket type: "+ type);
        System.out.println(" Number of seats booked: "+ numberOfSeats);
        System.out.println(" - - - - - - - - - - - - - - - - - - - -");
        if(oneticket){
            System.out.println(" >> Services:");
            if(rentCar) System.out.println(" Car: "+ car.getModel());
            else System.out.println(" Car : none");

            if(hotelReservation) System.out.println(" Hotel: "+ Hotel.getHotelName());
            else System.out.println(" Hotel : none");

            if(RoundFlight) System.out.println(" flight: Round flight");
            else if (OneWayFlightGoing) System.out.println("Flight: One way flight");
            else System.out.println(" Flight : none");
        }
        System.out.println(" - - - - - - - - - - - - - - - - - ");
        System.out.println(" - Total payments: "+ price);

    }

    @Override
    public String toString() {
        return "Ticket{" +
                "in=" + in +
                ", Id=" + Id +
                ", NumberOfTicket=" + NumberOfTicket +
                ", voucher=" + voucher +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", confirmationNumber='" + confirmationNumber + '\'' +
                ", car=" + car +
                ", rentCar=" + rentCar +
                ", hotelReservation=" + hotelReservation +
                ", Hotel=" + Hotel +
                ", RoundFlight=" + RoundFlight +
                ", OneWayFlight=" + OneWayFlight +
                ", Bookedflights=" + Bookedflights +
                ", numberOfSeats=" + numberOfSeats +
                ", ticketType='" + ticketType + '\'' +
                ", CustomerLocation='" + CustomerLocation + '\'' +
                ", CustomerDestination='" + CustomerDestination + '\'' +
                ", trip=" + trip +
                ", customerContactInfo='" + customerContactInfo + '\'' +
                '}';
    }


   // public static void main(String[] args) {

//        ArrayList<Flight> f1 = new ArrayList<>();
//        ArrayList<Flight> f2 = new ArrayList<>();
//
//        f1.add(new Flight(101, "A", "B",DayOfWeek.MONDAY, LocalTime.of(9, 30), 150, 200.0, "Economy"));
//        f1.add(new Flight(202, "C", "D",DayOfWeek.MONDAY, LocalTime.of(12, 45), 120, 250.0, "Business"));
//        f1.add( new Flight(303, "A", "B",DayOfWeek.MONDAY, LocalTime.of(15, 0), 100, 300.0, "FirstClass"));
//        f2.add( new Flight(404, "C", "B", DayOfWeek.MONDAY, LocalTime.of(18, 15), 180, 180.0, "Economy"));
//        f2.add( new Flight(505, "A", "B", DayOfWeek.MONDAY,   LocalTime.of(21, 30), 200, 220.0, "Business"));
//
//        Airport a=new Airport("cairo","Egypt","0111212",f1);
//        Airport.Airports.add(a);
//        Airport b=new Airport("cairo","Egypt","0111212",f2);
//        Airport.Airports.add(b);
//        Ticket t =new Ticket();
//        t.numberOfSeats=2;
//        t.trip.setEndDate("2023*12-11");
//
//        t.trip.setEndDate("2023-12-11");
//        t.bookAOneWayFlight ( "B" , "A", t.trip.getEndDate() );
//        t.EditBookingForFlight();
//        System.out.println(t.Bookedflights.size());

  //  }

    // getters and setters


}

////// CUSTOMER CODE /////////////////////////////////////////////////////////////////////////
/*import cis.travel.eg.Service.Activity;
import cis.travel.eg.Trip.Trip;

import java.util.ArrayList;

public class Ticket {
    public String getCustomerLocation() {
        return CustomerLocation;
    }


    public void setCustomerLocation(String customerLocation) {
        CustomerLocation = customerLocation;
    }

    private String CustomerLocation;
    public static int numberOfTotalTickets;

    public Trip trip;

    public ArrayList<Activity> activity;
    private String ticketType;
    public double price = 0; // check voucher after confirmation of ticket
    public boolean hotelReservation;
    //public hotelReservation Hotel;
    public boolean Flight;
    //ArrayList<Flight> flights = new ArrayList<>();
    private int numberOfSeats;

    int NumberOfTicket = 0;
    Voucher voucher;
    public String type;

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String TicketType) {
        this.ticketType = TicketType;
    }
    public String confirmationNumber;
    public String customerContactInfo;

    public Ticket() {
        confirmationNumber = "TCK" + numberOfTotalTickets;
        NumberOfTicket = numberOfTotalTickets;
        numberOfTotalTickets++;
    }

}
*/

