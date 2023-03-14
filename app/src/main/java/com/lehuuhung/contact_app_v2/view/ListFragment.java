package com.lehuuhung.contact_app_v2.view;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lehuuhung.contact_app_v2.R;
import com.lehuuhung.contact_app_v2.databinding.FragmentListBinding;
import com.lehuuhung.contact_app_v2.model.Contact;
import com.lehuuhung.contact_app_v2.viewModel.AppDatabase;
import com.lehuuhung.contact_app_v2.viewModel.ContactDao;
import com.lehuuhung.contact_app_v2.viewModel.ContactsAdapter;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;

    private ArrayList<Contact> contactList;
    private ContactsAdapter contactsAdapter;

    private AppDatabase appDatabase;
    private ContactDao contactDao;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().runOnUiThread(() -> {
            contactList = new ArrayList<>();
            binding.rvContact.setLayoutManager(new LinearLayoutManager(getActivity()));

            appDatabase = AppDatabase.getInstance(getActivity());
            contactDao = appDatabase.contactDAO();

            contactList = (ArrayList<Contact>) contactDao.getAll();
            contactsAdapter = new ContactsAdapter(contactList);
            binding.rvContact.setAdapter(contactsAdapter);
        });


        binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("id",-1);
                Navigation.findNavController(view).navigate(R.id.editFragment,bundle);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.mi_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                contactsAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
}