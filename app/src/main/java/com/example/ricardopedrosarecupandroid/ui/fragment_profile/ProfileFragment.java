package com.example.ricardopedrosarecupandroid.ui.fragment_profile;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ricardopedrosarecupandroid.R;
import com.example.ricardopedrosarecupandroid.data.local.AppDatabase;
import com.example.ricardopedrosarecupandroid.data.local.entity.MessageChat;
import com.example.ricardopedrosarecupandroid.databinding.FragmentProfileBinding;
import com.example.ricardopedrosarecupandroid.ui.main.MainActivityViewModel;
import com.example.ricardopedrosarecupandroid.ui.main.MainActivityViewModelFactory;
import com.example.ricardopedrosarecupandroid.utils.MyTimeUtils;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding b;
    private static final String BOT_FILE = "quilloquebot.txt";
    private NavController navController;
    private MainActivityViewModel viewModel;
    private ProfileFragmentViewAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(this, new MainActivityViewModelFactory(
                requireActivity().getApplication(), AppDatabase.getInstance(requireContext())
        )).get(MainActivityViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        if (savedInstanceState == null) {
            loadBotInfo();
            if (TextUtils.isEmpty(b.txtName.getText().toString())) {
                b.txtName.setText(requireContext().getString(R.string.bot_default_name));
                setToolbarTitle(requireContext().getString(R.string.bot_default_name));
                setBotImage();
            } else {
                setToolbarTitle(b.txtName.getText().toString());
                setBotImage();
            }
        }
        setupRecyclerView();
        editTextListening();
        observeData();
        addBotMessage();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnusave) {
            fileWriteBotInfo();
            navController.popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        listAdapter = new ProfileFragmentViewAdapter();
        b.listAnswers.setHasFixedSize(true);
        b.listAnswers.setLayoutManager(new LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
        ));
        b.listAnswers.setItemAnimator(new DefaultItemAnimator());
        b.listAnswers.setAdapter(listAdapter);
    }

    private void editTextListening() {
        b.txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setToolbarTitle(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        b.txtBotUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                setBotImage();
        });
    }

    private void setBotImage() {
        if (!TextUtils.isEmpty(b.txtBotUrl.getText().toString())) {
            Picasso.with(requireContext())
                    .load(b.txtBotUrl.getText().toString())
                    .error(R.drawable.ic_save_black_24dp)
                    .into(b.imgBot);
        }
    }

    private void fileWriteBotInfo() {
        String bot_name = b.txtName.getText().toString();
        String bot_url = b.txtBotUrl.getText().toString();
        String info = bot_name.concat(":").concat(bot_url);

        try (FileOutputStream fileOutputStream = requireContext().openFileOutput(BOT_FILE, Context.MODE_PRIVATE)) {
            fileOutputStream.write(info.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBotInfo() {
        try (FileInputStream fileInputStream = requireContext().openFileInput(BOT_FILE)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            br.close();

            String botName = sb.toString().substring(0, sb.toString().indexOf(":"));
            String botURL = sb.toString().substring(sb.toString().indexOf(":") + 1);
            b.lblName.setText(botName);
            b.txtName.setText(botName);
            b.txtBotUrl.setText(botURL);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setToolbarTitle(String title) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar()).setTitle(title);
    }

    private void observeData() {
        viewModel.getBotValues().observe(getViewLifecycleOwner(), strings -> {
            listAdapter.submitList(strings);
            b.lblBotAnswers.setText(getResources().getString(R.string.count_answers,
                    String.valueOf(strings.size())));
        });
    }

    private void addBotMessage() {
        b.profileFabSaveAnswer.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(b.txtBotAnswer.getText().toString())) {
                viewModel.addBotMessage(new MessageChat(
                        0,
                        b.txtBotAnswer.getText().toString(),
                        0,
                        false,
                        MyTimeUtils.getCurrentTime()
                ));
                b.txtBotAnswer.getText().clear();
                Toast.makeText(requireContext(),
                        "BOT message added",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(),
                        "Write something ompare",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
