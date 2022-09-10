package com.example.fitforfit.fragments;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.fitforfit.MainActivity;
import com.example.fitforfit.R;
import com.example.fitforfit.singleton.Database;
import com.example.fitforfit.sync.DatabaseSync;
import com.example.fitforfit.ui.main.AboutUsActivity;
import com.example.fitforfit.ui.main.LicenseActivity;
import com.example.fitforfit.ui.main.SettingsActivity;
import com.example.fitforfit.ui.main.WorkoutStatsActivity;

import java.io.File;

import de.raphaelebner.roomdatabasebackup.core.RoomBackup;

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

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(view -> requireActivity().onBackPressed());

        FragmentActivity fragmentActivity = getActivity();
        RoomBackup roomBackup = null;

        if (fragmentActivity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) fragmentActivity;
            roomBackup = mainActivity.roomBackup;
        }

        if (showOptionsMenu) {
            toolbar.inflateMenu(R.menu.main_options_menu);
            RoomBackup finalRoomBackup = roomBackup;
            toolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.settings:
                        Intent intent = new Intent(requireActivity(), SettingsActivity.class);
                        requireActivity().startActivity(intent);
                        return true;
                    case R.id.license:
                        Intent intent1 = new Intent(requireActivity(), LicenseActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.aboutUs:
                        Intent intent2 = new Intent(requireActivity(), AboutUsActivity.class);
                        startActivity(intent2);
                        return true;
                    case R.id.downloadDb:
                        if (finalRoomBackup != null) {
                            File restoreFile = DatabaseSync.downloadDB(requireContext());

                            BroadcastReceiver onComplete = new BroadcastReceiver() {
                                public void onReceive(Context context, Intent intent) {

                                    if (restoreFile.exists() && restoreFile.isFile() && restoreFile.canRead()) {
                                        finalRoomBackup.backupLocationCustomFile(restoreFile);
                                        finalRoomBackup.customBackupFileName(Database.DB_NAME + ".sqlite3");
                                        finalRoomBackup.onCompleteListener((success, message, exitCode) -> {
                                            if (success) {
                                                Log.d("abc", "message: " + message);
                                                restoreFile.delete();
                                                Toast.makeText(context, "Wiederherstellen erfolgreich", Toast.LENGTH_SHORT).show();
                                                finalRoomBackup.restartApp(new Intent(requireContext(), MainActivity.class));
                                            }
                                        });
                                        finalRoomBackup.restore();
                                    } else {
                                        Log.d("abc", "Datei gibts nicht oder ist nicht lesbar lool");
                                    }
                                }
                            };
                            requireContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        }
                        return true;
                    case R.id.uploadDb:
                        if (finalRoomBackup != null) {
                            finalRoomBackup.backupLocationCustomFile(new File(Database.BACKUP_PATH));
                            finalRoomBackup.customBackupFileName(Database.DB_NAME + ".sqlite3");
                            finalRoomBackup.onCompleteListener((success, message, exitCode) -> {
                                if (success) {
                                    Log.d("abc", "message: " + message);
                                    AsyncTask.execute(() -> {
                                        if (DatabaseSync.uploadDB(requireContext())) {
                                            requireActivity().runOnUiThread(() -> {
                                                Toast.makeText(requireContext(), "Speichern erfolgreich", Toast.LENGTH_SHORT).show();
                                            });
                                        }
                                    });
                                }
                            });
                            finalRoomBackup.backup();
                        }

                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            });
        } else {
            setHasOptionsMenu(false);
        }
    }

    @SuppressLint("NonConstantResourceId")
    protected void initWorkoutDetailToolbar(int workoutId) {
        toolbar = requireView().findViewById(R.id.mainToolbar);
        String title = getString(R.string.app_name) + " - " + getString(R.string.workout_name);
        toolbar.setTitle(title);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(view -> requireActivity().onBackPressed());

        toolbar.inflateMenu(R.menu.workout_detail_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.settings:
                    Intent intent = new Intent(requireActivity(), SettingsActivity.class);
                    requireActivity().startActivity(intent);
                    return true;
                case R.id.license:
                    Intent intent1 = new Intent(requireActivity(), LicenseActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.aboutUs:
                    Intent intent2 = new Intent(requireActivity(), AboutUsActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.stats:
                    Intent intent3 = new Intent(requireActivity(), WorkoutStatsActivity.class);
                    intent3.putExtra("workoutId", workoutId);
                    startActivity(intent3);
                default:
                    return super.onOptionsItemSelected(item);
            }
        });
    }

    protected void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    protected void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }
}
