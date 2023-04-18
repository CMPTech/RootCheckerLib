package com.example.rootcheckerlib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RootDetection {

    static final String[] knownRootAppsPackages = {
            "com.noshufou.android.su",
            "com.noshufou.android.su.elite",
            "eu.chainfire.supersu",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.yellowes.su",
            "com.topjohnwu.magisk",
            "com.kingroot.kinguser",
            "com.kingo.root",
            "com.smedialink.oneclickroot",
            "com.zhiqupk.root.global",
            "com.alephzain.framaroot",
            "com.koushikdutta.rommanager",
            "com.koushikdutta.rommanager.license",
            "com.dimonvideo.luckypatcher",
            "com.chelpus.lackypatch",
            "com.ramdroid.appquarantine",
            "com.ramdroid.appquarantinepro",
            "com.android.vending.billing.InAppBillingService.COIN",
            "com.android.vending.billing.InAppBillingService.LUCK",
            "com.chelpus.luckypatcher",
            "com.blackmartalpha",
            "org.blackmart.market",
            "com.allinone.free",
            "com.repodroid.app",
            "org.creeplays.hack",
            "com.baseappfull.fwd",
            "com.zmapp",
            "com.dv.marketmod.installer",
            "org.mobilism.android",
            "com.android.wp.net.log",
            "com.android.camera.update",
            "cc.madkite.freedom",
            "com.charles.lpoqasert",
            "catch_.me_.if_.you_.can_"
    };

    static final String[] knownSUPaths = {
            "/system/bin/su",
            "/system/xbin/su",
            "/sbin/su",
            "/system/su",
            "/system/bin/.ext/.su",
            "/system/usr/we-need-root/su-backup",
            "/system/xbin/mu"
    };

    public static boolean checkRootFilesAndPackages(Context context) {
        boolean result = false;
        for(String string1: knownRootAppsPackages) {
            if(isPackageInstalled(string1, context)) {
                result = true;
                break;
            }
        }
        return result;
    }
    private static boolean isPackageInstalled(String packagename, Context context){
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean checkSUPaths() {
        boolean result = false;
        for(String string1: knownSUPaths) {
            File f = new File(string1);
            boolean fileExists = f.exists();
            if (fileExists) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean checkCustomOS(){
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        return false;
    }

    public static boolean checkOTACerts() {
        String OTAPath = "/etc/security/otacerts.zip";
        File f = new File(OTAPath);boolean fileExists = f.exists();
        if (fileExists) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkForDangerousProps() {
        final Map<String, String> dangerousProps = new HashMap<>();
        dangerousProps.put("ro.debuggable", "1");
        dangerousProps.put("ro.secure", "0");
        boolean result = false;
        String[] lines = propsReader();
        if (lines == null){
            return false;
        }
        for (String line : lines) {
            for (String key : dangerousProps.keySet()) {
                if (line.contains(key)) {
                    String badValue = dangerousProps.get(key);
                    badValue = "[" + badValue + "]";
                    if (line.contains(badValue)) {
                        Log.d("",key + " = " + badValue + " detected!");
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private String[] propsReader() {
        try {
            InputStream inputstream =
                    Runtime.getRuntime().exec("getprop").getInputStream();
            if (inputstream == null) return null;
            String propVal = new Scanner(inputstream).useDelimiter("\\A").next();
            return propVal.split("\n");
        } catch (IOException | NoSuchElementException e) {
            Log.e("TAG", "propsReader: ",e );
            return null;
        }
    }
}

