package com.example.ricardopedrosarecupandroid.ui.fragment_main;

import android.content.DialogInterface;
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
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ricardopedrosarecupandroid.R;
import com.example.ricardopedrosarecupandroid.base.YesNoDialogFragment;
import com.example.ricardopedrosarecupandroid.data.local.AppDatabase;
import com.example.ricardopedrosarecupandroid.data.local.entity.LiveChat;
import com.example.ricardopedrosarecupandroid.data.local.entity.MessageChat;
import com.example.ricardopedrosarecupandroid.databinding.FragmentMainBinding;
import com.example.ricardopedrosarecupandroid.ui.main.MainActivityViewModel;
import com.example.ricardopedrosarecupandroid.ui.main.MainActivityViewModelFactory;
import com.example.ricardopedrosarecupandroid.utils.MyTimeUtils;

import java.util.List;
import java.util.Random;

public class MainFragment extends Fragment {

    private FragmentMainBinding b;
    private NavController navController;
    private MainActivityViewModel viewModel;
    private MainFragmentViewAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(this, new MainActivityViewModelFactory(
                requireActivity().getApplication(), AppDatabase.getInstance(requireContext())
        )).get(MainActivityViewModel.class);
        if (savedInstanceState == null) {
            viewModel.addMessageToLiveChat(new LiveChat(
                    0,
                    "si pos mira resulta que el otro dia estaba fumando un cigarrito con el jackono y el puto se empezo a cagar a muelte tumentiendeloketdigo",
                    0,
                    false,
                    MyTimeUtils.getCurrentTime()
            ));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        setupRecyclerView();
        observePreferences();
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
                NavigationUI.onNavDestinationSelected(item, navController);
                return true;
            case R.id.profileFragment:
                NavigationUI.onNavDestinationSelected(item, navController);
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
        listAdapter = new MainFragmentViewAdapter((liveChat, position) -> {
            viewModel.updateFavoriteState(liveChat.getId());
            viewModel.noTriggerLiveChatCount();
            listAdapter.notifyItemChanged(position);
        });
        b.listChatMessages.setHasFixedSize(true);
        b.listChatMessages.setItemAnimator(new DefaultItemAnimator());
        b.listChatMessages.setLayoutManager(linearLayoutManager);
        b.listChatMessages.setAdapter(listAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        //final int fromPos = viewHolder.getAdapterPosition();
                        //final int toPos = target.getAdapterPosition();
                        //mAdapter.swapItems(fromPos, toPos);
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        viewModel.deleteMessageLiveChat(listAdapter.getItem(viewHolder.getAdapterPosition()));
                        viewModel.noTriggerLiveChatCount();
                        listAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                });
        itemTouchHelper.attachToRecyclerView(b.listChatMessages);
    }

    private void observeLiveDataChat() {
        viewModel.getLiveChat().observe(getViewLifecycleOwner(),
                liveChats -> listAdapter.submitList(liveChats));
        viewModel.liveChatCount.observe(getViewLifecycleOwner(),
                integer -> b.listChatMessages.smoothScrollToPosition(integer));
    }

    private void talkingWithBots(String savePreference) {
        viewModel.getBotMessages().observe(getViewLifecycleOwner(), messageChats ->
                b.mainFragmentFab.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(b.txtChat.getText().toString())) {
                        if (savePreference.equals("Off")) {
                            setupConversation(messageChats);
                            viewModel.updateRecyclerPosition();
                        } else {
                            YesNoDialogFragment yndialog = YesNoDialogFragment.newInstance(
                                    "Send message",
                                    b.txtChat.getText().toString(),
                                    "Send",
                                    "Cancel");
                            yndialog.setListener(new YesNoDialogFragment.Listener() {
                                @Override
                                public void onPositiveButtonClick(DialogInterface dialog) {
                                    setupConversation(messageChats);
                                    viewModel.updateRecyclerPosition();
                                }

                                @Override
                                public void onNegativeButtonClick(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });
                            yndialog.show(requireFragmentManager(), "Confirm Dialog");
                        }
                    }

                }));
    }

    private void setupConversation(List<MessageChat> messageChats) {
        Random random = new Random();
        int pos = random.nextInt(messageChats.size() - 1) + 1;
        b.listChatMessages.postDelayed(MainFragment.this::sendUserMessage, 1000);
        b.listChatMessages.postDelayed(() -> MainFragment.this.sendBotMessage(
                messageChats.get(pos)),
                2000);
    }

    private void observePreferences() {
        viewModel.getFavIconPositionPreference().observe(getViewLifecycleOwner(), s -> listAdapter.setFavPreference(s));
        viewModel.getSaveMessagePreference().observe(getViewLifecycleOwner(), this::talkingWithBots);
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

    private void sendUserMessage() {
        viewModel.addMessageToLiveChat(new LiveChat(
                0,
                b.txtChat.getText().toString(),
                1,
                false,
                MyTimeUtils.getCurrentTime()
        ));
        b.txtChat.getText().clear();
    }
}
