package com.example.rsrtcs.ui.activity.billdesk;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.billdesk.sdk.LibraryPaymentStatusProtocol;

public class BilldeskCallBack implements LibraryPaymentStatusProtocol, Parcelable {

	public BilldeskCallBack() {
	}

	public BilldeskCallBack(Parcel in) {
	}

	@Override
	public void paymentStatus(String status, Activity context) {
		Intent intent = new Intent(context, PaymentStatusActivity.class);
		intent.putExtra("status", status);
		context.startActivity(intent);
	}

	@Override
	public void tryAgain() {

	}

	@Override
	public void onError(Exception e) {
//		Log.e("exception ",""+e.getMessage());
	}

	@Override
	public void cancelTransaction() {

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@SuppressWarnings("rawtypes")
	public static final Creator CREATOR = new Creator() {
		@Override
		public BilldeskCallBack createFromParcel(Parcel in) {
			return new BilldeskCallBack(in);
		}

		@Override
		public Object[] newArray(int size) {
			return new BilldeskCallBack[size];
		}
	};
}
