package com.example.androidgameapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * This will display the Dialog upon winning the game
 */
public class CongratulationsDialog extends AppCompatDialogFragment {
    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ImageView image = new ImageView(getActivity());
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trophypng);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 300, 300, true);
        Resources resource = getResources();
        image.setImageDrawable(new BitmapDrawable(resource,scaledBitmap));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Congratulations")
                .setMessage("You've found all of the bombs!")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                })
                .setView(image);

        return builder.create();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
