package com.example.rootcheckerlib

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.SignalThresholdInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

object RootLib {

    fun isRooted(): Boolean {

        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true
        }

        // check if /system/app/Superuser.apk is present
        try {
            val file = File("/system/app/Superuser.apk")
            if (file.exists()) {
                return true
            }
            else{
//                Toast.makeText(this,"This device is not a rooted device",Toast.LENGTH_LONG).show();
            }
        } catch (e1: Exception) {
            // ignore
            return false
        }
        //return canExecuteCommand("/system/xbin/which su")|| canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
//        if (!canExecuteCommand("su")) if (findBinary("su")) return true
        return false
    }

     fun findBinary(binaryName: String): Boolean {
        var found = false
        if (!found) {
            val places = arrayOf(
                "/sbin/", "/system/bin/", "/system/xbin/",
                "/data/local/xbin/", "/data/local/bin/",
                "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"
            )
            for (where in places) {
                if (File(where + binaryName).exists()) {
                    found = true
                    break
                }
                else{
                    found = false
                }
            }
        }
        return found
    }

    // executes a command on the system
     fun canExecuteCommand(command: String): Boolean {
        val executedSuccesfully: Boolean = try {
            Runtime.getRuntime().exec(command)
            true
        } catch (e: Exception) {
            false
        }
        return executedSuccesfully
    }
}