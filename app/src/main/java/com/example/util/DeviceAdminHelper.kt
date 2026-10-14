package com.example.util

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.R
import com.example.receiver.StudyGuardAdminReceiver

/**
 * Utility helper to query Device Admin status, launch activation request,
 * and lock the screen immediately when the user chooses studying.
 */
object DeviceAdminHelper {

    private const val TAG = "DeviceAdminHelper"

    fun getAdminComponent(context: Context): ComponentName {
        return ComponentName(context, StudyGuardAdminReceiver::class.java)
    }

    /**
     * Checks if StudyGuard has active device administration privileges to lock the screen.
     */
    fun isDeviceAdminActive(context: Context): Boolean {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as? DevicePolicyManager
            ?: return false
        return dpm.isAdminActive(getAdminComponent(context))
    }

    /**
     * Opens the system Device Administration activation screen.
     */
    fun requestDeviceAdmin(context: Context) {
        try {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, getAdminComponent(context))
                putExtra(
                    DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    context.getString(R.string.device_admin_explanation)
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to launch device admin settings", e)
        }
    }

    /**
     * Immediately locks the phone screen and turns off the display using DevicePolicyManager.
     * Returns true if successfully locked, false if device admin is not yet active.
     */
    fun lockDevice(context: Context): Boolean {
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as? DevicePolicyManager
            ?: return false
        val adminComponent = getAdminComponent(context)
        if (dpm.isAdminActive(adminComponent)) {
            return try {
                dpm.lockNow()
                Log.d(TAG, "Screen successfully locked via DevicePolicyManager.lockNow()")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Error calling dpm.lockNow()", e)
                false
            }
        }
        Log.w(TAG, "Device Admin is not active; cannot lock screen yet.")
        return false
    }
}
