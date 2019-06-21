package com.example.ricardopedrosarecupandroid.ui.fragment_main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ricardopedrosarecupandroid.R;
import com.example.ricardopedrosarecupandroid.data.local.AppDatabase;
import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;
import com.example.ricardopedrosarecupandroid.data.local.entity.MessageChat;
import com.example.ricardopedrosarecupandroid.databinding.FragmentMainBinding;
import com.example.ricardopedrosarecupandroid.ui.main.MainActivityViewModel;
import com.example.ricardopedrosarecupandroid.ui.main.MainActivityViewModelFactory;
import com.example.ricardopedrosarecupandroid.utils.MyTimeUtils;

public class MainFragment extends Fragment {

    private FragmentMainBinding b;
    private NavController navController;
    private MainActivityViewModel viewModel;
    private MainFragmentViewAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        viewModel = ViewModelProviders.of(this, new MainActivityViewModelFactory(
                requireActivity().getApplication(), AppDatabase.getInstance(requireContext())
        )).get(MainActivityViewModel.class);
        if (savedInstanceState == null) {
            viewModel.addMessageToLiveChat(new LiveChat(
                    0,
                    "Hello there!",
                    0,
                    false,
                    MyTimeUtils.getCurrentTime()
            ));
        }
        setupRecyclerView();
        chatWithTheBot();
        observeLiveDataChat();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuCleanChat:
                viewModel.clearLiveChat();
                return true;
            case R.id.settingsFragment:
                navController.navigate(R.id.settingsFragment);
                return true;
            case R.id.profileFragment:
                navController.navigate(R.id.profileFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false);
        linearLayoutManager.setStackFromEnd(true);
        listAdapter = new MainFragmentViewAdapter(viewModel);
        b.listChatMessages.setHasFixedSize(false);
        b.listChatMessages.setItemAnimator(new DefaultItemAnimator());
        b.listChatMessages.setLayoutManager(linearLayoutManager);
        b.listChatMessages.setAdapter(listAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT) {

                    // Cuando se detecta un gesto drag & drop.
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        //final int fromPos = viewHolder.getAdapterPosition();
                        //final int toPos = target.getAdapterPosition();
                        //mAdapter.swapItems(fromPos, toPos);
                        return true;
                    }

                    // Cuando se detecta un gesto swipe to dismiss.
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        // mAdapter.removeItem(viewHolder.getAdapterPosition());
                        viewModel.deleteMessageLiveChat(listAdapter.getItem(viewHolder.getAdapterPosition()));
                        viewModel.triggerDelete();
                    }
                });
        itemTouchHelper.attachToRecyclerView(b.listChatMessages);
    }

    private void observeLiveDataChat() {
        viewModel.getLiveChat().observe(this, liveChats -> listAdapter.submitList(liveChats));

        viewModel.theFuckingBot().observe(this, messageChat -> {
            if (messageChat != null) {
                b.listChatMessages.postDelayed(this::sendMessage, 1000);
                b.listChatMessages.postDelayed(() -> sendBotMessage(messageChat), 2000);
            }
        });

        viewModel.scrollPositionPointer().observe(this,
                integer -> {
                    if (integer != null) {
                        b.listChatMessages.smoothScrollToPosition(integer);
                    }
                });
    }

    private void chatWithTheBot() {
        b.mainFragmentFab.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(b.txtChat.getText().toString())) {
                viewModel.triggerMessage();
            }
        });
    }

    private void sendMessage() {
        viewModel.setUserMessage(b.txtChat.getText().toString());
        viewModel.addMessageToLiveChat(new LiveChat(
                0,
                viewModel.getUserMessage(),
                1,
                false,
                MyTimeUtils.getCurrentTime()
        ));
        b.txtChat.setText("");
    }

    private void sendBotMessage(MessageChat messageChat) {
        viewModel.addMessageToLiveChat(new LiveChat(
                0,
                messageChat.getValue(),
                0,
                false,
                MyTimeUtils.getCurrentTime()
        ));
    }
}
