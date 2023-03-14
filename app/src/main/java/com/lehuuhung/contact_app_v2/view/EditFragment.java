package com.lehuuhung.contact_app_v2.view;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lehuuhung.contact_app_v2.R;
import com.lehuuhung.contact_app_v2.databinding.FragmentEditBinding;
import com.lehuuhung.contact_app_v2.model.Contact;
import com.lehuuhung.contact_app_v2.viewModel.AppDatabase;
import com.lehuuhung.contact_app_v2.viewModel.ContactDao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditFragment extends Fragment {

    private FragmentEditBinding binding;
    private AppDatabase appDatabase;
    private ContactDao contactDao;
    private int idContact;
    private boolean isImageSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idContact = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appDatabase = AppDatabase.getInstance(getContext());
        contactDao = appDatabase.contactDAO();

        Contact contact = contactDao.getContact(idContact);

        if ( contact != null ){
            binding.editName.setText(contact.getName());
            binding.editPhone.setText(contact.getMobile());
            binding.editHome.setText(contact.getHome());
            binding.editEmail.setText(contact.getEmail());
            byte[] imageByteArray = contact.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
            binding.ivAvatar.setImageBitmap(bitmap);
            isImageSelected = true;
        }else{
            contact = new Contact();
        }

        binding.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        binding.ivBack.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.listFragment));

        Contact finalContact = contact;
        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = binding.editName.getText().toString();
                String mobile = binding.editPhone.getText().toString();
                String email = binding.editEmail.getText().toString();
                String home = binding.editHome.getText().toString();
                byte[]  image;
                if (isImageSelected) {
                    image = convertImageToByteArray(binding.ivAvatar);
                }else{
                    image = getByteArrayFromName(name);
                }

                finalContact.setName(name);
                finalContact.setMobile(mobile);
                finalContact.setEmail(email);
                finalContact.setHome(home);
                finalContact.setImage(image);

                updateOrCraete(finalContact);

                Navigation.findNavController(view).navigate(R.id.listFragment);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            try {
                Uri uri = data.getData();
                isImageSelected = true;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                binding.ivAvatar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] convertImageToByteArray(@NonNull ImageView imageView) {

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    public static byte[] getByteArrayFromName(String name) {
        char firstChar = name.charAt(0);

        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(50, 50, 50, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(firstChar), 50, 70, paint);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void updateOrCraete(Contact contacts){
        Contact contact = contactDao.getContact(contacts.getId());

        if ( contact != null ){
            contactDao.update(contacts);
            Toast.makeText(getContext(),"Updated", Toast.LENGTH_SHORT).show();
        }else{
            contactDao.insert(contacts);
            Toast.makeText(getContext(),"Inserted", Toast.LENGTH_SHORT).show();
        }
    }
}