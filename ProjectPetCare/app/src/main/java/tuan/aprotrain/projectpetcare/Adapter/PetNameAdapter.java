package tuan.aprotrain.projectpetcare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Pets;

public class PetNameAdapter extends ArrayAdapter<Pets> {
    public PetNameAdapter(@NonNull Context context, int resource, @NonNull List<Pets> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull  ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_selected_dropdown, parent,false);
        TextView selected = convertView.findViewById(R.id.selectedTxt);

        Pets pet = this.getItem(position);
        if (pet != null){
            selected.setText(pet.getPetName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_drop_down, parent,false);
        TextView dropDown = convertView.findViewById(R.id.dropDown);

        Pets pet = this.getItem(position);
        if (pet != null){
            dropDown.setText(pet.getPetName());
        }
        return convertView;
    }



}

