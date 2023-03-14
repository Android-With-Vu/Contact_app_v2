package com.lehuuhung.contact_app_v2.viewModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.lehuuhung.contact_app_v2.R;
import com.lehuuhung.contact_app_v2.databinding.ContactRowItemBinding;
import com.lehuuhung.contact_app_v2.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> implements Filterable {

    private static ArrayList<Contact> contactList;
    private static ArrayList<Contact> contactListCopy;
    public ContactsAdapter( ArrayList<Contact> contactList) {
        this.contactList = contactList;
        this.contactListCopy = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactRowItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                                                R.layout.contact_row_item,
                                                parent,
                                                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Contact contact = contactList.get(position);

        holder.binding.tvName.setText("Name: " + contact.getName());
        holder.binding.tvInfoPhone.setText("Phone: " + contact.getMobile());

        byte[] imageByteArray = contact.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        holder.binding.ivAvatar.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String input = constraint.toString().toLowerCase();

                List<Contact> filteredContact = new ArrayList<Contact>();
                if (input.isEmpty()){
                    filteredContact.addAll(contactListCopy);
                }
                else{
                    for (Contact contact: contactListCopy){
                        if(contact.getName().toLowerCase(Locale.ROOT).contains(input)){
                            filteredContact.add(contact);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults() ;
                filterResults.values = filteredContact;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactList = new ArrayList<>();
                contactList.addAll((List)results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ContactRowItemBinding binding;

        public ViewHolder(ContactRowItemBinding itemBinding) {
            super(itemBinding.getRoot());

            this.binding = itemBinding;

            binding.tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Contact contact = contactList.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",contact.getId());
                    Navigation.findNavController(view).navigate(R.id.detailFragment,bundle);
                }
            });

            itemView.setOnTouchListener(new OnSwipeTouchListener(){
                @Override
                public void onSwipeLeft() {
                    if ( binding.ivCall.getVisibility() == View.GONE){
                        binding.ivCall.setVisibility(View.VISIBLE);
                        binding.ivEdit.setVisibility(View.GONE);
                    }else{
                        binding.ivEdit.setVisibility(View.VISIBLE);
                        binding.ivCall.setVisibility(View.GONE);

                        binding.ivEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Contact contact = contactList.get(getAdapterPosition());
                                Bundle bundle = new Bundle();
                                bundle.putInt("id",contact.getId());
                                Navigation.findNavController(v).navigate(R.id.editFragment,bundle);
                            }
                        });
                    }
                }
            });
        }
    }
    public class OnSwipeTouchListener implements View.OnTouchListener{

        private final GestureDetector gestureDetector = new GestureDetector( new GestureListener());

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }


        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 50;
            private static final int SWIPE_VELOCITY_THRESHOLD = 50;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onClick();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                OnSwipeTouchListener.this.onLongPress();
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                        result = true;
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                    result = true;

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }

        public void onClick() {

        }
        public void onLongPress() {
        }
    }
}
