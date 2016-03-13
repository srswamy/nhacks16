package com.nhacks.share;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Sagar on 3/12/2016.
 */
public class FilterDialog extends DialogFragment {

    public interface DialogCloseListener
    {
        public void handleDialogClose();
    }


    private EditText mCategotyEditText;
    private Button saveBtn;
    private Button cancelBtn;
    private DialogCloseListener mListener;

    public FilterDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_category, container);
        mCategotyEditText = (EditText) view.findViewById(R.id.categoryName);
        saveBtn = (Button) view.findViewById(R.id.save_btn);
        cancelBtn = (Button) view.findViewById(R.id.cancel_btn);

        // set this instance as callback for editor action
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Please enter exercise name");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = mCategotyEditText.getText().toString();
                if (category.trim().equals("")) {
                    Toast toast = Toast.makeText(getContext(), "Please enter a category!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    dismiss();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // TODO Auto-generated method stub

        super.onDismiss(dialog);
        if(mListener!=null){
            mListener.handleDialogClose();
        }

    }

    public void DismissListener(DialogCloseListener closeListener){
        this.mListener = closeListener;
    }

}
