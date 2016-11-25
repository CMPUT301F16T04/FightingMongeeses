package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.util.UUID;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;


/** The RiderRequestView displays a list of active requests for a logged in rider
 * @author mackenzie
 * */

public class RiderRequestView extends Activity {

    private UUID currentUUID; // UUID of the currently logged-in rider
    private String riderName; // string of the curretn UUID
    private String clickedDriverIdStr; //id of driver who is clicked in popup
    private String clickedDriverNameStr; //name of driver who is clicked in popup
    private String clickedRequestIDStr; //id string of request that is clicked in listview
    private Activity activity = this;
    public ArrayList<Request> requests = new ArrayList<>();
    //Declaring reference buttons in the GUI
    ListView oldRequestsList;

    private RequestController reqCon = new RequestController();
    private DriverController driverCon = new DriverController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_request_view);
        oldRequestsList = (ListView) (findViewById(R.id.oldRequestLists));


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            riderName = extras.getString("Name");
        }

        AsyncController controller = new AsyncController();
        JsonArray queryResults = controller.getAllFromIndexFiltered("request", "rider", riderName);
        requests.clear(); // Fix for duplicates in list
        for (JsonElement result : queryResults) {
            try {
                requests.add(new Request(result.getAsJsonObject().getAsJsonObject("_source")));
            } catch (Exception e) {
                Log.i("Error parsing requests", e.toString());
            }


            RequestAdapter customAdapter = new RequestAdapter(activity, requests);
            oldRequestsList.setAdapter(customAdapter);

            //this is to recognize listview item presses within the view
            oldRequestsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Request request = requests.get(position);
                    clickedRequestIDStr = request.getID().toString();
                    displayDrivers(request);
                }
            });
            
        }
    }


    //thinking of popup window as outlined in http://stackoverflow.com/questions/15153651/set-own-layout-in-popup-window-in-android
    //date link accessed : Nov 5 2016
    //author: Emil Adz ,edited Vladimir Kulyk
    public void displayDrivers(Request request) {

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) findViewById(R.id.drivers_who_accepted);
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.drivers_who_accepted, viewGroup);

        // Creating the PopupWindow
        final PopupWindow driverPopUp = new PopupWindow(activity);
        driverPopUp.setContentView(layout);
        driverPopUp.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        driverPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        driverPopUp.setFocusable(true);


        // Displaying the popup at the specified location, + offsets.
        driverPopUp.showAtLocation(layout, Gravity.CENTER, 0, 0);


        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.exit_popup);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                driverPopUp.dismiss();
            }
        });


        final ArrayList<String> possibleDrivers = capitalizeAllNames(reqCon.getPossibleDrivers(clickedRequestIDStr));
        System.out.println(possibleDrivers);


        ListView popupList = (ListView) layout.findViewById(R.id.drivers_list);
        ArrayAdapter<String> adapter_popup = new ArrayAdapter<String>(activity, R.layout.driver_who_accepted, possibleDrivers);
        popupList.setAdapter(adapter_popup);

        //this is to recognize listview item presses within the popup
        popupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                clickedDriverNameStr = possibleDrivers.get(position);
                //Driver joe = driverCon.getDriverFromServerUsingName(clickedDriverNameStr);
                clickedDriverIdStr = driverCon.getDriverFromServerUsingName(clickedDriverNameStr).getElasticID();
//                Log.i("the id for driver", clickedDriverIdStr);
//                Log.i("the name of the driver", clickedDriverNameStr);
//                Log.i("the name of the driver", joe.getElasticID());

                //must close popup before going to next activity
                driverPopUp.dismiss();


                //TODO pass the driver at clicked position to the next activity
                Intent intent = new Intent(activity, AcceptDriverView.class);
                ArrayList<String> ids = new ArrayList<>();
                ids.add(riderName); //pass the current user
                ids.add(clickedDriverIdStr);
                ids.add(clickedRequestIDStr);
                //System.out.println(ids);
                intent.putStringArrayListExtra("ids", ids);
                startActivity(intent);
                //go to driver profile
            }
        });


    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();


    }
    /** capitalizes all of the names in the possible drivers list so that when we
     *  display them in the popup they are guaranteed to be capitalized
     *  this may become obsolete if we enforce qualities on the usernames
     *
     *
     * @param names the possibly lowercased names in the list of possibel drivers
     * @return a list of the names, all with the first letter capitalized
     */
    private ArrayList<String> capitalizeAllNames(ArrayList<String> names) {
        ArrayList<String> captNames = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            captNames.add(names.get(i).substring(0, 1).toUpperCase().concat(names.get(i).substring(1)));
        }
        return(captNames);
    }
}

