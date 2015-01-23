package com.gurpreetsingh.rainy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Gurpreet on 2015-01-19.
 */
public class DialogFragment extends android.app.DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.error_title))
                .setMessage(context.getString(R.string.error_message))
                .setPositiveButton(context.getString(R.string.error_ok_button_text), null); //Null onClickListener will close the dialog

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
