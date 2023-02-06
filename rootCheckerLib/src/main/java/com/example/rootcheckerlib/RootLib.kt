package com.example.rootcheckerlib

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class RootLib : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRooted();
        canExecuteCommand("su");
        findBinary("su");
    }

    private fun isRooted(): Boolean {
        // get from build info
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true
        }

        // check if /system/app/Superuser.apk is present
        try {
            val file = File("/system/app/Superuser.apk")
//            Toast.makeText(this, "vlaue is ${file.exists()}`", Toast.LENGTH_LONG).show()
            if (file.exists()) {
//                Toast.makeText(this,"This device is a rooted device",Toast.LENGTH_LONG).show();
                return true
            }
            else{
//                Toast.makeText(this,"This device is not a rooted device",Toast.LENGTH_LONG).show();
            }
        } catch (e1: Exception) {
            // ignore
        }

        // try executing commands
        //return canExecuteCommand("/system/xbin/which su")|| canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
        if (!canExecuteCommand("su")) if (findBinary("su")) return true
        return false
    }

    private fun findBinary(binaryName: String): Boolean {
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
                    Toast.makeText(this,"This device is a rooted device", Toast.LENGTH_LONG).show();
                    break
                }
                else{
                    Toast.makeText(this,"This device is not a rooted device", Toast.LENGTH_LONG).show();
                }
            }
        }
        return found
    }

    // executes a command on the system
    private fun canExecuteCommand(command: String): Boolean {
        val executedSuccesfully: Boolean = try {
            Runtime.getRuntime().exec(command)
            true
        } catch (e: Exception) {
            false
        }
        return executedSuccesfully
    }
}