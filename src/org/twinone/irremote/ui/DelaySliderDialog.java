package org.twinone.irremote.ui;

import org.twinone.irremote.R;
import org.twinone.irremote.components.AnimHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DelaySliderDialog extends DialogPreference implements
		DialogInterface.OnClickListener {

	public DelaySliderDialog(Context context) {
		this(context, null);
	}

	public DelaySliderDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPersistent(true);
		mDefaultValue = context.getResources().getInteger(
				R.integer.pref_def_delay);
		mMax = context.getResources().getInteger(R.integer.pref_def_delay_max);
		mMin = context.getResources().getInteger(R.integer.pref_def_delay_min);
	}

	private TextView mText;
	private SeekBar mSlider;
	private CheckBox mDefault;
	private int mDefaultValue;
	private int mMax;
	private int mMin;

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return mDefaultValue;
	}

	@Override
	protected View onCreateDialogView() {
		View v = LayoutInflater.from(getContext()).inflate(
				R.layout.slider_dialog, null);

		mText = (TextView) v.findViewById(R.id.delay_text);
		mSlider = (SeekBar) v.findViewById(R.id.delay_slider);

		mDefault = (CheckBox) v.findViewById(R.id.delay_cb_default);
		mDefault.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					setProgress(mDefaultValue);
				}
				mSlider.setEnabled(!isChecked);
			}
		});

		mSlider.setMax(mMax - mMin);
		setProgress(getPersistedInt(mDefaultValue));

		mSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onProgressChanged(SeekBar bar, int prog, boolean arg2) {
				mText.setText(getContext().getString(R.string.ms, prog + mMin));
			}
		});
		return v;
	}

	@Override
	public Dialog getDialog() {

		AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
		ab.setTitle(R.string.color_dlgtit);
		ab.setNegativeButton(android.R.string.cancel, null);
		ab.setPositiveButton(android.R.string.ok, null);
		return AnimHelper.addAnimations(ab.create());
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if (positiveResult && mSlider != null) {
			persistInt(getProgress());
		}
	}

	private int getProgress() {
		return mSlider.getProgress() + mMin;
	}

	/**
	 * @param progress
	 *            The progress as the user would see it
	 */
	private void setProgress(int progress) {
		mSlider.setProgress(progress - mMin);
		mText.setText(getContext().getString(R.string.ms, progress));
	}

}