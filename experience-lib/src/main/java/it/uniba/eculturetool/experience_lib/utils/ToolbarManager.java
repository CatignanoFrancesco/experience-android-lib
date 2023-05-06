package it.uniba.eculturetool.experience_lib.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import it.uniba.eculturetool.experience_lib.R;

public class ToolbarManager {
    private ToolbarManager() {}

    public static void setIcons(Toolbar toolbar, AppCompatActivity activity, MenuHost menuHost, Fragment fragment, int helpMessageResource) {
        activity.setSupportActionBar(toolbar);
        menuHost.addMenuProvider(getMenuProvider(activity, helpMessageResource), fragment.getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
    }

    public static MenuProvider getMenuProvider(Context context, int helpMessageResource) {
        return new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.help_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.help_item) {
                    HelpDialog.show(context, helpMessageResource);
                }
                return true;
            }
        };
    }
}
