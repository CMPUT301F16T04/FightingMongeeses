package ca.ualberta.ridr;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * this view displays the info of the user when a username was presented and clicked on
 * one note is that the user in prev activity could have been either a rider or a driver
 * and each has specific info , so we will need to have their type passed to us
 */

public class ProfileView extends Activity {

    private Button userEmail;
    private Button userPhone;
    private TextView xName;
    private TextView vehicleInfo;
    private TextView vehicleTitle;
    private Driver driver;
    private Rider rider;

    private DriverController driverCon = new DriverController();
    private RiderController riderCon = new RiderController();

    //just hardcoded for now
    private String username = "Justin";
    private String type = "Driver";
    private String userEmailStr = "anemail@email.email";
    private  String userPhoneStr = "123123123213";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

//        Intent intent = getIntent();
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            username = extras.getString("username");
//            type = extras.getString("type");
//        }

        userEmail = (Button) findViewById(R.id.user_email);
        userPhone = (Button) findViewById(R.id.user_phone);
        xName = (TextView) findViewById(R.id.x_name);
        vehicleTitle = (TextView) findViewById(R.id.vehicle_title);

//        if(type.equals("Rider")){
//            rider = getRider(username);
//            userEmailStr = rider.getEmail();
//            userPhoneStr = rider.getPhoneNumber();
              vehicleTitle.setVisibility(View.GONE);
//        }
//        //else user was a driver
//        else{
//            driver = getDriver(username);
//            userEmailStr = driver.getEmail();
//            userPhoneStr = driver.getPhoneNumber();
//            vehicleInfo = (TextView) findViewById(R.id.vehicle_info);
//            //vehicleInfo.setText(driver.getVehicleDescription());
//            vehicleInfo.setText("Autodomahickey");
//        }



        //for testing only
       // vehicleInfo = (TextView) findViewById(R.id.vehicle_info);
        //vehicleInfo.setText("Autodomahickey");


        userEmail.setText(userEmailStr);
        userPhone.setText(userPhoneStr);
        xName.setText(capitalizeName(username));

        //if the user clicks the drivers displayed phone number we take them to a phone app and predial for them
        userPhone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // found at link: http://stackoverflow.com/questions/11699819/how-do-i-get-the-dialer-to-open-with-phone-number-displayed
                // accessed on Nov. 10 2016
                // answered by AAnkit
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+userPhoneStr));
                startActivity(callIntent);
            }
        });

        //if the user clicks the drivers displayed email we will want to take them to an email app
        userEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //http://stackoverflow.com/questions/2197741/how-can-i-send-emails-from-my-android-application?rq=1
                // Nov 24 2016
                // author Jeremy Logan
                //note in order for this to work you must set up an email on your device
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{userEmailStr});
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ProfileView.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * gets the user for the data we display on this view
     *
     * @param username used to fetch the driver
     * @return Driver object
     */
    public Driver getDriver(String username){
        return(driverCon.getDriverFromServerUsingName(username));
    }

    /**
     * gets the user for the data we display on this view
     *
     * @param username used to fetch the rider
     * @return Driver object
     */
    public Rider getRider(String username){
        return(riderCon.getDriverFromServerUsingName(username));
    }

    /** just some formatting, might not be necessary if the names are enforced
     *  to be capitalized but wont hurt to have this til then
     *
     * @param name the possibly lowercased name
     * @return the name with the first letter capitalized
     */
    private String capitalizeName(String name){
        return (name.substring(0,1).toUpperCase().concat(name.substring(1)));
    }

}
