package ca.ualberta.ridr;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by kristynewbury on 2016-11-07.
 */

public class RequestControllerTest {
    @Test
    public void searchTest() throws Exception {
        Gson gson = new Gson();
        Context context = null;
        RequestController requestController = new RequestController(context);
        Request request = new Request("Justin Barclay", "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
        String jsonString = gson.toJson(request);
        JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);
        Assert.assertTrue(requestController.doesJsonContainKeyword("Justin",jsonElement));
        Assert.assertTrue(requestController.doesJsonContainKeyword("Alberta",jsonElement));
        Assert.assertTrue(requestController.doesJsonContainKeyword("Edmonton",jsonElement));
    }

/*


  //  @Test
//    public void testRequestControllerGetPossibleDrivers(){
//        Request request = new Request("Edmonton", "Timbuktu");
//        ArrayList<Driver> drivers = new ArrayList<Driver>();
//        Vehicle vehicle = new Vehicle(1990, "pontiac", "grandam");
//        Driver john = new Driver("john", new Date(), "credit", "email", "phone", vehicle, "bankaccountno");
//        drivers.add(john);
//        request.setPossibleDrivers(drivers);
//
//        RequestController RC = new RequestController();
//
//        ArrayList<Driver> testdrivers = RC.getPossibleDriversWithRequestID(request);
//
//        assertTrue(testdrivers.get(0).equals(john));
//    }
//    @Test
//    public void testRequestControllerRemoveRequest(){
//        RequestController RC = new RequestController();
//        Request request = new Request("Edmonton", "Timbuktu");
//        Rider rider = new Rider("Guy", new Date(), "credit", "email", "phone");
//
//        rider.addRequest(request);
//
//        //check that it was successfully added
//        assertEquals(rider.getRequests().size(), 1);
//
//        RC.removeRequest(request, rider);
//
//        //then the real check is to make sure the controller can remove it
//        assertEquals(rider.getRequests().size(), 0);
//
//    }

>>>>>>> 2385fd7675cceed014c43f673d174e9e36f67d58
    @Test
    public void testRequestControllerAccept(){
        LatLng coords = new LatLng(1,2);
        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Request request = new Request(rider.getID().toString(), "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
        RequestController RC = new RequestController();

        RC.accept(request);

        assertTrue(request.isAccepted());

    }
/*
    @Test
    public void testRequestControllerAddDriver() {
        LatLng coords = new LatLng(1,2);

        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Request request = new Request(rider.getID().toString(), "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
        Driver driver = new Driver("joe", new Date(), "credit", "email", "phone", "bankno");

        RequestController RC = new RequestController();

        RC.addDriver(request, driver);

        assertEquals(request.getPossibleDriversWithRequestID().size(), 1);
        assertTrue(request.getPossibleDriversWithRequestID().get(0).equals(driver));
    }
    */

    /*@Test
    public void testRequestControllerGetPossibleDrivers(){
        LatLng coords = new LatLng(1,2);

        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Request request = new Request(rider.getID().toString(), "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );
        ArrayList<Driver> drivers = new ArrayList<Driver>();
        Driver john = new Driver("john", new Date(), "credit", "email", "phone", "bankaccountno");
        drivers.add(john);
        request.setPossibleDrivers(drivers);

        RequestController RC = new RequestController();

        ArrayList<Driver> testdrivers = RC.getPossibleDriversWithRequestID(request);

        assertTrue(testdrivers.get(0).equals(john));
    }
    @Test
    public void testRequestControllerRemoveRequest(){
        RequestController RC = new RequestController();
        LatLng coords = new LatLng(1,2);

        Rider rider = new Rider("Steve", new Date(), "321", "goodemail", "9999999");
        Request request = new Request(rider.getID().toString(), "University of Alberta", "10615 47 Avenue Northwest, Edmonton", new LatLng(53.525288, -113.525454), new LatLng(53.484775, -113.505067), new Date() );

        rider.addRequest(request);

        //check that it was successfully added
        assertEquals(rider.getRequests().size(), 1);

        RC.removeRequest(request, rider);

        //then the real check is to make sure the controller can remove it
        assertEquals(rider.getRequests().size(), 0);

    }*/

}
