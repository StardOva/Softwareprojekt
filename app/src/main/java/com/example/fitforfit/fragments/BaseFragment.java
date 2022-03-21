package com.example.fitforfit.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.fitforfit.R;
import com.example.fitforfit.ui.main.AboutUsActivity;
import com.example.fitforfit.ui.main.SettingsActivity;

public class BaseFragment extends Fragment {

    protected Toolbar toolbar;

    public BaseFragment(int fragment_tracker) {
        super(fragment_tracker);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void initToolbar() {
        initToolbar("", true);
    }

    protected void initToolbar(String addToAppName) {
        initToolbar(addToAppName, true);
    }

    protected void initToolbar(boolean showOptionsMenu) {
        initToolbar("", showOptionsMenu);
    }

    @SuppressLint("NonConstantResourceId")
    protected void initToolbar(String addToAppName, boolean showOptionsMenu) {
        toolbar = requireView().findViewById(R.id.mainToolbar);

        String title = getString(R.string.app_name);
        if (!addToAppName.equals("")) {
            title += " - " + addToAppName;
        }
        toolbar.setTitle(title);

        if (showOptionsMenu) {
            toolbar.inflateMenu(R.menu.main_options_menu);
            toolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.settings:
                        Intent intent = new Intent(requireActivity(), SettingsActivity.class);
                        requireActivity().startActivity(intent);
                        return true;
                    case R.id.aboutUs:
                        Intent intent1 = new Intent(requireActivity(), AboutUsActivity.class);
                        startActivity(intent1);
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            });
        } else {
            setHasOptionsMenu(false);
        }
    }

    protected void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    protected void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }
}