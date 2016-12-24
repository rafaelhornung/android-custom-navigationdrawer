package com.rfhornung.customnavigationdrawer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LabelDialogFragment extends AppCompatDialogFragment
{
	public static final String DIALOG_TAG = "LabelDialogFragment";

	private String currentText;

	public interface LabelDialogListener
	{
		void onLabelDialogDone(String text);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (!(getActivity() instanceof LabelDialogListener))
		{
			throw new ClassCastException(getActivity().toString() + " must implement LabelDialogListener");
		}

		if (savedInstanceState != null)
			currentText = savedInstanceState.getString("currentText");
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		if (currentText != null)
			outState.putString("currentText", currentText);

		super.onSaveInstanceState(outState);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		View view = View.inflate(getContext(), R.layout.dialog_edittext, null);
		final EditText editText = (EditText) view.findViewById(R.id.ed_name);

		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setNegativeButton(R.string.txt_cancel, null);
		builder.setTitle(R.string.nav_create_label);
		builder.setCancelable(false);
		builder.setView(view);

		builder.setPositiveButton(R.string.txt_add, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				//What ever you want to do with the value
				String text = editText.getText().toString().trim();
				if (text.length() == 0)
					return;

				((LabelDialogListener) getActivity()).onLabelDialogDone(text);
			}
		});

		final AlertDialog alertDialog = builder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
		{
			@Override
			public void onShow(DialogInterface dialog)
			{
				if (currentText != null)
				{
					editText.setText(currentText);
					editText.setSelection(currentText.length());
				}

				final Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				if (button != null)
					button.setEnabled(false);

				editText.addTextChangedListener(new TextWatcher()
				{
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after)
					{
					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count)
					{
						if (button != null)
						{
							int length = (s.toString().trim()).length();
							button.setEnabled(length > 0);
						}

						// keep current text
						currentText = s.toString();
					}

					@Override
					public void afterTextChanged(Editable s)
					{
					}
				});
			}
		});

		return alertDialog;
	}
}
