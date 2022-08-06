package tuan.aprotrain.projectpetcare.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import tuan.aprotrain.projectpetcare.Adapter.ExpandLVCheckBox;
import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Booking;
import tuan.aprotrain.projectpetcare.entity.Pet;

public class BookingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /*
      Phần khai báo cho adapter category
       */
    ExpandLVCheckBox listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listCategory;
    HashMap<String, List<String>> listService;

    EditText notePet;

    ArrayList<String> selectedService;
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
    private Spinner spinnerPayment;
    private Spinner spinnerAddress;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        reference = FirebaseDatabase.getInstance().getReference();
        selectedService = new ArrayList<>();
        notePet = findViewById(R.id.notePet);

        spinnerPetName = findViewById(R.id.spnPetName);
        ArrayAdapter<String> petNameAdapter = new ArrayAdapter<>(BookingActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,getListPetName());
        petNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPetName.setAdapter(petNameAdapter);
        spinnerPetName.setOnItemSelectedListener(this);

        spinnerPayment = findViewById(R.id.spnPayment);
        ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(this,R.array.payment, android.R.layout.simple_spinner_item);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayment.setOnItemSelectedListener(this);
        spinnerPayment.setAdapter(paymentAdapter);

        spinnerAddress = findViewById(R.id.spnAddress);
        ArrayAdapter<CharSequence> addressAdapter = ArrayAdapter.createFromResource(this,R.array.adress, android.R.layout.simple_spinner_item);
        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddress.setOnItemSelectedListener(this);
        spinnerAddress.setAdapter(addressAdapter);

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
//                Toast.makeText(getApplicationContext(),
//                        listCategory.get(groupPosition) + " Expanded",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listCategory.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();

            }
        });

        //expListView.get
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                int i = 0;
                selectedService.add(listService.get(
                        listCategory.get(groupPosition)).get(
                        childPosition));
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
        Button button = (Button) findViewById(R.id.btnSubmit);
        final TextView textView = (TextView) findViewById(R.id.price);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String petName = spinnerPetName.getSelectedItem().toString().trim();
                String date = date_input.getText().toString().trim();
                String time = time_input.getText().toString().trim();
                String payment = spinnerPayment.getSelectedItem().toString().trim();
                String note = notePet.getText().toString();
                getSelectedItem(petName, date, time, payment, note, selectedService);
//                for (String item : listAdapter.get)
                int count =0;
                for(int mGroupPosition =0; mGroupPosition < listAdapter.getGroupCount(); mGroupPosition++)
                {
                    count = count +  listAdapter.getNumberOfCheckedItemsInGroup(mGroupPosition);
                    //list
                }
                textView.setText(""+count);

            }
        });

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
                    list.add(serviceSnapshot.child("serviceName").getValue(String.class)+"\t abc \t def");
                    System.out.println("Service:"+serviceSnapshot.child("serviceName").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listService.put(listCategory.get(0), list);
    }

    private List<String> getListPetName() {
        List<String> petNameList = new ArrayList<>();
        petNameList.add("Choose Pet");
        reference.child("Pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("hello 1");
                for (DataSnapshot petSnapshot : snapshot.getChildren()) {
                    //Pet pet = petSnapshot.getValue(Pet.class);
                    petNameList.add(petSnapshot.child("petName").getValue(String.class));
                    //System.out.println("Service:"+serviceSnapshot.child("serviceName").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return petNameList;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void getSelectedItem(String petName, String date, String time, String payment, String note, ArrayList<String> selectedService){

        if (petName.isEmpty() || date.isEmpty() || time.isEmpty() || payment.isEmpty()){
            System.out.println("All field are required!");
        }else {
            ArrayList<Pet> petList = new ArrayList<>();
            reference.child("Pets").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot petSnapshot: snapshot.getChildren()){
                        Pet pet = petSnapshot.getValue(Pet.class);
                        petList.add(pet);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            long petId = 0;

            for (Pet pet : petList) {
                if(pet.getPetName().equals(petName))
                    petId = pet.getPetId();
            }
            System.out.println("list Service: "+ petId);

            Booking booking = new Booking(petId, date, time, payment, note);
            Intent intent = new Intent(getBaseContext(), BookingDetailActivity.class);
            intent.putExtra("BOOKING", booking);
            intent.putExtra("PET_NAME", petName);
            startActivity(intent);
            //reference.child()
        }
    }
}