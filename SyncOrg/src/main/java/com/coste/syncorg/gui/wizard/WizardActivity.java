package com.coste.syncorg.gui.wizard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.coste.syncorg.OrgNodeListActivity;
import com.coste.syncorg.gui.wizard.wizards.DropboxWizard;
import com.coste.syncorg.gui.wizard.wizards.SDCardWizard;
import com.coste.syncorg.gui.wizard.wizards.SSHWizard;
import com.coste.syncorg.gui.wizard.wizards.UbuntuOneWizard;
import com.coste.syncorg.gui.wizard.wizards.WebDAVWizard;
import com.coste.syncorg.R;
import com.coste.syncorg.orgdata.SyncOrgApplication;

public class WizardActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wizard_choose_synchronizer);

		final RadioGroup syncGroup = (RadioGroup) findViewById(R.id.sync_group);

		SharedPreferences srcPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String syncSource = srcPrefs.getString("syncSource", "nullSync");
		Log.v("sync", "source : "+ syncSource);
		int id = getResources().getIdentifier(syncSource, "id", getPackageName() );
		RadioButton radioButton = (RadioButton) findViewById(id);
		if(radioButton != null) radioButton.setChecked(true);

		findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int id = syncGroup.getCheckedRadioButtonId();
				if(id < 0) return;

				// Saving selected synchronizer
				SharedPreferences appSettings = PreferenceManager
						.getDefaultSharedPreferences(WizardActivity.this);
				SharedPreferences.Editor editor = appSettings.edit();
				String syncName = getResources().getResourceEntryName(id);
				editor.putString("syncSource", syncName);
				editor.apply();

				final int request_code = -1;
				switch (syncName) {
					case "webdav":
						startActivity(new Intent(WizardActivity.this, WebDAVWizard.class));
						break;
					case "dropbox":
						startActivity(new Intent(WizardActivity.this, DropboxWizard.class));
						break;
					case "ubuntuone":
						startActivity(new Intent(WizardActivity.this, UbuntuOneWizard.class));
						break;
					case "sdcard":
						startActivity(new Intent(WizardActivity.this, SDCardWizard.class));
						break;
					case "scp":
						startActivity(new Intent(WizardActivity.this, SSHWizard.class));
						break;
					default:
						((SyncOrgApplication) getApplication()).startSynchronizer();
						startActivity(new Intent(WizardActivity.this, OrgNodeListActivity.class));
						break;
				}
			}
		});
	}
}

