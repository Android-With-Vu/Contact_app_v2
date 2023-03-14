package com.lehuuhung.contact_app_v2.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lehuuhung.contact_app_v2.R;
import com.lehuuhung.contact_app_v2.databinding.FragmentDetailBinding;
import com.lehuuhung.contact_app_v2.model.Contact;
import com.lehuuhung.contact_app_v2.viewModel.AppDatabase;
import com.lehuuhung.contact_app_v2.viewModel.ContactDao;

public class DetailFragment extends Fragment {

    private int idContact;

    private FragmentDetailBinding binding;

    private AppDatabase appDatabase;
    private ContactDao contactDao;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idContact = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appDatabase = AppDatabase.getInstance(getContext());
        contactDao = appDatabase.contactDAO();

        Contact contact = contactDao.getContact(idContact);

        binding.tvName.setText(contact.getName());
        binding.tvEamil.setText(contact.getEmail());
        binding.tvPhone.setText(contact.getMobile());

        byte[] imageByteArray = contact.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        binding.ivAvatar.setImageBitmap(bitmap);

        binding.ivBack.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.listFragment));

        binding.ivDel.setOnClickListener(view12 -> {


            Toast.makeText(getContext(),"Deleted", Toast.LENGTH_SHORT).show();

            contactDao.delete(idContact);
            Navigation.findNavController(view12).navigate(R.id.listFragment);
        });
    }
}