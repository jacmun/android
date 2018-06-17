package com.example.jackiemun1.shoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jackiemun1.shoppinglist.adapter.ItemsAdapter;
import com.example.jackiemun1.shoppinglist.data.Item;

public class CreateAndEditItemDialog extends DialogFragment {

    public interface ItemHandler {
        public void onNewItemCreated(Item item);

        public void onItemUpdated(Item item);
    }

    private ItemHandler itemHandler;
    private Spinner spinnerItemType;
    private EditText etName;
    private EditText etPrice;
    private EditText etDescription;
    private CheckBox cbStatus;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ItemHandler) {
            itemHandler = (ItemHandler)context;
        } else {
            throw new RuntimeException(getString(R.string.runtimeErrorMsg));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title);

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_create_item, null);

        spinnerItemType = rootView.findViewById(R.id.spinnerItemType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.itemtypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItemType.setAdapter(adapter);

        etName = rootView.findViewById(R.id.etName);
        etPrice = rootView.findViewById(R.id.etPrice);
        etDescription = rootView.findViewById(R.id.etDescription);
        cbStatus = rootView.findViewById(R.id.cbStatus);

        if (getArguments() != null &&
                getArguments().containsKey(MainActivity.KEY_EDIT)) {
            Item itemToEdit = (Item) getArguments().getSerializable(MainActivity.KEY_EDIT);
            etName.setText(itemToEdit.getItemName());
            etPrice.setText(Double.toString(itemToEdit.getItemPrice()));
            etDescription.setText(itemToEdit.getDescription());
            cbStatus.setChecked(itemToEdit.getItemStatus());
            spinnerItemType.setSelection(itemToEdit.getItemTypeAsEnum().getValue());
        }

        builder.setView(rootView);

        builder.setPositiveButton(R.string.addTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if (d != null) {
            Button positiveBtn = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(etName.getText())) {
                        etName.setError(getString(R.string.errorMsg));
                    }
                    if(TextUtils.isEmpty(etPrice.getText())) {
                        etPrice.setError(getString(R.string.errorMsg));
                    }
                    else if (!TextUtils.isEmpty(etName.getText()) && !TextUtils.isEmpty(etPrice.getText())) {
                        if (getArguments() != null &&
                                getArguments().containsKey(MainActivity.KEY_EDIT)) {
                            Item itemToEdit = (Item) getArguments().getSerializable(MainActivity.KEY_EDIT);
                            itemToEdit.setItemName(etName.getText().toString());
                            itemToEdit.setItemPrice(Double.parseDouble(etPrice.getText().toString()));
                            itemToEdit.setDescription(etDescription.getText().toString());
                            itemToEdit.setItemStatus(cbStatus.isChecked());
                            itemToEdit.setItemType(spinnerItemType.getSelectedItemPosition());

                            itemHandler.onItemUpdated(itemToEdit);
                        } else {
                            Item item = new Item(
                                    etName.getText().toString(),
                                    etDescription.getText().toString(),
                                    Double.parseDouble(etPrice.getText().toString()),
                                    cbStatus.isChecked(),
                                    spinnerItemType.getSelectedItemPosition()
                            );
                            itemHandler.onNewItemCreated(item);
                        }
                        d.dismiss();
                    }
                }
            });
        }
    }
}
