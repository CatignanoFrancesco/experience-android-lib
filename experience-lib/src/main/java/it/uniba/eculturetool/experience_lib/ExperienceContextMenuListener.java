package it.uniba.eculturetool.experience_lib;

import android.content.Context;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import it.uniba.eculturetool.experience_lib.listeners.OnDeleteListener;
import it.uniba.eculturetool.experience_lib.listeners.OnOpenListener;
import it.uniba.eculturetool.experience_lib.listeners.OnShareListener;

public class ExperienceContextMenuListener<T> implements View.OnCreateContextMenuListener {
    private int adapterPosition;
    private Context context;
    private OnOpenListener<T> onOpenListener;
    private OnDeleteListener onDeleteListener;
    private OnShareListener<T> onShareListener;
    private T item;

    public ExperienceContextMenuListener(Context context, int adapterPosition, T item, OnOpenListener<T> onOpenListener, OnDeleteListener onDeleteListener, OnShareListener<T> onShareListener) {
        this(context, adapterPosition, item, onOpenListener, onDeleteListener);
        this.onShareListener = onShareListener;
    }

    public ExperienceContextMenuListener(Context context, int adapterPosition, T item, OnOpenListener<T> onOpenListener, OnDeleteListener onDeleteListener) {
        this.context = context;
        this.adapterPosition = adapterPosition;
        this.onOpenListener = onOpenListener;
        this.onDeleteListener = onDeleteListener;
        this.item = item;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        MenuItem openItem = contextMenu.add(adapterPosition, 121, 0, R.string.open);
        MenuItem deleteItem = contextMenu.add(adapterPosition, 122, 0, R.string.delete_experience);
        MenuItem shareItem;

        if(onShareListener != null) {   // Inizializzo solo per le experience. Domande e risposte non hanno bisogno di questo pulsante.
            shareItem = contextMenu.add(adapterPosition, 123, 0, R.string.share_experience);

            shareItem.setOnMenuItemClickListener(item -> {
                onShareListener.onShare(this.item);
                return true;
            });
        }

        openItem.setOnMenuItemClickListener(item -> {
            onOpenListener.onOpen(this.item);
            return true;
        });

        deleteItem.setOnMenuItemClickListener(item -> {
            new AlertDialog.Builder(context)
                    .setMessage(R.string.confirm_delete)
                    .setPositiveButton(R.string.yes_delete, (dialogInterface, i) -> onDeleteListener.onDelete())
                    .setNegativeButton(R.string.no, ((dialogInterface, i) -> dialogInterface.dismiss()))
                    .show();
            return true;
        });
    }
}