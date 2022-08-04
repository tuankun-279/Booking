package tuan.aprotrain.projectpetcare.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import tuan.aprotrain.projectpetcare.Adapter.ExpandLVCheckBox;
import tuan.aprotrain.projectpetcare.Adapter.PaymentAdapter;
import tuan.aprotrain.projectpetcare.Adapter.PetNameAdapter;
import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Bookings;
import tuan.aprotrain.projectpetcare.entity.Pets;

public class BookingActivity extends AppCompatActivity {
    /*
      Phần khai báo cho adapter category
       */
    ExpandLVCheckBox listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listCategory;
    HashMap<String, List<String>> listService;
    /*
    Phần khai báo cho date and time
     */
    private TextView date_input;
    private TextView time_input;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    /*
    Phần khai báo cho adapter của spinner chọn pet name và payment
     */
    private Spinner spinnerPetName;
    private PetNameAdapter petnameAdapter;
    private Spinner spinnerPayment;
    private PaymentAdapter paymentAdapter;


    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        reference = FirebaseDatabase.getInstance().getReference();

        spinnerPetName = (Spinner) findViewById(R.id.spnPetName);
        petnameAdapter = new PetNameAdapter(this,R.layout.layout_selected_dropdown,getListName());
        spinnerPetName.setAdapter(petnameAdapter);

        spinnerPayment = findViewById(R.id.spnPayment);
        paymentAdapter = new PaymentAdapter(this,R.layout.layout_selected_dropdown,getListPay());
        spinnerPayment.setAdapter(paymentAdapter);


        expListView = (ExpandableListView) findViewById(R.id.expandLV);
        prepareListData();
        listAdapter = new ExpandLVCheckBox(this, listCategory, listService);
        expListView.setAdapter(listAdapter);

        date_input = findViewById(R.id.date_text);
        time_input = findViewById(R.id.time_text);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);


    /*
        Hàm set date
    */
        date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        BookingActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                datePickerDialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = day + "/" + month + "/" + year;
                date_input.setText(date);
            }
        };
    /*
        Hàm set time
    */
        time_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        BookingActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,timeSetListener,hour,minute,true
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hour + ":" + minute;
                time_input.setText(time);
            }
        };

    /*
        Hàm của spinner petname và payment
    */
        spinnerPetName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BookingActivity.this, petnameAdapter.getItem(position).getPetName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(BookingActivity.this, paymentAdapter.getItem(position).getPayment(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    /*
        Hàm của expandable listview checkbox
    */
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listCategory.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listCategory.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listCategory.get(groupPosition)
                                + " : "
                                + listService.get(
                                listCategory.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

    /*
       Phần code tham khảo, nôm na là ấn button thì hiện ra đã ấn vào bao nhiêu cái checkbox
    */
//
//        Button button = (Button) findViewById(R.id.button);
//        final TextView textView = (TextView) findViewById(R.id.textView);
//
//
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                int count =0;
//                for(int mGroupPosition =0; mGroupPosition < listAdapter.getGroupCount(); mGroupPosition++)
//                {
//                    count = count +  listAdapter.getNumberOfCheckedItemsInGroup(mGroupPosition);
//                }
//                textView.setText(""+count);
//            }
//        });
//

    }
    private void prepareListData() {
        listCategory = new ArrayList<String>();
        listService = new HashMap<String, List<String>>();
        //Intent pass data
        listCategory.add("Category");

        List<String> list = new ArrayList<String>();

        reference.child("Services").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("hello 1");
                for(DataSnapshot serviceSnapshot: snapshot.getChildren()){
                    list.add(serviceSnapshot.child("serviceName").getValue(String.class));
                    System.out.println("Service:"+serviceSnapshot.child("serviceName").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listService.put(listCategory.get(0), list);
    }

    private List<Pets> getListName(){
        List<Pets> listN = new ArrayList<>();
        reference.child("Pets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("hello 1");
                for(DataSnapshot petSnapshot: snapshot.getChildren()){
                    //Pets pet = petSnapshot.getValue(Pets.class);
                    listN.add(new Pets(petSnapshot.child("petName").getValue(String.class)));
                    //System.out.println("Service:"+serviceSnapshot.child("serviceName").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        System.out.println("Pets: "+listN.toString());
//        listN.add(new Pets("Pet's Name"));
//        listN.add(new Pets("Alexander III. Pudding"));
//        listN.add(new Pets("Cheems"));
//        listN.add(new Pets("Nasus"));
//        listN.add(new Pets("Yuumi"));
        return listN;
    }

    private List<Bookings> getListPay(){
        List<Bookings> listP = new ArrayList<>();
        reference.child("Bookings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("hello 1");
                for(DataSnapshot petSnapshot: snapshot.getChildren()){
                    //Pets pet = petSnapshot.getValue(Pets.class);
                    listP.add(new Bookings(petSnapshot.child("payment").getValue(String.class)));
                    //System.out.println("Service:"+serviceSnapshot.child("serviceName").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        listP.add(new Bookings("Payment"));
//        listP.add(new Bookings("Banking"));
//        listP.add(new Bookings("Cash"));
        return listP;
    }
}