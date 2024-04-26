package sp.openconnect.remote;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import sp.openconnect.VpnProfile;
import sp.openconnect.api.GrantPermissionsActivity;
import sp.openconnect.core.OpenConnectManagementThread;
import sp.openconnect.core.OpenVpnService;
import sp.openconnect.core.VPNConnector;
import sp.openconnect.fragments.FeedbackFragment;
import sp.openconnect.remote.data.Static;

/**
 * by MehrabSp
 * Java 11
 */
public class ProfileManager {
    public VpnProfile vpnProfile;
    private final Context context = Static.getGlobalData().getMainApplication();
    private final VPNConnector mConn;
    private OpenVpnService openVpnService;

    /**
     * first set context on ./data/Static
     * for start vpn first CreateProfileWithName
     */
    public ProfileManager(){
        mConn = new VPNConnector(context, true) { // run on activity (require)
            @Override
            public void onUpdate(OpenVpnService service) {
                openVpnService = service;
            }
        };
    }

    /**
     * Status!
     * @return Example: getUpdateUI().getConnectionState() == OpenConnectManagementThread
     * .STATE_CONNECTED: means Connected!
     */
    public OpenVpnService getUpdateUI(){
        return this.openVpnService;
    }

    /**
     * put on -->
     * Fragment: onDestroyView
     * Activity: onStop or ..
     */
    public void unBindUpdateUI(){
        this.mConn.unbind();
    }

    /**
     * You don't need this
     * @return Get current service
     */
    public VPNConnector getVpnConnector(){
        return this.mConn;
    }

    public boolean CreateProfileWithName(@NonNull String hostName){
        assert context != null;
        try{
            Log.d("OpenConnect", "CREATE profile from remote: " + hostName);

            hostName = hostName.replaceAll("\\s", "");
            if (!hostName.isEmpty()) {
                FeedbackFragment.recordProfileAdd(context);
                vpnProfile = sp.openconnect.core.ProfileManager.create(hostName);
            }

        }catch (Exception e){
            return false;
        }
        return true;
    }

    public void StartVPNWithProfile(){
        assert vpnProfile != null;

        Intent intent = new Intent(context, GrantPermissionsActivity.class);
        String pkg = context.getPackageName();

        intent.putExtra(pkg + GrantPermissionsActivity.EXTRA_UUID,
                vpnProfile.getUUID().toString());
        intent.setAction(Intent.ACTION_MAIN);
        context.startActivity(intent);
    }

    public void stopForceVPN(){
        if (mConn.service.getConnectionState() ==
                OpenConnectManagementThread.STATE_DISCONNECTED) {
            mConn.service.startReconnectActivity(context);
        } else {
            mConn.service.stopVPN();
        }
    }

    public void StopOrReconnect(){
        if (mConn.service.getConnectionState() ==
                OpenConnectManagementThread.STATE_DISCONNECTED) {
            mConn.service.startReconnectActivity(context);
        } else {
            mConn.service.stopVPN();
        }
    }

}
