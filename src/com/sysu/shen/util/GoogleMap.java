package com.sysu.shen.util;

import com.sysu.shen.youtour.R;

import android.app.Activity;
import android.os.Bundle;

public class GoogleMap extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.googlemap);
	}

	// private void setUpMapIfNeeded() {
	// // Do a null check to confirm that we have not already instantiated the
	// // map.
	// if (mMap == null) {
	// // Try to obtain the map from the SupportMapFragment.
	// mMap = ((SupportMapFragment) getSupportFragmentManager()
	// .findFragmentById(R.id.basicMap)).getMap();
	//
	// if (isGoogleMapsInstalled()) {
	// if (mMap != null) {
	// mMap.setLocationSource(this);
	//
	// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
	// new LatLng(44.9800, -93.2636), 10.0f));
	// setUpMap();
	// }
	// } else {
	// Builder builder = new AlertDialog.Builder(this);
	// builder.setMessage(getString(R.string.installGoogleMaps));
	// builder.setCancelable(false);
	// builder.setPositiveButton(getString(R.string.install),
	// getGoogleMapsListener());
	// AlertDialog dialog = builder.create();
	// dialog.show();
	// }
	// }
	// }
	//
	// public boolean isGoogleMapsInstalled() {
	// try {
	// ApplicationInfo info = getPackageManager().getApplicationInfo(
	// "com.google.android.apps.maps", 0);
	// return true;
	// } catch (PackageManager.NameNotFoundException e) {
	// return false;
	// }
	// }
	//
	// public OnClickListener getGoogleMapsListener() {
	// return new OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// Intent intent = new Intent(
	// Intent.ACTION_VIEW,
	// Uri.parse("market://details?id=com.google.android.apps.maps"));
	// startActivity(intent);
	//
	// // Finish the activity so they can't circumvent the check
	// finish();
	// }
	// };
	// }
}
